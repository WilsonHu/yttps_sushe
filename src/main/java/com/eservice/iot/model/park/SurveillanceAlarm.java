package com.eservice.iot.model.park;

import java.util.List;

/**
 * @program: yttps_sushe
 * @description: 布控报警
 * @author: yuhan_xie
 * @create: 2019/9/12 15:33
 */
public class SurveillanceAlarm {

    private String card_number;
    private String device_id;//设备id
    private String face_id;
    private String face_image_id;
    private String identity;
    private Person person;
    private String record_type;
    private String scene_image_id;
    private double score;
    private List<String> surveillance_policy_id_list;
    private int timestamp;//截取时间
    private String type;

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getRecord_type() {
        return record_type;
    }

    public void setRecord_type(String record_type) {
        this.record_type = record_type;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getFace_image_id() {
        return face_image_id;
    }

    public void setFace_image_id(String face_image_id) {
        this.face_image_id = face_image_id;
    }

    public String getScene_image_id() {
        return scene_image_id;
    }

    public void setScene_image_id(String scene_image_id) {
        this.scene_image_id = scene_image_id;
    }

    public String getFace_id() {
        return face_id;
    }

    public void setFace_id(String face_id) {
        this.face_id = face_id;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getSurveillance_policy_id_list() {
        return surveillance_policy_id_list;
    }

    public void setSurveillance_policy_id_list(List<String> surveillance_policy_id_list) {
        this.surveillance_policy_id_list = surveillance_policy_id_list;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean equals(String[] devices) {
        for (String device : devices) {
            if (device.equals(device_id)) {
                return true;
            }
        }
        return false;
    }
}
