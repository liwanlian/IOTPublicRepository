package com.srthink.iotboxaar.models;

/**
 * @author liwanlian
 * @date 2021/6/22 17:56
 */
public class MsgReplyInfo {

    /**
     * body : {"printerNum":"12134","deviceCode":"示例数据"}
     * deviceCode : C1N7HA-MVINPT
     * identifier : printclose
     * messageId : 1183925259431051264
     * productCode : R0U4QKGX2T6919222
     * timestamp : 1624334889118
     */

    public BodyBean body;
    public String deviceCode;
    public String identifier;
    public String messageId;
    public String productCode;
    public Long timestamp;

    public MsgReplyInfo(BodyBean body, String deviceCode, String identifier, String messageId, String productCode, Long timestamp) {
        this.body = body;
        this.deviceCode = deviceCode;
        this.identifier = identifier;
        this.messageId = messageId;
        this.productCode = productCode;
        this.timestamp = timestamp;
    }

    public static class BodyBean {
        /**
         * printerNum : 12134
         * deviceCode : 示例数据
         */

        public String printerNum;
        public String deviceCode;

        private int action;
        private String descriptions;


//        public BodyBean(String printerNum, String deviceCode) {
//            this.printerNum = printerNum;
//            this.deviceCode = deviceCode;
//        }


        public BodyBean(String printerNum, String deviceCode, int action, String descriptions) {
            this.printerNum = printerNum;
            this.deviceCode = deviceCode;
            this.action = action;
            this.descriptions = descriptions;
        }
    }
}
