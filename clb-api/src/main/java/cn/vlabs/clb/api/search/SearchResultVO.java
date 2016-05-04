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

package cn.vlabs.clb.api.search;


public class SearchResultVO {
	private String comment =null;
	private int docId = 0;
	private String summary = null;
	private String keywords = null;
	private String title = null;
	private String verNo = null;
	private String filename = null;
	private String updateDate= null;
	private String createBy = null;
	private String size=null;
	private boolean removed = false;
	public void setSize(String size){
		this.size=size;
	}
	
	public String getSize(){
		return this.size;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	/*public Tag[] getTags() {
		return tags;
	}
	public void setTags(Tag[] tags) {
		this.tags = tags;
	}*/
	public void setFilename(String fname){
		this.filename=fname;
	}
	public String getFilename(){
		return filename;
	}
	public void setUpdateTime(String time){
		this.updateDate=time;
	}
	public String getUpdateTime(){
		return updateDate;
	}
	public int getDocId() {
		return docId;
	}
	public void setDocId(int docId) {
		this.docId = docId;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getVerNo() {
		return verNo;
	}
	public void setVerNo(String verNo) {
		this.verNo = verNo;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getComment() {
		return comment;
	}

	public String getResourceId() {
		return Integer.toString(docId);
	}
	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	public boolean isRemoved() {
		return removed;
	}
}
