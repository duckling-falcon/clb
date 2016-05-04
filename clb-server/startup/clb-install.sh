#!/bin/bash

filepath=$(cd "$(dirname "$0")"; pwd)
cd ..
BASE_DIR=$(pwd)
TOMCAT_DIR=$BASE_DIR/servers/apache-tomcat-7.0.26
P_TEMPLATE=$filepath/template
P_CONF=$filepath/conf
LOG_FILE=$filepath/clb_setup.log

source $filepath/setup.properties

cd $BASE_DIR
rm -rf servers
mkdir servers
mkdir servers/nginx-1.2.5
mkdir servers/pcre-8.31
mkdir servers/mongodb-2.2.2
rm -rf src
mkdir src

#step 0. download apache-tomcat.zip & download sharedlib
cd $BASE_DIR/servers
wget -a $LOG_FILE http://ftp.cerc.cnic.cn/pub/project/deployment/apache-tomcat-7.0.26.zip
rm -rf apache-tomcat-7.0.26
unzip apache-tomcat-7.0.26.zip >> $LOG_FILE
rm -rf apache-tomcat-7.0.26.zip 
cd apache-tomcat-7.0.26 
wget -a $LOG_FILE http://ci.cerc.cnic.cn:8080/job/clb/ws/sharedlib/*zip*/sharedlib.zip
unzip sharedlib.zip >> $LOG_FILE
rm -rf sharedlib.zip
cd conf
sed -i 's/\${catalina\.home}\/lib\/\*\.jar/\${catalina\.home}\/lib\/\*\.jar,\${catalina\.base}\/sharedlib,\${catalina\.base}\/sharedlib\/\*\.jar/' catalina.properties

echo "Step 1. download and install clb in tomcat"
cd ../webapps
wget -a $LOG_FILE http://ci.cerc.cnic.cn:8080/job/clb/ws/target/clb.war 
unzip clb.war -d clb >> $LOG_FILE
rm -rf clb.war

echo "Step 2. create database in mysql"
cd clb/WEB-INF/conf/
mysql -u$CLB_DB_USERNAME -p$CLB_DB_PASSWORD -e "drop database $CLB_DB_NAME;";
mysql -u$CLB_DB_USERNAME -p$CLB_DB_PASSWORD -e "create database $CLB_DB_NAME;";
mysql -h localhost -u $CLB_DB_USERNAME -D $CLB_DB_NAME --password=$CLB_DB_PASSWORD < clb.sql

echo "Step 3. download and build clb nginx module"
cd $BASE_DIR/src
wget -a $LOG_FILE http://ftp.cerc.cnic.cn/incoming/liji/apps/pcre-8.31.tar.gz 
wget -a $LOG_FILE http://ftp.cerc.cnic.cn/incoming/liji/apps/nginx-gridfs.zip
wget -a $LOG_FILE http://ftp.cerc.cnic.cn/incoming/liji/apps/nginx-1.2.5.tar.gz 
tar xzvf pcre-8.31.tar.gz >> $LOG_FILE
tar xzvf nginx-1.2.5.tar.gz >> $LOG_FILE
unzip nginx-gridfs.zip >> $LOG_FILE
cd nginx-1.2.5
./configure --with-pcre=$BASE_DIR/src/pcre-8.31 --prefix=$BASE_DIR/servers/nginx-1.2.5 --with-http_stub_status_module --add-module=$BASE_DIR/src/nginx-gridfs >> $LOG_FILE
cd objs
cat Makefile | sed 's/ -W / -Wno-error /1' > temp
cat temp > Makefile
rm -rf temp
cd ..
make >> $LOG_FILE
make install

echo "Step 4. download and install mongodb"
cd $BASE_DIR/src
wget -a $LOG_FILE http://ftp.cerc.cnic.cn/incoming/liji/apps/mongodb-linux-x86_64-2.2.2.tgz
tar zxvf mongodb-linux-x86_64-2.2.2.tgz >> $LOG_FILE
cd mongodb-linux-x86_64-2.2.2
cp -r * $BASE_DIR/servers/mongodb-2.2.2


echo "Step 5. update clb-tomcat properties"
cd  $filepath
./clb-change-config.sh
echo "Finish install of clb, please run clb-startup.sh to start servers."
