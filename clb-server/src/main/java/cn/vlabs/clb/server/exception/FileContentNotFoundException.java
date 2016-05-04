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

package cn.vlabs.clb.server.exception;

import org.apache.log4j.Logger;

import cn.vlabs.clb.api.ErrorCode;

public class FileContentNotFoundException extends BaseException {

    private static final Logger LOG = Logger.getLogger(FileContentNotFoundException.class);

    private static final String USER_PROMPT = "Sorry, can not find your accessed file content.";
    private static final long serialVersionUID = 1L;

    public FileContentNotFoundException(String storageKey, String type) {
        super(USER_PROMPT + "[storageKey=" + storageKey + ",type=" + type + "]");
        writeLocalLog(storageKey, type);
    }

    public FileContentNotFoundException(int appid, int docid, int version, String storageKey, String type) {
        super(USER_PROMPT + "[docid=" + docid + ",version=" + version + ",storageKey=" + storageKey + ",type=" + type
                + "]");
        writeLocalLog(appid, docid, version, storageKey, type);
    }

    private void writeLocalLog(String storageKey, String type) {
        LOG.error("can not find content info by [storageKey=" + storageKey + ",type=" + type + "]", this);
    }

    private void writeLocalLog(int appid, int docid, int version, String storageKey, String type) {
        LOG.error("can not find content info by [appid=" + appid + "docid=" + docid + ",version=" + version
                + ",storageKey=" + storageKey + ",type=" + type + "]", this);
    }

    @Override
    public int getErrorCode() {
        return ErrorCode.CONTENT_NOT_FOUND;
    }

}
