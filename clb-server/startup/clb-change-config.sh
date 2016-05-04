#!/bin/bash

filepath=$(cd "$(dirname "$0")"; pwd)
cd ..
BASE_DIR=$(pwd)
TOMCAT_DIR=$BASE_DIR/servers/apache-tomcat-7.0.26
NGX_DIR=$BASE_DIR/servers/nginx-1.2.5
P_TEMPLATE=$filepath/template
P_CONF=$filepath/conf

source $filepath/setup.properties

#step 3. update clb-tomcat properties
cd $filepath
if [ -d $P_CONF ]; then
	echo "$P_CONF dir has existed."
else
	mkdir $P_CONF
fi
rm -rf $P_TEMPLATE/clb.tpl.properties
echo "update clbconfig.properties using setup.properties"
awk -v fp="$filepath" -f $filepath/replace-props.awk $filepath/setup.properties $P_TEMPLATE/clb.tpl
mv $P_TEMPLATE/clb.tpl.properties $P_CONF/clbconfig.properties
cp $P_CONF/clbconfig.properties $TOMCAT_DIR/webapps/clb/WEB-INF/conf
#cat $TOMCAT_DIR/webapps/clb/WEB-INF/conf/clbconfig.properties

echo "update nginx.conf using setup.properties"
sed "s/\$CLB_MONGO_IP/$CLB_MONGO_IP/" $P_TEMPLATE/nginx.tpl > $P_CONF/nginx.conf.tmp
sed -i "s/\$CLB_MONGO_PORT/$CLB_MONGO_PORT/" $P_CONF/nginx.conf.tmp 
mv $P_CONF/nginx.conf.tmp $P_CONF/nginx.conf
cp $P_CONF/nginx.conf $NGX_DIR/conf
#cat $NGX_DIR/conf/nginx.conf
