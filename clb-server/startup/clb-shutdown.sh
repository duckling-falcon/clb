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


function tooltip(){

while true; do
	stty -icanon min 0 time 100
	echo -n "Do you want to kill $1 service (Yes or No)?"
	read Arg
	case $Arg in
		Y|y|YES|yes)
			kill_service $1
			break;;
		N|n|NO|no)
			echo "$1 still alive."
			break;;
		"")  #Autocontinue
			echo "Skipped."
			break;;
	esac
done
 
}
tooltip tomcat
tooltip nginx
tooltip mongod 