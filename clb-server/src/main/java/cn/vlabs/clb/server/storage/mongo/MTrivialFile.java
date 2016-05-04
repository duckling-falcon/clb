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

public class MTrivialFile {

    private String id;
    private String fileName;
    private long size;
    private String contentType;
    private ObjectId storageKey;
    private ObjectId spaceName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String fileType) {
        this.contentType = fileType;
    }

    public ObjectId getStorageKey() {
        return storageKey;
    }

    public void setStorageKey(ObjectId storageKey) {
        this.storageKey = storageKey;
    }

    public ObjectId getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(ObjectId spaceName) {
        this.spaceName = spaceName;
    }

}
