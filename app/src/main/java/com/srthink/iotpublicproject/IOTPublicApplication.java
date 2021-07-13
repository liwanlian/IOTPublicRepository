package com.srthink.iotpublicproject;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.srthink.iotboxaar.callbacks.MqttConnectCallback;
import com.srthink.iotboxaar.models.EquipmentInfo;
import com.srthink.iotboxaar.models.MqttRegesterInfo;
import com.srthink.iotboxaar.models.MsgReplyInfo;
import com.srthink.iotboxaar.models.ResponseInfo;
import com.srthink.iotboxaar.utils.AppUtils;
import com.srthink.iotboxaar.utils.FileOperation;
import com.srthink.iotboxaar.utils.JsonUtil;
import com.srthink.iotboxaar.utils.LogUtil;
import com.srthink.iotboxaar.utils.RandomUtils;
import com.srthink.iotboxaar.utils.mqtt.DynamicRegisterByMqtt;
import com.srthink.iotboxaar.utils.mqtt.MqttUtil;
import com.srthink.iotboxaar.utils.mqtt.TopicUtils;
import com.srthink.iotengravingmachinelibrary.ExternalCallEntry;
import com.srthink.iotengravingmachinelibrary.networkservice.APIService;
import com.srthink.iotpublicproject.callbacks.ConnectCallback;
import com.srthink.iotpublicproject.events.PublicEvent;
import com.srthink.iotpublicproject.events.UpdateEvent;
import com.srthink.iotpublicproject.models.CacheUpdateInfo;
import com.srthink.iotpublicproject.models.NeedNoticeInfo;
import com.srthink.iotpublicproject.models.NeedNoticeItemInfo;
import com.srthink.iotpublicproject.models.NoticeServerUpdateSituation;
import com.srthink.iotpublicproject.models.UpdateInfo;
import com.srthink.iotpublicproject.models.UpdateItemInfo;
import com.srthink.iotpublicproject.services.DownloadIntentNewService;
import com.srthink.iotpublicproject.utils.AppContants;
import com.srthink.iotpublicproject.utils.AppSpUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * @author liwanlian
 * @date 2021/5/13 14:45
 */
public class IOTPublicApplication extends Application {
    public static IOTPublicApplication app;

    public static IOTPublicApplication getApp() {
        return app;
    }

    public static MqttUtil mqttUtil;

    public static MqttUtil getMqttUtil() {
        return mqttUtil;
    }

    public static ExternalCallEntry externalCallEntry;

    public static ExternalCallEntry updateEntryData(String token, EquipmentInfo equipmentInfo) {
        externalCallEntry = new ExternalCallEntry(token, equipmentInfo);
        return externalCallEntry;
    }

    public static ExternalCallEntry getExternalEnter() {
        return externalCallEntry;
    }


    public static boolean isUpdating = false;

    private static Context mContext;

    private static String topic_update;
    private static String topic_replymsg;
    private static String topic_service;
    private static String topic_serverinstruction;

    private static String curFirmware = "1.0.0";//当前的固件版本

    private static final int DOWNLOADAPK_ID = 13;
    public static final String TAG = IOTPublicApplication.class.getSimpleName();


    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        initData();
        initLifecycle();
        EventBus.getDefault().register(this);
    }

    //要在application下弹出弹窗  首先得获取当前的context
    private void initLifecycle() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                if (activity.getParent() != null) {//如果这个视图是嵌入的子视图
                    mContext = activity.getParent();
                } else {
                    mContext = activity;
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {
                if (activity.getParent() != null) {
                    mContext = activity.getParent();
                } else {
                    mContext = activity;
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {
                if (activity.getParent() != null) {
                    mContext = activity.getParent();
                } else {
                    mContext = activity;
                }
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }

        });
    }


    private static void initData() {
        String token = AppSpUtil.getAccessToken(IOTPublicApplication.getApp());
        if (TextUtils.isEmpty(token)) {
            AppSpUtil.setAccessToken(IOTPublicApplication.getApp(), "token");
        }
        EquipmentInfo equipmentInfo = AppSpUtil.getEquipmentInfo(IOTPublicApplication.getApp());
        if (equipmentInfo == null) {
            EquipmentInfo nInfo = new EquipmentInfo();
            AppSpUtil.setEquipmentInfo(IOTPublicApplication.getApp(), nInfo);
        }

        updateEntryData(AppSpUtil.getAccessToken(IOTPublicApplication.getApp()), AppSpUtil.getEquipmentInfo(IOTPublicApplication.getApp()));
    }

    /**
     * 在application中进行mqtt连接
     *
     * @param productKey   产品key
     * @param deviceName   设备名称
     * @param deviveSecret 设备秘钥
     */
    public static void connectMqtt(String productKey, String deviceName, String deviveSecret, ConnectCallback mqttConnectCallback) {
        MqttRegesterInfo mqttRegesterInfo = new MqttRegesterInfo();
        DynamicRegisterByMqtt client = new DynamicRegisterByMqtt();
        try {
            String clientID = AppUtils.getMacAddressFromIp(mContext).replace(":", "");//mac 地址
            String random = AppSpUtil.getRandomCache(mContext);
            if (!TextUtils.isEmpty(random)) {
                mqttRegesterInfo = client.register(mContext, clientID, random, productKey, deviveSecret, deviceName);
            } else {
                String newRandom = RandomUtils.getRandomsData();
                AppSpUtil.setRandomCache(mContext, newRandom);
                mqttRegesterInfo = client.register(mContext, clientID, newRandom, productKey, deviveSecret, deviceName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.logInfo(TAG + "注册抛异常了" + e.toString());
        }
        EquipmentInfo equipmentInfo = AppSpUtil.getEquipmentInfo(mContext);
        List<String> subscribeTopics = new ArrayList<>();
        topic_update = String.format(AppContants.topic_update, equipmentInfo.productKey, equipmentInfo.deviceName);//更新通知主题的订阅
        topic_serverinstruction = String.format(TopicUtils.topic_serverinstrution, equipmentInfo.productKey, equipmentInfo.deviceName);//服务通知订阅
        topic_replymsg = String.format(TopicUtils.topic_msgreply, equipmentInfo.productKey, equipmentInfo.deviceName);//服务通知回调
        subscribeTopics.add(topic_update);//订阅主题合集
        subscribeTopics.add(topic_serverinstruction);
        mqttUtil = MqttUtil.getInstance(IOTPublicApplication.getApp(), AppContants.MQTT_HOST, mqttRegesterInfo.clientId, mqttRegesterInfo.username, mqttRegesterInfo.password,
                subscribeTopics, equipmentInfo, new MqttConnectCallback() {
                    @Override
                    public void connectSuccess() {
                        mqttConnectCallback.connectSuccess();
                        LogUtil.logInfo(TAG + "连接成功");
                        handleLocalUpdateData(true);
                    }

                    @Override
                    public void connectFail() {
                        mqttConnectCallback.connectFail();
                        LogUtil.logInfo(TAG + "连接失败");
                        handleLocalUpdateData(false);
                    }

                    @Override
                    public void sendMsgComplete() {
                        LogUtil.logInfo(TAG + "消息发送成功");
                        mqttConnectCallback.sendMsgComplete();
                    }

                    @Override
                    public void msgArrived(String s, String s1) {
                        LogUtil.logInfo(TAG + "服务端有消息推送回来" + "topic---》》" + s + "消息是----》》" + s1);
                        if (s.equals(topic_update)) {
                            LogUtil.logInfo(TAG + "需要下载更新了");
                            UpdateInfo updateInfo = JsonUtil.getObject(s1, UpdateInfo.class);
                            showUpdateDialog(updateInfo);
                        } else if (s.equals(topic_serverinstruction)) {
                            LogUtil.logInfo(TAG + "远程服务指令运达");
                            MsgReplyInfo msgReplyInfo = JsonUtil.getObject(s1, MsgReplyInfo.class);
                            ResponseInfo.ResultBean resultBean = new ResponseInfo.ResultBean(com.srthink.iotboxaar.utils.AppContants.handleServerInstructionResult.RESULT_SUCCESS.getResult(), 200);
                            ResponseInfo responseInfo = new ResponseInfo(msgReplyInfo.messageId, resultBean);
                            String jsonString = JsonUtil.getJsonString(responseInfo);
                            LogUtil.logInfo(TAG + "发送的消息：" + jsonString);
                            mqttUtil.publishDeviceInfo(topic_replymsg, jsonString);
                            LogUtil.logInfo(TAG + "远程服务指令回调");
                        }
                    }

                    @Override
                    public void connectLost(Throwable throwable) {
                        LogUtil.logInfo(TAG + "连接丢失");
                    }

                    @Override
                    public void sendMsgFail() {
                        LogUtil.logInfo(TAG + "发送信息失败");
                        mqttConnectCallback.sendMsgFail();
                    }
                });
    }

    public static void disConnectMqtt() {
        if (mqttUtil != null) {
            if (mqttUtil.mqttNewClient != null) {
                mqttUtil.disconnectMqtt();
                LogUtil.logInfo(TAG + "断开设备");
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PublicEvent event) {
        if (event != null && event instanceof UpdateEvent) {
            if (!event.isTimeout) {
                if (event.isSuccess) {
                    handleDownloadResult(true, Integer.parseInt(((UpdateEvent) event).updateType), ((UpdateEvent) event).mid);
                } else {
                    handleDownloadResult(false, Integer.parseInt(((UpdateEvent) event).updateType), ((UpdateEvent) event).mid);
                }
            } else {
                AppUtils.showToast(mContext, "网络缓慢，下载超时了！");
            }
        }
    }

    /**
     * 处理本地缓存的更新数据
     *
     * @param isConnect
     */
    private static void handleLocalUpdateData(boolean isConnect) {
        if (isConnect) {
            LogUtil.logInfo(TAG + "mqtt连接成功  将本地没有通知服务端的更新情况上报给服务端");
            NeedNoticeInfo needNoticeInfo = AppSpUtil.getNeedNoticeInfo(getApp());
            if (needNoticeInfo != null) {
                if (needNoticeInfo.needNoticeItemInfos.size() != 0) {
                    for (int i = 0; i < needNoticeInfo.needNoticeItemInfos.size(); i++) {
                        String deviceCode = AppSpUtil.getEquipmentInfo(getApp()).deviceName;
                        NoticeServerUpdateSituation.ResultBean resultBean = new NoticeServerUpdateSituation.ResultBean(AppContants.UPDATE_SUCCRSS, AppContants.UPDATE_SUCCRSS_TIP, needNoticeInfo.needNoticeItemInfos.get(i).upgradeType);
                        NoticeServerUpdateSituation noticeServerUpdateSituation = new NoticeServerUpdateSituation(needNoticeInfo.needNoticeItemInfos.get(i).mid, deviceCode, resultBean);
                        String jsonString = JsonUtil.getJsonString(noticeServerUpdateSituation);
                        EquipmentInfo equipmentInfo = AppSpUtil.getEquipmentInfo(IOTPublicApplication.getApp());
                        String topic = String.format(AppContants.topic_notice, equipmentInfo.productKey, equipmentInfo.deviceName);
                        mqttUtil.publishUpdateSituation(topic, jsonString);//走mqtt通知更新情况
                        needNoticeInfo.needNoticeItemInfos.remove(i);
                    }
                    AppSpUtil.setNeedNoticeInfo(getApp(), needNoticeInfo);
                }
            }

            CacheUpdateInfo newData = AppSpUtil.getCacheUpdateInfo(getApp());
            if (newData == null) return;
            if (newData.updateItemInfos.size() != 0) {
                UpdateInfo updateInfo = newData.updateItemInfos.get(0).updateInfo;
                if (String.valueOf(updateInfo.getBody().getUpgradeType()).equals(AppContants.UPDATE_TYPE_SOFTWARE)) {
                    LogUtil.logInfo(TAG + "软件记录");
                    deleteLocalFile(newData, updateInfo, updateInfo.getBody().getFirmwareVersion(), AppUtils.getVersionName(getApp()), false);
                } else {
                    LogUtil.logInfo(TAG + "固件记录");
                    deleteLocalFile(newData, updateInfo, updateInfo.getBody().getFirmwareVersion(), curFirmware, false);
                }
            }
        } else {
            CacheUpdateInfo newData = AppSpUtil.getCacheUpdateInfo(getApp());
            if (newData == null) return;
            if (newData.updateItemInfos.size() != 0) {
                UpdateInfo updateInfo = newData.updateItemInfos.get(0).updateInfo;
                if (String.valueOf(updateInfo.getBody().getUpgradeType()).equals(AppContants.UPDATE_TYPE_SOFTWARE)) {
                    deleteLocalFile(newData, updateInfo, updateInfo.getBody().getFirmwareVersion(), AppUtils.getVersionName(getApp()), true);
                } else {
                    deleteLocalFile(newData, updateInfo, updateInfo.getBody().getFirmwareVersion(), curFirmware, true);
                }
            }
        }
    }

    /**
     * 检测本地下载回来的固件或者软件是否需要删除
     *
     * @param newData
     * @param updateInfo
     * @param newVersion
     * @param localVersion
     */
    private static void deleteLocalFile(CacheUpdateInfo newData, UpdateInfo updateInfo, String newVersion, String localVersion, boolean isNeedNotice) {
        if (AppUtils.isBiggerVersion(newVersion, localVersion)) {
            LogUtil.logInfo(TAG + "本地的版本不是最新的版本");
            getApp().handleDownloadResult(true, updateInfo.getBody().getUpgradeType(), updateInfo.getMessageId());
            String mDownloadFileName = updateInfo.getBody().getFirmwareVersion() + updateInfo.getBody().getFileName();
            File file = new File(FileOperation.getSaveFile(mContext, AppContants.FOLDERNAME) + "/" + mDownloadFileName);
            FileOperation.deleteFile(file);
            newData.updateItemInfos.remove(0);
            AppSpUtil.setCacheUpdateInfo(IOTPublicApplication.getApp(), newData);
            if (isNeedNotice) {
                NeedNoticeItemInfo needNoticeItemInfo = new NeedNoticeItemInfo();
                needNoticeItemInfo.mid = updateInfo.getMessageId();
                needNoticeItemInfo.upgradeType = updateInfo.getBody().getUpgradeType();
                NeedNoticeInfo needNoticeInfo = AppSpUtil.getNeedNoticeInfo(getApp());
                needNoticeInfo.needNoticeItemInfos.add(needNoticeItemInfo);
                AppSpUtil.setNeedNoticeInfo(getApp(), needNoticeInfo);
            }
        } else {
            LogUtil.logInfo(TAG + "本地的版本已是最新的版本");
            String[] urls = updateInfo.getBody().getUrl().split(APIService.BASE_URL);
            String downloadUrl = urls[1].trim();
            if (!isUpdating) {
                getApp().startUpServiceToDownload(updateInfo, downloadUrl);
            }
        }
    }

    /**
     * 处理本地剩余的数据
     */
    private static void handleSurplusData() {
        CacheUpdateInfo cacheUpdateInfo = AppSpUtil.getCacheUpdateInfo(getApp());
        if (cacheUpdateInfo.updateItemInfos.size() == 0) return;
        cacheUpdateInfo.updateItemInfos.remove(0);
        AppSpUtil.setCacheUpdateInfo(getApp(), cacheUpdateInfo);

        CacheUpdateInfo newData = AppSpUtil.getCacheUpdateInfo(getApp());
        if (newData.updateItemInfos.size() != 0) {
            UpdateInfo updateInfo = newData.updateItemInfos.get(0).updateInfo;
            String[] urls = updateInfo.getBody().getUrl().split(APIService.BASE_URL);
            String downloadUrl = urls[1].trim();
            if (!isUpdating) {
                getApp().startUpServiceToDownload(updateInfo, downloadUrl);
            }
        }
    }

    /**
     * 弹窗提示用户是否需要下载更新
     *
     * @param updateInfo
     */
    private static void showUpdateDialog(UpdateInfo updateInfo) {
        String tips = "";
        if (updateInfo.getBody().getUpgradeType() == 1) {
            tips = "有新的固件版本可更新，是否需要下载最新的版本";
        } else {
            tips = "有新的软件版本可更新，是否需要下载最新的版本";
        }
        AlertView alertView = new AlertView(null, tips, null, new String[]{"取消"},
                new String[]{"确定"},
                mContext, AlertView.Style.Alert, new OnItemClickListener() {
            public void onItemClick(Object o, int position) {
                if (position == 0) {
                    LogUtil.logInfo(TAG + "更新失败----通知服务端");
                    AppUtils.showToast(mContext, "更新失败");
                    getApp().handleDownloadResult(false, updateInfo.getBody().getUpgradeType(), updateInfo.getMessageId());
                } else if (position == 1) {
                    LogUtil.logInfo(TAG + "已加入下载列表");
                    CacheUpdateInfo cacheUpdateInfo = AppSpUtil.getCacheUpdateInfo(IOTPublicApplication.getApp());
                    UpdateItemInfo updateItemInfo = new UpdateItemInfo();
                    updateItemInfo.isFinish = false;
                    updateItemInfo.updateInfo = updateInfo;
                    cacheUpdateInfo.updateItemInfos.add(updateItemInfo);
                    AppSpUtil.setCacheUpdateInfo(IOTPublicApplication.getApp(), cacheUpdateInfo);

                    AppUtils.showToast(mContext, "已加入下载列表");
                    String[] urls = updateInfo.getBody().getUrl().split(APIService.BASE_URL);
                    String downloadUrl = urls[1].trim();
                    if (!isUpdating) {
                        LogUtil.logInfo(TAG + "启动服务下载");
                        getApp().startUpServiceToDownload(updateInfo, downloadUrl);
                    }

                }
            }
        });
        alertView.show();
    }

    /**
     * 启动后台服务进行下载任务
     *
     * @param updateInfo
     * @param downloadUrl
     */
    public void startUpServiceToDownload(UpdateInfo updateInfo, String downloadUrl) {
        LogUtil.logInfo(TAG + "需要启动服务更新了");
        isUpdating = true;

        String deviceCode = AppSpUtil.getEquipmentInfo(IOTPublicApplication.getApp()).deviceName;
        NoticeServerUpdateSituation.ResultBean resultBean = new NoticeServerUpdateSituation.ResultBean(AppContants.UPDATE_UPGRADING, AppContants.UPDATE_UPGRADING_TIP, updateInfo.getBody().getUpgradeType());
        NoticeServerUpdateSituation noticeServerUpdateSituation = new NoticeServerUpdateSituation(updateInfo.getBody().getMessageId(), deviceCode, resultBean);
        String jsonString = JsonUtil.getJsonString(noticeServerUpdateSituation);
        EquipmentInfo equipmentInfo = AppSpUtil.getEquipmentInfo(IOTPublicApplication.getApp());
        String topic = String.format(AppContants.topic_notice, equipmentInfo.productKey, equipmentInfo.deviceName);
        mqttUtil.publishUpdateSituation(topic, jsonString);

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(3000);//休眠3秒
                    Intent intent = new Intent(IOTPublicApplication.getApp(), DownloadIntentNewService.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("download_url", downloadUrl);
                    LogUtil.logInfo(TAG + "download---->" + downloadUrl);
                    bundle.putInt("download_id", DOWNLOADAPK_ID);
                    bundle.putString("download_file", updateInfo.getBody().getFirmwareVersion() + updateInfo.getBody().getFileName());
                    bundle.putString("updateType", String.valueOf(updateInfo.getBody().getUpgradeType()));
                    bundle.putString("msgid", updateInfo.getBody().getMessageId());
                    intent.putExtras(bundle);
                    startService(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    /**
     * 处理下载的结果
     *
     * @param isSuccess
     * @param updateType
     * @param mid
     */
    public void handleDownloadResult(boolean isSuccess, int updateType, String mid) {
        isUpdating = false;
        String deviceCode = AppSpUtil.getEquipmentInfo(IOTPublicApplication.getApp()).deviceName;
        EquipmentInfo equipmentInfo = AppSpUtil.getEquipmentInfo(this);
        String topic = String.format(AppContants.topic_notice, equipmentInfo.productKey, equipmentInfo.deviceName);
        String jsonString = "";
        if (isSuccess) {
            NoticeServerUpdateSituation.ResultBean resultBean = new NoticeServerUpdateSituation.ResultBean(AppContants.UPDATE_SUCCRSS, AppContants.UPDATE_SUCCRSS_TIP, updateType);
            NoticeServerUpdateSituation noticeServerUpdateSituation = new NoticeServerUpdateSituation(mid, deviceCode, resultBean);
            jsonString = JsonUtil.getJsonString(noticeServerUpdateSituation);
            AppUtils.showToast(this, AppContants.UPDATE_SUCCRSS_TIP);
            LogUtil.logInfo(TAG + "下载成功");
        } else {
            NoticeServerUpdateSituation.ResultBean resultBean = new NoticeServerUpdateSituation.ResultBean(AppContants.UPDATE_FAIL, AppContants.UPDATE_FAIL_TIP, updateType);
            NoticeServerUpdateSituation noticeServerUpdateSituation = new NoticeServerUpdateSituation(mid, deviceCode, resultBean);
            jsonString = JsonUtil.getJsonString(noticeServerUpdateSituation);
            AppUtils.showToast(this, AppContants.UPDATE_FAIL_TIP);
            LogUtil.logInfo(TAG + "下载失败");
        }
        mqttUtil.publishUpdateSituation(topic, jsonString);
        handleSurplusData();
    }
}
