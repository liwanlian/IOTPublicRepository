package com.srthink.iotengravingmachinelibrary.bean.NetworkRequest;

/**
 * @author liwanlian
 * @date 2021/5/13 13:37
 * 获取token
 */
public class GetTokenReq extends Req {
    public String version;
    public String clientId;
    public String signmethod = "hmacsha1";
    public String sign;
    public String productKey;
    public String deviceName;
    public String timestamp;

    public GetTokenReq(String token, String version, String clientId, String signmethod, String sign, String productKey, String deviceName, String timestamp) {
        super(token);
        this.version = version;
        this.clientId = clientId;
        this.signmethod = signmethod;
        this.sign = sign;
        this.productKey = productKey;
        this.deviceName = deviceName;
        this.timestamp = timestamp;
    }
}
