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
package cn.vlabs.clb.server.ui.frameservice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;


import com.mongodb.gridfs.GridFSDBFile;

@Service
public class LocalFileCacheService {
	private static final Logger LOG = Logger.getLogger(LocalFileCacheService.class);
	private Map<String, SrcPair> localFileCachedMap = new ConcurrentHashMap<String, SrcPair>();
	private CleanWorker cleanWorker = new CleanWorker();
	private static final int BUFFER_SIZE = 4096;

	public static String resizeTempDir;
	static {
		Resource tempDir = new ClassPathResource("/");
		try {
			resizeTempDir = tempDir.getFile().getPath() + File.separator
					+ "temp" + File.separator + "resize" + File.separator;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}                              
	
    @PostConstruct
    public void init() {
        cleanWorker.start();
        cleanWorker.setStopFlag(false);
        LOG.info("Clean file worker thread is ready to work.");
    }

    @PreDestroy
    public void destroy() {
        cleanWorker.setStopFlag(true);
        cleanWorker.interrupt();
        LOG.info("All workers are stopped.");
    }

	private File checkAndCreateTempDir() {
		File file = new File(resizeTempDir);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}

	public SrcPair loadOriginalImage(GridFSDBFile gfile) {
		String srcStorageKey = gfile.get("storageKey").toString();
		SrcPair sp = saveTempFile(srcStorageKey, gfile.getInputStream());
		return sp;
	}

	public SrcPair saveTempFile(String srcStorageKey, InputStream ins) {
		long start = System.currentTimeMillis();
		checkAndCreateTempDir();
		if (localFileCachedMap.containsKey(srcStorageKey)) { // SrcPair如果在的话并不表示文件在
			SrcPair sp = localFileCachedMap.get(srcStorageKey);
			File f = new File(sp.getSrcPath());
			if (f.exists()) {
				cleanWorker.removeJob(sp.getSrcPath());
				LOG.debug("Local file cache hit for " + srcStorageKey);
				return sp;
			} else {
				localFileCachedMap.remove(srcStorageKey);
			}
		}
		String srcPath = getTempFilename(srcStorageKey, "original");
		File srcTempFile = new File(srcPath);
		if (srcTempFile.exists() && srcTempFile.length() > 0) { // 如果发现源文件存在则马上去掉清理此文件的任务
			cleanWorker.removeJob(srcPath);
		} else {
			OutputStream ous = null;
			try {
				ous = new FileOutputStream(srcTempFile);
				int read = 0;
				byte buf[] = new byte[BUFFER_SIZE];
				while ((read = ins.read(buf, 0, BUFFER_SIZE)) != -1) {
					ous.write(buf, 0, read);
				}
			} catch (IOException e1) {
				LOG.error("Write image temp file failed", e1);
				return null;
			} finally {
				IOUtils.closeQuietly(ins);
				IOUtils.closeQuietly(ous);
			}
		}
		SrcPair sp = new SrcPair();
		sp.setSrcPath(srcPath);
		sp.setSrcStorageKey(srcStorageKey);
		long end = System.currentTimeMillis();
		localFileCachedMap.put(srcStorageKey, sp);
		LOG.debug("Save temp file use time " + (end - start) + " ms");
		return sp;
	}

	public String getTempFilename(String filename, String prefix) {
		return resizeTempDir + prefix + "_" + filename;
	}

	public void addCleanJobs(String[] array) {
		for(int i = 0;i<array.length;i++){
			cleanWorker.addNewJob(array[i]);
		}
	}

	public void removeJob(String srcPath) {
		cleanWorker.removeJob(srcPath);
	}

}
