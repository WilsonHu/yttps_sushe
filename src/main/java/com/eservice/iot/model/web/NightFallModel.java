package com.eservice.iot.model.web;

import java.io.Serializable;
import java.util.List;

public class NightFallModel  implements Serializable {

    private String floorNo;


    private List<AccessRecordModel> absentees;

    public String getFloorNo() {
        return floorNo;
    }

    public void setFloorNo(String floorNo) {
        this.floorNo = floorNo;
    }

    public List<AccessRecordModel> getAbsentees() {
        return absentees;
    }

    public void setAbsentees(List<AccessRecordModel> absentees) {
        this.absentees = absentees;
    }
}
