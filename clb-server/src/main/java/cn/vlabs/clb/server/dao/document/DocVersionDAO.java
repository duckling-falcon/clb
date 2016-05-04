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

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.stereotype.Repository;

import cn.vlabs.clb.server.dao.BaseDAO;
import cn.vlabs.clb.server.dao.DAOUtils;
import cn.vlabs.clb.server.dao.NamingRule;
import cn.vlabs.clb.server.model.DocVersion;
import cn.vlabs.clb.server.ui.frameservice.DPair;

@Repository
public class DocVersionDAO extends BaseDAO implements IDocVersionDAO {

    @Override
    public DocVersion create(DocVersion v) {
        DocVersion nv = getNewDocVersionId(v.getAppid(), v.getDocid());
        nv.setCreateTime(new Date());
        nv.setFileExtension(v.getFileExtension());
        nv.setSize(v.getSize());
        nv.setFilename(v.getFilename());
        nv.setStorageKey(v.getStorageKey());
        nv.setUploadStatus(v.getUploadStatus());
        updateObject(nv);
        return nv;
    }

    private DocVersion getFirstVersion(DocVersion v) {
        DocVersion nv = new DocVersion();
        nv.setAppid(v.getAppid());
        nv.setVersion(1);
        nv.setDocid(v.getDocid());
        Date now = new Date();
        nv.setCreateTime(now);
        nv.setCompleteTime(now);
        nv.setFileExtension(v.getFileExtension());
        nv.setSize(v.getSize());
        nv.setFilename(v.getFilename());
        nv.setStorageKey(v.getStorageKey());
        nv.setUploadStatus(v.getUploadStatus());
        return nv;
    }

    @Override
    public DocVersion createFirstVersion(DocVersion v) {
        DocVersion nv = getFirstVersion(v);
        int vid = super.insertObject(nv);
        nv.setId(vid);
        return nv;
    }

    @Override
    public int deleteByDocid(int appid, int docid) {
        return deleteObject(new DocVersion(appid, docid));
    }

    @Override
    public DocVersion read(DPair p) {
        if (p.getVersion() == DPair.LATEST) {
            return findAndReturnOnly(new DocVersion(p.getAppid(), p.getDocid()), "order by version desc");
        }
        return findAndReturnOnly(new DocVersion(p));
    }

    @Override
    public DocVersion readLastVersion(int appid, int docid) {
        return findAndReturnOnly(new DocVersion(appid, docid), "order by version desc");
    }

    @Override
    public List<DocVersion> readVersions(int appid, int docid) {
        return findByProperties(new DocVersion(appid, docid), "order by version desc");
    }

    @Override
    public <T> DAOUtils bindModelAndTable() {
        return new DAOUtils(DocVersion.class, "clb_doc_version", NamingRule.CAMEL);
    }

    @Override
    public DocVersion readByMaxId() {
        return getObjectByMaxID();
    }

    private DocVersion getNewDocVersionId(final int appid, final int docid) {
        String sql = "{call proc_create_doc_version(?,?,?,?)}";
        DocVersion dv = getJdbcTemplate().execute(sql, new CallableStatementCallback<DocVersion>() {
            @Override
            public DocVersion doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                cs.setInt(1, appid);
                cs.setInt(2, docid);
                cs.registerOutParameter(3, Types.INTEGER);
                cs.registerOutParameter(4, Types.INTEGER);
                cs.execute();
                DocVersion dv = new DocVersion();
                dv.setAppid(appid);
                dv.setDocid(docid);
                dv.setId(cs.getInt(3));
                dv.setVersion(cs.getInt(4));
                return dv;
            }
        });
        return dv;
    }

    @Override
    public int update(DocVersion v) {
        return updateObject(v);
    }

    @Override
    public int delete(DPair p) {
        return deleteObject(new DocVersion(p));
    }

    @Override
    public List<DocVersion> batchRead(DPair[] pairs) {
        StringBuilder sb = new StringBuilder();
        sb.append("select * from clb_doc_version where (appid,docid,version) in (");
        for (DPair p : pairs) {
            sb.append("(");
            sb.append(p.getAppid());
            sb.append(",");
            sb.append(p.getDocid());
            sb.append(",");
            sb.append(p.getVersion());
            sb.append("),");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        System.out.println(sb);
        return getJdbcTemplate().query(sb.toString(), getRowMapper(new DocVersion()));
    }

    @Override
    public int updateStatus(DocVersion v) {
        return updateObject(v, new String[] { "docid", "appid", "version" });
    }

	@Override
	public DocVersion read(String storageKey) {
		DocVersion docVersion = new DocVersion();
		docVersion.setStorageKey(storageKey);
		return findAndReturnOnly(docVersion, "order by version desc");
	}

}
