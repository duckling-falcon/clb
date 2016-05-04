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
package cn.vlabs.clb.api;

import java.util.Locale;

public enum CLBStatus {
    NOT_READY, READY, FAILED, ZOMBIE;
    @Override
    public String toString() {
        return this.name().toLowerCase(Locale.US);
    }

    public static boolean isReady(String type) {
        return isEqualStatus(READY, type);
    }

    public static boolean isNotReady(String type) {
        return isEqualStatus(NOT_READY, type);
    }

    public static boolean isFailed(String type) {
        return isEqualStatus(FAILED, type);
    }

    public static boolean isZombie(String type) {
        return isEqualStatus(ZOMBIE, type);
    }

    private static boolean isEqualStatus(CLBStatus s, String actual) {
        if (actual == null)
            return false;
        return s.toString().equals(actual.toLowerCase(Locale.US));
    }

    public static CLBStatus getStatus(String str) {
        if (isReady(str)) {
            return CLBStatus.READY;
        } else if (isNotReady(str)) {
            return CLBStatus.NOT_READY;
        } else if (isFailed(str)) {
            return CLBStatus.FAILED;
        } else if (isZombie(str)) {
            return CLBStatus.ZOMBIE;
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(CLBStatus.isNotReady("Not_ready"));
        System.out.println(CLBStatus.isReady("Ready"));
        System.out.println(CLBStatus.isFailed("Failed"));
        System.out.println(CLBStatus.isZombie("zoMbine"));
    }
}
