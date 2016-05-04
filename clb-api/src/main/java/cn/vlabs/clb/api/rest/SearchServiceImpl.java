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

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import cn.vlabs.clb.api.CLBConnection;
import cn.vlabs.clb.api.search.SearchResultVO;
import cn.vlabs.clb.api.search.SearchService;
import cn.vlabs.rest.ServiceException;

public class SearchServiceImpl implements SearchService {
	private CLBConnection conn;

	public SearchServiceImpl(CLBConnection conn) {
		this.conn = conn;
	}

	public Collection<SearchResultVO> getMyDocs() throws ServiceException {
		SearchArg arg = new SearchArg();
		arg.action = SearchArg.MYRECENT;
		arg.param = aMonthAgo();
		return execute(arg);
	}

	public Collection<SearchResultVO> getMyDocs(Date begin)
			throws ServiceException {
		SearchArg arg = new SearchArg();
		arg.action = SearchArg.MYRECENT;
		if (begin == null) {
			arg.param = aMonthAgo();
		} else {
			arg.param = begin;
		}
		return execute(arg);
	}

	public Collection<SearchResultVO> getRecentUpdate() throws ServiceException {
		SearchArg arg = new SearchArg();
		arg.action = SearchArg.RECENT_UPDATE;
		arg.param = aMonthAgo();
		return execute(arg);
	}
	
	public Collection<SearchResultVO> getRecentUpdate(Date begin)
			throws ServiceException {
		SearchArg arg = new SearchArg();
		arg.action = SearchArg.RECENT_UPDATE;
		if (begin == null) {
			arg.param = aMonthAgo();
		} else {
			arg.param = begin;
		}
		return execute(arg);
	}

	public Collection<SearchResultVO> search(String searchString)
			throws ServiceException {
		SearchArg arg = new SearchArg();
		arg.action = SearchArg.CONTENT;
		arg.param = searchString;
		return execute(arg);
	}

	@SuppressWarnings("unchecked")
	private Collection<SearchResultVO> execute(SearchArg arg){
		return (Collection<SearchResultVO>) conn.sendService("search", arg);
	}

	private Date aMonthAgo() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		return cal.getTime();
	}
}
