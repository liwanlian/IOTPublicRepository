package com.srthink.iotengravingmachinelibrary.callbacks;

/**
 * @author liwanlian
 * @date 2021/6/11 16:26
 */
public interface InternalCallback<T> {
    void success(T data);

    void fail(String msg);
}
