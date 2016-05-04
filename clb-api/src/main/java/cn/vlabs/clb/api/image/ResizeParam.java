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

/**
 * @title: ResizeParam.java
 * @package cn.vlabs.clb.api.image
 * @description: 图片压缩参数
 * @author clive
 * @date 2012-12-28 下午4:59:37
 */
public class ResizeParam {

    public static final String USE_WIDTH = "width";
    public static final String USE_HEIGHT = "height";

    private int docid;
    private String version;
    /**
     * @description 小图压缩基准像素
     */
    private int smallPoint;
    /**
     * @description 中图压缩基准像素
     */
    private int mediumPoint;
    /**
     * @description 大图压缩基准像素
     */
    private int largePoint;
    /**
     * @description 以长度还是宽度作为基准
     */
    private String useWidthOrHeight;

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

    public String getUseWidthOrHeight() {
        return useWidthOrHeight;
    }

    public void setUseWidthOrHeight(String useWidthOrHeight) {
        this.useWidthOrHeight = useWidthOrHeight;
    }

    public int getSmallPoint() {
        return smallPoint;
    }

    public void setSmallPoint(int smallPoint) {
        this.smallPoint = smallPoint;
    }

    public int getMediumPoint() {
        return mediumPoint;
    }

    public void setMediumPoint(int mediumPoint) {
        this.mediumPoint = mediumPoint;
    }

    public int getLargePoint() {
        return largePoint;
    }

    public void setLargePoint(int largePoint) {
        this.largePoint = largePoint;
    }

}
