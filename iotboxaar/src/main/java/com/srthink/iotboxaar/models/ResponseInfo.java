package com.srthink.iotboxaar.models;

/**
 * @author liwanlian
 * @date 2021/6/24 15:25
 */
public class ResponseInfo {

    /**
     * messageId :
     * result : {"msg":"","code":""}
     */

    public String messageId;
    public ResultBean result;

    public ResponseInfo(String messageId, ResultBean result) {
        this.messageId = messageId;
        this.result = result;
    }

    public static class ResultBean {
        /**
         * msg :
         * code :
         */

        public String msg;
        public int code;

        public ResultBean(String msg, int code) {
            this.msg = msg;
            this.code = code;
        }
    }
}
