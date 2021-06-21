package com.srthink.iotpublicproject.callbacks;

/**
 * @author liwanlian
 * @date 2021/6/18 9:31
 */
public interface ConnectCallback {
    void connectSuccess();

    void connectFail();

    void sendMsgComplete();

    void sendMsgFail();
}
