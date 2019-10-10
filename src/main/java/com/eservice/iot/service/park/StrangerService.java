package com.eservice.iot.service.park;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eservice.iot.model.ResponseCode;
import com.eservice.iot.model.ResponseModel;
import com.eservice.iot.model.park.SurveillanceAlarm;
import com.eservice.iot.model.park.VisitRecord;
import com.eservice.iot.model.web.AccessRecordModel;
import org.apache.catalina.LifecycleState;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import sun.rmi.runtime.Log;

import java.util.*;

/**
 * @program: yttps_sushe
 * @description: 获取布控策略中的陌生人
 * @author: yuhan_xie
 * @create: 2019/10/8 11:56
 */
@Component
public class StrangerService {
    private final static Logger logger = LoggerFactory.getLogger(StrangerService.class);

    @Value("${park_base_url}")
    private String PARK_BASE_URL;

    @Autowired
    private TokenService tokenService;
    @Autowired
    private RestTemplate restTemplate;

    private String token;

    @Value("${surveillance_policy_id}")
    private String policyIds;
    @Value("${type}")
    private String type;


    public List<SurveillanceAlarm> requestSurveillanceAlarm(Long queryStartTime, Long queryFinishTime, String[] deviceIds) {

        if (token == null) {
            token = tokenService.getToken();
        }
        if (token != null) {
            try {
                HashMap<String, Object> postParameters = new HashMap<>();
                postParameters.put("start_timestamp", queryStartTime);
                postParameters.put("end_timestamp", queryFinishTime);
                //指定设备
                List<String> arrayList = Arrays.asList(deviceIds);
                postParameters.put("device_id_list", arrayList);
                //查询陌生人
                ArrayList<String> identity = new ArrayList<>();
                identity.add("STRANGER");
                postParameters.put("identity_list", identity);
                //指定布控
                ArrayList<String> policyId = new ArrayList<>();
                policyId.add(policyIds);
                postParameters.put("surveillance_policy_id_list", policyId);
                //指定查询类型，比中，未必中
                ArrayList<String> policyType = new ArrayList<>();
                policyType.add(type);
                postParameters.put("type_list", policyType);
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
                headers.add(HttpHeaders.AUTHORIZATION, token);
                HttpEntity httpEntity = new HttpEntity<>(JSON.toJSONString(postParameters), headers);
                ResponseEntity<String> responseEntity = restTemplate.postForEntity(PARK_BASE_URL + "/surveillance/alarm", httpEntity, String.class);
                if (responseEntity.getStatusCodeValue() == ResponseCode.OK) {
                    String body = responseEntity.getBody();
                    return processSurveillanceAlarm(body);
                } else {
                    logger.error("params is :startTime=>{},endTime=>{},deviceIds=>{}", queryStartTime, queryFinishTime, deviceIds);
                    requestSurveillanceAlarm(queryStartTime, queryFinishTime, deviceIds);
                }

            } catch (HttpClientErrorException exception) {
                if (exception.getStatusCode().value() == ResponseCode.TOKEN_INVALID) {
                    //token失效,重新获取token后再进行数据请求
                    token = tokenService.getToken();
                    requestSurveillanceAlarm(queryStartTime, queryFinishTime, deviceIds);
                }
                logger.error(exception.getMessage());
            } catch (Exception exception) {
                exception.printStackTrace();
                logger.error(exception.getMessage());
            }

        }
        return null;
    }

    public List<SurveillanceAlarm> processSurveillanceAlarm(String body) {
        if (body != null) {
            ResponseModel responseModel = JSONObject.parseObject(body, ResponseModel.class);
            if (responseModel != null && responseModel.getResult() != null) {
               List<SurveillanceAlarm> tempList = JSONArray.parseArray(responseModel.getResult(), SurveillanceAlarm.class);
                if (tempList != null && tempList.size() > 0) {
                    logger.info("StranageAlarm num is ==>{}", tempList.size());
                    return tempList;
                }
            }
        }
        return null;
    }

   /* public AccessRecordModel processStrangeRecord(SurveillanceAlarm surveillanceAlarm) {
        AccessRecordModel accessRecordModel = new AccessRecordModel();
        accessRecordModel.setClasses("? ? ?");
        accessRecordModel.setName("陌生人");
        accessRecordModel.setType("未注册");
        accessRecordModel.setPass_time(new Date(surveillanceAlarm.getTimestamp() * 1000L));
        accessRecordModel.setImageId(surveillanceAlarm.getFace_image_id());
        accessRecordModel.setDeviceId(surveillanceAlarm.getDevice_id());
        return accessRecordModel;
    }*/
}
