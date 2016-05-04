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
package cn.vlabs.clb.server.dao.image;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import cn.vlabs.clb.server.dao.BaseDAO;
import cn.vlabs.clb.server.dao.DAOUtils;
import cn.vlabs.clb.server.dao.NamingRule;
import cn.vlabs.clb.server.model.ImageItem;
import cn.vlabs.clb.server.model.ResizeType;
import cn.vlabs.clb.server.ui.frameservice.DPair;

@Repository
public class ImageItemDAO extends BaseDAO implements IImageItemDAO {

    @Override
    public List<ImageItem> readByDocPair(DPair p) {
        return findByProperties(new ImageItem(p), null);
    }

    @Override
    public int create(ImageItem item) {
        return insertObject(item, null);
    }

    @Override
    public int create(final List<ImageItem> items) {
        String sql = "insert into clb_image_item (docid,version,resizeType,storageKey,size,status,width,height,updateTime,"
                + "filename,fileExtension,appid) values(?,?,?,?,?,?,?,?,?,?,?,?) ";
        this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int index) throws SQLException {
                ImageItem t = items.get(index);
                int i = 0;
                ps.setInt(++i, t.getDocid());
                ps.setInt(++i, t.getVersion());
                ps.setString(++i, t.getResizeType());
                ps.setString(++i, t.getStorageKey());
                ps.setLong(++i, t.getSize());
                ps.setString(++i, t.getStatus());
                ps.setInt(++i, t.getWidth());
                ps.setInt(++i, t.getHeight());
                ps.setTimestamp(++i, new Timestamp(t.getUpdateTime().getTime()));
                ps.setString(++i, t.getFilename());
                ps.setString(++i, t.getFileExtension());
                ps.setInt(++i, t.getAppid());
            }

            @Override
            public int getBatchSize() {
                return items.size();
            }
        });
        return 0;
    }

    @Override
    public int update(ImageItem item) {
        return updateObject(item);
    }

    @Override
    public int delete(DPair p, String resizeType) {
        return deleteObject(new ImageItem(p, resizeType));
    }
    
    @Override
    public int delete(final DPair p, final List<ResizeType> rt) {
        String sql = "delete from clb_image_item where appid=? and docid=? and version=? and resizeType=?";
        this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int index) throws SQLException {
                int i = 0;
                ps.setInt(++i, p.getAppid());
                ps.setInt(++i, p.getDocid());
                ps.setInt(++i, p.getVersion());
                ps.setString(++i, rt.get(index).toString());
            }

            @Override
            public int getBatchSize() {
                return rt.size();
            }
        });
        return 0;
    }

    @Override
    public <T> DAOUtils bindModelAndTable() {
        return new DAOUtils(ImageItem.class, "clb_image_item", NamingRule.CAMEL);
    }

    @Override
    public List<ImageItem> readByStorageKey(String targetKey) {
        ImageItem item = new ImageItem();
        item.setStorageKey(targetKey);
        return findByProperties(item, null);
    }

    @Override
    public ImageItem readByMaxId() {
        return getObjectByMaxID();
    }

    @Override
    public ImageItem readById(int id) {
        ImageItem item = new ImageItem();
        item.setId(id);
        return findAndReturnOnly(item);
    }

    @Override
    public List<ImageItem> readBeforeDate(int appid, Date startTime, Date endTime) {
        String sql = "select * from clb_image_item where appid=? and updateTime > ? and updateTime < ?";
        return getJdbcTemplate().query(sql, new Object[]{appid, startTime, endTime}, getRowMapper(new ImageItem()));
    }

    @Override
    public int delete(DPair p) {
        return deleteObject(new ImageItem(p));
    }

}
