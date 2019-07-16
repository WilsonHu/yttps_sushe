package com.eservice.iot.service.park;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eservice.iot.model.ResponseModel;
import com.eservice.iot.model.park.Tag;
import com.eservice.iot.model.ResponseCode;
import com.eservice.iot.util.Constant;
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
public class TagService {

    private final static Logger logger = LoggerFactory.getLogger(TagService.class);

    @Value("${park_base_url}")
    private String PARK_BASE_URL;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TokenService tokenService;

    /**
     * Token
     */
    private String token;

    /**
     * 全部tag列表
     */
    private ArrayList<Tag> allTagList = new ArrayList<>();

    /**
     * 访客tag
     */
    private List<Tag> visitorTagList = new ArrayList<>();
    /**
     * 员工tag
     */
    private List<Tag> staffTagList = new ArrayList<>();

    private List<Tag> floorTags = new ArrayList<>();
    /**
     * 一分钟更新一次TAG
     */
    @Scheduled(initialDelay = 1000,fixedRate = 1000 * 60)
    public void fetchTags() {
        if (token == null && tokenService != null) {
            token = tokenService.getToken();
        }
        if (token != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.ACCEPT, "application/json");
            headers.add("Authorization", token);
            HttpEntity entity = new HttpEntity(headers);
            try {
                ResponseEntity<String> responseEntity = restTemplate.exchange(PARK_BASE_URL + "/tags?size=0", HttpMethod.GET, entity, String.class);
                if (responseEntity.getStatusCodeValue() == ResponseCode.OK) {
                    String body = responseEntity.getBody();
                    if (body != null) {
                        processTagResponse(body);
                    } else {
                        fetchTags();
                    }
                }
            } catch (HttpClientErrorException errorException) {
                if (errorException.getStatusCode().value() == ResponseCode.TOKEN_INVALID) {
                    //token失效,重新获取token后再进行数据请求
                    token = tokenService.getToken();
                    if (token != null) {
                        fetchTags();
                    }
                }
            }
        }
    }

    private void processTagResponse(String body) {
        ResponseModel responseModel = JSONObject.parseObject(body, ResponseModel.class);
        if (responseModel != null && responseModel.getResult() != null) {
            ArrayList<Tag> tmpList = (ArrayList<Tag>) JSONArray.parseArray(responseModel.getResult(), Tag.class);
            if (tmpList != null &&tmpList.size()>0) {
                ArrayList<Tag> visitorTagList = new ArrayList<>();
                ArrayList<Tag> staffTagList = new ArrayList<>();
                for (Tag tag : tmpList) {
                    for (String str : tag.getVisible_identity()) {
                        if (Constant.VISITOR.equals(str)) {
                            visitorTagList.add(tag);
                        }
                        if (Constant.STAFF.equals(str)) {
                            staffTagList.add(tag);
                        }
                        if(tag.getTag_name().indexOf("号")!=-1){
                            floorTags.add(tag);
                        }
                    }
                }
                if (this.allTagList.size() != tmpList.size()) {
                    logger.info("The number of allTagList：{} ==> {}", this.allTagList.size(), tmpList.size());
                    logger.info("The number of staffTagList：{} ==> {}", this.staffTagList.size(), staffTagList.size());
                    logger.info("The number of visitorTagList：{} ==> {}", this.visitorTagList.size(), visitorTagList.size());
                }
                this.allTagList = tmpList;
                this.staffTagList = staffTagList;
                this.visitorTagList = visitorTagList;
            }
        }
    }

    public boolean createTag(String name, String identity,Map mate) {
        if (token == null && tokenService != null) {
            token = tokenService.getToken();
        }
        if (token != null) {
            HashMap<String, Object> postParameters = new HashMap<>();
            ArrayList<Tag> tagList = new ArrayList<>();
            Tag tag = new Tag();
            tag.setTag_name(name);
            if (mate != null) {
                tag.setMeta(mate);
            }

            ArrayList<String> identityList = new ArrayList<>();
            identityList.add(identity);
            tag.setVisible_identity(identityList);
            tagList.add(tag);
            postParameters.put("tag_list", tagList);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
            headers.add(HttpHeaders.AUTHORIZATION, tokenService.getToken());
            HttpEntity httpEntity = new HttpEntity<>(JSON.toJSONString(postParameters), headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(PARK_BASE_URL + "/tags", httpEntity, String.class);
        /*
{
  "message": "string",
  "result": [
    {
      "message": "string",
      "result": {
        "count": 0,
        "create_time": 0,
        "meta": {
          "additionalProp1": {},
          "additionalProp2": {},
          "additionalProp3": {}
        },
        "tag_id": "string",
        "tag_name": "string",
        "visible_identity": [
          "STAFF"
        ]
      },
      "rtn": 0
    }
  ],
  "rtn": 0
}
       */
            if (responseEntity.getStatusCodeValue() == ResponseCode.OK) {
                return true;
            }
        }
        return false;
    }

    public boolean deleteTag(String id) {
        if (token == null && tokenService != null) {
            token = tokenService.getToken();
        }
        if (token != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.ACCEPT, "application/json");
            headers.add("Authorization", token);
            HttpEntity entity = new HttpEntity(headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(PARK_BASE_URL + "/tags/" + id, HttpMethod.DELETE, entity, String.class);
        /*
{
  "message": "string",
  "result": {},
  "rtn": 0
}
         */
            if (responseEntity.getStatusCodeValue() == ResponseCode.OK) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据名称获取id，名称不存在则新建
     * 多用于新增员工
     * @param tagNames
     * @return
     */
    public ArrayList<String> getDepartmentId(String[] tagNames) {
        ArrayList<String> idList = new ArrayList<>();
        for (String str : tagNames) {
            isExistStaff(str);//判断标签是否存在，不存在则先新增
            for (Tag tag : visitorTagList) {
                if (str.equals(tag.getTag_name())) {
                    idList.add(tag.getTag_id());
                }
            }
        }
        return idList;
    }

    /**
     * 根据名称获取id，名称不存在则新建
     * 多用于新增访客
     * @param tagNames
     * @return
     */
    public ArrayList<String> getVisitorId(String[] tagNames) {
        ArrayList<String> idList = new ArrayList<>();
        for (String str : tagNames) {
            isExistVisitor(str);//判断标签是否存在，不存在则先新增
            for (Tag tag : visitorTagList) {
                if (str.equals(tag.getTag_name())) {
                    idList.add(tag.getTag_id());
                }
            }
        }
        return idList;
    }

    public void isExistStaff(String tagName) {
        boolean isExist = false;
        for (Tag tag : staffTagList) {
            if (tagName.equals(tag.getTag_name())) {
                isExist = true;
            }
        }
        if (!isExist) {
            createTag(tagName, Constant.STAFF,null);
            fetchTags();
        }
    }

    public void isExistVisitor(String tagName) {
        boolean isExist = false;
        for (Tag tag : visitorTagList) {
            if (tagName.equals(tag.getTag_name())) {
                isExist = true;
            }
        }
        if (!isExist) {
            createTag(tagName, Constant.VISITOR,null);
            fetchTags();
        }
    }

    /**
     * 根据tagId获取tagName
     * @param tagId
     * @return tagId or ""
     */
    public String getTagName(String tagId) {
        String tagName = "";
        for(Tag tag : allTagList){
            if(tagId.equals(tag.getTag_id())){
                tagName=tag.getTag_name();
                break;
            }
        }
        return tagName;
    }

    /**
     * 根据tagIds获取tagNames
     * @param tagIds
     * @return tagIds  or tagIds.size = 0
     */
    public ArrayList<String> getTagName(List<String> tagIds){
        ArrayList<String> tagNames = new ArrayList<>();
        for(String tagId : tagIds){
            for(Tag tag : allTagList){
                if(tagId.equals(tag.getTag_id())){
                    tagNames.add(tag.getTag_name());
                    break;
                }
            }
        }
        return tagNames;
    }

    public ArrayList<Tag> getAllTagList() {
        return allTagList;
    }

    public List<Tag> getVisitorTagList() {
        return visitorTagList;
    }

    public List<Tag> getStaffTagList() {
        return staffTagList;
    }

    public List<Tag> getFloorTags() {
        return floorTags;
    }
}
