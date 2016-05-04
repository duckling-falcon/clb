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

public final class StackTrace {
	public static String getStackTraceAsString(Throwable e) {
		StringBuilder result = new StringBuilder();
		String prefix = "";
		while (e != null) {
			result.append(prefix + e.getClass().getName() + ": "
					+ e.getMessage() + "\n");
			StackTraceElement s[] = e.getStackTrace();
			for (int i = 0; i < s.length; i++)
				result.append("        at " + s[i] + "\n");

			prefix = "Caused by: ";
			e = e.getCause();
		}
		return result.toString();
	}
}
