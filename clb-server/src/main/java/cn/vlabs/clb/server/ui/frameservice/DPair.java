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

import cn.vlabs.clb.api.document.DocPair;

public class DPair {
    private int appid;
    private int docid;
    private int version;

    public static final int LATEST = -1;

    public DPair(int appid, int docid, int version) {
        this.docid = docid;
        this.version = version;
        this.appid = appid;
    }

    public DPair(int appid, DocPair pair) {
        this.appid = appid;
        this.docid = pair.getDocid();
        if (pair.isLatestVersion()) {
            this.version = LATEST;
        } else {
            this.version = Integer.parseInt(pair.getVersion());
        }
    }

    public int getAppid() {
        return appid;
    }

    public void setAppid(int appid) {
        this.appid = appid;
    }

    public DPair(int appid, int docid) {
        this.appid = appid;
        this.docid = docid;
        this.version = LATEST;
    }

    public int getDocid() {
        return docid;
    }

    public void setDocid(int docid) {
        this.docid = docid;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String toString() {
        return "appid=" + this.getAppid() + ",docid=" + this.getDocid() + ",version=" + this.getVersion();
    }

}
