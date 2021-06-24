package com.srthink.iotboxaar.utils.mqtt;

/**
 * @author liwanlian
 * @date 2021/6/22 18:13
 */
public class TopicUtils {
    public static String topic_prop = "topic/iot/sys/%s/%s/prop";
    //    <!--    订阅的主题  更新通知的topic-->
    public static String topic_update = "topic/iot/sys/%s/%s/upgrade/down";
    //    <!--    更新情况反馈的topic-->
    public static String topic_notice = "topic/iot/sys/%s/%s/upgrade/reply";
    //    服务调用返回结果上报
    public static String topic_msgreply = "topic/iot/sys/%s/%s/message/reply";
    //服务指令通知订阅指令
    public static String topic_serverinstrution = "topic/iot/sys/%s/%s/service/invoke";
}
