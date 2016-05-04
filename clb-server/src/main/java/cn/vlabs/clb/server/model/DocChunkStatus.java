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

import java.util.BitSet;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import cn.vlabs.clb.server.utils.BitSetUtil;

public class DocChunkStatus {

    private int id;
    private int chunkSize;
    private int appid;
    private int docid;
    private int version;
    private int currentChunkIndex;
    private String status;
    private int numOfChunk;
    private Date beginUploadTime;
    private Date lastUpdateTime;
    private String chunkMap;
    private long fileSize;
    private String md5;

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getChunkMap() {
        return chunkMap;
    }

    public void setChunkMap(String chunkMap) {
        this.chunkMap = chunkMap;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    public int getAppid() {
        return appid;
    }

    public void setAppid(int appid) {
        this.appid = appid;
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

    public int getCurrentChunkIndex() {
        return currentChunkIndex;
    }

    public void setCurrentChunkIndex(int currentChunkIndex) {
        this.currentChunkIndex = currentChunkIndex;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getNumOfChunk() {
        return numOfChunk;
    }

    public void setNumOfChunk(int numOfChunk) {
        this.numOfChunk = numOfChunk;
    }

    public Date getBeginUploadTime() {
        return beginUploadTime;
    }

    public void setBeginUploadTime(Date beginUploadTime) {
        this.beginUploadTime = beginUploadTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public BitSet loadChunkMap(){
        return BitSetUtil.deserialize(this.chunkMap);
    }
    
    public void updateChunkMap(BitSet bitSet){
        this.chunkMap = BitSetUtil.serialize(bitSet); 
    }
    
    
    public Set<Integer> findEmptyChunk() {
        TreeSet<Integer> set = new TreeSet<Integer>();
        BitSet bs = this.loadChunkMap();
        int length = this.getNumOfChunk();
        for (int i = 0; i < length; i++) {
            boolean flag = bs.get(i);
            if (!flag) {
                set.add(i);
            }
        }
        return set;
    }
}
