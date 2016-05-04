/*
 * Copyright (c) 2008-2016 Computer Network Information Center (CNIC), Chinese Academy of Sciences.
 * 
 * This file is part of Duckling project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *
 */
package cn.vlabs.clb.server.ui.frameservice.image;

public class SizePair {
    private int width;
    private int height;
    
    
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public SizePair(){}
    
    public SizePair(int width,int height){
        this.width = width;
        this.height = height;
    }

    public SizePair getResizePair(int resizePoint, String resizeType) {
        SizePair dst = new SizePair();
        if ("width".equals(resizeType)) {
            if (this.width < resizePoint) {
                dst.width = this.width;
                dst.height = this.height;
            } else {
                dst.width = resizePoint;
                dst.height = (resizePoint * this.height) / this.width;
            }
        } else {
            if (this.height < resizePoint) {
                dst.width = this.width;
                dst.height = this.height;
            } else {
                dst.height = resizePoint;
                dst.width = (resizePoint * this.width) / this.height;
            }
        }
        return dst;
    }
}