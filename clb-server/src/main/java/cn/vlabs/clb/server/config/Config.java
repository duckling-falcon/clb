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

package cn.vlabs.clb.server.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class Config {

    private Properties props;
    private static Config config;
    private Logger log;
    private Pattern pattern = Pattern.compile("([^\\}]*)\\$\\{([^}]*)\\}(.*)");

    /**
     * 获取配置文件读取类的实例
     * 
     * @return 配置读取对象的实例
     */
    public static Config getInstance() {
        if (config == null) {
            config = new Config();
        }
        return config;
    }

    private Config() {
        log = Logger.getLogger(Config.class);
        props = new Properties();
        String appRootPath = System.getProperty("clb.appRoot");
        String clbConfPropPath = appRootPath + "WEB-INF" + File.separator + "conf" + File.separator
                + "clbconfig.properties";
        Resource res = new FileSystemResource(clbConfPropPath);
        try {
            props = PropertiesLoaderUtils.loadProperties(res);
        } catch (FileNotFoundException e) {
            log.error("配置文件未找到。", e);
        } catch (IOException e) {
            log.error(e);
        }
    }
    
    public String getConvertConfigPath(){
        String appRootPath = System.getProperty("clb.appRoot");
        return  appRootPath + "WEB-INF" + File.separator + "conf" + File.separator + "clb-convert.cfg";
    }
    
    public String getProbeConvertConfigPath(){
        String appRootPath = System.getProperty("clb.appRoot");
        return  appRootPath + "WEB-INF" + File.separator + "conf" + File.separator + "clb-probe.cfg";
    }

    public Object setProperty(String key, String value) {
        return this.props.setProperty(key, value);
    }

    /**
     * 从配置文件中读取一个整数值
     * 
     * @param key
     *            该配置项的关键字
     * @param defaultval
     *            缺省值
     * @return 如果找到了该配置项，并且能转换成整数值，则返回读取的内容。否则返回缺省值
     */
    public int getInt(String key, int defaultval) {
        String value = getStringProp(key, null);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                log.warn("配置错误" + key + "=" + value + " 不是整数值");
            }
        }
        return defaultval;
    }

    /**
     * 从配置文件中读取一个布尔值
     * 
     * @param key
     *            该配置项的关键字
     * @param defaultValue
     *            缺省值
     * @return 如果找到了该配置项，并且能转换成布尔值，则返回读取的内容。否则返回缺省值
     */
    public boolean getBooleanProp(String key, boolean defaultValue) {
        String value = getStringProp(key, null);
        if (value != null)
            return Boolean.parseBoolean(value);
        else
            return defaultValue;
    }

    /**
     * 从配置文件中获取一个字符串值
     * 
     * @param key
     *            该配置项的关键字
     * @param defaultval
     *            缺省值
     * @return 如果找到了该配置项，则返回读取的内容。否则返回缺省值
     */
    public String getStringProp(String key, String defaultval) {
        String value = props.getProperty(key);
        if (value != null) {
            return replace(value);
        } else
            return defaultval;
    }

    private String replace(String input) {
        input = input.trim();
        int dollerPos = input.indexOf('$');
        if (dollerPos != -1) {
            Matcher matcher = pattern.matcher(input);
            if (matcher.matches()) {
                String left = matcher.group(1);
                String value = getStringProp(matcher.group(2), null);
                if (value == null)
                    value = System.getProperty(matcher.group(2));
                if (value == null)
                    value = matcher.group(2);
                String right = replace(matcher.group(3));
                return left + value + right;
            }
        }
        return input;
    }


    /*
     * private String guessPath() { String result =
     * System.getProperty(CONF_DIR); if (isRightPlace(result)) return result;
     * else { return defaultPath(); } }
     */

    /*
     * private boolean isRightPlace(String path) { if (path != null) { File f =
     * new File(path); return (f.exists() && f.isDirectory()); } else return
     * false; }
     * 
     * private String defaultPath() { String uri =
     * Config.class.getResource("").toString(); uri = uri.substring(6,
     * uri.length()); int num = uri.indexOf("WEB-INF"); uri = uri.substring(0,
     * num + "WEB-INF".length()); return uri; }
     */

}
