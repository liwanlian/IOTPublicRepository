package com.srthink.iotpublicproject;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.srthink.iotboxaar.models.EquipmentInfo;
import com.srthink.iotboxaar.models.EventInfo;
import com.srthink.iotboxaar.utils.AppUtils;
import com.srthink.iotboxaar.utils.DateUtil;
import com.srthink.iotboxaar.utils.FileOperation;
import com.srthink.iotboxaar.utils.JsonUtil;
import com.srthink.iotboxaar.utils.LogUtil;
import com.srthink.iotboxaar.utils.mqtt.MqttUtil;
import com.srthink.iotengravingmachinelibrary.ExternalCallEntry;
import com.srthink.iotengravingmachinelibrary.bean.GetNewVersionBean;
import com.srthink.iotengravingmachinelibrary.callbacks.ExternalAccessCallback;
import com.srthink.iotengravingmachinelibrary.callbacks.ExternalAccessUpdateCallback;
import com.srthink.iotengravingmachinelibrary.utils.NetUtils;
import com.srthink.iotpublicproject.callbacks.ConnectCallback;
import com.srthink.iotpublicproject.models.CacheUpdateInfo;
import com.srthink.iotpublicproject.models.DeviceInfo;
import com.srthink.iotpublicproject.utils.AppContants;
import com.srthink.iotpublicproject.utils.AppSpUtil;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnActive;
    private Button btnUploadDeviceInfo;
    private Button btnUploadDeviceIP;
    private Button btnTestingVersion;
    private Button btnDownload;
    private Button btnEventUpload;

    private String deviceSn = "34466711";//设备的sn （从硬件设备获取 ----》硬件里面的sn  web上都得有相应的设备信息科查看）
    private MqttUtil mqttUtil;
    private ExternalCallEntry externalCallEntry;
    private Context mContext;
    private boolean isConnect = false;
    private String curFirmwareVersion = "0.0.3";
    private String ipAddress;

    private String productKey_cache = "";
    private String deviceName_cache = "";
    private String deviceSecret_cache = "";
    private String sn_cache = "";

    private static final int DOWNLOADAPK_ID = 13;
    //读写权限---6.0以上需要动态申请
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
    }

    private void initData() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            } else {
                LogUtil.logInfo(TAG + "已有权限");
                FileOperation.getSaveFile(this, AppContants.FOLDERNAME);
            }
        } else {
            LogUtil.logInfo(TAG + "小于6.0");
        }

        CacheUpdateInfo cacheUpdateInfo = AppSpUtil.getCacheUpdateInfo(this);
        if (cacheUpdateInfo == null) {
            CacheUpdateInfo newCacheUpdateInfo = new CacheUpdateInfo();
            AppSpUtil.setCacheUpdateInfo(this, newCacheUpdateInfo);
        } else {
            if (cacheUpdateInfo.updateItemInfos.size() != 0) {
                LogUtil.logInfo(TAG + "缓存中有记录-----》需要启动服务更新了");
            }
        }
        mContext = this;
        externalCallEntry = IOTPublicApplication.getExternalEnter();
        mqttUtil = IOTPublicApplication.getMqttUtil();
    }

    private void initView() {
        btnUploadDeviceInfo = (Button) findViewById(R.id.btn_uploaddeviceinfo);
        btnUploadDeviceIP = (Button) findViewById(R.id.btn_uploaddeviceip);
        btnTestingVersion = (Button) findViewById(R.id.btn_testingversion);
        btnDownload = (Button) findViewById(R.id.btn_download);
        btnActive = (Button) findViewById(R.id.btn_active);
        btnEventUpload = (Button) findViewById(R.id.btn_uploadeventinfo);

        btnUploadDeviceInfo.setOnClickListener(this::onClick);
        btnUploadDeviceIP.setOnClickListener(this::onClick);
        btnTestingVersion.setOnClickListener(this::onClick);
        btnDownload.setOnClickListener(this::onClick);
        btnActive.setOnClickListener(this::onClick);
        btnEventUpload.setOnClickListener(this::onClick);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
                FileOperation.getSaveFile(this, AppContants.FOLDERNAME);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.logInfo(TAG + "get token");
        externalCallEntry.getDeviceToken(this, new ExternalAccessCallback<String>() {
            @Override
            public void success(String data) {
                LogUtil.logInfo(TAG + "get token success");
                AppSpUtil.setAccessToken(mContext, data);
                externalCallEntry = IOTPublicApplication.updateEntryData(data, AppSpUtil.getEquipmentInfo(mContext));
            }

            @Override
            public void fail(String msg) {
                LogUtil.logInfo(TAG + "get token fail");
            }
        });

        //检测是否需要从服务端拉取设备信息
        externalCallEntry.isNeedUpdateCacheDeviceInfo(deviceSn, new ExternalAccessUpdateCallback<String>() {

            @Override
            public void needUpdate(String data) {
                LogUtil.logInfo(TAG + "need update local equipmentinfo");
                externalCallEntry.getDeviceInfoBySn(data, new ExternalAccessCallback<EquipmentInfo>() {
                    @Override
                    public void success(EquipmentInfo data) {
                        LogUtil.logInfo(TAG + "get deviceInfo success");
                        AppSpUtil.setEquipmentInfo(mContext, data);
                        externalCallEntry = IOTPublicApplication.updateEntryData(AppSpUtil.getAccessToken(mContext), data);
                    }

                    @Override
                    public void fail(String msg) {
                        LogUtil.logInfo(TAG + "get deviceInfo fail");
                    }
                });
            }

            @Override
            public void doNotUpdate() {
                LogUtil.logInfo(TAG + "do not need update");
            }

            @Override
            public void testingFail() {
                LogUtil.logInfo(TAG + "testing fail");
            }
        });
        if (mqttUtil == null) {
            LogUtil.logInfo(TAG + "mqtt connect");
            EquipmentInfo equipmentInfo = AppSpUtil.getEquipmentInfo(mContext);
            IOTPublicApplication.connectMqtt(equipmentInfo.productKey, equipmentInfo.deviceName, equipmentInfo.deviceSecret, new ConnectCallback() {
                @Override
                public void connectSuccess() {
                    LogUtil.logInfo(TAG + "mqtt connect success");
                    isConnect = true;
                }

                @Override
                public void connectFail() {
                    LogUtil.logInfo(TAG + "mqtt connect fail");
                    isConnect = false;
                }

                @Override
                public void sendMsgComplete() {
                    AppUtils.showToast(mContext, "信息上报成功");
                }

                @Override
                public void sendMsgFail() {
                    AppUtils.showToast(mContext, "信息上报失败");
                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_uploaddeviceinfo:
                if (isConnect) {
                    mqttUtil = IOTPublicApplication.getMqttUtil();
                    LogUtil.logInfo(TAG + "走mqtt上报设备信息");
                    EquipmentInfo equipmentInfo = AppSpUtil.getEquipmentInfo(mContext);
                    String topic = String.format(AppContants.topic_prop, equipmentInfo.productKey, equipmentInfo.deviceName);
                    String jsonString = JsonUtil.getJsonString(new DeviceInfo(equipmentInfo.deviceName, AppUtils.getVersionName(mContext), curFirmwareVersion));
                    mqttUtil.publishDeviceInfo(topic, jsonString);
                } else {
                    LogUtil.logInfo(TAG + "mqtt connect fail");
                }
                break;
            case R.id.btn_uploaddeviceip:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String ip = NetUtils.getNetIp();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ipAddress = ip;
                                LogUtil.logInfo(TAG + "设备当前的外网IP是：" + ipAddress);
                                externalCallEntry.uploadDeviceIpAddress(ipAddress, new ExternalAccessCallback<String>() {
                                    @Override
                                    public void success(String data) {
                                        AppUtils.showToast(mContext, "IP上报成功");
                                    }

                                    @Override
                                    public void fail(String msg) {
                                        AppUtils.showToast(mContext, "IP上报成功");
                                    }
                                });
                            }
                        });
                    }
                }).start();
                break;
            case R.id.btn_testingversion:
                LogUtil.logInfo(TAG + "检测是否有新的固件需要下载更新");
                externalCallEntry.getNewVersionInfo(AppContants.UPDATETYPE_SOFTWARE, curFirmwareVersion, AppContants.PRODUCT_CATEGORY, AppContants.PRODUCT_NAME, new ExternalAccessUpdateCallback<GetNewVersionBean>() {
                    @Override
                    public void needUpdate(GetNewVersionBean data) {
                        LogUtil.logInfo(TAG + "通知服务端需要推送最新的固件");
                        externalCallEntry.noticeServerUpdate("2021-08-06 17:04:12", data, new ExternalAccessCallback<String>() {
                            @Override
                            public void success(String data) {
                                LogUtil.logInfo(TAG + "notice  success");
                            }

                            @Override
                            public void fail(String msg) {
                                LogUtil.logInfo(TAG + "notice  fail");
                            }
                        });
                    }

                    @Override
                    public void doNotUpdate() {
                        AppUtils.showToast(mContext, "不用更新");
                    }

                    @Override
                    public void testingFail() {
                        AppUtils.showToast(mContext, "检测失败");
                    }
                });
                break;
            case R.id.btn_download:
//                String downloadUrl = "/qqmi/aphone_p2p/TencentVideo_V6.0.0.14297_848.apk";
//                Intent intent = new Intent(MainActivity.this, DownloadIntentNewService.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("download_url", downloadUrl);
//                bundle.putInt("download_id", DOWNLOADAPK_ID);
//                bundle.putString("download_file", downloadUrl.substring(downloadUrl.lastIndexOf('/') + 1));
////                bundle.putString("download_file", "qwer");
//                bundle.putString("updateType", "1");
//                bundle.putString("msgid", "2355676");
//                intent.putExtras(bundle);
//                startService(intent);
                break;
            case R.id.btn_active:
                if (TextUtils.isEmpty(deviceSn)) {
                    Toast.makeText(this, "sn码不能为空！！", Toast.LENGTH_SHORT).show();
                } else {
                    LogUtil.logInfo(TAG + "进行设备激活");
                    externalCallEntry.getDeviceInfoBySn(deviceSn, new ExternalAccessCallback<EquipmentInfo>() {
                        @Override
                        public void success(EquipmentInfo data) {
                            LogUtil.logInfo(TAG + "获取成功" + data.deviceName);
                            deviceName_cache = data.deviceName;
                            deviceSecret_cache = data.deviceSecret;
                            productKey_cache = data.productKey;
                            sn_cache = data.sn;
//                            AppSpUtil.setEquipmentInfo(mContext);
                            externalCallEntry = IOTPublicApplication.updateEntryData(AppSpUtil.getAccessToken(mContext), data);
                            externalCallEntry.activeDevice(new ExternalAccessCallback<String>() {
                                @Override
                                public void success(String data) {
                                    LogUtil.logInfo(TAG + "激活成功");
                                    AppUtils.showToast(mContext, "激活成功");
                                    LogUtil.logInfo(TAG + "把激活成功的数据  存入外部的txt文档");
                                    EquipmentInfo equipmentInfo = new EquipmentInfo();
                                    equipmentInfo.deviceName = deviceName_cache;
                                    equipmentInfo.deviceSecret = deviceSecret_cache;
                                    equipmentInfo.productKey = productKey_cache;
                                    equipmentInfo.sn = sn_cache;
                                    String jsonString = JsonUtil.getJsonString(equipmentInfo);
                                    boolean result = FileOperation.writeTxt(jsonString, mContext, AppContants.FOLDERNAME);
                                    if (result) {
                                        AppSpUtil.setEquipmentInfo(mContext, equipmentInfo);
                                    } else {
                                        AppUtils.showToast(mContext, "激活信息存入本地 失败");
                                    }

                                }

                                @Override
                                public void fail(String msg) {
                                    LogUtil.logInfo(TAG + "激活失败");
                                    AppUtils.showToast(mContext, "激活失败");
                                }
                            });

                        }

                        @Override
                        public void fail(String msg) {
                            LogUtil.logInfo(TAG + "获取失败");
                            AppUtils.showToast(mContext, "激活失败");
                        }
                    });
                }
                break;
            case R.id.btn_uploadeventinfo:
                if (isConnect) {
                    mqttUtil = IOTPublicApplication.getMqttUtil();
                    LogUtil.logInfo(TAG + "走mqtt上报event信息");
                    EquipmentInfo equipmentInfo = AppSpUtil.getEquipmentInfo(mContext);
                    String topic = String.format(AppContants.topic_event, equipmentInfo.productKey, equipmentInfo.deviceName);
                    String jsonString = JsonUtil.getJsonString(new EventInfo(equipmentInfo.deviceName, "发卡出故障了"));
                    mqttUtil.publishDeviceInfo(topic, jsonString);
                } else {
                    LogUtil.logInfo(TAG + "mqtt connect fail");
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IOTPublicApplication.disConnectMqtt();
    }

}