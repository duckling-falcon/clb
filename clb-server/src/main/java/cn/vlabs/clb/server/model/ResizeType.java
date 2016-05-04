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
package cn.vlabs.clb.server.model;

import java.util.Locale;

public enum ResizeType {
    SMALL, MEDIUM, LARGE;
    @Override
    public String toString() {
        return this.name().toLowerCase(Locale.US);
    }
    
    private static boolean isEqualStatus(ResizeType t, String actual) {
        if (actual == null)
            return false;
        return t.toString().equals(actual.toLowerCase(Locale.US));
    }
    
    private static boolean isLarge(String str) {
        return isEqualStatus(ResizeType.LARGE,str);
    }

    private static boolean isMedium(String str) {
        return isEqualStatus(ResizeType.MEDIUM,str);
    }
    
    public static boolean isSmall(String str){
        return isEqualStatus(ResizeType.SMALL, str);
    }
    
    public static ResizeType getEnum(String str) {
        if (isSmall(str)) {
            return ResizeType.SMALL;
        } else if(isMedium(str)){
            return ResizeType.MEDIUM;
        } else if(isLarge(str)) {
            return ResizeType.LARGE;
        }
        else {
            return null;
        }
    }



}
