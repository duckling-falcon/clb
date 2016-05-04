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

package cn.vlabs.clb.server.ui.frameservice.document;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.print.Doc;

import net.duckling.falcon.api.idg.IIDGeneratorService;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.vlabs.clb.api.document.ChunkResponse;
import cn.vlabs.clb.api.document.ClbMeta;
import cn.vlabs.clb.api.document.DocPair;
import cn.vlabs.clb.api.document.ExecuteChunkUploadParam;
import cn.vlabs.clb.api.document.PrepareChunkUploadParam;
import cn.vlabs.clb.api.document.VersionInfo;
import cn.vlabs.clb.api.io.impl.MimeType;
import cn.vlabs.clb.server.CurrentConnection;
import cn.vlabs.clb.server.config.Config;
import cn.vlabs.clb.server.exception.FileContentNotFoundException;
import cn.vlabs.clb.server.exception.InvalidArgumentException;
import cn.vlabs.clb.server.exception.InvalidFileOperationException;
import cn.vlabs.clb.server.exception.MetaNotFoundException;
import cn.vlabs.clb.server.model.DocChunkStatus;
import cn.vlabs.clb.server.model.DocMeta;
import cn.vlabs.clb.server.model.DocRef;
import cn.vlabs.clb.server.model.DocVersion;
import cn.vlabs.clb.server.service.document.IDocChunkStatusService;
import cn.vlabs.clb.server.service.document.IDocMetaService;
import cn.vlabs.clb.server.service.document.IDocRefService;
import cn.vlabs.clb.server.service.document.IDocVersionService;
import cn.vlabs.clb.server.service.search.fulltext.parser.ParserReadable;
import cn.vlabs.clb.server.storage.IStorageService;
import cn.vlabs.clb.server.storage.mongo.FileMeta;
import cn.vlabs.clb.server.storage.mongo.MFile;
import cn.vlabs.clb.server.ui.frameservice.DPair;
import cn.vlabs.clb.server.ui.frameservice.LocalFileCacheService;
import cn.vlabs.clb.server.utils.GlobalConst;

import com.mongodb.gridfs.GridFSDBFile;

/**
 * 这个是文档服务的统一访问入口。 作为一个统一的访问Facade目的是减少别的模块和该模块之间的直接依赖关系。
 * 目前已经有三个访问入口需要使用到这里的访问代码。将这些代码独立出来是有实际意义的。
 * 
 * @author clive lee
 * 
 */
@Component("DocumentFacade")
public class DocumentFacade {

	@Autowired
	private IStorageService mongoStorage;

	@Autowired
	private IDocMetaService docMetaService;

	@Autowired
	private IDocVersionService docVersionService;

	@Autowired
	private IDocChunkStatusService docChunkStatusService;

	@Autowired
	private IIDGeneratorService idGeneratorService;

	@Autowired
	private LocalFileCacheService localCacheService;

	@Autowired
	private IDocRefService docRefService;

	public LocalFileCacheService getLocalCacheService() {
		return this.localCacheService;
	}

	private String ioMode = Config.getInstance().getStringProp("clb.io.mode",
			"writeable");

	public void checkSystemIOMode(String operation)
			throws InvalidFileOperationException {
		if ("readonly".equals(ioMode)) {
			throw new InvalidFileOperationException(operation);
		}
	}

	public DPair convertToPair(int appid, Object o)
			throws InvalidArgumentException {
		DocPair dp = (DocPair) o;
		if (dp.getDocid() <= 0) {
			throw new InvalidArgumentException(appid, dp);
		}
		if ("latest".equals(dp.getVersion())) {
			return new DPair(appid, dp.getDocid());
		}
		try {
			return new DPair(appid, dp.getDocid(), Integer.parseInt(dp
					.getVersion()));
		} catch (NumberFormatException e) {
			throw new InvalidArgumentException(appid, dp);
		}
	}

	public DPair[] convertToPairs(int appid, Object arg)
			throws InvalidArgumentException {
		DocPair[] pairs = (DocPair[]) arg;
		DPair[] array = null;
		if (pairs != null) {
			array = new DPair[pairs.length];
			for (int i = 0; i < pairs.length; i++) {
				DPair dp = convertToPair(appid, pairs[i]);
				array[i] = dp;
			}
		}
		return array;
	}

	private void metaNotFoundExceptionHandler(int appid, int docid)
			throws MetaNotFoundException {
		throw new MetaNotFoundException(appid, docid, "document");
	}

	public MFile buildMongoFile(int appid, DocVersion dv, int flag,
			ParserReadable readable) {
		MFile mf = new MFile();
		String filename = readable.getName();
		String suffix = MimeType.getSuffix(filename);
		mf.setAppid(appid);
		mf.setFilename(filename);
		mf.setContentType(MimeType.getContentType(suffix));
		mf.setFileExtension(suffix);
		mf.setStorageKey(new ObjectId(dv.getStorageKey()));
		mf.setDocid(dv.getDocid());
		mf.setVid(dv.getVersion() + "");
		mf.setIsPub(flag);
		return mf;
	}

	public MFile buildMongoFile(int appid, DocVersion dv, int isPub) {
		MFile mf = new MFile();
		String filename = dv.getFilename();
		String suffix = MimeType.getSuffix(filename);
		mf.setAppid(appid);
		mf.setFilename(filename);
		mf.setContentType(MimeType.getContentType(suffix));
		mf.setFileExtension(suffix);
		mf.setStorageKey(new ObjectId(dv.getStorageKey()));
		mf.setDocid(dv.getDocid());
		mf.setVid(dv.getVersion() + "");
		mf.setIsPub(isPub);
		mf.setLength(dv.getSize());
		return mf;
	}

	public void saveDocumentContent(MFile mf, InputStream ins) {
		mongoStorage.writeDocument(ins, mf);
	}

	public int createDocMeta(int appid, int isPub) {
		// DocMeta doc = new DocMeta();
		// String authId =
		// CurrentConnection.getSessions().currentUser().getName();
		// doc.setAppid(appid);
		// doc.setAuthId(authId);
		// doc.setCreateTime(new Date());
		// doc.setLastUpdateTime(new Date());
		// doc.setLastVersion(1);
		// doc.setIsPub(isPub);
		// doc.setDocid(idGeneratorService.getNextID("clb", "docmeta", appid));
		// docMetaService.create(doc);
		// return doc.getDocid();

		return this.createDocMeta(appid,
				idGeneratorService.getNextID("clb", "docmeta", appid), isPub);
	}

	public int createDocMeta(int appid, int docid, int isPub) {
		DocMeta doc = new DocMeta();
		String authId = CurrentConnection.getSessions().currentUser().getName();
		doc.setAppid(appid);
		doc.setAuthId(authId);
		doc.setCreateTime(new Date());
		doc.setLastUpdateTime(new Date());
		doc.setLastVersion(1);
		doc.setIsPub(isPub);
		doc.setDocid(docid);
		docMetaService.create(doc);
		return doc.getDocid();
	}

	public DocVersion createDocVersion(int appid, int docid, String filename,
			long size) {
		String storageKey = mongoStorage.generateStorageKey();
		DocVersion dv = buildDocVersion(appid, docid, filename, size,
				storageKey, DocVersion.WAITING_UPLOAD);
		DocVersion ndv = docVersionService.create(dv);
		updateDocMeta(ndv);
		return ndv;
	}

	public DocVersion createCompletedDocVersion(int appid, int docid,
			String filename, long size, String storageKey) {
		DocVersion dv = buildDocVersion(appid, docid, filename, size,
				storageKey, DocVersion.COMPLETE_UPLOAD);
		DocVersion ndv = docVersionService.createFirstVersion(dv);
		return ndv;
	}

	public DocVersion createWaitingDocVersion(int appid, int docid,
			String filename, long size) {
		return this.createWaitingDocVersion(appid, docid, filename, size,
				mongoStorage.generateStorageKey());
	}

	public DocVersion createWaitingDocVersion(int appid, int docid,
			String filename, long size, String storageKey) {
		DocVersion dv = buildDocVersion(appid, docid, filename, size,
				storageKey, DocVersion.WAITING_UPLOAD);
		DocVersion ndv = docVersionService.createFirstVersion(dv);
		return ndv;
	}

	private DocVersion buildDocVersion(int appid, int docid, String filename,
			long size, String storageKey, String status) {
		DocVersion dv = new DocVersion();
		dv.setAppid(appid);
		dv.setDocid(docid);
		dv.setFilename(filename);
		dv.setSize(size);
		dv.setStorageKey(storageKey);
		dv.setFileExtension(MimeType.getSuffix(filename));
		dv.setUploadStatus(status);
		return dv;
	}

	private void updateDocMeta(DocVersion ndv) {
		DocMeta m = new DocMeta();
		m.setAppid(ndv.getAppid());
		m.setDocid(ndv.getDocid());
		m.setLastUpdateTime(new Date());
		m.setLastVersion(ndv.getVersion());
		docMetaService.update(m, new String[] { "appid", "docid" });
	}

	public DocMeta getDocMetaByDocid(int appid, int docid)
			throws MetaNotFoundException {
		DocMeta doc = docMetaService.read(appid, docid);
		if (null == doc) {
			metaNotFoundExceptionHandler(appid, docid);
		}
		return doc;
	}
	
	public String queryMd5(String storageKey){
		return mongoStorage.queryMd5(storageKey);
	}

	public DocVersion getDocVersion(DPair p) throws MetaNotFoundException {
		DocVersion dv = docVersionService.read(p);
		if (dv == null) {
			metaNotFoundExceptionHandler(p.getAppid(), p.getDocid());
		}
		return dv;
	}

	public DocVersion getDocVersionWithoutException(DPair p) {
		return docVersionService.read(p);
	}

	public DocVersion getDocVersion(int appid, int docid)
			throws MetaNotFoundException {
		DocVersion dv = docVersionService.readLastVersion(appid, docid);
		if (dv == null) {
			metaNotFoundExceptionHandler(appid, docid);
		}
		return dv;
	}

	public VersionInfo[] getVersionsByDocId(int appid, int docId)
			throws MetaNotFoundException {
		List<DocVersion> versions = docVersionService
				.readVersions(appid, docId);
		if (versions.isEmpty()) {
			metaNotFoundExceptionHandler(appid, docId);
		}
		return VersionInfoAdapter.toVersionInfo(versions);
	}

	public boolean isDocExist(int appid, int docid) {
		DocMeta doc = docMetaService.read(appid, docid);
		return doc.getId() < 0;
	}

	public void removeDocument(int appid, int docId) {
		List<DocVersion> versions = docVersionService
				.readVersions(appid, docId);
		for (DocVersion v : versions) {
			mongoStorage.removeDocument(v.getStorageKey());
		}
		docVersionService.deleteByDocid(appid, docId);
		docMetaService.delete(appid, docId);
	}

	public int removeDocVersion(DocVersion dv) {
		mongoStorage.removeDocument(dv.getStorageKey());
		int flag = docVersionService.delete(new DPair(dv.getAppid(), dv
				.getDocid(), dv.getVersion()));
		DocVersion ndv = docVersionService.readLastVersion(dv.getAppid(),
				dv.getDocid());
		if (ndv != null) {
			updateDocMeta(ndv);
		} else {
			docMetaService.delete(dv.getAppid(), dv.getDocid());
		}
		return flag;
	}

	public GridFSDBFile readDocContent(DocVersion dv)
			throws FileContentNotFoundException {
		String storageKey = dv.getStorageKey();
		if (storageKey == null) {
			throw new FileContentNotFoundException(dv.getAppid(),
					dv.getDocid(), dv.getVersion(), null, "document");
		}
		GridFSDBFile dfile = mongoStorage.loadDocument(dv.getStorageKey());
		if (dfile == null) {
			throw new FileContentNotFoundException(storageKey, "document");
		}
		return dfile;
	}

	public DocVersion getLastVersionByDocid(int appid, Integer docid) {
		return docVersionService.readLastVersion(appid, docid);
	}

	public List<DocVersion> getMultipleDocVersion(DPair[] pairs) {
		return docVersionService.batchRead(pairs);
	}

	public GridFSDBFile readDocContent(DPair p)
			throws FileContentNotFoundException {
		GridFSDBFile dfile = mongoStorage.loadDocument(p);
		if (dfile == null) {
			throw new FileContentNotFoundException(p.getAppid() + ","
					+ p.getDocid() + "," + p.getVersion(), "document");
		}
		return dfile;
	}

	public List<DocMeta> getDocMetaByIds(int appid, DPair[] pairs) {
		int[] docids = new int[pairs.length];
		for (int i = 0; i < pairs.length; i++) {
			docids[i] = pairs[i].getDocid();
		}
		return docMetaService.batchRead(appid, docids);
	}

	public void replaceDocVersion(DPair[] pairs, List<DocMeta> metas) {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (int i = 0; i < pairs.length; i++) {
			map.put(pairs[i].getDocid(), i);
		}
		for (DocMeta m : metas) {
			pairs[map.get(m.getDocid())].setVersion(m.getLastVersion());
		}
	}

	public int updateMetaSize(DocVersion dv) {
		FileMeta fm = mongoStorage.findDocument(dv.getStorageKey());
		dv.setSize(fm.getLength());
		docVersionService.update(dv);
		return (int) fm.getLength();
	}

	/**
	 * 根据MD5查询文件Clbid
	 * 
	 * @param md5
	 * @param size
	 * @return
	 */
	public Integer queryClbidForMd5(String md5, long size) {
		DocRef ref = docRefService.read(md5, size);
		if (ref != null && ref.getRef() > 0) {
			DocVersion docVersion = docVersionService.read(ref.getStorageKey());
			if (docVersion != null) {
				return docVersion.getDocid();
			}
		}
		return null;
	};

	/**
	 * 创建文件上传记录
	 * 
	 * @param clbid
	 * @param md5
	 * @param size
	 * @param fileName
	 */
	public void createClbRef(int appid, ClbMeta meta) {
		createDocMeta(appid, meta.getDocid(), 0); // 创建元数据记录

		DocRef ref = docRefService.read(meta.getMd5(), meta.getSize());
		ref.setRef(ref.getRef() + 1);
		docRefService.updateRef(ref);

		DocVersion docVersion = new DocVersion(appid, meta.getDocid());
		docVersion.setFilename(meta.getFilename());
		docVersion.setSize(meta.getSize());
		docVersion.setUploadStatus(DocVersion.COMPLETE_UPLOAD);
		docVersion.setVersion(DocVersion.FIRST_VERSION);
		docVersion.setAppid(appid);
		docVersion.setFileExtension(MimeType.getSuffix(meta.getFilename()));
		docVersion.setStorageKey(ref.getStorageKey());
		docVersionService.createFirstVersion(docVersion);
	}

	public ChunkResponse prepareUpload(int appid, PrepareChunkUploadParam param) {
		int docid = this.createDocMeta(appid, 0);
		String md5 = param.getMd5();
		long size = param.getSize();
		Map<String, Object> opts = new HashMap<String, Object>();
		Set<Integer> set = null;
		int chunkSize = param.getChunkSize();
		int code = ChunkResponse.SUCCESS;
		DocRef ref = docRefService.read(md5, size);
		if (chunkSize > GlobalConst.MAX_CHUNK_SIZE) {
			code = ChunkResponse.EXCEED_MAX_CHUNK_SIZE;
			opts.put("currentChunkSize", chunkSize);
		} else if (ref != null && ref.getRef() > 0) { // 存在一个已经完成的完整文件
			docRefService.incrRef(md5, size);
			createCompletedDocVersion(appid, docid, param.getFilename(),
					param.getSize(), ref.getStorageKey());
			code = ChunkResponse.FOUND_THE_SAME_CONTENT;
		} else if (ref != null && ref.getRef() == 0) { // 如果有未完成的文件,则返回此文件的storageKey
			createWaitingDocVersion(appid, docid, param.getFilename(), param.getSize(), ref.getStorageKey());
			docChunkStatusService.create(appid, docid, size, md5, chunkSize);
			Set<Integer> existSet = mongoStorage.queryDocumentChunkSet(ref
					.getStorageKey());
			DocChunkStatus dcs = docChunkStatusService.updateChunkStatus(appid,
					docid, existSet);
			set = dcs.findEmptyChunk();
		} else if (ref == null) {
			DocRef dr = createDocRef(md5, size,
					mongoStorage.generateStorageKey());
			createWaitingDocVersion(appid, docid, param.getFilename(),
					param.getSize(), dr.getStorageKey());
			DocChunkStatus dcs = docChunkStatusService.create(appid, docid,
					size, md5, chunkSize);
			set = dcs.findEmptyChunk();
		}
		return buildChunkUploadResponse(ChunkResponse.PREPARE_PHASE, docid, 0,
				code, chunkSize, set, opts);
	}

	private DocRef createDocRef(String md5, long size, String storageKey) {
		DocRef dr = new DocRef();
		dr.setMd5(md5);
		dr.setSize(size);
		dr.setRef(0); // 完成时再增加 因此刚开始时为0
		dr.setStorageKey(storageKey);
		int drid = docRefService.create(dr);
		dr.setId(drid);
		return dr;
	}

	public ChunkResponse writeChunkData(int appid, ExecuteChunkUploadParam cup,
			InputStream ins) throws MetaNotFoundException {
		
   	    System.out.print("start  chunkIndex="+cup.getIndex());

		int docid = cup.getDocid();
		DocVersion dv = this.getDocVersion(appid, docid);
		MFile mf = this.buildMongoFile(appid, dv, 0);
		DocChunkStatus dcs = docChunkStatusService.read(appid, docid);
		Map<String, Object> opts = new HashMap<String, Object>();
		Set<Integer> set = null;
		int status = 0;
		if (dcs == null) {
			status = ChunkResponse.NOT_FOUND_CHUNK_META;
		} else if (cup.getIndex() >= dcs.getNumOfChunk() || cup.getIndex() < 0) {
			status = ChunkResponse.CHUNK_INDEX_INVALID;
			opts.put("numOfChunk", dcs.getNumOfChunk());
		} else if (cup.getLength() > dcs.getChunkSize()) {
			status = ChunkResponse.TOO_LARGE_CHUNK;
			opts.put("chunkSize", dcs.getChunkSize());
			opts.put("currentChunkSize", cup.getLength());
		} else {
			set = dcs.findEmptyChunk();
			if (set.contains(cup.getIndex())) { // index from 0 to chunkSize - 1
				mf.setMd5(dcs.getMd5());
				mf.setLength(dcs.getFileSize());
				mongoStorage.chunkedWriteDocument(cup.getIndex(),
						dcs.getNumOfChunk(), ins, cup.getLength(), mf,
						dcs.getChunkSize());
				docChunkStatusService.updateChunkStatus(appid, docid,
						cup.getIndex());
				status = ChunkResponse.SUCCESS;
			} else {
				status = ChunkResponse.DUPLICATE_CHUNK;
			}
		}
		return buildChunkUploadResponse(ChunkResponse.EXECUTE_PHASE, docid,
				cup.getIndex(), status, dcs.getChunkSize(), set, opts);
	}

	private ChunkResponse buildChunkUploadResponse(String phase, int docid,
			int index, int status, int chunkSize, Set<Integer> remains,
			Map<String, Object> opts) {
		ChunkResponse res = new ChunkResponse();
		res.setChunkIndex(index);
		res.setOpts(opts);
		res.setEmptyChunkSet(remains);
		res.setDocid(docid);
		res.setStatusCode(status);
		res.setPhase(phase);
		res.setChunkSize(chunkSize);
		return res;
	}

	public ChunkResponse finishUpload(int appid, int docid) {
		DocVersion docVersion = docVersionService.readLastVersion(appid, docid);
		Set<Integer> existSet = mongoStorage.queryDocumentChunkSet(docVersion
				.getStorageKey());
		
		DocChunkStatus dcs = docChunkStatusService.updateChunkStatus(appid,
				docid, existSet);
		Set<Integer> set = dcs.findEmptyChunk();
		int status = 0;
		//DocChunkStatus dcs = docChunkStatusService.read(appid, docid);
		if (set.isEmpty()) {
			updateUploadStatus(appid, docid, 1);
			docRefService.incrRef(dcs.getMd5(), dcs.getFileSize());
			status = ChunkResponse.SUCCESS;
		} else {
			status = ChunkResponse.NEED_MORE_CHUNK;
		}
		return buildChunkUploadResponse(ChunkResponse.FINISH_PHASE, docid, 0,
				status, dcs.getChunkSize(), set, null);
	}

	public Set<Integer> queryEmptyChunks(int appid, int docid) {
		DocChunkStatus dcs = docChunkStatusService.read(appid, docid);
		if (dcs == null) {
			return null;
		}
		return dcs.findEmptyChunk();
	}

	public void updateUploadStatus(int appid, int docid, int version) {
		DocVersion dv = new DocVersion();
		dv.setAppid(appid);
		dv.setDocid(docid);
		dv.setVersion(version);
		dv.setUploadStatus(DocVersion.COMPLETE_UPLOAD);
		dv.setCompleteTime(new Date());
		docVersionService.updateStatus(dv);
	}
	
	public void incrRef(String storageKey){
		docRefService.incrRef(storageKey);
	}

}
