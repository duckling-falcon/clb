#!/bin/bash

yum install -y freetype gd-devel libgomp ibjpeg libjpeg-devel libpng libpng-devel libtiff libtiff-devel

wget http://sourceforge.net/projects/graphicsmagick/files/latest/download?source=files 

cd GraphicsMagick
tar zvxf GraphicsMagick-1.3.19.tar.gz
cd GraphicsMagick-1.3.19
./configure --enable-shared=yes --disable-openmp 
make
make install 

ln -s /usr/local/bin/gm /usr/bin/gm