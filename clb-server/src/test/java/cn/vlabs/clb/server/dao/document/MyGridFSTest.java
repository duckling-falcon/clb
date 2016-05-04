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
package cn.vlabs.clb.server.dao.document;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.vlabs.clb.server.storage.mongo.MFile;
import cn.vlabs.clb.server.storage.mongo.extend.MyGridFS;
import cn.vlabs.clb.server.storage.mongo.extend.MyGridFSInputFile;
import cn.vlabs.clb.server.utils.GlobalConst;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;

public class MyGridFSTest {

    public static final int BUF_SIZE = 4096;
    public static final int EOF = -1;

    private MongoClient client;

    public MongoClient getClient() {
        return client;
    }

    public void setClient(MongoClient client) {
        this.client = client;
    }

    public static void main(String[] args) throws IOException {
        MyGridFSTest t = new MyGridFSTest();
        t.setClient(new MongoClient("10.10.1.150", 27017));
        String path = "/Users/clive/PycharmProjects/pyclb/export/Boot2Docker-1.1.0.pkg";
        for (int i = 0; i < 50; i++) {
            System.out.println("Prepare to send data");
            File f = new File(path);
            long start = System.currentTimeMillis();
            t.uploadFile(path);
            long end = System.currentTimeMillis();
            System.out.println(String.format("Send nonsense data, filename=%s size=%d usetime=%.3f ms", f.getName(),
                    f.length(), (end - start) / 1000.0));
        }
    }

    public void uploadFile(String path) throws IOException {
        byte[] buf = new byte[261120];
        File f = new File(path);
        InputStream ins = new FileInputStream(f);
        int index = 0;
        int readBytes = 0;
        int count = 0;
        MFile mf = new MFile();
        mf.setMd5("507bf9b7a91a36e7615842aaa1b2ade4");
        mf.setFilename("Boot2Docker-1.1.0.pkg");
        mf.setContentType("application/octem");
        mf.setLength(141441397);
        while (-1 != (readBytes = ins.read(buf))) {
            ByteArrayInputStream bis = new ByteArrayInputStream(buf);
            System.out.println("upload chunk " + index);
            chunkedUpload(index, count, bis, readBytes, mf, "fs");
            index++;
        }
        ins.close();
    }

    private void chunkedUpload(int chunkIndex, int chunkCount, InputStream data, int dataLength, MFile mf,
            String tableName) {
        DB db = client.getDB("docs");
        db.requestStart();
        MyGridFS gfs = new MyGridFS(db, tableName);
        DBCollection col = db.getCollection(tableName + ".files");
        col.setWriteConcern(WriteConcern.SAFE);
        MyGridFSInputFile inf = new MyGridFSInputFile(gfs);
        inf.setFilename(mf.getFilename());
        inf.setId(mf.getStorageKey());
        inf.setContentType(mf.getContentType());
        inf.setChunkSize(GlobalConst.DEFLAUT_CHUNK_SIZE);
        inf.setContentType(mf.getContentType());
        inf.setMD5(mf.getMd5());
        inf.setLength(mf.getLength());
        int numBytes = 0;
        int tmpRead = 0;
        byte[] buf = new byte[BUF_SIZE];
        byte[] bigBuf = new byte[dataLength];
        try {
            while (EOF != (tmpRead = data.read(buf))) {
                System.arraycopy(buf, 0, bigBuf, numBytes, tmpRead);
                numBytes += tmpRead;
            }
            if (dataLength != numBytes) {
                System.out.println("Missing Data expected data-size=" + dataLength + ", actual read data-size="
                        + numBytes);
                return;
            }
            inf.setInputStream(new ByteArrayInputStream(bigBuf, 0, numBytes));
            inf.setSavedChunk(false);
            inf.setCurrentChunkNumber(chunkIndex);
            inf.saveChunks(GlobalConst.DEFLAUT_CHUNK_SIZE);
            System.out.println("Chunk " + chunkIndex + " upload finish, bytes " + numBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (chunkIndex == (chunkCount - 1)) {
            inf.setSavedChunk(true);
            inf.save();
        }
        db.requestDone();
    }

}
