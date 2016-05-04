/*
 Navicat Premium Data Transfer

 Source Server         : mac
 Source Server Type    : MySQL
 Source Server Version : 50515
 Source Host           : localhost
 Source Database       : clb2

 Target Server Type    : MySQL
 Target Server Version : 50515
 File Encoding         : utf-8

 Date: 01/15/2013 09:27:29 AM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `clb_app_auth`
-- ----------------------------
DROP TABLE IF EXISTS `clb_app_auth`;
CREATE TABLE `clb_app_auth` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `password` varbinary(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `clb_doc`
-- ----------------------------
DROP TABLE IF EXISTS `clb_doc`;
CREATE TABLE `clb_doc` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `authId` varchar(255) NOT NULL,
  `isPub` int(11) NOT NULL,
  `lastVersion` int(11) NOT NULL,
  `lastUpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `createTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `clb_doc_version`
-- ----------------------------
DROP TABLE IF EXISTS `clb_doc_version`;
CREATE TABLE `clb_doc_version` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `docid` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `filename` varchar(255) DEFAULT NULL,
  `fileExtension` varchar(20) DEFAULT NULL,
  `size` bigint(20) DEFAULT NULL,
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `storageKey` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `storageIndex` (`storageKey`),
  KEY `dv_index` (`docid`,`version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `clb_image_item`
-- ----------------------------
DROP TABLE IF EXISTS `clb_image_item`;
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
  PRIMARY KEY (`id`),
  KEY `storageIndex` (`storageKey`),
  KEY `dv_index` (`docid`,`version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `clb_pdf_item`
-- ----------------------------
DROP TABLE IF EXISTS `clb_pdf_item`;
CREATE TABLE `clb_pdf_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `docid` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `size` bigint(20) NOT NULL,
  `status` varchar(255) NOT NULL,
  `storageKey` varchar(255) NOT NULL,
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `filename` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `storageIndex` (`storageKey`),
  KEY `dv_index` (`docid`,`version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `clb_test`
-- ----------------------------
DROP TABLE IF EXISTS `clb_test`;
CREATE TABLE `clb_test` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `clb_trivial_item`
-- ----------------------------
DROP TABLE IF EXISTS `clb_trivial_item`;
CREATE TABLE `clb_trivial_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `docid` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `spaceName` varchar(127) NOT NULL,
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `storageIndex` (`spaceName`),
  KEY `dv_index` (`docid`,`version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Procedure structure for `calChildCount`
-- ----------------------------
DROP PROCEDURE IF EXISTS `calChildCount`;
delimiter ;;
CREATE PROCEDURE `calChildCount`()
begin
declare _childcount int;
declare _fid int;
declare stopFlag int;

declare _result_cur cursor for select pid, count(cid) from clb_frel group by pid;
DECLARE CONTINUE HANDLER FOR NOT FOUND set stopFlag=1;
open _result_cur;
repeat
fetch _result_cur into _fid, _childcount;
update clb_folder set childcount=_childcount where id=_fid;
UNTIL stopFlag = 1
end repeat;
close _result_cur;
end
 ;;
delimiter ;

-- ----------------------------
--  Procedure structure for `proc_create_doc_version`
-- ----------------------------
DROP PROCEDURE IF EXISTS `proc_create_doc_version`;
delimiter ;;
CREATE PROCEDURE `proc_create_doc_version`(IN arg_appid int,IN arg_docid int,OUT result int,OUT result2 int)
BEGIN   
DECLARE doc_version int default -1;
START TRANSACTION; 
select max(version) into doc_version from clb_doc_version where docid=arg_docid and appid=arg_appid;
if doc_version is null or doc_version<0 then
	set doc_version=1;
else
	set doc_version=doc_version+1;	
end if;
insert into clb_doc_version(appid,docid,version) values(arg_appid,arg_docid,doc_version);
select id,version into result,result2 from clb_doc_version where appid=arg_appid and docid=arg_docid and version=doc_version;
commit;
END
 ;;
delimiter ;

-- ----------------------------
--  Procedure structure for `refreshcount`
-- ----------------------------
DROP PROCEDURE IF EXISTS `refreshcount`;
delimiter ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `refreshcount`(in folderid int)
begin
declare v_childcount int;
select count(cid) into v_childcount from clb_frel where pid = folderid;
update clb_folder set childcount=v_childcount where id=folderid;
end
 ;;
delimiter ;

ALTER TABLE `clb_doc` ADD COLUMN `appid` int NOT NULL AFTER `createTime`;
ALTER TABLE `clb_doc` ADD COLUMN `docid` int NOT NULL AFTER `appid`;
ALTER TABLE `clb_doc` ADD INDEX `app_index`(appid);

ALTER TABLE `clb_doc_version` ADD COLUMN `appid` int NOT NULL AFTER `storageKey`;
ALTER TABLE `clb_doc_version` DROP INDEX `dv_index`, ADD INDEX `dv_index`(appid, docid, version);

ALTER TABLE `clb_image_item` ADD COLUMN `appid` int NOT NULL AFTER `fileExtension`;
ALTER TABLE `clb_image_item` DROP INDEX `dv_index`, ADD INDEX `dv_index`(appid, docid, version);

ALTER TABLE `clb_pdf_item` ADD COLUMN `appid` int NOT NULL AFTER `filename`;
ALTER TABLE `clb_pdf_item` DROP INDEX `dv_index`, ADD INDEX `dv_index`(appid, docid, version);

ALTER TABLE `clb_trivial_item` ADD COLUMN `appid` int NOT NULL AFTER `status`;
ALTER TABLE `clb_trivial_item` DROP INDEX `dv_index`, ADD INDEX `dv_index`(appid, docid, version);

ALTER TABLE `clb_app_auth` ADD UNIQUE `app_name_index`(name);

update clb_doc set clb_doc.docid=clb_doc.id;
ALTER TABLE `clb_doc` ADD INDEX `auth_index`(authId);
update clb_doc set clb_doc.appid = (select id from clb_app_auth where clb_doc.authId=clb_app_auth.name);
ALTER TABLE `clb_doc` ADD INDEX `doc_id`(docid);
update clb_doc_version set clb_doc_version.appid = ( select appid from clb_doc where clb_doc_version.docid=clb_doc.docid);

ALTER TABLE `clb_doc_version` ADD INDEX `tmp_index`(docid, version);
update clb_pdf_item a set a.appid = (select b.appid from clb_doc_version b where a.docid=b.docid and a.version=b.version);

ALTER TABLE `clb_doc` DROP INDEX `app_docid_index`, ADD UNIQUE `app_docid_index`(appid, docid);


