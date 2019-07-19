package com.eservice.iot.web;

import com.eservice.iot.core.Result;
import com.eservice.iot.core.ResultGenerator;
import com.eservice.iot.model.web.AccessRecordModel;
import com.eservice.iot.service.DormService;
import com.eservice.iot.service.park.TagService;
import com.eservice.iot.util.RedisUtil;
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

@RestController
@RequestMapping("/dorm")
public class DormController {

    private final static Logger logger = LoggerFactory.getLogger(DormController.class);

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    TagService tagService;
    @Resource
    private DormService dormService;

    /**
     * 调取查询最新数据接口
     * @param size
     * @param deviceId
     * @return
     */
    @PostMapping("/getNewestAccessRecordList")
    public Result getNewestAccessRecordList(@RequestParam(defaultValue = "10") Integer size ,String deviceId) {
        ArrayList<AccessRecordModel> accessRecordModelList = new ArrayList<>();
        if (deviceId != "") {
            String[] accessDeviceIds = deviceId.split(",");
            accessRecordModelList = dormService.getNewestVisitRecord(size, accessDeviceIds);
        }
        return ResultGenerator.genSuccessResult(accessRecordModelList);

    }

    @PostMapping("/getNewestAccessRecordListByPass")
    public Result getNewestAccessRecordListByPass(@RequestParam(defaultValue = "10") Integer size, String deviceId,String pass) {
        if (deviceId!="") {
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
     * @param size
     * @param deviceId
     * @param time
     * @return
     */

    @PostMapping("/getAccessRecordList")
    public Result getAccessRecordList(@RequestParam(defaultValue = "10") Integer size, String deviceId, String time) {
        String deviceIdList[] = deviceId.split(",");
        Long endTime = 0L;
        if (time!=""){
            try {
                endTime = formatter.parse(time).getTime() / 1000L-1;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        ArrayList<AccessRecordModel> accessRecordModelList = dormService.getVisitRecord(size, deviceIdList,endTime);
        return ResultGenerator.genSuccessResult(accessRecordModelList);
    }

    @PostMapping("/getAccessRecordListByPass")
    public Result getAccessRecordListByPass(@RequestParam(defaultValue = "10") Integer size, String deviceId,String time,String pass) {
        String deviceIdList[] = deviceId.split(",");
        ArrayList<AccessRecordModel> accessRecordModelList=new ArrayList<>();
        Long endTime = 0L;
        if (time!=""){
            try {
                endTime = formatter.parse(time).getTime() / 1000L-1;
            } catch (ParseException e) {
                e.printStackTrace();
            }
             accessRecordModelList = dormService.getAccessRecordByPass(size, deviceIdList, endTime,pass);
            return ResultGenerator.genSuccessResult(accessRecordModelList);
        }
        return ResultGenerator.genSuccessResult(accessRecordModelList);
    }

    @PostMapping("/getAccessRecordListByName")
    public Result getAccessRecordListByName(@RequestParam(defaultValue = "10") Integer size, String deviceId, String time,String name) {
        String deviceIdList[] = deviceId.split(",");
        Long endTime = 0L;
        if (time!=""){
            try {
                endTime = formatter.parse(time).getTime() / 1000L-1;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        ArrayList<AccessRecordModel> accessRecordModelList = dormService.getAccessRecordByName(size, deviceIdList,endTime,name);
        return ResultGenerator.genSuccessResult(accessRecordModelList);
    }

    @PostMapping("/getAccessRecordListByIdentity")
    public Result getAccessRecordListByIdentity(@RequestParam(defaultValue = "10") Integer size, String deviceId, String time,String identity) {
        String deviceIdList[] = deviceId.split(",");
        Long endTime = 0L;
        if (time!=""){
            try {
                endTime = formatter.parse(time).getTime() / 1000L-1;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        ArrayList<AccessRecordModel> accessRecordModelList = dormService.getVisitRecordByIdentity(size, deviceIdList,endTime,identity);
        return ResultGenerator.genSuccessResult(accessRecordModelList);
    }


}
