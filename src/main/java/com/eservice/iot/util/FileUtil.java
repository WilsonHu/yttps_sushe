package com.eservice.iot.util;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {
    private final  static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static File ImageFile(String img, String name){
        byte[] buff=Base64.decode(img);
        File file =null;
        FileOutputStream fout=null;
        try {
            file = File.createTempFile(name, ".jpg");
            fout=new FileOutputStream(file);
            fout.write(buff);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fout!=null) {
                try {
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    public static void deleteFile(String name ){
        File file = new File(name);
        if(file.delete()){
            logger.info("File name : {}, delete success. ",name);
        }else {
            logger.info("File name : {}, delete fail. ",name);
        }
    }
}
