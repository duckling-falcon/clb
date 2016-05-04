/*
 * Copyright (c) 2008-2016 Computer Network Information Center (CNIC), Chinese Academy of Sciences.
 * 
 * This file is part of Duckling project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *
 */
package cn.vlabs.clb.server.ui.frameservice.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import cn.vlabs.clb.api.CLBStatus;
import cn.vlabs.clb.api.image.ImageMeta;
import cn.vlabs.clb.api.image.ImageQuery;
import cn.vlabs.clb.api.image.ResizeImage;
import cn.vlabs.clb.api.image.ResizeParam;
import cn.vlabs.clb.server.config.AppFacade;
import cn.vlabs.clb.server.exception.FileContentNotFoundException;
import cn.vlabs.clb.server.exception.InvalidFileOperationException;
import cn.vlabs.clb.server.model.DocVersion;
import cn.vlabs.clb.server.model.ImageItem;
import cn.vlabs.clb.server.model.ResizeType;
import cn.vlabs.clb.server.service.image.IImageItemService;
import cn.vlabs.clb.server.storage.IStorageService;
import cn.vlabs.clb.server.storage.mongo.MFile;
import cn.vlabs.clb.server.ui.frameservice.DPair;
import cn.vlabs.clb.server.ui.frameservice.LocalFileCacheService;
import cn.vlabs.clb.server.ui.frameservice.OperationUtils;
import cn.vlabs.clb.server.ui.frameservice.SrcPair;
import cn.vlabs.clb.server.ui.frameservice.URLBuilder;

import com.mongodb.gridfs.GridFSDBFile;

@Component("ImageFacade")
public class ImageFacade {

    /*
     * 1.输入srcPath从clb中读取原始图片变成临时文件 没有找到时 读取失败时 2.调用压缩图片命令，将图片转换成为需要的格式
     * 需要读取原图的信息 输入参数的处理 转换失败时 3.将转换好的临时文件存入clb中 写入失败时 成功时删除文件 4.等待时间太长时 5.队列排重
     */

    private static final String JPEG = "jpeg";

    @Autowired
    private IImageItemService imageItemService;

    @Autowired
    private IStorageService mongoStorage;
    @Autowired
    private LocalFileCacheService localCacheService;

    private BlockingQueue<ResizeEvent> resizeEventQueue = new LinkedBlockingQueue<ResizeEvent>();
    private List<ResizeWorker> resizeWorkers = new ArrayList<ResizeWorker>();
    private Set<String> filterSet = new HashSet<String>();

    private static final Logger LOG = Logger.getLogger(ImageFacade.class);

    @PostConstruct
    public void init() {
        int num = Integer.parseInt(AppFacade.getConfig("clb.resize.worker.num"));
        for (int i = 0; i < num; i++) {
            ResizeWorker rw = new ResizeWorker(i + 1);
            LOG.info("Resize worker thread [" + (i + 1) + "] start.");
            rw.start();
            resizeWorkers.add(rw);
        }
        LOG.info("Clean file worker thread is ready to work.");
    }

    @PreDestroy
    public void destroy() {
        int num = Integer.parseInt(AppFacade.getConfig("clb.resize.worker.num"));
        for (int i = 0; i < num; i++) {
            resizeEventQueue.add(new ResizeEvent(null, null, true));
        }
        LOG.info("All workers are stopped.");
    }

    public boolean isEventInQueue(ResizeEvent event) {
        return filterSet.contains(event.getItem().getUniqueKey());
    }

    public static String resizeTempDir;
    static {
        Resource tempDir = new ClassPathResource("/");
        try {
            resizeTempDir = tempDir.getFile().getPath() + File.separator + "temp" + File.separator + "resize"
                    + File.separator;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkFileOperation(DocVersion dv) throws InvalidFileOperationException {
        if (!OperationUtils.canResize(dv.getFileExtension())) {
            throw new InvalidFileOperationException(dv, OperationUtils.OP_RESIZE);
        }
    }

    public ResizeImage resizeImage(DocVersion cfv, GridFSDBFile gfile, ResizeParam param) {
        long start = System.currentTimeMillis();
        SrcPair sp = loadOriginalImage(gfile);
        long end1 = System.currentTimeMillis();
        Map<ResizeType, ImageItem> map = convertImage(param, cfv, sp);
        long end2 = System.currentTimeMillis();
        List<ImageItem> itemList = new ArrayList<ImageItem>();
        itemList.addAll(map.values());
        long end3 = System.currentTimeMillis();
        ResizeImage result = buildResizeImageInfo(itemList, cfv);
        long end4 = System.currentTimeMillis();
        LOG.debug("Resize step 1 load original image use time " + (end1 - start) + " ms");
        LOG.debug("Resize step 2 convert small image use time " + (end2 - end1) + " ms");
        LOG.debug("Resize step 3 get image list use time " + (end3 - end2) + " ms");
        LOG.debug("Resize step 4 build resize image info use time " + (end4 - end3) + " ms");
        return result;
    }

    public SrcPair loadOriginalImage(GridFSDBFile gfile) {
        String srcStorageKey = gfile.get("storageKey").toString();
        SrcPair sp = localCacheService.saveTempFile(srcStorageKey, gfile.getInputStream());
        return sp;
    }

    private Map<ResizeType,ImageItem> convertImage(ResizeParam pm, DocVersion dv, SrcPair sp) {
        Map<ResizeType,ImageItem> map = new HashMap<ResizeType,ImageItem>();
        List<ResizeType> result = new ArrayList<ResizeType>();
        List<ImageItem> array = new ArrayList<ImageItem>();
        for (ResizeType rt : ResizeType.values()) {
            int point = getPoint(pm, rt);
            if (point <= 0) {
                LOG.error("Resize " + rt + " point should be greater than zero, docid=" + pm.getDocid() + ",version=" + pm.getVersion());
                continue;
            }
            String storageKey = mongoStorage.generateStorageKey();
            ImageItem item = buildImageItem(dv, storageKey, rt, point, pm.getUseWidthOrHeight());
            String skey = item.getUniqueKey();
            if (!filterSet.contains(skey)) {
                result.add(rt);
                array.add(item);
                map.put(rt, item);
            }else {
                LOG.warn("This job will be ignored, because there is the same job existed in the queue. Event detail:" + skey);
            }
        }
        imageItemService.delete(new DPair(dv.getAppid(), dv.getDocid(), dv.getVersion()), result);
        imageItemService.create(array);
        addEvents(sp, array);
        return map;
    }

    private int getPoint(ResizeParam pm, ResizeType rt) {
        int point = 0;
        switch (rt) {
        case SMALL:
            point = pm.getSmallPoint();
            break;
        case MEDIUM:
            point = pm.getMediumPoint();
            break;
        case LARGE:
            point = pm.getLargePoint();
            break;
        }
        return point;
    }

    public ResizeImage getResizeImageInfo(List<ImageItem> items, DocVersion dv) {
        for (ImageItem itm : items) {
            updateRetryCount(itm);
        }
        return buildResizeImageInfo(items, dv);
    }

    // 将请求参数resizePoint存入数据库中,以便查询；当指定了width时写入width字段中，当指定height时写入height字段中
    private ImageItem buildImageItem(DocVersion fv, String storageKey, ResizeType enumType, int resizePoint,
            String widthOrHeight) {
        ImageItem item = new ImageItem();
        item.setAppid(fv.getAppid());
        item.setDocid(fv.getDocid());
        item.setVersion(fv.getVersion());
        item.setFilename(fv.getFilename());
        item.setResizeType(enumType.toString());
        item.setStorageKey(storageKey);
        item.setFileExtension(JPEG);
        if ("width".equals(widthOrHeight)) {
            item.setWidth(resizePoint);
            item.setHeight(0);
        } else {
            item.setWidth(0);
            item.setHeight(resizePoint);
        }
        item.setUpdateTime(new Date());
        item.setStatus(CLBStatus.NOT_READY.toString());
        return item;
    }

    // 如果没有准备好时，url就返回null
    private ResizeImage buildResizeImageInfo(List<ImageItem> itemList, DocVersion cfv) {
        ResizeImage result = new ResizeImage();
        result.setDocid(cfv.getDocid());
        result.setFilename(cfv.getFilename());
        if(itemList==null || itemList.isEmpty()){
            result.setSmallURL(null);
            result.setMediumURL(null);
            result.setLargeURL(null);
        } else {
            for (ImageItem item : itemList) {
                ResizeType rt = ResizeType.getEnum(item.getResizeType());
                switch (rt) {
                case SMALL:
                    result.setSmallURL(getReturnURL(item));
                    break;
                case MEDIUM:
                    result.setMediumURL(getReturnURL(item));
                    break;
                case LARGE:
                    result.setLargeURL(getReturnURL(item));
                    break;
                }
            }
        }
        result.setVersion(cfv.getVersion() + "");
        result.setOriginalURL(URLBuilder.getDocURL(cfv.getStorageKey(), cfv.getFileExtension()));
        result.setMetaList(getImageMetaList(itemList));
        return result;
    }

    private String getReturnURL(ImageItem item) {
        String url = null;
        if (CLBStatus.isReady(item.getStatus())) {
            url = URLBuilder.getImageURL(item.getStorageKey());
        }
        return url;
    }

    private List<ImageMeta> getImageMetaList(List<ImageItem> itemList) {
        List<ImageMeta> result = new ArrayList<ImageMeta>();
        for (ImageItem item : itemList) {
            result.add(convertToImageMeta(item));
        }
        return result;
    }

    private void updateImageItem(String targetKey, long size, int desWidth, int desHeight, CLBStatus s) {
        List<ImageItem> itemList = imageItemService.readByStorageKey(targetKey);
        for (ImageItem t : itemList) {
            updateImageItem(t, size, desWidth, desHeight, s);
        }
    }

    public void updateImageItem(ImageItem t, long size, int desWidth, int desHeight, CLBStatus s) {
        if (t != null) {
            t.setUpdateTime(new Date());
            t.setWidth(desWidth);
            t.setHeight(desHeight);
            t.setStatus(s.toString());
            t.setSize(size);
            imageItemService.update(t);
        }
    }

    public String getTempFilename(String filename, String prefix) {
        return resizeTempDir + prefix + "_" + filename;
    }

    private File checkAndCreateTempDir() {
        File file = new File(resizeTempDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }
    
    public void addEvents(SrcPair sp, List<ImageItem> items) {
        for(ImageItem it:items){
            addEvent(sp,it);
        }
    }

    public void addEvent(SrcPair sp, ImageItem item) {
        ResizeEvent e = new ResizeEvent(sp, item, false);
        String key = e.getItem().getUniqueKey();
        if (filterSet.contains(key)) { // 任务去重过滤
            LOG.warn("This job will be ignored, because there is the same job existed in the queue. Event detail:" + key);
        } else {
            resizeEventQueue.add(e);
            filterSet.add(key);
        }
    }

    public GridFSDBFile readImgContent(String storageKey) throws FileContentNotFoundException {
        GridFSDBFile dfile = mongoStorage.loadImage(storageKey);
        if (storageKey == null) {
            throw new FileContentNotFoundException(null, "image");
        }
        return dfile;
    }

    private class ResizeWorker extends Thread {

        public ResizeWorker(int workerId) {
            this.setDaemon(true);
            this.workerId = workerId;
        }

        private int workerId = 0;

        public void run() {
            while (true) {
                try {
                    ResizeEvent e = resizeEventQueue.take();
                    if (e != null) {
                        if (e.isStopFlag()) {
                            break;
                        }
                        long start = System.currentTimeMillis();
                        resizeImageHandler(e);
                        long end = System.currentTimeMillis();
                        LOG.info("Success to resize image for doc[" + e.getItem().getUniqueKey() + "], use time "
                                + (end - start) + " ms and " + resizeEventQueue.size() + " events left.");
                        filterSet.remove(e.getItem().getUniqueKey());
                    }
                } catch (InterruptedException e1) {
                    LOG.error("Thread["+workerId+"] "+e1.getMessage(), e1);
                }
            }
        }

        private void resizeImageHandler(ResizeEvent e) {
            checkAndCreateTempDir();
            // Step.1.Load original file into local temp file
            String srcPath = e.getSp().getSrcPath();
            File srcTempFile = new File(srcPath);
            if (srcTempFile.exists()) { // refresh expire time
                localCacheService.removeJob(srcPath);
            } else {
                LOG.error("Write orignal image failed " + srcPath);
                return;
            }
            // Step.2.Execute resize command
            long start = System.currentTimeMillis();
            String dstPath = getTempFilename(e.getSp().getSrcStorageKey(), e.getItem().getResizeType());
            File dstTempFile = new File(dstPath);
            ImageUtil iu = new ImageUtil();
            SizePair p = iu.resize(srcPath, dstPath, getResizePoint(e.getItem()), getWidthOrHeight(e.getItem()));
            if (p == null) {
                updateImageItem(e.getItem().getStorageKey(), 0, 0, 0, CLBStatus.FAILED);
                LOG.error("Resize image failed when use the document is filename=" + e.getItem().getFilename()
                        + ", docid=" + e.getItem().getDocid() + ", storageKey=" + e.getSp().getSrcStorageKey());
                return;
            }
            long end = System.currentTimeMillis();
            LOG.debug("Resize image " + dstTempFile.getName() + " use time " + (end - start) + "ms");
            // Step.3.Send new file to mongodb
            start = System.currentTimeMillis();
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(dstTempFile);
                MFile mf = new MFile(e.getItem().getFilename(), e.getItem().getStorageKey());
                mongoStorage.writeImage(fis, mf);
            } catch (FileNotFoundException ex) {
                LOG.error(ex.getMessage(), ex);
            }  finally {
                IOUtils.closeQuietly(fis);
            }
            end = System.currentTimeMillis();
            LOG.debug("Write image " + dstTempFile.getName() + " to mongodb  use time " + (end - start) + "ms");
            // Step.4.Clear exist temp files and reset success status
            if (dstTempFile.length() != 0) {
                updateImageItem(e.getItem().getStorageKey(), dstTempFile.length(), p.getWidth(), p.getHeight(),
                        CLBStatus.READY);
            }
            // Step.5.Gabage clean worker
            localCacheService.addCleanJobs(new String[]{srcPath, dstPath});
        }

        private String getWidthOrHeight(ImageItem item) {
            if (item.getWidth() != 0)
                return "width";
            return "height";
        }

        private int getResizePoint(ImageItem item) {
            if (item.getWidth() != 0)
                return item.getWidth();
            return item.getHeight();
        }
    }

    public ImageItem getImageItem(int appid, ImageQuery q) {
        List<ImageItem> itemList = imageItemService.read(new DPair(appid, q.getDocid(),
                Integer.parseInt(q.getVersion())));
        for (ImageItem item : itemList) {
            if (item.getResizeType().equals(q.getResizeType())) {
                return item;
            }
        }
        return null;
    }

    public List<ImageItem> getImageItemList(int appid, int docid, int version) {
        List<ImageItem> itemList = imageItemService.read(new DPair(appid, docid, version));
        return itemList;
    }

    public ImageMeta convertToImageMeta(ImageItem item) {
        ImageMeta m = new ImageMeta();
        m.setDocid(item.getDocid());
        m.setVersion(item.getVersion());
        m.setWidth(item.getWidth());
        m.setHeight(item.getHeight());
        m.setStatus(CLBStatus.getStatus(item.getStatus()));
        m.setSize(item.getSize());
        m.setUpdateTime(item.getUpdateTime());
        m.setResizeType(item.getResizeType());
        return m;
    }

    public void updateRetryCount(ImageItem m) {
        String countKey = m.getUniqueKey();
        CLBStatus s = CLBStatus.getStatus(m.getStatus());
        if (s != null) {
            switch (CLBStatus.getStatus(m.getStatus())) {
            case NOT_READY:
                if (!filterSet.contains(countKey)) {
                    updateImageItem(m, 0, 0, 0, CLBStatus.ZOMBIE);
                    m.setStatus(CLBStatus.ZOMBIE.toString());
                }
                break;
            default:
            	break;
            }
        }
    }

    public void removeImage(String skey) {
        mongoStorage.removeImage(skey);
    }

}
