-- clb 6.0.1 to clb 6.1.0;

-- dhome have one duplicated record, we need to remove it.;

delete from clb_app_auth where id=2;

delete from clb_app_auth where id>3;

update clb_doc set authId='escience' where authId='bi';
update clb_doc set authId='escience' where authId='programtest2';
update clb_doc set authId='escience' where authId='lby';
update clb_doc set authId='escience' where authId='vgelab';
update clb_doc set authId='escience' where authId='ronline';
update clb_doc set authId='escience' where authId='default';
update clb_doc set authId='escience' where authId='nel-mto';
update clb_doc set authId='escience' where authId='html5';


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
ALTER TABLE `clb_trivial_item` ADD INDEX `dv_index`(appid, docid, version);

ALTER TABLE `clb_app_auth` ADD UNIQUE `app_name_index`(name);

update clb_doc set clb_doc.docid=clb_doc.id;
ALTER TABLE `clb_doc` ADD INDEX `auth_index`(authId);
update clb_doc set clb_doc.appid = (select id from clb_app_auth where clb_doc.authId=clb_app_auth.name);
ALTER TABLE `clb_doc` ADD INDEX `doc_id`(docid);
update clb_doc_version set clb_doc_version.appid = ( select appid from clb_doc where clb_doc_version.docid=clb_doc.docid);

ALTER TABLE `clb_doc_version` ADD INDEX `tmp_index`(docid, version);
update clb_pdf_item a set a.appid = (select b.appid from clb_doc_version b where a.docid=b.docid and a.version=b.version);

-- all documents which have no validate appid should belong to app of escience;

update clb_doc set clb_doc.appid = 3 where clb_doc.appid=0;
update clb_doc_version set clb_doc_version.appid=3 where clb_doc_version.appid=0;

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