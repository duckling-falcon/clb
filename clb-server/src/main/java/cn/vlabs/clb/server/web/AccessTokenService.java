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

import java.io.UnsupportedEncodingException;
import java.util.List;

import net.duckling.falcon.api.serialize.JSONMapper;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import cn.vlabs.clb.server.ui.frameservice.DPair;

/**
 * @title: AccessTokenService.java
 * @package cn.vlabs.clb.server.web
 * @description: token由一个5分钟有效的公钥、appid、docid和version共同生成
 *               在redis中存储的数据结构为 token -> appid,docid,version
 * @author clive
 * @date 2014-3-18 上午11:51:16
 */
public class AccessTokenService {

    private static final String CLB_PUBLIC_KEY = "clb.publicKey";

    private JedisPool pool;

    private static final Logger LOG = Logger.getLogger(AccessTokenService.class);

    public void close() {
        pool.destroy();
        LOG.info("Access Token Service is closed.");
    }

    public AccessTokenService(String host, int port) {
        JedisPoolConfig conf = new JedisPoolConfig();
        conf.setMaxActive(100);
        conf.setMaxIdle(20);
        conf.setMaxWait(1000);
        pool = new JedisPool(conf, host, port);
    }

    public String getAccessToken(int appid, int docid, int version) { //生成token
        Jedis jdc = pool.getResource();
        String key = getPublicKey();
        String token = generateToken(appid,docid,version,key);
        pool.returnResource(jdc);
        return token;
    }
    
    private String generateToken(int appid, int docid, int version,String publicKey){
        try {
            String raw = appid+"#"+docid+"#"+version;
            byte[] b = Base64.encodeBase64(raw.getBytes(), true);
            String firstKey = new String(b,"UTF-8");
            String secondLevel = publicKey.substring(0,10)+firstKey+publicKey.substring(11);
            String token = DigestUtils.md5Hex(secondLevel);
            add(token,appid,docid,version);
            return token;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getPublicKey(){ //获得并刷新这个公钥
        Jedis jdc = pool.getResource();
        String key = jdc.get(CLB_PUBLIC_KEY);
        if(StringUtils.isEmpty(key)){
           key = refreshPublicKey();
        }
        pool.returnResource(jdc);
        return key;
    }
    
    private String refreshPublicKey(){ //5分钟刷新一次公钥
        Jedis jdc = pool.getResource();
        String key = new ObjectId().toString();
        jdc.set(CLB_PUBLIC_KEY, key);
        jdc.expire(CLB_PUBLIC_KEY, 5*60);
        pool.returnResource(jdc);
        return key;
    }

    private void add(String token, int appid, int docid, int version) {
        Jedis jdc = pool.getResource();
        String key = tokenKey(token);
        jdc.rpush(key, String.valueOf(appid), String.valueOf(docid), String.valueOf(version));
        jdc.expire(key, 5*60);
        pool.returnResource(jdc);
    }

    private String tokenKey(String token) {
        return "clb.token."+token;
    }
    
    public boolean isValidatePublicKey(String token){
        Jedis jdc = pool.getResource();
        boolean flag = jdc.exists(tokenKey(token));
        pool.returnResource(jdc);
        return flag;
    }

    public DPair parseAccessToken(String accessToken) {
        Jedis jdc = pool.getResource();
        String key = tokenKey(accessToken);
        boolean flag = jdc.exists(key);
        if(flag){
            List<String> list = jdc.lrange(key, 0, 2);
            int appid = Integer.parseInt(list.get(0));
            int docid = Integer.parseInt(list.get(1));
            int version = Integer.parseInt(list.get(2));
            pool.returnResource(jdc);
            return new DPair(appid, docid, version);
        }
        pool.returnResource(jdc);
        return null;
    }
    
    private static final String FILE_INFO_PREFIX = "clb.file.info.";
    
    public void setFileInfo(String key, FileInfo info){
        String infoStr = JSONMapper.getJSONString(info);
        Jedis jdc = pool.getResource();
        jdc.set(FILE_INFO_PREFIX+key, infoStr);
        pool.returnResource(jdc);
    }
    
    public FileInfo getFileInfo(String skey) {
        Jedis jdc = pool.getResource();
        String infoStr = jdc.get(FILE_INFO_PREFIX+skey);
        pool.returnResource(jdc);
        if (StringUtils.isEmpty(infoStr)) {
            return null;
        }
        return JSONMapper.getBean(infoStr, FileInfo.class);
    }

}
