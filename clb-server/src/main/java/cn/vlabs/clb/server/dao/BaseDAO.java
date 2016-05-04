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
package cn.vlabs.clb.server.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import cn.vlabs.clb.server.utils.CommonUtils;

public abstract class BaseDAO {
    @Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public NamedParameterJdbcTemplate getNamedJdbcTemplate() {
        return namedJdbcTemplate;
    }

    public void setNamedJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
        this.namedJdbcTemplate = jdbcTemplate;
    }

    private DAOUtils currentDAOUtils;

    public abstract <T> DAOUtils bindModelAndTable();

    private DAOUtils getCurrentDaoUtils() {
        if (currentDAOUtils == null) {
            currentDAOUtils = bindModelAndTable();
        }
        return currentDAOUtils;
    }

    /**
     * @description 命名规则正常的情况下，可使用，基本插入,排除"id"字段
     * @param t
     *            需要插入的对象
     * @return 新增的id
     * **/
    public <T> int insertObject(T t) {
        return insertObject(t, "");
    }

    /**
     * @description 命名规则正常的情况下，可使用，基本插入,排除"id"字段
     * @param t
     *            需要插入的对象
     * @param expect
     *            需要忽略的字段，bean中
     * @return 新增的id
     * **/
    public <T> int insertObject(T t, String expect) {
        DAOUtils<T> daoUtils = getCurrentDaoUtils();
        String sql = daoUtils.getInsertSQL("id," + expect);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getNamedJdbcTemplate().update(sql, new MapSqlParameterSource(daoUtils.useModelToFillParamMap(t, true)),
                keyHolder);
        return keyHolder.getKey().intValue();
    }

    /***
     * @description 命名规则正常的情况下，可使用，基本条件查询
     * @param t
     *            实例，其中的所有属性，除了@TempField和初始值以外都会当做条件来使用
     * */
    public <T> List<T> findByProperties(T t) {
        return findByProperties(t, "");
    }

    /***
     * @description 命名规则正常的情况下，可使用，基本条件查询
     * @author lvly
     * @since 2012-08-07
     * @param t
     *            实例，其中的所有属性，除了@TempField和初始值以外都会当做条件来使用
     * @param extendSQL
     *            排序SQL语句
     * */
    public <T> List<T> findByProperties(T t, String extendSQL) {
        DAOUtils<T> daoUtils = getCurrentDaoUtils();
        String sql = daoUtils.getSelectSQL(t) + (CommonUtils.isNull(extendSQL) ? "" : " " + extendSQL.trim());
        Map<String, Object> paramMap = daoUtils.getParamMap(t);
        return getNamedJdbcTemplate().query(sql, paramMap, daoUtils.getRowMapper(null));
    }

    /***
     * @description 命名规则正常的情况下，可使用，只返回一个结果
     * @param t
     * */
    public <T> T findAndReturnOnly(T t) {
        return findAndReturnOnly(t, "");
    }

    /***
     * @description 命名规则正常的情况下，可使用，只返回一个结果
     * @param t
     * */
    public <T> T findAndReturnOnly(T t, String extend) {
        List<T> list = findByProperties(t, extend);
        return !CommonUtils.isNull(list) ? CommonUtils.first(list) : null;
    }

    /***
     * @description 命名规则正常的情况下，可使用，返回布尔值，查询结果集存不存在
     * @param t
     * @return boolean 记录存在吗？
     * */
    public <T> boolean findAndReturnIsExist(T t) {
        return !CommonUtils.isNull(findByProperties(t));
    }

    /***
     * @description 命名规则正常的情况下，可使用，返回布尔值，查询结果集存不存在
     * @param t
     * @return boolean 记录存在吗？
     * */
    public <T> boolean findAndReturnIsExist(T t, String extend) {
        return !CommonUtils.isNull(findByProperties(t, extend));
    }

    /***
     * @description 命名规则正常的情况下，可使用，基本更新查询
     * @param t
     * */
    public <T> int updateObject(T t) {
        DAOUtils<T> daoUtils = getCurrentDaoUtils();
        String sql = daoUtils.getUpdateSQL(t);
        Map<String, Object> paramMap = daoUtils.getParamMap(t);
        return getNamedJdbcTemplate().update(sql, paramMap);
    }
    
    public <T> int updateObject(T t,String[] queryFields) {
        DAOUtils<T> daoUtils = getCurrentDaoUtils();
        String sql = daoUtils.getUpdateByFieldsSQL(t, queryFields);
        Map<String, Object> paramMap = daoUtils.getParamMap(t);
        return getNamedJdbcTemplate().update(sql, paramMap);
    }

    /***
     * @description 命名规则正常的情况下，可使用，基本删除，未经测试，请谨慎使用
     * @param t
     * */
    public <T> int deleteObject(T t) {
        DAOUtils<T> daoUtils = getCurrentDaoUtils();
        String sql = daoUtils.getDeleteSQL(t);
        Map<String, Object> paramMap = daoUtils.getParamMap(t);
        return getNamedJdbcTemplate().update(sql, paramMap);
    }

    public <T> T getObjectByMaxID() {
        DAOUtils<T> daoUtils = getCurrentDaoUtils();
        String sql = daoUtils.getQueryByMaxIdSQL();
        List<T> results = getNamedJdbcTemplate().query(sql, new HashMap<String, Object>(), daoUtils.getRowMapper(null));
        return (results != null && results.size() > 0) ? results.get(0) : null;
    }

    @SuppressWarnings("unused")
    public <T> RowMapper<T> getRowMapper(T t) {
        return getCurrentDaoUtils().getRowMapper(null);
    }

    // TODO 增加文档批量上传

}
