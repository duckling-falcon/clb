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

import cn.vlabs.clb.api.AccessForbidden;
import cn.vlabs.clb.api.InvalidArgument;
import cn.vlabs.clb.api.ResourceNotFound;
import cn.vlabs.rest.IFileSaver;

public interface IPdfService {

    /**
     * 下载指定版本的文档所对应的PDF文档
     * 
     * @param docid
     *            文档的ID
     * @param version
     *            文档的版本 合法输入为大于0的整数或者"latest"
     * @param fs
     *            文档内容保存对象
     * @return 文件流
     * @throws AccessForbidden
     *             如果当前操作用户不是文档的创建者，抛出该异常。
     * @throws ResourceNotFound
     *             如果请求的文档未找到，抛出该异常。
     */
    public void getPdfContent(int docid, String version, IFileSaver fs) throws InvalidArgument,ResourceNotFound, AccessForbidden;

    /**
     * 查询指定版本的文档所对应的PDF文档是否存在
     * 
     * @param docid
     *            文档的ID
     * @param version
     *            文档的版本 合法输入为大于0的整数或者"latest"
     * @return true or false
     * @throws AccessForbidden
     *             如果当前操作用户不是文档的创建者，抛出该异常。
     * @return 参考PdfStatusCode
     */
    public int queryPdfStatus(int docid, String version) throws InvalidArgument,AccessForbidden;

    /**
     * 发送将指定版本的文档转换成PDF文档的请求事件
     * 
     * @param docid
     *            文档的ID
     * @param version
     *            文档的版本 合法输入为大于0的整数或者"latest"
     * @throws AccessForbidden
     *             如果当前操作用户不是文档的创建者，抛出该异常。
     * @throws ResourceNotFound
     *             如果请求的文档未找到，抛出该异常。
     */
    public void sendPdfConvertEvent(int docid, String version) throws InvalidArgument,ResourceNotFound, AccessForbidden;

    /**
     * @description 获取PDF文件的直连URL
     * @param docid
     *            文档ID
     * @param version
     *            文档版本 合法输入为大于0的整数或者"latest"
     * @return 转化出来的PDF的直接下载URL
     */
    String getPDFURL(int docid, String version)  throws InvalidArgument,ResourceNotFound, AccessForbidden;
}
