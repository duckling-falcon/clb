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
package cn.vlabs.clb.api.rest;

import cn.vlabs.clb.api.CLBConnection;
import cn.vlabs.clb.api.UnImplemented;
import cn.vlabs.clb.api.document.DocPair;
import cn.vlabs.clb.api.trivial.ITrivialSpaceService;
import cn.vlabs.clb.api.trivial.TrivialSpace;
import cn.vlabs.clb.api.trivial.TrivialParam;

public class TrivialSpaceServiceImpl implements ITrivialSpaceService {

    private static final String CREATE_TRIVIAL_SPACE = "trivial.create.space";

    private static final String GET_TRIVIAL_SPACE = "trivial.get.space";

    private static final String UPDATE_TRIVIAL_SPACE = "trivial.update.space";

    private static final String DELETE_TRIVIAL_SPACE = "trivial.delete.space";

    private CLBConnection conn;

    public TrivialSpaceServiceImpl(CLBConnection conn) {
        this.conn = conn;
    }

    @Override
    public TrivialSpace createTrivialSpace(int docid, String version, String unzipBaseDir) {
        TrivialParam tp = new TrivialParam();
        tp.setDocPair(new DocPair(docid, version));
        tp.setUnzipBaseDir(unzipBaseDir);
        tp.setOperation(TrivialParam.OP_CREATE);
        return (TrivialSpace) conn.sendService(CREATE_TRIVIAL_SPACE, tp);
    }

    @Override
    public boolean updateTrivialSpace(int docid, String version, String unzipBaseDir, String spaceName) {
        TrivialParam tp = new TrivialParam();
        tp.setDocPair(new DocPair(docid, version));
        tp.setUnzipBaseDir(unzipBaseDir);
        tp.setOperation(TrivialParam.OP_UPDATE);
        tp.setSpaceName(spaceName);
        return (boolean) conn.sendService(UPDATE_TRIVIAL_SPACE, tp);
    }

    @Override
    public TrivialSpace getTrivialSpace(String spaceName) {
        return (TrivialSpace) conn.sendService(GET_TRIVIAL_SPACE, spaceName);
    }

    @Override
    public boolean removeTrivialSpace(String spaceName) {
        return (boolean) conn.sendService(DELETE_TRIVIAL_SPACE, spaceName);
    }

    @Override
    public TrivialSpace[] getTrivialSpaces(DocPair[] pairs) {
        throw new UnImplemented("TO DO");
    }

}
