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
package cn.vlabs.clb.api.image;

import java.util.List;

/**
 * @title: ResizeImage.java
 * @package cn.vlabs.clb.api.image
 * @description: 图片压缩后的数据结构
 * @author clive
 * @date 2012-12-28 下午4:56:57
 */
public class ResizeImage {
    /**
     * @description 原始图片文档号
     */
    private int docid;
    /**
     * @description 原始图片版本号
     */
    private String version;
    /**
     * @description 原始图片文件名称
     */
    private String filename;
    /**
     * @description 原始图片直接下载地址
     */
    private String originalURL;
    /**
     * @description 最小图片下载地址
     */
    private String smallURL;
    /**
     * @description 中等图片下载地址
     */
    private String mediumURL;
    /**
     * @description 较大图片的下载地址
     */
    private String largeURL;
    
    private List<ImageMeta> metaList;
    
    public List<ImageMeta> getMetaList() {
        return metaList;
    }

    public void setMetaList(List<ImageMeta> metaList) {
        this.metaList = metaList;
    }

    public String getMediumURL() {
        return mediumURL;
    }

    public void setMediumURL(String mediumURL) {
        this.mediumURL = mediumURL;
    }

    public String getLargeURL() {
        return largeURL;
    }

    public void setLargeURL(String largeURL) {
        this.largeURL = largeURL;
    }

    public String getOriginalURL() {
        return originalURL;
    }

    public void setOriginalURL(String originalURL) {
        this.originalURL = originalURL;
    }

    public String getSmallURL() {
        return smallURL;
    }

    public void setSmallURL(String smallURL) {
        this.smallURL = smallURL;
    }

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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

}
