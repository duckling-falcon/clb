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
package cn.vlabs.clb.server.dao.trivial;

import org.springframework.stereotype.Repository;

import cn.vlabs.clb.server.dao.BaseDAO;
import cn.vlabs.clb.server.dao.DAOUtils;
import cn.vlabs.clb.server.dao.NamingRule;
import cn.vlabs.clb.server.model.TrivialItem;
import cn.vlabs.clb.server.ui.frameservice.DPair;

@Repository
public class TrivialItemDAO extends BaseDAO implements ITrivialItemDAO {

    @Override
    public int create(TrivialItem item) {
        return insertObject(item);
    }

    @Override
    public int update(TrivialItem item) {
        System.out.println("appid="+item.getAppid()+",docid="+item.getDocid()+",version="+item.getVersion());
        return updateObject(item);
    }

    @Override
    public int delete(DPair p) {
        return deleteObject(getQueryItem(p));
    }

    @Override
    public TrivialItem read(DPair p) {
        return findAndReturnOnly(getQueryItem(p));
    }

    private TrivialItem getQueryItem(DPair p) {
        TrivialItem item = new TrivialItem();
        item.setAppid(p.getAppid());
        item.setDocid(p.getDocid());
        item.setVersion(p.getVersion());
        return item;
    }

    @Override
    public <T> DAOUtils bindModelAndTable() {
        return new DAOUtils(TrivialItem.class, "clb_trivial_item", NamingRule.CAMEL);
    }

    @Override
    public TrivialItem readById(int id) {
        TrivialItem item = new TrivialItem();
        item.setId(id);
        return findAndReturnOnly(item);
    }

    @Override
    public TrivialItem readBySpaceName(String spaceName) {
        TrivialItem item = new TrivialItem();
        item.setSpaceName(spaceName);
        return findAndReturnOnly(item);
    }
}
