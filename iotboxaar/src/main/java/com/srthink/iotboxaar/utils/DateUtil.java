package com.srthink.iotboxaar.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author liwanlian
 * @date 2021/5/14 16:10
 */
public class DateUtil {
    /**
     * 当前系统时间   XXXX-XX-XX hh:mm;ss格式
     *
     * @return
     */
    public static String getDetailDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        Date now = new Date();
        String time = sdf.format(now);
        return time;
    }

    /**
     * 将传入的日期转成时间戳 13位的时间戳
     *
     * @param time 时间点
     * @param type
     * @return
     */
    public static String getTimeStamp13(String time, String type) {
        SimpleDateFormat sdr = new SimpleDateFormat(type, Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 13);//截取0-13
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return times;
    }

    /**
     * 获取延迟后的时间
     *
     * @param seconds 秒
     * @return
     */
    public static String getDelayTime_Second(int seconds) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sDateFormat.format(new java.util.Date(System.currentTimeMillis() + seconds * 1000));//延后

        return date;
    }

    /**
     * 获取延迟的时间
     *
     * @param mins 分钟
     * @return
     */
    public static String getDelayTime_Minute(int mins) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sDateFormat.format(new java.util.Date(System.currentTimeMillis() + mins * 60 * 1000));
        return date;
    }
}
