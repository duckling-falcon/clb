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
import java.util.Date;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import cn.vlabs.clb.server.ServerBaseTest;
import cn.vlabs.clb.server.model.DocMeta;
import cn.vlabs.clb.server.service.cache.LocalGeneratorService;

public class IDocMetaDAOTest extends ServerBaseTest {

    private IDocMetaDAO dmo;
    private LocalGeneratorService lgs;

    @Before
    public void setUp() {
        dmo = f.getBean(IDocMetaDAO.class);
        lgs = new LocalGeneratorService();
        lgs.setDocMetaDAO(dmo);
        lgs.init();
    }

    @Test
    public void testCreate() {
        int appid = getRandomInt();
        int nextDocId = lgs.getNextID(appid);
        DocMeta last = getDocMetaBean(appid, nextDocId);
        int id = dmo.create(last);
        last.setId(id);
        DocMeta nm = dmo.read(last.getAppid(), last.getDocid());
        assertDocMetaEquals(last, nm);
    }

    private void assertDocMetaEquals(DocMeta v1, DocMeta v2) {
        SimpleDateFormat sdf = getDateFormat();
        boolean flag = (v1 != null && v2 != null) && (v1.getAppid() == v2.getAppid())
                && (v1.getLastVersion() == v2.getLastVersion()) && (v1.getDocid() == v2.getDocid())
                && (v1.getId() == v2.getId());
        flag = flag && sdf.format(v1.getLastUpdateTime()).equals(sdf.format(v2.getLastUpdateTime()));
        Assert.assertTrue(flag);
    }

    private DocMeta getDocMetaBean(int appid, int docid) {
        DocMeta o = new DocMeta();
        o.setAuthId("clive");
        o.setCreateTime(new Date());
        o.setIsPub(1);
        o.setAppid(appid);
        o.setDocid(docid);
        o.setLastUpdateTime(new Date());
        o.setLastVersion(1);
        return o;
    }

    @Test
    public void testDelete() {
        int appid = getRandomInt();
        int docid = lgs.getNextID(appid);
        DocMeta local = getDocMetaBean(appid, docid);
        dmo.delete(local.getAppid(), local.getDocid());
        dmo.create(local);
        int result = dmo.delete(local.getAppid(), local.getDocid());
        Assert.assertEquals(result, 1);
    }

    @Test
    public void testUpdate() {
        int appid = getRandomInt();
        int docid = lgs.getNextID(appid);
        DocMeta src = getDocMetaBean(appid, docid);
        dmo.delete(appid, docid);
        int id = dmo.create(src);
        src.setId(id);
        DocMeta current = dmo.read(src.getAppid(), src.getDocid());
        assertDocMetaEquals(src, current);
        DocMeta toUpdate = new DocMeta();
        toUpdate.setId(src.getId());
        toUpdate.setLastUpdateTime(new Date());
        toUpdate.setIsPub(2);
        toUpdate.setLastVersion(src.getLastVersion() + 1);
        dmo.update(toUpdate);
        DocMeta third = dmo.read(src.getAppid(), src.getDocid());
        SimpleDateFormat sdf = getDateFormat();
        Assert.assertEquals(third.getLastVersion(), toUpdate.getLastVersion());
        Assert.assertEquals(sdf.format(third.getLastUpdateTime()), sdf.format(toUpdate.getLastUpdateTime()));
    }
    
    @Test
    public void testUpdateByFields(){
        int appid = getRandomInt();
        int docid = lgs.getNextID(appid);
        DocMeta local = getDocMetaBean(appid, docid);
        dmo.delete(appid, docid);
        int id = dmo.create(local);
        local.setId(id);
        DocMeta remote = dmo.read(local.getAppid(), local.getDocid());
        assertDocMetaEquals(local, remote);
        DocMeta toUpdate = new DocMeta();
        toUpdate.setAppid(local.getAppid());
        toUpdate.setDocid(local.getDocid());
        toUpdate.setLastUpdateTime(new Date());
        toUpdate.setIsPub(2);
        toUpdate.setLastVersion(local.getLastVersion() + 1);
        dmo.update(toUpdate,new String[]{"appid","docid"});
        remote = dmo.read(local.getAppid(), local.getDocid());
        SimpleDateFormat sdf = getDateFormat();
        Assert.assertEquals(remote.getLastVersion(), toUpdate.getLastVersion());
        Assert.assertEquals(sdf.format(remote.getLastUpdateTime()), sdf.format(toUpdate.getLastUpdateTime()));
    }

    @Test
    public void testRead() {
        int appid = getRandomInt();
        int docid = lgs.getNextID(appid);
        DocMeta local = getDocMetaBean(appid, docid);
        local.setId(dmo.create(local));
        DocMeta remote = dmo.read(local.getAppid(), local.getDocid());
        assertDocMetaEquals(local, remote);
    }
    
    @Test
    public void testZero(){
        DocMeta remote =  dmo.read(1, 0);
        Assert.assertNull(remote);
    }

}
