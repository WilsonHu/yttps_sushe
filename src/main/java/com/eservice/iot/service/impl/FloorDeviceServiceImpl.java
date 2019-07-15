package com.eservice.iot.service.impl;

import com.eservice.iot.dao.FloorDeviceMapper;
import com.eservice.iot.model.floor_device.FloorDevice;
import com.eservice.iot.service.FloorDeviceService;
import com.eservice.iot.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.awt.datatransfer.FlavorListener;
import java.util.List;


/**
* Class Description: xxx
* @author Mr.Zhang
* @date 2019/07/14.
*/
@Service
@Transactional
public class FloorDeviceServiceImpl extends AbstractService<FloorDevice> implements FloorDeviceService {
    @Resource
    private FloorDeviceMapper floorDeviceMapper;


}
