package com.eservice.iot.service.park;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eservice.iot.model.ResponseCode;
import com.eservice.iot.model.ResponseModel;
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
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
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
    @Autowired
    private ImageService imageService;



    private Integer total;

    private String token;



    public List<VisitRecord> requestParkAccessRecord(HashMap<String, Object> postParameters) {
        if (token == null) {
            token = tokenService.getToken();
        }
        if (token != null) {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
                headers.add(HttpHeaders.AUTHORIZATION, token);
                HttpEntity httpEntity = new HttpEntity<>(JSON.toJSONString(postParameters), headers);

                ResponseEntity<String> responseEntity = restTemplate.postForEntity(PARK_BASE_URL + "/access/record", httpEntity, String.class);
                return processAccessRecord(responseEntity);
            } catch (HttpClientErrorException exception) {
                if (exception.getStatusCode().value() == ResponseCode.TOKEN_INVALID) {
                    //token失效,重新获取token后再进行数据请求
                    token = tokenService.getToken();
                    return requestParkAccessRecord(postParameters);
                }
                logger.error(exception.getMessage());
            } catch (Exception exception) {
                exception.printStackTrace();
                logger.error(exception.getMessage());
            }
        }
        return null;
    }

    public List<VisitRecord> requestParkVisitRecord(HashMap<String, Object> postParameters) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.add(HttpHeaders.AUTHORIZATION, token);

        HttpEntity httpEntity = new HttpEntity<>(JSON.toJSONString(postParameters), headers);
        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(PARK_BASE_URL + "/visit_record/query", httpEntity, String.class);
            return processVisitRecord(responseEntity);
        } catch (HttpClientErrorException exception) {
            if (exception.getStatusCode().value() == ResponseCode.TOKEN_INVALID) {
                //token失效,重新获取token后再进行数据请求
                token = tokenService.getToken();
                return requestParkVisitRecord(postParameters);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            logger.error(exception.getMessage());
        }
        return null;
    }

    public List<VisitRecord> processAccessRecord(ResponseEntity<String> responseEntity) throws Exception {
        if (responseEntity.getStatusCodeValue() == ResponseCode.OK) {
            String body = responseEntity.getBody();
            if (body != null) {
                ResponseModel responseModel = JSONObject.parseObject(body, ResponseModel.class);
                total=responseModel.getTotal();
                if (responseModel != null && responseModel.getResult() != null) {
                    List<VisitRecord> tempList = JSONArray.parseArray(responseModel.getResult(), VisitRecord.class);
                    if (tempList != null && tempList.size() > 0) {
                        return tempList;
                    }
                }
            }
        }
        return null;
    }

    public List<VisitRecord> processVisitRecord(ResponseEntity<String> responseEntity) throws Exception {
        if (responseEntity.getStatusCodeValue() == ResponseCode.OK) {
            String body = responseEntity.getBody();
            if (body != null) {
                ResponseModel responseModel = JSONObject.parseObject(body, ResponseModel.class);
                total=responseModel.getTotal();
                if (responseModel != null && responseModel.getResult() != null) {
                    List<VisitRecord> tempList =  JSONArray.parseArray(responseModel.getResult(), VisitRecord.class);
                    if (tempList != null && tempList.size() > 0) {
                     /*    ArrayList<VisitRecord> visitRecords = new ArrayList<>();
                       for (VisitRecord visitRecord : tempList) {
                            if (visitRecord.getIdentity().equals("STRANGER")) {
                                if(visitRecord.getScore() == 0.0){
                                    String img = imageService.getImageById(visitRecord.getFace_image_id());
                                    String isTrue = imageService.qualityVerify(img);
                                    if("true".equals(isTrue)){
                                        visitRecords.add(visitRecord);
                                    }
                                    logger.info(" qualityVerify : {} ",isTrue);
                                }else if (visitRecord.getScore() < 75) {
                                  visitRecords.add(visitRecord);
                                }

                            }else {
                                visitRecords.add(visitRecord);
                            }
                            logger.info(" {} :  {} ", visitRecord.getTrack_id(), visitRecord.getScore());
                        }*/
                        return tempList;
                    }
                }
            }
        }
        return null;
    }


    public Integer getTotal() {
        return total;
    }

}
