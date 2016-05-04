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
package cn.vlabs.clb.server.ui.frameservice;

public class OperationUtils {

    public static final String OP_CONVERT = "convert";
    public static final String OP_RESIZE = "resize";
    public static final String OP_UNZIP = "unzip";

    private static final String[] RESIZE_FILE_EXT = { "png", "gif", "jpg", "jpeg", "bmp" };

    private static final String[] UNZIP_FILE_EXT = { "zip" };

    private static final String[] CONVERT_FILE_EXT = { "ppt", "doc", "xls", "docx", "pptx", "xlsx", "rtf" };

    public static boolean canResize(String fileExtension) {
        return isFileSupportForOperation(fileExtension, RESIZE_FILE_EXT);
    }

    public static boolean canConvert(String fileExtension) {
        return isFileSupportForOperation(fileExtension, CONVERT_FILE_EXT);
    }

    public static boolean canUnzip(String fileExtension) {
        return isFileSupportForOperation(fileExtension, UNZIP_FILE_EXT);
    }

    private static boolean isFileSupportForOperation(String ext, String[] candidates) {
        for (int i = 0; i < candidates.length; i++) {
            if (ext != null && candidates[i].equals(ext.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

}
