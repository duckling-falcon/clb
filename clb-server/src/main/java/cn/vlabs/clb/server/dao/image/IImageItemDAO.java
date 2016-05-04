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
package cn.vlabs.clb.server.dao.image;

import java.util.Date;
import java.util.List;

import cn.vlabs.clb.server.model.ImageItem;
import cn.vlabs.clb.server.model.ResizeType;
import cn.vlabs.clb.server.ui.frameservice.DPair;

public interface IImageItemDAO {
    int create(ImageItem item);

    int create(List<ImageItem> items);

    int update(ImageItem item);

    int delete(DPair p, String resizeType);
    
    int delete(DPair p, List<ResizeType> resizeTypes);

    List<ImageItem> readByDocPair(DPair p);

    List<ImageItem> readByStorageKey(String targetKey);

    ImageItem readById(int id);

    ImageItem readByMaxId();

    List<ImageItem> readBeforeDate(int appid, Date startTime, Date endTime);

    int delete(DPair p);
}
