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

package cn.vlabs.clb.api;

import cn.vlabs.rest.ServiceException;

public class ErrorCode {
    public static CLBException transform(ServiceException e) {
        CLBException result = null;
        switch (e.getCode()) {
        case ACCESS_FORBIDDEN:
            result = new AccessForbidden(e.getMessage());
            break;
        case CONTENT_NOT_FOUND:
            result = new ResourceNotFound(e.getCode(), e.getMessage());
            break;
        case META_NOT_FOUND:
            result = new ResourceNotFound(e.getCode(), e.getMessage());
            break;
        case INVALID_OPERATION:
            result = new InvalidOperation(e.getCode(), e.getMessage());
            break;
        case INVALID_ARGUMENT:
            result = new InvalidArgument(e.getCode(),e.getMessage());
            break;
        case STATUS_NOT_READY:
            result = new StatusNotReadyException(e.getCode(), e.getMessage());
            break;
        default:
            result = new CLBException(e.getCode(), e.getMessage());
            break;
        }
        return result;
    }

    /**
     * 该文档还没有准备好
     */
    public static final int STATUS_NOT_READY = 417;
    /**
     * 试图进行不允许的操作
     */
    public static final int INVALID_OPERATION = 415;
    /**
     * 输入非法的参数
     */
    public static final int INVALID_ARGUMENT = 416;
    /**
     * 要访问的文件元信息没有找到
     */
    public static final int META_NOT_FOUND = 413;
    /**
     * 要访问的资源没有找到
     */
    public static final int CONTENT_NOT_FOUND = 401;
    /**
     * 权限访问失败
     */
    public static final int ACCESS_FORBIDDEN = 403;
    /**
     * 服务器内部错误
     */
    public static final int INTERNAL_ERROR = 402;
    /**
     * 参数错误
     */
    public static final int BAD_PARAMETER = 404;
    /**
     * 锁已经被强制解锁了
     */
    public static final int HIJACKED = 405;
    /**
     * 文档已被锁定
     */
    public static final int ALREADY_LOCKED = 406;
    /**
     * 需要登录
     */
    public static final int NEED_AUTH = 407;
    /**
     * 强制解锁失败
     */
    public static final int HIJACK_FAIL = 411;
    /**
     * 解锁失败
     */
    public static final int UNLOCK_FAIL = 412;
    
}
