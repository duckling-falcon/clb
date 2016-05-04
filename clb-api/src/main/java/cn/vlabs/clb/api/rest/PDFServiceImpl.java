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

import cn.vlabs.clb.api.AccessForbidden;
import cn.vlabs.clb.api.CLBConnection;
import cn.vlabs.clb.api.ResourceNotFound;
import cn.vlabs.clb.api.UnImplemented;
import cn.vlabs.clb.api.document.DocPair;
import cn.vlabs.clb.api.pdf.IPdfService;
import cn.vlabs.rest.IFileSaver;

public class PDFServiceImpl implements IPdfService {

    private static final String CONVERT_DOCUMENT = "pdf.convert";
    private static final String GET_PDF_CONTENT = "pdf.get.content";
    private static final String GET_DIRECT_URL = "pdf.direct.url";
    private static final String QUERY_STATUS = "pdf.query.status";

    private static final String GET_PDF_DOCUMENT = "document.pdfviewer";

    private static final String QUERY_PDFEXIST = "document.pdfexist";

    private static final String SEND_PDF_TRANSFORM_EVENT = "document.pdftransform";

    private CLBConnection conn;

    public PDFServiceImpl(CLBConnection conn) {
        this.conn = conn;
    }

    @Override
    public void getPdfContent(int docid, String version, IFileSaver fs) throws ResourceNotFound, AccessForbidden {
        switch (conn.getServerVersion()) {
        case PREVIOUS:
            conn.sendService(GET_PDF_DOCUMENT, new DocPair(docid,version), fs);
            break;
        default:
            conn.sendService(GET_PDF_CONTENT, new DocPair(docid,version), fs);
            break;
        }
    }

    @Override
    public int queryPdfStatus(int docid, String version) {
        switch (conn.getServerVersion()) {
        case PREVIOUS:
            return (int) conn.sendService(QUERY_PDFEXIST, new DocPair(docid,version));
        default:
            return (int) conn.sendService(QUERY_STATUS, new DocPair(docid,version));
        }
    }

    @Override
    public void sendPdfConvertEvent(int docid, String version) throws ResourceNotFound, AccessForbidden {
        switch (conn.getServerVersion()) {
        case PREVIOUS:
            conn.sendService(SEND_PDF_TRANSFORM_EVENT, new DocPair(docid,version));
            break;
        default:
            conn.sendService(CONVERT_DOCUMENT, new DocPair(docid,version));
            break;
        }
    }

    @Override
    public String getPDFURL(int docid, String version) {
        switch (conn.getServerVersion()) {
        case PREVIOUS:
            throw new UnImplemented("This function will be implement in next version");
        default:
            return (String) conn.sendService(GET_DIRECT_URL, new DocPair(docid,version));
        }
    }

}
