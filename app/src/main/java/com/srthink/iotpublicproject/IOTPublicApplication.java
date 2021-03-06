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
import com.srthink.iotboxaar.utils.AppUtils;
import com.srthink.iotboxaar.utils.FileOperation;
import com.srthink.iotboxaar.utils.JsonUtil;
import com.srthink.iotboxaar.utils.LogUtil;
import com.srthink.iotboxaar.utils.RandomUtils;
import com.srthink.iotboxaar.utils.mqtt.DynamicRegisterByMqtt;
import com.srthink.iotboxaar.utils.mqtt.MqttUtil;
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

    private static String curFirmware = "1.0.0";//?????????????????????

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

    //??????application???????????????  ????????????????????????context
    private void initLifecycle() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                if (activity.getParent() != null) {//???????????????????????????????????????
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
     * ???application?????????mqtt??????
     *
     * @param productKey   ??????key
     * @param deviceName   ????????????
     * @param deviveSecret ????????????
     */
    public static void connectMqtt(String productKey, String deviceName, String deviveSecret, ConnectCallback mqttConnectCallback) {
        MqttRegesterInfo mqttRegesterInfo = new MqttRegesterInfo();
        DynamicRegisterByMqtt client = new DynamicRegisterByMqtt();
        try {
            String clientID = AppUtils.getMacAddressFromIp(mContext).replace(":", "");//mac ??????
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
            LogUtil.logInfo(TAG + "??????????????????" + e.toString());
        }
        EquipmentInfo equipmentInfo = AppSpUtil.getEquipmentInfo(mContext);
        List<String> subscribeTopics = new ArrayList<>();
        topic_update = String.format(AppContants.topic_update, equipmentInfo.productKey, equipmentInfo.deviceName);//???????????????????????????
        subscribeTopics.add(topic_update);//??????????????????
        mqttUtil = MqttUtil.getInstance(IOTPublicApplication.getApp(), AppContants.MQTT_HOST, mqttRegesterInfo.clientId, mqttRegesterInfo.username, mqttRegesterInfo.password,
                subscribeTopics, equipmentInfo, new MqttConnectCallback() {
                    @Override
                    public void connectSuccess() {
                        mqttConnectCallback.connectSuccess();
                        LogUtil.logInfo(TAG + "????????????");
                        handleLocalUpdateData(true);
                    }

                    @Override
                    public void connectFail() {
                        mqttConnectCallback.connectFail();
                        LogUtil.logInfo(TAG + "????????????");
                        handleLocalUpdateData(false);
                    }

                    @Override
                    public void sendMsgComplete() {
                        LogUtil.logInfo(TAG + "??????????????????");
                        mqttConnectCallback.sendMsgComplete();
                    }

                    @Override
                    public void msgArrived(String s, String s1) {
                        LogUtil.logInfo(TAG + "??????????????????????????????" + "topic---??????" + s + "?????????----??????" + s1);
                        if (s.equals(topic_update)) {
                            LogUtil.logInfo(TAG + "?????????????????????");
                            UpdateInfo updateInfo = JsonUtil.getObject(s1, UpdateInfo.class);
                            showUpdateDialog(updateInfo);
                        }
                    }

                    @Override
                    public void connectLost(Throwable throwable) {
                        LogUtil.logInfo(TAG + "????????????");
                    }

                    @Override
                    public void sendMsgFail() {
                        LogUtil.logInfo(TAG + "??????????????????");
                        mqttConnectCallback.sendMsgFail();
                    }
                });
    }

    public static void disConnectMqtt() {
        if (mqttUtil != null) {
            if (mqttUtil.mqttNewClient != null) {
                mqttUtil.disconnectMqtt();
                LogUtil.logInfo(TAG + "????????????");
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
                AppUtils.showToast(mContext, "?????????????????????????????????");
            }
        }
    }

    /**
     * ?????????????????????????????????
     *
     * @param isConnect
     */
    private static void handleLocalUpdateData(boolean isConnect) {
        if (isConnect) {
            LogUtil.logInfo(TAG + "mqtt????????????  ???????????????????????????????????????????????????????????????");
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
                        mqttUtil.publishUpdateSituation(topic, jsonString);//???mqtt??????????????????
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
                    LogUtil.logInfo(TAG + "????????????");
                    deleteLocalFile(newData, updateInfo, updateInfo.getBody().getFirmwareVersion(), AppUtils.getVersionName(getApp()), false);
                } else {
                    LogUtil.logInfo(TAG + "????????????");
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
     * ???????????????????????????????????????????????????????????????
     *
     * @param newData
     * @param updateInfo
     * @param newVersion
     * @param localVersion
     */
    private static void deleteLocalFile(CacheUpdateInfo newData, UpdateInfo updateInfo, String newVersion, String localVersion, boolean isNeedNotice) {
        if (AppUtils.isBiggerVersion(newVersion, localVersion)) {
            LogUtil.logInfo(TAG + "????????????????????????????????????");
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
            LogUtil.logInfo(TAG + "????????????????????????????????????");
            String[] urls = updateInfo.getBody().getUrl().split(APIService.BASE_URL);
            String downloadUrl = urls[1].trim();
            if (!isUpdating) {
                getApp().startUpServiceToDownload(updateInfo, downloadUrl);
            }
        }
    }

    /**
     * ???????????????????????????
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
     * ??????????????????????????????????????????
     *
     * @param updateInfo
     */
    private static void showUpdateDialog(UpdateInfo updateInfo) {
        String tips = "";
        if (updateInfo.getBody().getUpgradeType() == 1) {
            tips = "??????????????????????????????????????????????????????????????????";
        } else {
            tips = "??????????????????????????????????????????????????????????????????";
        }
        AlertView alertView = new AlertView(null, tips, null, new String[]{"??????"},
                new String[]{"??????"},
                mContext, AlertView.Style.Alert, new OnItemClickListener() {
            public void onItemClick(Object o, int position) {
                if (position == 0) {
                    LogUtil.logInfo(TAG + "????????????----???????????????");
                    AppUtils.showToast(mContext, "????????????");
                    getApp().handleDownloadResult(false, updateInfo.getBody().getUpgradeType(), updateInfo.getMessageId());
                } else if (position == 1) {
                    LogUtil.logInfo(TAG + "?????????????????????");
                    CacheUpdateInfo cacheUpdateInfo = AppSpUtil.getCacheUpdateInfo(IOTPublicApplication.getApp());
                    UpdateItemInfo updateItemInfo = new UpdateItemInfo();
                    updateItemInfo.isFinish = false;
                    updateItemInfo.updateInfo = updateInfo;
                    cacheUpdateInfo.updateItemInfos.add(updateItemInfo);
                    AppSpUtil.setCacheUpdateInfo(IOTPublicApplication.getApp(), cacheUpdateInfo);

                    AppUtils.showToast(mContext, "?????????????????????");
                    String[] urls = updateInfo.getBody().getUrl().split(APIService.BASE_URL);
                    String downloadUrl = urls[1].trim();
                    if (!isUpdating) {
                        LogUtil.logInfo(TAG + "??????????????????");
                        getApp().startUpServiceToDownload(updateInfo, downloadUrl);
                    }

                }
            }
        });
        alertView.show();
    }

    /**
     * ????????????????????????????????????
     *
     * @param updateInfo
     * @param downloadUrl
     */
    public void startUpServiceToDownload(UpdateInfo updateInfo, String downloadUrl) {
        LogUtil.logInfo(TAG + "???????????????????????????");
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
                    Thread.sleep(3000);//??????3???
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
     * ?????????????????????
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
            LogUtil.logInfo(TAG + "????????????");
        } else {
            NoticeServerUpdateSituation.ResultBean resultBean = new NoticeServerUpdateSituation.ResultBean(AppContants.UPDATE_FAIL, AppContants.UPDATE_FAIL_TIP, updateType);
            NoticeServerUpdateSituation noticeServerUpdateSituation = new NoticeServerUpdateSituation(mid, deviceCode, resultBean);
            jsonString = JsonUtil.getJsonString(noticeServerUpdateSituation);
            AppUtils.showToast(this, AppContants.UPDATE_FAIL_TIP);
            LogUtil.logInfo(TAG + "????????????");
        }
        mqttUtil.publishUpdateSituation(topic, jsonString);
        handleSurplusData();
    }
}
