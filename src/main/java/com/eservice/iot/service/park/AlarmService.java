package com.eservice.iot.service.park;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eservice.iot.model.ResponseCode;
import com.eservice.iot.model.ResponseModel;
import com.eservice.iot.model.park.SurveillanceAlarm;
import com.eservice.iot.model.park.VisitRecord;
import com.eservice.iot.util.FileUtil;
import com.eservice.iot.util.PhotoDigestUtil;
import com.eservice.iot.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: yttps_sushe
 * @description: 获取布控报警
 * @author: yuhan_xie
 * @create: 2019/9/12 15:03
 */
@Component
public class AlarmService {

    private final static Logger logger = LoggerFactory.getLogger(AlarmService.class);

    @Value("${park_base_url}")
    private String PARK_BASE_URL;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ImageService imageService;

    private ThreadPoolTaskExecutor mExecutor;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Value("${stranger_score}")
    private int STRANGER_SCORE;

    @Value("${attendance_size}")
    private int ATTENDANCE_SIZE;

    @Value("${valid_time}")
    private int VALID_TIME;

    private String token;

    private static ArrayList<VisitRecord> CURRENT_ATTENDANCE = new ArrayList<>();
    private static ArrayList<String> CURRENT_TRACK = new ArrayList<>();
    private static ArrayList<String> IMAGE_NAMES = new ArrayList<>();

    @Scheduled(cron = "0 0 1 * * ?")
    public void resetStaffDataScheduled() {
        logger.info("每天凌晨一点清除前一天签到记录：{}", formatter.format(new Date()));


        logger.info("================ 清理临时文件 ===============");
        if (IMAGE_NAMES.size() > 0) {
            for (String name : IMAGE_NAMES) {
                FileUtil.deleteFile(name);
            }
        }
        //通过MQTT将员工签到信息发送至web端
        logger.warn("Send message to client to clear sign in data!");
        //mqttMessageHelper.sendToClient("staff/sign_in/reset", "{}");
    }

    public List<VisitRecord> requestParkStranger(String[] deviceId, Long startTime, Long queryEndTime) {
        if (token == null && tokenService != null) {
            token = tokenService.getToken();
        }
        if (token != null) {
            HashMap<String, Object> postParameters = new HashMap<>();
//        ///考勤记录查询开始时间

            postParameters.put("start_timestamp", startTime);
//        ///考勤记录查询结束时间
            if (queryEndTime == null || queryEndTime <= 0) {
                queryEndTime = System.currentTimeMillis() / 1000;
            }
            postParameters.put("end_timestamp", queryEndTime);
            logger.info("startTime :  {} ,queryEndTime : {}", formatter.format(startTime * 1000L), formatter.format(queryEndTime * 1000L));
            //只获取员工数据
            ArrayList<String> identity = new ArrayList<>();
            identity.add("STRANGER");
            postParameters.put("identity_list", identity);
            //只获取指定考勤设备的过人记录
            List<String> arrayList = Arrays.asList(deviceId);
            postParameters.put("device_id_list", arrayList);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
            headers.add(HttpHeaders.AUTHORIZATION, token);

            HttpEntity httpEntity = new HttpEntity<>(JSON.toJSONString(postParameters), headers);
            try {
                ResponseEntity<String> responseEntity = restTemplate.postForEntity(PARK_BASE_URL + "/visit_record/query", httpEntity, String.class);
                if (responseEntity.getStatusCodeValue() == ResponseCode.OK) {
                    String body = responseEntity.getBody();
                    if (body != null) {
                        ResponseModel responseModel = JSONObject.parseObject(body, ResponseModel.class);
                        if (responseModel != null && responseModel.getResult() != null) {
                            ArrayList<VisitRecord> tempList = (ArrayList<VisitRecord>) JSONArray.parseArray(responseModel.getResult(), VisitRecord.class);
                            if (tempList != null && tempList.size() > 0) {
                                logger.info(" STRANGER size :  {}", tempList.size());
                                List<VisitRecord> strangerList = processStaffSignInResponse(tempList, startTime.equals(Util.getDateStartTime().getTime() / 1000));
                                //query成功后用上一次查询的结束时间作为下一次开始时间，减去1秒形成闭区间，
                                // 这里的时间是服务器时间，所以跟门禁或者抓拍机不一定是一个时间，容易遗漏
                                //queryStartTime = queryEndTime - 1;
                                return strangerList;
                            } else {
                                clear();
                            }
                        }
                    }
                }
            } catch (HttpClientErrorException exception) {
                if (exception.getStatusCode().value() == ResponseCode.TOKEN_INVALID) {
                    //token失效,重新获取token后再进行数据请求
                    token = tokenService.getToken();
                    requestParkStranger(deviceId, startTime, queryEndTime);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                logger.error(exception.getMessage());
            }
        }
        return null;
    }

    private List<VisitRecord> processStaffSignInResponse(ArrayList<VisitRecord> records, boolean initial) {
        Collections.reverse(records);
        for (VisitRecord visitRecord : records) {
            ArrayList<VisitRecord> mSendList = new ArrayList<>();
            logger.info("STRANGER visitRecord score : " + visitRecord.getScore());
            if (visitRecord.getIdentity().equals("STRANGER") && visitRecord.getScore() < STRANGER_SCORE) {
                String img = imageService.getImageById(visitRecord.getFace_image_id());
                //String isTrue = imageService.qualityVerify(img);
//                if (!"true".equals(isTrue)) {
//                    logger.warn(" qualityVerify : {} ", isTrue);
//                    continue;
//                }

                logger.info("visitRecord TrackId : " + visitRecord.getTrack_id());
                if (!initial) {
                    if (!CURRENT_TRACK.contains(visitRecord.getTrack_id())) {
                        CURRENT_TRACK.add(visitRecord.getTrack_id());
                        if (image(visitRecord, img)) {
                            if (CURRENT_ATTENDANCE.size() > ATTENDANCE_SIZE) {
                                CURRENT_ATTENDANCE.remove(0);
                            }
                            CURRENT_ATTENDANCE.add(visitRecord);
                            mSendList.add(visitRecord);
                        }
                    }

                    //建立线程池发送钉钉
                    if (mSendList.size() > 0) {
                        if (mExecutor == null) {
                            //initExecutor();
                            logger.warn("Send sign in staff to web, size ==> {} , {}", mSendList.size(), JSON.toJSONString(mSendList));
                            return mSendList;
                        }

                      /*  mExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                //通过MQTT将员工签到信息发送至web端
                                logger.warn("Send sign in staff to web, size ==> {} , {}", mSendList.size(),JSON.toJSONString(mSendList));
                               // mqttMessageHelper.sendToClient("stranger/sign_in", JSON.toJSONString(mSendList));

                            }
                        });*/
                    }
                }
            }
        }
        return null;
    }

    public boolean image(VisitRecord visitRecord, String img) {
        try {
            if (!IMAGE_NAMES.contains(visitRecord.getFace_image_id())) {
                IMAGE_NAMES.add(visitRecord.getFace_image_id() + ".jpg");
                File file = FileUtil.ImageFile(img, visitRecord.getFace_image_id());
                if (CURRENT_ATTENDANCE.size() > 0) {
                    for (VisitRecord visit : CURRENT_ATTENDANCE) {
                        File fileTemp = new File(visit.getFace_image_id() + ".jpg");
                        double score = PhotoDigestUtil.compare(PhotoDigestUtil.getData(fileTemp), PhotoDigestUtil.getData(file));
                        logger.info("image1 {} : image2 {}  ===> {} ", visitRecord.getFace_image_id(), visit.getFace_image_id(), score);
                        if (score >= STRANGER_SCORE) {
                            return false;
                        }
                    }
                }
                return true;
            }
        } catch (Exception e) {
            logger.warn("visitRecord TrackId : {}, Error : {}", visitRecord.getTrack_id(), e.getMessage());
        }
        return false;
    }

    private void initExecutor() {
        mExecutor = new ThreadPoolTaskExecutor();
        mExecutor.setCorePoolSize(9);
        mExecutor.setMaxPoolSize(100);
        mExecutor.setThreadNamePrefix("YTTPS-");
        mExecutor.initialize();
    }

    public void clear() {
        if (CURRENT_ATTENDANCE.size() > 0) {
            int begin = CURRENT_ATTENDANCE.size();
            CURRENT_ATTENDANCE.removeIf(
                    visitRecord -> System.currentTimeMillis() / 1000 - visitRecord.getTimestamp() >= VALID_TIME
            );
            int end = CURRENT_ATTENDANCE.size();
            if (begin != end) {
                logger.info(" CURRENT_ATTENDANCE size {} -> {} ", begin, end);
            }

        }
    }

}
