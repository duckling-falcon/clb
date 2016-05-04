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
package cn.vlabs.clb.server.ui.frameservice.trivial;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import cn.vlabs.clb.server.ServerBaseTest;
import cn.vlabs.clb.server.storage.IStorageService;
import cn.vlabs.clb.server.storage.mongo.MongoStorageService;

public class TrivialFacadeTest extends ServerBaseTest {
    private TrivialFacade tf;
    private IStorageService storage;

    @Before
    public void setUp() throws Exception {
        tf = f.getBean(TrivialFacade.class);
        storage = f.getBean(MongoStorageService.class);
    }

    @Test
    public void testDecompressZipFile() throws IOException {
        Resource res = new ClassPathResource("/");
        String srcDir = res.getFile().getPath() + File.separator + "trivial" + File.separator + "source"
                + File.separator;
        File zipFile = new File(srcDir+"resources.zip");
        tf.decompressZipFile(zipFile, storage.generateStorageKey(),"resources");
    }

}
