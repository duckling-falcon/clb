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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import cn.vlabs.clb.server.dao.BaseDAO;
import cn.vlabs.clb.server.dao.DAOUtils;
import cn.vlabs.clb.server.dao.NamingRule;
import cn.vlabs.clb.server.model.DocMeta;

@Repository
public class DocMetaDAO extends BaseDAO implements IDocMetaDAO {
    @Override
    public int create(DocMeta doc) {
        return insertObject(doc, null);
    }

    @Override
    public synchronized void update(DocMeta doc) {
        updateObject(doc);
    }
    
    @Override
    public synchronized void update(DocMeta doc, String[] queryFields) {
        updateObject(doc,queryFields);
    }

    @Override
    public int delete(int appid,int docid) {
        return deleteObject(new DocMeta(appid,docid));
    }

    @Override
    public DocMeta read(int appid,int docid) {
        DocMeta m = new DocMeta();
        m.setAppid(appid);
        m.setDocid(docid);
        return findAndReturnOnly(m);
    }

    @Override
    public <T> DAOUtils bindModelAndTable() {
        return new DAOUtils(DocMeta.class, "clb_doc", NamingRule.CAMEL);
    }

    @Override
    public DocMeta readByMaxId() {
        return getObjectByMaxID();
    }

    @Override
    public List<DocMeta> batchRead(int appid,int[] docids) {
        StringBuilder sb = new StringBuilder();
        sb.append("select * from clb_doc where appid="+appid+" and (docid) in (");
        for (int id : docids) {
            sb.append(id);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        System.out.println(sb);
        return getJdbcTemplate().query(sb.toString(), getRowMapper(new DocMeta()));
    }

    public int getMaxDocId(int appid) {
        String sql = "select max(id) from clb_doc where appid=?";
        return getJdbcTemplate().queryForInt(sql, new Object[] { appid });
    }

    @Override
    public Map<Integer, Integer> getMaxDocId() {
        String sql = "select max(docid) as docid,appid from clb_doc group by appid";
        List<Pair> plist = getJdbcTemplate().query(sql, new Object[] {}, pairRowMapper);
        Map<Integer, Integer> result = new HashMap<Integer, Integer>();
        for (Pair p : plist) {
            result.put(p.appid, p.docid);
        }
        return result;
    }

    private static class Pair {
        public int docid;
        public int appid;
    }

    private static RowMapper<Pair> pairRowMapper = new RowMapper<Pair>() {
        @Override
        public Pair mapRow(ResultSet rs, int rowNum) throws SQLException {
            Pair item = new Pair();
            item.docid = rs.getInt("docid");
            item.appid = rs.getInt("appid");
            return item;
        }
    };



}
