package com.eservice.iot.model.web;

/**
 * @program: yttps_sushe
 * @description: 晚归考勤   照片ID，姓名，考勤时间，考勤状态
 * @author: yuhan_xie
 * @create: 2019/7/13 21:11
 */
public class NightAttendance {

    private String face_id;
    private String name;
    private String time;
    private String type;

    public String getFace_id() {
        return face_id;
    }

    public void setFace_id(String face_id) {
        this.face_id = face_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
