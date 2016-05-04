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

import java.util.Map;

import cn.vlabs.clb.api.AccessForbidden;
import cn.vlabs.clb.api.CLBConnection;
import cn.vlabs.clb.api.ResourceNotFound;
import cn.vlabs.clb.api.document.DocPair;
import cn.vlabs.clb.api.image.IResizeImageService;
import cn.vlabs.clb.api.image.ImageMeta;
import cn.vlabs.clb.api.image.ImageQuery;
import cn.vlabs.clb.api.image.ResizeImage;
import cn.vlabs.clb.api.image.ResizeParam;
import cn.vlabs.rest.IFileSaver;

public class ResizeImageServiceImpl implements IResizeImageService {

    private static final String CREATE_RESIZE_COPY = "image.create.resize.copy";

    private static final String GET_RESIZE_INFO = "image.get.resize.info";

    private static final String GET_IMAGE_CONTENT = "image.get.content";
    
    private static final String QUERY_RESIZE_STATUS = "image.query.resize.status";

    private CLBConnection conn;

    private ResizeParam defaultParam;

    public ResizeImageServiceImpl(CLBConnection conn) {
        this.conn = conn;
        this.defaultParam = new ResizeParam();
        this.defaultParam.setSmallPoint(200);
        this.defaultParam.setMediumPoint(400);
        this.defaultParam.setLargePoint(600);
        this.defaultParam.setUseWidthOrHeight(ResizeParam.USE_WIDTH);
    }

    private ResizeParam cloneResizeParam(ResizeParam src) {
        ResizeParam e = new ResizeParam();
        e.setLargePoint(src.getLargePoint());
        e.setUseWidthOrHeight(src.getUseWidthOrHeight());
        e.setMediumPoint(src.getMediumPoint());
        e.setSmallPoint(src.getSmallPoint());
        return e;
    }

    public ResizeImageServiceImpl(CLBConnection conn, int defaultSmallSize, int defaultMeduimSize,
            int defaultLargeSize, String sizeType) {
        this.conn = conn;
        this.defaultParam = new ResizeParam();
        this.defaultParam.setLargePoint(defaultLargeSize);
        this.defaultParam.setUseWidthOrHeight(sizeType);
        this.defaultParam.setMediumPoint(defaultMeduimSize);
        this.defaultParam.setSmallPoint(defaultSmallSize);
    }

    @Override
    public ResizeImage resize(int docid, String version) {
        ResizeParam rzp = cloneResizeParam(defaultParam);
        rzp.setDocid(docid);
        rzp.setVersion(version);
        return (ResizeImage) conn.sendService(CREATE_RESIZE_COPY, rzp);
    }

    @Override
    public ResizeImage resize(int docid, String version, final ResizeParam resizeParam) {
        ResizeParam rzp = cloneResizeParam(resizeParam);
        rzp.setDocid(docid);
        rzp.setVersion(version);
        return (ResizeImage) conn.sendService(CREATE_RESIZE_COPY, rzp);
    }

    @Override
    public ResizeImage getResizeImage(int docid, String version) {
        return (ResizeImage) conn.sendService(GET_RESIZE_INFO, new DocPair(docid, version));
    }

    @Override
    public void getContent(int docid, String version, String resizeType, IFileSaver fs) throws ResourceNotFound,
            AccessForbidden {
        buildContentRequest(docid, version, resizeType,null,true, fs);
    }
    
    @Override
    public void getContent(ImageQuery query, IFileSaver fs){
        if(query!=null){
            conn.sendService(GET_IMAGE_CONTENT, query, fs);
        }
    }

    private void buildContentRequest(int docid, String version, String resizeType,ResizeParam param, boolean needOrginal, IFileSaver fs) {
        ImageQuery query = new ImageQuery();
        query.setDocid(docid);
        query.setVersion(version);
        query.setResizeType(resizeType);
        query.setResizeParam(param);
        query.setNeedOrignal(needOrginal);
        conn.sendService(GET_IMAGE_CONTENT, query, fs);
    }
    
    public void getContent(int docid, String version, String resizeType, boolean needOrginal, IFileSaver fs) {
        buildContentRequest(docid, version, resizeType, null, needOrginal, fs);
    }


    @Override
    public void getContent(int docid, String version, String resizeType, ResizeParam resizeParam, IFileSaver fs) {
        buildContentRequest(docid,version,resizeType,resizeParam,true,fs);
    }

    @Override
    public Map<String,ImageMeta> queryResizeStatus(int docid, String version) {
        DocPair pair = new DocPair(docid,version);
        return (Map<String,ImageMeta>)conn.sendService(QUERY_RESIZE_STATUS, pair);
    }

}
