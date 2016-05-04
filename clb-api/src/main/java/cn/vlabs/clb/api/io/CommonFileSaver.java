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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.vlabs.rest.IFileSaver;

public class CommonFileSaver implements IFileSaver {
	/**
	 * 构造函数
	 * 
	 * @param dir
	 *            文件存放的目录
	 * @throws FileNotFoundException
	 *             如果该目录不存在，抛出这个异常。
	 */
	public CommonFileSaver(String dir) throws FileNotFoundException {
		f = new File(dir);
		if (!f.exists()) {
			throw new FileNotFoundException(dir);
		}
	}

	public void save(String filename, InputStream in) {
		FileOutputStream out;
		try {
			out = new FileOutputStream(guessFile(filename));
			FileUtil.copy(in, out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String guessFile(String filename) {
		File dir;
		String result;
		int count = 1;
		do {
			result = f.getPath() + File.separator + rename(filename, count);
			dir = new File(result);
			count++;
		} while (dir.exists());
		return result;
	}

	private String rename(String filename, int count) {
		if (count == 1)
			return filename;
		int pos = filename.lastIndexOf('.');
		String suffix = filename.substring(pos);
		String prefix = filename.substring(0, pos - 1);
		return prefix + "(" + count + ")" + suffix;
	}

	private File f;
}
