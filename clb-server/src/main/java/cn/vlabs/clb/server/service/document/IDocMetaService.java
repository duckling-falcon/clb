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
package cn.vlabs.clb.server.service.document;

import java.util.List;

import cn.vlabs.clb.server.model.DocMeta;

public interface IDocMetaService {

    public int create(DocMeta doc);

    public DocMeta read(int appid,int docid);
    
    public List<DocMeta> batchRead(int appid,int[] docids);

    public void update(DocMeta doc);
    
    public void update(DocMeta doc,String[] queryFields);

    public void delete(int appid,int docid);

}
