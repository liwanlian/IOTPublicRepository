package com.srthink.iotengravingmachinelibrary.callbacks;

/**
 * @author liwanlian
 * @date 2021/6/11 15:14
 */
public interface ExternalAccessUpdateCallback<T> {

    void needUpdate(T data);

    void doNotUpdate();

    void testingFail();
}
