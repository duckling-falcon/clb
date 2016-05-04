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

import java.util.Date;

/**
 * @title: MetaInfo.java
 * @package cn.vlabs.clb.api.document
 * @description: 文档元信息
 * @author clive
 * @date 2012-12-28 下午5:28:32
 */

@java.lang.Deprecated
public class MetaInfo {
    /**
     * @description 文档ID号
     */
    public int docid;
    /**
     * @description 文档版本号
     */
    public String version;

    /**
     * @description 文档名称
     */
    public String filename;
    /**
     * @description 创建者
     */
    public String creatBy;
    /**
     * @description 创建时间
     */
    public Date createTime;
    /**
     * @description 最后更新时间
     */
    public Date lastUpdate;
    /**
     * @description 文件大小
     */
    public int size;
    /**
     * @description 是否为公开文件
     */
    public int isPub;

    @java.lang.Deprecated
    public String summary;

    @java.lang.Deprecated
    public String title;

    @java.lang.Deprecated
    public String comment;

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

    @java.lang.Deprecated
    public String getTitle() {
        return title;
    }

    @java.lang.Deprecated
    public void setTitle(String title) {
        this.title = title;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getCreatBy() {
        return creatBy;
    }

    public void setCreatBy(String creatBy) {
        this.creatBy = creatBy;
    }

    @java.lang.Deprecated
    public String getSummary() {
        return summary;
    }

    @java.lang.Deprecated
    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @java.lang.Deprecated
    public String getComment() {
        return comment;
    }

    @java.lang.Deprecated
    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getIsPub() {
        return isPub;
    }

    public void setIsPub(int isPub) {
        this.isPub = isPub;
    }

    public String toString() {
        return "docid:" + this.docid + ",version:" + this.getVersion() + ",filename:" + this.getFilename() + ",isPub:"
                + (isPub == 1);
    }

}
