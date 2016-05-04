<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
pageContext.setAttribute("contextPath", request.getContextPath());
String basePath = getServletContext().getRealPath("/");
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<html>
<head>
<title>CLB Index Page</title>
</head>
<body>
	<h2>When you see this page, it means clb worked.</h2>
	<p>CLB Server Version:6.3.4</p>
	<p>CLB Client Version:6.3.4</p>
	<div class="source" style="font-family: 'Courier New', Consolas, 'Lucida Console'; color: rgb(0, 0, 0); background-color: rgb(249, 247, 237); "><span style="color: rgb(0, 112, 0); ">&lt;dependency&gt;</span><br> &nbsp; &nbsp;<span style="color: rgb(0, 112, 0); ">&lt;groupId&gt;</span>net.duckling<span style="color: rgb(0, 112, 0); ">&lt;/groupId&gt;</span><br> &nbsp; &nbsp;<span style="color: rgb(0, 112, 0); ">&lt;artifactId&gt;</span>clb-api<span style="color: rgb(0, 112, 0); ">&lt;/artifactId&gt;</span><br> &nbsp; &nbsp;<span style="color: rgb(0, 112, 0); ">&lt;version&gt;</span>6.3.0-SNAPSHOT<span style="color: rgb(0, 112, 0); ">&lt;/version&gt;</span><br><span style="color: rgb(0, 112, 0); ">&lt;/dependency&gt;</span><br></div>
	<h2>CHANGE LOGS</h2>
	
	<h3>clb(v6.3.4) at 2016-03-04 by liyanzhao</h3>
	<ul>
	   <li> 新增4个API接口:
       public UpdateInfo createDocument(CreateDocInfo ci, IResource si); //创建文档
       public DocMetaInfo getDocMeta(int docid, String version) throws InvalidArgument, ResourceNotFound, AccessForbidden; //获取文档信息
       public DocMetaInfo getDocMeta(int docid) throws ResourceNotFound, AccessForbidden; //获取文档信息
       public void createClbRef(ClbMeta meta); //添加clb上传记录
		</li>   
	</ul>
	
	<h3>clb(v6.3.3) at 2015-03-31 by liyanzhao</h3>
	<ul>
	   <li>MetaInfo 中添加Md5字段
		</li>   
	</ul>
	
	<h3>clb(v6.3.2) at 2015-03-31 by liyanzhao</h3>
	<ul>
	   <li>DocumentService添加四个用于分块上传的接口
			<code><pre>
    public Integer queryClbidForMD5(String md5,long size);
    
    public void createClbRef(int clbid,String fileName,String md5,long size);	
              </pre>
			</code>
		</li>   
	</ul>
	<h3>clb(v6.3.1) at 2015-02-11 by clive</h3>
	<ul>
		<li>增加clb只读模式，clbconfig.properties新增配置项
			<code><pre>
clb.io.mode=readonly #表示只读模式
clb.io.mode=writeable #表示可读写模式</pre></code>
		</li>
		<li>当设置为只读模式时，所有涉及写入的操作均会抛出InvalidOperationException</li>
		</ul>
	<h3>clb(v6.3.0) at 2014-07-28 by clive</h3>
	<ul>
		<li>新增分块上传服务</li>
		<li>为分块上传内置了md5去重处理</li>
		<li>DocumentService添加四个用于分块上传的接口
			<code><pre>
public ChunkResponse prepareChunkUpload(String filename, String md5, long size, long chunkSize);

public ChunkResponse executeChunkUpload(int docid, int chunkedIndex, byte[] buf, int numOfBytes);

public ChunkResponse finishChunkUpload(int docid);

public Set<Integer> queryEmptyChunks(int docid);		
</pre>
			</code>
		</li>
		<li>数据库发生变化：
			<code><pre>
ALTER TABLE `clb_doc_version` ADD COLUMN `uploadStatus` varchar(255) default 'waiting' AFTER `appid`;

ALTER TABLE `clb_doc_version` ADD COLUMN `completeTime` timestamp NULL default '0000-00-00 00:00:00' AFTER `uploadStatus`;

update clb_doc_version set uploadStatus='complete';


CREATE TABLE `clb_chunk_status` (
  `id` int(11) NOT NULL auto_increment,
  `appid` int(11) NOT NULL,
  `docid` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `num_of_chunk` int(11) NOT NULL,
  `chunk_size` bigint(20) NOT NULL,
  `current_chunk_index` int(11) NOT NULL,
  `last_update_time` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `status` varchar(20) character set latin1 NOT NULL,
  `begin_upload_time` timestamp NOT NULL default '0000-00-00 00:00:00',
  `chunk_map` text,
  `md5` varchar(255) NOT NULL,
  `file_size` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `pk_chunk_status` (`appid`,`docid`,`version`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `clb_doc_ref` (
  `id` int(11) NOT NULL auto_increment,
  `md5` varchar(64) character set latin1 NOT NULL,
  `size` bigint(20) NOT NULL,
  `ref` int(11) NOT NULL,
  `storage_key` varchar(32) character set latin1 NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `md5_size` (`md5`,`size`),
  UNIQUE KEY `storage_key` (`storage_key`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;	
			</pre></code>
		</li>
	</ul>
	<h3>clb(v6.2.1) at 2014-03-18 by clive</h3>
	<ul>
		<li>新增Office Web App Server（owa）以提供文档预览服务</li>
		<li>新增owa数据源访问接口：
			${clbdomain}/wopi/p?accessToken=${accessToken}
		</li>
		<li>其他应用获取预览界面时需要首先从clb获得一个5分钟内有效的token，产生此token的REST接口为：
			${clbdomain}/wopi/fetch/accessToken?appname=xx&password=xxx&docid=1&version=2
		</li>
		<li>增加两个配置项:
			clb.local.domain=159.226.10.89:8081,
			clb.wopi.domain=159.226.11.139
		<li>
	</ul>
	<h3>clb(v6.2.0) at 2014-01-26 by clive</h3>
	<ul>
		<li>使用oracle提供的开源pdfexport工具完成文档转换功能，需要在clb的本地安装pdfexport，系统要求为centos 6.4</li>		
		<li>重写dconvert功能代码，将dconvert融入clb中</li>
		<li>使用oracle提供的开源pdfexport工具完成文档转换功能，需要在clb的本地安装pdfexport，系统要求为centos 6.4</li>
		<li>增加配置项：clb.pdfconvert.worker.num = 3, 表示文档转换后台工作线程的个数</li>
		<li>只转换文档的前100页,小于100页转换成功时返回CONVERT_SUCCESS状态码;</li>
		<li>对于100页以上的文件返回CONVERT_SUCCESS_AND_HAS_MORE状态码;</li>
		<li>识别更为具体的错误码，如密码保护，源文件损坏等情况;</li>
	</ul>
	<h3>clb(v6.1.7) at 2013-12-24 by clive</h3>
	<ul>
		<li>修复由于同步引起的后台线程等待的性能问题</li>
		<li>修复mongodb退出时的内存泄露</li>
	</ul>
	<h3>clb(v6.1.6) at 2013-12-19 by clive</h3>
	<ul>
		<li>替换图片压缩引擎: 使用GraphicsMagicks来替换已有的java标准图片压缩方式;</li>
		<li>图片压缩速度提高3-6倍，详细对比请看图;</li>
		<li>支持更多种的图片转换格式;</li>
		<li>支持gif动态图片的压缩;</li>
		<li>图片压缩服务增加任务去重机制;</li>
		<li>初次上传图片时如果是图片将会缓存一份到本地临时文件中，利用本地缓存文件加速压缩图片的速度;</li>
		<li>startup文件夹中增加graphicsmagicks安装脚本graphicsmagicks-install.sh;</li>
	</ul>
	<img src="${contextPath}/assets/resize-performance.png">
	<h3>clb(v6.1.5) at 2013-12-12 by clive</h3>
	<ul>
		<li>新增一个新数据类ImageMeta，表示压缩图片的元信息，可查看状态、大小、宽高等;</li>
		<li>修改ResizeImage类，增加一个新的属性List<ImageMeta> metaList;</li>
		<li>新增图片压缩状态查询接口,IResizeImageService.queryResizeStatus，返回的是一个HashMap<String,ImageMeta>对象；</li>
		<li>当图片解析错误时，直接将压缩状态置为failed;</li>
	</ul>
	<h3>clb(v6.1.4) at 2013-12-03 by clive</h3>
	<ul>
		<li>增加索引 ALTER TABLE `clb_doc` DROP INDEX `app_docid_index`, ADD UNIQUE `app_docid_index`(appid, docid);</li>
		<li>使用falcon-0.3.3版本提供的IDGeneratorService服务，增加两个配置项以指定redis的ip和端口</li>
		<li>扩展后台压缩图片线程的数目，提高压缩图片转化速度</li>
		<li>增加配置项clb.resize.worker.num</li>
		<li>压缩服务新增一个方法IResizeImageService.getContent(ImageQuery query, IFileSaver fs);</li>
		<li>获取压缩图片时可指定ImageQuery对象中的needOriginal属性来决定是否获取原始图片</li>
	</ul>
	<h3>clb(v6.1.3) at 2013-11-27 by clive</h3>
	<ul>
		<li>增加更详细的日志记录</li>
		<li>使用frmwork.0.99来提高文件上传速度(不再需要写临时文件,而是直接以流的方式传输文件数据)</li>
	</ul>
	<h3>clb(v6.1.2p1) at 2013-08-23 by clive</h3>
	<ul>
		<li>修复只能转换大图的Bug</li>
		<li>修复当图像转换参数为0时转换失败的Bug</li>
	</ul>
	<h3>clb(v6.1.2) at 2013-08-15 by clive</h3>
	<ul>
		<li>在IResizeImageService新增一个接口
			<code><pre>
void getContent(int docid, String version, String type, ResizeParam resizeParam, IFileSaver fs);
</pre></code>
		</li>
		<li>修复因转换图片失败而导致的服务中断</li>
		<li>对已有图片进行再次压缩时将删除上次压缩过的图片</li>
		<li>当压缩尺寸比原图尺寸还大时将直接返回使用原图尺寸</li>
	</ul>
</body>
</html>