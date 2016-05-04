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

import cn.vlabs.clb.api.document.MetaInfo;
import cn.vlabs.clb.server.config.AppFacade;
import cn.vlabs.clb.server.exception.BaseException;
import cn.vlabs.clb.server.model.DocMeta;
import cn.vlabs.clb.server.model.DocVersion;
import cn.vlabs.clb.server.ui.frameservice.CLBAbstractAction;
import cn.vlabs.clb.server.ui.frameservice.DPair;
import cn.vlabs.clb.server.ui.frameservice.auth.AuthFacade;
import cn.vlabs.clb.server.ui.frameservice.document.DocumentFacade;
import cn.vlabs.rest.ServiceException;
import cn.vlabs.rest.TakeOver;

@java.lang.Deprecated
public class GetMetaInfoHandler extends CLBAbstractAction {

    private DocumentFacade df = AppFacade.getDocumentFacade();
    private AuthFacade af = AppFacade.getAuthFacade();
    
    private static final Logger LOG = Logger.getLogger(GetDocMetaInfoHandler.class);

    @Override
    protected Object doAction(Object arg) throws ServiceException {
        try {
            int appid = af.getAppId();
            DPair p = df.convertToPair(appid,arg);
            DocMeta meta = df.getDocMetaByDocid(p.getAppid(),p.getDocid());
            af.checkPermission(meta);
            DocMeta m = df.getDocMetaByDocid(appid,p.getDocid());
            DocVersion v = df.getDocVersion(p);
            return convertDocMetaToMetaInfo(m, v);
        } catch (BaseException e) {
            exceptionHandler(e);
        }
        return TakeOver.NO_MESSAGE;
    }
    

    public MetaInfo convertDocMetaToMetaInfo(DocMeta dm, DocVersion dv) {
        MetaInfo meta = new MetaInfo();
        meta.lastUpdate = dv.getCreateTime();
        meta.version = String.valueOf(dv.getVersion());
        meta.filename = dv.getFilename();
        meta.size = (int) dv.getSize();
        if(meta.size == -1){
            meta.size = df.updateMetaSize(dv);
            LOG.info("Update document content length for ["+ dv.toBriefString()+"]");
        }
        meta.docid = dv.getDocid();
        meta.isPub = dm.getIsPub();
        meta.createTime = dm.getCreateTime();
        return meta;
    }

}
