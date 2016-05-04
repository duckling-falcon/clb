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
package cn.vlabs.clb.api.pdf;

public class PdfStatusCode {
    
    public static final int INTERNAL_ERROR = -1;
    public static final int NOT_FOUND_SOURCE_FILE = 0;
    public static final int CONVERT_ONGOING = 1;
    public static final int CONVERT_SUCCESS_AND_HAS_MORE = 7;
    public static final int CONVERT_SUCCESS = 2;
    public static final int CONVERT_FAILED = 3;
    public static final int UNCONVERT_SOURCE_FILE = 4;
    public static final int ENCRYPTED_SOURCE_FILE = 5;
    public static final int CORRUPT_SOURCE_FILE = 6;

}
