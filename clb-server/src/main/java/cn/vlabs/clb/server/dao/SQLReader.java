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

package cn.vlabs.clb.server.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * Introduction Here.
 * 
 * @date Feb 23, 2010
 * @author xiejj@cnic.cn
 */
public class SQLReader {
	public SQLReader(Reader reader) {
		this.reader = new BufferedReader(reader);
	}

	public SQLReader(BufferedReader reader) {
		this.reader = reader;
	}

	public SQLReader(InputStream in, String encode)
			throws UnsupportedEncodingException {
		if (encode == null)
			encode = "UTF-8";
		reader = new BufferedReader(new InputStreamReader(in, encode));
	}

	public String next() throws WrongSQL {
		if (!isClosed()) {
			StringBuffer buffer = new StringBuffer();

			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					line = line.trim();
					if (isDelimiter(line)) {
						changeDelimiter(line);
					} else if (isComment(line)) {
						// FIXME Do not support multiline comment, Just throw off now.
					    log.info(line);
					} else {
						if (line.endsWith(delimeter)) {
							line = line.substring(0, line.length()
									- delimeter.length());
							buffer.append(line + "\n");
							return StringUtils.trimToNull(buffer.toString());
						} else
							buffer.append(line + "\n");
					}
				}
			} catch (IOException e) {
				log.error(e.getMessage());
				log.debug("", e);
				close();
			}
			if (line == null) {
				close();
			}
			return StringUtils.trimToNull(buffer.toString());
		}
		return null;
	}

	private boolean isComment(String line) {
		return line.startsWith("/*");
	}

	private void changeDelimiter(String line) throws WrongSQL {
		delimeter = parseDelimiter(line);
		if (StringUtils.isEmpty(delimeter)) {
			log.error("Wrong format SQL");
			throw new WrongSQL("delimiter is empty:" + line);
		}
	}

	public void close() {
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				log
						.error("Exception has happended while reading initialize SQL.");
				log.error(e.getMessage());
				log.debug("", e);
			}
			reader = null;
		}
	}

	private String parseDelimiter(String line) {
		return line.substring("delimiter".length()).trim();
	}

	private boolean isDelimiter(String line) {
		if (line != null) {
			String ignored = line.toLowerCase();
			return ignored.startsWith("delimiter");
		}
		return false;
	}

	private boolean isClosed() {
		return reader == null;
	}

	private BufferedReader reader;
	private String delimeter = ";";

	private Logger log = Logger.getLogger(SQLReader.class);
}
