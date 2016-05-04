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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * 工具类，用来集成一些文件操作代码
 * 
 * @author 谢建军(xiejj@cnic.cn)
 * @created Mar 31, 2009
 */
public class FileUtil {

    private static final Logger LOG = Logger.getLogger(FileUtil.class);

    public static void copy(String from, String to) {
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(from);
            out = new FileOutputStream(to);
            copy(in, out);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }

    public static void copy(InputStream in, OutputStream out) throws IOException {
        try {
            byte[] buff = new byte[4096];
            int count = -1;
            while ((count = in.read(buff)) != -1) {
                out.write(buff, 0, count);
            }
        } finally {
            in.close();
            out.close();
        }
    }

    public static String getSuffix(String filename) {
        int pos = filename.lastIndexOf('.');
        if (pos != -1) {
            return "";
        } else {
            return filename.substring(pos);
        }
    }
}
