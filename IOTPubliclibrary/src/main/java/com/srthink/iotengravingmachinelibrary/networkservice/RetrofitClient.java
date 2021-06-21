package com.srthink.iotengravingmachinelibrary.networkservice;


import androidx.annotation.NonNull;

import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;
import com.srthink.iotboxaar.utils.LogUtil;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

//import okhttp3.logging.HttpLoggingInterceptor;

public class RetrofitClient {

    private static volatile RetrofitClient instance;
    private APIService apiService;
    private String baseUrl = "http://192.168.1.28:8856/";
    //    private String baseUrl = "http://120.77.240.215:8856/";
    private Retrofit retrofit;
    private OkHttpClient okHttpClient;


    private String mToken;

    private static final String TAG = "RetrofitClient";

    RetrofitClient(String token) {
        mToken = token;
    }

    public static RetrofitClient getInstance(String token) {
        if (instance == null) {
            synchronized (RetrofitClient.class) {
                if (instance == null) {
                    instance = new RetrofitClient(token);
                }
            }
        }
        return instance;
    }

    /**
     * 设置Header
     *
     * @return
     */
    private Interceptor getHeaderInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder();

                HttpUrl.Builder hBuilder = original.url()
                        .newBuilder()
                        .host(original.url().host());// .addQueryParameter("language", getLanguge())
                Request request = requestBuilder.addHeader("Authorization", mToken)
                        .method(original.method(), original.body())
                        .url(hBuilder.build())
                        .build();//header

                Response response = chain.proceed(request);
                LogUtil.logInfo("响应码" + response.code());
                return response;
            }
        };
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

    public OkHttpClient getOkHttpClient() {
        okHttpClient = new OkHttpClient().newBuilder()
                //设置Header
                .addInterceptor(getHeaderInterceptor())
                //设置拦截器
                .addInterceptor(getInterceptor())
                .build();

        return okHttpClient;
    }

    public APIService getApi() {
        //初始化一个client,不然retrofit会自己默认添加一个
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    //设置数据解析器
                    .addConverterFactory(GsonConverterFactory.create())
                    //设置网络请求适配器，使其支持RxJava与RxAndroid
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .client(getOkHttpClient())
                    .build();
        }
        //创建—— 网络请求接口—— 实例
        if (apiService == null) {
            apiService = retrofit.create(APIService.class);
        }
        return apiService;
    }

}
