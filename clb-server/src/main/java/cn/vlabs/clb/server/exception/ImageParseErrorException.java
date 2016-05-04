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

public class ImageParseErrorException extends BaseException{
    /*
     * This exception is an internal exception at now, may be someday it will change.
     */
    private static final long serialVersionUID = 1L;
    
    private static final Logger LOG = Logger.getLogger(ImageParseErrorException.class);
    
    public ImageParseErrorException(String message){
        super(message);
    }
    
    public ImageParseErrorException(String message,String targetKey,String filename) {
        super(message);
        writeLocalLog(targetKey, filename);
    }
    
    private void writeLocalLog(String storageKey, String type) {
        LOG.error("This image is corrupted, where is [storageKey=" + storageKey + ",type=" + type + "].");
    }

    @Override
    public int getErrorCode() {
        return 0;
    }

}
