package com.eservice.iot.service.park;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eservice.iot.model.ResponseCode;
import com.eservice.iot.model.ResponseModel;
import com.eservice.iot.model.park.FaceListBean;
import com.eservice.iot.model.park.PersonInformation;
import com.eservice.iot.model.park.Staff;
import com.eservice.iot.model.park.UpdateFace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;


/**
 * @author HT
 */
@Component
public class StaffService {

    private final static Logger logger = LoggerFactory.getLogger(StaffService.class);

    @Value("${park_base_url}")
    private String PARK_BASE_URL;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Token
     */
    private String token;
    /**
     * 员工列表
     */
    private ArrayList<Staff> staffList = new ArrayList<>();

    @Autowired
    private TokenService tokenService;
  
    /**
     * 每10分钟获取一次需要签到的员工信息
     */
    @Scheduled(initialDelay = 2000, fixedRate = 1000*60*10)
    public void fetchStaffScheduled() {
        if (token == null && tokenService != null) {
            token = tokenService.getToken();
        }
        if (token != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.ACCEPT, "application/json");
            headers.add(HttpHeaders.AUTHORIZATION, token);
            HttpEntity entity = new HttpEntity(headers);
            try {
                String url = PARK_BASE_URL + "/staffs?";
                url += "page=0&size=0";
                ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
                if (responseEntity.getStatusCodeValue() == ResponseCode.OK) {
                    String body = responseEntity.getBody();
                    if (body != null) {
                        processStaffResponse(body);
                    } else {
                        fetchStaffScheduled();
                    }
                }
            } catch (HttpClientErrorException exception) {
                if (exception.getStatusCode().value() == ResponseCode.TOKEN_INVALID) {
                    token = tokenService.getToken();
                    if (token != null) {
                        fetchStaffScheduled();
                    }
                }
            }
        }
    }

    private void processStaffResponse(String body) {
        ResponseModel responseModel = JSONObject.parseObject(body, ResponseModel.class);
        if (responseModel != null && responseModel.getResult() != null) {
            ArrayList<Staff> tmpList = (ArrayList<Staff>) JSONArray.parseArray(responseModel.getResult(), Staff.class);
            if (tmpList != null && tmpList.size() != 0) {
                if (staffList.size()!=tmpList.size()) {
                    logger.info("The number of staff：{} ==> {}", staffList.size(), tmpList.size());
                }
                staffList = tmpList;
            }
        }
    }

    public boolean deleteStaff(String staffId) {
        if (token == null && tokenService != null) {
            token = tokenService.getToken();
        }
        if (token != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.ACCEPT, "application/json");
            headers.add(HttpHeaders.AUTHORIZATION, token);
            HttpEntity entity = new HttpEntity(headers);
            try {
                ResponseEntity<String> responseEntity = restTemplate.exchange(PARK_BASE_URL + "/staffs/" + staffId, HttpMethod.DELETE, entity, String.class);
                if (responseEntity.getStatusCodeValue() == ResponseCode.OK) {
                    String body = responseEntity.getBody();
                    if (body != null) {
                        ResponseModel responseModel = JSONObject.parseObject(body, ResponseModel.class);
                        if (responseModel != null && responseModel.getRtn() == 0) {
                            logger.info("id ："+staffId+"\t"+responseModel.getMessage());
                            return  true;
                        }
                    }
                }
            } catch (HttpClientErrorException exception) {
                if (exception.getStatusCode().value() == ResponseCode.TOKEN_INVALID) {
                    token = tokenService.getToken();
                    if (token != null) {
                        deleteStaff(staffId);
                    }
                }
            }
        }
        return false;
    }

    /**
     * 添加到园区
     *
     * @param map
     * @return
     */
    public boolean createStaff(Map map) {
        if (token == null && tokenService != null) {
            token = tokenService.getToken();
        }
        if (token != null) {
            HashMap<String, Object> postParameters = new HashMap<>();
            ArrayList<Staff> staffList = new ArrayList<>();
            Staff staff = new Staff();
            PersonInformation personInformation = new PersonInformation();
            personInformation.setId(map.get("id").toString());
            personInformation.setName(map.get("name").toString());
            personInformation.setBirthday(map.get("birthday").toString());
            personInformation.setEmployed_date(map.get("employed_date").toString());

            ArrayList<String> list = new ArrayList<>();
            staff.setCard_numbers(list);
            staff.setFace_image_content_list((ArrayList<String>) map.get("image"));
            staff.setMeta((Map) map.get("mate"));
            staff.setPerson_information(personInformation);
            staff.setTag_id_list((List<String>) map.get("tag"));
            staffList.add(staff);

            postParameters.put("staff_list", staffList);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
            headers.add(HttpHeaders.AUTHORIZATION, token);
            HttpEntity httpEntity = new HttpEntity<>(JSON.toJSONString(postParameters), headers);
            try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(PARK_BASE_URL + "/staffs", httpEntity, String.class);
            if (responseEntity.getStatusCodeValue() == ResponseCode.OK) {
                return  examineBody(responseEntity.getBody(),map.get("name").toString());
            }
            }catch (HttpClientErrorException exception) {
                if (exception.getStatusCode().value() == ResponseCode.TOKEN_INVALID) {
                    token = tokenService.getToken();
                    if (token != null) {
                        createStaff( map);
                    }
                }
            }
        }
        return false;
    }

    /**
     * 修改人员信息
     *
     * @param map
     * @return
     */
    public boolean updateStaff(Map map, List<String> faceId,String staffId) {
        if (token == null && tokenService != null) {
            token = tokenService.getToken();
        }
        if (token != null) {
            Staff staff = new Staff();
            PersonInformation personInformation = new PersonInformation();
            personInformation.setId(map.get("id").toString());
            personInformation.setName(map.get("name").toString());
            personInformation.setBirthday(map.get("birthday").toString());
            personInformation.setEmployed_date(map.get("employed_date").toString());

            staff.setPerson_information(personInformation);
            ArrayList<String> list = new ArrayList<>();
            staff.setCard_numbers(list);
            staff.setMeta((Map) map.get("mate"));
            staff.setTag_id_list((List<String>) map.get("tag"));

            UpdateFace updateFace = new UpdateFace();
            updateFace.setDelete_face_id_list(faceId);//原有的人脸图片id
            updateFace.setInsert_face_image_content_list((List<String>) map.get("image"));//存入新的人脸图片
            updateFace.setEnforce(true);//强制更新
            staff.setUpdate_face(updateFace);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
            headers.add("Authorization", token);
            HttpEntity httpEntity = new HttpEntity<>(JSON.toJSONString(staff), headers);
            try {
                ResponseEntity<String> responseEntity = restTemplate.exchange(PARK_BASE_URL + "/staffs/" + staffId, HttpMethod.PUT, httpEntity, String.class);
                if (responseEntity.getStatusCodeValue() == ResponseCode.OK) {
                    return   examineBody(responseEntity.getBody(),map.get("name").toString());
                }
            } catch (HttpClientErrorException exception) {
                if (exception.getStatusCode().value() == ResponseCode.TOKEN_INVALID) {
                    token = tokenService.getToken();
                    if (token != null) {
                        updateStaff(map, faceId,staffId);
                    }
                }
            }
        }
        return false;
    }

    public String isExistToPark(String id,List<String> faceId) {
        String staffId=null;
        for (Staff temp : staffList) {
            if (temp.getPerson_information().getId().equals(id)) {
                staffId=temp.getStaffId();
                for (FaceListBean faceListBean : temp.getFace_list()) {
                    faceId.add(faceListBean.getFace_id());
                }
            }
        }
        return staffId;
    }

    public boolean examineBody(String body,String name){
        if (body != null) {
            ResponseModel responseModel = JSONObject.parseObject(body, ResponseModel.class);
            if (responseModel != null &&responseModel.getRtn() != 0) {
                responseModel = JSONObject.parseObject(responseModel.getResult(), ResponseModel.class);
                logger.error("姓名："+name+"\t"+responseModel.getMessage());
            }else {
                fetchStaffScheduled();
                return true;
            }
        }
        return false;
    }

    public ArrayList<Staff> getStaffAllList() {
        return staffList;
    }
}
