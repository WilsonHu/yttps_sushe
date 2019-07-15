package com.eservice.iot.service.park;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eservice.iot.model.ResponseModel;
import com.eservice.iot.model.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 人脸质量检测
 */
@Component
public class ImageService {
    private final  static Logger logger = LoggerFactory.getLogger(ImageService.class);

    @Value("${park_base_url}")
    private String PARK_BASE_URL;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TokenService tokenService;

    private String token;

    /**
     * 图片质量检查
     * @param img
     * @return  true Or error info
     */
    public String qualityVerify(String img ) {
        if (tokenService != null) {
            if (token == null) {
                token = tokenService.getToken();
            }
            if (token != null) {
                    List demo = new ArrayList();
                    demo.add(img);
                    HttpHeaders headers = new HttpHeaders();
                    headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
                    headers.add(HttpHeaders.AUTHORIZATION, token);
                    HttpEntity httpEntity = new HttpEntity<>(JSON.toJSONString(demo), headers);
                    ResponseEntity<String> responseEntity = restTemplate.postForEntity(PARK_BASE_URL + "/image_quality/verify", httpEntity, String.class);
                    String body = responseEntity.getBody();
                    ResponseModel responseModel = JSONObject.parseObject(body, ResponseModel.class);
                    if(responseModel.getRtn()==0){
                        String result = responseModel.getResult();
                        Map<String, Object> imageMap = JSONObject.parseObject(result.substring(result.indexOf("[") + 1, result.indexOf("]")));
                        for (Map.Entry<String, Object> entry : imageMap.entrySet()) {
                            switch (entry.getKey()) {
                                case "pixel_too_small":
                                case "no_face":
                                case "multi_face":
                                    if(Boolean.parseBoolean(entry.getValue().toString())){
                                        return entry.getKey();
                                    }
                                default:
                                    break;
                            }
                        }
                        return "true";
                    }else {
                        logger.error(""+responseModel.getMessage());
                        return  responseModel.getMessage();
                    }
            }
        }
        return "Port or service not started";
    }

    /**
     * 根据image_id,获取图片base64
     * @param imageId
     * @return base64 Or null
     */
    public byte[] getImageById(String imageId) {
        if (token == null && tokenService != null) {
            token = tokenService.getToken();
        }
        if (token != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.ACCEPT, "application/json");
            headers.add(HttpHeaders.AUTHORIZATION, token);
            HttpEntity entity = new HttpEntity(headers);
            try {
                ResponseEntity<byte[]> responseEntity = restTemplate.exchange(PARK_BASE_URL + "/image/" + imageId, HttpMethod.GET, entity, byte[].class);
                if (responseEntity.getStatusCodeValue() == ResponseCode.OK) {
                    byte[] body = responseEntity.getBody();
                    if (body != null) {
                        return  body;
                    }
                }
            } catch (HttpClientErrorException exception) {
                if (exception.getStatusCode().value() == ResponseCode.TOKEN_INVALID) {
                    token = tokenService.getToken();
                    if (token != null) {
                        getImageById(imageId);
                    }
                }
            }
        }
        return null;
    }
}
