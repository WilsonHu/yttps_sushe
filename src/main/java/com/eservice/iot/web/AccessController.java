package com.eservice.iot.web;

import com.eservice.iot.core.Result;
import com.eservice.iot.core.ResultGenerator;
import com.eservice.iot.model.web.AccessRecordModel;
import com.eservice.iot.model.web.AttendanceNumber;
import com.eservice.iot.model.web.NightAttendance;
import com.eservice.iot.service.DormService;
import com.eservice.iot.util.RedisUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @program: yttps_sushe
 * @description: 获取redis中所有通行记录
 * @author: yuhan_xie
 * @create: 2019/7/15 10:35
 */
@RestController
@RequestMapping("/access")
public class AccessController {
    private final static Logger logger = LoggerFactory.getLogger(AccessController.class);

    @Resource
    private DormService dormService;


    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size, String floorDevice) {
        PageHelper.startPage(page, size);
        List<AccessRecordModel> accessRecordModelList = dormService.queryAccessRecordList(floorDevice);
        PageInfo pageInfo = new PageInfo(accessRecordModelList);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/attendanceCount")
    public Result attendanceCount() {
        AttendanceNumber attendanceNumber = dormService.queryAttendanceNum();
        return ResultGenerator.genSuccessResult(attendanceNumber);
    }


}
