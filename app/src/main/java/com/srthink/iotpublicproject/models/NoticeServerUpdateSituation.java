package com.srthink.iotpublicproject.models;

/**
 * @author liwanlian
 * @date 2021/5/31 14:24
 */
public class NoticeServerUpdateSituation {

//    {"messageId":"1168646327739875328","result":{"code":2,"msg":"升级中","upgradeType":1}}

    private String messageId;
    private String deviceCode;
    private ResultBean result;

    public NoticeServerUpdateSituation(String messageId, ResultBean result) {
        this.messageId = messageId;
        this.result = result;
    }

    public NoticeServerUpdateSituation(String messageId, String deviceCode, ResultBean result) {
        this.messageId = messageId;
        this.deviceCode = deviceCode;
        this.result = result;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * code : 2
         * msg : 升级中
         * <p>
         * code的分类：0 等待  1已发送   2升级中（设备已收到服务端推送过来的信息）  3成功（设备升级成功）   4失败（设备升级失败）  5停止（设备离线 停止推送）
         */

        private Integer code;
        private String msg;
        private int upgradeType;

        public ResultBean(Integer code, String msg, int upgradeType) {
            this.code = code;
            this.msg = msg;
            this.upgradeType = upgradeType;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public int getUpgradeType() {
            return upgradeType;
        }

        public void setUpgradeType(int upgradeType) {
            this.upgradeType = upgradeType;
        }
    }
}
