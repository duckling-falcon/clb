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
package cn.vlabs.clb.api.trivial;

import cn.vlabs.clb.api.document.DocPair;

public class TrivialParam {
    public static final String OP_CREATE = "create";
    public static final String OP_UPDATE = "update";
    public static final String OP_DELETE = "delete";

    private DocPair docPair;
    
    public DocPair getDocPair() {
        return docPair;
    }

    public void setDocPair(DocPair pair) {
        this.docPair = pair;
    }

    private String unzipBaseDir;
    private String operation;
    private String spaceName;

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    public String getUnzipBaseDir() {
        return unzipBaseDir;
    }

    public void setUnzipBaseDir(String unzipBaseDir) {
        this.unzipBaseDir = unzipBaseDir;
    }

}
