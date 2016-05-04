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

import java.util.List;

import cn.vlabs.clb.api.CLBStatus;
import cn.vlabs.clb.api.image.ImageMeta;
import cn.vlabs.clb.api.image.ResizeImage;
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
import cn.vlabs.rest.TakeOver;

import com.mongodb.gridfs.GridFSDBFile;

public class GetResizeImageInfoHandler extends CLBAbstractAction {
    
    private ImageFacade imf = AppFacade.getImageFacade();
    private DocumentFacade df = AppFacade.getDocumentFacade();
    private AuthFacade af = AppFacade.getAuthFacade();

    @Override
    protected Object doAction(Object arg) throws ServiceException, BaseException {
        try {
            int appid = af.getAppId(); //get application id
            DPair p = df.convertToPair(appid,arg); //convert argument to DPair object
            DocMeta meta = df.getDocMetaByDocid(p.getAppid(),p.getDocid()); //query meta info
            af.checkPermission(meta); // check meta
            DocVersion dv = df.getDocVersion(p); // find 
            List<ImageItem> itemLists = imf.getImageItemList(dv.getAppid(), dv.getDocid(), dv.getVersion());
            ResizeImage rsi = imf.getResizeImageInfo(itemLists,dv); //查询imageInfo时也需要做是否为zombie状态的检查
            boolean isZombie = false;
            for(ImageMeta m:rsi.getMetaList()){
                if(m.getStatus() == CLBStatus.ZOMBIE){
                    isZombie = true;
                    break;
                }
            }
            if(isZombie) { //如果是zombie状态则重新压一遍
                imf.checkFileOperation(dv);
                GridFSDBFile gfile = df.readDocContent(dv);
                SrcPair sp = imf.loadOriginalImage(gfile);
                for(ImageItem m: itemLists){
                    imf.addEvent(sp, m);
                }
            }
            return rsi;
        } catch (BaseException e) {
            exceptionHandler(e);
        }
        return TakeOver.NO_MESSAGE;
    }

}
