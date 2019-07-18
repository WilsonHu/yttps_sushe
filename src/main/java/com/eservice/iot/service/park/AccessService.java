package com.eservice.iot.service.park;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eservice.iot.model.ResponseCode;
import com.eservice.iot.model.ResponseModel;
import com.eservice.iot.model.park.AccessRecord;
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

import java.util.*;

/**
 * @author HT
 */
@Component
public class AccessService {
    private final static Logger logger = LoggerFactory.getLogger(AccessService.class);

    @Value("${park_base_url}")
    private String PARK_BASE_URL;

    @Autowired
    private TokenService tokenService;
    @Autowired
    private RestTemplate restTemplate;

    private String token;

    /**
     * 查询指定设备id的该时间段的所有通行记录
     * @param startTime
     * @param queryEndTime
     * @param deviceIds
     * @return
     */
    public   ArrayList<AccessRecord> queryAccessStaffByTime(Long startTime,Long queryEndTime,List<String> deviceIds ) {
        if (token == null) {
            token = tokenService.getToken();
        }
        if (token != null) {
            HashMap<String, Object> postParameters = new HashMap<>();
            ///考勤记录查询开始时间
            postParameters.put("start_timestamp", startTime);
            ///考勤记录查询结束时间
            postParameters.put("end_timestamp", queryEndTime);
            ///指定设备
            if (deviceIds != null && deviceIds.size() > 0) {
                postParameters.put("device_id_list", deviceIds);
            }
            try {
               return requestParkAccessRecord(postParameters);
            } catch (HttpClientErrorException exception) {
                if (exception.getStatusCode().value() == ResponseCode.TOKEN_INVALID) {
                    //token失效,重新获取token后再进行数据请求
                    token = tokenService.getToken();
                    return queryAccessStaffByTime(startTime, queryEndTime, deviceIds);
                }
                logger.error(exception.getMessage());
            } catch (Exception exception) {
                exception.printStackTrace();
                logger.error(exception.getMessage());
            }
        }
        return null;
    }

    /**
     * 查询指定设备id的第page页的size个通行记录
     * @param endTime
     * @param size
     * @param deviceIds
     * @return
     */
    public  ArrayList<AccessRecord> queryAccessStaffByPaging(int page,Long endTime,int size,List<String> deviceIds,String name ) {
        if (token == null) {
            token = tokenService.getToken();
        }
        if (token != null) {
            HashMap<String, Object> postParameters = new HashMap<>();
            if (endTime!=null){
                postParameters.put("end_timestamp", endTime);
            }
            postParameters.put("size", size);
            if (page!=-1){
                postParameters.put("page",page);
            }
            if (name!=null&&name!=""){
                postParameters.put("fuzzy_name",name);
            }
            ///指定设备
            if (deviceIds != null && deviceIds.size() > 0) {
                postParameters.put("device_id_list", deviceIds);
            }
            try {
                return requestParkAccessRecord(postParameters);
            } catch (HttpClientErrorException exception) {
                if (exception.getStatusCode().value() == ResponseCode.TOKEN_INVALID) {
                    //token失效,重新获取token后再进行数据请求
                    token = tokenService.getToken();
                    return queryAccessStaffByPaging(page,endTime, size, deviceIds,name);
                }
                logger.error(exception.getMessage());
            } catch (Exception exception) {
                exception.printStackTrace();
                logger.error(exception.getMessage());
            }
        }
        return null;
    }

    private ArrayList<AccessRecord> requestParkAccessRecord(HashMap<String, Object> postParameters) throws  Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.add(HttpHeaders.AUTHORIZATION, token);
        HttpEntity httpEntity = new HttpEntity<>(JSON.toJSONString(postParameters), headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(PARK_BASE_URL + "/access/record", httpEntity, String.class);
        if (responseEntity.getStatusCodeValue() == ResponseCode.OK) {
            String body = responseEntity.getBody();
            if (body != null) {
                ResponseModel responseModel = JSONObject.parseObject(body, ResponseModel.class);
                if (responseModel != null && responseModel.getResult() != null) {
                    ArrayList<AccessRecord> tempList = (ArrayList<AccessRecord>) JSONArray.parseArray(responseModel.getResult(), AccessRecord.class);
                    if (tempList != null && tempList.size() > 0) {
                        return tempList;
                    }
                }
            }
        }
        return null;
    }
}
