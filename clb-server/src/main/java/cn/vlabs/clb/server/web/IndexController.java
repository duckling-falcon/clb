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
package cn.vlabs.clb.server.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.vlabs.clb.api.CLBStatus;
import cn.vlabs.clb.server.config.AppFacade;
import cn.vlabs.clb.server.exception.FileContentNotFoundException;
import cn.vlabs.clb.server.model.ImageItem;
import cn.vlabs.clb.server.service.image.IImageItemService;
import cn.vlabs.clb.server.storage.IStorageService;
import cn.vlabs.clb.server.ui.frameservice.DPair;
import cn.vlabs.clb.server.ui.frameservice.LocalFileCacheService;
import cn.vlabs.clb.server.ui.frameservice.SrcPair;
import cn.vlabs.clb.server.ui.frameservice.document.DocumentFacade;
import cn.vlabs.clb.server.ui.frameservice.image.ImageFacade;

import com.mongodb.gridfs.GridFSDBFile;

@Controller
@RequestMapping("/admin")
public class IndexController {

    @RequestMapping
    public ModelAndView show() {
        return new ModelAndView("a");
    }

    @Autowired
    private IImageItemService imageService;
    @Autowired
    private IStorageService mongoStorage;

    @RequestMapping("/updateImageStatus")
    @ResponseBody
    public JSONObject updateImageStatus(@RequestParam("appid") Integer appid, @RequestParam("docid") Integer docid,
            @RequestParam("version") Integer version, @RequestParam("status") String newStatus) {
        JSONObject json = new JSONObject();
        List<ImageItem> items = imageService.read(new DPair(appid, docid, version));
        if (items.size() != 0) {
            for (ImageItem item : items) {
                CLBStatus s = CLBStatus.getStatus(newStatus);
                if (s == null) {
                    json.put("status", "error");
                    json.put("message", "Invalidate Status Param");
                    return json;
                }
                item.setStatus(s.toString());
                imageService.update(item);
            }
            json.put("status", "success");
        } else {
            json.put("status", "empty");
        }
        return json;
    }

    private ImageFacade imf = AppFacade.getImageFacade();
    @Autowired
    private LocalFileCacheService localCacheService;
    private DocumentFacade df = AppFacade.getDocumentFacade();
    private static final Logger LOG = Logger.getLogger(IndexController.class);

    @RequestMapping("/batchResizeExistImages")
    @ResponseBody
    public JSONObject batchResizeExistImages(@RequestParam("appid") int appid,
            @RequestParam("startTime") String startTimeStr, @RequestParam("endTime") String endTimeStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date startTime = sdf.parse(startTimeStr);
            Date endTime = sdf.parse(endTimeStr);
            if (startTime != null) {
                List<ImageItem> items = imageService.readAll(appid, startTime, endTime);
                int i = 0;
                LOG.info("There are " + items.size() + " records.");
                long start1 = System.currentTimeMillis();
                for (ImageItem item : items) {
                    long start = System.currentTimeMillis();
                    i++;
                    DPair p = new DPair(appid, item.getDocid(), item.getVersion());
                    String skey = item.getStorageKey();
                    imf.removeImage(skey);
                    try {
                        GridFSDBFile gbfile = df.readDocContent(p);
                        if (gbfile != null) {
                            SrcPair sp = localCacheService.saveTempFile(item.getStorageKey(), gbfile.getInputStream());
                            imf.addEvent(sp, item);
                            long end = System.currentTimeMillis();
                            LOG.info("Finish send a new resize event. There are (" + i + "/" + items.size() + ") jobs to send. Use time " + (end - start) + "ms");
                        }
                    } catch (FileContentNotFoundException e) {
                        LOG.info("The original file is not found." + item.getUniqueKey());
                    }
                }
                long end1 = System.currentTimeMillis();
                LOG.info("The whole job for refresh "+ items.size() + " images use time "+(end1 - start1) +" ms.");
            }
        } catch (ParseException e1) {
            LOG.error("Date parse error", e1);
        }
        JSONObject json = new JSONObject();
        json.put("status", "success");
        return json;
    }

    @RequestMapping("/queryDocument")
    @ResponseBody
    public JSONObject query(@RequestParam("storageKey") String storageKey) {
        GridFSDBFile dbf = mongoStorage.loadDocument(storageKey);
        if(dbf!=null){
            JSONObject json = new JSONObject();
            json.put("filename", dbf.getFilename());
            json.put("appid", dbf.get("appid"));
            json.put("docid", dbf.get("docid"));
            json.put("version", dbf.get("version"));
            json.put("length", dbf.getLength());
            json.put("md5", dbf.getMD5());
            return json;
        }
        JSONObject json = new JSONObject();
        json.put("status", "no content found");
        return json;
    }

}