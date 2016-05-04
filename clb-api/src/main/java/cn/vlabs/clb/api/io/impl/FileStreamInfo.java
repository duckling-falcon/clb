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

package cn.vlabs.clb.api.io.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import cn.vlabs.clb.api.io.FileUtil;
import cn.vlabs.rest.stream.IResource;

public class FileStreamInfo implements IResource {
	public FileStreamInfo(String absoluteFilePath) throws FileNotFoundException {
		f = new File(absoluteFilePath);
		if (!f.exists()) {
			throw new FileNotFoundException(f.getPath());
		}

	}

	public String getFilename() {
		return f.getName();
	}

	public long length() {
		return f.length();
	}

	public InputStream getInputStream() {
		try {
			return new FileInputStream(f);
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	public long getLength() {
		return f.length();
	}

	public String getMimeType() {
		return MimeType.getContentType(FileUtil.getSuffix(f.getName()));
	}

	private File f;
}
