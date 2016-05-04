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
package cn.vlabs.clb.server.ui.frameservice.document.handler.chunk;

import org.apache.log4j.Logger;

import cn.vlabs.clb.api.document.ChunkResponse;
import cn.vlabs.clb.api.document.ExecuteChunkUploadParam;
import cn.vlabs.clb.server.config.AppFacade;
import cn.vlabs.clb.server.exception.BaseException;
import cn.vlabs.clb.server.ui.frameservice.CLBAbstractActionWithStream;
import cn.vlabs.clb.server.ui.frameservice.auth.AuthFacade;
import cn.vlabs.clb.server.ui.frameservice.document.DocumentFacade;
import cn.vlabs.rest.ServiceException;
import cn.vlabs.rest.TakeOver;
import cn.vlabs.rest.stream.IResource;

public class ExecuteChunkUploadHandler extends CLBAbstractActionWithStream {

    private static final Logger LOG = Logger.getLogger(ExecuteChunkUploadHandler.class);

    private DocumentFacade df = AppFacade.getDocumentFacade();
    private AuthFacade af = AppFacade.getAuthFacade();

    @Override
    protected Object doAction(Object arg, IResource resource) throws ServiceException {
        try {
            df.checkSystemIOMode("executeChunkUpload");
            ExecuteChunkUploadParam cup = (ExecuteChunkUploadParam) arg;
            int appid = af.getAppId();
            ChunkResponse res = df.writeChunkData(appid, cup, resource.getInputStream());
            LOG.info(res.getStatusMessage());
            return res;
        } catch (BaseException e) {
            exceptionHandler(e);
        }
        return TakeOver.NO_MESSAGE;
    }

}
