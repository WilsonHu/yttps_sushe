package com.eservice.iot.service;

import com.eservice.iot.model.floor_device.FloorDevice;
import com.eservice.iot.model.park.AccessRecord;
import com.eservice.iot.model.park.Staff;
import com.eservice.iot.model.park.Tag;
import com.eservice.iot.model.web.AccessRecordModel;
import com.eservice.iot.model.web.AttendanceNumber;
import com.eservice.iot.model.web.AttendanceNumbers;
import com.eservice.iot.service.park.AccessService;
import com.eservice.iot.service.park.StaffService;
import com.eservice.iot.service.park.TagService;
import com.eservice.iot.model.Constant;
import com.eservice.iot.util.RedisUtil;
import com.eservice.iot.util.Util;
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
    @Autowired
    private StaffService staffService;

    @Resource
    private FloorDeviceService floorDeviceService;
    @Resource
    private RedisUtil redisUtil;

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");

    private static long queryTime = 0L;
    private static String day;

    //通行记录过滤
    private static ArrayList<String> recordExistList = new ArrayList<>();
    //考勤过滤
    private static ArrayList<String>[] attendExistList = null;
    //出过滤
    private static ArrayList<String>[] outExistList = null;
    //进过滤
    private static ArrayList<String>[] inExistList = null;

    public DormService() {
        day = simple.format(new Date()) + " ";
    }

    /**
     * 每天5：0：0 清除
     */
    @Scheduled(cron = "0 0 5 * * ?")
    private void clearExistList() {
        logger.info("\n================ 清空过滤 ===============");
        if (recordExistList.size() > 0) {
            int begin = recordExistList.size();
            recordExistList.clear();
            int end = recordExistList.size();
            logger.info("Clear attendance record. size: {} -> {}", begin, end);
        }
        if (attendExistList.length > 0) {
            for (int i = 0; i < attendExistList.length; i++) {
                int begin = attendExistList[i].size();
                attendExistList[i].clear();
                int end = attendExistList[i].size();
                logger.info("Clear attendExistList[{}] record. size: {} -> {}", i, begin, end);
            }
        }
        if (outExistList.length > 0) {
            for (int i = 0; i < outExistList.length; i++) {
                int begin = outExistList[i].size();
                outExistList[i].clear();
                int end = outExistList[i].size();
                logger.info("Clear outExistList[{}] record. size: {} -> {}", i, begin, end);
            }
        }
        if (inExistList.length > 0) {
            for (int i = 0; i < inExistList.length; i++) {
                int begin = inExistList[i].size();
                inExistList[i].clear();
                int end = inExistList[i].size();
                logger.info("Clear inExistList[{}] record. size: {} -> {}", i, begin, end);
            }
        }
    }

    @Scheduled(cron = "0 0 5 * * ?")
    public void clearRedis(){
        List<Tag> tags = tagService.getFloorTagList();
        if (tags != null && tags.size() > 0) {
            for (Tag tag : tags) {
                redisUtil.del("_"+tag.getTag_id());
                redisUtil.del("_Num"+tag.getTag_id());
                redisUtil.del("index");
            }
        }
    }

    private void initExistList(int size) {
        logger.info("\n================ 初始化过滤 ===============");
        if (attendExistList == null) {
            attendExistList = new ArrayList[size];
            for (int i = 0; i < size; i++) {
                attendExistList[i] = new ArrayList<>();
            }
        }
        if (outExistList == null) {
            outExistList = new ArrayList[size];
            for (int i = 0; i < size; i++) {
                outExistList[i] = new ArrayList<>();
            }
        }
        if (inExistList == null) {
            inExistList = new ArrayList[size];
            for (int i = 0; i < size; i++) {
                inExistList[i] = new ArrayList<>();
            }
        }
    }

    public ArrayList<AccessRecordModel> getAccessByPaging(int page,Long endTime, int size, String[] deviceIds,String name) {
        List<String> arrayList = Arrays.asList(deviceIds);
        ArrayList<AccessRecord> tempList = accessService.queryAccessStaffByPaging(page-1,endTime, size, arrayList,name);
        if (tempList != null && tempList.size() > 0) {
            ArrayList<AccessRecordModel> accessRecordModels = new ArrayList<>();
            for (AccessRecord accessRecord : tempList) {
                accessRecordModels.add(processStaffSign(accessRecord));
            }
            return accessRecordModels;
        }
        return null;
    }

    public AccessRecordModel processStaffSign(AccessRecord accessRecord) {
        AccessRecordModel accessRecordModel = new AccessRecordModel();
        //分类型进行数据抽象
        switch (accessRecord.getIdentity()) {
            case Constant.STRANGER:
                accessRecordModel.setClasses("? ? ?");
                accessRecordModel.setName("陌生人");
                accessRecordModel.setType("未注册");
                accessRecordModel.setPass_time(new Date(accessRecord.getTimestamp() * 1000L));
                accessRecordModel.setImageId(accessRecord.getFace_image_id());
                accessRecordModel.setDeviceId(accessRecord.getDevice_id());
                break;
            case Constant.STAFF:
                accessRecordModel.setClasses(tagService.getTagName(accessRecord.getPerson().getTag_id_list().get(0)));
                accessRecordModel.setName(accessRecord.getPerson().getPerson_information().getName());
                if (accessRecord.getPass_result().equals("PASS")) {
                    accessRecordModel.setType(isOutOrIn(accessRecord.getDevice_id()));
                } else {
                    accessRecordModel.setType("禁止");
                }
                accessRecordModel.setPass_time(new Date(accessRecord.getTimestamp() * 1000L));
                accessRecordModel.setPersonId(accessRecord.getPerson().getPerson_id());
                accessRecordModel.setTagIds(accessRecord.getPerson().getTag_id_list());
                accessRecordModel.setImageId(accessRecord.getFace_image_id());
                accessRecordModel.setDeviceId(accessRecord.getDevice_id());
                break;
            case Constant.BLACKLIST:
                accessRecordModel.setClasses(tagService.getTagName(accessRecord.getPerson().getTag_id_list().get(0)));
                accessRecordModel.setName(accessRecord.getPerson().getPerson_information().getName());
                accessRecordModel.setType("黑名单");
                accessRecordModel.setPass_time(new Date(accessRecord.getTimestamp() * 1000L));
                accessRecordModel.setPersonId(accessRecord.getPerson().getPerson_id());
                accessRecordModel.setImageId(accessRecord.getFace_image_id());
                accessRecordModel.setDeviceId(accessRecord.getDevice_id());
                break;
            default:
                break;
        }
        return accessRecordModel;
    }

    public ArrayList<AccessRecordModel> getNightAccess(String[] deviceIds) {
        try {
            List<String> arrayList = Arrays.asList(deviceIds);
            long lateTime = formatter.parse(day + LATE_TIME).getTime() / 1000L;
            long truancyEnd = formatter.parse(day + TRUANCY_END).getTime() / 1000L;
            lateTime -= 24 * 60 * 60;
            List<AccessRecord> absentees = accessService.queryAccessStaffByTime(lateTime, truancyEnd, arrayList);
            ArrayList<AccessRecordModel> absenteeList = new ArrayList<>();
            if (tagService.getFloorTagList() != null && tagService.getFloorTagList().size() > 0) {
                if (absentees != null && absentees.size() > 0) {
                    for (AccessRecord accessRecord : absentees) {
                        //筛选出员工
                        if (accessRecord.getIdentity().equals(Constant.STAFF)) {
                            absenteeList.add(processStaffSign(accessRecord));
                        }
                    }
                }
            }
            return absenteeList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Scheduled(initialDelay = 5000, fixedRate = 1000 * 60 * 10)
    private void countFloorStaffNumber() {
        List<Staff> staffs = staffService.getStaffAllList();
        List<Tag> tags = tagService.getFloorTagList();
        if (tags != null && tags.size() > 0) {
            initExistList(tags.size());
        }
        if (queryTime == 0L) {
            try {
                queryTime = formatter.parse(day + TRUANCY_END).getTime() / 1000L;
            } catch (ParseException e) {
                e.printStackTrace();
                queryTime = Util.getDateStartTime().getTime() / 1000L;
            }
        }
        long endTime = System.currentTimeMillis() / 1000L;

        ArrayList<AccessRecord> records = accessService.queryAccessStaffByTime(queryTime, endTime, null);

        Collections.reverse(records);
        ArrayList<AccessRecordModel> accessRecordModels = new ArrayList<>();

        if (records != null && records.size() > 0) {
            for (AccessRecord accessRecord : records) {
                if (!recordExistList.contains(accessRecord.getScene_image_id())) {
                    recordExistList.add(accessRecord.getScene_image_id());
                    accessRecordModels.add(processStaffSign(accessRecord));
                }
            }
            queryTime = endTime - 1;
        }
        if (accessRecordModels != null && accessRecordModels.size() > 1) {
            Collections.sort(accessRecordModels, new Comparator<AccessRecordModel>() {
                @Override
                public int compare(AccessRecordModel o1, AccessRecordModel o2) {
                    Long time1 = o1.getPass_time().getTime() / 1000L;
                    Long time2 = o2.getPass_time().getTime() / 1000L;
                    return (int) (time1 - time2);
                }
            });
        }
        for (int i = 0; i < tags.size(); i++) {
            Tag tag = tags.get(i);
            AttendanceNumber attendanceNumber = new AttendanceNumber();
            int staffCount = 0;
            for (Staff staff : staffs) {
                if (staff.getTag_id_list().contains(tag.getTag_id())) {
                    staffCount++;
                }
            }
            for (AccessRecordModel accessRecord : accessRecordModels) {
                if (accessRecord.getTagIds() != null) {
                    if (accessRecord.getTagIds().contains(tag.getTag_id())) {
                        if (accessRecord.getType().indexOf("进")!=-1) {
                            if (!inExistList[i].contains(accessRecord.getPersonId())) {
                                inExistList[i].add(accessRecord.getPersonId());
                                if (outExistList[i].contains(accessRecord.getPersonId())) {
                                    outExistList[i].remove(accessRecord.getPersonId());
                                }
                            }
                        }
                        if (accessRecord.getType().indexOf("出")!=-1) {
                            if (!outExistList[i].contains(accessRecord.getPersonId())) {
                                outExistList[i].add(accessRecord.getPersonId());
                                if (inExistList[i].contains(accessRecord.getPersonId())) {
                                    inExistList[i].remove(accessRecord.getPersonId());
                                }
                            }
                        }
                        if (accessRecord.getTagIds() != null && accessRecord.getTagIds().size() > 0) {
                            if (!attendExistList[i].contains(accessRecord.getPersonId())) {
                                attendExistList[i].add(accessRecord.getPersonId());
                            }
                        }
                    }
                } else {
                    logger.info(accessRecord.getName());
                }
            }
            attendanceNumber.setTotal(staffCount);
            attendanceNumber.setInDormitory(inExistList[i].size());
            attendanceNumber.setOutDormitory(outExistList[i].size());
            attendanceNumber.setAttendanceNum(attendExistList[i].size());
            if (redisUtil.set("_" + tag.getTag_id(), attendanceNumber)) {
                logger.info("countFloorStaffNumber ==>Set  {} : {} ", tag.getTag_name(), attendanceNumber.show());
            } else {
                logger.error("countFloorStaffNumber ==>Set {} save Key {} false ! ", tag.getTag_name(), "_Num" + tag.getTag_id());
            }
        }
    }

    @Scheduled(initialDelay = 8000, fixedRate = 1000 * 60 * 15)
    private void countFloorStaffNumbers() {
        List<Tag> tags = tagService.getFloorTagList();
        if (tags != null && tags.size() > 0) {
            for (Tag tag : tags) {
                AttendanceNumber attendanceNumber = null;
                if (redisUtil.get("_" + tag.getTag_id()) != null) {
                    attendanceNumber = (AttendanceNumber) redisUtil.get("_" + tag.getTag_id());
                    logger.info("countFloorStaffNumbers ==>Get  {} : {} ", tag.getTag_name(), attendanceNumber.show());
                }

                AttendanceNumbers attendanceNumbers = null;
                if (redisUtil.get("_Num" + tag.getTag_id()) != null) {
                    attendanceNumbers = (AttendanceNumbers) redisUtil.get("_Num" + tag.getTag_id());
                } else {
                    attendanceNumbers = new AttendanceNumbers();
                }
                int index = 0;
                if (redisUtil.get("index") != null) {
                    index = (int) redisUtil.get("index");
                    if (index == 12) {
                        attendanceNumbers.attendanceNumbers = moveArray(attendanceNumbers.attendanceNumbers);
                        attendanceNumbers.inDormitories = moveArray(attendanceNumbers.inDormitories);
                        attendanceNumbers.outDormitories = moveArray(attendanceNumbers.outDormitories);
                        index=11;
                    }
                }
                if (attendanceNumber != null) {
                    attendanceNumbers.attendanceNumbers[index] = attendanceNumber.getAttendanceNum();
                    attendanceNumbers.inDormitories[index] = attendanceNumber.getInDormitory();
                    attendanceNumbers.outDormitories[index] = attendanceNumber.getOutDormitory();
                    if (redisUtil.set("_Num" + tag.getTag_id(), attendanceNumbers)) {
                        redisUtil.set("index", ++index);
                        logger.info("countFloorStaffNumbers ==>Set  {} : {} ", tag.getTag_name(), attendanceNumbers.show());
                    } else {
                        logger.error("countFloorStaffNumbers ==>Set  {} save Key {} false ! ", tag.getTag_name(), "_Num" + tag.getTag_id());
                    }
                }
            }

        }
    }

    public int[] moveArray(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            array[i] = array[i + 1];
        }
        array[array.length - 1] = 0;
        return array;
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
                    return "进_pass";
                } else {
                    return "出_pass";
                }
            }
        }
        return "未知";
    }
}
