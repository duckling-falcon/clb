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

package cn.vlabs.clb.server.ui.frameservice.document.handler;

import cn.vlabs.clb.api.document.UpdateInfo;
import cn.vlabs.clb.api.rest.UpdateMessage;
import cn.vlabs.clb.server.config.AppFacade;
import cn.vlabs.clb.server.exception.BaseException;
import cn.vlabs.clb.server.model.DocMeta;
import cn.vlabs.clb.server.model.DocVersion;
import cn.vlabs.clb.server.ui.frameservice.CLBAbstractActionWithStream;
import cn.vlabs.clb.server.ui.frameservice.auth.AuthFacade;
import cn.vlabs.clb.server.ui.frameservice.document.DocumentFacade;
import cn.vlabs.clb.server.ui.frameservice.document.FileWriterHelper;
import cn.vlabs.clb.server.ui.frameservice.document.ResourceAdapter;
import cn.vlabs.rest.ServiceException;
import cn.vlabs.rest.TakeOver;
import cn.vlabs.rest.stream.IResource;

public class UpdateDocHandler extends CLBAbstractActionWithStream {
    
    private DocumentFacade df = AppFacade.getDocumentFacade();
    private AuthFacade af = AppFacade.getAuthFacade();

    @Override
    protected Object doAction(Object arg, IResource resource) throws ServiceException {
        try {
            df.checkSystemIOMode("updateDocument");
            ResourceAdapter adapter = new ResourceAdapter(resource);
            UpdateMessage uarg = (UpdateMessage) arg;
            int appid = af.getAppId();
            DocMeta meta = df.getDocMetaByDocid(appid,uarg.docid);
            af.checkPermission(meta);
            DocVersion dv = df.createDocVersion(appid,uarg.docid, adapter.getName(), adapter.getSize());
            FileWriterHelper helper = new FileWriterHelper(df);
            helper.saveFileContent(meta.getIsPub(), adapter, appid, dv);
            df.updateUploadStatus(appid, dv.getDocid(), dv.getVersion());
            UpdateInfo ui = new UpdateInfo();
            ui.docid = dv.getDocid();
            ui.version = dv.getVersion() + "";
            return ui;
        } catch (BaseException e) {
            exceptionHandler(e);
        }
        return TakeOver.NO_MESSAGE;
    }

}
