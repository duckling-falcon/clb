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

package cn.vlabs.clb.server.ui.frameservice.document;

import java.util.List;

import cn.vlabs.clb.api.document.VersionInfo;
import cn.vlabs.clb.server.model.DocVersion;

public class VersionInfoAdapter {
	public static VersionInfo toVersionInfo(DocVersion v) {
		VersionInfo vinfo = new VersionInfo();
		vinfo.version=String.valueOf(v.getVersion());
		vinfo.filename = v.getFilename();
		vinfo.filesize = v.getSize();
		vinfo.updateDate = v.getCreateTime();
		return vinfo;
	}

	public static VersionInfo[] toVersionInfo(List<DocVersion> vers) {
		if (vers == null || vers.isEmpty()) {
			return new VersionInfo[0];
		}
		VersionInfo[] infos = new VersionInfo[vers.size()];
		int i = 0;
		for (DocVersion ver : vers) {
			infos[i] = toVersionInfo(ver);
			i++;
		}
		return infos;
	}
}
