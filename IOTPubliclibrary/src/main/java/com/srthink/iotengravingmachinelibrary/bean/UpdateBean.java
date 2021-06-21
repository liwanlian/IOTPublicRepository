package com.srthink.iotengravingmachinelibrary.bean;

/**
 * @author liwanlian
 * @date 2021/5/14 19:16
 */
public class UpdateBean {

    /**
     * region :
     * responseMessageId :
     * responseTime :
     * url :
     */

    private String region;
    private String responseMessageId;
    private String responseTime;
    private String url;

    public UpdateBean(String region, String responseMessageId, String responseTime, String url) {
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
