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

package cn.vlabs.clb.server.dao;

import cn.vlabs.clb.server.exception.BaseException;



/**
 * 所有数据库操作相关异常的父类
 * @author xiejianjun
 *
 */
public class GeneralDataBaseException extends BaseException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6805187847129326293L;

	public GeneralDataBaseException(String message, Throwable e){
		super(message,e);
	}
	public GeneralDataBaseException(String message){
		super(message);
	}
	
	public GeneralDataBaseException(Throwable e){
		super(e);
	}
    @Override
    public int getErrorCode() {
        // TODO Auto-generated method stub
        return 0;
    }
}
