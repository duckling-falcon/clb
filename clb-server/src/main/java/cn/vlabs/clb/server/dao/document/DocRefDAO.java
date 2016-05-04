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

import org.springframework.stereotype.Repository;

import cn.vlabs.clb.server.dao.BaseDAO;
import cn.vlabs.clb.server.dao.DAOUtils;
import cn.vlabs.clb.server.dao.NamingRule;
import cn.vlabs.clb.server.model.DocRef;

@Repository
public class DocRefDAO extends BaseDAO implements IDocRefDAO {

    public int create(DocRef dr) {
        return super.insertObject(dr);
    }

    @Override
    public <T> DAOUtils bindModelAndTable() {
        return new DAOUtils(DocRef.class, "clb_doc_ref", NamingRule.UNDERSCORE);
    }

    public int updateRef(DocRef dr) {
        return super.updateObject(dr);
    }

    public DocRef read(DocRef dr) {
        return findAndReturnOnly(dr);
    }

    @Override
    public int incrRef(String md5, long size) {
        String sql = "update clb_doc_ref set ref=ref+1 where md5=? and size=?";
        return getJdbcTemplate().update(sql, new Object[] { md5, size });
    }
    
    @Override
    public int incrRef(String storageKey) {
    	String sql = "update clb_doc_ref set ref=ref+1 where storage_key=?";
    	return getJdbcTemplate().update(sql, new Object[] { storageKey });
    }

}
