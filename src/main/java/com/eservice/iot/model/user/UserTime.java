package com.eservice.iot.model.user;

import java.util.Date;
import java.util.Map;

public class UserTime {
    private String name;
    private String tagList;
    Map<Integer,Date[]> day_time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTagList() {
        return tagList;
    }

    public void setTagList(String tagList) {
        this.tagList = tagList;
    }

    public Map<Integer, Date[]> getDay_time() {
        return day_time;
    }

    public void setDay_time(Map<Integer, Date[]> day_time) {
        this.day_time = day_time;
    }
}
