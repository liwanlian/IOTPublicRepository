package com.srthink.iotengravingmachinelibrary.bean.NetworkRequest;

/**
 * @author liwanlian
 * @date 2021/6/15 17:01
 */
public class UploadDeviceIpReq {

    /**
     * devName :
     * ip :
     * productKey :
     */

    public String devName;
    public String ip;
    public String productKey;

    public UploadDeviceIpReq(String devName, String ip, String productKey) {
        this.devName = devName;
        this.ip = ip;
        this.productKey = productKey;
    }
}
