package com.srthink.iotengravingmachinelibrary.networkservice;

import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by liwanlian
 * on 2021/5/22 22:45
 **/
public class RetrofitDownload {
    private static final int DEFAULT_TIMEOUT = 10;
    private static final String TAG = "RetrofitDownload";

    private APIService apiService;

    private OkHttpClient okHttpClient;

    public static String baseUrl = APIService.BASE_URL;

    private static RetrofitDownload sIsntance;

    public static RetrofitDownload getInstance() {
        if (sIsntance == null) {
            synchronized (RetrofitDownload.class) {
                if (sIsntance == null) {
                    sIsntance = new RetrofitDownload();
                }
            }
        }
        return sIsntance;
    }

    /**
     * 设置拦截器 打印日志
     *
     * @return
     */
    private Interceptor getInterceptor() {
        return new LoggingInterceptor.Builder()
                .loggable(true)
                .setLevel(Level.BASIC)
                .log(Platform.INFO)
                .request("Request")
                .response("Response")
                .build();
    }

    private RetrofitDownload() {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(getInterceptor())
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
        apiService = retrofit.create(APIService.class);
    }

    public APIService getApiService() {
        return apiService;
    }

}
