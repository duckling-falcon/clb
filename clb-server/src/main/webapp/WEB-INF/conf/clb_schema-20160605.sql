-- MySQL dump 10.13  Distrib 5.1.69, for redhat-linux-gnu (x86_64)
--
-- Host: localhost    Database: clbdb
-- ------------------------------------------------------
-- Server version	5.1.69-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `clb_app_auth`
--

DROP TABLE IF EXISTS `clb_app_auth`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clb_app_auth` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `password` varbinary(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `app_name_index` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `clb_chunk_status`
--

DROP TABLE IF EXISTS `clb_chunk_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clb_chunk_status` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `appid` int(11) NOT NULL,
  `docid` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `num_of_chunk` int(11) NOT NULL,
  `chunk_size` bigint(20) NOT NULL,
  `current_chunk_index` int(11) NOT NULL,
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` varchar(20) CHARACTER SET latin1 NOT NULL,
  `begin_upload_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `chunk_map` text,
  `md5` varchar(255) NOT NULL,
  `file_size` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `pk_chunk_status` (`appid`,`docid`,`version`)
) ENGINE=InnoDB AUTO_INCREMENT=399 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `clb_doc`
--

DROP TABLE IF EXISTS `clb_doc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clb_doc` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `authId` varchar(255) NOT NULL,
  `isPub` int(11) NOT NULL,
  `lastVersion` int(11) NOT NULL,
  `lastUpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `createTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `appid` int(11) NOT NULL,
  `docid` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `app_docid_index` (`appid`,`docid`),
  KEY `app_index` (`appid`),
  KEY `auth_index` (`authId`),
  KEY `doc_id` (`docid`)
) ENGINE=InnoDB AUTO_INCREMENT=3439032 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `clb_doc_ref`
--

DROP TABLE IF EXISTS `clb_doc_ref`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clb_doc_ref` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `md5` varchar(64) CHARACTER SET latin1 NOT NULL,
  `size` bigint(20) NOT NULL,
  `ref` int(11) NOT NULL,
  `storage_key` varchar(32) CHARACTER SET latin1 NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `md5_size` (`md5`,`size`),
  UNIQUE KEY `storage_key` (`storage_key`)
) ENGINE=InnoDB AUTO_INCREMENT=385 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `clb_doc_version`
--

DROP TABLE IF EXISTS `clb_doc_version`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clb_doc_version` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `docid` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `filename` varchar(255) DEFAULT NULL,
  `fileExtension` varchar(20) DEFAULT NULL,
  `size` bigint(20) DEFAULT NULL,
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `storageKey` varchar(50) DEFAULT NULL,
  `appid` int(11) NOT NULL,
  `uploadStatus` varchar(255) DEFAULT 'waiting',
  `completeTime` timestamp NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`),
  KEY `storageIndex` (`storageKey`),
  KEY `dv_index` (`appid`,`docid`,`version`),
  KEY `tmp_index` (`docid`,`version`)
) ENGINE=InnoDB AUTO_INCREMENT=3441908 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `clb_image_item`
--

DROP TABLE IF EXISTS `clb_image_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clb_image_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `docid` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `resizeType` varchar(255) NOT NULL,
  `storageKey` varchar(50) NOT NULL,
  `size` bigint(20) NOT NULL,
  `status` varchar(255) NOT NULL,
  `width` int(11) NOT NULL,
  `height` int(11) NOT NULL,
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `filename` varchar(255) NOT NULL,
  `fileExtension` varchar(20) NOT NULL,
  `appid` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `storageIndex` (`storageKey`),
  KEY `dv_index` (`appid`,`docid`,`version`)
) ENGINE=InnoDB AUTO_INCREMENT=678131 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `clb_pdf_item`
--

DROP TABLE IF EXISTS `clb_pdf_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clb_pdf_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `docid` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `size` bigint(20) NOT NULL,
  `status` varchar(255) NOT NULL,
  `storageKey` varchar(255) NOT NULL,
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `filename` varchar(255) NOT NULL,
  `appid` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `storageIndex` (`storageKey`),
  KEY `dv_index` (`appid`,`docid`,`version`)
) ENGINE=InnoDB AUTO_INCREMENT=1589117 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `clb_test`
--

DROP TABLE IF EXISTS `clb_test`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clb_test` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `clb_trivial_item`
--

DROP TABLE IF EXISTS `clb_trivial_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clb_trivial_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `docid` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `spaceName` varchar(127) NOT NULL,
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` varchar(20) NOT NULL,
  `appid` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `spaceIndex` (`spaceName`),
  KEY `dv_index` (`appid`,`docid`,`version`)
) ENGINE=InnoDB AUTO_INCREMENT=2052 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `old_clb_acl`
--

DROP TABLE IF EXISTS `old_clb_acl`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `old_clb_acl` (
  `ACLID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `entityName` varchar(765) NOT NULL,
  `voName` varchar(765) DEFAULT NULL,
  `rootGroupName` varchar(765) DEFAULT NULL,
  `entityType` char(3) NOT NULL,
  `delegator` varchar(765) NOT NULL,
  `resourceName` varchar(765) NOT NULL,
  `resourceType` varchar(192) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `viewop` char(3) DEFAULT NULL,
  `deleteop` char(3) DEFAULT NULL,
  `updateop` char(3) DEFAULT NULL,
  `grantop` char(3) DEFAULT NULL,
  PRIMARY KEY (`ACLID`)
) ENGINE=MyISAM AUTO_INCREMENT=1023 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `old_clb_app_auth`
--

DROP TABLE IF EXISTS `old_clb_app_auth`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `old_clb_app_auth` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `password` varbinary(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=86 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `old_clb_document`
--

DROP TABLE IF EXISTS `old_clb_document`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `old_clb_document` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `deleted` char(1) DEFAULT 'F',
  `title` varchar(128) NOT NULL DEFAULT '',
  `keywords` text,
  `createBy` varchar(64) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `tags` varchar(255) DEFAULT '',
  `isPub` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=32121 DEFAULT CHARSET=utf8 ROW_FORMAT=FIXED;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `old_clb_docver`
--

DROP TABLE IF EXISTS `old_clb_docver`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `old_clb_docver` (
  `docid` int(11) NOT NULL DEFAULT '0',
  `verno` int(11) NOT NULL DEFAULT '0',
  `updateBy` varchar(64) DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  `comment` text,
  `summary` text,
  `filename` varchar(240) NOT NULL DEFAULT '',
  `size` int(11) DEFAULT '0',
  `hashcode` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`docid`,`verno`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 ROW_FORMAT=FIXED;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `old_clb_faver_tag`
--

DROP TABLE IF EXISTS `old_clb_faver_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `old_clb_faver_tag` (
  `tagid` int(11) NOT NULL DEFAULT '0',
  `user` varchar(64) NOT NULL DEFAULT '',
  PRIMARY KEY (`user`,`tagid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `old_clb_folder`
--

DROP TABLE IF EXISTS `old_clb_folder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `old_clb_folder` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `type` char(1) NOT NULL DEFAULT 'f',
  `docid` int(11) DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `childcount` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=REDUNDANT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `old_clb_frel`
--

DROP TABLE IF EXISTS `old_clb_frel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `old_clb_frel` (
  `cid` int(11) NOT NULL DEFAULT '0',
  `pid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`cid`,`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `old_clb_lockinfo`
--

DROP TABLE IF EXISTS `old_clb_lockinfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `old_clb_lockinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `docid` int(11) DEFAULT NULL,
  `lockBy` varchar(64) DEFAULT NULL,
  `lockDate` varchar(32) DEFAULT NULL,
  `locktype` tinyint(4) DEFAULT NULL,
  `power` tinyint(4) DEFAULT '0',
  `unlockBy` varchar(64) DEFAULT NULL,
  `unlockDate` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `old_clb_taginfo`
--

DROP TABLE IF EXISTS `old_clb_taginfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `old_clb_taginfo` (
  `docid` int(11) NOT NULL DEFAULT '0',
  `tagid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`docid`,`tagid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `old_clb_tags`
--

DROP TABLE IF EXISTS `old_clb_tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `old_clb_tags` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tagname` varchar(255) NOT NULL DEFAULT '',
  `creator` varchar(64) DEFAULT NULL,
  `scope` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `old_clb_test`
--

DROP TABLE IF EXISTS `old_clb_test`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `old_clb_test` (
  `a` char(1) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-06-05 17:52:28
