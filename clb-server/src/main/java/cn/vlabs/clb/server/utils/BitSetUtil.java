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
package cn.vlabs.clb.server.utils;

import java.util.BitSet;

import org.bouncycastle.util.encoders.Base64;

public class BitSetUtil {

    public static BitSet deserialize(String src) {
        if (src == null || "".equals(src)) {
            return new BitSet();
        }
        byte[] buf = Base64.decode(src);
        return new BitSet().valueOf(buf);
    }

    public static String serialize(BitSet bitSet) {
        if (bitSet != null) {
            byte[] buf = Base64.encode(bitSet.toByteArray());
            return new String(buf);
        }
        return null;
    }

    public static String printBitSet(BitSet bs) {
        return bs.toString();
    }
    
    public static void main(String[] args) {
        BitSetUtil.deserialize(null);
        System.out.println("finish");
    }

}
