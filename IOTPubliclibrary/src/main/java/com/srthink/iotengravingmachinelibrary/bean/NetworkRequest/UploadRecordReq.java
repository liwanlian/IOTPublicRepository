package com.srthink.iotengravingmachinelibrary.bean.NetworkRequest;

/**
 * Created by liwanlian
 * on 2021/5/15 23:57
 * 反馈上报
 **/
public class UploadRecordReq extends Req {
    public String devSn;
    public String devName;
    public String productKey;
    public String feedbackContent;
    public String feedbackTime;

    public UploadRecordReq(String token, String devSn, String devName, String productKey, String feedbackContent, String feedbackTime) {
        super(token);
        this.devSn = devSn;
        this.devName = devName;
        this.productKey = productKey;
        this.feedbackContent = feedbackContent;
        this.feedbackTime = feedbackTime;
    }
}
