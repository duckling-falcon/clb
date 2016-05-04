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

import java.io.InputStream;

import cn.vlabs.clb.api.io.FileUtil;
import cn.vlabs.rest.stream.IResource;

public class NormalStreamInfo implements IResource {
	public NormalStreamInfo(String filename, long length, InputStream in) {
		this.filename = filename;
		this.length = length;
		this.in = in;
	}

	public String getFilename() {
		return filename;
	}

	public InputStream getInputStream() {
		return in;
	}

	public long getLength() {
		return length;
	}

	public String getMimeType() {
		return MimeType.getContentType(FileUtil.getSuffix(filename));
	}

	private String filename;
	private long length;
	private InputStream in;
}
