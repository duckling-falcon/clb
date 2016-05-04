
#!/bin/bash
PX_HOME=/usr/local/pdfexport

if [ -d ${PX_HOME} ]
	then
		echo "${PX_HOME} is already existed."
	else
		echo "Create ${PX_HOME}"
		mkdir -p ${PX_HOME}
fi

cd ${PX_HOME}
rm -rf *
wget ftp://ftp.cerc.cnic.cn/incoming/liji/apps/pdfexport.x64.tar.gz
tar zxvf pdfexport.x64.tar.gz
cd pdfexport
mv * ../
cd ../
rm -rf pdfexport pdfexport.x64.tar.gz

if [ -z "$LD_LIBRARY_PATH" ]
        then
        	etc_profile=/etc/profile
			echo "export LD_LIBRARY_PATH=${PX_HOME}/lib" >> $etc_profile
			echo "LD_LIBRARY_PATH now is initialized, you need to run [source /etc/profile]"
        else
        	echo "LD_LIBRARY_PATH is already existed"
fi

yum -y install compat-libstdc++-33.x86_64
