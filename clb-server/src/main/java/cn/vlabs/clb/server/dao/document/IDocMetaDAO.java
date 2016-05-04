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

package cn.vlabs.clb.server.dao.document;

import java.util.List;
import java.util.Map;

import cn.vlabs.clb.server.dao.GeneralDataBaseException;
import cn.vlabs.clb.server.model.DocMeta;

public interface IDocMetaDAO {
    /**
     * 创建一个新文档对象
     * 
     * @param doc
     *            文档对象
     * @return 新创建的文档对象的ID号
     * @throws GeneralDataBaseException
     */
    int create(DocMeta doc);

    /**
     * 删除文档对象
     * 
     * @param docid
     *            被删除的文档的id
     * @throws GeneralDataBaseException
     */
    int delete(int appid, int docid);

    /**
     * 更新文档信息
     * 
     * @param doc
     *            被更新的文档的内容
     *            以id作为检索条件
     * @throws GeneralDataBaseException
     */
    void update(DocMeta doc);
    
    void update(DocMeta doc,String[] queryFields);

    /**
     * 读取一个文档对象内容
     * 
     * @param docid
     *            文档ID
     * @return 文档对象
     * @throws GeneralDataBaseException
     */
    DocMeta read(int appid, int docid);

    DocMeta readByMaxId();

    List<DocMeta> batchRead(int appid, int[] docids);

    Map<Integer, Integer> getMaxDocId();

}
