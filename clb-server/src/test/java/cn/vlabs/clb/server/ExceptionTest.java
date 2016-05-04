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

import org.junit.Test;

import cn.vlabs.clb.server.exception.MetaNotFoundException;
import cn.vlabs.clb.server.exception.NoAuthorityException;

public class ExceptionTest {

    @Test(expected = NoAuthorityException.class)
    public void testNoAuthorityExceptionInt() throws NoAuthorityException {
        throw new NoAuthorityException(1, "abc", "def");
    }

    @Test(expected = MetaNotFoundException.class)
    public void testMetaNotFoundException() throws MetaNotFoundException {
        throw new MetaNotFoundException(1, 2, "document");
    }

}
