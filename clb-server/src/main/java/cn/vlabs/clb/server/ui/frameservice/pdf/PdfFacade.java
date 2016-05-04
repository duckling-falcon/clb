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
package cn.vlabs.clb.server.ui.frameservice.pdf;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.vlabs.clb.api.CLBStatus;
import cn.vlabs.clb.server.exception.FileContentNotFoundException;
import cn.vlabs.clb.server.exception.InvalidFileOperationException;
import cn.vlabs.clb.server.exception.MetaNotFoundException;
import cn.vlabs.clb.server.model.DocVersion;
import cn.vlabs.clb.server.model.PdfItem;
import cn.vlabs.clb.server.service.pdf.IPdfConvertService;
import cn.vlabs.clb.server.service.pdf.IPdfItemService;
import cn.vlabs.clb.server.storage.IStorageService;
import cn.vlabs.clb.server.ui.frameservice.DPair;
import cn.vlabs.clb.server.ui.frameservice.OperationUtils;
import cn.vlabs.dconvert.api.rest.ConvertFailInfo;

import com.mongodb.gridfs.GridFSDBFile;

@Component("PdfFacade")
public class PdfFacade {
    @Autowired
    private IStorageService mongoStorage;
    @Autowired
    private IPdfItemService pdfItemService;
    @Autowired
    private IPdfConvertService pdfConvertService;

    public PdfItem getPdfItem(DPair p) throws MetaNotFoundException {
        PdfItem item = pdfItemService.read(p);
        if (item == null) {
            throw new MetaNotFoundException(p, "pdf");
        }
        return item;
    }

    public PdfItem getPdfItemWithoutException(DPair p) {
        return pdfItemService.read(p);
    }

    public GridFSDBFile getPdfFile(PdfItem item) throws FileContentNotFoundException {
        String storageKey = item.getStorageKey();
        if (storageKey == null) {
            throw new FileContentNotFoundException(item.getAppid(),item.getDocid(), item.getVersion(), null, "pdf");
        }
        GridFSDBFile dbfile = mongoStorage.loadPdf(storageKey);
        if (dbfile == null) {
            throw new FileContentNotFoundException(storageKey, "pdf");
        }
        return dbfile;
    }

    public GridFSDBFile getPdfFileWithNoException(String storageKey) {
        return mongoStorage.loadPdf(storageKey);
    }

    public void checkFileOperation(DocVersion dv) throws InvalidFileOperationException {
        if (!OperationUtils.canConvert(dv.getFileExtension())) {
            throw new InvalidFileOperationException(dv, OperationUtils.OP_CONVERT);
        }
    }

    public PdfItem createPdfItem(DocVersion dv) {
        PdfItem item = new PdfItem();
        item.setAppid(dv.getAppid());
        item.setDocid(dv.getDocid());
        item.setVersion(dv.getVersion());
        item.setStorageKey(mongoStorage.generateStorageKey());
        item.setFilename(dv.getFilename());
        item.setSize(0);
        item.setUpdateTime(new Date());
        item.setStatus(CLBStatus.NOT_READY.toString());
        int id = pdfItemService.create(item);
        item.setId(id);
        return item;
    }

    public void updateItemToReadyStatus(PdfItem item, GridFSDBFile dbfile) {
        item.setStatus(CLBStatus.READY.toString());
        item.setUpdateTime(new Date());
        item.setSize(dbfile.getLength());
        pdfItemService.update(item);
    }

    public void updatePdfItemToFailedStatus(PdfItem item) {
        item.setStatus(CLBStatus.FAILED.toString());
        item.setUpdateTime(new Date());
        pdfItemService.update(item);
    }
    
    public void updatePdfItemToNotReadyStatus(PdfItem item){
        item.setStatus(CLBStatus.NOT_READY.toString());
        item.setUpdateTime(new Date());
        pdfItemService.update(item);
    }

    public void convert(String sourceKey, String targetKey) {
        pdfConvertService.convert(sourceKey, targetKey);
    }

    public ConvertFailInfo checkConvertErrorLog(String storageKey) {
        // 1.若是解析错误
        // 2.若是找不到源文档
        // 3.若是IO读写错误
        // 4.服务器内部错误
        return pdfConvertService.isConvertSuccess(storageKey);
    }

    public String updatePdfItemStorageKey(PdfItem item) {
        String newStorageKey = mongoStorage.generateStorageKey();
        item.setStatus(CLBStatus.NOT_READY.toString());
        item.setStorageKey(newStorageKey);
        pdfItemService.update(item);
        return newStorageKey;
    }

}
