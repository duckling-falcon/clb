#!/bin/bash


filepath=$(cd "$(dirname "$0")"; pwd)
cd ..
BASE_DIR=$(pwd)
TOMCAT_DIR=$BASE_DIR/servers/apache-tomcat-7.0.26
NGX_DIR=$BASE_DIR/servers/nginx-1.2.5
MONGO_DIR=$BASE_DIR/servers/mongodb-2.2.2


function check_state(){
	pid_list=`ps -ef|grep $1|grep -v "grep"|awk '{print $2}'`
	echo "$1 service started: $pid_list"
}

function start_mongod(){
	mkdir $BASE_DIR/data
	cd $MONGO_DIR/bin
	chmod +x *
	./mongod --dbpath $BASE_DIR/data --logpath $BASE_DIR/data/mongod.log &
	check_state mongod
	echo "Please waiting at least 30 seconds to finish mongodb initialized."
	sleep 35
}

function start_tomcat(){
	cd $TOMCAT_DIR/bin
	chmod +x *.sh
	./startup.sh #2>/dev/null 1>&2 &
	sleep 3
	check_state tomcat
}

function start_nginx(){
	cd $NGX_DIR/sbin
	chmod +x nginx
	$NGX_DIR/sbin/nginx
	sleep 3
	check_state nginx
}

function tooltip(){

while true; do
        stty -icanon min 0 time 100
        echo -n "Do you want to start $1 service (Yes or No)?"
        read Arg
        case $Arg in
                Y|y|YES|yes)
			case $1 in
				tomcat)
					start_tomcat
					break;;
				mongod)
					start_mongod
					break;;
				nginx)
					start_nginx
					break;;
			esac	
                        break;;
                N|n|NO|no)
                        echo "$1 service starting skipped."
                        break;;
                "")  #Autocontinue
			echo "$1 service starting skipped."
                        break;;
        esac
done

}

tooltip mongod
tooltip tomcat
tooltip nginx
