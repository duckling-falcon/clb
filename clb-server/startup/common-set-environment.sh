#!/bin/bash

#install environment
filepath=$(cd "$(dirname "$0")"; pwd)

cd ..
BASE_DIR=$(pwd)
mkdir src
mkdir servers

yum -y install gcc
yum -y install gcc-c++
yum -y install man
yum -y install mysql-server
yum -y install zlib-devel
yum -y install wget
#yum -y install bind*


#install java 
echo "install java 7"
cd $BASE_DIR/src
rm -rf jdk-7u17-linux-x64.tar.gz
wget http://ftp.cerc.cnic.cn/incoming/liji/apps/jdk-7u17-linux-x64.tar.gz
tar xzvf jdk-7u17-linux-x64.tar.gz #> $filepath/data/install.log
if [ ! -d /usr/lib/jvm ]
	then
		echo "create a new dir /usr/lib/jvm"
		mkdir /usr/lib/jvm	
fi
yes | cp -rf jdk1.7.0_17/ /usr/lib/jvm

#edit java_home
if [ -z "$JAVA_HOME" ]
        then
        	java_profile=/etc/profile
			echo 'export JAVA_HOME=/usr/lib/jvm/jdk1.7.0_17' >> $java_profile
			echo 'export JRE_HOME=${JAVA_HOME}/jre' >> $java_profile
			echo 'export CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib' >> $java_profile
			echo 'export PATH=${JAVA_HOME}/bin:$PATH' >> $java_profile
			#source /etc/profile
			echo "JAVA_HOME now is initialized, you need to run [source /etc/profile]"
        else
        	echo "JAVA_HOME is already existed"
fi

echo "java installation is finish."

#edit /etc/hosts
current_host=`hostname`
grep -q $current_host /etc/hosts
if [ $? -eq 0 ]; then
	echo "hostname already in /etc/hosts"
else
	echo "add $current_host into /etc/hosts"
	echo "127.0.0.1 $current_host" >> /etc/hosts
fi


