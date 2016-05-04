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
package cn.vlabs.clb.server.service;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringManager {
    public void destroy() {
        if (factory != null) {
            factory.close();
            factory = null;
        }
    }

    public BeanFactory getFactory() {
        return factory;
    }

    public void init() {
        initLog4j();
        initWebRoot();
        initApplicationContext();
    }

    private void initApplicationContext() {
        String contextxml = "test-context.xml";
        try {
            factory = new ClassPathXmlApplicationContext(contextxml);
        } catch (Throwable e) {
            log.error("Startup failed in read context.",e);
        }
    }

    private void initWebRoot() {
        String webRootKey = "clb.root";
        String webroot = "clb";
        log.info("set web root path[" + webRootKey + "]:" + webroot);
        System.setProperty(webRootKey, webroot);
    }

    private void initLog4j() {
        String log4jConfig = "src/test/resources/log4j.properties";
        PropertyConfigurator.configure(log4jConfig);
        log = Logger.getLogger(SpringManager.class);
    }

    private Logger log;

    private ClassPathXmlApplicationContext factory;
}
