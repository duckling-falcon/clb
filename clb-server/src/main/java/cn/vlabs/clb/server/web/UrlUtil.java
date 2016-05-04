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
package cn.vlabs.clb.server.web;

import javax.servlet.http.HttpServletRequest;

/**
 * @title: UrlUtil.java
 * @package cn.vlabs.clb.server.web
 * @description: URL的工具类
 * @author clive
 * @date 2012-9-6 上午11:36:49
 */
public final class UrlUtil {
    private UrlUtil() {
    }

    /**
     * @description 根据request获得域名，不包含contextPath
     * @param request
     * @return
     */
    public static String getBaseURL(HttpServletRequest request) {
        String baseURL = request.getScheme() + "://" + request.getServerName();
        int port = request.getLocalPort();
        if (port != DEFAULT_HTTP_PORT || port != DEFAULT_HTTPS_PORT) {
            baseURL = baseURL + ":" + port;
        }
        return baseURL;
    }

    private static final int DEFAULT_HTTP_PORT = 80;
    private static final int DEFAULT_HTTPS_PORT = 443;

}
