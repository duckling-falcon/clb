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

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LocalGeneratorServiceTest {

    LocalGeneratorService lgs = new LocalGeneratorService();
    @Before
    public void setUp() throws Exception {
    }
    
    @Test
    public void testGetObjectId() throws InterruptedException{
        List<MyThread> list = new ArrayList<MyThread>();
        int count = 100;
        for(int i=0;i<count;i++){
            list.add(new MyThread());
        }
        for(MyThread t:list){
            t.start();
        }
        Thread.sleep(1000);
        for(int i=0;i<count;i++){
            for(int j=0;j<count;j++){
                if(i!=j){
                    Assert.assertFalse(list.get(i).getObjectId().toString().equals(list.get(j).getObjectId().toString()));
                }
            }
        }
    }
    
    
    class MyThread extends Thread{
        private ObjectId obj;
        public ObjectId getObjectId(){
            return obj;
        }
        public void run(){
            obj = lgs.getStorageKey();
        }
    }


}
