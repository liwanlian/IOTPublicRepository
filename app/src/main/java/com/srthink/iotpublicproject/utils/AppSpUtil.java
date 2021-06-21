package com.srthink.iotpublicproject.utils;

import android.content.Context;

import com.srthink.iotboxaar.models.EquipmentInfo;
import com.srthink.iotpublicproject.R;
import com.srthink.iotpublicproject.models.CacheUpdateInfo;
import com.srthink.iotpublicproject.models.NeedNoticeInfo;


/**
 * @author liwanlian
 * @date 2021/5/13 13:57
 */
public class AppSpUtil {
    //token
    public static String getAccessToken(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString(R.string.Key_Token);
    }

    //token
    public static void setAccessToken(Context context, String token) {
        SharedPreferencesUtil.getInstance(context).putString(R.string.Key_Token, token);
    }

    //设备信息
    public static EquipmentInfo getEquipmentInfo(Context context) {
        return SharedPreferencesUtil.getInstance(context).get(R.string.Key_Equip, EquipmentInfo.class);
    }

    public static void setEquipmentInfo(Context context, EquipmentInfo equipmentInfo) {
        SharedPreferencesUtil.getInstance(context).put(R.string.Key_Equip, equipmentInfo);
    }

    //设备随机数-----mqtt连接的时候时需要的clientid其中一部分由随机数拼接
    public static String getRandomCache(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString(R.string.Key_random);
    }

    public static void setRandomCache(Context context, String random) {
        SharedPreferencesUtil.getInstance(context).putString(R.string.Key_random, random);
    }

    //需要更新的记录
    public static CacheUpdateInfo getCacheUpdateInfo(Context context) {
        return SharedPreferencesUtil.getInstance(context).get(R.string.Key_cacheupdate, CacheUpdateInfo.class);
    }

    public static void setCacheUpdateInfo(Context context, CacheUpdateInfo cacheUpdateInfo) {
        SharedPreferencesUtil.getInstance(context).put(R.string.Key_cacheupdate, cacheUpdateInfo);
    }

    //需要通知服务器的更新情况合集
    public static NeedNoticeInfo getNeedNoticeInfo(Context context) {
        return SharedPreferencesUtil.getInstance(context).get(R.string.Key_neednotice, NeedNoticeInfo.class);
    }

    public static void setNeedNoticeInfo(Context context, NeedNoticeInfo needNoticeInfo) {
        SharedPreferencesUtil.getInstance(context).put(R.string.Key_neednotice, needNoticeInfo);
    }
}
