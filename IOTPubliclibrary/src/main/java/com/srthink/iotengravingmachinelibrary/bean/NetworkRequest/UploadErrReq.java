package com.srthink.iotengravingmachinelibrary.bean.NetworkRequest;

/**
 * Created by liwanlian
 * on 2021/5/15 23:57
 * 故障上报
 **/
public class UploadErrReq extends Req {
    public String devSn;
    public String devName;
    public String productKey;
    public int alarmType;//报警类型 1.电量过低 2.定位丢失 3.离线 4.充电失败 5.急停 6.路径规划失败
    public String alarmTime;//报警的时间


    public UploadErrReq(String token, String devSn, String devName, String productKey, int alarmType, String alarmTime) {
        super(token);
        this.devSn = devSn;
        this.devName = devName;
        this.productKey = productKey;
        this.alarmType = alarmType;
        this.alarmTime = alarmTime;
    }
}
