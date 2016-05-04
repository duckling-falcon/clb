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
package cn.vlabs.clb.server.ui.frameservice.trivial;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import cn.vlabs.clb.api.CLBStatus;
import cn.vlabs.clb.api.io.impl.MimeType;
import cn.vlabs.clb.api.trivial.TrivialSpace;
import cn.vlabs.clb.server.exception.InvalidFileOperationException;
import cn.vlabs.clb.server.exception.MetaNotFoundException;
import cn.vlabs.clb.server.model.DocVersion;
import cn.vlabs.clb.server.model.TrivialItem;
import cn.vlabs.clb.server.service.search.fulltext.parser.ParserReadable;
import cn.vlabs.clb.server.service.trivial.ITrivialItemService;
import cn.vlabs.clb.server.storage.IStorageService;
import cn.vlabs.clb.server.storage.mongo.MTrivialFile;
import cn.vlabs.clb.server.ui.frameservice.DPair;
import cn.vlabs.clb.server.ui.frameservice.OperationUtils;
import cn.vlabs.clb.server.ui.frameservice.URLBuilder;
import cn.vlabs.clb.server.utils.ExtractListener;
import cn.vlabs.clb.server.utils.UnCompress;

import com.mongodb.gridfs.GridFSDBFile;

@Component("TrivialFacade")
public class TrivialFacade {

    @Autowired
    private ITrivialItemService trivialItemService;

    @Autowired
    private IStorageService mongoStorage;

    private DecompressWorker decompressWorker = new DecompressWorker();

    private BlockingQueue<DecompressEvent> eventQueue = new LinkedBlockingQueue<DecompressEvent>();

    private static String zipOutputDir;
    
    private static final Logger LOG = Logger.getLogger(TrivialFacade.class);
    
    @PreDestroy
    public void close(){
        decompressWorker.interrupt();
        LOG.info("Decompress worker is destoryed.");
    }
    
    static {
        Resource res = new ClassPathResource("/");
        try {
            zipOutputDir = res.getFile().getPath() + File.separator + "trivial" + File.separator + "output"
                    + File.separator;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TrivialFacade() {
        decompressWorker.start();
    }

    private File checkAndCreateTempDir() {
        File file = new File(zipOutputDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public void checkFileOperation(DocVersion dv) throws InvalidFileOperationException {
        if (!OperationUtils.canUnzip(dv.getFileExtension())) {
            throw new InvalidFileOperationException(dv, OperationUtils.OP_UNZIP);
        }
    }

    public void decompressZipFile(File zipFile, String spaceName, String filterDir) {
        eventQueue.add(new DecompressEvent(zipFile, spaceName, filterDir));
    }

    private class DecompressEvent {

        private File zipFile;
        private String spaceName;
        private String filterDir;

        public DecompressEvent(File zipFile, String spaceName, String filterDir) {
            this.zipFile = zipFile;
            this.spaceName = spaceName;
            this.filterDir = filterDir;
        }

        public File getZipFile() {
            return zipFile;
        }

        public String getSpaceName() {
            return spaceName;
        }

        public String getFilterDir() {
            return filterDir;
        }

    }

    public File writeZipFileToTempFile(GridFSDBFile gfile) {
        checkAndCreateTempDir();
        File tmpZipFile = new File(zipOutputDir + File.separator + "temp_" + System.currentTimeMillis() + "_"
                + gfile.getFilename());
        try {
            gfile.writeTo(tmpZipFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tmpZipFile;
    }

    public TrivialSpace saveTrivialItemInfo(DocVersion dv) {
        TrivialItem item = new TrivialItem();
        item.setAppid(dv.getAppid());
        item.setDocid(dv.getDocid());
        item.setVersion(dv.getVersion());
        item.setSpaceName(dv.getStorageKey());
        item.setUpdateTime(new Date());
        item.setStatus(CLBStatus.READY.toString());
        trivialItemService.create(item);
        return getTrivialSpace(item);
    }
    
    public TrivialSpace getTrivialSpace(TrivialItem item){
        TrivialSpace space = new TrivialSpace();
        space.setDocid(item.getDocid());
        space.setSpaceName(item.getSpaceName());
        space.setVersion(item.getVersion()+"");
        space.setSpaceURL(URLBuilder.getTrivialURL(item.getSpaceName()));
        return space; 
    }

    public TrivialItem getTrivialItem(DPair p) throws MetaNotFoundException {
        TrivialItem item = trivialItemService.read(p);
        if (item == null) {
            throw new MetaNotFoundException(p, "trivial");
        }
        return item;
    }

    public TrivialItem getTrivialItemByStorageKey(String spaceName) throws MetaNotFoundException {
        TrivialItem item = trivialItemService.readBySpaceName(spaceName);
        if (item == null) {
            throw new MetaNotFoundException(spaceName, "trivial");
        }
        return item;
    }

    public void removeTrivialFiles(String spaceName) {
        mongoStorage.removeTrivial(spaceName);
    }

    public int updateTrivialItemInfo(DocVersion dv, TrivialItem item) {
        item.setAppid(dv.getAppid());
        item.setDocid(dv.getDocid());
        item.setVersion(dv.getVersion());
        item.setUpdateTime(new Date());
        item.setStatus(CLBStatus.READY.toString());
        return trivialItemService.update(item);
    }

    public int removeTrivialItem(TrivialItem itm) {
        return trivialItemService.delete(new DPair(itm.getAppid(),itm.getDocid(), itm.getVersion()));
    }

    private class DecompressWorker extends Thread {

        private static final String CHARSET_UTF8 = "utf-8";

        public DecompressWorker() {
            this.setDaemon(true);
        }

        public void run() {
            while(true){
                try {
                    DecompressEvent evt = eventQueue.take();
                    if (evt != null) {
                        UnCompress util = null;
                        util = new UnCompress(
                                new BatchCreateTrivialFileListener(evt.getSpaceName(), evt.getFilterDir()),CHARSET_UTF8);
                        util.unzip(evt.getZipFile());
                        if(evt.getZipFile().exists()){
                            evt.getZipFile().delete();
                        }
                    }
                } catch (InterruptedException e) {
                    LOG.info("Decompress worker is going to close.");
                }
            }
        }

        private class BatchCreateTrivialFileListener implements ExtractListener {

            private String spaceName;
            private String filterDir;

            public BatchCreateTrivialFileListener(String spaceName, String filterDir) {
                this.spaceName = spaceName;
                this.filterDir = filterDir;
            }

            @Override
            public void onNewEntry(ParserReadable readable) throws IOException {
                MTrivialFile mtf = buildMTrivialFile(readable);
                mongoStorage.writeTrivial(readable.getInputStream(), mtf);
                readable.getInputStream().close();
            }

            private MTrivialFile buildMTrivialFile(ParserReadable readable) {
                MTrivialFile mtf = new MTrivialFile();
                String relativeFileName = readable.getName().replace(filterDir + File.separator, "");
                mtf.setFileName(relativeFileName);
                mtf.setSize(readable.getSize());
                mtf.setSpaceName(new ObjectId(spaceName));
                mtf.setStorageKey(new ObjectId(mongoStorage.generateStorageKey()));
                String suffix = MimeType.getSuffix(readable.getName());
                mtf.setContentType(MimeType.getContentType(suffix));
                return mtf;
            }
        }
    }

}
