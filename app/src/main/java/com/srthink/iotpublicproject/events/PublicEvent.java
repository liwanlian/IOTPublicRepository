package com.srthink.iotpublicproject.events;

/**
 * @author liwanlian
 * @date 2021/5/13 18:09
 */
public class PublicEvent {
    public boolean isSuccess = false;
    public boolean isTimeout = false;//是否请求超时
    public String msgCode = "";

    public PublicEvent() {
    }

    public PublicEvent(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public PublicEvent(boolean isSuccess, boolean isTimeout) {
        this.isSuccess = isSuccess;
        this.isTimeout = isTimeout;
    }
}
