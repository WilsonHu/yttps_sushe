package com.eservice.iot.model.web;

/**
 * @program: yttps_sushe
 * @description: 总人数 考勤人数，在寝人数，外出人数
 * @author: yuhan_xie
 * @create: 2019/7/13:12:02
 */
public class AttendanceNumber {

    private int total;
    private int attendanceNum;
    private int inDormitory;
    private int outDormitory;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getAttendanceNum() {
        return attendanceNum;
    }

    public void setAttendanceNum(int attendanceNum) {
        this.attendanceNum = attendanceNum;
    }

    public int getInDormitory() {
        return inDormitory;
    }

    public void setInDormitory(int inDormitory) {
        this.inDormitory = inDormitory;
    }

    public int getOutDormitory() {
        return outDormitory;
    }

    public void setOutDormitory(int outDormitory) {
        this.outDormitory = outDormitory;
    }
}
