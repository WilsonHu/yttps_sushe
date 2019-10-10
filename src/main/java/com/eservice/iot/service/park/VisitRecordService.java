package com.eservice.iot.service.park;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eservice.iot.model.ResponseCode;
import com.eservice.iot.model.ResponseModel;
import com.eservice.iot.model.floor_device.FloorDevice;
import com.eservice.iot.model.park.VisitRecord;
import com.eservice.iot.model.web.VisitRecordModel;
import com.eservice.iot.service.FloorDeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @program: yttps_sushe
 * @description: 根据人员id查询最近一次抓拍记录
 * @author: yuhan_xie
 * @create: 2019/10/9 9:32
 */
@Component
public class VisitRecordService {

    private final static Logger logger = LoggerFactory.getLogger(VisitRecordService.class);

    @Value("${park_base_url}")
    private String PARK_BASE_URL;

    @Autowired
    private TokenService tokenService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private StaffService staffService;
    @Autowired
    private TagService tagService;
    @Autowired
    private FloorDeviceService floorDeviceService;

    private String token;

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Value("${day}")
    private int day;

    //获取人员的最近一次抓拍记录
    private ArrayList<VisitRecordModel> visitRecordList = new ArrayList<>();

    @Scheduled(initialDelay = 5000, fixedRate = 1000 * 60 * 10)
    public void requestVisitRecord() {
        if (token == null) {
            token = tokenService.getToken();
        }
        if (token != null) {
            try {
                HashMap<String, Object> postParameters = new HashMap<>();
                List<String> staffId = staffService.getStaffIdList();
                postParameters.put("person_id_list", staffId);
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
                headers.add(HttpHeaders.AUTHORIZATION, token);
                HttpEntity httpEntity = new HttpEntity<>(JSON.toJSONString(postParameters), headers);
                ResponseEntity<String> responseEntity = restTemplate.postForEntity(PARK_BASE_URL + "/visit_record/recent_capture", httpEntity, String.class);
                if (responseEntity.getStatusCodeValue() == ResponseCode.OK) {
                    String body = responseEntity.getBody();
                    if (body != null) {
                        processVisitRecord(body);
                    } else {
                        requestVisitRecord();
                    }
                }

            } catch (HttpClientErrorException exception) {
                if (exception.getStatusCode().value() == ResponseCode.TOKEN_INVALID) {
                    //token失效,重新获取token后再进行数据请求
                    token = tokenService.getToken();
                    requestVisitRecord();
                }
                logger.error(exception.getMessage());
            } catch (Exception exception) {
                exception.printStackTrace();
                logger.error(exception.getMessage());
            }
        }
    }

    public void processVisitRecord(String body) {
        Long nowTime = System.currentTimeMillis() / 1000;
        ArrayList<VisitRecord> staffVisitRecord = new ArrayList<>();
        if (body != null) {
            ResponseModel responseModel = JSONObject.parseObject(body, ResponseModel.class);
            if (responseModel != null && responseModel.getResult() != null) {
                List<VisitRecord> tempList = JSONArray.parseArray(responseModel.getResult(), VisitRecord.class);
                if (tempList != null && tempList.size() > 0) {
                    for (VisitRecord visitRecord : tempList) {
                        if (nowTime - visitRecord.getTimestamp() >= day) {
                            staffVisitRecord.add(visitRecord);
                        }
                    }

                    visitRecordList = ScreeningVisitRecord(staffVisitRecord);
                    logger.info("visitRecordList num ==> {}", visitRecordList.size());
                }
            }
        }
    }

    public ArrayList<VisitRecordModel> getVisitRecordList() {
        return visitRecordList;
    }

    /**
     * 提取前端页面所需要展示的信息
     *
     * @param visitRecord
     * @return
     */
    public ArrayList<VisitRecordModel> ScreeningVisitRecord(List<VisitRecord> visitRecord) {
        ArrayList<VisitRecordModel> visitRecordsList = new ArrayList<>();
        for (VisitRecord visitRecords : visitRecord) {
            VisitRecordModel visitRecordModel = new VisitRecordModel();
            visitRecordModel.setName(visitRecords.getPerson().getPerson_information().getName());
            visitRecordModel.setType(isOutOrIn(visitRecords.getDevice_id()));
            visitRecordModel.setTagNames(tagService.getTagName(visitRecords.getPerson().getTag_id_list()));
            visitRecordModel.setImageId(visitRecords.getFace_image_id());
            visitRecordModel.setTime(new Date(visitRecords.getTimestamp() * 1000L));
            visitRecordModel.setPersonId(visitRecords.getPerson().getPerson_id());
            visitRecordsList.add(visitRecordModel);
        }
        return visitRecordsList;
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
