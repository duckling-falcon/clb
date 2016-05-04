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

package cn.vlabs.clb.api;

import org.apache.log4j.Logger;

import cn.vlabs.rest.IFileSaver;
import cn.vlabs.rest.ServiceClient;
import cn.vlabs.rest.ServiceContext;
import cn.vlabs.rest.ServiceException;
import cn.vlabs.rest.stream.IResource;

/**
 * @Description: 用于向CLB Server端发送请求
 * @author CERC
 * @date Jul 11, 2011 5:23:43 PM
 * 
 */
public class CLBConnection {
    
	private static final String LOGIN = "login";

	private static final long TWENTY_FIVE_MINUTES = 25L * 60 * 1000;
	
	private static final Logger LOG = Logger.getLogger(CLBConnection.class);

	private CLBAuthInfo auth;

	private boolean autoLogin;

	private ServiceClient client;

	private ServiceContext context;

	private Gauge sessionGauge;
	
	private CLBServerVersion serverVersion;

	public CLBServerVersion getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(CLBServerVersion serverVersion) {
        this.serverVersion = serverVersion;
    }

    public CLBConnection(String url, CLBAuthInfo auth) {
		context = new ServiceContext(url);
		context.setTimeout(60000);
		context.setKeepAlive(true);
		if (auth instanceof CLBPasswdInfo) {
			context.setAppname("");
			context.setApppassword("");
		}
		client = new ServiceClient(context);
		this.auth = auth;
		this.sessionGauge = new Gauge(TWENTY_FIVE_MINUTES);
		this.serverVersion = CLBServerVersion.CURRENT;
	}

	/**
	* @author: CERC
	* @Description: 发送一般请求
	* @param service
	* @param param
	* @return TODO
	* @return Object   
	* @throws
	*/
	public Object sendService(String service, Object param) {
		ensureLogin();
		int count = 0;
		boolean retry = false;
		Object result = null;
		do {
			count++;
			try {
				result = client.sendService(service, param);
			} catch (ServiceException e) {
				if (e.getCode() == ErrorCode.NEED_AUTH) {
					if (login()) {
						retry = true;
					} else {
						throw new AccessForbidden("登录信息错误，无法获得授权。");
					}
				} else {
					throw ErrorCode.transform(e);
				}
			}
		} while (retry && count < 2);
		sessionGauge.refreshAccessTime();
		return result;
	}

	
	
	/**
	* @author: CERC
	* @Description: 用于下载的请求
	* @param service
	* @param param
	* @param fs TODO
	* @return void   
	* @throws
	*/
	public void sendService(String service, Object param, IFileSaver fs) {
		ensureLogin();
		int count = 0;
		boolean retry = false;
		do {
			count++;
			try {
				client.sendService(service, param, fs);
			} catch (ServiceException e) {
				if (e.getCode() == ErrorCode.NEED_AUTH) {
					if (login()) {
						retry = true;
					} else {
						throw new AccessForbidden("登录信息错误，无法获得授权。");
					}
				} else
					throw ErrorCode.transform(e);
			}
		} while (retry && count < 2);
		sessionGauge.refreshAccessTime();
	}


	/**
	* @author: CERC
	* @Description: 用于上传
	* @param service
	* @param param
	* @param si
	* @return 
	* @return Object   
	* @throws
	*/
	public Object sendService(String service, Object param, IResource si) {
		ensureLogin();
		int count = 0;
		boolean retry = false;
		Object result = null;
		do {
			count++;
			try {
				result = client.sendService(service, param, si);
			} catch (ServiceException e) {
				if (e.getCode() == ErrorCode.NEED_AUTH) {
					if (login()) {
						retry = true;
					} else {
						throw new AccessForbidden("登录信息错误，无法获得授权。");
					}
				} else{
				    LOG.error(e.getMessage(),e);
				    throw ErrorCode.transform(e);
				}
			}
		} while (retry && count < 2);
		sessionGauge.refreshAccessTime();
		return result;
	}

	/**
	* @author: CERC
	* @Description: 刷新session并登录 
	* @return void   
	* @throws
	*/
	public void ensureLogin() {
		synchronized (sessionGauge) {
			if (sessionGauge.isTimeOut()) {
				if (login()) {
					sessionGauge.refreshAccessTime();
				} else {
					throw new AccessForbidden("登录信息错误，无法获得授权。");
				}
			}
		}
	}
	
	/**
	* @author: CERC
	* @Description: app登录
	* @return 
	* @return boolean   
	* @throws
	*/
	private boolean login() {
		try {
			return (Boolean) client.sendService(LOGIN, auth);
		} catch (ServiceException e) {
		    LOG.error(e.getMessage(),e);
			throw ErrorCode.transform(e);
		}
	}

	public void releaseConnection() {
		context.close();
	}

	public boolean autoLogin() {
		return autoLogin;
	}

	public void setAutoLogin(boolean autologin) {
		this.autoLogin = autologin;
	}

	public void setKeepAlive(boolean alive) {
		context.setKeepAlive(alive);
	}

	public void setSessionTime(int minSessionTime) {
		long interval = minSessionTime;
		interval = interval * 60 * 1000;
		sessionGauge.setInterval(interval);
	}


}
