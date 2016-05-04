DROP PROCEDURE  IF  EXISTS proc_create_doc_version;
DELIMITER //  
CREATE PROCEDURE proc_create_doc_version(IN arg_docid int,OUT result int,OUT result2 int)  
BEGIN   
DECLARE doc_version int default -1;
START TRANSACTION; 
select max(version) into doc_version from clb_doc_version where docid=arg_docid;
if doc_version is null or doc_version<0 then
	set doc_version=1;
else
	set doc_version=doc_version+1;	
end if;
insert into clb_doc_version(docid,version) values(arg_docid,doc_version);
select id,version into result,result2 from clb_doc_version where docid=arg_docid and version=doc_version;
commit;
END