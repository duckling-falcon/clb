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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.vlabs.clb.server.dao.image.IImageItemDAO;
import cn.vlabs.clb.server.model.ImageItem;
import cn.vlabs.clb.server.model.ResizeType;
import cn.vlabs.clb.server.ui.frameservice.DPair;

@Service
public class ImageItemService implements IImageItemService {

    @Autowired
    private IImageItemDAO imageItemDAO;

    @Override
    public int create(ImageItem v) {
        return imageItemDAO.create(v);
    }

    @Override
    public List<ImageItem> read(DPair p) {
        return imageItemDAO.readByDocPair(p);
    }

    @Override
    public int update(ImageItem v) {
        return imageItemDAO.update(v);
    }

    @Override
    public int delete(DPair p,String resizeType) {
        return imageItemDAO.delete(p,resizeType);
    }

    @Override
    public List<ImageItem> readByStorageKey(String targetKey) {
        return imageItemDAO.readByStorageKey(targetKey);
    }

    @Override
    public int create(List<ImageItem> items) {
        return imageItemDAO.create(items);
    }

    @Override
    public List<ImageItem> readAll(int appid, Date startTime, Date endTime) {
        return imageItemDAO.readBeforeDate(appid, startTime, endTime);
    }

    @Override
    public int delete(DPair p, List<ResizeType> rt) {
        return imageItemDAO.delete(p, rt);
    }

    @Override
    public int delete(DPair p) {
        return imageItemDAO.delete(p);
    }

}
