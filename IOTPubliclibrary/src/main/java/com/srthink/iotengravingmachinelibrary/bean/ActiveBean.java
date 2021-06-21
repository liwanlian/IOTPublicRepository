package com.srthink.iotengravingmachinelibrary.bean;

/**
 * @author liwanlian
 * @date 2021/5/14 19:07
 * 设备激活
 */
public class ActiveBean {
    /**
     * region :
     * responseMessageId :
     * responseTime :
     * url :
     */

    public String region;
    public String responseMessageId;
    public String responseTime;
    public String url;


    public ActiveBean(String region, String responseMessageId, String responseTime, String url) {
        this.region = region;
        this.responseMessageId = responseMessageId;
        this.responseTime = responseTime;
        this.url = url;
    }

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
