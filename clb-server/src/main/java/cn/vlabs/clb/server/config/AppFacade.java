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
package cn.vlabs.clb.server.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import cn.vlabs.clb.server.ui.frameservice.auth.AuthFacade;
import cn.vlabs.clb.server.ui.frameservice.document.DocumentFacade;
import cn.vlabs.clb.server.ui.frameservice.image.ImageFacade;
import cn.vlabs.clb.server.ui.frameservice.pdf.PdfFacade;
import cn.vlabs.clb.server.ui.frameservice.trivial.TrivialFacade;

public class AppFacade implements ApplicationContextAware {

    private static ApplicationContext cxt;

    public void setApplicationContext(ApplicationContext _cxt) throws BeansException {
        cxt = _cxt;
    }

    public static ApplicationContext getApplicationContext() {
        return cxt;
    }

    public static DocumentFacade getDocumentFacade() {
        return cxt.getBean("DocumentFacade", DocumentFacade.class);
    }

    public static AuthFacade getAuthFacade() {
        return cxt.getBean("AuthFacade", AuthFacade.class);
    }

    public static PdfFacade getPdfFacade() {
        return cxt.getBean("PdfFacade", PdfFacade.class);
    }

    public static ImageFacade getImageFacade() {
        return cxt.getBean("ImageFacade", ImageFacade.class);
    }

    public static TrivialFacade getTrivialFacade() {
        return cxt.getBean("TrivialFacade", TrivialFacade.class);
    }

    public static String getConfig(String key) {
        PropertySourcesAdderBean psb = cxt.getBean(PropertySourcesAdderBean.class);
        return (String) psb.getProperties().get(key);
    }

}
