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

package cn.vlabs.clb.server.utils;

import java.util.ArrayList;

public class StringUtil {
	public static String rebuildName(String filename, int count) {
		int pos = filename.lastIndexOf('.');
		if (pos == -1) {
			return filename + "_" + count;
		} else {
			return filename.substring(0, pos) + "_" + count
					+ filename.substring(pos);
		}
	}

	public static String getSuffix(String file) {
		if (file == null || file.trim().equals(""))
			return null;
		else {
			int dotPos = file.indexOf('.');
			if (dotPos != -1) {
				return file.substring(dotPos);
			} else
				return null;
		}
	}

	public static String toSetString(ArrayList<Integer> values) {
		StringBuilder buff = new StringBuilder();
		buff.append("(");
		if (values != null)
			for (int i = 0; i < values.size(); i++) {
				if (i != 0) {
					buff.append("," + values.get(i));
				} else {
					buff.append(values.get(i));
				}
			}
		buff.append(")");
		return buff.toString();
	}

	public static String toSetString(int[] values) {
		StringBuilder buff = new StringBuilder();
		buff.append("(");
		if (values != null)
			for (int i = 0; i < values.length; i++) {
				if (i != 0) {
					buff.append("," + values[i]);
				} else {
					buff.append(values[i]);
				}
			}
		buff.append(")");
		return buff.toString();
	}
	
}
