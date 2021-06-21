package com.srthink.iotengravingmachinelibrary.bean.NetworkRequest;

/**
 * @author liwanlian
 * @date 2021/5/28 14:26
 * 获取所有的故障的信息  、获取所有反馈的信息
 */
public class GetAlarmListReq extends Req{
    public String devSn;
    public String devName;
    public String productKey;
    public int currentPage;
    public int pageSize;

    public GetAlarmListReq(String token, String devSn, String devName, String productKey, int currentPage, int pageSize) {
        super(token);
        this.devSn = devSn;
        this.devName = devName;
        this.productKey = productKey;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }
}
