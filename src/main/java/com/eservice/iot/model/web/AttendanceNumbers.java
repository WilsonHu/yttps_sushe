package com.eservice.iot.model.web;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * @program: yttps_sushe
 * @description: 12个节点（考勤人数，在寝人数，外出人数）
 * @author: yuhan_xie
 * @create: 2019/7/13:12:02
 */
public class AttendanceNumbers implements Serializable {

    public int[] attendanceNumbers;
    public int[] inDormitories;
    public int[] outDormitories;

    public AttendanceNumbers(){
        attendanceNumbers = new int[12];
        inDormitories = new int[12];
        outDormitories = new int[12];
    }

    public int[] getAttendanceNumbers() {
        return attendanceNumbers;
    }

    public void setAttendanceNumbers(int[] attendanceNumbers) {
        this.attendanceNumbers = attendanceNumbers;
    }

    public int[] getInDormitories() {
        return inDormitories;
    }

    public void setInDormitories(int[] inDormitories) {
        this.inDormitories = inDormitories;
    }

    public int[] getOutDormitories() {
        return outDormitories;
    }

    public void setOutDormitories(int[] outDormitories) {
        this.outDormitories = outDormitories;
    }


    public String show(){
        return String.format(" attendanceNumbers : %s , inDormitories : %s , outDormitories : %s ",JSONObject.toJSON(attendanceNumbers),JSONObject.toJSON(inDormitories),JSONObject.toJSON(outDormitories));
    }
}
