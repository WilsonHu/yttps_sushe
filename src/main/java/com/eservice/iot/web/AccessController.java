package com.eservice.iot.web;

import com.eservice.iot.core.Result;
import com.eservice.iot.core.ResultGenerator;
import com.eservice.iot.model.web.AccessRecordModel;
import com.eservice.iot.model.web.AttendanceNumber;
import com.eservice.iot.model.web.AttendanceNumbers;
import com.eservice.iot.model.web.NightFallModel;
import com.eservice.iot.service.DormService;
import com.eservice.iot.service.park.TagService;
import com.eservice.iot.util.RedisUtil;
import com.eservice.iot.util.Util;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    TagService tagService;
    @Resource
    private DormService dormService;

    @Resource
    private RedisUtil redisUtil;

    ArrayList<AccessRecordModel> accessRecordModelsAll = new ArrayList<>();


    @PostMapping("/list")
    public Result list(String startTime, @RequestParam(defaultValue = "0") Integer size, String deviceList, @RequestParam(defaultValue = "") String type, @RequestParam(defaultValue = "") String name) {
        String deviceIds[] = deviceList.split(",");
        Long endTime = System.currentTimeMillis() / 1000L;
        try {
            endTime = formatter.parse(startTime).getTime() / 1000L-1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
         if (accessRecordModelsAll.size()>0){
             accessRecordModelsAll.clear();
         }
        while (accessRecordModelsAll.size() < size) {
            Long time = getAccesssRecord(endTime, size, deviceIds, type,name,accessRecordModelsAll);
            if (endTime.equals(time)) {
                break;
            }
            logger.info(formatter.format(new Date(endTime * 1000L)));
        }
        return ResultGenerator.genSuccessResult(accessRecordModelsAll);
    }

    public Long getAccesssRecord(Long endTime, Integer size, String[] deviceList, String type,String name, ArrayList<AccessRecordModel> accessRecordModels) {
        ArrayList<AccessRecordModel> accessRecordModelList = dormService.getAccessByPaging(0, endTime, size, deviceList,name);
        if (!type.equals("")) {
            for (AccessRecordModel accessRecordModel : accessRecordModelList) {
                if (accessRecordModel.getType().indexOf(type) != -1) {
                    accessRecordModels.add(accessRecordModel);
                    if (accessRecordModels.size() == 10) {
                        break;
                    }
                }
            }
        } else {
            accessRecordModelsAll = accessRecordModelList;
        }
        return accessRecordModelList.get(0).getPass_time().getTime() / 1000L;
    }

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
        List<AccessRecordModel> nightFallModelList = dormService.getNightAccess(deviceIds);
        PageInfo pageInfo = new PageInfo();
        pageInfo.setTotal(nightFallModelList.size());
        pageInfo.setList(Util.pagingQuery(page, size, nightFallModelList));
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/getAccess")
    public Result getAccess(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size, String deviceList, @RequestParam(defaultValue = "") String type,@RequestParam(defaultValue = "")String name) {
        String deviceIds[] = deviceList.split(",");
        ArrayList<AccessRecordModel> accessRecordModels = dormService.getAccessByPaging(page, null, size, deviceIds,name);
        if (!type.equals("")) {
            ArrayList<AccessRecordModel> accessRecordModelArrayList = new ArrayList<>();
            for (AccessRecordModel accessRecordModel : accessRecordModels) {
                if (accessRecordModel.getType().indexOf(type) != -1) {
                    accessRecordModelArrayList.add(accessRecordModel);
                }
            }
            return ResultGenerator.genSuccessResult(accessRecordModelArrayList);
        }
        return ResultGenerator.genSuccessResult(accessRecordModels);
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
}
