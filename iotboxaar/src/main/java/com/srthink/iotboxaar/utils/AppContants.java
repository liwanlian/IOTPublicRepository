package com.srthink.iotboxaar.utils;

/**
 * @author liwanlian
 * @date 2021/6/25 9:54
 */
public class AppContants {
    public enum handleServerInstructionResult {
        RESULT_SUCCESS("成功"),
        RESULT_UNKNOW("未知异常");
        String result;

        handleServerInstructionResult(String result) {
            this.result = result;
        }

        public String getResult() {
            return result;
        }
    }
}
