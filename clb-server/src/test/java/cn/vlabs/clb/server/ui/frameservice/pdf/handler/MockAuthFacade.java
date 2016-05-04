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

import cn.vlabs.clb.server.exception.NoAuthorityException;
import cn.vlabs.clb.server.model.DocMeta;
import cn.vlabs.clb.server.ui.frameservice.auth.AuthFacade;

public class MockAuthFacade extends AuthFacade{
	public int getAppId() {
		// return 1 用于QueryPdfStatusHandlerTest中的testDoActionForExpiredPdfConvert()
        return 1;
    }
	
	public void checkPermission(DocMeta meta) throws NoAuthorityException {
		// return 用于QueryPdfStatusHandlerTest中的testDoActionForExpiredPdfConvert()
		//若其他测试需要抛出异常，请使用条件区分
        return;
    }
}
