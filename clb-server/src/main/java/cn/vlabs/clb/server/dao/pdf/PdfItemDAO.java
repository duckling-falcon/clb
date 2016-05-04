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
package cn.vlabs.clb.server.dao.pdf;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.vlabs.clb.api.CLBStatus;
import cn.vlabs.clb.server.dao.BaseDAO;
import cn.vlabs.clb.server.dao.DAOUtils;
import cn.vlabs.clb.server.dao.NamingRule;
import cn.vlabs.clb.server.model.PdfItem;
import cn.vlabs.clb.server.ui.frameservice.DPair;

@Repository
public class PdfItemDAO extends BaseDAO implements IPdfItemDAO {

    @Override
    public int create(PdfItem item) {
        return insertObject(item, null);
    }

    @Override
    public int update(PdfItem item) {
        return updateObject(item);
    }

    @Override
    public PdfItem read(DPair p) {
        return findAndReturnOnly(new PdfItem(p));
    }

    @Override
    public int delete(DPair p) {
        return deleteObject(new PdfItem(p));
    }

    @Override
    public <T> DAOUtils bindModelAndTable() {
        return new DAOUtils(PdfItem.class, "clb_pdf_item", NamingRule.CAMEL);
    }

    @Override
    public PdfItem readById(int id) {
        PdfItem i = new PdfItem();
        i.setId(id);
        return findAndReturnOnly(i,null);
    }

    @Override
    public PdfItem readByMaxId() {
        return getObjectByMaxID();
    }

    @Override
    public PdfItem readByStorageKey(String storageKey) {
        PdfItem i = new PdfItem();
        i.setStorageKey(storageKey);
        return findAndReturnOnly(i,null);
    }

	@Override
	public List<PdfItem> readUnfinishedItems() {
		PdfItem i = new PdfItem();
		i.setStatus(CLBStatus.NOT_READY.toString());
		return super.findByProperties(i);
	}

}
