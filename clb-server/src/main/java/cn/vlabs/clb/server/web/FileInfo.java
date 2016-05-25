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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

public class FileInfo {
    private String sn;
    private String fileName;
    private String filePath;
    
    public FileInfo(String sn,String fileName,String filePath){
        this.sn = sn;
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getExt() {
        return StringUtils.substring(this.getFileName().toLowerCase(), this.getFileName().lastIndexOf(".") + 1);
    }

    public File getFile() {
        File file = new File(this.getFilePath());
        return file;
    }

    public long getSize() {
        return this.getFile().length();
    }

    public String getSHA256() throws IOException {
        File file = this.getFile();
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        byte[] temp = new byte[1024];
        int size = 0;
        while ((size = in.read(temp)) != -1) {
            out.write(temp, 0, size);
        }
        in.close();

        byte[] content = out.toByteArray();

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(content);
            String output = Hex.encodeHexString(hash);
            System.out.println(output);
            return output;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";
    }
    
    public String getOwaServerUrl(String owaServerDomain, String mode, String wopiSrc, String accessToken) {
        OwaConfig oc = OwaConfig.getInstance();
        String tempMode = mode;
        if(StringUtils.isEmpty(mode)){
            tempMode = "view";
        }
        return "http://" + owaServerDomain + oc.getServiceUrl(tempMode, this.getExt()) + "WOPISrc=" + wopiSrc + "&access_Token=" + accessToken;
    }
    
    

}
