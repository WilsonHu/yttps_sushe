package com.eservice.iot.web;

import com.eservice.iot.core.Result;
import com.eservice.iot.core.ResultGenerator;
import com.eservice.iot.model.park.AccessRecord;
import com.eservice.iot.model.park.SurveillanceAlarm;
import com.eservice.iot.model.park.VisitRecord;
import com.eservice.iot.model.web.*;
import com.eservice.iot.service.DormService;
import com.eservice.iot.service.park.StrangerService;
import com.eservice.iot.service.park.TagService;
import com.eservice.iot.service.park.VisitRecordService;
import com.eservice.iot.util.RedisUtil;
import com.eservice.iot.util.Util;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.swing.plaf.PanelUI;
import java.security.acl.LastOwnerException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: yttps_sushe
 * @description: 获取redis中所有通行记录
 * @author: yuhan_xie
 * @create: 2019/7/15 10:35
 */
@RestController
@RequestMapping("/access")
public class AccessController {
    @Autowired
    TagService tagService;
    @Resource
    private DormService dormService;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private VisitRecordService visitRecordService;
    @Resource
    private StrangerService strangerService;


    @PostMapping("/attendanceCount")
    public Result attendanceCount(String floor) {
        String tagId = tagService.getStaffId(new String[]{floor}).get(0);
        AttendanceNumber attendanceNumber = null;
        if (redisUtil.get("_" + tagId) != null) {
            attendanceNumber = (AttendanceNumber) redisUtil.get("_" + tagId);
        }
        return ResultGenerator.genSuccessResult(attendanceNumber);
    }

    @PostMapping("/getNightfall")
    public Result getNightfall(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size, String deviceList) {
        PageHelper.startPage(page, size);
        String[] deviceIds = deviceList.split(",");
        List<AccessRecordModel> nightFallModelList = dormService.getNightAccessRecord(deviceIds);
        PageInfo pageInfo = new PageInfo();
        if (nightFallModelList != null) {
            pageInfo.setTotal(nightFallModelList.size());
            pageInfo.setList(Util.pagingQuery(page, size, nightFallModelList));
            return ResultGenerator.genSuccessResult(pageInfo);
        }
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/getNumber")
    public Result getNumber(String floor) {
        String tagId = tagService.getStaffId(new String[]{floor}).get(0);
        AttendanceNumbers attendanceNumbers = null;
        if (redisUtil.get("_Num" + tagId) != null) {
            attendanceNumbers = (AttendanceNumbers) redisUtil.get("_Num" + tagId);
        }
        return ResultGenerator.genSuccessResult(attendanceNumbers);
    }

    @PostMapping("/getVisitRecord")
    public Result getVisitRecord(String tagName) {
        ArrayList<VisitRecordModel> list = new ArrayList<>();
        ArrayList<VisitRecordModel> visitRecords = visitRecordService.getVisitRecordList();
        if (visitRecords.size() > 0) {
            for (VisitRecordModel visitRecordModel : visitRecords) {
                if (visitRecordModel.getTagNames().contains(tagName)) {
                    list.add(visitRecordModel);
                }
            }
        }
        Collections.sort(list, new Comparator<VisitRecordModel>() {
            @Override
            public int compare(VisitRecordModel o1, VisitRecordModel o2) {
                if (o1.getTime().getTime() > o2.getTime().getTime()) {
                    return -1;
                }
                if (o1.getTime().getTime() == o2.getTime().getTime()) {
                    return 0;
                }
                return 1;
            }
        });
        return ResultGenerator.genSuccessResult(list);
    }

    @PostMapping("/getStranageList")
    public Result getStranageList(Date queryStartTime, Date queryFinishTime, String deviceIds) {
        String deviceIdList[] = deviceIds.split(",");
        try {
            Long startTime = queryStartTime.getTime() / 1000L;
            Long endTime = queryFinishTime.getTime() / 1000L;
            List<SurveillanceAlarm> stranageList = strangerService.requestSurveillanceAlarm(startTime, endTime, deviceIdList);
            return ResultGenerator.genSuccessResult(stranageList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultGenerator.genSuccessResult();
    }

}
