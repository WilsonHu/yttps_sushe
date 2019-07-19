package com.eservice.iot.model.park;

import com.eservice.iot.model.park.Person;

import java.util.List;

/**
 * Class Description:
 *
 * @author Wilson Hu
 * @date 8/15/2018
 */
public class VisitRecord {

    /**
     * score : 92.72464909912985
     * device_id : 01
     * person : {"tag_id_list":["5b6ac6f4a62882389d20b34d"],"upload_time":1534185428,"person_information":{"birthday":"2018-08-14","phone":"13675874370","name":"嘟嘟","company":"","id":"002","employed_date":"2018-08-14"},"face_list":[{"face_image_id":"5b71cfd4a628820cde1e4216","scene_image_id":"5b71cfd4a628820cde1e4215","face_id":"9204231738438451209"}],"identity":"STAFF","person_id":"5b71cfd4a628820cde1e4217"}
     * identity : STAFF
     * track_id : 1534295402675
     * face_image_id : 5b737d6b6a128e301b0f9808
     * scene_image_id : 5b737d6b6a128e301b0f9809
     * face_id : 9204231738438451209
     * timestamp : 1534295403
     * track_id : string
     * 	access_policy_id_list : [ "string" ],
     * 	pass_result : "UNKNOWN",  ///UNKNOWN, PASS, NOT_PASS, CARD_PASS, CARD_NOT_PASS
     */
    private String card_number;
    private String device_id;//设备id
    private String face_id;
    private String face_image_id;
    private String identity;
    private String location;
    private Person person;
    private String record_type;
    private String scene_image_id;
    private double score;
    private int timestamp;//截取时间
    private String track_id;
    private List<String> access_policy_id_list;
    private String pass_result;

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTrack_id() {
        return track_id;
    }

    public void setTrack_id(String track_id) {
        this.track_id = track_id;
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

    public List<String> getAccess_policy_id_list() {
        return access_policy_id_list;
    }

    public void setAccess_policy_id_list(List<String> access_policy_id_list) {
        this.access_policy_id_list = access_policy_id_list;
    }

    public String getPass_result() {
        return pass_result;
    }

    public void setPass_result(String pass_result) {
        this.pass_result = pass_result;
    }

    public boolean equals(String[] devices){
        for (String device: devices ) {
            if(device.equals(device_id)){
                return true;
            }
        }
        return false;
    }
}
