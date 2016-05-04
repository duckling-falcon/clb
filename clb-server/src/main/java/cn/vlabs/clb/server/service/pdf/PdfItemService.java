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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.vlabs.clb.server.dao.pdf.IPdfItemDAO;
import cn.vlabs.clb.server.model.PdfItem;
import cn.vlabs.clb.server.ui.frameservice.DPair;

@Service
public class PdfItemService implements IPdfItemService {

    @Autowired
    private IPdfItemDAO pdfItemDAO;
    
    @Override
    public PdfItem read(DPair p) {
        return pdfItemDAO.read(p);
    }

    @Override
    public int update(PdfItem pdf) {
        return pdfItemDAO.update(pdf);
    }

    @Override
    public int delete(DPair p) {
        return pdfItemDAO.delete(p);
    }

    @Override
    public int create(PdfItem pdf) {
        return pdfItemDAO.create(pdf);
    }

}
