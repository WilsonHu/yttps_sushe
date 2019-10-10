package com.eservice.iot.model.floor_device;

import javax.persistence.*;

@Table(name = "floor_device")
public class FloorDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 楼号
     */
    @Column(name = "floor_no")
    private Integer floorNo;

    /**
     * 设备ip
     */
    @Column(name = "device_id")
    private String deviceId;


    /**
     * 设备进出类型：0=>进，1=>出
     */
    private Integer type;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取楼号
     *
     * @return floor_no - 楼号
     */
    public Integer getFloorNo() {
        return floorNo;
    }

    /**
     * 设置楼号
     *
     * @param floorNo 楼号
     */
    public void setFloorNo(Integer floorNo) {
        this.floorNo = floorNo;
    }

    /**
     * 获取设备ip
     *
     * @return device_id - 设备ip
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * 设置设备ip
     *
     * @param deviceId 设备ip
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * 获取设备进出类型：0=>进，1=>出
     *
     * @return type - 设备进出类型：0=>进，1=>出
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置设备进出类型：0=>进，1=>出
     *
     * @param type 设备进出类型：0=>进，1=>出
     */
    public void setType(Integer type) {
        this.type = type;
    }

}