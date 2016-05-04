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

import java.util.Date;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import cn.vlabs.clb.api.CLBStatus;
import cn.vlabs.clb.server.ServerBaseTest;
import cn.vlabs.clb.server.model.TrivialItem;
import cn.vlabs.clb.server.ui.frameservice.DPair;

public class ITrivialItemDAOTest extends ServerBaseTest {
    
    private ITrivialItemDAO tio;
    
    @Before
    public void setUp() throws Exception {
        tio = f.getBean(ITrivialItemDAO.class);
    }

    @Test
    public void testCreate() {
        TrivialItem ex = getRandomExample();
        DPair p = getDPairFromTrivialItem(ex);
        tio.delete(p);
        int id = tio.create(ex);
        TrivialItem np = tio.readById(id);
        Assert.assertEquals(ex.getSpaceName(), np.getSpaceName());
        tio.delete(p);
    }
    
    private DPair getDPairFromTrivialItem(TrivialItem ex){
        return  new DPair(ex.getAppid(),ex.getDocid(),ex.getVersion());
    }

    @Test
    public void testRead() {
        TrivialItem ex = getRandomExample();
        DPair p = getDPairFromTrivialItem(ex);
        tio.delete(p);
        tio.create(ex);
        TrivialItem np = tio.read(p);
        Assert.assertEquals(ex.getSpaceName(), np.getSpaceName());
        tio.delete(p);
    }

    @Test
    public void testUpdate() {
        TrivialItem ex = getRandomExample();
        DPair p = getDPairFromTrivialItem(ex);
        tio.delete(p);
        int id = tio.create(ex);
        TrivialItem np = tio.readById(id);
        ex.setId(np.getId());
        ex.setSpaceName(getRandomInt()+"");
        ex.setStatus("READY");
        tio.update(ex);
        np = tio.readById(id);
        Assert.assertEquals(ex.getSpaceName(), np.getSpaceName());
        Assert.assertEquals(ex.getStatus(), np.getStatus());
        //tio.delete(p);
    }

    @Test
    public void testDelete() {
        TrivialItem ex = getRandomExample();
        DPair p = getDPairFromTrivialItem(ex);
        tio.create(ex);
        int count = tio.delete(p);
        Assert.assertTrue(count>0);
    }
    
    private TrivialItem getRandomExample() {
        TrivialItem v = new TrivialItem();
        v.setAppid(getRandomInt());
        v.setDocid(getRandomInt());
        v.setVersion(getRandomInt());
        v.setSpaceName(getRandomInt() + "");
        v.setUpdateTime(new Date());
        v.setStatus(CLBStatus.NOT_READY.toString());
        return v;
    }

}
