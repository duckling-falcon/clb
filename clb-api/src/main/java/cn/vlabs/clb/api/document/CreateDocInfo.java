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

/**
 * 创建文档信息
 * @author liyanzhao
 *
 */
public class CreateDocInfo {
    
    public String title;
    @java.lang.Deprecated
    public String keywords;
    @java.lang.Deprecated
    public String summary;
    public int clbid;

	/**
     * @description 是否为公开文件
     */
    public int isPub;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getIsPub() {
        return isPub;
    }

    public void setIsPub(int isPub) {
        this.isPub = isPub;
    }

	public int getClbid() {
		return clbid;
	}

	public void setClbid(int clbid) {
		this.clbid = clbid;
	}

}
