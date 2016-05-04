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
import cn.vlabs.clb.server.ui.frameservice.DPair;

public class MetaNotFoundException extends BaseException {

    private static final Logger LOG = Logger.getLogger(MetaNotFoundException.class);

    private static final String USER_PROMPT = "Sorry, can not find your accessed file meta.";
    private static final long serialVersionUID = 1L;

    public MetaNotFoundException(DPair p, String type) {
        super(USER_PROMPT + "[appid=" + p.getAppid() + ",docid=" + p.getDocid() + ",version=" + p.getVersion()
                + ",type=" + type + "]");
        writeLocalLog(p, type);
    }

    public MetaNotFoundException(int appid, int docid, String type) {
        super(USER_PROMPT + "[appid=" + appid + ",docid=" + docid + ",type=" + type + "]");
        writeLocalLog(appid, docid, type);
    }

    public MetaNotFoundException(String storageKey, String type) {
        super(USER_PROMPT + "[storageKey=" + storageKey + ",type=" + type + "]");
        writeLocalLog(storageKey, type);
    }

    private void writeLocalLog(String storageKey, String type) {
        LOG.error("can not find meta info by [storageKey=" + storageKey + ",type=" + type + "]", this);
    }

    private void writeLocalLog(int appid, int docid, String type) {
        LOG.error("can not find meta info by [appid=" + appid + ",docid=" + docid + ",type=" + type + "]", this);
    }

    private void writeLocalLog(DPair p, String type) {
        LOG.error("can not find meta info by [" + p + ",type=" + type + "]", this);
    }

    @Override
    public int getErrorCode() {
        return ErrorCode.META_NOT_FOUND;
    }

}
