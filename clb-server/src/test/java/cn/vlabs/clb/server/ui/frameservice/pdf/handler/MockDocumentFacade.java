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

import cn.vlabs.clb.server.exception.InvalidArgumentException;
import cn.vlabs.clb.server.exception.MetaNotFoundException;
import cn.vlabs.clb.server.model.DocMeta;
import cn.vlabs.clb.server.model.DocVersion;
import cn.vlabs.clb.server.ui.frameservice.DPair;
import cn.vlabs.clb.server.ui.frameservice.document.DocumentFacade;

public class MockDocumentFacade extends DocumentFacade {

	public DPair convertToPair(int appid, Object o) throws InvalidArgumentException {
        if(appid == 1){
        	//用于QueryPdfStatusHandlerTest中的testDoActionForExpiredPdfConvert()
        	return new DPair(1,1);
        }
        return null;
    }
	
	public DocMeta getDocMetaByDocid(int appid, int docid) throws MetaNotFoundException {
        if (appid == 1 && docid == 1) {
        	//用于QueryPdfStatusHandlerTest中的testDoActionForExpiredPdfConvert()
            return new DocMeta();
        }
        return null;
    }
	
	public DocVersion getDocVersionWithoutException(DPair p) {
		if(p.getAppid() ==1 && p.getDocid() ==1){
			//用于QueryPdfStatusHandlerTest中的testDoActionForExpiredPdfConvert()
			return new DocVersion();
		}
        return null;
    }
}
