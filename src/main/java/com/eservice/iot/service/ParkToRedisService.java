package com.eservice.iot.service;

import com.eservice.iot.model.floor_device.FloorDevice;
import com.eservice.iot.model.park.AccessRecord;
import com.eservice.iot.model.park.Tag;
import com.eservice.iot.model.web.AccessRecordModel;
import com.eservice.iot.model.web.NightFallModel;
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
public class ParkToRedisService {
    private final static Logger logger = LoggerFactory.getLogger(ParkToRedisService.class);

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

    private static double index = 0;

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 每天0：0：0 清除
     */
    @Scheduled(cron = "0 0 0 * * ?")
    private void resetStaffDataScheduled() {
        logger.info("\n================ 清空昨日通行记录 ===============");
        if (recordExistList.size() > 0) {
            int begin = recordExistList.size();
            recordExistList.clear();
            int end = recordExistList.size();
            logger.info("Clear attendance record. size: {} -> {}", begin, end);
        }
    }

    public void processStaffSign(ArrayList<AccessRecord> records) {
        Collections.reverse(records);
        if (index == 0) {
            index = redisUtil.zSize("access_set") + 1;
        }
        for (AccessRecord accessRecord : records) {
            if (!recordExistList.contains(accessRecord.getScene_image_id())) {
                recordExistList.add(accessRecord.getScene_image_id());
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
                            access.setType(isOutOrIn(accessRecord.getDevice_id()));
                        } else {
                            access.setType("禁止");
                        }
                        access.setPass_time(new Date(accessRecord.getTimestamp() * 1000L));

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
                redisUtil.zSet("access_set", access,index);
            }
        }

    }


    @Scheduled(cron = "0 0 5 * * ?")
    public void countNightStaff() {
        try {
            String day = simple.format(new Date()) + " ";
            long lateTime = formatter.parse(day + LATE_TIME).getTime() / 1000L;
            long truancyEnd = formatter.parse(day + TRUANCY_END).getTime() / 1000L;
            lateTime -= 24 * 60 * 60;
            List<AccessRecord> absentees = accessService.querySignInCount(lateTime, truancyEnd);
            if (tagService.getFloorTags() != null && tagService.getFloorTags().size() > 0) {
                for (Tag tag : tagService.getFloorTags()) {
                    NightFallModel nightFallModel = new NightFallModel();
                    nightFallModel.setFloorNo(tag.getTag_name());
                    List<AccessRecordModel> absenteeList = new ArrayList<>();
                    if (absentees != null) {
                        for (AccessRecord accessRecord : absentees) {
                            absenteeList.add(packaging(accessRecord));
                        }
                    }
                    nightFallModel.setAbsentees(absenteeList);
                    redisUtil.sSet("night_fall", nightFallModel);
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public AccessRecordModel packaging(AccessRecord accessRecord) {
        AccessRecordModel access = new AccessRecordModel();

        access.setClasses(tagService.getTagName(accessRecord.getPerson().getTag_id_list().get(0)));
        access.setName(accessRecord.getPerson().getPerson_information().getName());
        if (accessRecord.getPass_result().equals("PASS")) {
            access.setType(isOutOrIn(accessRecord.getDevice_id()));
        } else {
            access.setType("禁止");
        }
        access.setPass_time(new Date(accessRecord.getTimestamp() * 1000L));
        access.setImageId(accessRecord.getFace_image_id());
        access.setDeviceId(accessRecord.getDevice_id());
        access.setPersonId(accessRecord.getPerson().getPerson_id());

        return access;
    }


    /**
     * 判断是进还是出,
     *
     * @param deviceId
     * @return
     */
    public String isOutOrIn(String deviceId) {
        List<FloorDevice> floorDevices = floorDeviceService.findAll();

        for (FloorDevice floorDevice : floorDevices) {
            if (floorDevice.getDeviceId().equals(deviceId)) {
                if (floorDevice.getType() == 0) {
                    return "进";
                } else {
                    return "出";
                }
            }
        }
        return "未知";
    }

}
