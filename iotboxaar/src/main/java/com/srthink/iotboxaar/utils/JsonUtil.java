package com.srthink.iotboxaar.utils;

import com.google.gson.Gson;

/**
 * json工具类
 *
 * @author liwanlian
 * @date 2020/12/31 15:58
 */
public class JsonUtil {
    private static Gson gson = new Gson();

    public static <T extends Object> T getObject(String jsonString, Class<T> clazz) {
        return gson.fromJson(jsonString, clazz);
    }

    public static String getJsonString(Object object) {
        return gson.toJson(object);
    }

    public static <T> T fromJson(String json, Class<? extends T> clz) {
        T rtn = gson.fromJson(json, clz);

        return rtn;
    }
}
