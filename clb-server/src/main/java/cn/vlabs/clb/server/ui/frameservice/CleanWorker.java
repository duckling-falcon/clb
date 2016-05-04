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
package cn.vlabs.clb.server.ui.frameservice;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

public class CleanWorker extends Thread {

    private Map<String, Date> toDeleteMap = new ConcurrentHashMap<String, Date>();
    private static final int MAX_ALIVE_TIME = 2 * 60; // 2min
    private static final int MAX_SLEEP_INTEVAL = 10 * 1000; //10min or 1s
    private boolean stopFlag = true;
    private static final Logger LOG = Logger.getLogger(CleanWorker.class);
    
    public boolean isStopFlag() {
        return stopFlag;
    }

    public void setStopFlag(boolean stopFlag) {
        this.stopFlag = stopFlag;
    }

    public CleanWorker(){
        this.setDaemon(true);
    }
    
    public void run() {
        while(!stopFlag){
            Calendar ca = Calendar.getInstance();
            Iterator<Entry<String, Date>> iter = toDeleteMap.entrySet().iterator();
            while (iter.hasNext()) { //iter方法迭代可以避免ConcurrentModificationException
                Entry<String, Date> en = iter.next();
                Date lastEditTime = en.getValue();
                ca.setTime(lastEditTime);
                Date now = new Date();
                ca.add(Calendar.SECOND, MAX_ALIVE_TIME);
                Date expireDate = ca.getTime();
                if (now.after(expireDate)) {
                    File f = new File(en.getKey());
                    if (f.exists()) {
                        f.delete();
                    }
                    iter.remove();
                }
            }
            try {
                Thread.sleep(MAX_SLEEP_INTEVAL);
            } catch (InterruptedException e) {
                LOG.info("Clean worker thread is going to close.");
            }
        }
    }

    public void addNewJob(String toDeletePath) {
        toDeleteMap.put(toDeletePath, new Date());
    }

    public void removeJob(String toDeletePath) {
        toDeleteMap.remove(toDeletePath);
    }
    
}
