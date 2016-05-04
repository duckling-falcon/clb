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
package cn.vlabs.clb.server.dao;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cn.vlabs.clb.server.model.PdfItem;

public class QueryHelper {

    public static <T> T getQueryItem(int docid, int version, Class clazz) {
        Method[] methods = clazz.getMethods();
        try {
            Constructor cons = clazz.getConstructor();
            T t = (T) cons.newInstance();
            for (Method m : methods) {
                if ("setDocid".equals(m.getName())) {
                    m.invoke(t, docid);
                }
                if ("setVersion".equals(m.getName())) {
                    m.invoke(t, version);
                }
            }
            return t;
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        PdfItem t = QueryHelper.getQueryItem(1, 2, PdfItem.class);
        System.out.println(t.getDocid());
        System.out.println(t.getVersion());
    }

}
