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

import java.io.FileNotFoundException;
import java.io.InputStream;

import cn.vlabs.clb.api.io.impl.FileStreamInfo;
import cn.vlabs.clb.api.io.impl.NormalStreamInfo;
import cn.vlabs.rest.stream.IResource;

/**
* @Description: TODO
* @author CERC
* @date Jul 12, 2011 9:27:20 AM
*
*/
public class ResourceFactory {
	public static IResource getStream(String absoluteFilePath)
			throws FileNotFoundException {
		return new FileStreamInfo(absoluteFilePath);
	}

	public static IResource getStream(String filename, long length,
			InputStream in) {
		return new NormalStreamInfo(filename, length, in);
	}
}
