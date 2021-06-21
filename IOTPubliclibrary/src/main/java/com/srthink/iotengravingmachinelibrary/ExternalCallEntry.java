package com.srthink.iotengravingmachinelibrary;

import android.content.Context;

import com.srthink.iotboxaar.models.EquipmentInfo;
import com.srthink.iotboxaar.utils.DateUtil;
import com.srthink.iotboxaar.utils.LogUtil;
import com.srthink.iotboxaar.utils.MD5Utils;
import com.srthink.iotboxaar.utils.mqtt.IotHttpClient;
import com.srthink.iotengravingmachinelibrary.bean.AlarmDetailBean;
import com.srthink.iotengravingmachinelibrary.bean.GetAlarmListBean;
import com.srthink.iotengravingmachinelibrary.bean.GetDataBySnBean;
import com.srthink.iotengravingmachinelibrary.bean.GetNewVersionBean;
import com.srthink.iotengravingmachinelibrary.bean.GetRecordListBean;
import com.srthink.iotengravingmachinelibrary.bean.GetTokenBean;
import com.srthink.iotengravingmachinelibrary.callbacks.DownloadCallBack;
import com.srthink.iotengravingmachinelibrary.callbacks.ExternalAccessCallback;
import com.srthink.iotengravingmachinelibrary.callbacks.ExternalAccessDownloadCallback;
import com.srthink.iotengravingmachinelibrary.callbacks.ExternalAccessUpdateCallback;
import com.srthink.iotengravingmachinelibrary.callbacks.InternalCallback;
import com.srthink.iotengravingmachinelibrary.models.DownloadInfo;
import com.srthink.iotengravingmachinelibrary.presenters.IOTEngravingMachineNewPresenter;
import com.srthink.iotengravingmachinelibrary.utils.AppUrls;

import java.io.File;
import java.util.List;

/**
 * @author liwanlian
 * @date 2021/6/16 15:00
 * 外部调用入口
 */
public class ExternalCallEntry {

    private String mToken;
    private EquipmentInfo equipmentInfo;

    private IOTEngravingMachineNewPresenter machineNewPresenter;

    public static final String UPDATETYPE_FIRMWARE = "upgradeDevFirmWare";//更新固件
    public static final String UPDATETYPE_SOFTWARE = "upgradeDevSoft";//更新软件
    private static final String TAG = "ExternalCallEntry";

    public ExternalCallEntry(String mToken, EquipmentInfo equipmentInfo) {
        this.mToken = mToken;
        this.equipmentInfo = equipmentInfo;
        machineNewPresenter = new IOTEngravingMachineNewPresenter();
    }

    /**
     * 检测是否需要从服务端拉取设备的秘钥信息
     *
     * @param snCode
     * @param externalAccessCallback
     */
    public void isNeedUpdateCacheDeviceInfo(String snCode, ExternalAccessUpdateCallback<String> externalAccessCallback) {
        if (equipmentInfo == null) {
            externalAccessCallback.needUpdate(snCode);
            LogUtil.logInfo(TAG + "need to update");
            return;
        } else {
            if (!snCode.equals(equipmentInfo.sn)) {
                externalAccessCallback.needUpdate(snCode);
                LogUtil.logInfo(TAG + "need to update");
            } else {
                externalAccessCallback.doNotUpdate();
                LogUtil.logInfo(TAG + "do not need to update");
            }
        }
    }

    /**
     * 主工程传入设备sn 获取相应的设备信息（设备秘钥  设备名称  产品key）
     *
     * @param snCode
     * @param externalAccessCallback
     */
    public void getDeviceInfoBySn(String snCode, ExternalAccessCallback<EquipmentInfo> externalAccessCallback) {
        machineNewPresenter.getEquipDataBySn(snCode, mToken, new InternalCallback<GetDataBySnBean>() {
            @Override
            public void success(GetDataBySnBean data) {
                LogUtil.logInfo(TAG + "get deviceinfo success" + "devicename---->" + data.getDevName());
                EquipmentInfo eInfo = new EquipmentInfo();
                eInfo.sn = data.getDevSn();
                eInfo.deviceName = data.getDevName();
                eInfo.deviceSecret = data.getSecret();
                eInfo.productKey = data.getProductKey();
                externalAccessCallback.success(eInfo);
            }

            @Override
            public void fail(String msg) {
                LogUtil.logInfo(TAG + "get deviceinfo fail");
                externalAccessCallback.fail(msg);
            }
        });
    }

    /**
     * 设备token  获取
     *
     * @param externalAccessCallback
     */

    public void getDeviceToken(Context context, ExternalAccessCallback<String> externalAccessCallback) {
        if (equipmentInfo == null) {
            externalAccessCallback.fail("");
            return;
        }
        String autoBody = IotHttpClient.authBody(context, equipmentInfo.productKey, equipmentInfo.deviceName, equipmentInfo.deviceSecret);
        LogUtil.logInfo(TAG + "合并的信息是：" + autoBody);
        machineNewPresenter.getEquipMentToken(autoBody, new InternalCallback<GetTokenBean>() {
            @Override
            public void success(GetTokenBean data) {
                LogUtil.logInfo(TAG + "get token success");
                externalAccessCallback.success(data.getToken());
            }

            @Override
            public void fail(String msg) {
                LogUtil.logInfo(TAG + "get token fail");
                externalAccessCallback.fail(msg);
            }
        }, mToken);
    }

    /**
     * 设备激活
     *
     * @param externalAccessCallback
     */
    public void activeDevice(ExternalAccessCallback<String> externalAccessCallback) {
        if (equipmentInfo == null) {
            externalAccessCallback.fail("");
            return;
        }
        String nUrl = String.format(new AppUrls().url_active, equipmentInfo.productKey, equipmentInfo.deviceName);
        machineNewPresenter.activeEquipment(equipmentInfo.sn, equipmentInfo.productKey, equipmentInfo.deviceSecret, equipmentInfo.deviceName, nUrl, mToken, new InternalCallback<String>() {
            @Override
            public void success(String data) {
                LogUtil.logInfo(TAG + "active device success");
                externalAccessCallback.success(data);
            }

            @Override
            public void fail(String msg) {
                LogUtil.logInfo(TAG + "active device fail");
                externalAccessCallback.fail(msg);
            }
        });
    }

    /**
     * 设备故障信息上报
     *
     * @param alarmType              故障类型
     * @param externalAccessCallback
     */
    public void uploadDeviceAlarmMsg(int alarmType, ExternalAccessCallback<String> externalAccessCallback) {
        if (equipmentInfo == null) {
            externalAccessCallback.fail("");
            return;
        }
        String nUrl = String.format(new AppUrls().url_uploadalamr, equipmentInfo.productKey, equipmentInfo.deviceName);
        machineNewPresenter.uploadErrorMsg(equipmentInfo.sn, equipmentInfo.productKey, equipmentInfo.deviceSecret, equipmentInfo.deviceName, alarmType, DateUtil.getDetailDate(), nUrl, mToken, new InternalCallback<String>() {
            @Override
            public void success(String data) {
                LogUtil.logInfo(TAG + "upload alarm successful");
                externalAccessCallback.success(data);
            }

            @Override
            public void fail(String msg) {
                LogUtil.logInfo(TAG + "upload alarm fail");
                externalAccessCallback.fail(msg);
            }
        });
    }


    /**
     * 获取上报的故障信息列表
     *
     * @param currentPage
     * @param pageSize
     * @param externalAccessCallback
     */
    public void getDeviceAlarmListMsg(int currentPage, int pageSize, ExternalAccessCallback<GetAlarmListBean> externalAccessCallback) {
        if (equipmentInfo == null) {
            externalAccessCallback.fail("");
            return;
        }
        String url = String.format(new AppUrls().url_getalarm, equipmentInfo.productKey, equipmentInfo.deviceName);
        machineNewPresenter.getAlarmListMsg(equipmentInfo.sn, equipmentInfo.productKey, equipmentInfo.deviceSecret, equipmentInfo.deviceName, currentPage, pageSize, url, mToken, new InternalCallback<GetAlarmListBean>() {
            @Override
            public void success(GetAlarmListBean data) {
                externalAccessCallback.success(data);
                LogUtil.logInfo(TAG + "get alarmlistMsg successful");
            }

            @Override
            public void fail(String msg) {
                externalAccessCallback.fail(msg);
                LogUtil.logInfo(TAG + "get alarmlistMsg fail");
            }
        });
    }

    /**
     * 获取故障详情
     *
     * @param aid
     * @param externalAccessCallback
     */
    public void getAlarmDetailInfo(String aid, ExternalAccessCallback<AlarmDetailBean> externalAccessCallback) {
        if (equipmentInfo == null) {
            externalAccessCallback.fail("");
            return;
        }
        String url = String.format(new AppUrls().url_getalarmdetail, equipmentInfo.productKey, equipmentInfo.deviceName);
        machineNewPresenter.getAlarmDetailMsg(equipmentInfo.sn, equipmentInfo.productKey, equipmentInfo.deviceName, aid, url, mToken, new InternalCallback<AlarmDetailBean>() {
            @Override
            public void success(AlarmDetailBean data) {
                LogUtil.logInfo(TAG + "get alarm detail successful");
                externalAccessCallback.success(data);
            }

            @Override
            public void fail(String msg) {
                LogUtil.logInfo(TAG + "get alarm detail fail");
                externalAccessCallback.fail(msg);
            }
        });
    }

    /**
     * 检测是否有新的固件版本   /软件
     *
     * @param curVersion
     * @param externalAccessCallback
     */
    public void getNewVersionInfo(String type, String curVersion, String productCategory, String productName, ExternalAccessUpdateCallback<GetNewVersionBean> externalAccessCallback) {
        if (equipmentInfo == null) {
            externalAccessCallback.testingFail();
            return;
        }
        String url = String.format(new AppUrls().url_querynewversion, equipmentInfo.productKey, equipmentInfo.deviceName);
        machineNewPresenter.getNewVersionInfo(equipmentInfo.productKey, equipmentInfo.deviceName, productCategory, productName, url, mToken, new InternalCallback<List<GetNewVersionBean>>() {
            @Override
            public void success(List<GetNewVersionBean> data) {
                LogUtil.logInfo(TAG + "get new versioninfo success");
                if (data.size() != 0) {
                    boolean hasFirmwareRecord = false;
                    for (GetNewVersionBean getNewVersionBean : data) {
                        if (getNewVersionBean.getUpgradeType().equals(type)) {
                            hasFirmwareRecord = true;
                            if (!curVersion.equals(getNewVersionBean.getUpgradeVersion())) {
                                LogUtil.logInfo(TAG + "has new firmware to update");
                                externalAccessCallback.needUpdate(getNewVersionBean);
                                break;
                            } else {
                                externalAccessCallback.doNotUpdate();
                                LogUtil.logInfo(TAG + "the current version of the firmware is newest");
                                break;
                            }
                        }
                    }
                    if (!hasFirmwareRecord) {
                        LogUtil.logInfo(TAG + "there is no record in server");
                        externalAccessCallback.doNotUpdate();
                    }
                }
            }

            @Override
            public void fail(String msg) {
                externalAccessCallback.testingFail();
                LogUtil.logInfo(TAG + "get new versioninfo fail");
            }
        });
    }

    /**
     * 通知服务器推送新固件
     *
     * @param updateTime             更新时间
     * @param getNewVersionBean
     * @param externalAccessCallback
     */
    public void noticeServerUpdate(String updateTime, GetNewVersionBean getNewVersionBean, ExternalAccessCallback<String> externalAccessCallback) {
        if (equipmentInfo == null) {
            externalAccessCallback.fail("");
            return;
        }
        String appKey = getNewVersionBean.getId() + getNewVersionBean.getUpgradeType() + getNewVersionBean.getUpgradeVersion() + equipmentInfo.deviceSecret;
        String mdKey = MD5Utils.getMd5(appKey);
        String url = String.format(new AppUrls().url_update, equipmentInfo.productKey, equipmentInfo.deviceName);
        machineNewPresenter.updateDevice(equipmentInfo.productKey, equipmentInfo.deviceName, mdKey, getNewVersionBean.getId(), getNewVersionBean.getUpgradeType(),
                DateUtil.getDelayTime_Second(1), getNewVersionBean.getUpgradeVersion(), url, mToken, new InternalCallback<String>() {
                    @Override
                    public void success(String data) {
                        LogUtil.logInfo(TAG + "notice server success");
                        externalAccessCallback.success(data);
                    }

                    @Override
                    public void fail(String msg) {
                        LogUtil.logInfo(TAG + "notice server fail");
                        externalAccessCallback.fail(msg);
                    }
                });
    }

    /**
     * 将设备的外网地址上报   方便web端在地图上定位
     *
     * @param deviceIp
     * @param externalAccessCallback
     */
    public void uploadDeviceIpAddress(String deviceIp, ExternalAccessCallback<String> externalAccessCallback) {
        if (equipmentInfo == null) {
            externalAccessCallback.fail("");
            return;
        }
        String url = String.format(new AppUrls().url_uploadip, equipmentInfo.productKey, equipmentInfo.deviceName);
        machineNewPresenter.uploadDeviceIP(equipmentInfo.productKey, equipmentInfo.deviceName, deviceIp, url, mToken, new InternalCallback<String>() {
            @Override
            public void success(String data) {
                externalAccessCallback.success(data);
                LogUtil.logInfo(TAG + "upload ip success");
            }

            @Override
            public void fail(String msg) {
                externalAccessCallback.fail(msg);
                LogUtil.logInfo(TAG + "upload ip fail");
            }
        });
    }

    /**
     * 反馈上报
     *
     * @param content
     * @param externalAccessCallback
     */
    public void feedbackToServer(String content, ExternalAccessCallback<String> externalAccessCallback) {
        if (equipmentInfo == null) {
            externalAccessCallback.fail("");
            return;
        }
        String url = String.format(new AppUrls().url_feedback, equipmentInfo.productKey, equipmentInfo.deviceName);
        machineNewPresenter.uploadRecordMsg(equipmentInfo.sn, equipmentInfo.productKey, equipmentInfo.deviceSecret, equipmentInfo.deviceName, content, DateUtil.getDetailDate(), url, mToken, new InternalCallback<String>() {
            @Override
            public void success(String data) {
                LogUtil.logInfo(TAG + "feedback success");
                externalAccessCallback.success(data);
            }

            @Override
            public void fail(String msg) {
                LogUtil.logInfo(TAG + "feedback fail");
                externalAccessCallback.fail(msg);
            }
        });
    }

    /**
     * 获取反馈列表
     *
     * @param currentPage
     * @param pageSize
     * @param externalAccessCallback
     */
    public void getFeedbackList(int currentPage, int pageSize, ExternalAccessCallback<GetRecordListBean> externalAccessCallback) {
        if (equipmentInfo == null) {
            externalAccessCallback.fail("");
            return;
        }
        String url = String.format(new AppUrls().url_getfeedback, equipmentInfo.productKey, equipmentInfo.deviceName);
        machineNewPresenter.getRecordList(equipmentInfo.sn, equipmentInfo.productKey, equipmentInfo.deviceSecret, equipmentInfo.deviceName, currentPage, pageSize, url, mToken, new InternalCallback<GetRecordListBean>() {
            @Override
            public void success(GetRecordListBean data) {
                LogUtil.logInfo(TAG + "get feedbackList success");
                externalAccessCallback.success(data);
            }

            @Override
            public void fail(String msg) {
                LogUtil.logInfo(TAG + "get feedbackList fail");
                externalAccessCallback.fail(msg);
            }
        });
    }

    /**
     * 下载服务端推送回来的文件
     *
     * @param file
     * @param range
     * @param fileName
     * @param url
     * @param externalAccessCallback
     */
    public void downloadServerPushFile(File file, long range, String fileName, String url, ExternalAccessDownloadCallback externalAccessCallback) {
        machineNewPresenter.downloadServerPushFile(range, url, fileName, file, new DownloadCallBack() {
            @Override
            public void onProgress(int progress) {
                DownloadInfo downloadInfo = new DownloadInfo();
                downloadInfo.key = url;
                downloadInfo.value = progress;
                externalAccessCallback.onProgress(downloadInfo);
            }

            @Override
            public void onCompleted() {
                externalAccessCallback.onCompleted();
            }

            @Override
            public void onError(String msg) {
                externalAccessCallback.onError(msg);
            }

            @Override
            public void interrupt() {
                externalAccessCallback.interrupt();
            }

            @Override
            public void saveData(String url, long total) {
                externalAccessCallback.saveData(url, total);
            }
        });
    }


}
