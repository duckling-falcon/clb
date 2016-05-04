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

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

public class ServletOutputStream {
	public ServletOutputStream(String agent, HttpServletResponse response) {
		this.agent = agent;
		this.response = response;
		response.setHeader("Pragma", "");
		response.setHeader("Cache-Control", "");
	}

	public ServletOutputStream(HttpServletRequest request,
			HttpServletResponse response) {
		this.agent = request.getHeader("user-agent");
		String range = request.getHeader("range");
		if (range != null) {
			try {
				this.startPos = Long.parseLong(range.substring(6, range
						.length() - 1));
			} catch (NumberFormatException e) {

			}
		}
		this.response = response;
		response.setHeader("Pragma", "");
		response.setHeader("Cache-Control", "");
	}

	public OutputStream getOutputStream() throws IOException {
		return response.getOutputStream();
	}

	public void setFileName(String fname) {
		String suffix = MimeType.getSuffix(fname);
		// response.setCharacterEncoding("UTF-8");
		// 文件的内容类型
		response.setContentType(MimeType.getContentType(suffix));
		try {
			// 浏览器端存储的文件名
			if (isFirefox(agent))
				fname = javax.mail.internet.MimeUtility.encodeText(fname,
						"UTF-8", "B");
			else if (isIE(agent)) {
				String codedFname = URLEncoder.encode(fname, "UTF-8");
				codedFname = StringUtils.replace(codedFname, "+", "%20");
				if (codedFname.length() > 150) {
					codedFname = new String(fname.getBytes("GBK"), "ISO8859-1");
				}
				fname = codedFname;
			} else
				fname = URLEncoder.encode(fname, "UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename=\""
					+ fname + "\"");
		} catch (UnsupportedEncodingException e) {
		}
	}

	private boolean isFirefox(String agent) {
		return agent != null && agent.contains("Firefox");
	}

	private boolean isIE(String agent) {
		return agent != null && agent.contains("MSIE");
	}

	public long getStartPos() {
		return startPos;
	}

	private long startPos = -1;
	private HttpServletResponse response;
	private String agent;
}
