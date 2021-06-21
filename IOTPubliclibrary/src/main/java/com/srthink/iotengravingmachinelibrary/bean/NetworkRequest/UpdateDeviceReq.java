package com.srthink.iotengravingmachinelibrary.bean.NetworkRequest;

/**
 * @author liwanlian
 * @date 2021/6/15 16:19
 */
public class UpdateDeviceReq {

    /**
     * appkey : 
     * devName : 
     * productKey : 
     * taskId : 
     * updateFirmType : 
     * upgradeTime : 
     * version : 
     */

    public String appkey;
    public String devName;
    public String productKey;
    public String taskId;
    public String updateFirmType;
    public String upgradeTime;
    public String version;

    public UpdateDeviceReq(String appkey, String devName, String productKey, String taskId, String updateFirmType, String upgradeTime, String version) {
        this.appkey = appkey;
        this.devName = devName;
        this.productKey = productKey;
        this.taskId = taskId;
        this.updateFirmType = updateFirmType;
        this.upgradeTime = upgradeTime;
        this.version = version;
    }
}
