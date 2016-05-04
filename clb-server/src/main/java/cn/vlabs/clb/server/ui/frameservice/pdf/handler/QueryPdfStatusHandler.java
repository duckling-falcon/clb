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

import java.util.Date;

import cn.vlabs.clb.api.CLBStatus;
import cn.vlabs.clb.api.pdf.PdfStatusCode;
import cn.vlabs.clb.server.config.AppFacade;
import cn.vlabs.clb.server.config.Config;
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

public class QueryPdfStatusHandler extends CLBAbstractAction {

    private PdfFacade pf = AppFacade.getPdfFacade();
    private DocumentFacade df = AppFacade.getDocumentFacade();
    private AuthFacade af = AppFacade.getAuthFacade();

    public void setPf(PdfFacade pf) {
        this.pf = pf;
    }

    public void setDf(DocumentFacade df) {
        this.df = df;
    }

    public void setAf(AuthFacade af) {
        this.af = af;
    }

    @Override
    protected Object doAction(Object arg) throws ServiceException, BaseException {
        try {
            int appid = af.getAppId();
            DPair p = df.convertToPair(appid,arg);
            DocMeta meta = df.getDocMetaByDocid(p.getAppid(),p.getDocid());
            af.checkPermission(meta);
            DocVersion dv = df.getDocVersionWithoutException(p);
            if (dv == null) {
                return PdfStatusCode.NOT_FOUND_SOURCE_FILE;
            }
            PdfItem item = pf.getPdfItemWithoutException(p);
            if (item == null) {
                return PdfStatusCode.UNCONVERT_SOURCE_FILE;
            }

            // step 1. 如果meta中为准备好，则直接返回
            if (CLBStatus.isReady(item.getStatus())) {
                return PdfStatusCode.CONVERT_SUCCESS;
            }

            if (CLBStatus.isNotReady(item.getStatus())) {
                GridFSDBFile dbfile = pf.getPdfFileWithNoException(item.getStorageKey());
                if (dbfile != null) {
                    // step 2. 若meta中状态为等待，且gfile中有内容，则更新meta并更改状态为OK；
                    pf.updateItemToReadyStatus(item, dbfile);
                    return PdfStatusCode.CONVERT_SUCCESS;
                }
                //step 3. 若meta中状态为等待，且gfile中无内容
                ConvertFailInfo cfi = pf.checkConvertErrorLog(item.getStorageKey());
                if (cfi != null || isConvertExpired(item.getUpdateTime())) {
                    // 满足以下条件的视为转换失败：（1） 出错表中有内容； （2） 出错表无内容，但等待时间超过了给定时间阈值
                    pf.updatePdfItemToFailedStatus(item);
                    return PdfStatusCode.CONVERT_FAILED;
                }
                // step 4. 若meta中状态为等待，且gfile中无内容，且出错表中无内容，但等待时间在给定时间阈值内，则返回WAIT
                return PdfStatusCode.CONVERT_ONGOING;
            }
        } catch (BaseException e) {
            exceptionHandler(e);
        }
        return PdfStatusCode.CONVERT_FAILED;
    }
    
    private boolean isConvertExpired(Date createTime){
        Date current = new Date();
        long maxWaitingTime = Config.getInstance().getInt("clb.dconvert.maxWaitingTime", 60*60*1000);
        return current.getTime() - createTime.getTime() > maxWaitingTime;
    }

}
