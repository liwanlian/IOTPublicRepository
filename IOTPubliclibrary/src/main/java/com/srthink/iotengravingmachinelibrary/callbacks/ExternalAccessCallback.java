package com.srthink.iotengravingmachinelibrary.callbacks;

/**
 * @author liwanlian
 * @date 2021/6/11 15:14
 */
public interface ExternalAccessCallback<T> {
    void success(T data);

    void fail(String msg);
}
