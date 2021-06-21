package com.srthink.iotengravingmachinelibrary.bean;

/**
 * Created by liwanlian
 * on 2021/5/16 0:05
 * 故障上报
 **/
public class UploadErrorBean {

    /**
     * region :
     * responseMessageId : 1165753452031115264
     * responseTime : 2021-05-28 10:24:00
     * url :
     */

    private String region;
    private String responseMessageId;
    private String responseTime;
    private String url;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getResponseMessageId() {
        return responseMessageId;
    }

    public void setResponseMessageId(String responseMessageId) {
        this.responseMessageId = responseMessageId;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
