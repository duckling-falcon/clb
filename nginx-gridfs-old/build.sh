#!/bin/bash

# filepath=$(cd "$(dirname "$0")"; pwd)
# ./configure --with-debug --with-pcre=/usr/local/pcre-8.31 --prefix=$filepath/target --with-http_stub_status_module --add-module=$filepath/add_module_src/nginx-gridfs

###
### Please modify --prefix as you want
###   and check pcre
###
#./configure --prefix=/home/appleii/rt/nginx --with-http_stub_status_module --add-module=./add_module_src/nginx-gridfs --with-debug

./configure --prefix=/home/appleii/rt/nginx --with-http_stub_status_module --add-module=./add_module_src/nginx-gridfs
cd objs
cat Makefile | sed 's/ -W / -Wno-error /1' > temp
cat temp > Makefile
rm -rf temp
cd ..
#make
#make install
