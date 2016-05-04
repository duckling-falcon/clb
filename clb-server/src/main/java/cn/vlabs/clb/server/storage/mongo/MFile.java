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
package cn.vlabs.clb.server.storage.mongo;

import org.bson.types.ObjectId;

import cn.vlabs.clb.api.io.impl.MimeType;

public class MFile {

    private String id;
    private int appid;
    private String filename;
    private ObjectId storageKey;
    private String fileExtension;
    private String contentType;
    private int docid;
    private String vid;
    private int isPub;
    private String md5;
    private long length;
    
    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public int getAppid() {
        return appid;
    }

    public void setAppid(int appid) {
        this.appid = appid;
    }

    public int getIsPub() {
        return isPub;
    }

    public void setIsPub(int isPub) {
        this.isPub = isPub;
    }

    public int getDocid() {
        return docid;
    }

    public void setDocid(int docid) {
        this.docid = docid;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public MFile(){}
    
    public MFile(String filename,String storageKey){
        this.fileExtension = MimeType.getSuffix(filename);
        this.filename = filename;
        this.contentType = MimeType.getContentType(this.fileExtension);
        this.storageKey = new ObjectId(storageKey);
    }
    
    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public ObjectId getStorageKey() {
        return storageKey;
    }

    public void setStorageKey(ObjectId storageKey) {
        this.storageKey = storageKey;
    }

    public String toString() {
        return "{fileName:" + filename + ",storageKey:" + storageKey + ",_id:" + id + "}";
    }

}
