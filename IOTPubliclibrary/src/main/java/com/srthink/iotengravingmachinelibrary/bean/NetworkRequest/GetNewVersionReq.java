package com.srthink.iotengravingmachinelibrary.bean.NetworkRequest;

/**
 * @author liwanlian
 * @date 2021/6/15 16:12
 */
public class GetNewVersionReq {

    /**
     * devName :
     * productKey :
     * softCategory :
     * softProductName :
     */

    public String devName;
    public String productKey;
    public String softCategory;
    public String softProductName;

    public GetNewVersionReq(String devName, String productKey, String softCategory, String softProductName) {
        this.devName = devName;
        this.productKey = productKey;
        this.softCategory = softCategory;
        this.softProductName = softProductName;
    }
}
