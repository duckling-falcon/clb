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
package cn.vlabs.clb.server.storage;

import java.io.InputStream;
import java.util.Set;

import cn.vlabs.clb.server.storage.mongo.FileMeta;
import cn.vlabs.clb.server.storage.mongo.MFile;
import cn.vlabs.clb.server.storage.mongo.MTrivialFile;
import cn.vlabs.clb.server.ui.frameservice.DPair;

import com.mongodb.gridfs.GridFSDBFile;

public interface IStorageService {

    public String generateStorageKey();
    
    public GridFSDBFile loadDocument(DPair p);

    public GridFSDBFile loadDocument(String storageKey);

    public GridFSDBFile loadImage(String storageKey);

    public GridFSDBFile loadPdf(String storageKey);
    
    public GridFSDBFile loadPdf(int appid,int docid,String vid);

    public GridFSDBFile loadTrivial(String spaceName, String filename);
    
    public FileMeta findDocument(String storageKey);
    
    public FileMeta findImage(String storageKey);
    
    public FileMeta findPdf(String storageKey);
    
    public MTrivialFile findTrivial(String spaceName, String fileName);

    public void writeTrivial(InputStream content, MTrivialFile meta);

    public void writeDocument(InputStream content, MFile meta);

    public void writeImage(InputStream content, MFile meta);

    public void writePdf(InputStream content, MFile meta);

    public void removeDocument(String storageKey);

    public void removeTrivial(String spaceName);

    void removeImage(String storageKey);

    void removePdf(String storageKey);

    void chunkedWriteDocument(int chunkIndex, int totalChunkNum, InputStream ins,int length, MFile meta, int chunkSize);

    Set<Integer> queryDocumentChunkSet(String storageKey);
    
    public String queryMd5(String storageKey);

}