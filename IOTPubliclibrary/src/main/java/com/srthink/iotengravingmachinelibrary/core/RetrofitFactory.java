package com.srthink.iotengravingmachinelibrary.core;

import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;
import com.srthink.iotboxaar.utils.LogUtil;
import com.srthink.iotengravingmachinelibrary.networkservice.APIService;
import com.srthink.iotengravingmachinelibrary.utils.RetryWithDelay;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitFactory {
    private static final int TIME_OUT_SECNOD = 15;
    private static OkHttpClient.Builder mBuilder;

    private static Retrofit getDownloadRetrofit(DownloadListener downloadListener) {
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request.Builder requestBuilder = originalRequest.newBuilder()
                        .addHeader("Authorization", "mToken")
                        .method(originalRequest.method(), originalRequest.body());
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };

        if (null == mBuilder) {
            mBuilder = new OkHttpClient.Builder()
                    .connectTimeout(TIME_OUT_SECNOD, TimeUnit.SECONDS)
                    .readTimeout(TIME_OUT_SECNOD, TimeUnit.SECONDS)
                    .writeTimeout(TIME_OUT_SECNOD, TimeUnit.SECONDS)
                    .addInterceptor(headerInterceptor)
                    .addInterceptor(getInterceptor())
                    .addInterceptor(new DownloadInterceptor(downloadListener));
        }
        //http://imtt.dd.qq.com/
        return new Retrofit.Builder()
                .baseUrl(APIService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(mBuilder.build())
                .build();
    }

    /**
     * 设置拦截器 打印日志
     *
     * @return
     */
    private static Interceptor getInterceptor() {
        return new LoggingInterceptor.Builder()
                .loggable(true)
                .setLevel(Level.BASIC)
                .log(Platform.INFO)
                .request("Request")
                .response("Response")
                .build();
    }

    /**
     * 取消网络请求
     */
    public static void cancel(Disposable d) {
        if (null != d && !d.isDisposed()) {
            d.dispose();
            LogUtil.logInfo("取消下载。。。。。");
        }
    }

    /**
     * 下载文件请求
     */
    public static void downloadFile(String url, long startPos, DownloadListener downloadListener, io.reactivex.rxjava3.core.Observer<ResponseBody> observer) {
        getDownloadRetrofit(downloadListener).create(APIService.class).executeDownload("bytes=" + startPos + "-", url).retryWhen(new RetryWithDelay()).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

}
