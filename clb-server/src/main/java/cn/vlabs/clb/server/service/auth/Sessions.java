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
import java.util.HashMap;
import java.util.Locale;

import org.apache.log4j.Logger;

import cn.vlabs.clb.server.config.Config;
import cn.vlabs.clb.server.model.AppAuthInfo;
import cn.vlabs.clb.server.service.auth.permission.Entity;
import cn.vlabs.simpleAuth.Principal;
import cn.vlabs.simpleAuth.UserPrincipal;

public class Sessions {

    private HashMap<String, Object> map;

    private static Logger log = Logger.getLogger("Login");

    public Sessions() {
        map = new HashMap<String, Object>();
    }

    public static String getServerName() {
        return Config.getInstance().getStringProp("server.name", "");
    }

    public void addAppInfo(AppAuthInfo appAuthInfo) {
        map.put("auth.info", appAuthInfo);
    }

    public void addPrincipals(Principal[] principals) {
        if (principals != null && principals.length > 0) {
            map.put("auth.entity", bridgeToEntity(principals));
            for (int i = 0; i < principals.length; i++) {
                log.debug(principals[i].getName());
                if (principals[i] instanceof UserPrincipal) {
                    map.put("auth.user", principals[i]);
                }
            }
        }
    }

    public UserPrincipal currentUser() {
        return (UserPrincipal) map.get("auth.user");
    }

    private Entity[] bridgeToEntity(Principal[] principals) {
        ArrayList<Entity> entities = new ArrayList<Entity>();
        for (int i = 0; i < principals.length; i++) {
            if (principals[i] instanceof UserPrincipal) {
                BridgeUser bu = new BridgeUser((UserPrincipal) principals[i]);
                entities.add(bu);
            }
        }
        return entities.toArray(new Entity[entities.size()]);
    }

    public String getNickName() {
        UserPrincipal up = (UserPrincipal) map.get("auth.user");
        if (up != null) {
            if (up.getNickname() != null)
                return up.getNickname();
            else
                return up.getUsername();
        } else
            return null;
    }

    public String getUsername() {
        UserPrincipal up = (UserPrincipal) map.get("auth.user");
        if (up != null) {
            return up.getUsername();
        } else
            return null;
    }

    public boolean isLoggedin() {
        return (map.get("auth.user") != null);
    }

    public void setLocale(Locale locale) {
        map.put("current.locale", locale);
    }

    public Locale getLocale() {
        if (map.get("current.locale") == null)
            return Locale.CHINA;
        else
            return (Locale) map.get("current.locale");
    }

    public int getAppId() {
        AppAuthInfo info = (AppAuthInfo) map.get("auth.info");
        if (info != null) {
            return info.getId();
        }
        return 0;
    }

}
