package com.eservice.iot.util;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.*;

/**
 * @program: yttps_shzx
 * @description: 提供图片的操作
 * @author: Mr.Zhang
 * @create: 2019-05-30 11:15
 **/
public class ImageUtil {

    private final static Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    /**
     * 图片转化成base64字符串
     *
     * @param imgFile
     * @return
     */
    public static String getImageStr(String imgFile) {//将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            logger.error("image not find : " + imgFile);
            return null;
        }
        //返回Base64编码过的字节数组字符串
        String imageBase64 = org.apache.commons.codec.binary.Base64.encodeBase64String(data);
        if (imageBase64 != null && !"".equals(imageBase64)) {
            return imageBase64.replaceAll("[\\s*\t\n\r]", "");
        }
        return null;
    }

    /**
     * base64字符串转化成图片
     *
     * @param base64str
     * @param savePath
     * @return
     */
    public static boolean generateImage(String base64str, String savePath) {   //对字节数组字符串进行Base64解码并生成图片
        if (base64str == null) {
            //图像数据为空
            return false;
        }
        //开始解码
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            //Base64解码
            byte[] b = decoder.decodeBuffer(base64str);
            //解码完成
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {//调整异常数据
                    b[i] += 256;
                }
            }
            //开始生成图片生成jpeg图片
            OutputStream out = new FileOutputStream(savePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 图片转化成base64字符串
     *
     * @param in
     * @return
     */
    public static String getImageStr(InputStream in) {//将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        byte[] data = null;
        //读取图片字节数组
        try {
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            logger.error("image not find  ");
            return null;
        }
        //返回Base64编码过的字节数组字符串
        String imageBase64 = org.apache.commons.codec.binary.Base64.encodeBase64String(data);
        if (imageBase64 != null && !"".equals(imageBase64)) {
            return imageBase64.replaceAll("[\\s*\t\n\r]", "");
        }
        return null;
    }

    public static String compressedFiles(MultipartFile avatarFile) {
        try {
            File file = FileUtil.multipartFileToFile(avatarFile);
            Long fileSize = file.length();
            if (fileSize > 1024 * 1024 * 1.5) {
                Thumbnails.of(file).scale(1F).outputQuality(0.3F).toFile(file);
            } else if (fileSize > 1024 * 1024) {
                Thumbnails.of(file).scale(1F).outputQuality(0.5F).toFile(file);
            } else if (fileSize > 600 * 1024) {
                Thumbnails.of(file).scale(1F).outputQuality(0.7F).toFile(file);
            }
            return fileBase64(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String compressedBase64(String url) {
        try {
            File file = new File(url);
            Long fileSize = file.length();
            if (fileSize > 1024 * 1024 * 4) {
                Thumbnails.of(file).scale(1F).outputQuality(0.1F).toFile(file);
            } else if (fileSize > 1024 * 1024 * 3) {
                Thumbnails.of(file).scale(1F).outputQuality(0.2F).toFile(file);
            } else if (fileSize > 1024 * 1024 * 2) {
                Thumbnails.of(file).scale(1F).outputQuality(0.3F).toFile(file);
            } else if (fileSize > 1024 * 1024) {
                Thumbnails.of(file).scale(1F).outputQuality(0.5F).toFile(file);
            } else if (fileSize > 600 * 1024) {
                Thumbnails.of(file).scale(1F).outputQuality(0.7F).toFile(file);
            }
            return fileBase64(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static  String findImage(String path,String fileName) {
        File dir = new File(path);        //获取其file对象
        File[] files = dir.listFiles();    //遍历path下的文件和目录，放在File数组中
        for (File file : files) {                    //遍历File[]数组
            if (!file.isDirectory()) {
                if(file.getName().indexOf(fileName)!=-1){
                    return file.getName();
                }
            }
        }
        return null;
    }


    public static String fileBase64(File file) {
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(file);
            byte[] buffInput = new byte[fin.available()];
            fin.read(buffInput);
            return Base64.encode(buffInput);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            file.delete();
        }
        return null;
    }
}
