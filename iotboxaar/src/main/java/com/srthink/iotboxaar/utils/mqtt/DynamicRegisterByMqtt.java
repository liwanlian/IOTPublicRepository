package com.srthink.iotboxaar.utils.mqtt;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.srthink.iotboxaar.models.MqttRegesterInfo;
import com.srthink.iotboxaar.utils.LogUtil;

import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author liwanlian
 * @date 2021/5/17 9:09
 */
public class DynamicRegisterByMqtt {
    // 定义加密方式。可选MAC算法：HmacMD5、HmacSHA1、HmacSHA256，需和signmethod取值一致。
    private static final String HMAC_ALGORITHM = "hmacsha1";

    private static final String TAG = "DynamicRegisterByMqtt";

    /**
     * 动态注册。
     *
     * @param productKey    产品的ProductKey
     * @param productSecret 产品密钥
     * @param deviceName    设备名称
     * @throws Exception
     */
    public MqttRegesterInfo register(Context context, String clientId, String random, String productKey, String productSecret, String deviceName) throws Exception {
        // 接入域名
        String broker = "";
        // 表示客户端ID，使用设备端的mac地址，去掉符号  “-”
//        String clientId = "509A4C399096";
//        String clientId = AppUtils.getMacAddressFromIp(IOTCrashRegisterApplication.getApp()).replace(":", "");//mac 地址
//        // 获取随机值,一旦生产，则作为全局标量，下次参数带改值不能修改
//        String random = AppSpUtil.getRandomCache(IOTCrashRegisterApplication.getApp());
//        if (TextUtils.isEmpty(random)) {
//            random = RandomUtils.getRandomsData();
////            AppSpUtil.setRandomCache(context, random);
//        }
        LogUtil.logInfo("random---->" + random);
        // MQTT接入客户端ID,23字符内。
        String mqttClientId = clientId + "+" + random;
        LogUtil.logInfo(TAG + "mqttClientId------>" + mqttClientId);
        LogUtil.logInfo(TAG + "mqttClientId长度------>" + mqttClientId.getBytes().length);

        // MQTT接入用户名。
        String mqttUsername = deviceName + "&" + productKey;
        LogUtil.logInfo(TAG + "mqttUsername---->" + mqttUsername);
        // MQTT接入密码，即签名。
        JSONObject params = new JSONObject();
        params.put("productKey", productKey);
        params.put("deviceName", deviceName);
        params.put("random", random);
        String mqttPassword = sign(params, productSecret);
        LogUtil.logInfo(TAG + "密码------>" + mqttPassword);

        MqttRegesterInfo mqttRegesterInfo = new MqttRegesterInfo();
        mqttRegesterInfo.clientId = mqttClientId;
        mqttRegesterInfo.username = mqttUsername;
        mqttRegesterInfo.password = mqttPassword;
        // 通过MQTT connect报文进行动态注册。
        return mqttRegesterInfo;
    }

    /**
     * 通过MQTT connect报文发送动态注册信息。
     *
     * @param serverURL 动态注册域名地址
     * @param clientId  客户端ID
     * @param username  MQTT用户名
     * @param password  MQTT密码
     */
    @SuppressWarnings("resource")
    private void connect(String serverURL, String clientId, String username, String password) {

    }

    /**
     * 动态注册签名。
     *
     * @param params        签名参数
     * @param productSecret 产品密钥
     * @return 签名十六进制字符串
     */
    private String sign(JSONObject params, String productSecret) {

        // 请求参数按字典顺序排序。
        Set<String> keys = getSortedKeys(params);
        // sign、signMethod除外。
        keys.remove("sign");
        keys.remove("signMethod");

        // 组装签名明文。
        StringBuffer content = new StringBuffer();
        for (String key : keys) {
            content.append(key);
            content.append(params.getString(key));
        }

        // 计算签名。
        String sign = encrypt(content.toString(), productSecret);
        LogUtil.logInfo(TAG + "sign content=" + content);
        LogUtil.logInfo("sign result=" + sign);

        return sign;
    }

    /**
     * 获取JSON对象排序后的key集合。
     *
     * @param json 需要排序的JSON对象
     * @return 排序后的key集合
     */
    private Set<String> getSortedKeys(JSONObject json) {
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
     * @param content 明文
     * @param secret  密钥
     * @return 密文
     */
    private String encrypt(String content, String secret) {
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
     * @param b 二进制数组
     * @return 十六进制字符串
     */
    private String byte2hex(byte[] b) {
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
