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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.IdentifyCmd;
import org.im4java.process.ArrayListOutputConsumer;
import org.im4java.process.Pipe;

public class ImageUtil {

    private static final String SUCCESS_PREFIX = "state";
    private static final Log LOG = LogFactory.getLog(ImageUtil.class);
    
    public SizePair resize(String srcPath, String dstPath, int rzp, String rzpType){
        List<String> lines = validateImage(srcPath);
        List<SizePair> splist = parseValidateOutput(lines);
        if(splist.size() == 0){//Not a image
            return null;
        } else {
            SizePair dstPair = splist.get(0).getResizePair(rzp, rzpType); 
            boolean flag = processScaleCommand(srcPath, dstPath, dstPair, splist.size() > 1);
            if (!flag) {
                return null;
            }
            return dstPair;
        }
    }
    
    public SizePair resize(InputStream ins, OutputStream ous, int rzp, String rzpType){
        List<String> lines = validateImage(ins);
        List<SizePair> splist = parseValidateOutput(lines);
        String type = parseImageType(lines);
        if(splist.size() == 0){//Not a image
            return null;
        } else {
            SizePair dstPair = splist.get(0).getResizePair(rzp, rzpType);
            boolean flag = processScaleCommand(ins, ous, dstPair,type, splist.size() > 1);
            if (!flag) {
                return null;
            }
            return dstPair;
        }
    }
    
    private String parseImageType(List<String> lines) {
        String l = lines.get(0);
        if (l != null && l.startsWith(SUCCESS_PREFIX)) {
            String[] array = l.split(" ");
            return array[1].toLowerCase();
        }
        return null;
    }

    private List<String> validateImage(InputStream ins) {
        List<String> lines = new ArrayList<String>();
        try {
            ins.mark(0);
            IMOperation op = new IMOperation();
            op.format(SUCCESS_PREFIX + " %m %w %h %n %p\n");
            IdentifyCmd idcmd = new IdentifyCmd(true);
            Pipe pipeIn  = new Pipe(ins, null);
            ArrayListOutputConsumer output = new ArrayListOutputConsumer();
            idcmd.setInputProvider(pipeIn);
            idcmd.setOutputConsumer(output);
            op.addImage("-");
            idcmd.run(op);
            lines = output.getOutput();
        } catch (IOException | InterruptedException | IM4JavaException e) {
            LOG.error(e.getMessage(), e);
        } catch(RuntimeException e){
            LOG.error(e.getMessage(), e);
        }
        return lines;
    }
    
    //gm identify -format "state %m %f %w %h %n %p\n" old.jpg
    private List<String> validateImage(String srcPath) {
        List<String> lines = new ArrayList<String>();
        try {
            IMOperation op = new IMOperation();
            op.format(SUCCESS_PREFIX + " %m %w %h %n %p\n");
            IdentifyCmd idcmd = new IdentifyCmd(true);
            ArrayListOutputConsumer output = new ArrayListOutputConsumer();
            idcmd.setOutputConsumer(output);
            op.addImage(srcPath);
            idcmd.run(op);
            lines = output.getOutput();
        } catch (IOException | InterruptedException | IM4JavaException e) {
            LOG.error(e.getMessage(), e);
        } catch(RuntimeException e){
            LOG.error(e.getMessage(), e);
        }
        return lines;
    }
    
    private List<SizePair> parseValidateOutput(List<String> lines){
        List<SizePair> results = new ArrayList<SizePair>();
        for(String l:lines){
            if(l.startsWith(SUCCESS_PREFIX)){
                String[] array = l.split(" ");
                int width = Integer.parseInt(array[2]);
                int height = Integer.parseInt(array[3]);
                SizePair sp = new SizePair(width,height);
                results.add(sp);
            }
        }
        return results;
    }
    
    
    private boolean processScaleCommand(InputStream ins, OutputStream ous, SizePair dstPair, String type, boolean isMultiple)  {
        try {
            ins.reset();
        } catch (IOException e1) {
            LOG.error(e1.getMessage(), e1);
        }
        ConvertCmd convert = new ConvertCmd(true);
        try {
            Pipe pipeIn  = new Pipe(ins, null);
            Pipe pipeOut  = new Pipe(null, ous);
            convert.setInputProvider(pipeIn);
            convert.setOutputConsumer(pipeOut);
            IMOperation op = null;
            if (isMultiple) {
                op = resizeDynamicGifOperation(type, dstPair);
            } else {
                op = resizeStaticImageOperation(type, dstPair);
            }
            convert.run(op);
        } catch (IOException | InterruptedException | IM4JavaException e) {
            LOG.error(e.getMessage(), e);
            return false;
        }
        return true;
    }
    
    private String quality = "85";
    
    private IMOperation resizeStaticImageOperation(String type, SizePair dstPair) {
        IMOperation op = new IMOperation();
        op.addImage("-");
        int width = dstPair.getWidth();
        int height = dstPair.getHeight();
        op.addRawArgs("-scale", width + "x" + height);
        op.addRawArgs("-gravity", "center");
        op.addRawArgs("-quality", quality);
        op.addImage(type + ":-");
        return op;
    }

    private IMOperation resizeDynamicGifOperation(String type, SizePair dstPair) {
        IMOperation op = new IMOperation();
        op.addImage("-");
        int width = dstPair.getWidth();
        int height = dstPair.getHeight();
        op.addRawArgs("-thumbnail", width + "x" + height);
        op.addImage(type + ":-");
        return op;
    }

    private boolean processScaleCommand(String srcPath, String dstPath, SizePair dstPair, boolean isMultiple) {
        IMOperation op = null;
        if (isMultiple) {
            op = resizeDynamicGifOperation(srcPath, dstPath, dstPair);
        } else {
            op = resizeStaticImageOperation(srcPath, dstPath, dstPair);
        }
        ConvertCmd convert = new ConvertCmd(true);
        try {
            convert.run(op);
        } catch (IOException | InterruptedException | IM4JavaException e) {
            LOG.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    //gm convert old.gif [-coalesce] -thumbnail 50x50 new.gif
    private IMOperation resizeDynamicGifOperation(String srcPath, String dstPath, SizePair dstPair) {
        IMOperation op = new IMOperation();
        op.addImage(srcPath);
        int width = dstPair.getWidth();
        int height = dstPair.getHeight();
        op.addRawArgs("-thumbnail", width + "x" + height);
        op.addImage(dstPath);
        return op;
    }

    //gm convert old.jpg -scale 200x100 -gravity center -quality 80 new.jpg
    private IMOperation resizeStaticImageOperation(String srcPath, String dstPath, SizePair dstPair) {
        IMOperation op = new IMOperation();
        op.addImage(srcPath);
        int width = dstPair.getWidth();
        int height = dstPair.getHeight();
        op.addRawArgs("-scale", width + "x" + height);
        op.addRawArgs("-gravity", "center");
        op.addRawArgs("-quality", quality);
        op.addImage(dstPath);
        return op;
    }
    
    public void testPipe(String srcDir,String dstDir, String filename,String type) {
        IMOperation op = new IMOperation();  
        op.addImage("-");                   
        // read from stdin  
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(srcDir+filename); 
            fos = new FileOutputStream(dstDir+filename); 
            Pipe pipeIn  = new Pipe(fis,null); 
            Pipe pipeOut = new Pipe(null,fos);  
            op.addRawArgs("-scale","200x200");
            op.addRawArgs("-gravity", "center");
            op.addRawArgs("-quality", quality);
            // write to stdout in tif-format
            op.addImage(type+":-");               
            // set up command 
            ConvertCmd convert = new ConvertCmd(true); 
            convert.setInputProvider(pipeIn); 
            convert.setOutputConsumer(pipeOut); 
            convert.run(op);
        } catch (IOException | InterruptedException | IM4JavaException e) {
            LOG.error(e.getMessage(), e);
        } finally{
            IOUtils.closeQuietly(fis);
            IOUtils.closeQuietly(fos);
        }
    }
    private static int BUFFER_SIZE = 4096;
    
    public static InputStream byteToInputStream(byte[] in) throws Exception{  
        ByteArrayInputStream is = new ByteArrayInputStream(in);  
        return is;  
    } 
    
    public static byte[] inputStreamToByte(InputStream in) throws IOException{  
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;  
        while((count = in.read(data,0,BUFFER_SIZE)) != -1){
            outStream.write(data, 0, count);  
        }
        data = null;  
        return outStream.toByteArray();  
    }  
    
    public static void main(String[] args) throws Exception {
        String srcDirPath = "/Users/clive/test/resize-test/src/";
        String dstDirPath = "/Users/clive/test/resize-test/dst/";
        String[] filenames = {"yilabao.jpg"};
        int[] size = {600};
        String[] q = {"80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100","101"};
        
        ImageUtil iu = new ImageUtil();
        for (String fn : filenames) {
            for (int i = 0; i < size.length; i++) {
                for (int j = 0; j < q.length; j++) {
                    iu.quality = q[j];
                    String filename = iu.getTempfilename(fn, size[i], q[j]);
                    InputStream fis = new FileInputStream(new File(srcDirPath + fn));
                    byte[] cache = inputStreamToByte(fis);
                    long start = System.currentTimeMillis();
                    InputStream ins = byteToInputStream(cache);
                    File f = new File(dstDirPath + filename);
                    OutputStream fos = new FileOutputStream(f);
                    iu.resize(ins, fos, size[i], "width");
                    long end = System.currentTimeMillis();
                    System.out.printf("filename=%-40s time=%-10d size=%-10d scale-width=%-10d quality=%s\n", fn,(end-start),f.length(), size[i],q[j]);
                }
            }
        }

    }

    private String getTempfilename(String fn, int i, String string) {
        return fn+"_"+i+"_"+string;
    }

}
