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
package cn.vlabs.clb.server.service.cache;

import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.vlabs.clb.server.dao.document.IDocMetaDAO;

@Service
public class LocalGeneratorService implements IGeneratorService {
    /*
     * 注意:这只是一个暂时简易版,不考虑分布情况下的状态问题,也就是说分布时这段程序是错的.
     * 请视具体情况来使用,正式的GeneratorService会在Faclon的0.3版中进行支持
     */

    private static final int FIRST = 1;
    @Autowired
    private IDocMetaDAO docMetaDAO;

    public void setDocMetaDAO(IDocMetaDAO docMetaDAO) {
        this.docMetaDAO = docMetaDAO;
    }

    private Map<Integer, Integer> localMap;

    @Override
    public void init() {
        localMap = docMetaDAO.getMaxDocId();
    }

    @Override
    public synchronized int getNextID(int appid) {
        Integer docid = localMap.get(appid);
        if (docid != null) {
            docid++;
            localMap.put(appid, docid);
            return docid;
        } else {
            localMap.put(appid, FIRST);
            return FIRST;
        }
    }
    
    public ObjectId getStorageKey(){
        return new ObjectId();
    }
    

}
