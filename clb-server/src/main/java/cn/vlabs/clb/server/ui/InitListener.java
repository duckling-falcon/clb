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
package cn.vlabs.clb.server.ui;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import cn.vlabs.clb.server.config.Config;
import cn.vlabs.clb.server.dao.SQLReader;

/**
 * @Description: 该服务用于初始化数据库和索引优化及重建服务
 * @author CERC
 * @date Aug 9, 2011 11:59:31 AM
 * 
 */
@SuppressWarnings("unchecked")
public class InitListener implements ApplicationListener {
    @Autowired
    @Qualifier("indexBuildExecutor")
    private ThreadPoolTaskExecutor indexBuildExecutor;
    @Autowired
    @Qualifier("indexReBuildExecutor")
    private ThreadPoolTaskExecutor indexReBuildExecutor;

    private boolean firstInit = true;

    public void onApplicationEvent(ApplicationEvent e) {
        if (e instanceof ContextRefreshedEvent) {
            if (firstInit) {
                String appRootPath = System.getProperty("clb.root");
                String confDirPath = appRootPath + "WEB-INF" + File.separator + "conf";
                // initialize config
                Config config = Config.getInstance();
                // initialize index optimizing service
                // used in AuthModuleFactory4UMT2
                config.setProperty("clb.DucklingkeysDir", confDirPath + File.separator + "DucklingkeysDir");
                // initialize database and tables
                String sqlfilePath = confDirPath + File.separator + "clb.sql";
                String databasename = config.getStringProp("clb.db.name", "duckling");
                String databaseip = config.getStringProp("clb.db.ip", "localhost:3306");
                String user = config.getStringProp("clb.db.user", "root");
                String password = config.getStringProp("clb.db.password", "root");
                initDatabase(sqlfilePath, databasename, databaseip, user, password);
                firstInit = false;
            }
        } else if (e instanceof ContextClosedEvent) {
            indexBuildExecutor.shutdown();
            indexReBuildExecutor.shutdown();
        }
    }

    private void initDatabase(String sqlfile, String databasename, String databaseip, String user, String password) {
        ifNotExistCreateDatabase(databasename, databaseip, user, password);
        initOriginalData(sqlfile, databasename, databaseip, user, password);
    }

    private void ifNotExistCreateDatabase(String databasename, String databaseip, String user, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName("org.gjt.mm.mysql.Driver").newInstance();
            String url = "jdbc:mysql://" + databaseip + "/?useUnicode=true&characterEncoding=utf8";
            conn = DriverManager.getConnection(url, user, password);
            pstmt = conn.prepareStatement("show databases");
            ResultSet rs = pstmt.executeQuery();
            boolean exist = false;
            while (rs.next()) {
                if (databasename.equals(rs.getString(1))) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                pstmt = conn.prepareStatement("CREATE database IF NOT EXISTS " + databasename + " CHARACTER SET utf8");
                pstmt.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void initOriginalData(String sqlfile, String databasename, String databaseip, String user, String password) {
        Connection conn = null;
        Statement stmt = null;
        SQLReader reader = null;
        try {
            String sql = null;
            String url = "jdbc:mysql://" + databaseip + "/" + databasename + "?useUnicode=true&characterEncoding=utf8";
            conn = DriverManager.getConnection(url, user, password);
            stmt = conn.createStatement();
            if (checkTable("clb_acl", conn)) {
                return;
            }
            reader = new SQLReader(new FileInputStream(sqlfile), "UTF-8");
            while ((sql = reader.next()) != null) {
                stmt.execute(sql);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null)
                reader.close();
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkTable(String tablename, Connection conn) {
        boolean found = false;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select count(*) from " + tablename);
            if (rs.next()) {
                found = true;
            }
        } catch (Exception se) {
            se.printStackTrace();
        }
        return found;
    }

}
