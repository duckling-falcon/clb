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

import org.apache.log4j.Logger;

import cn.vlabs.clb.api.image.ResizeImage;
import cn.vlabs.clb.api.image.ResizeParam;
import cn.vlabs.clb.server.config.AppFacade;
import cn.vlabs.clb.server.exception.BaseException;
import cn.vlabs.clb.server.model.DocMeta;
import cn.vlabs.clb.server.model.DocVersion;
import cn.vlabs.clb.server.ui.frameservice.CLBAbstractAction;
import cn.vlabs.clb.server.ui.frameservice.DPair;
import cn.vlabs.clb.server.ui.frameservice.auth.AuthFacade;
import cn.vlabs.clb.server.ui.frameservice.document.DocumentFacade;
import cn.vlabs.clb.server.ui.frameservice.image.ImageFacade;
import cn.vlabs.rest.ServiceException;

import com.mongodb.gridfs.GridFSDBFile;

public class CreateResizeCopyHandler extends CLBAbstractAction {
    
    private static final Logger LOG = Logger.getLogger(CreateResizeCopyHandler.class);

    private ImageFacade imf = AppFacade.getImageFacade();
    private DocumentFacade df = AppFacade.getDocumentFacade();
    private AuthFacade af = AppFacade.getAuthFacade();

    @Override
    protected Object doAction(Object arg) throws ServiceException, BaseException {
        // 参数验证 少设置某个参数时，使用默认值
        ResizeParam rzp = (ResizeParam) arg;
        try {
            df.checkSystemIOMode("resizeImage");
            long start = System.currentTimeMillis();
            int appid = af.getAppId();
            DocMeta meta = df.getDocMetaByDocid(appid,rzp.getDocid());
            af.checkPermission(meta);
            DPair dp = new DPair(appid,rzp.getDocid(), Integer.parseInt(rzp.getVersion()));
            DocVersion dv = df.getDocVersion(dp);
            imf.checkFileOperation(dv);
            GridFSDBFile gfile = df.readDocContent(dv);
            ResizeImage result = imf.resizeImage(dv, gfile, rzp);
            if (result != null) {
                LOG.info("Send a resize event for document[" + dv.toBriefString() + "]");
            }
            long end = System.currentTimeMillis();
            log.info("Resize image request use time "+(end - start)+ " ms, where is " + dp);
            return result;
        } catch (BaseException e) {
            exceptionHandler(e);
        }
        return null;
    }

}
