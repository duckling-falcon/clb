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

package cn.vlabs.clb.api.document;

import org.apache.commons.lang3.StringUtils;

import cn.vlabs.clb.api.ErrorCode;
import cn.vlabs.clb.api.InvalidArgument;

/**
 * @title: DocPair.java
 * @package cn.vlabs.clb.api.document
 * @description: 表示文档ID和版本的数据结构
 * @author clive
 * @date 2012-12-28 下午5:26:28
 */
public class DocPair {
    /**
     * @description 文档ID号
     */
    public int docid;
    /**
     * @description 文档版本号
     */
    public String version;

    public static final String LATEST_VERSION = "latest";

    public int getDocid() {
        return docid;
    }

    public void setDocid(int docid) {
        this.docid = docid;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public DocPair(int docid, String version) throws InvalidArgument {
        this.docid = docid;
        if (StringUtils.isNumeric(version)) {
            this.version = version;
        } else if (LATEST_VERSION.equals(version)) {
            this.version = LATEST_VERSION;
        } else {
            throw new InvalidArgument(ErrorCode.INVALID_ARGUMENT,
                    "The version argument should be Integer.toString() or \"latest\", but you input '" + version + "'.");
        }
    }

    public DocPair(int docid) {
        this.docid = docid;
        this.version = LATEST_VERSION;
    }
    
    public boolean isLatestVersion(){
        return DocPair.LATEST_VERSION.equals(this.version);
    }

}
