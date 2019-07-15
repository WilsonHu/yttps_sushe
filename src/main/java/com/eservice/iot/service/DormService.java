package com.eservice.iot.service;

import com.eservice.iot.model.floor_device.FloorDevice;
import com.eservice.iot.model.park.AccessRecord;
import com.eservice.iot.model.web.AccessRecordModel;
import com.eservice.iot.service.park.AccessService;
import com.eservice.iot.service.park.TagService;
import com.eservice.iot.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class DormService {
    private final static Logger logger = LoggerFactory.getLogger(DormService.class);

    @Value("${late_time}")
    private String LATE_TIME;
    @Value("${truancy_startTime}")
    private String TRUANCY_START;
    @Value("${truancy_endTime}")
    private String TRUANCY_END;

    @Autowired
    private TagService tagService;
    @Autowired
    private AccessService accessService;

    @Resource
    private FloorDeviceService floorDeviceService;
    @Resource
    private RedisUtil redisUtil;
    //通行记录过滤
    private static ArrayList<String> recordExistList = new ArrayList<>();
    //考勤统计过滤
    private static ArrayList<String> attendanceExistList = new ArrayList<>();
    //进入统计过滤
    private static ArrayList<String> inExistList = new ArrayList<>();
    //外出统计过滤
    private static ArrayList<String> outExistList = new ArrayList<>();

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * 每天0：0：0 清除
     */
    @Scheduled(cron = "0 0 0 * * ?")
    private void resetStaffDataScheduled() {
        logger.info("\n================ 清空昨日通行记录 ===============");
        if(recordExistList.size() > 0) {
            int begin = recordExistList.size();
            recordExistList.clear();
            int end = recordExistList.size();
            logger.info("Clear attendance record. size: {} -> {}", begin,end);
        }
    }

    public void processStaffSign(ArrayList<AccessRecord> records) {
        Collections.reverse(records);
        for (AccessRecord accessRecord : records) {
            if (!recordExistList.contains(accessRecord.getScene_image_id())) {
                recordExistList.add(accessRecord.getScene_image_id());
                attendanceStaffCount(accessRecord);
                AccessRecordModel access = new AccessRecordModel();
                switch (accessRecord.getIdentity()) {
                    case "STRANGER":
                        access.setClasses("? ? ?");
                        access.setName("陌生人");
                        access.setType("未注册");
                        access.setPass_time(new Date(accessRecord.getTimestamp() * 1000L));
                        access.setImageId(accessRecord.getFace_image_id());
                        access.setDeviceId(accessRecord.getDevice_id());
                        break;
                    case "STAFF":
                        access.setClasses(tagService.getTagName(accessRecord.getPerson().getTag_id_list().get(0)));
                        access.setName(accessRecord.getPerson().getPerson_information().getName());
                        if (accessRecord.getPass_result().equals("PASS")) {
                            if (isOutOrIn(accessRecord.getDevice_id())==0) {
                                access.setType("进");
                                if(!inExistList.contains(accessRecord.getPerson().getPerson_id())){
                                    inExistList.add(accessRecord.getPerson().getPerson_id());
                                    if(outExistList.contains(accessRecord.getPerson().getPerson_id())){
                                        outExistList.remove(accessRecord.getPerson().getPerson_id());
                                    }
                                }
                            }
                            if (isOutOrIn(accessRecord.getDevice_id())==1) {
                                access.setType("出");
                                if(!outExistList.contains(accessRecord.getPerson().getPerson_id())){
                                    outExistList.add(accessRecord.getPerson().getPerson_id());
                                    if(inExistList.contains(accessRecord.getPerson().getPerson_id())){
                                        inExistList.remove(accessRecord.getPerson().getPerson_id());
                                    }
                                }
                            }
                            redisUtil.set("inDormitory",inExistList.size());
                            redisUtil.set("outDormitory",outExistList.size());
                        }else {
                            access.setType("禁止");
                        }
                        access.setPass_time(new Date(accessRecord.getTimestamp()*1000L));
                        access.setImageId(accessRecord.getFace_image_id());
                        access.setDeviceId(accessRecord.getDevice_id());
                        break;
                    case "BLACKLIST":
                        access.setClasses(tagService.getTagName(accessRecord.getPerson().getTag_id_list().get(0)));
                        access.setName(accessRecord.getPerson().getPerson_information().getName());
                        access.setType("黑名单");
                        access.setPass_time(new Date(accessRecord.getTimestamp() * 1000L));
                        access.setImageId(accessRecord.getFace_image_id());
                        access.setDeviceId(accessRecord.getDevice_id());
                        break;
                    default:
                        break;
                }
                redisUtil.sSet("access_set",access);
            }
        }
    }

    private void attendanceStaffCount(AccessRecord record){
        if(!attendanceExistList.contains(record.getPerson().getPerson_id())){
            attendanceExistList.add(record.getPerson().getPerson_id());
            redisUtil.set("attendanceNum",attendanceExistList.size());
        }
    }

    @Scheduled(cron = "0 0 5 * * ?")
    public void countNightStaff(){
        try {
            String day = simple.format(new Date())+" ";
            long lateTime = formatter.parse(day+LATE_TIME).getTime()/1000L;
            long truancyEnd = formatter.parse(day+TRUANCY_END).getTime()/1000L;
            long truancyStart = formatter.parse(day+TRUANCY_START).getTime()/1000L;
            if(truancyStart>truancyEnd){
                truancyStart -= 24*60*60;
            }
            redisUtil.set("lateCount",accessService.querySignInCount(lateTime,truancyStart));
            redisUtil.set("truancyCount",accessService.querySignInCount(truancyStart,truancyEnd));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
     /**
     * 判断是进还是出,
     * @param deviceId
     * @return
     */
    public int isOutOrIn(String deviceId){
        List<FloorDevice> floorDevices = floorDeviceService.findAll();
        for(FloorDevice floorDevice : floorDevices){
            if(floorDevice.getDeviceId().equals(deviceId)){
                return floorDevice.getType();
            }
        }
        return -1;
    }

}
