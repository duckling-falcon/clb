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
package cn.vlabs.clb.server.ui.frameservice.image.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.vlabs.clb.api.CLBStatus;
import cn.vlabs.clb.api.image.ImageMeta;
import cn.vlabs.clb.server.config.AppFacade;
import cn.vlabs.clb.server.exception.BaseException;
import cn.vlabs.clb.server.model.DocMeta;
import cn.vlabs.clb.server.model.DocVersion;
import cn.vlabs.clb.server.model.ImageItem;
import cn.vlabs.clb.server.ui.frameservice.CLBAbstractAction;
import cn.vlabs.clb.server.ui.frameservice.DPair;
import cn.vlabs.clb.server.ui.frameservice.SrcPair;
import cn.vlabs.clb.server.ui.frameservice.auth.AuthFacade;
import cn.vlabs.clb.server.ui.frameservice.document.DocumentFacade;
import cn.vlabs.clb.server.ui.frameservice.image.ImageFacade;
import cn.vlabs.rest.ServiceException;

import com.mongodb.gridfs.GridFSDBFile;

public class QueryResizeStatusHandler extends CLBAbstractAction {

    private DocumentFacade df = AppFacade.getDocumentFacade();
    private AuthFacade af = AppFacade.getAuthFacade();
    private ImageFacade imf = AppFacade.getImageFacade();
    
    @Override
    protected Object doAction(Object arg) throws ServiceException, BaseException {
        long start = System.currentTimeMillis();
        int appid = af.getAppId();
        DPair p = df.convertToPair(appid, arg);
        DocMeta meta = df.getDocMetaByDocid(p.getAppid(), p.getDocid());
        af.checkPermission(meta);
        List<ImageItem> itemList = imf.getImageItemList(p.getAppid(), p.getDocid(), p.getVersion());
        if (itemList == null || itemList.isEmpty()) {
            long end = System.currentTimeMillis();
            log.warn("Query status result is no image item, where request params are " + p + " and use time " + (end - start) + "ms");
            return null;
        }
        Map<String, ImageMeta> map = new HashMap<String, ImageMeta>();
        boolean isZombie = false;
        for(int i = 0; i < itemList.size(); i++){
            ImageItem m = itemList.get(i);
            imf.updateRetryCount(m);
            if(CLBStatus.isZombie(m.getStatus())){
                isZombie = true;
            }
            map.put(m.getResizeType(), imf.convertToImageMeta(m));
        }
        if(isZombie) {
            DocVersion dv = df.getDocVersion(new DPair(appid,p.getDocid(), p.getVersion()));
            imf.checkFileOperation(dv);
            GridFSDBFile gfile = df.readDocContent(dv);
            SrcPair sp = imf.loadOriginalImage(gfile);
            for(ImageItem m: itemList){
                imf.addEvent(sp, m);
            }
            long end = System.currentTimeMillis();
            log.warn("Query status result is a zombie status, where request params are " + p + " and use time " + (end - start) + "ms");
        } else {
            long end = System.currentTimeMillis();
            log.warn("Query status result is a normal image, where request params are " + p + " and use time " + (end - start) + "ms");
        }
        return map;
    }

}
