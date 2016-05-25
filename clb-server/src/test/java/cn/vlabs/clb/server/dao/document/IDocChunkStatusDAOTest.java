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

import java.text.SimpleDateFormat;

import junit.framework.Assert;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import cn.vlabs.clb.server.ServerBaseTest;
import cn.vlabs.clb.server.model.DocChunkStatus;
import cn.vlabs.clb.server.utils.GlobalConst;

public class IDocChunkStatusDAOTest extends ServerBaseTest {

    private IDocChunkStatusDAO cmo;

    @Before
    public void setUp() {
        cmo = f.getBean(IDocChunkStatusDAO.class);
    }

    @Test
    public void testCreate() {
        int appid = getRandomInt();
        int docid = getRandomInt();
        DocChunkStatus a = cmo.create(appid, docid, GlobalConst.DEFLAUT_CHUNK_SIZE * 3,
                "6b84ed6e4adb739d2916b86069ea85e3", GlobalConst.DEFLAUT_CHUNK_SIZE);
        DocChunkStatus b = cmo.read(appid, docid);
        assertDocMetaEquals(a, b);
    }

    @Test
    public void testUpdate() {
        int appid = getRandomInt();
        int docid = getRandomInt();
        DocChunkStatus a = cmo.create(appid, docid, GlobalConst.DEFLAUT_CHUNK_SIZE * 3 + 10,
                "6b84ed6e4adb739d2916b86069ea85e3", GlobalConst.DEFLAUT_CHUNK_SIZE);
        Assert.assertEquals(a.getNumOfChunk(), 4);
        DocChunkStatus c = cmo.updateChunkStatus(appid, docid, 1);
        DocChunkStatus remote = cmo.read(appid, docid);
        Assert.assertEquals(a.getChunkSize(), c.getChunkSize());
        assertDocMetaEquals(c, remote);
    }

    @Test
    public void testFinish() {
        int appid = getRandomInt();
        int docid = getRandomInt();
        DocChunkStatus a = cmo.create(appid, docid, GlobalConst.DEFLAUT_CHUNK_SIZE * 4,
                "6b84ed6e4adb739d2916b86069ea85e3", GlobalConst.DEFLAUT_CHUNK_SIZE);
        Assert.assertEquals(a.getNumOfChunk(), 4);
        for (int i = 0; i < 4; i++) {
            cmo.updateChunkStatus(appid, docid, i + 1);
        }
        cmo.updateFinishStatus(appid, docid);
        DocChunkStatus b = cmo.read(appid, docid);
        Assert.assertEquals(b.getStatus(), "finished");
        Assert.assertEquals(b.getNumOfChunk(), b.getCurrentChunkIndex());
    }

    private void assertDocMetaEquals(DocChunkStatus v1, DocChunkStatus v2) {
        SimpleDateFormat sdf = getDateFormat();
        boolean flag = (v1 != null && v2 != null) && (v1.getAppid() == v2.getAppid())
                && (v1.getDocid() == v2.getDocid()) && (v1.getNumOfChunk() == v2.getNumOfChunk())
                && (v1.getId() == v2.getId()) && (v1.getCurrentChunkIndex() == v2.getCurrentChunkIndex())
                && (StringUtils.equals(v1.getStatus(), v2.getStatus()));
        flag = flag && sdf.format(v1.getLastUpdateTime()).equals(sdf.format(v2.getLastUpdateTime()));
        Assert.assertTrue(flag);
    }

}
