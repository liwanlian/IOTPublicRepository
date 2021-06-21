package com.srthink.iotengravingmachinelibrary.bean.NetworkRequest;

/**
 * @author liwanlian
 * @date 2021/5/27 18:54
 * 通过设备sn获取相应的设备信息（eg:设备名称  设备秘钥  产品key信息）
 */
public class GetInfoBySn {
    public String devSn;

    public GetInfoBySn(String devSn) {
        this.devSn = devSn;
    }
}
