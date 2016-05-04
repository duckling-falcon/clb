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
import cn.vlabs.clb.server.model.DocVersion;

public class InvalidFileOperationException extends BaseException {

    private static final long serialVersionUID = 1L;
    
    private static final Logger LOG = Logger.getLogger(InvalidFileOperationException.class);

    public InvalidFileOperationException(DocVersion dv, String operation) {
        super("Sorry, your file " + dv.getFilename() + " can not execute this " + operation);
        writeLocalLog(dv.getAppid(), dv.getDocid(), dv.getVersion(), dv.getFilename(), operation);
    }
    
    public InvalidFileOperationException(String operation) {
        super("Sorry, The clb server is under the READ_ONLY status, so your "+ operation +" request is invalid. " );
        LOG.warn("Sorry, The clb server is under the READ_ONLY status, so your "+ operation +" request is invalid.");
    }

    private void writeLocalLog(int appid, int docid, int version, String type, String operation) {
        LOG.error("somebody want to perform " + operation + " on the file [appid="+ appid +
                "docid=" + docid + ",version=" + version + ",filename=" + type + "]");
    }

    @Override
    public int getErrorCode() {
        return ErrorCode.INVALID_OPERATION;
    }

}
