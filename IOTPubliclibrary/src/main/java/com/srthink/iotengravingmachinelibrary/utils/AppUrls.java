package com.srthink.iotengravingmachinelibrary.utils;

/**
 * @author liwanlian
 * @date 2021/6/16 17:39
 */
public class AppUrls {
    public AppUrls() {
    }

    //     <!--设备激活url-->
    public String url_active = "/sys/dev/%s/%s/active";
    //    <!--故障上报url-->
    public String url_uploadalamr = "/sys/dev/%s/%s/alarm/upload";
    //    <!--故障列表获取url-->
    public String url_getalarm = "/sys/dev/%s/%s/alarm/list";
    //    <!--反馈上报url-->
    public String url_feedback = "/sys/dev/%s/%s/feedBack/upload";
    //    <!--反馈列表获取url-->
    public String url_getfeedback = "/sys/dev/%s/%s/feedBack/list";
    //    <!--故障详情url-->
    public String url_getalarmdetail = "/sys/dev/%s/%s/alarm/detail";
    //    <!--查询最新的版本信息-->
    public String url_querynewversion = "/sys/dev/%s/%s/query/version/latest";
    //    <!--更新固件、软件-->
    public String url_update = "/sys/dev/%s/%s/upgrade";
    //    <!--设备外网地址上报-->
    public String url_uploadip = "/sys/dev/%s/%s/ip";
}
