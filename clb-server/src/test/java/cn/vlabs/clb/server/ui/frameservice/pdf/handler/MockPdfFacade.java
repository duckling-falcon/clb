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

import java.util.Calendar;
import java.util.Date;

import cn.vlabs.clb.api.CLBStatus;
import cn.vlabs.clb.server.model.PdfItem;
import cn.vlabs.clb.server.ui.frameservice.DPair;
import cn.vlabs.clb.server.ui.frameservice.pdf.PdfFacade;

import com.mongodb.gridfs.GridFSDBFile;

public class MockPdfFacade extends PdfFacade {
	
	private static final String STORAGE_KEY = "storagekey";
	
	public PdfItem getPdfItemWithoutException(DPair p) {
		if(p.getAppid()==1 && p.getDocid()==1){ 
			//用于QueryPdfStatusHandlerTest中的testDoActionForExpiredPdfConvert()
			PdfItem pi = new PdfItem(1,1);
			pi.setStorageKey(STORAGE_KEY);
			pi.setStatus(CLBStatus.NOT_READY.toString());
			Calendar cal = Calendar.getInstance();
			cal.set(2013, 3, 18, 0, 0, 0);
			pi.setUpdateTime(new Date(cal.getTimeInMillis()));
			return pi;
		}
        return null;
    }
	
	public GridFSDBFile getPdfFileWithNoException(String storageKey) {
		//用于QueryPdfStatusHandlerTest中的testDoActionForExpiredPdfConvert()
        return null;
    }
	

}
