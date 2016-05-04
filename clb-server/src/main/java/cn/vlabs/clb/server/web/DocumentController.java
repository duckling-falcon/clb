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
package cn.vlabs.clb.server.web;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import cn.vlabs.clb.server.config.AppFacade;
import cn.vlabs.clb.server.exception.FileContentNotFoundException;
import cn.vlabs.clb.server.model.AppAuthInfo;
import cn.vlabs.clb.server.model.DocVersion;
import cn.vlabs.clb.server.service.auth.IAppAuthService;
import cn.vlabs.clb.server.service.document.IDocVersionService;
import cn.vlabs.clb.server.storage.IStorageService;
import cn.vlabs.clb.server.ui.frameservice.DPair;
import cn.vlabs.clb.server.ui.frameservice.LocalFileCacheService;
import cn.vlabs.clb.server.ui.frameservice.SrcPair;
import cn.vlabs.clb.server.utils.ResponseHeaderUtils;

import com.mongodb.gridfs.GridFSDBFile;

@Controller
@RequestMapping("/wopi")
public class DocumentController {

    @Autowired
    private IDocVersionService docVersionService;

    @Autowired
    private LocalFileCacheService localFileCacheService;

    @Autowired
    private IAppAuthService appAuthService;

    @Autowired
    private AccessTokenService ats;

    @Autowired
    private IStorageService mongoStorage;

    private static final Logger LOG = Logger.getLogger(DocumentController.class);

    @ResponseBody
    @RequestMapping("/fetch/accessToken")
    public JSONObject getToken(@RequestParam("appname") String appname, @RequestParam("docid") Integer docid,
            @RequestParam("version") Integer version, @RequestParam("password") String password) {
        byte[] b = Base64.decodeBase64(password.getBytes());
        String realPasswd = new String(b);
        AppAuthInfo ai = appAuthService.authenticate(appname, realPasswd);
        JSONObject r = new JSONObject();
        if (ai != null) {
            int appid = ai.getId();
            r.put("appid", appid);
            String token = ats.getAccessToken(appid, docid, version);
            r.put("pf", token);
            r.put("status", "sucess");
            return r;
        } else {
            r.put("status", "failed");
            return r;
        }
    }

    @RequestMapping("/p")
    public ModelAndView getSSLRedirectView(@RequestParam("accessToken") String accessToken, HttpServletRequest request)
            throws FileContentNotFoundException, UnsupportedEncodingException {
        DPair dp = ats.parseAccessToken(accessToken);
        if (dp == null) {
            return new ModelAndView("/");
        }
        DocVersion dv = docVersionService.read(dp);
        String mode = request.getParameter("mode");
        GridFSDBFile gfile = AppFacade.getDocumentFacade().readDocContent(dv);
        SrcPair sp = localFileCacheService.loadOriginalImage(gfile);
        if (sp != null) {
            FileInfo info = new FileInfo(sp.getSrcStorageKey(), dv.getFilename(), sp.getSrcPath());
            ats.setFileInfo(sp.getSrcStorageKey(), info);
            String url = info.getOwaServerUrl(getOwaServerDomain(), mode,
                    getEncodedCheckFileUrl(sp.getSrcStorageKey()), accessToken);
            LOG.info("Access url:" + url);
            return new ModelAndView(new RedirectView(url));
        }
        return new ModelAndView("");
    }
    
    @ResponseBody
    @RequestMapping("/files/{skey}")
    public JSONObject checkFile(@PathVariable("skey") String skey) {
        LOG.info("Check File info");
        JSONObject obj = new JSONObject();
        FileInfo info = ats.getFileInfo(skey);
        obj.put("BaseFileName", info.getFileName());
        obj.put("OwnerId", "clive");
        obj.put("Size", info.getSize());
        obj.put("SHA256", "");
        obj.put("Version", "GIYDCMRNGEYC2MJREAZDCORQGA5DKNZOGIZTQMBQGAVTAMB2GAYA====");
        return obj;
    }

    @RequestMapping("/files/{skey}/contents")
    public void getFileContent(@PathVariable("skey") String skey, HttpServletRequest request,
            HttpServletResponse response) {
        GridFSDBFile dbfile = mongoStorage.loadDocument(skey);
        writeFileContentToResponse(dbfile, request, response);
    }

    private void writeFileContentToResponse(GridFSDBFile dbfile, HttpServletRequest request,
            HttpServletResponse response) {
        OutputStream os = null;
        try {
            long start = System.currentTimeMillis();
            response.setCharacterEncoding("utf-8");
            response.setContentLength((int) dbfile.getLength());
            response.setContentType("application/x-download");
            String filename = dbfile.getFilename();
            if (filename == null) {
                filename = "file" + dbfile.getId();
            }
            LOG.debug(dbfile.getFilename() + "," + dbfile.getLength());
            String headerValue = ResponseHeaderUtils.buildResponseHeader(request, filename, true);
            response.setHeader("Content-Disposition", headerValue);
            response.setHeader("Content-Length", dbfile.getLength() + "");
            os = response.getOutputStream();
            dbfile.writeTo(os);
            long end = System.currentTimeMillis();
            LOG.info("Read doc content using stream mode for doc [" + filename + "], use time " + (end - start) + "ms");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(os);
        }
    }

    private String getEncodedCheckFileUrl(String skey) throws UnsupportedEncodingException {
        return URLEncoder.encode(getWopiCheckFileUrl(getServerDomain(), "clb", skey), "utf-8");
    }

    private String getWopiCheckFileUrl(String serverDomain, String serverName, String fileSn) {
        return "http://" + serverDomain + "/" + serverName + "/wopi/files/" + fileSn;
    }

    private String getServerDomain() {
        return AppFacade.getConfig("clb.local.domain");
    }

    private String getOwaServerDomain() {
        return AppFacade.getConfig("clb.wopi.domain");
    }
}
