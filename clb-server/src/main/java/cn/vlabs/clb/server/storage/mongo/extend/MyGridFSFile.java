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

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.BSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;

public abstract class MyGridFSFile implements DBObject {

    public void save() {
        if (_fs == null)
            throw new MongoException("need _fs");
        _fs.getFilesCollection().save(this);
    }

    public void validate() {
        if (_fs == null)
            throw new MongoException("no _fs");
        if (_md5 == null)
            throw new MongoException("no _md5 stored");

        DBObject cmd = new BasicDBObject("filemd5", _id);
        cmd.put("root", _fs._bucketName);
        DBObject res = _fs._db.command(cmd);
        if (res != null && res.containsField("md5")) {
            String m = res.get("md5").toString();
            if (m.equals(_md5))
                return;
            throw new MongoException("md5 differ.  mine [" + _md5 + "] theirs [" + m + "]");
        }

        // no md5 from the server
        throw new MongoException("no md5 returned from server: " + res);

    }

    public int numChunks() {
        double d = _length;
        d = d / _chunkSize;
        return (int) Math.ceil(d);
    }

    // ------------------------------
    // --------- getters -------
    // ------------------------------

    public Object getId() {
        return _id;
    }

    public String getFilename() {
        return _filename;
    }

    public String getContentType() {
        return _contentType;
    }

    public long getLength() {
        return _length;
    }

    public long getChunkSize() {
        return _chunkSize;
    }

    public Date getUploadDate() {
        return _uploadDate;
    }

    public List<String> getAliases() {
        return (List<String>) _extradata.get("aliases");
    }

    public DBObject getMetaData() {
        return (DBObject) _extradata.get("metadata");
    }

    public void setMetaData(DBObject metadata) {
        _extradata.put("metadata", metadata);
    }

    public String getMD5() {
        return _md5;
    }

    // ------------------------------
    // --------- DBOBject methods ---
    // ------------------------------
    public Object put(String key, Object v) {
        if (key == null)
            throw new RuntimeException("key should never be null");
        else if (key.equals("_id"))
            _id = v;
        else if (key.equals("filename"))
            _filename = v == null ? null : v.toString();
        else if (key.equals("contentType"))
            _contentType = (String) v;
        else if (key.equals("length"))
            _length = ((Number) v).longValue();
        else if (key.equals("chunkSize"))
            _chunkSize = ((Number) v).longValue();
        else if (key.equals("uploadDate"))
            _uploadDate = (Date) v;
        else if (key.equals("md5"))
            _md5 = (String) v;
        else
            _extradata.put(key, v);
        return v;
    }

    public Object get(String key) {
        if (key == null)
            throw new RuntimeException("key should never be null");
        else if (key.equals("_id"))
            return _id;
        else if (key.equals("filename"))
            return _filename;
        else if (key.equals("contentType"))
            return _contentType;
        else if (key.equals("length"))
            return _length;
        else if (key.equals("chunkSize"))
            return _chunkSize;
        else if (key.equals("uploadDate"))
            return _uploadDate;
        else if (key.equals("md5"))
            return _md5;
        return _extradata.get(key);
    }

    public void putAll(BSONObject o) {
        throw new UnsupportedOperationException();
    }

    public void putAll(Map m) {
        throw new UnsupportedOperationException();
    }

    public Map toMap() {
        throw new UnsupportedOperationException();
    }

    public Object removeField(String key) {
        throw new UnsupportedOperationException();
    }

    public boolean containsKey(String s) {
        return containsField(s);
    }

    public boolean containsField(String s) {
        return keySet().contains(s);
    }

    public Set<String> keySet() {
        Set<String> keys = new HashSet();
        keys.addAll(VALID_FIELDS);
        keys.addAll(_extradata.keySet());
        return keys;
    }

    public boolean isPartialObject() {
        return false;
    }

    public void markAsPartialObject() {
        throw new RuntimeException("can't load partial GridFSFile file");
    }

    @Override
    public String toString() {
        return JSON.serialize(this);
    }

    protected MyGridFS getMyGridFS() {
        return this._fs;
    }

    protected void setMyGridFS(MyGridFS fs) {
        _fs = fs;
    }

    protected MyGridFS _fs = null;

    protected Object _id;
    protected String _filename;
    protected String _contentType;
    protected long _length;
    protected long _chunkSize;
    protected Date _uploadDate;
    protected List<String> _aliases;
    protected DBObject _extradata = new BasicDBObject();
    protected String _md5;

    final static Set<String> VALID_FIELDS = Collections.unmodifiableSet(new HashSet(Arrays.asList(new String[] { "_id",
            "filename", "contentType", "length", "chunkSize", "uploadDate", "aliases", "md5" })));
}
