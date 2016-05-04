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
package cn.vlabs.clb.server.service.image;

import java.util.Date;
import java.util.List;

import cn.vlabs.clb.server.model.ImageItem;
import cn.vlabs.clb.server.model.ResizeType;
import cn.vlabs.clb.server.ui.frameservice.DPair;

/**
 * @title: IImageItemService.java
 * @package cn.vlabs.clb.server.service.image
 * @description: image元信息存储服务
 * @author clive
 * @date 2013-8-15 下午5:26:48
 */
public interface IImageItemService {
    
    public int create(ImageItem v);
    
    public int create(List<ImageItem> v);

    public List<ImageItem> read(DPair p);

    public int update(ImageItem v);

    public int delete(DPair p);
    
    public int delete(DPair p, List<ResizeType> rt);
    
    public int delete(DPair p, String resizeType);

    public List<ImageItem> readByStorageKey(String targetKey);

    public List<ImageItem> readAll(int appid, Date time, Date endTime);
}
