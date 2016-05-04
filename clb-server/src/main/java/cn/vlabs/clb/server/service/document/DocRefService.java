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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.vlabs.clb.server.dao.document.IDocRefDAO;
import cn.vlabs.clb.server.model.DocRef;

@Service
public class DocRefService implements IDocRefService {

    @Autowired
    private IDocRefDAO dro;

    @Override
    public int create(DocRef dr) {
        return dro.create(dr);
    }

    @Override
    public int updateRef(DocRef dr) {
        return dro.updateRef(dr);
    }

    @Override
    public DocRef read(String md5, long size) {
        DocRef dr = new DocRef();
        dr.setMd5(md5);
        dr.setSize(size);
        return dro.read(dr);
    }

    @Override
    public int incrRef(String md5, long size) {
        return dro.incrRef(md5, size);
    }
    
    @Override
    public int incrRef(String storageKey) {
    	return dro.incrRef(storageKey);
    }
    
    

}
