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

import com.google.gson.Gson;

import cn.vlabs.clb.api.document.ClbMeta;
import cn.vlabs.clb.server.config.AppFacade;
import cn.vlabs.clb.server.exception.BaseException;
import cn.vlabs.clb.server.ui.frameservice.CLBAbstractAction;
import cn.vlabs.clb.server.ui.frameservice.auth.AuthFacade;
import cn.vlabs.clb.server.ui.frameservice.document.DocumentFacade;
import cn.vlabs.rest.ServiceException;

public class CreateClbRef extends CLBAbstractAction {

	private DocumentFacade df = AppFacade.getDocumentFacade();
	private AuthFacade af = AppFacade.getAuthFacade();
    private Gson mGson = new Gson();

	@Override
	protected Object doAction(Object arg) throws ServiceException,
			BaseException {
		ClbMeta meta = mGson.fromJson((String) arg, ClbMeta.class);
		df.createClbRef(af.getAppId(), meta);
		return null;
	}
}
