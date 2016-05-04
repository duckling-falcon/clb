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

import cn.vlabs.clb.server.dao.document.IDocMetaDAO;
import cn.vlabs.clb.server.model.DocMeta;

@Service
public class DocMetaService implements IDocMetaService {
    @Autowired
    private IDocMetaDAO docMetaDAO;

    @Override
    public int create(DocMeta doc) {
        return docMetaDAO.create(doc);
    }

    @Override
    public DocMeta read(int appid, int docid) {
        return docMetaDAO.read(appid, docid);
    }

    @Override
    public void update(DocMeta doc) {
        docMetaDAO.update(doc);
    }

    @Override
    public void delete(int appid, int docid) {
        docMetaDAO.delete(appid, docid);
    }

    @Override
    public List<DocMeta> batchRead(int appid, int[] docids) {
        return docMetaDAO.batchRead(appid, docids);
    }

    @Override
    public void update(DocMeta doc, String[] queryFields) {
        docMetaDAO.update(doc, queryFields);
    }

}
