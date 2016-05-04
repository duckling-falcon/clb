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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utility {
	public static int sortString(String str1, String str2) {
		if ((str1 == null) || (str1.equals("")))
			return 1;
		if ((str2 == null) || (str2.equals("")))
			return 0;
		int length1 = str1.length();
		int length2 = str2.length();
		int length;
		if (length1 < length2) {
			length = length1;
			for (int i = 0; i < length; i++) {
				if (str1.charAt(i) < str2.charAt(i))
					return 1;
				else if (str1.charAt(i) > str2.charAt(i))
					return 0;
			}
			return 1;
		} else {
			length = length2;
			for (int i = 0; i < length; i++) {
				if (str1.charAt(i) < str2.charAt(i))
					return 1;
				else if (str1.charAt(i) > str2.charAt(i))
					return 0;
			}
			return 0;
		}
	}

	/*
	 * 将数字按照1024进行简写，例如1024缩写成1K。
	 */
	private static String[] scalars = new String[] { "", "K", "M", "G", "T" };

	public static String getSizeShort(long size) {
		for (int i = 0; i < scalars.length - 1; i++) {
			if (size < 1024l)
				return size + scalars[i];
			else
				size = size >> 10;
		}
		return size + scalars[scalars.length - 1];
	}

	public static String getDateShort(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date d = sdf.parse(date);
			return getDateShort(d);
		} catch (ParseException e) {
			return "";
		}
	}

	public static String getDateShort(Date date) {
		Calendar calToday = Calendar.getInstance();
		calToday.set(Calendar.HOUR_OF_DAY, 0);
		Calendar calYear = Calendar.getInstance();
		calYear.set(Calendar.DAY_OF_YEAR, 1);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (cal.after(calToday))
			return (new SimpleDateFormat("HH:mm")).format(cal.getTime());
		else if (cal.after(calYear))
			return (new SimpleDateFormat("MM月dd日")).format(cal.getTime());
		else
			return (new SimpleDateFormat("yyyy年")).format(cal.getTime());
	}
}
