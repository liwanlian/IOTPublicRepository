package com.srthink.iotengravingmachinelibrary.utils;

import com.google.gson.JsonParseException;
import com.srthink.iotboxaar.utils.LogUtil;
import com.srthink.iotboxaar.utils.networks.ApiException;
import com.srthink.iotboxaar.utils.networks.ServerException;

import org.json.JSONException;

import java.net.ConnectException;
import java.text.ParseException;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import retrofit2.adapter.rxjava3.HttpException;


/**
 * @author liwanlian
 * @date 2021/5/8 10:21
 */
public class HandleExceptionUtil {
    //对应HTTP的状态码
    private static final int UNAUTHORIZED = 401;
    private static final int NOT_FOUND = 404;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;
    private static final int CODE_1001 = 1001;

    private static final int UNIFIED_CODE = 100;//统一处理的码

    private static long exitHandler = 0L;
    public static long oneSecond = 1000L;

    private static final String TAG = "HandleExceptionUtil";

    public static void initDataN() {
        exitHandler = 0L;
        oneSecond = 1000L;
    }

    public static ApiException handleException(Throwable e) {
        ApiException ex;
        if (e instanceof HttpException) {             //HTTP错误
            HttpException httpException = (HttpException) e;
            ex = new ApiException(e, ERROR.HTTP_ERROR);
            switch (httpException.code()) {
                case UNAUTHORIZED:
                    handleException(UNAUTHORIZED);
                    break;
                case NOT_FOUND:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    ex.message = "网络错误";  //均视为网络错误
                    handleException(UNIFIED_CODE);
                    break;
            }
            return ex;
        } else if (e instanceof ServerException) {    //服务器返回的错误
            ServerException resultException = (ServerException) e;
            ex = new ApiException(resultException, resultException.code);
            ex.message = resultException.message;
            handleException(UNIFIED_CODE);
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            ex = new ApiException(e, ERROR.PARSE_ERROR);
            ex.message = "解析错误";            //均视为解析错误
            handleException(UNIFIED_CODE);
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new ApiException(e, ERROR.NETWORD_ERROR);
            ex.message = "连接失败";  //均视为网络错误
            handleException(UNIFIED_CODE);
            return ex;
        } else {
            ex = new ApiException(e, ERROR.UNKNOWN);
            ex.message = "未知错误";          //未知错误
            handleException(UNIFIED_CODE);
            return ex;
        }
    }

    public class ERROR {
        /**
         * 未知错误
         */
        public static final int UNKNOWN = 1000;
        /**
         * 解析错误
         */
        public static final int PARSE_ERROR = 1001;
        /**
         * 网络错误
         */
        public static final int NETWORD_ERROR = 1002;
        /**
         * 协议出错
         */
        public static final int HTTP_ERROR = 1003;
    }

    /**
     * 防止多个接口同时报异常的时候  确保只处理一次
     *
     * @param i
     */
    private static void handleException(int i) {
        if (System.currentTimeMillis() - exitHandler < oneSecond) {
            LogUtil.logError(TAG + "不要处理");
            return;
        } else {
            exitHandler = System.currentTimeMillis();
            LogUtil.logError(TAG + "需要处理");
            Observable.just(i).take(1).subscribe(new Observer<Integer>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {

                }

                @Override
                public void onNext(@NonNull Integer integer) {
                    LogUtil.logInfo(TAG + "the err code is:" + integer);
                }

                @Override
                public void onError(@NonNull Throwable e) {

                }

                @Override
                public void onComplete() {
//                    Toast.makeText(IOTEngravingMachineApplication.getApp(), "访问有问题", Toast.LENGTH_SHORT).show();
                }
            });
            oneSecond = 10000L;
        }
    }
}
