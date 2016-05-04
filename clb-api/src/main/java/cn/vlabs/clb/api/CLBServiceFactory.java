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

package cn.vlabs.clb.api;

import cn.vlabs.clb.api.document.DocumentService;
import cn.vlabs.clb.api.image.IResizeImageService;
import cn.vlabs.clb.api.image.ResizeParam;
import cn.vlabs.clb.api.pdf.IPdfService;
import cn.vlabs.clb.api.rest.DocumentServiceImpl;
import cn.vlabs.clb.api.rest.PDFServiceImpl;
import cn.vlabs.clb.api.rest.ResizeImageServiceImpl;
import cn.vlabs.clb.api.rest.SearchServiceImpl;
import cn.vlabs.clb.api.rest.TrivialSpaceServiceImpl;
import cn.vlabs.clb.api.search.SearchService;
import cn.vlabs.clb.api.trivial.ITrivialSpaceService;

/**
 * @Description: 产生服务的工厂类，app通过这个类拿到需要的service实例
 * @author CERC
 * @date Jul 12, 2011 9:10:24 AM
 * 
 */
public class CLBServiceFactory {
    public static String getVersion() {
        return "6.1.2";
    }

    @Deprecated
    public static SearchService getSearchService(String url, CLBAuthInfo auth) {
        return new SearchServiceImpl(getClbConnection(url, auth, false));
    }

    @Deprecated
    public static SearchService getSearchService(CLBConnection conn) {
        return new SearchServiceImpl(conn);
    }

    public static DocumentService getDocumentService(String url, CLBAuthInfo auth) {
        return new DocumentServiceImpl(getClbConnection(url, auth, false));
    }

    public static DocumentService getDocumentService(CLBConnection conn) {
        return new DocumentServiceImpl(conn);
    }

    public static CLBConnection getClbConnection(String url, CLBAuthInfo auth, boolean alive) {
        return buildClbConnection(true, url, auth, alive);
    }
    
    public static CLBConnection getClbConnection(String url,CLBAuthInfo auth, boolean alive,String serverVersion){
        CLBConnection conn = buildClbConnection(true, url, auth, alive);
        if(CLBServerVersion.PREVIOUS.toString().equals(serverVersion)){
            conn.setServerVersion(CLBServerVersion.PREVIOUS);
        }else{
            conn.setServerVersion(CLBServerVersion.CURRENT);
        }
        return conn;
    }

    private static CLBConnection buildClbConnection(boolean autoConnect, String url, CLBAuthInfo auth, boolean alive) {
        CLBConnection conn = new CLBConnection(url, auth);
        conn.setKeepAlive(alive);
        conn.setAutoLogin(autoConnect);
        if (!autoConnect) {
            conn.ensureLogin();
        }
        return conn;
    }
    
    

    public static IResizeImageService getResizeImageService(CLBConnection conn) {
        return new ResizeImageServiceImpl(conn);
    }

    public static IResizeImageService getResizeImageService(CLBConnection conn, ResizeParam rzp) {
        return new ResizeImageServiceImpl(conn, rzp.getSmallPoint(), rzp.getMediumPoint(), rzp.getLargePoint(),
                rzp.getUseWidthOrHeight());
    }

    public static IPdfService getPdfServie(CLBConnection conn) {
        return new PDFServiceImpl(conn);
    }

    public static ITrivialSpaceService getTrivialSpaceService(CLBConnection conn) {
        return new TrivialSpaceServiceImpl(conn);
    }
}
