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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import cn.vlabs.clb.server.ServerBaseTest;
import cn.vlabs.clb.server.model.DocVersion;
import cn.vlabs.clb.server.ui.frameservice.DPair;

public class IDocVersionDAOTest extends ServerBaseTest {

    private IDocVersionDAO dvo;

    @Before
    public void setUp() throws Exception {
        dvo = f.getBean(IDocVersionDAO.class);
    }

    private DocVersion getRandomExample(int appid, int docid) {
        DocVersion v = new DocVersion();
        v.setDocid(docid);
        v.setAppid(appid);
        v.setVersion(getRandomInt());
        v.setFileExtension("jpg");
        v.setFilename("中文"+getRandomInt() + ".jpg");
        v.setStorageKey(getRandomInt() + "");
        v.setSize(getRandomInt());
        v.setCreateTime(new Date());
        return v;
    }

    private DocVersion getDocVersionExample() {
        DocVersion v = new DocVersion();
        v.setAppid(1);
        v.setDocid(1);
        v.setVersion(1);
        v.setFileExtension("jpg");
        v.setFilename("中文中文hello.jpg");
        v.setStorageKey("abc");
        v.setSize(65536);
        v.setCreateTime(new Date());
        return v;
    }

//    @Test
    public void testReadVersions() {
        int docid = getRandomInt();
        int appid = getRandomInt();
        dvo.deleteByDocid(appid, docid);
        int count = 10;
        for (int i = 0; i < count; i++) {
            dvo.create(getRandomExample(appid, docid));
        }
        List<DocVersion> dlist = dvo.readVersions(appid, docid);
        for (DocVersion v : dlist) {
            Assert.assertEquals(docid, v.getDocid());
        }
        Assert.assertEquals(count, dlist.size());
    }

//    @Test
    public void testReadLastVersion() {
        DocVersion ex = getDocVersionExample();
        ex.getDocid();
        DocVersion a = dvo.readLastVersion(ex.getAppid(), ex.getDocid());
        int version = 0;
        if (a != null) {
            version = a.getVersion();
        } else {
            version = 0;
        }
        dvo.create(ex);
        version++;
        dvo.create(ex);
        version++;
        dvo.create(ex);
        version++;
        DocVersion dv = dvo.readLastVersion(ex.getAppid(), ex.getDocid());
        Assert.assertEquals(version, dv.getVersion());
    }

//    @Test
    public void testRead() {
        DocVersion ex = getDocVersionExample();
        DocVersion dv1 = dvo.create(ex);
        DocVersion dv2 = dvo.readLastVersion(ex.getAppid(), ex.getDocid());
        assertDocVersionEquals(dv1, dv2);
    }

    @Test
    public void testCreate() {
        DocVersion dv1 = dvo.create(getDocVersionExample());
        DocVersion dv2 = dvo.read(new DPair(dv1.getAppid(), dv1.getDocid(), dv1.getVersion()));
        assertDocVersionEquals(dv1, dv2);
    }

    public void testDelete() {
        DocVersion last = dvo.readByMaxId();
        if (last != null) {
            int ar = dvo.deleteByDocid(last.getAppid(), last.getDocid());
            Assert.assertTrue(ar > 0);
        } else {
            int ar = dvo.deleteByDocid(last.getAppid(), 1);
            Assert.assertTrue(ar == 0);
        }
    }

//    @Test
    public void testBatchRead() throws InterruptedException {
        int count = 10;
        int maxVersion = 8;
        DPair[] pairs = new DPair[count];
        int[] versionSizeArray = new int[count];
        for (int i = 0; i < count; i++) {
            pairs[i] = new DPair(getRandomInt(), getRandomInt());
        }
        for (int i = 0; i < count; i++) {
            dvo.delete(pairs[i]); // 清理所有docid的版本
            versionSizeArray[i] = getRandomInt(maxVersion);
        }
        Map<String, DocVersion> dvmap = new HashMap<String, DocVersion>();
        for (int i = 0; i < count; i++) {
            for (int j = 1; j <= versionSizeArray[i]; j++) {
                DocVersion temp = getRandomExample(pairs[i].getAppid(), pairs[i].getDocid());
                DocVersion newVersion = dvo.create(temp); // 每个docid插入一个随机版本temp
                pairs[i].setVersion(j);
                dvmap.put(pairs[i].toString(), newVersion);
            }
        }
        for (int i = 0; i < count; i++) {
            pairs[i].setVersion(versionSizeArray[i]); // 最后一个版本
        }
        Thread.sleep(1000);
        assertBatchRead(pairs, dvmap);
        for (int i = 0; i < count; i++) {
            pairs[i].setVersion(1); // 第一个版本
        }
        assertBatchRead(pairs, dvmap);
    }

    private void assertBatchRead(DPair[] pairs, Map<String, DocVersion> dvmap) {
        List<DocVersion> vlist = dvo.batchRead(pairs);
        Assert.assertNotNull(vlist);
        Assert.assertTrue(vlist.size() > 0);
        for (DocVersion v : vlist) {
            DPair p = new DPair(v.getAppid(),v.getDocid(),v.getVersion());
//            System.out.println("Target Key:" + p);
            assertDocVersionEquals(v,dvmap.get(p.toString()));
        }
    }

    public void assertDocVersionEquals(DocVersion v1, DocVersion v2) {
//        System.out.println("DocVersion in db:" + v1);
//        System.out.println("DocVersion in mm:" + v2);
        boolean flag = (v1 != null && v2 != null) && (v1.getAppid() == v2.getAppid())
                && (v1.getDocid() == v2.getDocid()) && (v1.getVersion() == v2.getVersion())
                && (v1.getFilename().equals(v2.getFilename())) && (v1.getId() == v2.getId());
        Assert.assertTrue(flag);
    }

}
