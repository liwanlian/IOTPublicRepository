package com.srthink.iotboxaar.utils.networks;

/**
 * @author liwanlian
 * @date 2021/5/8 10:48
 */
public class ServerException extends RuntimeException{
    public int code;
    public String message;

    public ServerException(Throwable cause, int code) {
        super(cause);
        this.code = code;
    }
}
