package com.eservice.iot.util;

import com.eservice.iot.core.FileType;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class FileUtil {
    private final  static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * MultipartFile 转 File
     * @param file
     * @throws Exception
     */
    public static File multipartFileToFile(  MultipartFile file ) throws Exception {

        File toFile = null;
        if(file.equals("")||file.getSize()<=0){

        }else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }

    /**
     * File 转 MultipartFile
     * @param file
     * @throws Exception
     */
    public static MultipartFile fileToMultipartFile( File file ) throws Exception {

        FileInputStream fileInput = new FileInputStream(file);
        MultipartFile toMultipartFile = new MockMultipartFile("file",file.getName(),"text/plain", IOUtils.toByteArray(fileInput));
        toMultipartFile.getInputStream();
        return  toMultipartFile;
    }

    public static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取文件真实类型
     *
     * @param file 要获取类型的文件。
     * @return 文件类型枚举。
     */
    public static FileType getFileType(File file){
        FileInputStream inputStream =null;
        try{
            inputStream = new FileInputStream(file);
            byte[] head = new byte[4];
            if (-1 == inputStream.read(head)) {
                return FileType.UNKNOWN;
            }
            int headHex = 0;
            for (byte b : head) {
                headHex <<= 8;
                headHex |= b;
            }
            switch (headHex) {
                case 0x504B0304:
                    return FileType.ZIP;
                case 0x776f7264:
                    return FileType.TAR;
                case -0x51:
                    return FileType._7Z;
                case 0x425a6839:
                    return FileType.BZ2;
                case -0x74f7f8:
                    return FileType.GZ;
                case 0x52617221:
                    return FileType.RAR;
                default:
                    return FileType.UNKNOWN;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(inputStream!=null){
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return FileType.UNKNOWN;
    }

    /**
     * 删除
     * @param files
     */
    public static void deleteFile(File... files) {
        for (File file : files) {
            if (file.exists()) {
                file.delete();
            }
        }
    }


    public static void deleteFile(String name ){
        File file = new File(name);
        if(file.delete()){
            logger.info("File name : {}, delete success. ",name);
        }else {
            logger.info("File name : {}, delete fail. ",name);
        }
    }

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
}
