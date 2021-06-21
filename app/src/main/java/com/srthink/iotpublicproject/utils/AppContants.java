package com.srthink.iotpublicproject.utils;

/**
 * Created by liwanlian
 * on 2021/5/16 23:16
 **/
public class AppContants {
    public static final String MQTT_HOST = "tcp://120.77.240.215:9876";//1883
    //设备网络状态类型
    public static final int NETWORK_TYPE_WIFI = 0;
    public static final int NETWORK_TYPE_DATA = 1;
    public static final int NETWORK_TYPE_NONE = 2;

    //升级的一些状态
    public static final int UPDATE_UPGRADING = 2;//升级中
    public static final int UPDATE_SUCCRSS = 3;//升级成功
    public static final int UPDATE_FAIL = 4;//升级失败

    public static final String UPDATE_UPGRADING_TIP = "升级中";
    public static final String UPDATE_SUCCRSS_TIP = "升级成功";
    public static final String UPDATE_FAIL_TIP = "升级失败";

    public static final String UPDATE_TYPE_SOFTWARE = "3";//软件升级

    public static final String PRODUCT_CATEGORY = "EngravingMachine";//产品品类（web创建产品的时候生成的 同类产品的值一样）
    public static final String PRODUCT_NAME = "EngravingMachine";//产品名称（web创建产品的时候生成的 同类产品的值一样）

    public static final String FOLDERNAME = "IOTPublicDemo";//文件夹的名字

    public static final String UPDATETYPE_FIRMWARE = "upgradeDevFirmWare";//更新固件
    public static final String UPDATETYPE_SOFTWARE = "upgradeDevSoft";//更新软件


    //       <!--    mqtt通信需要用到的topic-->
//    <!--    物模型相关设备对应的属性信息上报的topic-->
    public static String topic_prop = "topic/iot/sys/%s/%s/prop";
    //    <!--    订阅的主题  更新通知的topic-->
    public static String topic_update = "topic/iot/sys/%s/%s/upgrade/down";
    //    <!--    更新情况反馈的topic-->
    public static String topic_notice = "topic/iot/sys/%s/%s/upgrade/reply";

}
