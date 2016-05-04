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
package cn.vlabs.clb.server;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import junit.framework.Assert;

import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import cn.vlabs.clb.server.service.SpringManager;

public class ServerBaseTest {

    public static int RAND_SEED = 6553690;
    public static BeanFactory f;
    public static Random rand = new Random();
    private static SpringManager testBase;

    @BeforeClass
    public static void setUpContext() throws Exception {
        if (testBase == null) {
            testBase = new SpringManager();
            testBase.init();
            f = testBase.getFactory();
        }
    }

    static {
        Resource rootClassRes = new ClassPathResource("/");
        try {
            String logRootDir = rootClassRes.getFile().getPath();
            System.setProperty("catalina.base", logRootDir);
            PropertyConfigurator.configure(logRootDir + "/log4j.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    }

    public static void assertDateEquals(Date d1, Date d2) {
        Assert.assertEquals(getDateFormat().format(d1), getDateFormat().format(d2));
    }

    public static int getRandomInt() {
        return assureLargeThanZero(RAND_SEED);
    }

    private static int assureLargeThanZero(int seed) {
        int num = rand.nextInt(seed);
        if (num == 0) {
            return 1;
        }
        return num;

    }

    public static int getRandomInt(int maxInt) {
        return assureLargeThanZero(maxInt);
    }
}
