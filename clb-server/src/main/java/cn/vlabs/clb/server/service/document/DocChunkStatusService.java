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
package cn.vlabs.clb.server.service.document;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.vlabs.clb.server.dao.document.IDocChunkStatusDAO;
import cn.vlabs.clb.server.model.DocChunkStatus;

@Service
public class DocChunkStatusService implements IDocChunkStatusService {

    @Autowired
    private IDocChunkStatusDAO dcsDao;

    public DocChunkStatus read(int appid, int docid) {
        return this.dcsDao.read(appid, docid);
    }

    public DocChunkStatus create(int appid, int docid, long size, String md5, long chunkSize) {
        return dcsDao.create(appid, docid, size, md5, chunkSize);
    }

    public DocChunkStatus updateChunkStatus(int appid, int docid, int chunkIndex) {
        return dcsDao.updateChunkStatus(appid, docid, chunkIndex);
    }
    
    public DocChunkStatus updateChunkStatus(int appid, int docid, Set<Integer> chunkSet){
        return dcsDao.updateChunkStatus(appid, docid, chunkSet);
    }

    public Set<Integer> updateFinishedStatus(int appid, int docid) {
        DocChunkStatus c = dcsDao.read(appid, docid);
        Set<Integer> set = c.findEmptyChunk();
        if (set.isEmpty()) {
            dcsDao.updateFinishStatus(appid, docid);
        } else {
            System.err.println("There are more chunk need to upload");
        }
        return set;
    }
}
