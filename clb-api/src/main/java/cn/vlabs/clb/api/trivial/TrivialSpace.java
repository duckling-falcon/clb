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


public class TrivialSpace {
    private int docid;
    private String version;
    private String spaceName;
    private String spaceURL;

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

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String trivialSpaceKey) {
        this.spaceName = trivialSpaceKey;
    }

    public String getSpaceURL() {
        return spaceURL;
    }

    public void setSpaceURL(String trivialSpaceURL) {
        this.spaceURL = trivialSpaceURL;
    }
    
    public TrivialSpace(){}

}
