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

package cn.vlabs.clb.server.ui.frameservice.document.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import cn.vlabs.clb.server.config.AppFacade;
import cn.vlabs.clb.server.exception.BaseException;
import cn.vlabs.clb.server.exception.ReadUncompletedDocumentException;
import cn.vlabs.clb.server.model.DocMeta;
import cn.vlabs.clb.server.model.DocVersion;
import cn.vlabs.clb.server.ui.frameservice.CLBAbstractActionWithStream;
import cn.vlabs.clb.server.ui.frameservice.DPair;
import cn.vlabs.clb.server.ui.frameservice.auth.AuthFacade;
import cn.vlabs.clb.server.ui.frameservice.document.DocumentFacade;
import cn.vlabs.clb.server.utils.ResponseHeaderUtils;
import cn.vlabs.rest.ServiceException;
import cn.vlabs.rest.TakeOver;
import cn.vlabs.rest.stream.IResource;

import com.mongodb.gridfs.GridFSDBFile;

public class GetDocContentHandler extends CLBAbstractActionWithStream {

    private static final Logger LOG = Logger.getLogger(GetDocContentHandler.class);

    private DocumentFacade df = AppFacade.getDocumentFacade();
    private AuthFacade af = AppFacade.getAuthFacade();

    @Override
    protected Object doAction(Object arg, IResource resource) throws ServiceException {
        try {
            int appid = af.getAppId();
            DPair p = df.convertToPair(appid, arg);
            DocMeta meta = df.getDocMetaByDocid(p.getAppid(), p.getDocid());
            af.checkPermission(meta);
            GridFSDBFile dbfile = null;
            DocVersion dv = df.getDocVersion(p);
            if (dv.isCompletedStatus()) {
                dbfile = df.readDocContent(dv);
            } else {
                LOG.error("Can not download a file which is not completed to upload.");
                throw new ReadUncompletedDocumentException(appid,p.getDocid(),dv.getVersion(),dv.getFilename());
            }
            if (dbfile != null) {
                LOG.info("Begin to send file content for [appid=" + appid + ",docid=" + p.getDocid() + ",filename=" + dbfile.getFilename() + "]");
            }
            writeFileContentToResponse(dbfile, getRequest(), getResponse());
        } catch (BaseException e) {
            exceptionHandler(e);
        }
        return TakeOver.NO_MESSAGE;
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
            LOG.debug(dbfile.getFilename() + "," + dbfile.getLength());
            String headerValue = ResponseHeaderUtils.buildResponseHeader(request, filename, true);
            response.setHeader("Content-Disposition", headerValue);
            response.setHeader("Content-Length", dbfile.getLength() + "");
            os = response.getOutputStream();
            dbfile.writeTo(os);
            long end = System.currentTimeMillis();
            LOG.info("Read doc content using stream mode for doc [" + filename + "], use time " + (end - start) + "ms");
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
