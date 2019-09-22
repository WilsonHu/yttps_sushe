package com.eservice.iot.web;

import com.alibaba.fastjson.JSON;
import com.eservice.iot.core.Result;
import com.eservice.iot.core.ResultGenerator;
import com.eservice.iot.model.faceImg.FaceImage;
import com.eservice.iot.model.floor_device.FloorDevice;
import com.eservice.iot.model.user.User;
import com.eservice.iot.service.UserService;
import com.eservice.iot.service.common.ExcelService;
import com.eservice.iot.service.impl.UserServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Class Description: xxx
 *
 * @author Mr.Zhang
 * @date 2019/07/14.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);
    @Resource
    private UserServiceImpl userService;

    @Resource
    private ExcelService excelService;

    @PostMapping("/add")
    public Result add(String jsonData) {
        User user = JSON.parseObject(jsonData, User.class);
        userService.save(user);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        userService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(String jsonData) {
        User user = JSON.parseObject(jsonData, User.class);
        userService.update(user);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam @NotNull Integer id) {
        User user = userService.findById(id);
        return ResultGenerator.genSuccessResult(user);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size, String key) {
        PageHelper.startPage(page, size);
        List<User> list = userService.getUserInfoByKey(key);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/requestLogin")
    public Result requestLogin(@RequestParam String account, @RequestParam String password) {

        if (account == null || "".equals(account)) {
            return ResultGenerator.genFailResult("账号不能为空！");
        } else if (password == null || "".equals(password)) {
            return ResultGenerator.genFailResult("密码不能为空！");
        } else {

            User user = userService.requestLogin(account, password);
            if (user == null) {
                return ResultGenerator.genFailResult("账号或密码不正确！");
            } else {
                return ResultGenerator.genSuccessResult(user);
            }
        }
    }

    @PostMapping("/upload")
    public Result upload(MultipartFile multipartFile) {
        List<FaceImage> list = excelService.importRecord(multipartFile, FaceImage.class);
        for (FaceImage faceimg : list) {
            if (faceimg.getFaceImgUrl() != null && !faceimg.getFaceImgUrl().equals("")) {
                String name = faceimg.getDepartments() + "_" + faceimg.getStudentNo() + "_" + faceimg.getName();
                try {
                    download(faceimg.getFaceImgUrl(), name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return ResultGenerator.genSuccessResult();
    }

    public void download(String urlString, String i) throws Exception {
        // 构造URL
        URL url = new URL(urlString);
        // 打开连接
        URLConnection con = url.openConnection();
        // 输入流
        InputStream is = con.getInputStream();
        // 1K的数据缓冲
        byte[] bs = new byte[1024];
        // 读取到的数据长度
        int len;
        // 输出的文件流
        String filename = "D:\\faceimg/" + i + ".jpg";  //下载路径及下载图片名称
        File file = new File(filename);
        FileOutputStream os = new FileOutputStream(file, true);
        // 开始读取
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        logger.info("get img is success: ==>{}", i);
        // 完毕，关闭所有链接
        os.close();
        is.close();
    }
}
