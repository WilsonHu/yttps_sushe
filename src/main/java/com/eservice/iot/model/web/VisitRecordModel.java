package com.eservice.iot.model.web;

import java.util.Date;
import java.util.List;

/**
 * @program: yttps_sushe
 * @description: 最近一次抓拍记录实体类
 * @author: yuhan_xie
 * @create: 2019/10/9 12:33
 */
public class VisitRecordModel {

    private String name;
    private String type;
    private String imageId;
    private String personId;
    private Date time;
    private List<String> tagNames;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public List<String> getTagNames() {
        return tagNames;
    }

    public void setTagNames(List<String> tagNames) {
        this.tagNames = tagNames;
    }
}
