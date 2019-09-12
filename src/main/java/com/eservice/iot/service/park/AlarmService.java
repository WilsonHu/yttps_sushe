package com.eservice.iot.service.park;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eservice.iot.model.ResponseCode;
import com.eservice.iot.model.ResponseModel;
import com.eservice.iot.model.park.SurveillanceAlarm;
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

import java.util.HashMap;
import java.util.List;

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

    private String token;

    public List<SurveillanceAlarm> requestParkSurveillanceAlarm(HashMap<String, Object> postParameters) {
        if (token == null) {
            token = tokenService.getToken();
        }
        if (token != null) {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
                headers.add(HttpHeaders.AUTHORIZATION, token);
                HttpEntity httpEntity = new HttpEntity<>(JSON.toJSONString(postParameters), headers);
                ResponseEntity<String> responseEntity = restTemplate.postForEntity(PARK_BASE_URL + "/surveillance/alarm", httpEntity, String.class);
                return null;
            } catch (HttpClientErrorException exception) {
                if (exception.getStatusCode().value() == ResponseCode.TOKEN_INVALID) {
                    //token失效,重新获取token后再进行数据请求
                    token = tokenService.getToken();
                    return null;
                }
                logger.error(exception.getMessage());
            } catch (Exception exception) {
                exception.printStackTrace();
                logger.error(exception.getMessage());
            }
        }
        return null;
    }

    public List<SurveillanceAlarm> processSurveillanceAlarm(ResponseEntity<String> responseEntity) {
        if (responseEntity.getStatusCodeValue() == ResponseCode.OK) {
            String body = responseEntity.getBody();
            if (body != null) {
                ResponseModel responseModel = JSONObject.parseObject(body, ResponseModel.class);
                if (responseModel != null && responseModel.getResult() != null) {
                    List<SurveillanceAlarm> templist = JSONArray.parseArray(responseModel.getResult(), SurveillanceAlarm.class);
                    if (templist != null && templist.size() > 0) {
                        return templist;
                    }
                }
            }
        }
        return null;
    }


}
