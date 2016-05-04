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

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import cn.vlabs.clb.api.io.impl.MimeType;
import cn.vlabs.clb.server.storage.mongo.MFile;
import cn.vlabs.clb.server.storage.mongo.MTrivialFile;
import cn.vlabs.clb.server.storage.mongo.MongoStorageService;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.gridfs.GridFSDBFile;

public class MongoServiceTest {

    private static MongoStorageService ms;

    @BeforeClass
    public static void setup() {
        ms = new MongoStorageService("10.10.1.162", 27017, "docs");
    }
    
//    @Test
    public void testForNotExist(){
        try {
            Mongo client = new MongoClient("10.10.1.154", 27018);
            DB db = client.getDB("docs");
            DBCollection c_chunks = db.getCollection("fs.chunks");
            BasicDBObject filter = new BasicDBObject();
            filter.put("files_id", 1);
            DBCursor rs = c_chunks.find(new BasicDBObject(),filter);
            HashSet<String> set = new HashSet<String>();
            System.out.println("Begin to read");
            while(rs.hasNext()){
                DBObject obj = rs.next();
                set.add(obj.get("files_id").toString());
                System.out.println(set.size());
            }
            System.out.println("init finish for set.");
            DBCollection c_files = db.getCollection("fs.files");
            DBCursor rs2 = c_files.find(new BasicDBObject());
            int index = 0;
            while(rs2.hasNext()){
                DBObject obj = rs2.next();
                if(!set.contains(obj.get("_id").toString())){
                    System.out.println(obj.get("docid"));
                }
                System.out.println(index++);
            }
        } catch (UnknownHostException | MongoException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void testGenerateStorageKey() {
        String key1 = ms.generateStorageKey();
        String key2 = ms.generateStorageKey();
        String key3 = ms.generateStorageKey();
        System.out.println(key1);
        System.out.println(key2);
        System.out.println(key3);
    }

    // public void testRemoveCollection() {
    // ms.removeCollection("storage_key");
    // }

    public void testWriteFile() throws IOException {
        String key = ms.generateStorageKey();
        MFile mf = new MFile();
        String filename = "fuck.jpg";
        String suffix = MimeType.getSuffix(filename);
        mf.setAppid(1);
        mf.setFilename(filename);
        mf.setContentType(MimeType.getContentType(suffix));
        mf.setFileExtension(suffix);
        mf.setStorageKey(new ObjectId(key));
        Resource res = new ClassPathResource("files/normal-size.jpg");
        File f = res.getFile();
        InputStream ins;
        try {
            ins = new FileInputStream(f);
            ms.writeImage(ins, mf);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadFile() {
        String key = "5481a667047e7758d3b9d1f1";
        GridFSDBFile grfile = ms.loadImage(key);
        System.out.println(grfile.getFilename() + "," + grfile.getLength());
    }

    public void testWriteTrivialFile() throws IOException {
        // ms.removeCollection("trivial.files");
        String spaceName = ms.generateStorageKey();
        Resource rootDir = new ClassPathResource("trivial/resources");
        List<File> fileList = scanFile(rootDir.getFile());
        String prefix = rootDir.getFile().getPath();
        for (File f : fileList) {
            saveTrivialFileIntoMongo(f, prefix, spaceName);
        }
    }

    public void readTrivialFile() {
        String spaceName = "516d97bee4b014e06e6f7959";
        GridFSDBFile grfile = ms.loadTrivial(spaceName, "skin.css");
        System.out.println(grfile.getFilename() + "," + grfile.getLength() + "," + grfile.getUploadDate());
        File f = new File("/tmp/skin.css");
        try {
            grfile.writeTo(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<File> scanFile(File root) {
        List<File> fileInfo = new ArrayList<File>();
        File[] files = root.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                if (pathname.isDirectory() && pathname.isHidden()) {
                    return false;
                }
                if (pathname.isFile() && pathname.isHidden()) {
                    return false;
                }
                return true;
            }
        });

        for (File file : files) {
            if (file.isDirectory()) {
                List<File> ff = scanFile(file);
                fileInfo.addAll(ff);
            } else {
                fileInfo.add(file);
            }
        }

        return fileInfo;
    }

    private String parseContentType(String name) {
        int lastIndex = name.lastIndexOf('.');
        if (lastIndex > 0) {
            return name.substring(lastIndex + 1);
        }
        return null;
    }

    private MTrivialFile saveTrivialFileIntoMongo(File f, String prefix, String spaceName) {
        MTrivialFile mtf = new MTrivialFile();
        mtf.setFileName(getRelativePath(f.getPath(), prefix));
        mtf.setStorageKey(new ObjectId(ms.generateStorageKey()));
        mtf.setSpaceName(new ObjectId(spaceName));
        mtf.setSize(f.length());
        mtf.setContentType(parseContentType(f.getName()));
        InputStream ins;
        try {
            ins = new FileInputStream(f);
            ms.writeTrivial(ins, mtf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mtf;
    }

    private String getRelativePath(String path, String prefix) {
        return path.replace(prefix + "/", "");
    }

}
