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
package cn.vlabs.clb.server.model;

import java.util.Date;

public class DocMeta {

    public static final int PUBLIC_DOC = 1;
    public static final int PRIVATE_DOC = 0;

    private int id;
    private int isPub;
    private int lastVersion;
    private Date lastUpdateTime;
    private Date createTime;
    private String authId;
    private int appid;
    private int docid;

    public DocMeta() {
    }

    public DocMeta(int appid, int docid) {
        this.appid = appid;
        this.docid = docid;
    }

    public int getDocid() {
        return docid;
    }

    public void setDocid(int docid) {
        this.docid = docid;
    }

    public int getAppid() {
        return appid;
    }

    public void setAppid(int appid) {
        this.appid = appid;
    }

    public int getLastVersion() {
        return lastVersion;
    }

    public void setLastVersion(int lastVersion) {
        this.lastVersion = lastVersion;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public int getIsPub() {
        return isPub;
    }

    public void setIsPub(int isPub) {
        this.isPub = isPub;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setId(int docid) {
        this.id = docid;
    }

    public int getId() {
        return id;
    }

    public String toString() {
        return "id=" + this.id + ",appid=" + this.appid + ",docid=" + this.docid + ",lastVersion=" + this.lastVersion
                + ",isPub=" + this.isPub + ",createTime=" + this.createTime + ",lastVersion=" + this.lastVersion
                + ",lastUpdateTime=" + this.lastUpdateTime;
    }

}
