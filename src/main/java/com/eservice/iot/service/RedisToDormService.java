package com.eservice.iot.service;

import com.eservice.iot.model.web.AccessRecordModel;
import com.eservice.iot.model.web.AttendanceNumber;
import com.eservice.iot.model.web.NightFallModel;
import com.eservice.iot.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class RedisToDormService {

    private final static Logger logger = LoggerFactory.getLogger(RedisToDormService.class);

    @Resource
    private RedisUtil redisUtil;

    //获取所有通行记录
    private List<AccessRecordModel> accessRecordModelList = new ArrayList<>();

    //获取所有夜归考勤数据
    private List<NightFallModel> nightFallModelList = new ArrayList<>();

    //进入统计过滤
    private static ArrayList<String> inExistList = new ArrayList<>();
    //外出统计过滤
    private static ArrayList<String> outExistList = new ArrayList<>();

    //总人数统计
    private static ArrayList<AccessRecordModel> total = new ArrayList<>();

    private AttendanceNumber attendanceNumber = new AttendanceNumber();
    //考勤统计过滤
    private static ArrayList<String> attendanceExistList = new ArrayList<>();


    /**
     * 通过不同楼层的设备查询进出人数
     * @param floorDevice
     * @return
     */

    public List<AccessRecordModel> queryAccessRecordList(String floorDevice) {
        String device[] = floorDevice.split(",");
        Iterator iterator = redisUtil.zGet("access_set",1,redisUtil.zSize("access_set")).iterator();
        if (accessRecordModelList.size() > 0) {
            accessRecordModelList.clear();
        }
        while (iterator.hasNext()) {
            AccessRecordModel accessRecord = (AccessRecordModel) iterator.next();
            if (accessRecord != null) {
                for (int i = 0; i < device.length; i++) {
                    if (accessRecord.getDeviceId().contains(device[i])) {
                        accessRecordModelList.add(accessRecord);
                        if (accessRecord.getType().equals("进")) {
                            if (!inExistList.contains(accessRecord.getPersonId())) {
                                inExistList.add(accessRecord.getPersonId());
                                if (outExistList.contains(accessRecord.getPersonId())) {
                                    outExistList.remove(accessRecord.getPersonId());
                                }
                            }
                        }
                        if (accessRecord.getType().equals("出")) {
                            if (!outExistList.contains(accessRecord.getPersonId())) {
                                outExistList.add(accessRecord.getPersonId());
                                if (inExistList.contains(accessRecord.getPersonId())) {
                                    inExistList.remove(accessRecord.getPersonId());
                                }
                            }
                        }
                        if (accessRecord.getPersonId() != null) {
                            if (!attendanceExistList.contains(accessRecord.getPersonId())) {
                                attendanceExistList.add(accessRecord.getPersonId());
                                //redisUtil.set("attendanceNum", attendanceExistList.size());
                            }
                        }

                    }
                }
                attendanceNumber.setInDormitory(inExistList.size());
                attendanceNumber.setOutDormitory(outExistList.size());
                attendanceNumber.setAttendanceNum(attendanceExistList.size());
                attendanceNumber.setTotal(total.size());
            } else {
                logger.info("accessRecord is null:{}", accessRecord);
            }
        }
        return accessRecordModelList;
    }

    /**
     * 统计在寝，外出人数
     * @return
     */
    public AttendanceNumber queryAttendanceNum(){
        return attendanceNumber;
    }

    /**
     * 根据楼栋号获取晚归记录
     * @param floorNo
     * @return
     */
    public List<NightFallModel> queryNihtFallModelList(String floorNo){
        Iterator iterator=redisUtil.sGet("night_fall").iterator();
        if (nightFallModelList.size()>0){
            nightFallModelList.clear();
        }
        while (iterator.hasNext()){
            NightFallModel nightFallModel=(NightFallModel) iterator.next();
            if (nightFallModel!=null){
                if (nightFallModel.getFloorNo().indexOf(floorNo)!=-1){
                    nightFallModelList.add(nightFallModel);
                }
            }
        }
        return nightFallModelList;
    }

}
