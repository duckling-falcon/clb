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
package cn.vlabs.clb.api.trivial;

import cn.vlabs.clb.api.AccessForbidden;
import cn.vlabs.clb.api.InvalidArgument;
import cn.vlabs.clb.api.ResourceNotFound;
import cn.vlabs.clb.api.document.DocPair;

public interface ITrivialSpaceService {

    /**
     * @description 将clb中某个文档解压生成一个地址空间
     * @param docid
     *            压缩包的文档ID号
     * @param version
     *            压缩包的版本号 合法输入为大于0的整数或者"latest"
     * @param unzipBaseDir
     *            解压的基准目录
     * @return 返回解压后的空间地址
     */
    TrivialSpace createTrivialSpace(int docid, String version, String unzipBaseDir) throws InvalidArgument,ResourceNotFound,
            AccessForbidden;

    /**
     * @description 更新某个地址空间下的所有文档，删除式的更新
     * @param docid
     *            新压缩包的文档号
     * @param version
     *            新压缩包的文档版本 合法输入为大于0的整数或者"latest"
     * @param unzipBaseDir
     *            解压的基准目录
     * @param spaceName
     *            原来的解压空间地址
     * @return 返回成功或者失败
     */
    boolean updateTrivialSpace(int docid, String version, String unzipBaseDir, String spaceName)
            throws InvalidArgument,ResourceNotFound, AccessForbidden;

    /**
     * @description 根据压缩包检索解压后的地址空间
     * @param spaceName
     *            空间地址
     * @return 返回对应的解压空间地址
     */
    TrivialSpace getTrivialSpace(String spaceName) throws InvalidArgument,ResourceNotFound, AccessForbidden;

    /**
     * @description TODO 方法说明
     * @param pairs
     * @return
     */
    TrivialSpace[] getTrivialSpaces(DocPair[] pairs);

    /**
     * @description 删除小文件空间
     * @param spaceName
     *            空间地址
     * @return 删除是否成功
     */
    boolean removeTrivialSpace(String spaceName) throws InvalidArgument,ResourceNotFound, AccessForbidden;

}
