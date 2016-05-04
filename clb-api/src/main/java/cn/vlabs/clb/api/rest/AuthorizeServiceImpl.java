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

import java.security.Principal;

import cn.vlabs.clb.api.CLBConnection;
import cn.vlabs.clb.api.auth.DocumentAuthorize;
import cn.vlabs.clb.api.auth.FolderAuthorize;
import cn.vlabs.clb.api.auth.Permission;
@Deprecated
public class AuthorizeServiceImpl implements FolderAuthorize, DocumentAuthorize {
	private CLBConnection conn;
	public AuthorizeServiceImpl(CLBConnection conn) {
		this.conn=conn;
	}
	
	public void grant(int docid, Principal prin, String op) {
		GrantMessage gm = new GrantMessage();
		gm.p = new Permission(prin, new String[] { op });
		gm.res = new ResourceInfo(docid);
		conn.sendService(GRANT, gm);
	}

	public void grant(String path, Principal prin, String op) {
		GrantMessage gm = new GrantMessage();
		gm.p = new Permission(prin, new String[] { op });
		gm.res = new ResourceInfo(path);
		conn.sendService(GRANT, gm);
	}

	public void grant(int docid, Permission[] permits) {
		BatchGrantMessage gm = new BatchGrantMessage();
		gm.p = permits.clone();
		gm.res = new ResourceInfo(docid);
		conn.sendService(UPDATE, gm);
	}

	public void grant(String path, Permission[] permits) {
		BatchGrantMessage gm = new BatchGrantMessage();
		gm.p = permits.clone();
		gm.res = new ResourceInfo(path);
		conn.sendService(UPDATE, gm);
	}

	public void grant(int docid, Permission p) {
		GrantMessage gm = new GrantMessage();
		gm.p = p;
		gm.res = new ResourceInfo(docid);
		conn.sendService(GRANT, gm);
	}

	public void grant(String path, Permission p) {
		GrantMessage gm = new GrantMessage();
		gm.p = p;
		gm.res = new ResourceInfo(path);
		conn.sendService(GRANT, gm);
	}

	public Permission[] list(int docid) {
		ResourceInfo res = new ResourceInfo(docid);
		return (Permission[]) conn.sendService(LIST, res);
	}

	public Permission[] list(String path) {
		ResourceInfo res = new ResourceInfo(path);
		return (Permission[]) conn.sendService(LIST, res);
	}

	public void revokeAll(int docid) {
		BatchGrantMessage gm = new BatchGrantMessage();
		gm.res=new ResourceInfo(docid);
		gm.p=new Permission[0];
		conn.sendService(UPDATE, gm);
	}

	public void revokeAll(String path) {
		BatchGrantMessage gm = new BatchGrantMessage();
		gm.res=new ResourceInfo(path);
		gm.p=new Permission[0];
		conn.sendService(UPDATE, gm);
	}

	public void revoke(String path, Permission p) {
		ResourceInfo res = new ResourceInfo(path);
		GrantMessage gm = new GrantMessage();
		gm.p = p;
		gm.res = res;
		conn.sendService(REVOKE, gm);
	}

	public void revoke(int docid, Permission p) {
		ResourceInfo res = new ResourceInfo(docid);
		GrantMessage gm = new GrantMessage();
		gm.p = p;
		gm.res = res;
		conn.sendService(REVOKE, gm);
	}

	private static final String GRANT = "auth.grant";

	private static final String UPDATE = "auth.update";

	private static final String REVOKE = "auth.revoke";

	private static final String LIST = "auth.list";
}
