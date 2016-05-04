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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.gridfs.GridFS;

public class MyGridFSInputFile extends MyGridFSFile {

    public MyGridFSInputFile(MyGridFS fs, InputStream in, String filename, boolean closeStreamOnPersist) {
        _fs = fs;
        _in = in;
        _filename = filename;
        _closeStreamOnPersist = closeStreamOnPersist;

        _id = new ObjectId();
        _chunkSize = GridFS.DEFAULT_CHUNKSIZE;
        _uploadDate = new Date();
//        try {
//            _messageDigester = MessageDigest.getInstance("MD5");
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException("No MD5!");
//        }
////        _messageDigester.reset();
        _buffer = new byte[(int) _chunkSize];
    }

    protected MyGridFSInputFile(MyGridFS fs, InputStream in, String filename) {
        this(fs, in, filename, true);
    }

    protected MyGridFSInputFile(MyGridFS fs, String filename) {
        this(fs, null, filename);
    }

    public MyGridFSInputFile(MyGridFS fs) {
        this(fs, null, null);
    }

    public void setId(Object id) {
        _id = id;
    }

    public void setFilename(String fn) {
        _filename = fn;
    }

    public void setContentType(String ct) {
        _contentType = ct;
    }

    public void setChunkSize(long chunkSize) {
        if (_outputStream != null || _savedChunks)
            return;
        _chunkSize = chunkSize;
        _buffer = new byte[(int) _chunkSize];
    }

    @Override
    public void save() {
//        _md5 = Util.toHex(_messageDigester.digest());
//        System.out.println(_md5 );
//        this._messageDigester = null;
        save(_chunkSize);
    }

    public void save(long chunkSize) {
        if (_outputStream != null)
            throw new MongoException("cannot mix OutputStream and regular save()");

        if (!_savedChunks) {
            try {
                saveChunks(chunkSize);
            } catch (IOException ioe) {
                throw new MongoException("couldn't save chunks", ioe);
            }
        }

        super.save();
    }

    private int _currentChunkNumber = 0;

    public void setCurrentChunkNumber(int index) {
        _currentChunkNumber = index;
    }

    public int saveChunks(long chunkSize) throws IOException {
        if (_outputStream != null)
            throw new MongoException("cannot mix OutputStream and regular save()");

        if (chunkSize <= 0) {
            throw new MongoException("chunkSize must be greater than zero");
        }

        if (_chunkSize != chunkSize) {
            _chunkSize = chunkSize;
            _buffer = new byte[(int) _chunkSize];
        }

        int bytesRead = 0;
        while (bytesRead >= 0) {
            _currentBufferPosition = 0;
            bytesRead = _readStream2Buffer();
            _dumpBuffer(_currentChunkNumber, true);
        }

        _finishChunkData();
        return _currentChunkNumber + 1;
    }

    public OutputStream getOutputStream() {
        if (_outputStream == null) {
            _outputStream = new MyOutputStream();
        }
        return _outputStream;
    }

    private void _dumpBuffer(int currentChunkNumber, boolean writePartial) {
        if ((_currentBufferPosition < _chunkSize) && !writePartial) {
            return;
        }
        if (_currentBufferPosition == 0) {// chunk is empty, may be last chunk
            return;
        }

        byte[] writeBuffer = _buffer;
        if (_currentBufferPosition != _chunkSize) {
            writeBuffer = new byte[_currentBufferPosition];
            System.arraycopy(_buffer, 0, writeBuffer, 0, _currentBufferPosition);
        }

        DBObject chunk = createChunk(_id, currentChunkNumber, writeBuffer);
        _fs._chunkCollection.save(chunk);
//        _totalBytes += writeBuffer.length;
//        _messageDigester.update(writeBuffer);
        _currentBufferPosition = 0;
    }

    protected DBObject createChunk(Object id, int currentChunkNumber, byte[] writeBuffer) {
        return BasicDBObjectBuilder.start().add("files_id", id).add("n", currentChunkNumber).add("data", writeBuffer)
                .get();
    }

    private int _readStream2Buffer() throws IOException {
        int bytesRead = 0;
        while (_currentBufferPosition < _chunkSize && bytesRead >= 0) {
            bytesRead = _in.read(_buffer, _currentBufferPosition, (int) _chunkSize - _currentBufferPosition);
            if (bytesRead > 0) {
                _currentBufferPosition += bytesRead;
            } else if (bytesRead == 0) {
                throw new RuntimeException("i'm doing something wrong");
            }
        }
        return bytesRead;
    }

    private void _finishChunkData() {
//        _length = _totalBytes;
//        System.out.println( _length);
        try {
            if (_in != null && _closeStreamOnPersist) {
                _in.close();
//                System.out.println("Close current inputstream, please assign a new inputstream.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setInputStream(InputStream ins) {
        this._in = ins;
    }

    private InputStream _in;
    private boolean _closeStreamOnPersist;
    private boolean _savedChunks = false;
    private byte[] _buffer = null;
    private int _currentBufferPosition = 0;
//    private long _totalBytes = 0;
//    private MessageDigest _messageDigester = null;
    private OutputStream _outputStream = null;

    public int appendChunk() {
        return 0;
    }

    public void setMD5(String md5) {
        super._md5 = md5;
    }
    
    public void setLength(long length){
        this._length = length;
    }

    public void commitFileUpload(String filename, String contentType, String md5, long length) {
        this.setFilename(filename);
        this.setContentType(contentType);
        this._md5 = md5;
        this._length = length;
    }

    class MyOutputStream extends OutputStream {

        private int currentChunkNumber = 0;

        @Override
        public void write(int b) throws IOException {
            byte[] byteArray = new byte[1];
            byteArray[0] = (byte) (b & 0xff);
            write(byteArray, 0, 1);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            int offset = off;
            int length = len;
            int toCopy = 0;
            while (length > 0) {
                toCopy = length;
                if (toCopy > _chunkSize - _currentBufferPosition) {
                    toCopy = (int) _chunkSize - _currentBufferPosition;
                }
                System.arraycopy(b, offset, _buffer, _currentBufferPosition, toCopy);
                _currentBufferPosition += toCopy;
                offset += toCopy;
                length -= toCopy;
                if (_currentBufferPosition == _chunkSize) {
                    _dumpBuffer(currentChunkNumber, false);
                    currentChunkNumber++;
                }
            }
        }

        @Override
        public void close() {
            // write last buffer if needed
            _dumpBuffer(currentChunkNumber, true);
            // finish stream
            _finishChunkData();
            // save file obj
            MyGridFSInputFile.super.save();
        }
    }

    public void setSavedChunk(boolean b) {
        this._savedChunks = b;
    }

}
