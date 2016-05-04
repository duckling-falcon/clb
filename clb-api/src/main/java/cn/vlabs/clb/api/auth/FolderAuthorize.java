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
 * 目录授权对象
 * @author 谢建军(xiejj@cnic.cn)
 * @created Apr 7, 2009
 */
public interface FolderAuthorize{
	/**
	 * 查询目录项的授权信息
	 * @param path
	 * @return
	 */
	public Permission[] list(String path);
	/**
	 * 添加授权信息
	 * @param path 目录项路径
	 * @param prin 授权身份
	 * @param op 授权操作
	 * @see PrincipalFactory
	 * @see Permission
	 */
	public void grant(String path, Principal prin, String op);
	/**
	 * 更新授权信息
	 * @param path 目录项路径
	 * @param permits 授权信息
	 * @see PrincipalFactory
	 * @see Permission
	 */
	public void grant(String path, Permission[] permits);
	/**
	 * 添加授权信息
	 * @param path 目录项路径
	 * @param p 授权信息
	 * @see PrincipalFactory
	 * @see Permission
	 */
	public void grant(String path, Permission p);
	/**
	 * 收回授权信息
	 * @param path 目录项路径
	 * @param p 授权信息
	 */
	public void revoke(String path, Permission p);
	/**
	 * 收回所有授权信息
	 * @param path 目录项路径
	 */
	public void revokeAll(String path);
}
