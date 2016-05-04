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
package cn.vlabs.clb.api.image;

import java.util.Map;

import cn.vlabs.clb.api.AccessForbidden;
import cn.vlabs.clb.api.InvalidArgument;
import cn.vlabs.clb.api.InvalidOperation;
import cn.vlabs.clb.api.ResourceNotFound;
import cn.vlabs.rest.IFileSaver;

public interface IResizeImageService {

    /**
     * @description 压缩图片，根据配置文件中的默认配置
     * @param docid
     *            图片的CLB文档号
     * @param version
     *            图片的CLB文档版本号 合法输入为大于0的整数或者"latest"
     * @return 压缩图片对象
     */
    ResizeImage resize(int docid, String version) throws InvalidArgument,AccessForbidden, InvalidOperation, ResourceNotFound;

    /**
     * @description 压缩图片的操作
     * @param docid
     *            图片的CLB文档号
     * @param version
     *            图片的CLB文档版本号 合法输入为大于0的整数或者"latest"
     * @param resizeParam
     *            图片压缩参数
     * @return 压缩图片对象
     */
    ResizeImage resize(int docid, String version, final ResizeParam resizeParam) throws InvalidArgument,AccessForbidden,
            InvalidOperation, ResourceNotFound;

    /**
     * @description 根据文档ID和版本号获取压缩图片对象
     * @param docid
     *            图片的CLB文档号
     * @param version
     *            图片的CLB文档版本号 合法输入为大于0的整数或者"latest"
     * @return 压缩图片对象
     */
    ResizeImage getResizeImage(int docid, String version) throws InvalidArgument,AccessForbidden,ResourceNotFound;
    
    /**
     * @description 获取压缩图片的文件流，如没有找到对应的压缩图时，返回原始图；
     * @param docid 文档ID号
     * @param version 文档版本号
     * @param type 图片压缩的类型，有三个值small,medium和large
     * @param fs 文档流回调接口
     */
    void getContent(int docid,String version,String type, IFileSaver fs) throws ResourceNotFound, AccessForbidden;
    
    /**
     * @description 获取压缩图片的文件流，如没有找到对应的压缩图时，返回原始图；
     *      如设置resizeParam同时没有找到压缩图时，发送一个新的压缩请求
     * @param docid 文档ID号
     * @param version 文档版本号
     * @param type  图片压缩的类型，有三个值small,medium和large
     * @param resizeParam 当没有找到压缩图时，以此参数重新发送一个压缩请求，并返回原图
     * @param fs 文档流回调接口
     */
    void getContent(int docid, String version, String type, ResizeParam resizeParam, IFileSaver fs);
    
    /**
     * @description 获取压缩后图片的文件流，使用ImageQuery类进行查询；
     *          如设置needOriginal为true则在找不到压缩图的情况下返回原图；
     *          ImageQuery中的ResizeParam参数将在没有找到对应压缩图时起作用；
     * @param query 查询封装类，包含文档号、版本、需要压缩的参数和是否需要原图选项
     * @param fs 文档流回调接口
     */
    void getContent(ImageQuery query, IFileSaver fs) throws ResourceNotFound, AccessForbidden;
    
    /**
     * @description 查询图片压缩状态
     * @param docid 原始图片的CLB文档号
     * @param version 图片的CLB版本号 合法输入为大于0的整数或者"latest"
     * @return 返回一个Map，Key为枚举值small, medium, large; Value是对应的ImageMeta
     */
    Map<String,ImageMeta> queryResizeStatus(int docid, String version);
    
}
