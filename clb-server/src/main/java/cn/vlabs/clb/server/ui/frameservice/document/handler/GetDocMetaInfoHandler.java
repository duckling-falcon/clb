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

import com.google.gson.Gson;

import cn.vlabs.clb.api.document.DocMetaInfo;
import cn.vlabs.clb.api.document.DocPair;
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

public class GetDocMetaInfoHandler extends CLBAbstractAction {

    private DocumentFacade df = AppFacade.getDocumentFacade();
    private AuthFacade af = AppFacade.getAuthFacade();
    
    private static final Logger LOG = Logger.getLogger(GetDocMetaInfoHandler.class);
    private Gson mGson = new Gson();

    @Override
    protected Object doAction(Object arg) throws ServiceException {
        try {
            int appid = af.getAppId();
            DPair p = df.convertToPair(appid,mGson.fromJson((String)arg, DocPair.class));
            DocMeta meta = df.getDocMetaByDocid(p.getAppid(),p.getDocid());
            af.checkPermission(meta);
            DocMeta m = df.getDocMetaByDocid(appid,p.getDocid());
            DocVersion v = df.getDocVersion(p);
            
            String md5 = df.queryMd5(v.getStorageKey());
            return convertDocMetaToMetaInfo(m, v,md5);
        } catch (BaseException e) {
            exceptionHandler(e);
        }
        return TakeOver.NO_MESSAGE;
    }
    

    public String convertDocMetaToMetaInfo(DocMeta dm, DocVersion dv,String md5) {
    	DocMetaInfo meta = new DocMetaInfo();
        meta.lastUpdate = dv.getCreateTime();
        meta.version = String.valueOf(dv.getVersion());
        meta.filename = dv.getFilename();
        meta.size = dv.getSize();
        if(meta.size == -1){
            meta.size = df.updateMetaSize(dv);
            LOG.info("Update document content length for ["+ dv.toBriefString()+"]");
        }
        meta.docid = dv.getDocid();
        meta.isPub = dm.getIsPub();
        meta.createTime = dm.getCreateTime();
        meta.md5 = md5;
        return mGson.toJson(meta);
    }

}
