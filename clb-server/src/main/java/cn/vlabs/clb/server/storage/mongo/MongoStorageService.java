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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import cn.vlabs.clb.server.storage.IStorageService;
import cn.vlabs.clb.server.storage.mongo.extend.MyGridFS;
import cn.vlabs.clb.server.storage.mongo.extend.MyGridFSInputFile;
import cn.vlabs.clb.server.ui.frameservice.DPair;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

public class MongoStorageService implements IStorageService {

	public static final String TABLE_TEMP_KEY = "clb_temp_key";
	public static final String TABLE_PDF = "clb_pdf";
	public static final String TABLE_DOCUMENT = "fs";
	public static final String TABLE_TRIVIAL = "clb_trivial";
	public static final String TABLE_IMAGE = "clb_image";

	public static final String FIELD_STORAGE_KEY = "storageKey";
	public static final String FIELD_SPACE_NAME = "spaceName";
	public static final String FIELD_FILE_NAME = "filename";
	public static final String FIELD_FILE_TYPE = "contentType";
	public static final String FIELD_FILE_SIZE = "length";
	public static final String FIELD_APP_ID = "appid";
	public static final String FIELD_DOC_ID = "docid";
	public static final String FILED_VERSION_ID = "vid";
	public static final String FILED_IS_PUB = "isPub";

	private MongoClient client;

	private MongoOperations options;

	public MongoStorageService(String host, int port, String dbName) {
		try {
			client = new MongoClient(host, port);
		} catch (UnknownHostException | MongoException e) {
			e.printStackTrace();
		}
		if (client != null) {
			this.options = new MongoTemplate(client, dbName);
		}
	}

	public void close() {
		if (client != null) {
			client.close();
			MongoTemplate t = (MongoTemplate) options;
			DB db = t.getDb();
			if (db != null) {
				Mongo m = db.getMongo();
				if (m != null) {
					m.close();
				}
			}
		}
	}

	@Override
	public String generateStorageKey() {
		return new ObjectId().toString();
	}

	@Override
	public GridFSDBFile loadDocument(DPair p) {
		return readFile(p.getAppid(), p.getDocid(), p.getVersion() + "",
				TABLE_DOCUMENT);
	}

	@Override
	public GridFSDBFile loadDocument(String storageKey) {
		return readFile(storageKey, TABLE_DOCUMENT);
	}

	@Override
	public GridFSDBFile loadImage(String storageKey) {
		return readFile(storageKey, TABLE_IMAGE);
	}

	@Override
	public GridFSDBFile loadPdf(String storageKey) {
		return readFile(storageKey, TABLE_PDF);
	}

	@Override
	public GridFSDBFile loadTrivial(String spaceName, String filename) {
		return readTrivialFile(spaceName, filename, TABLE_TRIVIAL);
	}

	@Override
	public void writeTrivial(InputStream content, MTrivialFile meta) {
		writeTrivialFile(content, meta, TABLE_TRIVIAL);
	}

	@Override
	public void writeDocument(InputStream content, MFile meta) {
		writeFile(content, meta, TABLE_DOCUMENT);
	}

	@Override
	public void writeImage(InputStream content, MFile meta) {
		writeFile(content, meta, TABLE_IMAGE);
	}

	@Override
	public void writePdf(InputStream content, MFile meta) {
		writeFile(content, meta, TABLE_PDF);
	}

	protected void removeCollection(String tableName) {
		options.remove(new Query(), tableName);
	}

	protected void deleteByStorageKey(String storageKey, String tableName) {
		options.remove(getQueryByStorageKey(storageKey), tableName);
	}

	public FileMeta findDocument(String storageKey) {
		return findFileMeta(storageKey, TABLE_DOCUMENT);
	}

	public FileMeta findImage(String storageKey) {
		return findFileMeta(storageKey, TABLE_IMAGE);
	}

	public FileMeta findPdf(String storageKey) {
		return findFileMeta(storageKey, TABLE_PDF);
	}

	public MTrivialFile findTrivial(String spaceName, String fileName) {
		return findTrivialFile(spaceName, fileName);
	}

	private FileMeta findFileMeta(String storageKey, String tableName) {
		DB db = options.getCollection(TABLE_TEMP_KEY).getDB();
		DBObject query = new BasicDBObject();
		query.put(FIELD_STORAGE_KEY, new ObjectId(storageKey));
		DBCollection c = db.getCollection(tableName + ".files");
		DBObject r = c.findOne(query);
		if (r != null) {
			FileMeta fm = new FileMeta();
			fm.setId(r.get("_id").toString());
			fm.setAppid((int) r.get("appid"));
			fm.setContentType((String) r.get("contentType"));
			fm.setFilename((String) r.get("filename"));
			fm.setLength((long) r.get("length"));
			fm.setDocid((int) r.get("docid"));
			fm.setMd5((String) r.get("md5"));
			fm.setStorageKey(new ObjectId(r.get("storageKey").toString()));
			fm.setUploadDate((Date) r.get("uploadDate"));
			fm.setVid((String) r.get("vid"));
			return fm;
		}
		return null;
	}

	protected MFile findFile(String storageKey, String tableName) {
		return options.findOne(getQueryByStorageKey(storageKey), MFile.class,
				tableName);
	}

	protected MTrivialFile findTrivialFile(String spaceName, String fileName) {
		return options.findOne(getTrivialFileQuery(spaceName, fileName),
				MTrivialFile.class, TABLE_TRIVIAL);
	}

	protected GridFSDBFile readFile(String storageKey, String tableName) {
		DB db = options.getCollection(TABLE_TEMP_KEY).getDB();
		DBObject query = new BasicDBObject();
		query.put(FIELD_STORAGE_KEY, new ObjectId(storageKey));
		GridFSDBFile gfsFile = new GridFS(db, tableName).findOne(query);
		return gfsFile;
	}

	protected GridFSDBFile readFile(int appid, int docid, String vid,
			String tableName) {
		DB db = options.getCollection(TABLE_TEMP_KEY).getDB();
		db.requestStart();
		DBObject query = new BasicDBObject();
		query.put(FIELD_DOC_ID, docid);
		query.put(FILED_VERSION_ID, vid);
		query.put(FIELD_APP_ID, appid);
		GridFSDBFile gfsFile = new GridFS(db, tableName).findOne(query);
		db.requestEnsureConnection();
		db.requestDone();
		return gfsFile;
	}

	protected GridFSDBFile readTrivialFile(String spaceName, String fileName,
			String tableName) {
		DB db = options.getCollection(TABLE_TEMP_KEY).getDB();
		DBObject query = new BasicDBObject();
		query.put(FIELD_SPACE_NAME, new ObjectId(spaceName));
		query.put(FIELD_FILE_NAME, fileName);
		GridFSDBFile gfsFile = new GridFS(db, tableName).findOne(query);
		return gfsFile;
	}

	protected void writeFile(InputStream ins, MFile mf, String tableName) {
		DB db = options.getCollection(TABLE_TEMP_KEY).getDB();
		db.requestStart();
		GridFSInputFile gfsInput;
		gfsInput = new GridFS(db, tableName).createFile(ins);
		DBCollection col = db.getCollection(tableName + ".files");
		col.setWriteConcern(WriteConcern.SAFE);
		gfsInput.setFilename(mf.getFilename());
		gfsInput.put(FIELD_STORAGE_KEY, mf.getStorageKey());
		gfsInput.put(FIELD_DOC_ID, mf.getDocid());
		gfsInput.put(FILED_VERSION_ID, mf.getVid());
		gfsInput.put(FIELD_APP_ID, mf.getAppid());
		gfsInput.put(FILED_IS_PUB, getIsPubStatus(mf.getIsPub()));
		gfsInput.setContentType(mf.getContentType());
		gfsInput.save();
		db.requestDone();
	}

	private String getIsPubStatus(int isPub) {
		return (isPub == 1) ? "true" : "false";
	}

	protected void writeTrivialFile(InputStream ins, MTrivialFile mtf,
			String tableName) {
		DB db = options.getCollection(TABLE_TEMP_KEY).getDB();
		db.requestStart();
		GridFSInputFile gfsInput;
		DBCollection col = db.getCollection(tableName + ".files");
		col.setWriteConcern(WriteConcern.SAFE);
		gfsInput = new GridFS(db, tableName).createFile(ins);
		gfsInput.setFilename(mtf.getFileName());
		gfsInput.put(FIELD_STORAGE_KEY, mtf.getStorageKey());
		gfsInput.put(FIELD_SPACE_NAME, mtf.getSpaceName());
		gfsInput.setContentType(mtf.getContentType());
		gfsInput.save();
		db.requestDone();
	}

	private Query getTrivialFileQuery(String spaceName, String fileName) {
		Query query = new Query();
		query.addCriteria(Criteria.where(FIELD_SPACE_NAME).is(
				new ObjectId(spaceName)));
		query.addCriteria(Criteria.where(FIELD_FILE_NAME).is(fileName));
		return query;
	}

	private Query getQueryByStorageKey(String storageKey) {
		return new Query(Criteria.where(FIELD_STORAGE_KEY).is(
				new ObjectId(storageKey)));
	}

	@Override
	public void removeDocument(String storageKey) {
		this.deleteMetaAndContent(storageKey, TABLE_DOCUMENT);
	}

	@Override
	public void removeTrivial(String spaceName) {
		DB db = options.getCollection(TABLE_TEMP_KEY).getDB();
		DBObject query = new BasicDBObject();
		query.put(FIELD_SPACE_NAME, new ObjectId(spaceName));
		GridFS gfs = new GridFS(db, TABLE_TRIVIAL);
		gfs.remove(query);
	}

	@Override
	public GridFSDBFile loadPdf(int appid, int docid, String vid) {
		return readFile(appid, docid, vid, TABLE_PDF);
	}

	@Override
	public void removeImage(String storageKey) {
		this.deleteMetaAndContent(storageKey, TABLE_IMAGE);
	}

	@Override
	public void removePdf(String storageKey) {
		this.deleteMetaAndContent(storageKey, TABLE_PDF);
	}

	@Override
	public Set<Integer> queryDocumentChunkSet(String storageKey) {
		return this.queryChunkMap(storageKey, TABLE_DOCUMENT);
	}

	private void deleteMetaAndContent(String storageKey, String tableName) {
		DB db = options.getCollection(TABLE_TEMP_KEY).getDB();
		DBObject query = new BasicDBObject();
		query.put(FIELD_STORAGE_KEY, new ObjectId(storageKey));
		GridFS gfs = new GridFS(db, tableName);
		gfs.remove(query);
	}

	private static final int EOF = -1;
	private static final int BUF_SIZE = 4096;

	private Set<Integer> queryChunkMap(String storageKey, String tableName) {
		DB db = options.getCollection(TABLE_TEMP_KEY).getDB();
		DBObject query = new BasicDBObject();
		query.put("storageKey", new ObjectId(storageKey));
		Set<Integer> result = new HashSet<Integer>();
		DBCollection chunkTable = db.getCollection(tableName + ".chunks");
		DBObject ref = new BasicDBObject();
		ref.put("files_id", new ObjectId(storageKey));
		DBObject keys = new BasicDBObject();
		keys.put("n", 1);
		DBCursor cursor = chunkTable.find(ref, keys);
		while (cursor.hasNext()) {
			DBObject o = cursor.next();
			result.add(Integer.parseInt(o.get("n").toString()));
		}
		return result;
	}

	@Override
	public void chunkedWriteDocument(int chunkIndex, int totalChunk,
			InputStream data, int length, MFile mf, int chunkSize) {
		chunkedUpload(chunkIndex, totalChunk, data, length, mf, TABLE_DOCUMENT,
				chunkSize);
	}

	private void chunkedUpload(int chunkIndex, int chunkCount,
			InputStream data, int dataLength, MFile mf, String tableName,
			int chunkSize) {
		DB db = options.getCollection(TABLE_TEMP_KEY).getDB();
		db.requestStart();
		MyGridFS gfs = new MyGridFS(db, tableName);
		DBCollection col = db.getCollection(tableName + ".files");
		col.setWriteConcern(WriteConcern.SAFE);
		MyGridFSInputFile inf = new MyGridFSInputFile(gfs);
		inf.setFilename(mf.getFilename());
		inf.setId(mf.getStorageKey());
		inf.put(FIELD_STORAGE_KEY, mf.getStorageKey());
		inf.put(FIELD_DOC_ID, mf.getDocid());
		inf.put(FILED_VERSION_ID, mf.getVid());
		inf.put(FIELD_APP_ID, mf.getAppid());
		inf.put(FILED_IS_PUB, getIsPubStatus(mf.getIsPub()));
		inf.setContentType(mf.getContentType());
		inf.setChunkSize(chunkSize);
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
				System.out.println("Missing Data expected data-size="
						+ dataLength + ", actual read data-size=" + numBytes);
				return;
			}
			inf.setInputStream(new ByteArrayInputStream(bigBuf, 0, numBytes));
			inf.setSavedChunk(false);
			inf.setCurrentChunkNumber(chunkIndex);
			inf.saveChunks(chunkSize);
			System.out.println("Chunk " + chunkIndex + " upload finish, bytes "
					+ numBytes);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
		if (chunkIndex == (chunkCount - 1)) {
			inf.setSavedChunk(true);
			inf.save();
		}
		db.requestDone();
	}

	@Override
	public String queryMd5(String storageKey) {
		return this.queryMd5(storageKey, TABLE_DOCUMENT);
	}

	public String queryMd5(String storageKey,String tableName) {
		DB db = options.getCollection(TABLE_TEMP_KEY).getDB();
		DBObject query = new BasicDBObject();
		query.put("storageKey", new ObjectId(storageKey));
		DBCollection fileTable = db.getCollection(tableName + ".files");
		DBCursor cursor = fileTable.find(query);
		if (cursor.hasNext()) {
			DBObject o = cursor.next();
			return o.get("md5").toString();
		}
		return "";
	}

}
