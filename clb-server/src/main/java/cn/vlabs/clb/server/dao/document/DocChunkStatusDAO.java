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

import java.util.BitSet;
import java.util.Date;
import java.util.Set;

import org.springframework.stereotype.Repository;

import cn.vlabs.clb.server.dao.BaseDAO;
import cn.vlabs.clb.server.dao.DAOUtils;
import cn.vlabs.clb.server.dao.NamingRule;
import cn.vlabs.clb.server.model.DocChunkStatus;

@Repository
public class DocChunkStatusDAO extends BaseDAO implements IDocChunkStatusDAO {

    public DocChunkStatus create(int appid, int docid, long fileSize, String md5, long chunkSize) {
        DocChunkStatus nv = new DocChunkStatus();
        nv.setAppid(appid);
        nv.setDocid(docid);
        nv.setChunkSize((int) chunkSize);
        nv.setVersion(1);
        Date now = new Date();
        nv.setLastUpdateTime(now);
        nv.setBeginUploadTime(now);
        int numOfChunk = (int) Math.ceil(fileSize * 1.0 / chunkSize);
        nv.setNumOfChunk(numOfChunk);
        nv.setCurrentChunkIndex(-1);
        nv.setStatus("waiting");
        nv.setChunkMap(null);
        nv.setMd5(md5);
        nv.setFileSize(fileSize);
        int id = insertObject(nv);
        nv.setId(id);
        return nv;
    }
    
    public DocChunkStatus updateChunkStatus(int appid, int docid, Set<Integer> chunkSet){
        DocChunkStatus old = read(appid, docid);
        BitSet bitset = old.loadChunkMap();
        if(chunkSet == null){
            return old;
        }
        for(Integer index:chunkSet){
            bitset.set(index);
        }
        old.updateChunkMap(bitset);
        updateObject(old);
        return old;
    }

    public DocChunkStatus updateChunkStatus(int appid, int docid, int chunkIndex) {
        DocChunkStatus old = read(appid, docid);
        Date now = new Date();
        old.setLastUpdateTime(now);
        old.setCurrentChunkIndex(chunkIndex);
        if (old.getCurrentChunkIndex() == 0) {
            old.setStatus("recieving");
        }
        BitSet bitset = old.loadChunkMap();
        bitset.set(chunkIndex);
        old.updateChunkMap(bitset);
        
        updateObject(old);
        if (chunkIndex == 0) {
            this.getJdbcTemplate().update("update clb_chunk_status set current_chunk_index=? where id=?",
                    new Object[] { chunkIndex, old.getId() });
        }
        return old;
    }

    public void updateFinishStatus(int appid, int docid) {
        DocChunkStatus tmp = new DocChunkStatus();
        tmp.setAppid(appid);
        tmp.setDocid(docid);
        tmp.setVersion(1);
        tmp.setStatus("finished");
        tmp.setLastUpdateTime(new Date());
        super.updateObject(tmp, new String[] { "appid", "docid", "version" });
    }

    public DocChunkStatus read(int appid, int docid) {
        DocChunkStatus m = new DocChunkStatus();
        m.setAppid(appid);
        m.setDocid(docid);
        m.setVersion(1);
        return findAndReturnOnly(m);
    }

    @Override
    public <T> DAOUtils bindModelAndTable() {
        return new DAOUtils(DocChunkStatus.class, "clb_chunk_status", NamingRule.UNDERSCORE);
    }

}
