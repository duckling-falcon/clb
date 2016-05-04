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

package cn.vlabs.clb.server.ui.frameservice.auth;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.vlabs.clb.server.CurrentConnection;
import cn.vlabs.clb.server.exception.NoAuthorityException;
import cn.vlabs.clb.server.model.AppAuthInfo;
import cn.vlabs.clb.server.model.DocMeta;
import cn.vlabs.clb.server.service.auth.IAppAuthService;

@Component("AuthFacade")
public class AuthFacade {

    @Autowired
    private IAppAuthService appAuthService;

    public AppAuthInfo authenticate(String name, String password) {
        return appAuthService.authenticate(name, password);
    }

    public void checkPermission(DocMeta meta) throws NoAuthorityException {
        appAuthService.isPermitted(meta);
    }

    public void checkPermission(List<DocMeta> metas) throws NoAuthorityException {
        appAuthService.isPermitted(metas);
    }

    public int getAppId() {
        return CurrentConnection.getSessions().getAppId();
    }

}
