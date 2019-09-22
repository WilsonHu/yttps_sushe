package com.eservice.iot.service;

import com.eservice.iot.model.floor_device.FloorDevice;
import com.eservice.iot.model.park.AccessRecord;
import com.eservice.iot.model.park.Staff;
import com.eservice.iot.model.park.Tag;
import com.eservice.iot.model.park.VisitRecord;
import com.eservice.iot.model.web.AccessRecordModel;
import com.eservice.iot.model.web.AttendanceNumber;
import com.eservice.iot.model.web.AttendanceNumbers;
import com.eservice.iot.service.park.AccessService;
import com.eservice.iot.service.park.StaffService;
import com.eservice.iot.service.park.TagService;
import com.eservice.iot.core.Constant;
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
    @Value("${index}")
    private Integer INDEX;

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

    @Scheduled(initialDelay = 5000, fixedRate = 1000 * 60 * 15)
    private void countFloorStaffNumber() {
        if (queryTime == 0L) {
            try {
                queryTime = formatter.parse(day + TRUANCY_END).getTime() / 1000L;
            } catch (ParseException e) {
                e.printStackTrace();
                queryTime = Util.getDateStartTime().getTime() / 1000L;
            }
        }
        long endTime = System.currentTimeMillis() / 1000L;

        HashMap<String, Object> postParameters = new HashMap<>();
        ///通行记录查询开始时间
        postParameters.put("start_timestamp", queryTime);
        ///通行记录查询结束时间
        postParameters.put("end_timestamp", endTime);
        ArrayList<String> identity = new ArrayList<>();
        identity.add("STAFF");
        postParameters.put("identity_list", identity);
        //查询记录
        List<VisitRecord> records = accessService.requestParkVisitRecord(postParameters);


        //记录过滤
        ArrayList<VisitRecord> visitRecords = new ArrayList<>();
        Collections.reverse(records);
        if (records != null && records.size() > 0) {
            for (VisitRecord accessRecord : records) {
                if (!recordExistList.contains(accessRecord.getScene_image_id())) {
                    recordExistList.add(accessRecord.getScene_image_id());
                    visitRecords.add(accessRecord);
                }
            }
        }

        if (visitRecords.size() > 0) {
            //记录抽象
            ArrayList<AccessRecordModel> accessRecordModels = processRecords(visitRecords);
            //时间倒序排序
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
            //获取所有员工
            List<Staff> staffs = staffService.getStaffAllList();
            //获取所有楼栋
            List<Tag> tags = tagService.getFloorTagList();
            if (tags != null && tags.size() > 0) {
                //初始化楼栋过滤变量
                initExistList(tags.size());
            }
            //分楼栋统计
            for (int i = 0; i < tags.size(); i++) {
                Tag tag = tags.get(i);
                AttendanceNumber attendanceNumber = new AttendanceNumber();
                //统计总人数
                int staffCount = 0;
                for (Staff staff : staffs) {
                    if (staff.getTag_id_list().contains(tag.getTag_id())) {
                        staffCount++;
                    }
                }
                attendanceNumber.setTotal(staffCount);
                //统计进、出、考勤人数
                for (AccessRecordModel accessRecord : accessRecordModels) {
                    if (accessRecord.getTagIds() != null) {
                        if (accessRecord.getTagIds().contains(tag.getTag_id())) {
                            if (accessRecord.getType().equals("进")) {
                                if (!inExistList[i].contains(accessRecord.getPersonId())) {
                                    inExistList[i].add(accessRecord.getPersonId());
                                    if (outExistList[i].contains(accessRecord.getPersonId())) {
                                        outExistList[i].remove(accessRecord.getPersonId());
                                    }
                                }
                            }
                            if (accessRecord.getType().equals("出")) {
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
    }

    @Scheduled(initialDelay = 8000, fixedRate = 1000 * 60 * 20)
    private void countFloorStaffNumbers() {
        List<Tag> tags = tagService.getFloorTagList();
        if (tags != null && tags.size() > 0) {
            //分楼栋统计（整点）
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
                    if (index == INDEX) {
                        attendanceNumbers.attendanceNumbers = moveArray(attendanceNumbers.attendanceNumbers);
                        attendanceNumbers.inDormitories = moveArray(attendanceNumbers.inDormitories);
                        attendanceNumbers.outDormitories = moveArray(attendanceNumbers.outDormitories);
                        index = INDEX - 1;
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

    /**
     * 自动查询最新数据
     *
     * @param size
     * @param deviceIds
     * @return
     */
    public ArrayList<AccessRecordModel> getNewestVisitRecord(int size, String[] deviceIds) {
        List<String> arrayList = Arrays.asList(deviceIds);

        HashMap<String, Object> postParameters = new HashMap<>();
        ///通行记录分页查询
        postParameters.put("page", 0);
        ///通行记录分页查询
        postParameters.put("size", size);
        ///指定设备
        postParameters.put("device_id_list", arrayList);

        List<VisitRecord> visitRecordArrayList = accessService.requestParkVisitRecord(postParameters);

        if (visitRecordArrayList != null && visitRecordArrayList.size() > 0) {
            return processRecords(visitRecordArrayList);
        }
        return null;
    }

    public ArrayList<AccessRecordModel> getNewestAccessRecordByPass(int size, String[] deviceIds, String pass) {
        List<String> arrayList = Arrays.asList(deviceIds);

        HashMap<String, Object> postParameters = new HashMap<>();
        ///通行记录分页查询
        postParameters.put("page", 0);
        ///通行记录分页查询
        postParameters.put("size", size);
        ///指定设备
        postParameters.put("device_id_list", arrayList);
        ///指定人员身份
        ArrayList<String> identities = new ArrayList<>();
        identities.add(Constant.STAFF);
        postParameters.put("identity_list", identities);
        List<VisitRecord> visitRecords = accessService.requestParkVisitRecord(postParameters);
        if (visitRecords != null && visitRecords.size() > 0) {
            return processRecords(visitRecords);
        }
        return null;
    }

    public ArrayList<AccessRecordModel> getNewestAccessRecordByName(int size, String[] deviceIds, String name) {
        List<String> arrayList = Arrays.asList(deviceIds);
        HashMap<String, Object> postParameters = new HashMap<>();
        ///通行记录分页查询
        postParameters.put("page", 0);
        ///通行记录分页查询
        postParameters.put("size", size);
        ///指定设备
        ///指定人员身份
        ArrayList<String> identities = new ArrayList<>();
        identities.add(Constant.STAFF);
        postParameters.put("identity_list", identities);
        postParameters.put("device_id_list", arrayList);
        ///指定员工名称
        postParameters.put("fuzzy_name", name);

        List<VisitRecord> visitRecords = accessService.requestParkVisitRecord(postParameters);
        if (visitRecords != null && visitRecords.size() > 0) {
            return processRecords(visitRecords);
        }
        return null;
    }

    public ArrayList<AccessRecordModel> getNewestVisitRecordByIdentity(int size, String[] deviceIds, String identity) {
        List<String> arrayList = Arrays.asList(deviceIds);

        HashMap<String, Object> postParameters = new HashMap<>();
        ///通行记录分页查询
        postParameters.put("page", 0);
        ///通行记录分页查询
        postParameters.put("size", size);
        ///指定设备
        postParameters.put("device_id_list", arrayList);
        ///指定人员身份
        ArrayList<String> identities = new ArrayList<>();
        identities.add(identity);
        postParameters.put("identity_list", identities);

        List<VisitRecord> visitRecords = accessService.requestParkVisitRecord(postParameters);
        if (visitRecords != null && visitRecords.size() > 0) {
            return processRecords(visitRecords);
        }
        return null;
    }

    /**
     * 分页查询所有数据
     *
     * @param startTime
     * @param deviceIds
     * @param endTime
     * @return
     */
    public ArrayList<AccessRecordModel> getVisitRecord(Integer page, Integer size, String[] deviceIds, Long startTime, Long endTime) {
        List<String> arrayList = Arrays.asList(deviceIds);
        HashMap<String, Object> postParameters = new HashMap<>();
     /*   ///通行记录分页查询
        postParameters.put("size", size);*/
        ///指定设备
        postParameters.put("page", page);
        postParameters.put("size", size);
        postParameters.put("device_id_list", arrayList);
        ///指定通行记录查询结束时间
        postParameters.put("start_timestamp", startTime);
        postParameters.put("end_timestamp", endTime);

        List<VisitRecord> visitRecords = accessService.requestParkVisitRecord(postParameters);

        if (visitRecords != null && visitRecords.size() > 0) {
            return processRecords(visitRecords);
        }
        return null;
    }

    public ArrayList<AccessRecordModel> getAccessRecordByPass(int page, int size, String[] deviceIds, Long startTime, Long endTime, String pass) {
        List<String> arrayList = Arrays.asList(deviceIds);
        HashMap<String, Object> postParameters = new HashMap<>();
        ///通行记录分页查询
        postParameters.put("page", page);
        postParameters.put("size", size);
        ///指定设备
        postParameters.put("device_id_list", arrayList);
        ///指定通行记录查询结束时间
        postParameters.put("start_timestamp", startTime);

        postParameters.put("end_timestamp", endTime);
        ///指定通行状态
        ArrayList<String> passes = new ArrayList<>();
        passes.add(pass);
        postParameters.put("pass_result_list", passes);
        ///指定人员身份
        ArrayList<String> identities = new ArrayList<>();
        identities.add(Constant.STAFF);
        postParameters.put("identity_list", identities);
        List<VisitRecord> visitRecords = accessService.requestParkVisitRecord(postParameters);
        if (visitRecords != null && visitRecords.size() > 0) {
            return processRecords(visitRecords);
        }
        return null;
    }

    public ArrayList<AccessRecordModel> getAccessRecordByName(int page, int size, String[] deviceIds, Long startTime, Long endTime, String name) {
        List<String> arrayList = Arrays.asList(deviceIds);
        HashMap<String, Object> postParameters = new HashMap<>();
        ///通行记录分页查询
        postParameters.put("page", page);
        postParameters.put("size", size);
        ///指定设备
        postParameters.put("device_id_list", arrayList);
        ///指定通行记录查询结束时间
        postParameters.put("start_timestamp", startTime);
        postParameters.put("end_timestamp", endTime);
        ///指定员工名称
        postParameters.put("fuzzy_name", name);

        List<VisitRecord> visitRecords = accessService.requestParkVisitRecord(postParameters);
        if (visitRecords != null && visitRecords.size() > 0) {
            return processRecords(visitRecords);
        }
        return null;
    }

    public ArrayList<AccessRecordModel> getVisitRecordByIdentity(int page, int size, String[] deviceIds, Long startTime, Long endTime, String identity) {
        List<String> arrayList = Arrays.asList(deviceIds);
        HashMap<String, Object> postParameters = new HashMap<>();
        ///通行记录分页查询
        postParameters.put("page", page);
        postParameters.put("size", size);
        ///指定设备
        postParameters.put("device_id_list", arrayList);
        ///指定通行记录查询结束时间
        postParameters.put("start_timestamp", startTime);
        postParameters.put("end_timestamp", endTime);
        ///指定人员身份
        ArrayList<String> identities = new ArrayList<>();
        identities.add(identity);
        postParameters.put("identity_list", identities);

        List<VisitRecord> visitRecords = accessService.requestParkVisitRecord(postParameters);
        if (visitRecords != null && visitRecords.size() > 0) {
            return processRecords(visitRecords);
        }
        return null;
    }

    /**
     * 晚归和未归的记录
     *
     * @param deviceIds
     * @return
     */
    public ArrayList<AccessRecordModel> getNightAccessRecord(String[] deviceIds) {
        try {
            List<String> arrayList = Arrays.asList(deviceIds);

            long lateTime = formatter.parse(day + LATE_TIME).getTime() / 1000L;
            long truancyEnd = formatter.parse(day + TRUANCY_END).getTime() / 1000L;
            lateTime -= 24 * 60 * 60;

            HashMap<String, Object> postParameters = new HashMap<>();
            ///通行记录查询开始时间
            postParameters.put("start_timestamp", lateTime);
            ///通行记录查询结束时间
            postParameters.put("end_timestamp", truancyEnd);
            ///指定设备
            if (arrayList != null && arrayList.size() > 0) {
                postParameters.put("device_id_list", arrayList);
            }
            List<VisitRecord> visitRecords = accessService.requestParkVisitRecord(postParameters);
            if (tagService.getFloorTagList() != null && tagService.getFloorTagList().size() > 0) {
                if (visitRecords != null && visitRecords.size() > 0) {
                    ArrayList<AccessRecordModel> accessRecordModels = new ArrayList<>();
                    for (VisitRecord visitRecord : visitRecords) {
                        //筛选 员工
                        if (visitRecord.getIdentity().equals(Constant.STAFF)) {
                            accessRecordModels.add(processStaffRecord(visitRecord));
                        }
                    }
                    return accessRecordModels;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AccessRecords To AccessRecordModels
     *
     * @param visitRecords
     * @return
     */
    public ArrayList<AccessRecordModel> processRecords(List<VisitRecord> visitRecords) {
        ArrayList<AccessRecordModel> accessRecordModels = new ArrayList<>();
        for (VisitRecord visitRecord : visitRecords) {
            //分类型进行数据抽象
            switch (visitRecord.getIdentity()) {
                case Constant.STRANGER:
                    accessRecordModels.add(processStrangeRecord(visitRecord));
                    break;
                case Constant.STAFF:
                    accessRecordModels.add(processStaffRecord(visitRecord));
                    break;
                case Constant.BLACKLIST:
                    accessRecordModels.add(processBlackListRecord(visitRecord));
                    break;
                default:
                    break;
            }
        }
        return accessRecordModels;
    }

    public AccessRecordModel processStaffRecord(VisitRecord visitRecord) {
        AccessRecordModel accessRecordModel = new AccessRecordModel();
        accessRecordModel.setClasses(tagService.getTagName(visitRecord.getPerson().getTag_id_list().get(0)));
        accessRecordModel.setName(visitRecord.getPerson().getPerson_information().getName());
        accessRecordModel.setType(isOutOrIn(visitRecord.getDevice_id()));
        accessRecordModel.setPass_time(new Date(visitRecord.getTimestamp() * 1000L));
        accessRecordModel.setPersonId(visitRecord.getPerson().getPerson_id());
        accessRecordModel.setTagIds(visitRecord.getPerson().getTag_id_list());
        accessRecordModel.setImageId(visitRecord.getFace_image_id());
        accessRecordModel.setDeviceId(visitRecord.getDevice_id());
        return accessRecordModel;
    }

    public AccessRecordModel processBlackListRecord(VisitRecord visitRecord) {
        AccessRecordModel accessRecordModel = new AccessRecordModel();
        accessRecordModel.setClasses(tagService.getTagName(visitRecord.getPerson().getTag_id_list().get(0)));
        accessRecordModel.setName(visitRecord.getPerson().getPerson_information().getRemark());
        accessRecordModel.setType("黑名单");
        accessRecordModel.setPass_time(new Date(visitRecord.getTimestamp() * 1000L));
        accessRecordModel.setPersonId(visitRecord.getPerson().getPerson_id());
        accessRecordModel.setImageId(visitRecord.getFace_image_id());
        accessRecordModel.setDeviceId(visitRecord.getDevice_id());
        return accessRecordModel;
    }

    public AccessRecordModel processStrangeRecord(VisitRecord visitRecord) {
        AccessRecordModel accessRecordModel = new AccessRecordModel();
        accessRecordModel.setClasses("? ? ?");
        accessRecordModel.setName("陌生人");
        accessRecordModel.setType("未注册");
        accessRecordModel.setPass_time(new Date(visitRecord.getTimestamp() * 1000L));
        accessRecordModel.setImageId(visitRecord.getFace_image_id());
        accessRecordModel.setDeviceId(visitRecord.getDevice_id());
        return accessRecordModel;
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

    /**
     * 统计过滤变量初始化
     *
     * @param size
     */
    private void initExistList(int size) {
        logger.info("\n================ 初始化过滤 ===============");
        if (attendExistList == null) {
            attendExistList = new ArrayList[size];
            for (int i = 0; i < size; i++) {
                attendExistList[i] = new ArrayList<>();
            }
            logger.info("Init attendExistList record size {} .", attendExistList.length);
        }
        if (outExistList == null) {
            outExistList = new ArrayList[size];
            for (int i = 0; i < size; i++) {
                outExistList[i] = new ArrayList<>();
            }
            logger.info("Init outExistList record size {} .", outExistList.length);
        }
        if (inExistList == null) {
            inExistList = new ArrayList[size];
            for (int i = 0; i < size; i++) {
                inExistList[i] = new ArrayList<>();
            }
            logger.info("Init inExistList record size {} .", inExistList.length);
        }
    }

    public int[] moveArray(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            array[i] = array[i + 1];
        }
        array[array.length - 1] = 0;
        return array;
    }
}
