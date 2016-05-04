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
package cn.vlabs.clb.server.service.pdf;

import org.springframework.stereotype.Service;

import cn.vlabs.clb.server.config.AppFacade;
import cn.vlabs.dconvert.api.DConvertConnection;
import cn.vlabs.dconvert.api.DConvertServiceFactory;
import cn.vlabs.dconvert.api.rest.ConvertFailInfo;
import cn.vlabs.dconvert.api.transform.DocConvertService;
import cn.vlabs.rest.ServiceContext;

@Service
public class PdfConvertService implements IPdfConvertService {
	
    private DConvertConnection conn = null;

    public void init() {
        if (conn == null) {
            String serverURL = AppFacade.getConfig("clb.dconvert.serverURL");
            ServiceContext.setMaxConnection(20, 20);
            conn = new DConvertConnection(serverURL);
        }
    }

    public void destory() {
        conn = null;
    }

    @Override
    public void convert(String srcKey, String desKey) {
        init();
        DocConvertService convert = DConvertServiceFactory.getDConvertService(conn);
        convert.convert(srcKey, desKey);
    }

    @Override
    public ConvertFailInfo isConvertSuccess(String targetKey) {
        init();
        DocConvertService convert = DConvertServiceFactory.getDConvertService(conn);
        return convert.isConvertError(targetKey);
    }
	
}
