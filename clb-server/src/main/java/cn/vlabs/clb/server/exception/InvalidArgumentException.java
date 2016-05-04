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
import cn.vlabs.clb.api.document.DocPair;

public class InvalidArgumentException extends BaseException {
    
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = Logger.getLogger(InvalidArgumentException.class);

    public InvalidArgumentException(int appid,DocPair dp) {
        super("Your input contains illegal arguments, please check it [appid="+ appid+",docid=" + dp.docid + ",version=" + dp.version +"]");
        writeLocalLog(appid,dp.getDocid(), dp.getVersion());
    }
    
    public InvalidArgumentException(String message) {
        super(message);
    }

    private void writeLocalLog(int appid, int docid, String version) {
        LOG.error("Invalidate argument for [appid="+ appid+",docid=" + docid + ",version=" + version + "]",this);
    }

    @Override
    public int getErrorCode() {
        return ErrorCode.INVALID_ARGUMENT;
    }

}
