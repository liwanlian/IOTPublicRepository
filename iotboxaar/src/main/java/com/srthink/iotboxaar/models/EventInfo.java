package com.srthink.iotboxaar.models;

/**
 * @author liwanlian
 * @date 2021/7/13 10:28
 */
public class EventInfo {
    public String deviceCode;//设备名称
    public String FaultNotification;

    public EventInfo(String deviceCode, String faultNotification) {
        this.deviceCode = deviceCode;
        FaultNotification = faultNotification;
    }
}
