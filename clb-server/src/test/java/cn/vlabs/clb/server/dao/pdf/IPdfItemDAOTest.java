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
package cn.vlabs.clb.server.dao.pdf;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import cn.vlabs.clb.api.CLBStatus;
import cn.vlabs.clb.server.ServerBaseTest;
import cn.vlabs.clb.server.model.PdfItem;
import cn.vlabs.clb.server.ui.frameservice.DPair;

public class IPdfItemDAOTest extends ServerBaseTest {
    private IPdfItemDAO pio;

    @Before
    public void setUp() throws Exception {
        pio = f.getBean(IPdfItemDAO.class);
    }

    @Test
    public void testCreate() {
        PdfItem local = getRandomExample();
        int id = pio.create(local);
        PdfItem remote = pio.readById(id);
        Assert.assertEquals(local.getFilename(), remote.getFilename());
    }

    @Test
    public void testRead() {
        PdfItem local = getRandomExample();
        pio.delete(getDPair(local));
        pio.create(local);
        PdfItem remote = pio.read(getDPair(local));
        Assert.assertEquals(remote.getStorageKey(), local.getStorageKey());
        pio.delete(getDPair(local));
    }

    @Test
    public void testReadByStorageKey() {
        PdfItem local = getRandomExample();
        int id = pio.create(local);
        PdfItem remote = pio.readById(id);
        pio.readByStorageKey(local.getStorageKey());
        Assert.assertEquals(local.getSize(), remote.getSize());
        pio.delete(getDPair(local));
    }
    
    private DPair getDPair(PdfItem item){
        return new DPair(item.getAppid(),item.getDocid(),item.getVersion());
    }

    @Test
    public void testUpdate() {
        PdfItem local = getRandomExample();
        int id = pio.create(local);
        PdfItem remote = pio.readById(id);
        remote.setStorageKey("anyword");
        remote.setUpdateTime(new Date());
        int count = pio.update(remote);
        Assert.assertEquals(count, 1);
        PdfItem ndp = pio.readById(id);
        Assert.assertEquals(remote.getStorageKey(), ndp.getStorageKey());
        assertDateEquals(remote.getUpdateTime(), ndp.getUpdateTime());
        pio.delete(getDPair(local));
    }

    @Test
    public void testDelete() {
        PdfItem local = getRandomExample();
        int id = pio.create(local);
        PdfItem remote = pio.readById(id);
        int count = pio.delete(getDPair(remote));
        Assert.assertTrue(count > 0);
    }

    private PdfItem getRandomExample() {
        PdfItem v = new PdfItem();
        v.setAppid(getRandomInt());
        v.setDocid(getRandomInt());
        v.setVersion(getRandomInt());
        v.setFilename(getRandomInt() + ".jpg");
        v.setStorageKey(getRandomInt() + "");
        v.setSize(getRandomInt());
        v.setUpdateTime(new Date());
        v.setStatus(CLBStatus.NOT_READY.toString());
        return v;
    }

}
