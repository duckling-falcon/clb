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

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import cn.vlabs.clb.server.ServerBaseTest;
import cn.vlabs.clb.server.model.DocRef;

public class IDocRefDAOTest extends ServerBaseTest {

    private IDocRefDAO dro;

    @Before
    public void setUp() {
        dro = f.getBean(IDocRefDAO.class);
    }

    String md5 = "6b84ed6e4adb739d2916b86069ea85e3";
    long size = 10241024;
    String storageKey = "53d1f0f0da069b963d854a36";

    private DocRef getInstance() {
        DocRef r = new DocRef();
        r.setMd5(md5);
        r.setRef(1);
        r.setSize(1024);
        r.setStorageKey(storageKey);
        return r;
    }

    @Test
    public void testCreate() {
        DocRef r = getInstance();
        int id = dro.create(r);
        DocRef tmp = new DocRef();
        tmp.setId(id);
        DocRef rr = dro.read(tmp);
        Assert.assertEquals(rr.getMd5(), r.getMd5());
    }

    @Test
    public void testUpdate() {
        DocRef r = getInstance();
        int id = dro.create(r);
        DocRef tmp = new DocRef();
        tmp.setId(id);
        tmp.setRef(r.getRef() + 1);
        dro.updateRef(tmp);
        DocRef rr = dro.read(tmp);
        Assert.assertEquals(rr.getRef(), tmp.getRef());
        dro.incrRef(r.getMd5(), r.getSize());
        tmp.setRef(tmp.getRef()+1);
        DocRef rrr = dro.read(tmp);
        Assert.assertEquals(rrr.getRef(), tmp.getRef());
    }
    
}
