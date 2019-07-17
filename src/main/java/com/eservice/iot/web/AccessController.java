package com.eservice.iot.web;

import com.eservice.iot.core.Result;
import com.eservice.iot.core.ResultGenerator;
import com.eservice.iot.model.park.AccessRecord;
import com.eservice.iot.model.web.AccessRecordModel;
import com.eservice.iot.model.web.AttendanceNumber;
import com.eservice.iot.model.web.NightFallModel;
import com.eservice.iot.service.RedisToDormService;
import com.eservice.iot.util.Util;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    private RedisToDormService redisToDormService;


    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size, String floorDevice, String name) {
        PageHelper.startPage(page, size);
        List<AccessRecordModel> nameList = new ArrayList<>();
        List<AccessRecordModel> accessRecordModelList = redisToDormService.queryAccessRecordList(floorDevice);
        Collections.sort(accessRecordModelList, new Comparator<AccessRecordModel>() {
            @Override
            public int compare(AccessRecordModel o1, AccessRecordModel o2) {
                String date=formatter.format(o1.getPass_time());
                String date1=formatter.format(o2.getPass_time());
                Long time=0L;
                Long time1=0L;
                try {
                    time=formatter.parse(date).getTime()/1000L;
                    time1=formatter.parse(date1).getTime()/1000L;

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (time>time1){
                      return -1;
                }
                if (time.equals(time1)){
                    return 0;
                }
                return 1;
            }
        });
        if (name != null && name != "") {
            for (AccessRecordModel accessRecordModel : accessRecordModelList) {
                if (accessRecordModel.getName().indexOf(name) != -1) {
                    nameList.add(accessRecordModel);
                }
            }
            PageInfo pageInfo = new PageInfo();
            pageInfo.setTotal(nameList.size());
            pageInfo.setList(Util.pagingQuery(page, size, nameList));
            return ResultGenerator.genSuccessResult(pageInfo);
        }
        PageInfo pageInfo = new PageInfo();
        pageInfo.setTotal(accessRecordModelList.size());
        pageInfo.setList(Util.pagingQuery(page, size, accessRecordModelList));
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/attendanceCount")
    public Result attendanceCount() {
        AttendanceNumber attendanceNumber = redisToDormService.queryAttendanceNum();
        return ResultGenerator.genSuccessResult(attendanceNumber);
    }

    @PostMapping("/getNightfall")
    public Result getNightfall(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size, String floorNo) {
        PageHelper.startPage(page, size);
        List<NightFallModel> nightFallModelList = redisToDormService.queryNihtFallModelList(floorNo);
        PageInfo pageInfo = new PageInfo();
        pageInfo.setTotal(nightFallModelList.size());
        pageInfo.setList(Util.pagingQuery(page, size, nightFallModelList));
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
