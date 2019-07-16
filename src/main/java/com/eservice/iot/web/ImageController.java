package com.eservice.iot.web;

import com.eservice.iot.core.Result;
import com.eservice.iot.core.ResultGenerator;
import com.eservice.iot.service.park.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wy
 * @date 2019/7/16
 */
@RestController
public class ImageController {

    @Autowired
    ImageService imageService;

   public static List<String> images=new ArrayList<>();
    @PostMapping("/image/{icNumber}")
    public Result query(@PathVariable String icNumber){
        byte[] data=imageService.getImageById(icNumber);
        //对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        //返回Base64编码过的字节数组字符串
        return ResultGenerator.genSuccessResult(encoder.encode(data));
    }


}
