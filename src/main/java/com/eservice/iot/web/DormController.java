package com.eservice.iot.web;

import com.eservice.iot.core.Result;
import com.eservice.iot.core.ResultGenerator;
import com.eservice.iot.model.park.VisitRecord;
import com.eservice.iot.model.web.AccessRecordModel;
import com.eservice.iot.service.DormService;
import com.eservice.iot.service.park.AccessService;
import com.eservice.iot.service.park.AlarmService;
import com.eservice.iot.service.park.TagService;
import com.eservice.iot.util.RedisUtil;
import com.eservice.iot.util.Util;
import com.fasterxml.jackson.annotation.JsonProperty;
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
import java.lang.reflect.MalformedParameterizedTypeException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/dorm")
public class DormController {

    private final static Logger logger = LoggerFactory.getLogger(DormController.class);

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    TagService tagService;
    @Resource
    private DormService dormService;
    @Resource
    private AccessService accessService;
    @Resource
    private AlarmService alarmService;


    /**
     * 调取查询最新数据接口
     *
     * @param size
     * @param deviceId
     * @return
     */
    @PostMapping("/getNewestAccessRecordList")
    public Result getNewestAccessRecordList(@RequestParam(defaultValue = "10") Integer size, String deviceId) {
        ArrayList<AccessRecordModel> accessRecordModelList = new ArrayList<>();
        if (!deviceId.equals("")) {
            String[] accessDeviceIds = deviceId.split(",");
            accessRecordModelList = dormService.getNewestVisitRecord(size, accessDeviceIds);
        }
        return ResultGenerator.genSuccessResult(accessRecordModelList);

    }

    @PostMapping("/getNewestAccessRecordListByPass")
    public Result getNewestAccessRecordListByPass(@RequestParam(defaultValue = "10") Integer size, String deviceId, String pass) {
        if (deviceId != "") {
            String deviceIdList[] = deviceId.split(",");
            ArrayList<AccessRecordModel> accessRecordModelList = dormService.getNewestAccessRecordByPass(size, deviceIdList, pass);
            return ResultGenerator.genSuccessResult(accessRecordModelList);
        }
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/getNewestAccessRecordListByName")
    public Result getNewestAccessRecordListByName(@RequestParam(defaultValue = "10") Integer size, String deviceId, String name) {
        String deviceIdList[] = deviceId.split(",");
        ArrayList<AccessRecordModel> accessRecordModelList = dormService.getNewestAccessRecordByName(size, deviceIdList, name);
        return ResultGenerator.genSuccessResult(accessRecordModelList);
    }

    @PostMapping("/getNewestAccessRecordListByIdentity")
    public Result getNewestAccessRecordListByIdentity(@RequestParam(defaultValue = "10") Integer size, String deviceId, String identity) {
        String deviceIdList[] = deviceId.split(",");
        ArrayList<AccessRecordModel> accessRecordModelList = dormService.getNewestVisitRecordByIdentity(size, deviceIdList, identity);
        return ResultGenerator.genSuccessResult(accessRecordModelList);
    }

    /**
     * 调取分页查询接口
     *
     * @param page
     * @param size
     * @param deviceId
     * @param queryFinishTime
     * @param queryEndTime
     * @return
     */

    @PostMapping("/getAccessRecordList")
    public Result getAccessRecordList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, String deviceId, String queryFinishTime, String queryEndTime) {
        String deviceIdList[] = deviceId.split(",");
        try {
            Long startTime = formatter.parse(queryFinishTime).getTime() / 1000L;
            Long endTime = formatter.parse(queryEndTime).getTime() / 1000L;
            ArrayList<AccessRecordModel> accessRecordModelList = dormService.getVisitRecord(page, size, deviceIdList, startTime, endTime);
            Map<String, Object> map = new HashMap<>();
            map.put("list", accessRecordModelList);
            map.put("total", accessService.getTotal());

            return ResultGenerator.genSuccessResult(map);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/getAccessRecordListByPass")
    public Result getAccessRecordListByPass(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, String deviceId, String queryFinishTime, String queryEndTime, String pass) {
        String deviceIdList[] = deviceId.split(",");
        ArrayList<AccessRecordModel> accessRecordModelList = new ArrayList<>();
        try {
            Long startTime = formatter.parse(queryFinishTime).getTime() / 1000L;
            Long endTime = formatter.parse(queryEndTime).getTime() / 1000L;
            accessRecordModelList = dormService.getAccessRecordByPass(page, size, deviceIdList, startTime, endTime, pass);
            Map<String, Object> map = new HashMap<>();
            map.put("list", accessRecordModelList);
            map.put("total", accessService.getTotal());
            return ResultGenerator.genSuccessResult(map);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/getAccessRecordListByName")
    public Result getAccessRecordListByName(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, String deviceId, String queryFinishTime, String queryEndTime, String name) {
        String deviceIdList[] = deviceId.split(",");
        try {
            Long startTime = formatter.parse(queryFinishTime).getTime() / 1000L;
            Long endTime = formatter.parse(queryEndTime).getTime() / 1000L;
            ArrayList<AccessRecordModel> accessRecordModelList = dormService.getAccessRecordByName(page, size, deviceIdList, startTime, endTime, name);
            Map<String, Object> map = new HashMap<>();
            map.put("list", accessRecordModelList);
            map.put("total", accessService.getTotal());
            return ResultGenerator.genSuccessResult(map);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/getAccessRecordListByIdentity")
    public Result getAccessRecordListByIdentity(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, String deviceId, String queryFinishTime, String queryEndTime, String identity) {
        String deviceIdList[] = deviceId.split(",");
        try {
            Long startTime = formatter.parse(queryFinishTime).getTime() / 1000L;
            Long endTime = formatter.parse(queryEndTime).getTime() / 1000L;
            ArrayList<AccessRecordModel> accessRecordModelList = dormService.getVisitRecordByIdentity(page, size, deviceIdList, startTime, endTime, identity);
            Map<String, Object> map = new HashMap<>();
            map.put("list", accessRecordModelList);
            map.put("total", accessService.getTotal());
            return ResultGenerator.genSuccessResult(map);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ResultGenerator.genSuccessResult();
    }


    @PostMapping("/getStrangerList")
    public Result getStrangerList(String deviceId, Date queryStartTime, Date queryFinishTime) {
        String deviceIdList[] = deviceId.split(",");
        try {
            /*Long startTime = formatter.parse(queryStartTime).getTime() / 1000L;
            Long endTime = formatter.parse(queryFinishTime).getTime() / 1000L;*/
            Long startTime = queryStartTime.getTime() / 1000L;
            Long endTime = queryFinishTime.getTime() / 1000L;
            List<VisitRecord> strangerList = alarmService.requestParkStranger(deviceIdList, startTime, endTime);
            return ResultGenerator.genSuccessResult(strangerList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultGenerator.genSuccessResult();
    }
}
