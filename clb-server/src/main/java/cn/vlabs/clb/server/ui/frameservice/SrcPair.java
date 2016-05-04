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

public class SrcPair {

    private String srcPath;
    private String srcStorageKey;

    public String getSrcPath() {
        return srcPath;
    }

    public void setSrcPath(String srcPath) {
        this.srcPath = srcPath;
    }

    public String getSrcStorageKey() {
        return srcStorageKey;
    }

    public void setSrcStorageKey(String srcStorageKey) {
        this.srcStorageKey = srcStorageKey;
    }
    
    @Override
    public String toString(){
        return srcPath + "_" + srcStorageKey;
    }
    
    @Override
    public boolean equals(Object sp){
        if(sp instanceof SrcPair){
            SrcPair o = (SrcPair)sp;
            return o.toString().equals(this.toString());
        }
        return false;
    }

}
