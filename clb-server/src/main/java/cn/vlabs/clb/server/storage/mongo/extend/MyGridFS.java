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
package cn.vlabs.clb.server.storage.mongo.extend;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.gridfs.GridFSDBFile;

public class MyGridFS {
    private static final Logger LOGGER = Logger.getLogger("com.mongodb.gridfs");

    /**
     * file's chunk size
     */
    public static final int DEFAULT_CHUNKSIZE = 255 * 1024;

    /**
     * file's max chunk size
     * 
     * You can calculate max chunkSize with a similar formula
     * {@link com.mongodb.MongoClient#getMaxBsonObjectSize()} - 500*1000. Please
     * ensure that you left enough space for metadata (500kb is enough).
     */
    public static final long MAX_CHUNKSIZE = (long) (15.5 * 1024 * 1024);

    /**
     * bucket to use for the collection namespaces
     */
    public static final String DEFAULT_BUCKET = "fs";

    // --------------------------
    // ------ constructors -------
    // --------------------------

    public MyGridFS(DB db) {
        this(db, DEFAULT_BUCKET);
    }

    public MyGridFS(DB db, String bucket) {
        _db = db;
        _bucketName = bucket;

        _filesCollection = _db.getCollection(_bucketName + ".files");
        _chunkCollection = _db.getCollection(_bucketName + ".chunks");

        // ensure standard indexes as long as collections are small
        try {
            if (_filesCollection.count() < 1000) {
                _filesCollection
                        .ensureIndex(BasicDBObjectBuilder.start().add("filename", 1).add("uploadDate", 1).get());
            }
            if (_chunkCollection.count() < 1000) {
                _chunkCollection.ensureIndex(BasicDBObjectBuilder.start().add("files_id", 1).add("n", 1).get(),
                        BasicDBObjectBuilder.start().add("unique", true).get());
            }
        } catch (MongoException e) {
            LOGGER.info(String.format("Unable to ensure indices on GridFS collections in database %s", db.getName()));
        }
        _filesCollection.setObjectClass(GridFSDBFile.class);
    }

    // --------------------------
    // ------ utils -------
    // --------------------------

    public DBCursor getFileList() {
        return getFileList(new BasicDBObject());
    }

    public DBCursor getFileList(DBObject query) {
        return getFileList(query, new BasicDBObject("filename", 1));
    }

    public DBCursor getFileList(DBObject query, DBObject sort) {
        return _filesCollection.find(query).sort(sort);
    }

    // --------------------------
    // ------ reading -------
    // --------------------------

    public MyGridFSDBFile find(ObjectId id) {
        return findOne(id);
    }

    public MyGridFSDBFile findOne(ObjectId id) {
        return findOne(new BasicDBObject("_id", id));
    }

    public MyGridFSDBFile findOne(String filename) {
        return findOne(new BasicDBObject("filename", filename));
    }

    public MyGridFSDBFile findOne(DBObject query) {
        return _fix(_filesCollection.findOne(query));
    }

    public List<MyGridFSDBFile> find(String filename) {
        return find(filename, null);
    }

    public List<MyGridFSDBFile> find(String filename, DBObject sort) {
        return find(new BasicDBObject("filename", filename), sort);
    }

    public List<MyGridFSDBFile> find(DBObject query) {
        return find(query, null);
    }

    public List<MyGridFSDBFile> find(DBObject query, DBObject sort) {
        List<MyGridFSDBFile> files = new ArrayList<MyGridFSDBFile>();

        DBCursor c = null;
        try {
            c = _filesCollection.find(query);
            if (sort != null) {
                c.sort(sort);
            }
            while (c.hasNext()) {
                files.add(_fix(c.next()));
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return files;
    }

    /**
     * @deprecated This method is NOT a part of public API and will be dropped
     *             in 3.x versions.
     */
    @Deprecated
    protected MyGridFSDBFile _fix(Object o) {
        if (o == null)
            return null;

        if (!(o instanceof GridFSDBFile))
            throw new RuntimeException("somehow didn't get a GridFSDBFile");

        MyGridFSDBFile f = (MyGridFSDBFile) o;
        f._fs = this;
        return f;
    }

    // --------------------------
    // ------ remove -------
    // --------------------------

    public void remove(ObjectId id) {
        if (id == null) {
            throw new IllegalArgumentException("file id can not be null");
        }
        _filesCollection.remove(new BasicDBObject("_id", id));
        _chunkCollection.remove(new BasicDBObject("files_id", id));
    }

    public void remove(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("filename can not be null");
        }
        remove(new BasicDBObject("filename", filename));
    }

    public void remove(DBObject query) {
        if (query == null) {
            throw new IllegalArgumentException("query can not be null");
        }
        for (MyGridFSDBFile f : find(query)) {
            f.remove();
        }
    }

    public MyGridFSInputFile createFile(byte[] data) {
        return createFile(new ByteArrayInputStream(data), true);
    }

    public MyGridFSInputFile createFile(File f) throws IOException {
        return createFile(new FileInputStream(f), f.getName(), true);
    }

    public MyGridFSInputFile createFile(InputStream in) {
        return createFile(in, null);
    }

    public MyGridFSInputFile createFile(InputStream in, boolean closeStreamOnPersist) {
        return createFile(in, null, closeStreamOnPersist);
    }

    public MyGridFSInputFile createFile(InputStream in, String filename) {
        return new MyGridFSInputFile(this, in, filename);
    }

    public MyGridFSInputFile createFile(InputStream in, String filename, boolean closeStreamOnPersist) {
        return new MyGridFSInputFile(this, in, filename, closeStreamOnPersist);
    }

    public MyGridFSInputFile createFile(String filename) {
        return new MyGridFSInputFile(this, filename);
    }

    public MyGridFSInputFile createFile() {
        return new MyGridFSInputFile(this);
    }

    public String getBucketName() {
        return _bucketName;
    }

    public DB getDB() {
        return _db;
    }

    protected DBCollection getFilesCollection() {
        return _filesCollection;
    }

    protected DBCollection getChunksCollection() {
        return _chunkCollection;
    }

    protected final DB _db;

    protected final String _bucketName;

    protected final DBCollection _filesCollection;

    protected final DBCollection _chunkCollection;

}
