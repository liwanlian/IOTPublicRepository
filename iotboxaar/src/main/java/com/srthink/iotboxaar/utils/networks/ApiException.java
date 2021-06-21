package com.srthink.iotboxaar.utils.networks;

/**
 * @author liwanlian
 * @date 2021/5/8 10:48
 */
public class ApiException extends Exception {
    public int code;
    public String message;

    public ApiException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;

    }
}
