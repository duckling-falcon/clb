#!/bin/bash

function kill_service(){
        pid_list=`ps -ef|grep $1|grep -v "grep"|awk '{print $2}'`
        if [ $pid_list = "" ]; then
                echo "No $1 process alive!"
        else
                echo "Find $1 process: $pid_list"
                kill -9 $pid_list
                echo "$1 process have been killed."
        fi
}

filepath=$(cd "$(dirname "$0")"; pwd)
cd ..
BASE_DIR=$(pwd)
BIN_HOME=$BASE_DIR/servers/apache-tomcat-7.0.26/bin
CLB_HOME=$BASE_DIR/servers/apache-tomcat-7.0.26/webapps

kill_service tomcat

cd $CLB_HOME/clb/WEB-INF/conf
cp clbconfig.properties /tmp
cd $CLB_HOME
rm -rf clb.war
rm -rf clb
wget http://ci.cerc.cnic.cn:8080/job/clb/ws/target/clb.war
unzip clb.war -d clb
yes | mv /tmp/clbconfig.properties $CLB_HOME/clb/WEB-INF/conf
cd $BIN_HOME
./startup.sh