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
package cn.vlabs.clb.server.dao.auth;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import cn.vlabs.clb.server.dao.BaseDAO;
import cn.vlabs.clb.server.dao.DAOUtils;
import cn.vlabs.clb.server.dao.NamingRule;
import cn.vlabs.clb.server.model.AppAuthInfo;

@Repository
@Qualifier(value = "AppAuthDAOImpl")
@SuppressWarnings("unchecked")
public class AppAuthDAO extends BaseDAO implements IAppAuthDAO {
    private static final String TABLE = " clb_app_auth ";
    private static final String FIELDS = " name,password ";
    private static final String SAVED_VALUES = " :name,:password ";
    private static final String UPDATED_VALUES = " name=:name,password=:password ";

    public int save(AppAuthInfo appAuthInfo) {
        String sql = "insert into " + TABLE + " ( " + FIELDS + " ) values ( " + SAVED_VALUES + " )";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getNamedJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(appAuthInfo), keyHolder);
        return keyHolder.getKey().intValue();
    }

    public void update(AppAuthInfo appAuthInfo) {
        String sql = "update " + TABLE + " set " + UPDATED_VALUES + " where id=:id";
        SqlParameterSource ps = new BeanPropertySqlParameterSource(appAuthInfo);
        getNamedJdbcTemplate().update(sql, ps);
    }

    public void delete(String name) {
        String sql = "delete from " + TABLE + " where name=:name";
        Map params = new HashMap();
        params.put("name", name);
        getNamedJdbcTemplate().update(sql, params);
    }

    public List<AppAuthInfo> findAll() {
        String sql = "select * from " + TABLE;
        return getNamedJdbcTemplate().query(sql, new HashMap(), rowMapper);
    }

    public List<AppAuthInfo> findByProperty(String propertyName, Object value) {
        String sql = "select * from " + TABLE + " where " + propertyName + "=:" + propertyName;
        Map paramMap = new HashMap();
        paramMap.put(propertyName, value);
        return getNamedJdbcTemplate().query(sql, paramMap, rowMapper);
    }

    public List<AppAuthInfo> find(String name, String password) {
        String sql = "select * from " + TABLE + " where name=:name and password=:password";
        Map paramMap = new HashMap();
        paramMap.put("name", name);
        paramMap.put("password", password);
        return getNamedJdbcTemplate().query(sql, paramMap, rowMapper);
    }

    public AppAuthInfo getAppAuthInfo(String name) {
        List<AppAuthInfo> infos = findByProperty("name", name);
        AppAuthInfo authInfo = null;
        if (!infos.isEmpty()) {
            authInfo = infos.get(0);
        }
        return authInfo;
    }

    private RowMapper rowMapper = new RowMapper() {
        public Object mapRow(ResultSet rs, int arg1) throws SQLException {
            AppAuthInfo authInfo = new AppAuthInfo();
            authInfo.setId(rs.getInt("id"));
            authInfo.setName(rs.getString("name"));
            authInfo.setPassword(rs.getString("password"));
            return authInfo;
        }
    };
    
    @Override
    public <T> DAOUtils bindModelAndTable() {
        return new DAOUtils(AppAuthInfo.class,"clb_app_auth",NamingRule.CAMEL);
    }

}
