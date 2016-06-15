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

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.duckling.falcon.api.idg.IIDGeneratorService;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.vlabs.clb.server.dao.document.IDocMetaDAO;

public class CLBStartupListener implements ServletContextListener {
    
    private static final Logger LOG = Logger.getLogger(CLBStartupListener.class);
    
    @Override
    public void contextDestroyed(ServletContextEvent event) {
	
    }
    
    @Override
    public void contextInitialized(ServletContextEvent event) {
        WebApplicationContext factory = getWebApplicationContext(event);
        IDocMetaDAO docMetaDao = factory.getBean(IDocMetaDAO.class);
        Map<Integer,Integer> currentMap = docMetaDao.getMaxDocId();
	IIDGeneratorService idg = factory.getBean(IIDGeneratorService.class);
	idg.init("clb", "docmeta", currentMap);
	LOG.info("Remote ID Generator init success.");
    }
    
    
    private WebApplicationContext getWebApplicationContext(ServletContextEvent event){
        ServletContext servletContext = event.getServletContext();
        return WebApplicationContextUtils.getWebApplicationContext(servletContext);
    }
    
    
    
}
