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
import cn.vlabs.clb.api.CLBAuthInfo;
import cn.vlabs.clb.api.CLBPasswdInfo;
import cn.vlabs.clb.api.ErrorCode;
import cn.vlabs.rest.ServiceClient;
import cn.vlabs.rest.ServiceContext;
import cn.vlabs.rest.ServiceException;

public class AuthorizedService {
	public AuthorizedService(String url, CLBAuthInfo auth){
		context = new ServiceContext(url);
		context.setKeepAlive(false);
		if (auth instanceof CLBPasswdInfo){
			context.setAppname("");
			context.setApppassword("");
		}
		client = new ServiceClient(context);
		if (!login(auth)){
			throw new AccessForbidden("无法正确登录服务器。");
		}
	}
	private boolean login(CLBAuthInfo auth){
		try {
			return (Boolean)getClient().sendService(LOGIN, auth);
		} catch (ServiceException e) {
			throw ErrorCode.transform(e);
		}
	}
	
	public void releaseConnection(){
		context.close();
	}
	protected ServiceClient getClient() {
		return client;
	}

	private ServiceContext context;
	private ServiceClient client;
	private static final String LOGIN="login";
}
