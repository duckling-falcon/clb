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
package cn.vlabs.clb.server.ui.frameservice.pdf.handler;

import org.apache.log4j.Logger;

import cn.vlabs.clb.api.CLBStatus;
import cn.vlabs.clb.server.config.AppFacade;
import cn.vlabs.clb.server.exception.BaseException;
import cn.vlabs.clb.server.model.DocMeta;
import cn.vlabs.clb.server.model.DocVersion;
import cn.vlabs.clb.server.model.PdfItem;
import cn.vlabs.clb.server.ui.frameservice.CLBAbstractAction;
import cn.vlabs.clb.server.ui.frameservice.DPair;
import cn.vlabs.clb.server.ui.frameservice.auth.AuthFacade;
import cn.vlabs.clb.server.ui.frameservice.document.DocumentFacade;
import cn.vlabs.clb.server.ui.frameservice.pdf.PdfFacade;
import cn.vlabs.dconvert.api.rest.ConvertFailInfo;
import cn.vlabs.rest.ServiceException;

import com.mongodb.gridfs.GridFSDBFile;

public class ConvertDocumentHandler extends CLBAbstractAction {

    private static final Logger LOG = Logger.getLogger(ConvertDocumentHandler.class);
    
    private PdfFacade pf= AppFacade.getPdfFacade();
    private DocumentFacade df = AppFacade.getDocumentFacade();
    private AuthFacade af = AppFacade.getAuthFacade();

    @Override
    protected Object doAction(Object arg) throws ServiceException, BaseException {
        try {
            df.checkSystemIOMode("convertPdf");
            int appid = af.getAppId();
            DPair p = df.convertToPair(appid,arg);
            DocMeta meta = df.getDocMetaByDocid(p.getAppid(),p.getDocid());
            af.checkPermission(meta);
            DocVersion dv = df.getDocVersion(p);
            pf.checkFileOperation(dv);
            // step3. 如果已经存在记录,并是失败状态,则重新发送转换请求，更新状态
            PdfItem oldItem = pf.getPdfItemWithoutException(p);
            if (oldItem != null) {
                if (CLBStatus.isReady(oldItem.getStatus())) {
                    // step 1. 若该文档已经转化过且是OK状态，则不做任何事情
                    return null;
                }
                if (CLBStatus.isFailed(oldItem.getStatus())){ 
                    // step 1.5 若文档转化失败，则更新状态为not_ready，重新转化
                    pf.updatePdfItemToNotReadyStatus(oldItem);
                    pf.convert(dv.getStorageKey(), oldItem.getStorageKey());
                }
                if (CLBStatus.isNotReady(oldItem.getStatus())) {
                    ConvertFailInfo cfi = pf.checkConvertErrorLog(oldItem.getStorageKey());
                    if (cfi != null) {
                        // step 2. 若该文档有出错记录，则重新转换一次
                        String newStorageKey = pf.updatePdfItemStorageKey(oldItem);
                        pf.convert(dv.getStorageKey(), newStorageKey);
                    } else {
                        // step 3. 若文档已经转化记录,且无出错记录，但有内容数据，则更新状态
                        GridFSDBFile gfile = pf.getPdfFileWithNoException(oldItem.getStorageKey());
                        if (gfile != null) {
                            pf.updateItemToReadyStatus(oldItem, gfile);
                        } else {
                            // step 4. 若该文档已经转化记录，且为等待状态，且无出错记录，则继续等待呗
                            return null;
                        }
                    }
                }
            } else {
                PdfItem item = pf.createPdfItem(dv);
                pf.convert(dv.getStorageKey(),item.getStorageKey());
                LOG.info("Send a convert event for document["+dv.toBriefString()+"]");
            }
        } catch (BaseException e) {
            exceptionHandler(e);
        }
        return null;
    }
}
