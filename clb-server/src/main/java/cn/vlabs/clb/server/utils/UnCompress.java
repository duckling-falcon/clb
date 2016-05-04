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

package cn.vlabs.clb.server.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipException;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.util.FileCopyUtils;

import cn.vlabs.clb.server.service.search.fulltext.parser.ParserReadable;
import cn.vlabs.clb.server.ui.frameservice.document.ZipEntryAdapter;

public class UnCompress {
    private static Logger log = Logger.getLogger(UnCompress.class);;
    private ExtractListener listener;
    private String encoding = null;

    public UnCompress(ExtractListener listener, String encoding) {
        this.listener = listener;
        this.encoding = encoding;
    }

    public UnCompress(ExtractListener listener) {
        this(listener, null);
    }

    public UnCompress(String outputDir) {
        File dir = new File(outputDir);
        if (dir.isDirectory())
            this.listener = new DefaultListener(outputDir);
        else
            throw new IllegalArgumentException();
    }

    public void unzip(File file) {
        ZipFile zipfile = null;
        try {
            if (encoding == null)
                zipfile = new ZipFile(file);
            else
                zipfile = new ZipFile(file, encoding);
            Enumeration<ZipArchiveEntry> iter = zipfile.getEntries();
            while (iter.hasMoreElements()) {
                ZipArchiveEntry entry = iter.nextElement();
                if (!entry.isDirectory()) {
                    InputStream entryis = null;
                    try {
                        entryis = zipfile.getInputStream(entry);
                        listener.onNewEntry(new ZipEntryAdapter(entry, zipfile));
                    } finally {
                        IOUtils.closeQuietly(entryis);
                    }
                }
            }
        } catch (ZipException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error("file not found " + e.getMessage(), e);
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e) {
                    log.error(e);
                }
            }
        }
    }

    public void unzip(String filename) {
        File file = new File(filename);
        unzip(file);
    }

    private class DefaultListener implements ExtractListener {
        private String outputDir;

        public DefaultListener(String outputDir) {
            this.outputDir = outputDir;
        }

        public void onNewEntry(ParserReadable readable) throws IOException {
            String outputFile = outputDir + File.separator + readable.getName();
            OutputStream out = null;
            InputStream in = null;
            try {
                in = readable.getInputStream();
                out = new BufferedOutputStream(new FileOutputStream(outputFile));
                // copyContent(in, out);
                FileCopyUtils.copy(in, out);
            } catch (FileNotFoundException e) {
            } finally {
                IOUtils.closeQuietly(out);
                IOUtils.closeQuietly(in);
            }
        }
    }

}
