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

package cn.vlabs.clb.api.io;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.vlabs.clb.api.io.impl.ServletOutputStream;
import cn.vlabs.rest.IFileSaver;

public class ServletFileSaver implements IFileSaver {
	public ServletFileSaver(HttpServletRequest request,
			HttpServletResponse response) {
		this.response = response;
		this.request = request;
	}

	public void save(String filename, InputStream in) {
		try {
			ServletOutputStream out = new ServletOutputStream(request, response);
			out.setFileName(filename);
			FileUtil.copy(in, out.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private HttpServletResponse response;

	private HttpServletRequest request;
}
