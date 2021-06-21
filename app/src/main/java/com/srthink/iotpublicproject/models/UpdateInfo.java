package com.srthink.iotpublicproject.models;

/**
 * @author liwanlian
 * @date 2021/5/31 14:13
 */
public class UpdateInfo {
    /**
     * body : {"devSn":"34466798","deviceCode":"WJYZA7-C2FRM3","firmwareVersion":"1.0.3","id":"1168646327580491776","messageId":"1168646327739875328","productKey":"2E1PZO6UWQ8837347","productName":"�����3","pushType":2,"signCode":"930B323A4EAD55B0A4974D0100AF638F","signType":"md5","taskId":"1168646327580491776","upgradeType":1,"url":"http://acc-huahui.oss-cn-shenzhen.aliyuncs.com/IOT/softFrimWare/dev/02405025938212945705845855161745%26%26app-life-release.bin?Expires=1937875793&OSSAccessKeyId=STS.NUXPQ2Bvmzh2ncWksu3M9Qjsa&Signature=wTy3PgOEnOqEIPI2hWC%2FnfStNbc%3D&security-token=CAISqgJ1q6Ft5B2yfSjIr5btG%2BuGr6lMzarZbEXmj3MgP8IVvq%2FYgzz2IHlOe3duBOEYvvw0mGpZ7%2F4Ylol4S5JDVFHUbM1Y9Y5L8QKnYIfb%2Fcuz5LgFmYCjX2OUDkYxKgEWl6KrIunGc9KBNnr29EYqs4aYGBymW1u6S%2Brr7bdsctUQWCShcDNCH604DwB%2BqcgcRxSzWPG2KUyo%2BCmyanBloQ1hk2hyxL2iy8mHkHrkgUb91%2FUeqvaWQP2tZNI%2BO4xkAZXnnr40VNKYjHUBs0Ubqfwn1%2FwboG%2BehLzHXQkNuSfhGvHP79hiIDV%2BYqUHAKNepJD%2B76Yh4LONydmmlUYVZr0PAn2HHprTyc%2FFCf6vMc0iabH4NnLC39aCLJDptBk%2BZnYWJLbnjHfb2NAaGoABsxix8KBQFlwKJz6Y7pjiVc8z8yz%2Fd0vFOgOeMVflL7GIlmD7flXgKlj9DcrwRZgz07HdV1WavPZYZDiLTpJ%2FxhsPIgQHq6JkdgdZbpkPihD7iuCLxq4CczOtn2COLAtjeISoie8T4BaZnWpeMeFw%2F%2B1MqjxeFDwUxM5WhQwt%2BdA%3D"}
     * deviceCode : WJYZA7-C2FRM3
     * messageId : 1168646327739875328
     * productCode : 2E1PZO6UWQ8837347
     * timestamp : 1622515797881
     */

    private BodyBean body;
    private String deviceCode;
    private String messageId;
    private String productCode;
    private Long timestamp;

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public static class BodyBean {
        /**
         * devSn : 34466798
         * deviceCode : WJYZA7-C2FRM3
         * firmwareVersion : 1.0.3
         * id : 1168646327580491776
         * messageId : 1168646327739875328
         * productKey : 2E1PZO6UWQ8837347
         * productName : �����3
         * pushType : 2
         * signCode : 930B323A4EAD55B0A4974D0100AF638F
         * signType : md5
         * taskId : 1168646327580491776
         * upgradeType : 1
         * fileName
         * url : http://acc-huahui.oss-cn-shenzhen.aliyuncs.com/IOT/softFrimWare/dev/02405025938212945705845855161745%26%26app-life-release.bin?Expires=1937875793&OSSAccessKeyId=STS.NUXPQ2Bvmzh2ncWksu3M9Qjsa&Signature=wTy3PgOEnOqEIPI2hWC%2FnfStNbc%3D&security-token=CAISqgJ1q6Ft5B2yfSjIr5btG%2BuGr6lMzarZbEXmj3MgP8IVvq%2FYgzz2IHlOe3duBOEYvvw0mGpZ7%2F4Ylol4S5JDVFHUbM1Y9Y5L8QKnYIfb%2Fcuz5LgFmYCjX2OUDkYxKgEWl6KrIunGc9KBNnr29EYqs4aYGBymW1u6S%2Brr7bdsctUQWCShcDNCH604DwB%2BqcgcRxSzWPG2KUyo%2BCmyanBloQ1hk2hyxL2iy8mHkHrkgUb91%2FUeqvaWQP2tZNI%2BO4xkAZXnnr40VNKYjHUBs0Ubqfwn1%2FwboG%2BehLzHXQkNuSfhGvHP79hiIDV%2BYqUHAKNepJD%2B76Yh4LONydmmlUYVZr0PAn2HHprTyc%2FFCf6vMc0iabH4NnLC39aCLJDptBk%2BZnYWJLbnjHfb2NAaGoABsxix8KBQFlwKJz6Y7pjiVc8z8yz%2Fd0vFOgOeMVflL7GIlmD7flXgKlj9DcrwRZgz07HdV1WavPZYZDiLTpJ%2FxhsPIgQHq6JkdgdZbpkPihD7iuCLxq4CczOtn2COLAtjeISoie8T4BaZnWpeMeFw%2F%2B1MqjxeFDwUxM5WhQwt%2BdA%3D
         */

        private String devSn;
        private String deviceCode;
        private String firmwareVersion;
        private String id;
        private String messageId;
        private String productKey;
        private String productName;
        private Integer pushType;
        private String signCode;
        private String signType;
        private String taskId;
        private Integer upgradeType;
        private String url;
        private String fileName;

        public String getDevSn() {
            return devSn;
        }

        public void setDevSn(String devSn) {
            this.devSn = devSn;
        }

        public String getDeviceCode() {
            return deviceCode;
        }

        public void setDeviceCode(String deviceCode) {
            this.deviceCode = deviceCode;
        }

        public String getFirmwareVersion() {
            return firmwareVersion;
        }

        public void setFirmwareVersion(String firmwareVersion) {
            this.firmwareVersion = firmwareVersion;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }

        public String getProductKey() {
            return productKey;
        }

        public void setProductKey(String productKey) {
            this.productKey = productKey;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public Integer getPushType() {
            return pushType;
        }

        public void setPushType(Integer pushType) {
            this.pushType = pushType;
        }

        public String getSignCode() {
            return signCode;
        }

        public void setSignCode(String signCode) {
            this.signCode = signCode;
        }

        public String getSignType() {
            return signType;
        }

        public void setSignType(String signType) {
            this.signType = signType;
        }

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public Integer getUpgradeType() {
            return upgradeType;
        }

        public void setUpgradeType(Integer upgradeType) {
            this.upgradeType = upgradeType;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
    }

    //    "deviceCode" : "ffszjwpa2gpukyps",//设备编码
//            "firmwareVersion" : "1.0.1",//固件版本
//            "messageId" : "7872484049906691856",//消息ID，回执时需要这个
//            "seqNo" : 1,//版本序列号，可以根据这个序列号和本地序列号对比决定是否升级
//            "signCode" : "12344",//文件的md5值
//            "url" : "http://www.baidu.com"
//},
//        "messageId" : "7872484049906691856",
//        "timestamp" : 1618736556058


//    public String deviceCode;//设备编码
//    public String firmwareVersion;
//    public String messageId;
//    public String seqNo;
//    public String signCode;
//    public String url;


}

