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
package cn.vlabs.clb.server.service.mongo;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.bson.types.ObjectId;

import cn.vlabs.clb.api.io.impl.MimeType;
import cn.vlabs.clb.server.storage.mongo.MFile;
import cn.vlabs.clb.server.storage.mongo.MongoStorageService;
import cn.vlabs.clb.server.storage.mongo.extend.MyGridFS;
import cn.vlabs.clb.server.storage.mongo.extend.MyGridFSInputFile;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;

public class MongoChunkedUpload {

    public static void testWriteFile(String path) {
        MongoStorageService ms = new MongoStorageService("10.10.1.157", 27017, "temp_docs");
        String key = new ObjectId().toString();
        MFile mf = new MFile();
        File f = new File(path);
        String filename = f.getName();
        String suffix = MimeType.getSuffix(filename);
        mf.setAppid(1);
        mf.setFilename(filename);
        mf.setContentType(MimeType.getContentType(suffix));
        mf.setFileExtension(suffix);
        mf.setStorageKey(new ObjectId(key));
        InputStream ins;
        try {
            ins = new FileInputStream(f);
            ms.writeDocument(ins, mf);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void testOldFile(String path){
        System.out.println("First write into old gridfs.");
        long start = System.currentTimeMillis();
        testWriteFile(path);
        long end = System.currentTimeMillis();
        System.out.println("Upload into old fs use time "+(end - start));
    }
    
    public static void testNewFileSystem(String path) throws UnknownHostException{
        System.out.println("Second write into myfs");
        long start = System.currentTimeMillis();
        Mongo client = new MongoClient("10.10.1.157", 27017);
        DB db = client.getDB("myfs");
        MyGridFS gfs = new MyGridFS(db);
        byte[] buf = new byte[CHUNK_SIZE];
        File f = new File(path);
        String fn = f.getName();
        try {
            FileInputStream fins = new FileInputStream(f);
            MyGridFSInputFile inf = new MyGridFSInputFile(gfs);
            String fileId = new ObjectId().toString();
            inf.setChunkSize(CHUNK_SIZE);
            inf.setFilename(fn);
            inf.setId(fileId);
            inf.setContentType("application/pdf");
            int index = 0;
            int numBytes = 0;
            while (EOF != (numBytes = fins.read(buf))) {
                inf.setInputStream(new ByteArrayInputStream(buf, 0, numBytes));
                inf.setSavedChunk(false);
                inf.setCurrentChunkNumber(index);
                inf.saveChunks(CHUNK_SIZE);
                if (numBytes < CHUNK_SIZE) {
                    inf.setSavedChunk(true);
                } //a326a3a4107fcdd5bcac7d40b4eb0e30
                System.out.println("Chunk " + index + " upload finish, bytes " + numBytes);
                index++;
            }
            inf.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("Upload into new fs use time "+(end - start));
    }

    public static void main(String[] args) throws UnknownHostException {
        String path = "/Users/clive/Movies/C标准库(英文版).pdf";
        testOldFile(path);
        testNewFileSystem(path);
    }

    static final int CHUNK_SIZE = 4 * 1024;
    static final int BUF_SIZE = 4096;
    static final String TEMP_FILE_DIR = "/workspace/temp";
    private static final int EOF = -1;

    public static int splitFileToChunks(String path) {
        File f = new File(path);
        byte[] buf = new byte[BUF_SIZE];
        int chunkIndex = 0;
        long numBytesOfRead = 0;
        int chunkCount = 0;
        boolean flag = true;
        while (flag) {
            try {
                OutputStream output = new FileOutputStream(new File("/workspace/temp/part." + chunkIndex));
                RandomAccessFile raf = new RandomAccessFile(f, "r");
                raf.seek(CHUNK_SIZE * chunkIndex);
                long count = 0;
                int n = 0;
                while (EOF != (n = raf.read(buf))) {
                    output.write(buf, 0, n);
                    count += n;
                    if (count < CHUNK_SIZE) {
                        continue;
                    } else {
                        break;
                    }
                }
                System.out.println("Chunk file[" + chunkIndex + "] write finish, size " + count);
                numBytesOfRead += count;
                chunkCount++;
                if (numBytesOfRead >= f.length()) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            chunkIndex++;
        }
        System.out.println("Finish");
        return chunkCount;
    }

    public static void sendChunkFileToMongo(DBCollection c, String prefixPath, int chunkCount) {
        ObjectId _id = new ObjectId();
        for (int i = 0; i < chunkCount; i++) {
            int numOfChunk = i;
            byte[] buf = new byte[CHUNK_SIZE];
            byte[] toWriteBuf = null;
            try {
                FileInputStream fis = new FileInputStream(prefixPath + "." + numOfChunk);
                int n = 0;
                int tempCount = 0;
                while (EOF != (n = fis.read(buf))) {
                    System.out.println("read buf size " + n + ".");
                    tempCount += n;
                }
                if (tempCount < CHUNK_SIZE && tempCount > 0) {
                    System.out.println("copy to a new array");
                    toWriteBuf = new byte[tempCount];
                    System.arraycopy(buf, 0, toWriteBuf, 0, tempCount);
                } else {
                    toWriteBuf = buf;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            DBObject chunkData = createChunk(_id, numOfChunk, toWriteBuf);
            System.out.println("Write chunk " + numOfChunk);
            c.save(chunkData);
        }
    }

    private static DBObject createChunk(Object id, int currentChunkNumber, byte[] writeBuffer) {
        return BasicDBObjectBuilder.start().add("files_id", id).add("n", currentChunkNumber).add("data", writeBuffer)
                .get();
    }

    public static String restPost(String serverURL, File targetFile, Map<String, String> mediaInfoMap) {

        String content = "";
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost post = new HttpPost(serverURL + "?");
            httpClient.getParams().setParameter("http.socket.timeout", 60 * 60 * 1000);
            MultipartEntity mpEntity = new MultipartEntity();
            List<String> keys = new ArrayList<String>(mediaInfoMap.keySet());
            Collections.sort(keys, String.CASE_INSENSITIVE_ORDER);
            for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
                String key = iterator.next();
                if (StringUtils.isNotBlank(mediaInfoMap.get(key))) {
                    mpEntity.addPart(key, new StringBody(mediaInfoMap.get(key)));
                }
            }

            if (targetFile != null && targetFile.exists()) {
                ContentBody contentBody = new FileBody(targetFile);
                mpEntity.addPart("file", contentBody);
            }
            post.setEntity(mpEntity);

            HttpResponse response = httpClient.execute(post);
            content = EntityUtils.toString(response.getEntity());
            httpClient.getConnectionManager().shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("=====RequestUrl==========================\n");
        System.out.println("=====content==========================\n" + content);
        return content.trim();
    }

}
