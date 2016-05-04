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
package cn.vlabs.clb.server.ui.frameservice.auth;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import cn.vlabs.clb.api.CLBConnection;
import cn.vlabs.clb.api.CLBPasswdInfo;
import cn.vlabs.clb.api.CLBServiceFactory;
import cn.vlabs.clb.api.document.DocumentService;
import cn.vlabs.clb.api.document.MetaInfo;

public class AuthFacadeTest  {

    public static void main(String[] args) {
        List<MyThread> threads = new ArrayList<MyThread>();
        for(int i=0;i<100;i++){
            threads.add(getOne());
            threads.add(getTwo());
            threads.add(getThree());
        }
        for(int i=0;i<100;i++){
            threads.get(i).start();
        }
    }

    private static MyThread getThree() {
        return new MyThread("hello", "fuck", "dee", "http://clbs.escience.cn/doc/123.pdf");
    }

    private static MyThread getTwo() {
        return new MyThread("escience", "escienceatcnic", "Tulips.jpg",
                "http://clbs.escience.cn/doc/whatafuckday.jpg");
    }

    private static MyThread getOne() {
        return new MyThread("dhome", "dhomeatcnic", "1346428633356.JPG_scale",
                "http://clbs.escience.cn/doc/5040e249e4b0ddf8d9d45762.JPG_scale");
    }
    

    private static class MyThread extends Thread {
        private CLBConnection conn;
        DocumentService ds = null;
        String name;
        String password;
        String filename;
        String url;

        public MyThread(String name, String password,String expectFileName,String expectURL) {
            this.name = name;
            this.password = password;
            this.filename = expectFileName;
            this.url = expectURL;
        }

        private CLBConnection login(String name, String password) {
            CLBPasswdInfo auth2 = new CLBPasswdInfo();
            auth2.setPassword(password);
            auth2.setUsername(name);
            CLBConnection conn2 = CLBServiceFactory.getClbConnection("http://159.226.10.89:8080/clb/ServiceServlet",
                    auth2, true);
            conn2.setAutoLogin(true);
            conn2.ensureLogin();
            return conn2;
        }
        
        @Override
        public void run() {
            conn = login(name, password);
            ds = CLBServiceFactory.getDocumentService(conn);
            print(ds.getMeta(6375));
            String tempURL = ds.getContentURL(6375, "latest");
            System.out.println("Thread["+getId()+"]:"+tempURL);
            Assert.assertEquals(tempURL, url);
        }

        private void print(MetaInfo meta) {
            System.out.println("Thread[" + getId() + "]:" + meta.getDocid() + "," + meta.getVersion() + ","
                    + meta.filename);
            Assert.assertEquals(filename , meta.filename);
        }
    }

}
