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
package cn.vlabs.clb.server.dao.image;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cn.vlabs.clb.api.CLBStatus;
import cn.vlabs.clb.server.ServerBaseTest;
import cn.vlabs.clb.server.model.ImageItem;
import cn.vlabs.clb.server.model.ResizeType;
import cn.vlabs.clb.server.ui.frameservice.DPair;

public class IImageItemDAOTest extends ServerBaseTest {
    
    private static IImageItemDAO iio;

    @Before
    public void setUp() throws Exception {
        iio = f.getBean(IImageItemDAO.class);
    }
    
    private ImageItem getRandomExample(){
        ImageItem v = new ImageItem();
        v.setAppid(getRandomInt());
        v.setDocid(getRandomInt());
        v.setVersion(getRandomInt());
        v.setFileExtension("jpg");
        v.setFilename(getRandomInt()+ ".jpg");
        v.setStorageKey(getRandomInt() + "");
        v.setSize(getRandomInt());
        v.setUpdateTime(new Date());
        v.setResizeType(ResizeType.LARGE.toString());
        v.setStatus(CLBStatus.NOT_READY.toString());
        v.setHeight(getRandomInt());
        v.setWidth(getRandomInt());
        return v;
    }

    @Test
    public void testCreateImageItem() {
        ImageItem local = getRandomExample();
        int id = iio.create(local);
        local.setId(id);
        ImageItem remote = iio.readById(id);
        Assert.assertNotNull(remote);
        Assert.assertEquals(remote.getStatus(), local.getStatus());
        Assert.assertEquals(local.getSize(), remote.getSize());
        Assert.assertEquals(remote.getSize(), local.getSize());
        Assert.assertEquals(remote.getId(), local.getId());
        Assert.assertEquals(remote.getWidth(), local.getWidth());
    }
    
    @Test
    public void testUpdate() {
        ImageItem local = getRandomExample();
        int id = iio.create(local);
        local.setId(id);
        ImageItem remote = iio.readById(id);
        remote.setFilename("hello.png");
        remote.setUpdateTime(new Date());
        remote.setStatus(CLBStatus.READY.toString());
        int access = iio.update(remote);
        Assert.assertEquals(1, access);
        ImageItem nt = iio.readById(id);
        Assert.assertEquals(nt.getFilename(), remote.getFilename());
        assertDateEquals(nt.getUpdateTime(),remote.getUpdateTime());
    }
    
    @Test
    public void testDelete() {
        iio.create(getRandomExample());
        ImageItem item = iio.readByMaxId();
        if(item!=null){
            DPair p = new DPair(item.getAppid(),item.getDocid(), item.getVersion());
            List<ImageItem> list = iio.readByDocPair(p);
            iio.delete(p,ResizeType.SMALL.toString());
            iio.delete(p, ResizeType.MEDIUM.toString());
            iio.delete(p, ResizeType.LARGE.toString());
            Assert.assertEquals(list.size(), 1);
        }
    }
    
    @Test
    public void testReadByStorageKey() {
        ImageItem ex = getRandomExample();
        iio.create(ex);
        List<ImageItem> items = iio.readByStorageKey(ex.getStorageKey());
        Assert.assertTrue(items.size()>0);
    }

}
