package com.eservice.iot.web;

import com.alibaba.fastjson.JSON;
import com.eservice.iot.core.Result;
import com.eservice.iot.core.ResultGenerator;
import com.eservice.iot.model.floor_device.FloorDevice;
import com.eservice.iot.service.FloorDeviceService;
import com.eservice.iot.service.impl.FloorDeviceServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Class Description: xxx
 *
 * @author Mr.Zhang
 * @date 2019/07/14.
 */
@RestController
@RequestMapping("/floor/device")
public class FloorDeviceController {
    @Resource
    private FloorDeviceServiceImpl floorDeviceService;

    @PostMapping("/add")
    public Result add(String jsonData) {
        FloorDevice floorDevice= JSON.parseObject(jsonData,FloorDevice.class);
        floorDeviceService.save(floorDevice);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        floorDeviceService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(String jsonData) {
        FloorDevice floorDevice= JSON.parseObject(jsonData,FloorDevice.class);
        floorDeviceService.update(floorDevice);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam @NotNull Integer id) {
        FloorDevice floorDevice = floorDeviceService.findById(id);
        return ResultGenerator.genSuccessResult(floorDevice);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<FloorDevice> list = floorDeviceService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/getDevice")
    public Result getDevice(String floorNo) {
       Condition condition=new Condition(FloorDevice.class);
       condition.createCriteria().andEqualTo("floorNo",floorNo);
       List<FloorDevice> list=floorDeviceService.findByCondition(condition);
        return ResultGenerator.genSuccessResult(list);
    }
}
