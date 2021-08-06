package com.srthink.iotboxaar.utils.mqtt;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;

import com.srthink.iotboxaar.callbacks.MqttConnectCallback;
import com.srthink.iotboxaar.models.EquipmentInfo;
import com.srthink.iotboxaar.utils.AppContants;
import com.srthink.iotboxaar.utils.LogUtil;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liwanlian
 * @date 2021/5/17 10:08
 */
public class MqttUtil {

    public static MqttUtil instance;
    private MqttConnectOptions options;
    public MqttAndroidClient mqttNewClient;

    private final HandlerThread mConnectThread;
    private final Handler mBackgroundHandler;

    private int mQos = 0;

    private static List<String> subscribeTopics = new ArrayList<>();//需要订阅的主题

    private MqttConnectCallback mqttConnectCallback;
    private static String MQTT_HOST = "";//tcp://120.77.240.215:9876
    private static String TAG = "MqttUtil";

    public synchronized static MqttUtil getInstance(Context context, String host, String clientID, String usrname, String pwd, List<String> topics, EquipmentInfo equipmentInfo, MqttConnectCallback mqttConnectCallback) {
        if (instance == null) {
            synchronized (MqttUtil.class) {
                if (instance == null) {
                    instance = new MqttUtil(context, clientID, usrname, pwd, mqttConnectCallback);
                    subscribeTopics = topics;
                    MQTT_HOST = host;
                }
            }
        }
        return instance;
    }


    private MqttUtil(Context context, String clientID, String usrname, String pwd, MqttConnectCallback mqttConnectCallback) {
        mConnectThread = new HandlerThread("mqtt_connect");
        mConnectThread.start();
        mBackgroundHandler = new Handler(mConnectThread.getLooper());
        mBackgroundHandler.post(new Runnable() {
            @Override
            public void run() {
                mqttInitNew(context, clientID, usrname, pwd, mqttConnectCallback);
            }
        });
    }

    /**
     * mqtt连接前 需要配置的信息
     *
     * @param context
     * @param clientID
     * @param usrname
     * @param pwd
     * @param mqttConnectCallback
     */
    private void mqttInitNew(Context context, String clientID, String usrname, String pwd, MqttConnectCallback mqttConnectCallback) {
        LogUtil.logInfo(TAG + "传入的信息：" + clientID + "\n" + usrname + "\n" + pwd + "\n" + "结束" + "\n");
        try {
            // host为主机名，test为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
//            mqttClient = new MqttClient(MQTT_HOST, clientID, new MemoryPersistence());
            mqttNewClient = new MqttAndroidClient(context, MQTT_HOST, clientID);
            // MQTT的连接设置
            options = new MqttConnectOptions();
            // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(true);
            // 设置连接的用户名
            options.setUserName(usrname);
            // 设置连接的密码
            options.setPassword(pwd.toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(10);
            // 设置会话心跳时间 单位为秒 但这个方法并没有重连的机制
//            options.setKeepAliveInterval(10);//1.5*200=300   5分钟
            options.setAutomaticReconnect(true);
            mqttConnect(mqttConnectCallback);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.logError(TAG + "mqtt连接异常");
        }
    }

    /**
     * mqtt连接
     *
     * @param callback
     */

    private void mqttConnect(MqttConnectCallback callback) {
        mqttConnectCallback = callback;
        mqttNewClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                LogUtil.logInfo(TAG + "连接完成");
                try {
                    LogUtil.logInfo(TAG + "主题订阅----更新通知topic");
                    for (String topic : subscribeTopics) {
                        mqttNewClient.subscribe(topic, mQos);
                        LogUtil.logInfo(TAG + "订阅成功---->>" + topic);
                    }
                } catch (Exception e) {
                    LogUtil.logInfo(TAG + "订阅异常");
                    e.printStackTrace();
                }
                callback.connectSuccess();
            }

            @Override
            public void connectionLost(Throwable cause) {
                if (cause == null) {
                    LogUtil.logInfo(TAG + "连接丢失");
                } else {
                    LogUtil.logInfo(TAG + "连接丢失" + cause.toString());
                }
                callback.connectLost(cause);
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                LogUtil.logInfo(TAG + "----服务端有信息推送过来-----");
                LogUtil.logInfo(TAG + "消息到达" + "topcic-->" + topic + "  收到的消息是：" + message.toString());
                callback.msgArrived(topic, message.toString());
//                IOException e = new IOException();
//                throw e;

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                callback.sendMsgComplete();
                LogUtil.logInfo(TAG + "设备发送信息给服务端-----deliveryComplete---");
            }
        });
        try {
            mqttNewClient.connect(options, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    LogUtil.logInfo(TAG + "mqtt连接成功-----11111ii1");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    LogUtil.logInfo(TAG + "mqtt连接失败------22222222" + exception.toString());
                    callback.connectFail();
                }
            });
//            mqttClient.connectWithResult(options);

        } catch (Exception e) {
            LogUtil.logError(TAG + "mqtt连接失败--------" + e.toString());
            callback.connectFail();
            e.printStackTrace();
//            reConnection(callback);
        }
    }

    /**
     * 通知服务端更新的进度
     *
     * @param jsonString
     */
    public void publishUpdateSituation(String publishTopic, String jsonString) {
        LogUtil.logInfo(TAG + "通知更新进度---->" + jsonString);
        try {
            MqttMessage message = new MqttMessage(jsonString.getBytes("UTF-8"));
            message.setQos(mQos);
            mqttNewClient.publish(publishTopic, message);
        } catch (MqttException e) {
            e.printStackTrace();
            LogUtil.logInfo(TAG + "发布信息异常" + e.toString());
            mqttConnectCallback.sendMsgFail();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            LogUtil.logError(TAG + "转码异常" + e.toString());
            mqttConnectCallback.sendMsgFail();
        } catch (Exception e) {
            e.printStackTrace();
            mqttConnectCallback.sendMsgFail();
        }
    }

    /**
     * 上报物模型属性信息
     *
     * @param jsonString
     */
    public void publishDeviceInfo(String topic, String jsonString) {
        LogUtil.logInfo(TAG + "发布的信息：" + jsonString);
        try {
            MqttMessage message = new MqttMessage(jsonString.getBytes("UTF-8"));
            message.setQos(mQos);
            mqttNewClient.publish(topic, message);
        } catch (MqttException e) {
            e.printStackTrace();
            mqttConnectCallback.sendMsgFail();
            LogUtil.logInfo(TAG + "发布信息异常" + e.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            mqttConnectCallback.sendMsgFail();
            LogUtil.logError(TAG + "转码异常" + e.toString());
        } catch (Exception e) {
            mqttConnectCallback.sendMsgFail();
            e.printStackTrace();
        }
    }

    /**
     * mqtt断开连接
     */
    public void disconnectMqtt() {
        try {
            mqttNewClient.disconnect();
            LogUtil.logInfo(TAG + "已断开成功");
        } catch (MqttException e) {
            e.printStackTrace();
            LogUtil.logError(TAG + e.toString() + "异常");
        }
    }

}
