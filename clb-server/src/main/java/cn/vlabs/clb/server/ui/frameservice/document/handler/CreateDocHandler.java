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

import org.apache.log4j.Logger;

import cn.vlabs.clb.api.document.CreateInfo;
import cn.vlabs.clb.api.document.UpdateInfo;
import cn.vlabs.clb.server.config.AppFacade;
import cn.vlabs.clb.server.exception.BaseException;
import cn.vlabs.clb.server.model.DocVersion;
import cn.vlabs.clb.server.service.search.fulltext.parser.ParserReadable;
import cn.vlabs.clb.server.ui.frameservice.CLBAbstractActionWithStream;
import cn.vlabs.clb.server.ui.frameservice.auth.AuthFacade;
import cn.vlabs.clb.server.ui.frameservice.document.DocumentFacade;
import cn.vlabs.clb.server.ui.frameservice.document.FileWriterHelper;
import cn.vlabs.clb.server.ui.frameservice.document.ResourceAdapter;
import cn.vlabs.rest.ServiceException;
import cn.vlabs.rest.TakeOver;
import cn.vlabs.rest.stream.IResource;

@java.lang.Deprecated
public class CreateDocHandler extends CLBAbstractActionWithStream {
    
    private static final Logger LOG = Logger.getLogger(CreateDocHandler.class);

    private DocumentFacade df = AppFacade.getDocumentFacade();
    private AuthFacade af = AppFacade.getAuthFacade();
    
    
    @SuppressWarnings("deprecation")
	@Override
    protected Object doAction(Object arg, IResource resource) throws ServiceException {
        CreateInfo info = (CreateInfo) arg;
        try {
            df.checkSystemIOMode("createDocument");
            ParserReadable readable = new ResourceAdapter(resource);
            int appid = af.getAppId();
            long start = System.currentTimeMillis();
            int docid = df.createDocMeta(appid, info.isPub);
            DocVersion dv = df.createWaitingDocVersion(appid, docid, readable.getName(), readable.getSize());
            long end1 = System.currentTimeMillis();
            FileWriterHelper helper = new FileWriterHelper(df);
            helper.saveFileContent(info.isPub, readable, appid, dv);
            long end2 = System.currentTimeMillis();
            LOG.debug("Save file meta info finished [" + dv.getFilename() + "] (" + (end1 - start) + ") ms");
            LOG.info("Save file content finished [" + dv.toBriefString() + "], use time " + (end2 - end1) + " ms.");
            df.updateUploadStatus(appid, docid, dv.getVersion());
            UpdateInfo ui = new UpdateInfo();
            ui.setDocid(dv.getDocid());
            ui.setVersion(dv.getVersion() + "");
            return ui;
        } catch (BaseException e) {
            exceptionHandler(e);
        }
        return TakeOver.NO_MESSAGE;
    }

}
