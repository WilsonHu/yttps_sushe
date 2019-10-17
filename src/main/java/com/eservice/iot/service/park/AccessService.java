package com.eservice.iot.service.park;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eservice.iot.model.ResponseCode;
import com.eservice.iot.model.ResponseModel;
import com.eservice.iot.model.park.VisitRecord;
import com.eservice.iot.model.user.User;
import com.eservice.iot.model.user.UserTime;
import com.eservice.iot.util.FileUtil;
import com.eservice.iot.util.PhotoDigestUtil;
import com.eservice.iot.util.Util;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
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
    @Autowired
    private TagService tagService;
    @Value("${score}")
    private int score;

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
                total = responseModel.getTotal();
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
                total = responseModel.getTotal();
                if (responseModel != null && responseModel.getResult() != null) {
                    List<VisitRecord> tempList = JSONArray.parseArray(responseModel.getResult(), VisitRecord.class);
                    if (tempList != null && tempList.size() > 0) {
                        /* ArrayList<VisitRecord> visitRecords = new ArrayList<>();
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

    /**
     * 查询学员最早以及最晚抓拍时间
     * @param startTime
     * @param endTime
     * @param type
     * @return
     */
    public List<UserTime> querySignInVisitor(Long startTime, Long endTime, String type) {
        if (token == null) {
            token = tokenService.getToken();
        }
        //存储所有人员通行记录
        ArrayList<VisitRecord> visitRecords = new ArrayList<>();
        //所有通过人员的信息
        ArrayList<VisitRecord> visitRecordPass = new ArrayList<>();
        //保存所有人员姓名
        ArrayList<String> mAllName = new ArrayList<>();
        //保存所有人员通过的时间
        ArrayList<Date> time = new ArrayList<>();
        if (token != null) {
            HashMap<String, Object> postParameters = new HashMap<>();
            postParameters.put("start_timestamp", startTime);
            postParameters.put("end_timestamp", endTime);
            ArrayList<String> identity = new ArrayList<>();
            identity.add("STAFF");
            postParameters.put("identity_list", identity);
            ArrayList<String> tmpTagList = new ArrayList<>();
            String tagId = tagService.getTagId(type);
            if (tagId == null) {
                postParameters.put("tag_id_list", tagId);
            } else {
                tmpTagList.add(tagId);
                postParameters.put("tag_id_list", tmpTagList);
            }
            logger.info("query tagName is ==>{}", tagId);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
            headers.add(HttpHeaders.AUTHORIZATION, tokenService.getToken());

            HttpEntity httpEntity = new HttpEntity<>(JSON.toJSONString(postParameters), headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(PARK_BASE_URL + "/visit_record/query", httpEntity, String.class);
            if (responseEntity.getStatusCodeValue() == ResponseCode.OK) {
                String body = responseEntity.getBody();
                if (body != null) {
                    ResponseModel responseModel = JSONObject.parseObject(body, ResponseModel.class);
                    if (responseModel != null && responseModel.getResult() != null) {
                        visitRecords = (ArrayList<VisitRecord>) JSONArray.parseArray(responseModel.getResult(), VisitRecord.class);
                        if (visitRecords != null) {
                            for (VisitRecord visitRecord : visitRecords) {
                                if (visitRecord.getScore()>score) {
                                    if (type != null && !type.equals("")) {
                                        for (String tagIds : visitRecord.getPerson().getTag_id_list()) {
                                            if (tagIds.equals(tagId)) {
                                                visitRecordPass.add(visitRecord);
                                                mAllName.add(visitRecord.getPerson().getPerson_information().getName());
                                            }
                                        }
                                    } else {
                                        visitRecordPass.add(visitRecord);
                                        mAllName.add(visitRecord.getPerson().getPerson_information().getName());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            logger.error("Token is null,remove tag error");
            token = tokenService.getToken();
            querySignInVisitor(startTime, endTime, type);
        }
        mAllName = noRepeat(mAllName);

        Map<String, List<Date>> empDate = new HashMap<>();
        for (String user : mAllName) {
            List<Date> dates = new ArrayList<>();
            for (VisitRecord userTime : visitRecordPass) {
                if (user.equals(userTime.getPerson().getPerson_information().getName())) {
                    dates.add(new Date(userTime.getTimestamp() * 1000L));
                }
            }
            empDate.put(user, dates);
        }

        int start = new Date(startTime * 1000L).getDate();
        int end = new Date(endTime * 1000L).getDate();
        List<UserTime> userTimes = new ArrayList<>();    //每个员工每天对应得签到时间
        for (Map.Entry<String, List<Date>> staff : empDate.entrySet()) {
            Map<Integer, Date[]> day = new HashMap<>(); //暂存当前员工本月每一天的最早和最晚时间
            for (int i = start; i <= end; i++) {
                List<Date> dates = new ArrayList<>();
                for (Date date : staff.getValue()) {
                    if (i == date.getDate()) {
                        dates.add(date);
                    }
                }
                if (dates.size() > 0) {
                    day.put(i, new Date[]{dates.get(0), dates.get(dates.size() - 1)});
                }
            }
            //存储人员最终的信息
            UserTime userTime = new UserTime();
            userTime.setName(staff.getKey());
            userTime.setDay_time(day);
            userTimes.add(userTime);
        }
        logger.info("Staff VistiRecord size ==>{}", userTimes.size());
        return userTimes;
    }

    /**
     * 去除重复元素
     *
     * @return
     */
    public ArrayList noRepeat(ArrayList obj) {
        if (obj != null) {
            for (int i = 0; i < obj.size() - 1; i++) {
                for (int j = obj.size() - 1; j > i; j--) {
                    if (obj.get(j).equals(obj.get(i))) {
                        obj.remove(j);
                    }
                }
            }
        } else {
            logger.warn("obj is null, NoRepeat is error");
        }
        return obj;
    }

    public Integer getTotal() {
        return total;
    }

}
