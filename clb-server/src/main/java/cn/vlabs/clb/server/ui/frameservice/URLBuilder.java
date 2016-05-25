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

import org.apache.commons.lang3.StringUtils;

import cn.vlabs.clb.server.config.AppFacade;

public class URLBuilder {

    private static final String NGX_DOMAIN = AppFacade.getConfig("clb.nginx.domain");
    private static final String DOC_CTX = AppFacade.getConfig("clb.nginx.context.doc");
    private static final String PDF_CTX = AppFacade.getConfig("clb.nginx.context.pdf");
    private static final String TRIVIAL_CTX = AppFacade.getConfig("clb.nginx.context.trivial");
    private static final String IMAGE_CTX = AppFacade.getConfig("clb.nginx.context.image");

    public static String getDocURL(String storageKey, String ext) {
        if(StringUtils.isEmpty(storageKey))
            return null;
        return NGX_DOMAIN + "/" + DOC_CTX + "/" + storageKey + "." + ext;
    }

    public static String getImageURL(String storageKey) {
        if(StringUtils.isEmpty(storageKey))
            return null;
        return NGX_DOMAIN + "/" + IMAGE_CTX + "/" + storageKey + ".jpg";
    }

    public static String getPdfURL(String storageKey) {
        if(StringUtils.isEmpty(storageKey))
            return null;
        return NGX_DOMAIN + "/" + PDF_CTX + "/" + storageKey + ".pdf";
    }

    public static String getTrivialURL(String storageKey) {
        if(StringUtils.isEmpty(storageKey))
            return null;
        return NGX_DOMAIN + "/" + TRIVIAL_CTX + "/" + storageKey;
    }

}
