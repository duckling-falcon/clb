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

package cn.vlabs.clb.server.ui.frameservice.auth.handler;

import cn.vlabs.clb.api.CLBPasswdInfo;
import cn.vlabs.clb.server.CurrentConnection;
import cn.vlabs.clb.server.config.AppFacade;
import cn.vlabs.clb.server.model.AppAuthInfo;
import cn.vlabs.clb.server.service.auth.Sessions;
import cn.vlabs.clb.server.ui.frameservice.auth.AuthFacade;
import cn.vlabs.rest.RestSession;
import cn.vlabs.rest.ServiceAction;
import cn.vlabs.rest.ServiceException;
import cn.vlabs.simpleAuth.Principal;
import cn.vlabs.simpleAuth.UserPrincipal;

public class LoginHandler implements ServiceAction {
    
    private AuthFacade af = AppFacade.getAuthFacade();

    public Object doAction(RestSession session, Object arg) throws ServiceException {
        if (arg instanceof CLBPasswdInfo) {
            CLBPasswdInfo auth = (CLBPasswdInfo) arg;
            AppAuthInfo info = af.authenticate(auth.getUsername(), auth.getPassword());
            if (info!=null) {
                Sessions sessions = new Sessions();
                sessions.addPrincipals(new Principal[] { new UserPrincipal(auth.getUsername(), auth.getUsername()) });
                sessions.addAppInfo(info);
                session.setAttribute("sessions", sessions);
                CurrentConnection.setSessions(sessions);
                return true;
            }
        }
        return false;
    }
}
