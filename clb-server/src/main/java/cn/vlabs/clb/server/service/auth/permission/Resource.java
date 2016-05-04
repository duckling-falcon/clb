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

package cn.vlabs.clb.server.service.auth.permission;

public abstract class Resource {
	/**
	 * 获得资源的标识,该标识在同资源类型内部唯一。
	 * @return
	 */
	public abstract String getResourceId();
	/**
	 * 获得文档的创建者。
	 * @return
	 */
	public abstract Entity getOwner();
	/**
	 * 获得资源的类型。
	 * @return
	 */
	public abstract String getType();
	protected static final String TYPE_DOC="DOCUMENT";
	protected static final String TYPE_FOLDER="FOLDER";
}
