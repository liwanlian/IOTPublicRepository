package com.srthink.iotboxaar.callbacks;

/**
 * @author liwanlian
 * @date 2021/1/18 18:03
 */
public interface MqttConnectCallback {
    void connectSuccess();

    void connectFail();

    void sendMsgComplete();

    void msgArrived(String topic, String msg);

    void connectLost(Throwable throwable);

    void sendMsgFail();
}
