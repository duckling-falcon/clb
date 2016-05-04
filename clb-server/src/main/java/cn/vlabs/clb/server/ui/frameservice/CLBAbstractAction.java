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

package cn.vlabs.clb.server.ui.frameservice;

import org.apache.log4j.Logger;

import cn.vlabs.clb.api.ErrorCode;
import cn.vlabs.clb.server.CurrentConnection;
import cn.vlabs.clb.server.exception.BaseException;
import cn.vlabs.clb.server.service.auth.Sessions;
import cn.vlabs.rest.RestSession;
import cn.vlabs.rest.ServiceAction;
import cn.vlabs.rest.ServiceException;

public abstract class CLBAbstractAction implements ServiceAction {
    
    protected static Logger log = Logger.getLogger(CLBAbstractAction.class);
    
    private RestSession session;

    public Object doAction(RestSession session, Object arg) throws ServiceException {
        if (isLoggin(session)) {
            Sessions sessions = (Sessions) session.getAttribute("sessions");
            CurrentConnection.setSessions(sessions);

            this.session = session;
            try {
                return doAction(arg);
            } catch (BaseException e) {
                log.error(e.toLog());
                throw new ServiceException(ErrorCode.INTERNAL_ERROR, e.getMessage());
            }
        } else {
            throw new ServiceException(ErrorCode.NEED_AUTH, "登录信息无效。");
        }
    }

    protected abstract Object doAction(Object arg) throws ServiceException, BaseException;

    protected RestSession getSession() {
        return this.session;
    }

    private boolean isLoggin(RestSession session) {
        Sessions sm = (Sessions) session.getAttribute("sessions");
        return (sm != null && sm.isLoggedin());
    }

    public static void exceptionHandler(BaseException e) throws ServiceException {
        //log.error(e.getMessage(), e);
        throw new ServiceException(e.getErrorCode(), e.getMessage());
    }

}
