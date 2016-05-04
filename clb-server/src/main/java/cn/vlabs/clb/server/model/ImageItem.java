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

public class ImageItem {

    private int id;
    private int appid;
    private int docid;
    private int version;
    private String storageKey;
    private String fileExtension;
    private String filename;
    private long size;
    private Date updateTime;
    private int width;
    private int height;
    private String resizeType;
    private String status;

    public int getAppid() {
        return appid;
    }

    public void setAppid(int appid) {
        this.appid = appid;
    }

    public ImageItem() {
    }

    public ImageItem(DPair p) {
        this.docid = p.getDocid();
        this.version = p.getVersion();
        this.appid = p.getAppid();
    }
    
    public ImageItem(DPair p,String resizeType) {
        this.docid = p.getDocid();
        this.version = p.getVersion();
        this.appid = p.getAppid();
        this.resizeType = resizeType;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getResizeType() {
        return resizeType;
    }

    public void setResizeType(String resizeType) {
        this.resizeType = resizeType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    
    public String getUniqueKey(){
        return this.getAppid()+ "." + this.getDocid() +"." + this.getVersion()+ "." +this.getResizeType() + "." + this.getWidth() + "x" + this.getHeight();
    }

}
