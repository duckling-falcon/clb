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
package cn.vlabs.clb.server.service.pdf;

public class PdfItemStatus {
	
    public static final String ENCRYPTED = "encrypted";
    public static final String CORRUPT = "corrupt";
    public static final String NOFILE = "nofile";
    
    public static boolean isEncryptedFile(String status){
    	return ENCRYPTED.equals(status);
    }
    
    public static boolean isCorrupFile(String status){
    	return CORRUPT.equals(status);
    }
    
    public static boolean isNoFile(String status){
        return NOFILE.equals(status);
    }

}
