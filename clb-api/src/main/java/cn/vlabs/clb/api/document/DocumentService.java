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

package cn.vlabs.clb.api.document;

import java.util.HashMap;
import java.util.Set;

import cn.vlabs.clb.api.AccessForbidden;
import cn.vlabs.clb.api.InvalidArgument;
import cn.vlabs.clb.api.ResourceNotFound;
import cn.vlabs.clb.api.io.CommonFileSaver;
import cn.vlabs.clb.api.io.ServletFileSaver;
import cn.vlabs.rest.IFileSaver;
import cn.vlabs.rest.stream.IResource;

/**
 * @Description: CLB文档的增删改操作接口 例子： CLBPasswdInfo pwd = new CLBPasswdInfo();
 *               pwd.setUsername("yourAppUsername");
 *               pwd.setPassword("yourAppPassword"); DocumentService
 *               service=CLBServiceFactory.getDocumentService(
 *               "http://yourServerIP:yourServerPort/clb/ServiceServlet", pwd);
 * @author CERC
 * @date Aug 9, 2011 11:33:48 AM
 * 
 */
public interface DocumentService {

    /**
     * @author: CERC
     * @Description: 创建单个文档
     * @param ci
     *            文档创建信息
     * @param si
     *            文档的数据流
     * @return 文档创建结果信息，包括docId和版本号
     * @throws
     */
	@java.lang.Deprecated
    public UpdateInfo createDocument(CreateInfo ci, IResource si);

    /**
     * @author: CERC
     * @Description: 创建单个文档
     * @param ci
     *            文档创建信息
     * @param si
     *            文档的数据流
     * @return 文档创建结果信息，包括docId和版本号 (Json字符串)
     * @throws
     */
    public UpdateInfo createDocument(CreateDocInfo ci, IResource si);
    
    
    public ChunkResponse prepareChunkUpload(String filename, String md5, long size);

    public ChunkResponse executeChunkUpload(int docid, int chunkedIndex, byte[] buf, int numOfBytes);

    public ChunkResponse finishChunkUpload(int docid);

    public Set<Integer> queryEmptyChunks(int docid);
    
    /**
     * 根据MD5查询Clbid
     * @param md5
     * @return
     */
    public Integer queryClbidForMD5(String md5,long size);
    
    /**
     * 添加clb上传记录
     * @param clbid
     * @param fileName
     * @param md5
     * @param size
     */
    @java.lang.Deprecated
    public void createClbRef(int clbid,String fileName,String md5,long size);
    
    /**
     * 添加clb上传记录
     * @param meta
     */
    public void createClbRef(ClbMeta meta);
    

    /**
     * 批量创建一个文档
     * 
     * @param ci
     *            文档创建信息
     * @param si
     *            zip文件数据流
     * @return 文档创建结果信息，包括docId和版本号
     */
    public HashMap<String, UpdateInfo> batchCreate(CreateInfo ci, IResource si);

    /**
     * 更新一个文档
     * 
     * @param docid
     *            文档编号
     * @param comment
     *            更新注释
     * @param si
     *            文档流信息
     * @return 返回更新信息，包括docId和版本号
     * @throws AccessForbidden
     *             如果当前操作用户不是文档的创建者，抛出该异常。
     * @throws ResourceNotFound
     *             如果请求的文档未找到，抛出该异常。
     */
    public UpdateInfo update(int docid, String comment, IResource si) throws ResourceNotFound, AccessForbidden;

    /**
     * 删除文档
     * 
     * @param docid
     *            文档ID
     * @return 文件流
     * @throws AccessForbidden
     *             如果当前操作用户不是文档的创建者，抛出该异常。
     * @throws ResourceNotFound
     *             如果请求的文档未找到，抛出该异常。
     */
    public void delete(int docid) throws AccessForbidden, ResourceNotFound;

    /**
     * 下载最新版本的文档
     * 
     * @param docid
     *            文档的ID
     * @param fs
     *            文档内容保存对象 <li>{@link ServletFileSaver}Servlet输出流保存对象</li> <li>
     *            {@link CommonFileSaver}普通文件保存对象</li>
     * @return 文件流
     * @throws AccessForbidden
     *             如果当前操作用户不是文档的创建者，抛出该异常。
     * @throws ResourceNotFound
     *             如果请求的文档未找到，抛出该异常。
     */
    public void getContent(int docid, IFileSaver fs) throws ResourceNotFound, AccessForbidden;

    /**
     * 下载指定版本的文档
     * 
     * @param docid
     *            文档的ID
     * @param version
     *            文档的版本
     * @param fs
     *            文档内容保存对象
     * @return 文件流
     * @throws AccessForbidden
     *             如果当前操作用户不是文档的创建者，抛出该异常。
     * @throws ResourceNotFound
     *             如果请求的文档未找到，抛出该异常。
     */
    public void getContent(int docid, String version, IFileSaver fs) throws InvalidArgument, ResourceNotFound,
            AccessForbidden;

    /**
     * 批量下载，得到zip文件
     * 
     * @param pairs
     *            需要下载的文档的ID和版本，如果版本为"latest"，则是最新版本
     * @param fs
     *            文档内容保存对象
     * @return 文件流
     * @throws AccessForbidden
     *             如果当前操作用户不是文档的创建者，抛出该异常。
     * @throws ResourceNotFound
     *             如果请求的文档未找到，抛出该异常。
     */
    public void getContent(DocPair[] pairs, IFileSaver fs) throws ResourceNotFound, AccessForbidden;

    /**
     * 批量下载，得到zip文件
     * 
     * @param docs
     *            需要下载的文档的ID号列表
     * @param fs
     *            保存下载文件的保存对象
     * @return 文件流
     * @throws AccessForbidden
     *             如果当前操作用户不是文档的创建者，抛出该异常。
     * @throws ResourceNotFound
     *             如果请求的文档未找到，抛出该异常。
     */
    public void getContent(int[] docs, IFileSaver fs) throws ResourceNotFound, AccessForbidden;

    /**
     * 获得指定文档的历史版本信息
     * 
     * @param docid
     *            文档的ID
     * @return 文档的版本历史信息
     * @throws ResourceNotFound
     *             如果请求的文档未找到，抛出该异常。
     */
    public VersionInfo[] getHistory(int docid) throws ResourceNotFound;

    /**
     * 获取指定文档的元数据信息
     * 
     * @param docid
     *            文档ID
     * @return 元数据信息
     * @return 文档的元数据信息
     * @throws AccessForbidden
     *             如果当前操作用户不是文档的创建者，抛出该异常。
     * @throws ResourceNotFound
     *             如果请求的文档未找到，抛出该异常。
     */
    @java.lang.Deprecated
    public MetaInfo getMeta(int docid) throws ResourceNotFound, AccessForbidden;
    
    public DocMetaInfo getDocMeta(int docid) throws ResourceNotFound, AccessForbidden;

    /**
     * 获取指定文档的指定版本的元数据信息
     * (请使用getDocMeta)
     * @param docid
     *            文档ID
     * @param version
     *            文档版本号
     * @return 元数据信息
     * @return 文档的元数据信息
     * @throws AccessForbidden
     *             如果当前操作用户不是文档的创建者，抛出该异常。
     * @throws ResourceNotFound
     *             如果请求的文档未找到，抛出该异常。
     */
    @java.lang.Deprecated
    public MetaInfo getMeta(int docid, String version) throws InvalidArgument, ResourceNotFound, AccessForbidden;
    
    public DocMetaInfo getDocMeta(int docid, String version) throws InvalidArgument, ResourceNotFound, AccessForbidden;

    /**
     * 根据文档的hashcode获取指定文档的元数据信息
     * 
     * @param hash
     *            文档的Hash值
     * @return 元数据信息
     * @return 文档的元数据信息
     * @throws AccessForbidden
     *             如果当前操作用户不是文档的创建者，抛出该异常。
     * @throws ResourceNotFound
     *             如果请求的文档未找到，抛出该异常。
     */
    @Deprecated
    public VersionInfo[] getMeta(String hash) throws ResourceNotFound, AccessForbidden;

    /**
     * @description 获取直接访问文档的URL地址
     * @param docid
     *            文档id号
     * @param version
     *            文档对应的版本号
     * @return 通过nginx直连的URL
     */
    public String getContentURL(int docid, String version) throws InvalidArgument, ResourceNotFound, AccessForbidden;

    /**
     * @description 删除文档
     * @param docid
     *            文档id号
     * @param version
     *            文档对应的版本号
     * @return 返回删除条数
     */
    public int removeDocument(int docid, String version) throws InvalidArgument, ResourceNotFound, AccessForbidden;

}
