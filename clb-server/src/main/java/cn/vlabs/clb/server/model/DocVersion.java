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

public class DocVersion {

    public static final int FIRST_VERSION = 1;
    public static final String LATEST_VERSION = "latest";

    public static final String COMPLETE_UPLOAD = "complete";
    public static final String WAITING_UPLOAD = "waiting";
    
    private int id;
    private int docid;
    private int appid;
    private int version;
    private String filename;
    private String fileExtension;
    private long size;
    private Date createTime;
    private String storageKey;
    private String uploadStatus;
    private Date completeTime;

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    public String getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(String uploadType) {
        this.uploadStatus = uploadType;
    }

    public DocVersion() {
    }

    public DocVersion(int appid, int docid) {
        this.appid = appid;
        this.docid = docid;
    }

    public DocVersion(DPair p) {
        this.appid = p.getAppid();
        this.docid = p.getDocid();
        this.version = p.getVersion();
    }

    public int getAppid() {
        return appid;
    }

    public void setAppid(int appid) {
        this.appid = appid;
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

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStorageKey() {
        return storageKey;
    }

    public void setStorageKey(String storageKey) {
        this.storageKey = storageKey;
    }

    public String toString() {
        return "id=" + this.id + ",appid=" + this.appid + ",docid=" + this.docid + ",version=" + this.version
                + ",filename=" + this.filename + ",storageKey=" + this.storageKey;
    }
    
    public boolean isCompletedStatus(){
        return COMPLETE_UPLOAD.equals(uploadStatus);
    }

    public String toBriefString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"appid\":" + this.appid + ",");
        sb.append("\"docid\":" + this.docid + ",");
        sb.append("\"version\":" + this.version + ",");
        sb.append("\"storageKey\":\"" + this.storageKey + "\",");
        sb.append("\"filename\":\"" + this.filename + ",");
        sb.append("\"size\":" + this.size);
        sb.append("}");
        return sb.toString();
    }

}
