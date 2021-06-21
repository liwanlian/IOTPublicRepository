package com.srthink.iotengravingmachinelibrary.bean.NetworkRequest;

/**
 * @author liwanlian
 * @date 2021/5/14 19:10
 * 设备激活
 */
public class ActiveReq {
    public String devSn;
    public String devName;
    public String productKey;

    public ActiveReq(String devSn, String devName, String productKey) {
        this.devSn = devSn;
        this.devName = devName;
        this.productKey = productKey;
    }
}
