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
package cn.vlabs.clb.server.service.document;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.vlabs.clb.server.dao.document.IDocVersionDAO;
import cn.vlabs.clb.server.model.DocVersion;
import cn.vlabs.clb.server.ui.frameservice.DPair;

@Service
public class DocVersionService implements IDocVersionService {

    @Autowired
    private IDocVersionDAO docVersionDAO;

    @Override
    public DocVersion create(DocVersion v) {
        return docVersionDAO.create(v);
    }
    
    @Override
    public DocVersion createFirstVersion(DocVersion v){
        return docVersionDAO.createFirstVersion(v);
    }

    @Override
    public DocVersion readLastVersion(int appid, int docid) {
        return docVersionDAO.readLastVersion(appid, docid);
    }

    @Override
    public DocVersion read(DPair p) {
        return docVersionDAO.read(p);
    }

    @Override
    public List<DocVersion> readVersions(int appid, int docid) {
        return docVersionDAO.readVersions(appid, docid);
    }

    @Override
    public int update(DocVersion v) {
        return docVersionDAO.update(v);
    }
    
    public int updateStatus(DocVersion v){
        return docVersionDAO.updateStatus(v);
    }

    @Override
    public int delete(DPair p) {
        return docVersionDAO.delete(p);
    }

    @Override
    public int deleteByDocid(int appid, int docid) {
        return docVersionDAO.deleteByDocid(appid, docid);
    }

    @Override
    public List<DocVersion> batchRead(DPair[] pairs) {
        return docVersionDAO.batchRead(pairs);
    }

	@Override
	public DocVersion read(String storageKey) {
		return docVersionDAO.read(storageKey);
	}

}
