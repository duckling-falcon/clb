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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.springframework.util.FileCopyUtils;

import cn.vlabs.clb.api.ErrorCode;
import cn.vlabs.clb.api.document.CreateInfo;
import cn.vlabs.clb.api.document.UpdateInfo;
import cn.vlabs.clb.server.config.AppFacade;
import cn.vlabs.clb.server.config.Config;
import cn.vlabs.clb.server.exception.BaseException;
import cn.vlabs.clb.server.exception.LocalFileIOException;
import cn.vlabs.clb.server.model.DocVersion;
import cn.vlabs.clb.server.service.search.fulltext.parser.ParserReadable;
import cn.vlabs.clb.server.ui.frameservice.CLBAbstractActionWithStream;
import cn.vlabs.clb.server.ui.frameservice.auth.AuthFacade;
import cn.vlabs.clb.server.ui.frameservice.document.DocumentFacade;
import cn.vlabs.clb.server.ui.frameservice.document.FileWriterHelper;
import cn.vlabs.clb.server.utils.ExtractListener;
import cn.vlabs.clb.server.utils.UnCompress;
import cn.vlabs.rest.ServiceException;
import cn.vlabs.rest.stream.IResource;

public class CreateMultiDocsHandler extends CLBAbstractActionWithStream {

    private CreateInfo info = new CreateInfo();
    private String title;
    private HashMap<String, UpdateInfo> infos = new HashMap<String, UpdateInfo>();
    private String filename;
    private AuthFacade af = AppFacade.getAuthFacade();
    private DocumentFacade df = AppFacade.getDocumentFacade();

    @Override
    protected Object doAction(Object arg, IResource resource) throws ServiceException {
        try {
            df.checkSystemIOMode("createMultipleDocument");
        } catch (BaseException e) {
            exceptionHandler(e);
        }
        info = (CreateInfo) arg;
        filename = resource.getFilename();
        if (!isZipFile()) {
            throw new ServiceException(ErrorCode.BAD_PARAMETER, "Invalid parameters");
        }
        String tempPath = Config.getInstance().getStringProp("directory.temp", "/tmp");
        File tmpZipFile = new File(tempPath + File.separator + filename);
        try {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(tmpZipFile);
                FileCopyUtils.copy(resource.getInputStream(), fos);
            } catch (IOException e) {
                exceptionHandler(new LocalFileIOException("Copy file has error.", e.getMessage()));
            } finally {
                IOUtils.closeQuietly(fos);
            }
            String encode = Config.getInstance().getStringProp("System.encoding", "utf-8");
            int appid = af.getAppId();
            UnCompress util = null;
            if (encode != null && encode.length() > 0) {
                util = new UnCompress(new BatchCreateListener(appid), encode);
            } else {
                util = new UnCompress(new BatchCreateListener(appid));
            }
            util.unzip(tmpZipFile);
            return infos;
        } finally {
            tmpZipFile.delete();
        }
    }

    private boolean isZipFile() {
        return filename.toLowerCase().endsWith(".zip");
    }

    private class BatchCreateListener implements ExtractListener {
        public void onNewEntry(ParserReadable readable) throws IOException {
            setTitle(readable.getName());
            DocumentFacade df = AppFacade.getDocumentFacade();
            int docid = df.createDocMeta(appid, info.isPub);
            DocVersion dv = df.createDocVersion(appid, docid, info.title, readable.getSize());
            FileWriterHelper helper = new FileWriterHelper(df);
            helper.saveFileContent(info.isPub, readable, appid, dv);
            UpdateInfo ui = new UpdateInfo();
            ui.setDocid(dv.getDocid());
            ui.setVersion(dv.getVersion() + "");
            infos.put(readable.getName(), ui);
        }

        private int appid;

        public BatchCreateListener(int appid) {
            this.appid = appid;
        }
    }

    private void setTitle(String filename) {
        filename = trim(filename);
        String filepart = null;
        if (filename != null) {
            int pos = filename.indexOf('.');
            if (pos != -1)
                filepart = filename.substring(0, pos);
            else
                filepart = filename;
        }
        if (filepart != null) {
            if (title == null || title.trim().equals(""))
                info.title = filepart;
            else
                info.title = title + "-" + filepart;
        }
    }

    private String trim(String filename) {
        int slashIndex = filename.lastIndexOf('/');
        int backSlash = filename.lastIndexOf('\\');
        if (slashIndex != -1) {
            return filename.substring(slashIndex + 1);
        }
        if (backSlash != -1)
            return filename.substring(backSlash + 1);
        return filename;
    }
}
