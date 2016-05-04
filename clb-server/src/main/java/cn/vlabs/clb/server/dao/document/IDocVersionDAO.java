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
package cn.vlabs.clb.server.dao.document;

import java.util.List;

import cn.vlabs.clb.server.model.DocVersion;
import cn.vlabs.clb.server.ui.frameservice.DPair;

public interface IDocVersionDAO {

    DocVersion create(DocVersion v);
    
    DocVersion createFirstVersion(DocVersion v);

    List<DocVersion> readVersions(int appid,int docid);

    DocVersion readLastVersion(int appid,int docid);

    DocVersion read(DPair p);
    DocVersion read(String storageKey);
    DocVersion readByMaxId();

    int update(DocVersion v);

    int deleteByDocid(int appid,int docid);

    int delete(DPair p);
    
    List<DocVersion> batchRead(DPair[] pairs);

    int updateStatus(DocVersion v);

}
