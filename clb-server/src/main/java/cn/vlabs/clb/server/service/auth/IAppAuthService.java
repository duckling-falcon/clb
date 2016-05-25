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
package cn.vlabs.clb.server.service.auth;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.vlabs.clb.server.CurrentConnection;
import cn.vlabs.clb.server.dao.auth.IAppAuthDAO;
import cn.vlabs.clb.server.exception.NoAuthorityException;
import cn.vlabs.clb.server.model.AppAuthInfo;
import cn.vlabs.clb.server.model.DocMeta;

@Service
public class IAppAuthService {
    private static final Logger LOG = Logger.getLogger(IAppAuthService.class);
    
    @Autowired
    private IAppAuthDAO dao;
    
    @PreDestroy
    public void destory(){
    }

    public AppAuthInfo authenticate(String name, String password) {
        if (password == null) {
            password = "";
        }
        AppAuthInfo appAuth = getAppAuthInfo(name);
        if (appAuth != null) {
            if (StringUtils.equals(password, appAuth.getPassword())) {
                return appAuth;
            } else {
                return null;
            }
        } else {
            AppAuthInfo info = new AppAuthInfo();
            info.setName(name);
            info.setPassword(password);
            int appid = dao.save(info);
            info.setId(appid);
            return info;
        }
    }

    private synchronized AppAuthInfo getAppAuthInfo(String name) {
        return dao.getAppAuthInfo(name);
    }

    public boolean isPermitted(List<DocMeta> metas) throws NoAuthorityException {
        List<DocMeta> blackList = new ArrayList<DocMeta>();
        for (DocMeta m : metas) {
            int flag = checkAuth(m, getCurrentUser());
            if (flag != 0) {
                blackList.add(m);
            }
        }
        if (blackList.size() != 0) {
            LOG.info("log failed for "+ getCurrentUser());
            throw new NoAuthorityException(blackList, getCurrentUser());
        }
        return true;
    }

    public boolean isPermitted(DocMeta meta) throws NoAuthorityException {
        boolean result = true;
        if (meta == null || meta.getId() < 0) {
            result = false;
        } else {
            int flag = checkAuth(meta, getCurrentUser());
            if (flag != 0) {
                LOG.info("log failed for "+ getCurrentUser());
                throw new NoAuthorityException(flag, meta.getAuthId(), getCurrentUser());
            }
        }
        return result;
    }

    private int checkAuth(DocMeta m, String currentUser) {
        if (m.getIsPub() == DocMeta.PRIVATE_DOC && !StringUtils.equals(m.getAuthId(), currentUser)) {
            return m.getDocid();
        }
        return 0;
    }

    private String getCurrentUser() {
        return CurrentConnection.getSessions().currentUser().getName();
    }

}
