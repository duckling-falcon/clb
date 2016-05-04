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

package cn.vlabs.clb.api.search;

import java.util.Collection;
import java.util.Date;

import cn.vlabs.rest.ServiceException;
/**
* @Description: CLB文档的检索接口
* 例子：
* CLBPasswdInfo pwd = new CLBPasswdInfo();
* pwd.setUsername("yourAppUsername");
* pwd.setPassword("yourAppPassword");
* SearchService service=CLBServiceFactory.getSearchService("http://yourServerIP:yourServerPort/clb/ServiceServlet", pwd);
* @author CERC
* @date Aug 9, 2011 11:33:48 AM
*
*/
public interface SearchService {
	
	
	/**
	* @author: CERC
	* @Description: 执行全文检索获得与指定关键字相关的所有文档信息。
	* @param keywords 全文减速的关键字，多个关键字用空格分隔
	* @return Collection<SearchResultVO> 检索结果  
	* @throws ServiceException
	*/
	public Collection<SearchResultVO> search(String keywords) throws ServiceException;


	/**
	* @author: CERC
	* @Description: 获得一个月之内当前用户（应用）上传的所有文档信息
	* @return Collection<SearchResultVO> 检索结果
	* @throws ServiceException 
	*/
	public Collection<SearchResultVO> getMyDocs() throws ServiceException;
	/**
	* @author: CERC
	* @Description: 获得从指定时间到现在这段时间内当前用户（应用）上传的所有文档信息
	* @param begin 开始时间
	* @return Collection<SearchResultVO> 检索结果
	* @throws ServiceException 
	*/
	public Collection<SearchResultVO> getMyDocs(Date begin) throws ServiceException;
	
	/**
	* @author: CERC
	* @Description: 获得一个月之内所有用户（应用）上传的所有文档信息
	* @return Collection<SearchResultVO> 检索结果
	* @throws ServiceException 
	*/
	public Collection<SearchResultVO> getRecentUpdate() throws ServiceException;
	
	/**
	* @author: CERC
	* @Description: 获得从指定时间到现在这段时间内所有用户（应用）上传的所有文档信息
	* @param begin 开始时间
	* @return Collection<SearchResultVO> 检索结果
	* @throws ServiceException 
	*/
	public Collection<SearchResultVO> getRecentUpdate(Date begin) throws ServiceException;
	
}
