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

import cn.vlabs.clb.server.ui.frameservice.DPair;

public class PdfItem {
    private int id;
    private int appid;
    private int docid;
    private int version;
    private String storageKey;
    private long size;
    private String status;
    private Date updateTime;
    private String filename;
//    private long useTime;
//    private String hasMorePage;

//    public String getHasMorePage() {
//		return hasMorePage;
//	}
//
//	public void setHasMorePage(String hasMorePage) {
//		this.hasMorePage = hasMorePage;
//	}
//
//	public long getUseTime() {
//		return useTime;
//	}
//
//	public void setUseTime(long useTime) {
//		this.useTime = useTime;
//	}

	public PdfItem() {
    }
    
    public PdfItem(int appid,int docid) {
        this.appid = appid;
        this.docid = docid;
    }

    public PdfItem(DPair p) {
        this.docid = p.getDocid();
        this.appid = p.getAppid();
        this.version = p.getVersion();
    }

    
    public int getAppid() {
        return appid;
    }

    public void setAppid(int appid) {
        this.appid = appid;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String fileName) {
        this.filename = fileName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDocid() {
        return docid;
    }

    public void setDocid(int docid) {
        this.docid = docid;
    }

    public String getStorageKey() {
        return storageKey;
    }

    public void setStorageKey(String storageKey) {
        this.storageKey = storageKey;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    
    public String getUniqueKey(){
        return this.getAppid()+ "." + this.getDocid() +"." + this.getVersion();
    }

}
