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
package cn.vlabs.clb.server.ui.frameservice.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import cn.vlabs.clb.api.io.impl.MimeType;
import cn.vlabs.clb.server.model.DocVersion;
import cn.vlabs.clb.server.service.search.fulltext.parser.ParserReadable;
import cn.vlabs.clb.server.storage.mongo.MFile;
import cn.vlabs.clb.server.ui.frameservice.LocalFileCacheService;
import cn.vlabs.clb.server.ui.frameservice.OperationUtils;
import cn.vlabs.clb.server.ui.frameservice.SrcPair;

public class FileWriterHelper {

    private static final Logger LOG = Logger.getLogger(FileWriterHelper.class);

    private DocumentFacade df = null;

    private LocalFileCacheService localCacheService;

    public FileWriterHelper(DocumentFacade df) {
        this.df = df;
        this.localCacheService = df.getLocalCacheService();
    }

    public void saveFileContent(int isPub, ParserReadable readable, int appid, DocVersion dv) {
        InputStream ins = null;
        MFile mf = df.buildMongoFile(appid, dv, isPub, readable);
        try {
            String suffix = MimeType.getSuffix(dv.getFilename());
            if (OperationUtils.canResize(suffix)) { // 如果是图片的话，缓存一份数据到本地
                SrcPair p = localCacheService.saveTempFile(mf.getStorageKey().toString(), readable.getInputStream());
                File f = new File(p.getSrcPath());
                ins = new FileInputStream(f);
                df.saveDocumentContent(mf, ins);
            } else {
                ins = readable.getInputStream();
                df.saveDocumentContent(mf, ins);
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(ins);
        }
    }

    public static void test() {
        try {
            System.out.println("in body");
            throw new RuntimeException();
        } catch (Exception e) {
            System.out.println("in exception");
            return;
        } finally {
            System.out.println("in finally");
        }
    }

    public static void main(String[] args) {
        test();
    }
}
