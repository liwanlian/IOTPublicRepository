package com.srthink.iotengravingmachinelibrary.bean.NetworkRequest;

/**
 * @author liwanlian
 * @date 2021/5/31 17:31
 * 获取故障详情
 */
public class GetAlarmDetailReq {
    public String devSn;
    public String devName;
    public String productKey;
    public String alarmId;

    public GetAlarmDetailReq(String devSn, String devName, String productKey, String alarmId) {
        this.devSn = devSn;
        this.devName = devName;
        this.productKey = productKey;
        this.alarmId = alarmId;
    }
}
