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
package cn.vlabs.clb.server.ui.frameservice.image.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import cn.vlabs.clb.api.image.ImageQuery;
import cn.vlabs.clb.api.image.ResizeParam;
import cn.vlabs.clb.server.config.AppFacade;
import cn.vlabs.clb.server.exception.BaseException;
import cn.vlabs.clb.server.exception.ResizeNotReadyException;
import cn.vlabs.clb.server.model.DocMeta;
import cn.vlabs.clb.server.model.DocVersion;
import cn.vlabs.clb.server.model.ImageItem;
import cn.vlabs.clb.server.ui.frameservice.CLBAbstractActionWithStream;
import cn.vlabs.clb.server.ui.frameservice.DPair;
import cn.vlabs.clb.server.ui.frameservice.auth.AuthFacade;
import cn.vlabs.clb.server.ui.frameservice.document.DocumentFacade;
import cn.vlabs.clb.server.ui.frameservice.image.ImageFacade;
import cn.vlabs.clb.server.utils.ResponseHeaderUtils;
import cn.vlabs.rest.ServiceException;
import cn.vlabs.rest.TakeOver;
import cn.vlabs.rest.stream.IResource;

import com.mongodb.gridfs.GridFSDBFile;

public class GetImageContentHandler extends CLBAbstractActionWithStream {
    
    private static final Logger LOG = Logger.getLogger(GetImageContentHandler.class);
    
    private DocumentFacade df = AppFacade.getDocumentFacade();
    private ImageFacade imf = AppFacade.getImageFacade();
    private AuthFacade af = AppFacade.getAuthFacade();

    @Override
    protected Object doAction(Object arg, IResource resource) throws ServiceException {
        try {
            int appid = af.getAppId();
            ImageQuery q = (ImageQuery) arg;
            DocMeta meta = df.getDocMetaByDocid(appid, q.getDocid());
            if ("latest".equals(q.getVersion())) {
                q.setVersion(meta.getLastVersion() + "");
            }
            af.checkPermission(meta);
            ImageItem item = imf.getImageItem(appid, q);
            GridFSDBFile dbfile = null;
            DPair p = new DPair(appid, q.getDocid(), Integer.parseInt(q.getVersion()));
            if (item != null) {
                dbfile = imf.readImgContent(item.getStorageKey()); //读取压缩文件
                if(dbfile == null){
                    if(q.isNeedOrignal()){  //当前格式还没有转换好且需要原图时
                        LOG.info("Return orignal image for doc[appid="+p.getAppid()+",docid="+p.getDocid()+"], because resize image is not ready.");
                        dbfile = df.readDocContent(p); //返回原始图片
                    }else{ //不需要原图时
                        throwResizeNotReady(appid, q);
                    }
                }
            } else {
                if (q.getResizeParam() != null) { //重发一个压缩请求
                    ResizeParam rzp = q.getResizeParam();
                    DocVersion dv = df.getDocVersion(p);
                    imf.checkFileOperation(dv);
                    GridFSDBFile gfile = df.readDocContent(dv);
                    imf.resizeImage(dv, gfile, rzp);
                }
                if(q.isNeedOrignal()){
                    dbfile = df.readDocContent(p); //返回原始图片
                } else {
                    throwResizeNotReady(appid, q);
                }
            }
            writeFileContentToResponse(dbfile, getRequest(), getResponse());
        } catch (BaseException e) {
            exceptionHandler(e);
        }
        return TakeOver.NO_MESSAGE;
    }

    private void throwResizeNotReady(int appid, ImageQuery q) throws ResizeNotReadyException {
        throw new ResizeNotReadyException("Resize image is not ready for appid="+ appid +",docid="+q.getDocid()+",version=" + q.getVersion());
    }

    private void writeFileContentToResponse(GridFSDBFile dbfile, HttpServletRequest request,
            HttpServletResponse response) {
        OutputStream os = null;
        try {
            long start = System.currentTimeMillis();
            response.setCharacterEncoding("utf-8");
            response.setContentLength((int) dbfile.getLength());
            response.setContentType("application/x-download");
            String filename = dbfile.getFilename();
            if (filename == null) {
                filename = "file" + dbfile.getId();
            }
            String headerValue = ResponseHeaderUtils.buildResponseHeader(request, filename, true);
            response.setHeader("Content-Disposition", headerValue);
            response.setHeader("Content-Length", dbfile.getLength() + "");
            os = response.getOutputStream();
            dbfile.writeTo(os);
            long end = System.currentTimeMillis();
            LOG.info("Read image content using stream mode for image ["+ dbfile.getId() +"], use time "+(end-start)+"ms");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(os);
        }
    }
}
