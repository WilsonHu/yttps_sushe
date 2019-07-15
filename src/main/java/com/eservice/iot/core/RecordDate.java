package com.eservice.iot.core;

import com.eservice.iot.model.park.VisitRecord;

import java.util.*;

public class RecordDate {
    private VisitRecord mapAlert;
    private Date date;
    private boolean isKey=true;

    public boolean isKey() {
        return isKey;
    }

    public void setKey(boolean key) {
        isKey = key;
    }

    public VisitRecord getMapAlert() {
        return mapAlert;
    }

    public void setMapAlert(VisitRecord mapAlert) {
        this.mapAlert = mapAlert;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    //将两个集合合并并且去重
    public static List mergeList(List a, List b){
        Set<String> set = new HashSet<String>();
        set.addAll(a);
        set.addAll(b);
        List<String> c = new ArrayList<String>(set);
        return c;
    }
}
