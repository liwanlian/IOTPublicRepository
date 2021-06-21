package com.srthink.iotengravingmachinelibrary.utils;

import com.srthink.iotboxaar.utils.LogUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;
import retrofit2.adapter.rxjava3.HttpException;

/**
 * 接口访问 重连（出现网络相关异常时就会启动）
 */
public class RetryWithDelay implements Function<Observable<? extends Throwable>, Observable<?>> {

    private int maxRetries = 3;//最大出错重试次数
    private int retryDelayMillis = 1000;//重试间隔时间
    private int retryCount = 0;//当前出错重试次数

    private static final String TAG = "RetryWithDelay";

    public RetryWithDelay() {
    }

    public RetryWithDelay(int maxRetries, int retryDelayMillis) {
        this.maxRetries = maxRetries;
        this.retryDelayMillis = retryDelayMillis;
    }

    @Override
    public Observable<?> apply(Observable<? extends Throwable> observable) throws Exception {
        return observable
                .flatMap(new Function<Throwable, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Throwable throwable) throws Exception {
                        if (throwable instanceof IOException) {
                            LogUtil.logInfo(TAG + "网络的问题  可以进行重连");
                            if (++retryCount <= maxRetries) {
                                return Observable.just(1).delay(1000, TimeUnit.MILLISECONDS);//1s后重连
                            } else {
                                LogUtil.logInfo(TAG + "请求次数已达到最大限制-----IOException");
                                return Observable.error(new Throwable("重连已达到最大次数限制"));
                            }
                        } else {
                            if (throwable instanceof HttpException) {
                                if (++retryCount <= maxRetries) {
                                    return Observable.just(1).delay(1000, TimeUnit.MILLISECONDS);//1s后重连
                                } else {
                                    LogUtil.logInfo(TAG + "请求次数已达到最大限制-----HttpException");
                                    return Observable.error(new Throwable("重连已达到最大次数限制"));
                                }
//                                return Observable.error(throwable);
                            } else {
                                return Observable.error(new Throwable(throwable.toString()));
                            }
                        }
                    }
                });
    }
}