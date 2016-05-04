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

package cn.vlabs.clb.server;

import org.apache.log4j.Logger;

import cn.vlabs.clb.server.service.auth.Sessions;

public class CurrentConnection {

    private CurrentConnection() {
    }

    public static void setSessions(Sessions s) {
        if (s == null)
            throw new NullPointerException();
        getBaseInfo().setSessions(s);
    }

    public static Sessions getSessions() {
        return getBaseInfo().getSessions();
    }

    public static void clear() {
        conn.set(null);
        log.debug("Current Session cleared");
    }

    private static BaseInfo getBaseInfo() {
        if (conn.get() == null) {
            conn.set(new BaseInfo());
        }
        return conn.get();
    }

    private static ThreadLocal<BaseInfo> conn = new ThreadLocal<BaseInfo>();

    public static final transient Logger log;
    static {
        log = Logger.getLogger(CurrentConnection.class);
    }
}
