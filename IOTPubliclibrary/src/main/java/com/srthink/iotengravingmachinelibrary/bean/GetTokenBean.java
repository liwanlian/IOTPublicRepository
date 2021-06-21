package com.srthink.iotengravingmachinelibrary.bean;

/**
 * @author liwanlian
 * @date 2021/5/13 18:04
 * 获取token
 */
public class GetTokenBean {

    /**
     * responseMessageId : 1155739682076622848
     * responseTime : 2021-05-14 14:48:26
     * token :
     */

    public String responseMessageId;
    public String responseTime;
    public String token;

    public GetTokenBean(String responseMessageId, String responseTime, String token) {
        this.responseMessageId = responseMessageId;
        this.responseTime = responseTime;
        this.token = token;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
