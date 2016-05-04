#################################################
#This is the CLB's configuration file			#
#Config Item example:							#
#	#comment line								#
#	Category.SubCategory.configname=value		#
#################################################
#NOTE: items you may need to change
#1. server.name
#2. clb.db.name
#3. clb.db.ip
#4. clb.db.user
#5. clb.db.password
#6. clb.mongo.url
#7. clb.mongo.port
#8. duckling.umt.site
#9. duckling.vmt.site


#CLB server config
server.name=$CLB_URL

clb.db.name = $CLB_DB_NAME
clb.db.ip = $CLB_DB_IP
clb.db.user=$CLB_DB_USERNAME
clb.db.password=$CLB_DB_PASSWORD
clb.db.jdbcurl=$CLB_DB_FULL_URL
clb.db.driverClass=com.mysql.jdbc.Driver
clb.db.initialPoolSize=10
clb.db.minPoolSize=5
clb.db.maxPoolSize=100
clb.db.acquireIncrement=10
clb.db.acquireRetryAttempts=5
clb.db.acquireRetryDelay=20
clb.db.numHelperThreads=5
clb.db.idleConnectionTestPeriod=1800
clb.db.automaticTestTable=clb_test

#MongoDB url 
clb.mongo.url=$CLB_MONGO_IP
clb.mongo.port=27017
clb.mongo.dbname=docs

#dconvert
clb.dconvert.serverURL=$CLB_DCONVERT_URL

#nginx
clb.nginx.domain=$CLB_NGINX_DOMAIN
clb.nginx.context.doc=doc
clb.nginx.context.pdf=pdf
clb.nginx.context.image=image
clb.nginx.context.trivial=trivial

#document index directory
directory.temp=${clb.appRoot}/var/tmp

Search.indexDir=${clb.appRoot}/var/docindex
Search.index.temp=${clb.appRoot}/var/doctemp
search.index.fuzzy=0.8
summarization.abstract.maxSentences=10
Summmary.length=200

System.encoding=GBK
signon.enable=true
duckling.clb.localName=clb

