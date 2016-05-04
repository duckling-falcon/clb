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
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.IOUtils;
import org.springframework.util.FileCopyUtils;

import cn.vlabs.clb.server.config.AppFacade;
import cn.vlabs.clb.server.exception.BaseException;
import cn.vlabs.clb.server.exception.FileContentNotFoundException;
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

public class GetMultiDocsContentHandler extends CLBAbstractActionWithStream {

    private DocumentFacade df = AppFacade.getDocumentFacade();
    private AuthFacade af = AppFacade.getAuthFacade();

    @Override
    protected Object doAction(Object arg, IResource resource) throws ServiceException {
        try {
            int appid = af.getAppId();
            DPair[] pairs = df.convertToPairs(appid, arg);
            List<DocMeta> metas = df.getDocMetaByIds(appid, pairs);
            af.checkPermission(metas);
            df.replaceDocVersion(pairs, metas);
            List<DocVersion> dvlist = df.getMultipleDocVersion(pairs);
            readDocuments(dvlist, "patch.zip", getRequest(), getResponse());
        } catch (BaseException e) {
            exceptionHandler(e);
        }
        return TakeOver.NO_MESSAGE;
    }

    private void readDocuments(List<DocVersion> dvlist, String zipFileName, HttpServletRequest request,
            HttpServletResponse response) throws FileContentNotFoundException {
        DocumentFacade df = AppFacade.getDocumentFacade();
        OutputStream os = null;
        InputStream is = null;
        ZipArchiveOutputStream zos = null;
        try {
            if (dvlist != null) {
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/zip");
                String headerValue = ResponseHeaderUtils.buildResponseHeader(request, zipFileName, true);
                response.setHeader("Content-Disposition", headerValue);
                os = response.getOutputStream();
                zos = new ZipArchiveOutputStream(os);
                zos.setEncoding("utf-8");
                Map<String, Boolean> dupMap = new HashMap<String, Boolean>();
                GridFSDBFile dbfile = null;
                int i = 1;
                for (DocVersion dv : dvlist) {
                    dbfile = df.readDocContent(dv);
                    if (dbfile != null) {
                        String filename = dbfile.getFilename();
                        ZipArchiveEntry entry = null;
                        if (dupMap.get(filename) != null) {
                            entry = new ZipArchiveEntry((i + 1) + "-" + filename);

                        } else {
                            entry = new ZipArchiveEntry(filename);
                        }
                        entry.setSize(dbfile.getLength());
                        zos.putArchiveEntry(entry);
                        is = dbfile.getInputStream();
                        FileCopyUtils.copy(is, zos);
                        zos.closeArchiveEntry();
                        dupMap.put(filename, true);
                    }
                    i++;
                }
                zos.finish();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(zos);
            IOUtils.closeQuietly(os);
            IOUtils.closeQuietly(is);
        }
    }

}
