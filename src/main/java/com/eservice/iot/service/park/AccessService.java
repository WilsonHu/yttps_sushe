package com.eservice.iot.service.park;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eservice.iot.model.ResponseCode;
import com.eservice.iot.model.ResponseModel;
import com.eservice.iot.model.floor_device.FloorDevice;
import com.eservice.iot.model.park.AccessRecord;
import com.eservice.iot.model.web.AccessRecordModel;
import com.eservice.iot.service.DormService;
import com.eservice.iot.service.FloorDeviceService;
import com.eservice.iot.util.RedisUtil;
import com.eservice.iot.util.Util;
import io.swagger.models.auth.In;
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

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
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
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private DormService dormService;

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * Token
     */
    private String token;

    /**
     * 查询开始时间,单位为秒
     */
    private Long queryStartTime = 0L;
    /**
     * 每秒查询一次考勤信息
     */
  @Scheduled(initialDelay = 5000, fixedRate = 1000)
    public void fetchSignInScheduled() {
      if (token == null) {
          token = tokenService.getToken();
      }
      if (token != null) {
          if(queryStartTime==0L){
              //设备重启，获取redi记录中的最后一个时间,去掉当前那一秒的数据
              queryStartTime=getRedisTime()+1;
          }
          querySignInStaff(queryStartTime);
      }
  }

    private void querySignInStaff(Long startTime ) {
        if (token == null) {
            token = tokenService.getToken();
        }
        if(token!=null) {
            HashMap<String, Object> postParameters = new HashMap<>();
//        ///考勤记录查询开始时间
            postParameters.put("start_timestamp", startTime);
//        ///考勤记录查询结束时间

              Long  queryEndTime = System.currentTimeMillis() / 1000L;
            postParameters.put("end_timestamp", queryEndTime);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
            headers.add(HttpHeaders.AUTHORIZATION, token);
            HttpEntity httpEntity = new HttpEntity<>(JSON.toJSONString(postParameters), headers);
            try {
                ResponseEntity<String> responseEntity = restTemplate.postForEntity(PARK_BASE_URL + "/access/record", httpEntity, String.class);
                if (responseEntity.getStatusCodeValue() == ResponseCode.OK) {
                    String body = responseEntity.getBody();
                    if (body != null) {
                        ResponseModel responseModel = JSONObject.parseObject(body, ResponseModel.class);
                        if (responseModel != null && responseModel.getResult() != null) {
                            ArrayList<AccessRecord> tempList = (ArrayList<AccessRecord>) JSONArray.parseArray(responseModel.getResult(), AccessRecord.class);
                            if (tempList != null && tempList.size() > 0) {
                                dormService.processStaffSign(tempList);
                                //本次结束时间的前一秒作为下次开始的时间
                                queryStartTime = queryEndTime - 1;
                            }
                        }
                    }
                }
            } catch (HttpClientErrorException exception) {
                if (exception.getStatusCode().value() == ResponseCode.TOKEN_INVALID) {
                    //token失效,重新获取token后再进行数据请求
                    token = tokenService.getToken();
                    querySignInStaff(startTime);
                }
                exception.printStackTrace();
                logger.error(exception.getMessage());
            }
        }
    }


    public int querySignInCount(Long startTime,Long queryEndTime){
        if (token == null) {
            token = tokenService.getToken();
        }
        if(token!=null) {
            HashMap<String, Object> postParameters = new HashMap<>();
//        ///考勤记录查询开始时间
            postParameters.put("start_timestamp", startTime);
//        ///考勤记录查询结束时间
            postParameters.put("end_timestamp", queryEndTime);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
            headers.add(HttpHeaders.AUTHORIZATION, token);
            HttpEntity httpEntity = new HttpEntity<>(JSON.toJSONString(postParameters), headers);
            try {
                ResponseEntity<String> responseEntity = restTemplate.postForEntity(PARK_BASE_URL + "/access/record/count", httpEntity, String.class);
                if (responseEntity.getStatusCodeValue() == ResponseCode.OK) {
                    String body = responseEntity.getBody();
                    if (body != null) {
                        ResponseModel responseModel = JSONObject.parseObject(body, ResponseModel.class);
                        try {
                            return Integer.parseInt(responseModel.getResult());
                        }catch (Exception e){
                            logger.error("Result: {}, Massage: {}",responseModel.getResult(),responseModel.getMessage());
                        }
                    }
                }
            } catch (HttpClientErrorException exception) {
                if (exception.getStatusCode().value() == ResponseCode.TOKEN_INVALID) {
                    //token失效,重新获取token后再进行数据请求
                    token = tokenService.getToken();
                   return querySignInCount(startTime,queryEndTime);
                }
                exception.printStackTrace();
                logger.error(exception.getMessage());
            }
        }
        return -1;
    }
    /*
     * 获取已记录的通行记录中最晚时间
     */
    private Long  getRedisTime(){

        Iterator iterator = redisUtil.sGet("access_set").iterator();
        long time = 0L;
        while (iterator.hasNext()) {
            AccessRecordModel accessRecord = (AccessRecordModel)iterator.next();
            if(accessRecord.getPass_time().getTime()>time){
                time=accessRecord.getPass_time().getTime();
            }
        }
        if(time==0L){
            time = Util.getDateStartTime().getTime();
        }
        logger.info(formatter.format(new Date(time)));
        return time/1000L;
    }


}
