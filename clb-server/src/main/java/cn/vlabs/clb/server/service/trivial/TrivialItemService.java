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
package cn.vlabs.clb.server.service.trivial;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.vlabs.clb.server.dao.trivial.ITrivialItemDAO;
import cn.vlabs.clb.server.model.TrivialItem;
import cn.vlabs.clb.server.ui.frameservice.DPair;

@Service
public class TrivialItemService implements ITrivialItemService {

    @Autowired
    private ITrivialItemDAO trivialItemDAO;
    
    @Override
    public int create(TrivialItem item) {
        return trivialItemDAO.create(item);
    }

    @Override
    public TrivialItem read(DPair p) {
        return trivialItemDAO.read(p);
    }

    @Override
    public TrivialItem readById(int id) {
        return trivialItemDAO.readById(id);
    }

    @Override
    public int update(TrivialItem item) {
        return trivialItemDAO.update(item);
    }

    @Override
    public int delete(DPair p){
        return trivialItemDAO.delete(p);
    }

    @Override
    public TrivialItem readBySpaceName(String spaceName) {
        return trivialItemDAO.readBySpaceName(spaceName);
    }

}
