package com.eservice.iot.model.faceImg;

/**
 * @program: yttps_sushe
 * @description: 通过url获取照片保存在本地
 * @author: yuhan_xie
 * @create: 2019/9/21 21:01
 */
public class FaceImage {

    private String floor;
    private String house;
    private String name;
    private String departments;
    private String studentNo;
    private String faceImgUrl;

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartments() {
        return departments;
    }

    public void setDepartments(String departments) {
        this.departments = departments;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public String getFaceImgUrl() {
        return faceImgUrl;
    }

    public void setFaceImgUrl(String faceImgUrl) {
        this.faceImgUrl = faceImgUrl;
    }
}
