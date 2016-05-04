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

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class WorkerThread extends Thread {

    protected volatile AtomicBoolean suspendedFlag = null;
    protected volatile AtomicBoolean stoppedFlag = null;

    @Override
    public synchronized void run() {
        synchronized (this) {
            while (true) {
                while (suspendedFlag.get()) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                work();
                if (stoppedFlag.get()) {
                    break;
                }
            }
        }
    }

    /**
     * @description 挂起当前线程
     */
    public synchronized void suspendThread() {
        suspendedFlag.set(true);
    }

    /**
     * @description 停止当前线程
     */
    public synchronized void stopThread() {
        stoppedFlag.set(true);
        suspendedFlag.set(false);
        if (!suspendedFlag.get()) {
            notifyAll();
        }
    }
    

    /**
     * @description 重启当前线程
     */
    public synchronized void resumeThread() {
        suspendedFlag.set(false);
        if (!suspendedFlag.get()) {
            notifyAll();
        }
    }

    public abstract void work();

}
