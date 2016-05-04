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

package cn.vlabs.clb.api.rest;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import cn.vlabs.clb.api.AccessForbidden;
import cn.vlabs.clb.api.CLBConnection;
import cn.vlabs.clb.api.InvalidArgument;
import cn.vlabs.clb.api.ResourceNotFound;
import cn.vlabs.clb.api.UnImplemented;
import cn.vlabs.clb.api.document.ChunkResponse;
import cn.vlabs.clb.api.document.ClbMeta;
import cn.vlabs.clb.api.document.CreateDocInfo;
import cn.vlabs.clb.api.document.CreateInfo;
import cn.vlabs.clb.api.document.DocMetaInfo;
import cn.vlabs.clb.api.document.DocPair;
import cn.vlabs.clb.api.document.DocumentService;
import cn.vlabs.clb.api.document.ExecuteChunkUploadParam;
import cn.vlabs.clb.api.document.MetaInfo;
import cn.vlabs.clb.api.document.PrepareChunkUploadParam;
import cn.vlabs.clb.api.document.UpdateInfo;
import cn.vlabs.clb.api.document.VersionInfo;
import cn.vlabs.rest.IFileSaver;
import cn.vlabs.rest.stream.IResource;
import cn.vlabs.rest.stream.StreamInfo;

public class DocumentServiceImpl implements DocumentService {

	private static final String CREATE_DOCUMENT = "document.create";
	private static final String CREATE_DOCUMENT_JSON = "document.create.doc"; //json格式

	private static final String BATCH_CREATE = "document.batchcreate";

	private static final String DELETE_DOCUMENT = "document.delete";

	private static final String GET_DOCUMENT = "document.getcontent";

	private static final String GET_DOCID_MD5 = "document.docid.md5";

	private static final String CREATE_CLB_REF = "document.create.clb.ref";

	private static final String PACKAGE_DOCUMENT = "document.package";

	private static final String GET_HISTORY = "document.history";

	private static final String GET_META = "document.meta.docid";
	private static final String GET_DOC_META = "document.meta"; //json格式

	private static final String GET_META_HASH = "document.meta.hash";

	private static final String UPDATE_DOCUMENT = "document.update";

	private static final String GET_DIRECT_URL = "document.direct.url";

	private static final String DELETE_DOCUMENT_VERSION = "document.delete.version";

	private static final String QUERY_EMPTY_CHUNK = "document.chunked.upload.query";

	private static final String EXECUTE_CHUNK_UPLOAD = "document.chunked.upload.execute";

	private static final String PREPARE_CHUNK_UPLOAD = "document.chunked.upload.prepare";

	private static final String FINISH_CHUNK_UPLOAD = "document.chunked.upload.finish";

	private CLBConnection conn;
    private Gson mGson;
	
	public DocumentServiceImpl(CLBConnection conn) {
		this.conn = conn;
		this.mGson = new Gson();
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, UpdateInfo> batchCreate(CreateInfo ci, IResource si) {
		return (HashMap<String, UpdateInfo>) conn.sendService(BATCH_CREATE, ci,
				si);
	}

	public UpdateInfo createDocument(CreateInfo ci, IResource resource) {
		return (UpdateInfo) conn.sendService(CREATE_DOCUMENT, ci, resource);
	}
	
	@Override
	public UpdateInfo createDocument(CreateDocInfo ci, IResource si) {
		Object obj = conn.sendService(CREATE_DOCUMENT_JSON, mGson.toJson(ci), si);
		return mGson.fromJson((String) obj,UpdateInfo.class);
	}

	public void delete(int docid) {
		conn.sendService(DELETE_DOCUMENT, Integer.valueOf(docid));
	}

	public void getContent(int docid, IFileSaver fs) {
		DocPair pair = new DocPair(docid);
		conn.sendService(GET_DOCUMENT, pair, fs);
	}

	public void getContent(int docid, String version, IFileSaver fs)
			throws InvalidArgument {
		DocPair pair = new DocPair(docid, version);
		conn.sendService(GET_DOCUMENT, pair, fs);
	}

	public void getContent(int[] docs, IFileSaver fs) {
		if (docs == null || docs.length == 0)
			return;
		DocPair[] pairs = new DocPair[docs.length];
		for (int i = 0; i < docs.length; i++) {
			DocPair pair = new DocPair(docs[i]);
			pairs[i] = pair;
		}
		getContent(pairs, fs);
	}

	public void getContent(DocPair[] pairs, IFileSaver fs) {
		conn.sendService(PACKAGE_DOCUMENT, pairs, fs);
	}

	public VersionInfo[] getHistory(int docid) {
		return (VersionInfo[]) conn.sendService(GET_HISTORY,
				Integer.valueOf(docid));
	}

	public MetaInfo getMeta(int docid) {
		DocPair pair = new DocPair(docid);
		return (MetaInfo) conn.sendService(GET_META, pair);
	}

	public MetaInfo getMeta(int docid, String version) throws InvalidArgument {
		DocPair pair = new DocPair(docid, version);
		return (MetaInfo) conn.sendService(GET_META, pair);
	}
	
	@Override
	public DocMetaInfo getDocMeta(int docid) throws ResourceNotFound,
			AccessForbidden {
		DocPair pair = new DocPair(docid);
		Object obj = conn.sendService(GET_DOC_META, mGson.toJson(pair));
		return mGson.fromJson((String)obj, DocMetaInfo.class);
	}
	
	@Override
	public DocMetaInfo getDocMeta(int docid, String version) throws InvalidArgument {
		DocPair pair = new DocPair(docid, version);
		Object obj = conn.sendService(GET_DOC_META, mGson.toJson(pair));
		return mGson.fromJson((String)obj, DocMetaInfo.class);
	}

	public VersionInfo[] getMeta(String hash) {
		switch (conn.getServerVersion()) {
		case PREVIOUS:
			return (VersionInfo[]) conn.sendService(GET_META_HASH, hash);
		default:
			throw new UnImplemented(
					"This function is deprecated in this version");
		}
	}

	public UpdateInfo update(int docid, String comment, IResource si) {
		UpdateMessage um = new UpdateMessage();
		um.docid = docid;
		um.comment = comment;
		return (UpdateInfo) conn.sendService(UPDATE_DOCUMENT, um, si);
	}

	@Override
	public String getContentURL(int docid, String version) {
		switch (conn.getServerVersion()) {
		case PREVIOUS:
			throw new UnImplemented(
					"This function will be implement in next version");
		default:
			return (String) conn.sendService(GET_DIRECT_URL, new DocPair(docid,
					version));
		}
	}

	@Override
	public int removeDocument(int docid, String version) {
		switch (conn.getServerVersion()) {
		case PREVIOUS:
			throw new UnImplemented(
					"This function will be implement in next version");
		default:
			return (int) conn.sendService(DELETE_DOCUMENT_VERSION, new DocPair(
					docid, version));
		}
	}

	private static final int _16MB =16 * 1024 * 1024;
	private static final int _32MB = 32 * 1024 * 1024;
	private static final int _512MB = 512 * 1024 * 1024;
	private static final int _256MB = 256 * 1024 * 1024;
	private static final int _128MB = 128 * 1024 * 1024;

	@Override
	public ChunkResponse prepareChunkUpload(String filename, String md5,
			long size) {
		PrepareChunkUploadParam cpp = new PrepareChunkUploadParam();
		cpp.setFilename(filename);
		cpp.setMd5(md5);
		cpp.setSize(size);
		int chunkSize = 0;
		if (size < _16MB) {
			chunkSize = (int) size;
		} else if (size >= _16MB && size < _32MB) {
			chunkSize = (1024 - 1) * 1024; // 1023KB
		} else if (size >= _32MB && size < _128MB) {
			chunkSize = (2 * 1024 - 1) * 1024; // 4095KB
		} else if (size >= _128MB && size < _256MB) {
			chunkSize = (4 * 1024 - 1) * 1024; // 4095KB
		} else if (size >= _256MB && size < _512MB) {
			chunkSize = (8 * 1024 - 1) * 1024; // 8MB - 1KB
		} else if (size >= _512MB) {
			chunkSize = (16 * 1024 - 1) * 1024; // 16MB - 1KB
		}
		cpp.setChunkSize(chunkSize);
		return (ChunkResponse) conn.sendService(PREPARE_CHUNK_UPLOAD, cpp);
	}

	@Override
	public ChunkResponse executeChunkUpload(int docid, int chunkedIndex,
			byte[] buf, int numOfBytes) {
		ExecuteChunkUploadParam cup = new ExecuteChunkUploadParam();
		cup.setDocid(docid);
		cup.setIndex(chunkedIndex);
		cup.setLength(numOfBytes);
		StreamInfo info = new StreamInfo();
		info.setFilename("chunk");
		info.setInputStream(new ByteArrayInputStream(buf, 0, numOfBytes));
		info.setLength(numOfBytes);
		info.setMimeType("application/binary");
		return (ChunkResponse) conn
				.sendService(EXECUTE_CHUNK_UPLOAD, cup, info);
	}

	@Override
	public ChunkResponse finishChunkUpload(int docid) {
		return (ChunkResponse) conn.sendService(FINISH_CHUNK_UPLOAD, docid);
	}

	@Override
	public Set<Integer> queryEmptyChunks(int docid) {
		return (Set<Integer>) conn.sendService(QUERY_EMPTY_CHUNK, docid);
	}

	@Override
	public Integer queryClbidForMD5(String md5, long size) {
		ClbMeta meta = new ClbMeta();
		meta.setMd5(md5);
		meta.setSize(size);
		Object obj = conn.sendService(GET_DOCID_MD5, meta);
		if (obj != null) {
			return (Integer) obj;
		}
		return null;
	}

	@Override
	public void createClbRef(int clbid, String fileName, String md5, long size) {
		ClbMeta meta = new ClbMeta();
		meta.setMd5(md5);
		meta.setSize(size);
		meta.setDocid(clbid);
		meta.setFilename(fileName);
		conn.sendService(CREATE_CLB_REF, mGson.toJson(meta));
	}
	
	@Override
	public void createClbRef(ClbMeta meta) {
		conn.sendService(CREATE_CLB_REF, mGson.toJson(meta));
	}
}
