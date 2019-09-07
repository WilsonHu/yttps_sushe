package com.eservice.iot.model.web;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @program:yttps_sushe
 * @description:前端显示通行记录
 * @author:yuhan_xie
 * @create:2019/7/13:11:55
 */
public class AccessRecordModel {

    // 姓名
    private String name;
    //班级
    private String classes;
    //通过时间
    private Date pass_time;
    // 通行状态：进，出，未注册，黑名单
    private String type;

    private String imageId;

    private String deviceId;

    private String personId;

    private List<String> tagIds;


    public List<String> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<String> tagIds) {
        this.tagIds = tagIds;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    public Date getPass_time() {
        return pass_time;
    }

    public void setPass_time(Date pass_time) {
        this.pass_time = pass_time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
