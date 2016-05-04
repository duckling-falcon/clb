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
package cn.vlabs.clb.api.document;


/**
 * clb存储的文档信息
 * @author liyanzhao
 *
 */
public class ClbMeta{

    private int docid;
    private int version;
    private String filename;
    private long size;
    private String storageKey;
    private String md5;

    public ClbMeta() {
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

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }


    public String getStorageKey() {
        return storageKey;
    }

    public void setStorageKey(String storageKey) {
        this.storageKey = storageKey;
    }

    

    public String toBriefString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"docid\":" + this.docid + ",");
        sb.append("\"version\":" + this.version + ",");
        sb.append("\"storageKey\":\"" + this.storageKey + "\",");
        sb.append("\"filename\":\"" + this.filename + ",");
        sb.append("\"size\":" + this.size);
        sb.append("}");
        return sb.toString();
    }

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

}
