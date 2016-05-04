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

package cn.vlabs.clb.api.rest;

public class ResourceInfo {
	public ResourceInfo(int docid){
		this.type=DOCUMENT;
		this.docid=docid;
		this.path="";
	}
	public ResourceInfo(String path){
		this.type=FOLDER;
		this.path=path;
		this.docid=-1;
	}
	public ResourceInfo(){
		this.type="";
		this.path="";
		this.docid=-1;
	}
	
	public boolean isDocument(){
		return DOCUMENT.equals(type);
	}
	public boolean isFolder(){
		return FOLDER.equals(type);
	}
	public void setType(String type){
		this.type=type;
	}
	public void setDocid(int docid){
		this.docid=docid;
	}
	public int getDocid(){
		return this.docid;
	}
	public void setPath(String path){
		this.path=path;
	}
	public String getPath(){
		return this.path;
	}
	private String type;
	private int docid;
	private String path;
	private static final String DOCUMENT="doc";
	private static final String FOLDER="folder";
}
