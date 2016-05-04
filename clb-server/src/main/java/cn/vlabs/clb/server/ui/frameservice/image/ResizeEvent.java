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

import cn.vlabs.clb.server.model.ImageItem;
import cn.vlabs.clb.server.ui.frameservice.SrcPair;

public class ResizeEvent {
    
    private boolean stopFlag;
    
    public boolean isStopFlag() {
        return stopFlag;
    }

    public void setStopFlag(boolean stopFlag) {
        this.stopFlag = stopFlag;
    }

    private SrcPair sp;

    public SrcPair getSp() {
        return sp;
    }

    public void setSp(SrcPair sp) {
        this.sp = sp;
    }

    private ImageItem item;
    
    public ImageItem getItem() {
        return item;
    }

    public void setItem(ImageItem item) {
        this.item = item;
    }
    
    public ResizeEvent(SrcPair sp, ImageItem item,boolean stopFlag){
        this.sp = sp;
        this.item = item;
        this.stopFlag = stopFlag;
    }
    

}
