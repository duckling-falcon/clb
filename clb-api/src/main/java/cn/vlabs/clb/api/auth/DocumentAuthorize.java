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

package cn.vlabs.clb.api.auth;

import java.security.Principal;
/**
 * 文档授权操作
 * @author 谢建军(xiejj@cnic.cn)
 * @created Apr 7, 2009
 */
public interface DocumentAuthorize{
	/**
	 * 查询文档的授权信息
	 * @param docid 文档ID
	 * @return
	 */
	public Permission[] list(int docid);
	/**
	 * 授权
	 * @param docid 文档ID
	 * @param prin 授权的对象
	 * @param op 操作
	 * @see Permission
	 */
	public void grant(int docid, Principal prin, String op);
	/**
	 * 更新授权
	 * @param docid 文档ID
	 * @param permits 文档的权限
	 * @see Permission
	 */
	public void grant(int docid, Permission[] permits);
	/**
	 * 添加一个授权信息
	 * @param docid 文档ID
	 * @param p 授权信息
	 */
	public void grant(int docid, Permission p);
	/**
	 * 取消一个授权
	 * @param docid 文档ID
	 * @param p 权限
	 */
	public void revoke(int docid, Permission p);
	/**
	 * 清空文档的授权
	 * @param docid 文档ID
	 */
	public void revokeAll(int docid);
}
