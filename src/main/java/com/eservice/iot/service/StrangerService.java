package com.eservice.iot.service;

import com.eservice.iot.model.park.SurveillanceAlarm;
import com.eservice.iot.service.park.AlarmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @program: yttps_sushe
 * @description: 陌生人报警
 * @author: yuhan_xie
 * @create: 2019/9/12 16:11
 */
@Service
public class StrangerService {
    private final static Logger logger = LoggerFactory.getLogger(StrangerService.class);

    @Autowired
    private AlarmService alarmService;

    public List<SurveillanceAlarm> getSurveillanceAlarm(String deviceId[], Long startTime, Long endTime) {
        List<String> deviceList = Arrays.asList(deviceId);
        HashMap<String, Object> postParameters = new HashMap<>();
        postParameters.put("device_id_list", deviceId);
        ///指定通行记录查询结束时间
        postParameters.put("start_timestamp", startTime);
        postParameters.put("end_timestamp", endTime);
        return null;
    }


}
