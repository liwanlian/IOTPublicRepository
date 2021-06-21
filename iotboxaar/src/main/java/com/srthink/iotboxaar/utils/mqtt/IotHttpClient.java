package com.srthink.iotboxaar.utils.mqtt;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.srthink.iotboxaar.utils.AppUtils;
import com.srthink.iotboxaar.utils.DateUtil;
import com.srthink.iotboxaar.utils.LogUtil;

import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author liwanlian
 * @date 2021/5/14 14:59
 */
public class IotHttpClient {
    // 定义加密方式，MAC算法可选以下算法HmacSHA1
    private static final String HMAC_ALGORITHM = "hmacsha1";

    // token有效期7天，失效后需要重新获取。
    private String token = null;


    /**
     * 生成认证请求内容。
     *
     * @param ，认证参数。
     * @return 认证请求消息体。
     */
    public static String authBody(Context context, String productKey, String deviceName, String deviceSecret) {

        // 构建认证请求。
        JSONObject body = new JSONObject();
        try {
            body.put("clientId", productKey + "." + deviceName);
            body.put("deviceName", deviceName);
            body.put("timestamp", DateUtil.getTimeStamp13(DateUtil.getDetailDate(), "yyyy-MM-dd HH:mm:ss"));
            body.put("productKey", productKey);
            body.put("signmethod", HMAC_ALGORITHM);
            body.put("version", AppUtils.getVersionName(context));
            body.put("sign", sign(body, deviceSecret));
        } catch (Exception e) {
            e.printStackTrace();
        }
//
//        System.out.println("----- auth body -----");
//        System.out.println(body.toString());
//        JsonUtil.getJsonString(new GetSignReq(productKey"."+deviceName,deviceName,productKey,sign()))
        return body.toString();
    }

    /**
     * 设备端签名。
     *
     * @param params，签名参数。
     * @param deviceSecret，设备密钥。
     * @return 签名十六进制字符串。
     */
    private static String sign(JSONObject params, String deviceSecret) {

        // 请求参数按字典顺序排序。
        Set<String> keys = getSortedKeys(params);

        // sign、signmethod和version除外。
        keys.remove("sign");
        keys.remove("signmethod");
        keys.remove("version");

        // 组装签名明文。
        StringBuffer content = new StringBuffer();
        for (String key : keys) {
            content.append(key);
            try {
                content.append(params.getString(key));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 计算签名。
        String sign = encrypt(content.toString(), deviceSecret);
        LogUtil.logInfo("sign content=" + content);
        LogUtil.logInfo("sign result=" + sign);

        return sign;
    }

    /**
     * 获取JSON对象排序后的key集合。
     *
     * @param json，需要排序的JSON对象。
     * @return 排序后的key集合。
     */
    private static Set<String> getSortedKeys(JSONObject json) {
        SortedMap<String, String> map = new TreeMap<>();

        for (String key : json.keySet()) {
            String vlaue = json.getString(key);
            map.put(key, vlaue);
        }

        return map.keySet();
    }


    /**
     * 使用HMAC_ALGORITHM加密。
     *
     * @param content，明文。
     * @param secret，密钥。
     * @return 密文。
     */
    private static String encrypt(String content, String secret) {
        try {
            byte[] text = content.getBytes(StandardCharsets.UTF_8);
            byte[] key = secret.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec secretKey = new SecretKeySpec(key, HMAC_ALGORITHM);
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            return byte2hex(mac.doFinal(text));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 二进制转十六进制字符串。
     *
     * @param b，二进制数组。
     * @return 十六进制字符串。
     */
    private static String byte2hex(byte[] b) {
        StringBuffer sb = new StringBuffer();
        for (int n = 0; b != null && n < b.length; n++) {
            String stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1) {
                sb.append('0');
            }
            sb.append(stmp);
        }
        return sb.toString().toUpperCase();
    }
}
