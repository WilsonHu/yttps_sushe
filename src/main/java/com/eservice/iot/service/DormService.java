package com.eservice.iot.service;

import com.eservice.iot.model.floor_device.FloorDevice;
import com.eservice.iot.model.park.AccessRecord;
import com.eservice.iot.model.web.AccessRecordModel;
import com.eservice.iot.model.web.AttendanceNumber;
import com.eservice.iot.model.web.NightAttendance;
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
import java.util.*;

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

    //获取所有通行记录
    private List<AccessRecordModel> accessRecordModelList = new ArrayList<>();

    //获取所有夜归考勤数据
    private List<NightAttendance> nightAttendanceList = new ArrayList<>();

    //进入统计过滤
    private static ArrayList<String> inExistList = new ArrayList<>();
    //外出统计过滤
    private static ArrayList<String> outExistList = new ArrayList<>();

    private AttendanceNumber attendanceNumber = new AttendanceNumber();
    //考勤统计过滤
    private static ArrayList<String> attendanceExistList = new ArrayList<>();

    private static double index = 0;

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");

    public DormService() {

    }

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
                        access.setPersonId(null);
                        break;
                    case "STAFF":
                        access.setClasses(tagService.getTagName(accessRecord.getPerson().getTag_id_list().get(0)));
                        access.setName(accessRecord.getPerson().getPerson_information().getName());
                        if (accessRecord.getPass_result().equals("PASS")) {
                            if (isOutOrIn(accessRecord.getDevice_id()) == 0) {
                                access.setType("进");
                            }
                            if (isOutOrIn(accessRecord.getDevice_id()) == 1) {
                                access.setType("出");
                            }

                        } else {
                            access.setType("禁止");
                        }
                        access.setPass_time(new Date(accessRecord.getTimestamp() * 1000L));
                        access.setImageId(accessRecord.getFace_image_id());
                        access.setDeviceId(accessRecord.getDevice_id());
                        access.setPersonId(accessRecord.getPerson().getPerson_id());
                        break;
                    case "BLACKLIST":
                        access.setClasses(tagService.getTagName(accessRecord.getPerson().getTag_id_list().get(0)));
                        access.setName(accessRecord.getPerson().getPerson_information().getName());
                        access.setType("黑名单");
                        access.setPass_time(new Date(accessRecord.getTimestamp() * 1000L));
                        access.setImageId(accessRecord.getFace_image_id());
                        access.setDeviceId(accessRecord.getDevice_id());
                        access.setPersonId(null);
                        break;
                    default:
                        break;
                }
                redisUtil.zSet("access_set", access, index);
            }
        }
    }

    public void queryNightAttendance() {
        try {
            String day = simple.format(new Date()) + " ";
            long lateTime = formatter.parse(day + LATE_TIME).getTime() / 1000L;
            long truancyEnd = formatter.parse(day + TRUANCY_END).getTime() / 1000L;
            long truancyStart = formatter.parse(day + TRUANCY_START).getTime() / 1000L;
            if (truancyStart > truancyEnd) {
                truancyStart -= 24 * 60 * 60;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是进还是出,
     *
     * @param deviceId
     * @return
     */
    public int isOutOrIn(String deviceId) {
        List<FloorDevice> floorDevices = floorDeviceService.findAll();
        for (FloorDevice floorDevice : floorDevices) {
            if (floorDevice.getDeviceId().equals(deviceId)) {
                return floorDevice.getType();
            }
        }
        return -1;
    }

    /**
     * 通过楼层不同的设备查询进出人数
     * @param floorDevice
     * @return
     */
    public List<AccessRecordModel> queryAccessRecordList(String floorDevice) {
        String device[] = floorDevice.split(",");
        Iterator iterator = redisUtil.zGet("access_set", 0, redisUtil.zSize("access_set")).iterator();
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
                                redisUtil.set("attendanceNum", attendanceExistList.size());
                            }
                        }
                        attendanceNumber.setInDormitory(inExistList.size());
                        attendanceNumber.setOutDormitory(outExistList.size());
                        attendanceNumber.setAttendanceNum(attendanceExistList.size());
                    }
                }
            } else {
                logger.info("accessRecord is null:{}", accessRecord);
            }
        }
        return accessRecordModelList;
    }

    public AttendanceNumber queryAttendanceNum(){
        return attendanceNumber;
    }
}
