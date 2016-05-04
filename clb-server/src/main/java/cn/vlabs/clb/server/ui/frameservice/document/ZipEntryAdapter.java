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

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;

import cn.vlabs.clb.server.service.search.fulltext.parser.ParserReadable;

public class ZipEntryAdapter implements ParserReadable {
	public ZipEntryAdapter(ZipArchiveEntry entry, ZipFile file) {
		this.entry = entry;
		this.file = file;
	}

	public long getSize() {
		return entry.getSize();
	}

	public boolean exists() {
		return true;
	}

	public InputStream getInputStream() throws IOException {
		return file.getInputStream(entry);
	}

	
	public String getName() {
		return entry.getName();
	}

	private ZipArchiveEntry entry;

	private ZipFile file;
}
