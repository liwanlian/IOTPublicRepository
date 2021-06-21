package com.srthink.iotengravingmachinelibrary.bean;

/**
 * @author liwanlian
 * @date 2021/5/27 18:58
 * 通过设备sn获取设备的一些信息
 */
public class GetDataBySnBean {

    /**
     * createTime : null
     * currentPage : 1
     * deleted : 0
     * description :
     * devModel : T5
     * devName : WJYZA7-C2FRM3
     * devSn : 34466798
     * id : 0
     * pageSize : 15
     * productKey : 2E1PZO6UWQ8837347
     * responseMessageId : 1165294311110082561
     * responseTime : 2021-05-27 19:11:46
     * secret : c64add462d1b50021d95eaab5e5f56a2
     * updateTime : null
     */

    private String createTime;
    private Integer currentPage;
    private Integer deleted;
    private String description;
    private String devModel;
    private String devName;
    private String devSn;
    private Integer id;
    private Integer pageSize;
    private String productKey;
    private String responseMessageId;
    private String responseTime;
    private String secret;
    private String updateTime;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDevModel() {
        return devModel;
    }

    public void setDevModel(String devModel) {
        this.devModel = devModel;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public String getDevSn() {
        return devSn;
    }

    public void setDevSn(String devSn) {
        this.devSn = devSn;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getResponseMessageId() {
        return responseMessageId;
    }

    public void setResponseMessageId(String responseMessageId) {
        this.responseMessageId = responseMessageId;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
