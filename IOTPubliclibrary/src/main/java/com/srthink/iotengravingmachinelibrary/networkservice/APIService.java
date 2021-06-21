package com.srthink.iotengravingmachinelibrary.networkservice;

import com.srthink.iotengravingmachinelibrary.bean.ActiveBean;
import com.srthink.iotengravingmachinelibrary.bean.AlarmDetailBean;
import com.srthink.iotengravingmachinelibrary.bean.BaseArrayBean;
import com.srthink.iotengravingmachinelibrary.bean.BaseObjectBean;
import com.srthink.iotengravingmachinelibrary.bean.GetAlarmListBean;
import com.srthink.iotengravingmachinelibrary.bean.GetDataBySnBean;
import com.srthink.iotengravingmachinelibrary.bean.GetNewVersionBean;
import com.srthink.iotengravingmachinelibrary.bean.GetRecordDetailBean;
import com.srthink.iotengravingmachinelibrary.bean.GetRecordListBean;
import com.srthink.iotengravingmachinelibrary.bean.GetTokenBean;
import com.srthink.iotengravingmachinelibrary.bean.UpdateDeviceBean;
import com.srthink.iotengravingmachinelibrary.bean.UploadDeviceIpBean;
import com.srthink.iotengravingmachinelibrary.bean.UploadErrorBean;
import com.srthink.iotengravingmachinelibrary.bean.UploadRecordBean;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface APIService {

    /**
     * 获取设备token
     *
     * @param
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("sys/dev/auth")
    Observable<BaseObjectBean<GetTokenBean>> getEquipmentToken(@Body RequestBody requestBody);


    /**
     * 通过设备sn获取信息
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/sys/dev/product/query/secret")
    Observable<BaseObjectBean<GetDataBySnBean>> getEquipDataBySn(@Body RequestBody requestBody);

    /**
     * 设备激活
     *
     * @param requestBody
     * @return
     * @POST("sys/dev/product/dev/active")
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST
    Observable<BaseObjectBean<ActiveBean>> activeEquipment(@Body RequestBody requestBody, @Url() String url);

    /**
     * 故障上报
     *
     * @param requestBody
     * @return
     * @POST("sys/dev/soft/product/download")
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST
    Observable<BaseObjectBean<UploadErrorBean>> uploadErrorMsg(@Body RequestBody requestBody, @Url() String url);

    /**
     * 反馈上报
     *
     * @param requestBody
     * @return
     * @POST("sys/dev/soft/product/download")
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST
    Observable<BaseObjectBean<UploadRecordBean>> uploadRecordMsg(@Body RequestBody requestBody, @Url() String url);

    /**
     * 获取故障记录
     *
     * @param requestBody
     * @param url
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST
    Observable<BaseObjectBean<GetAlarmListBean>> getAlarmListMsg(@Body RequestBody requestBody, @Url() String url);


    /**
     * 故障详情
     *
     * @param requestBody
     * @param url
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST
    Observable<BaseObjectBean<AlarmDetailBean>> getAlarmDetailMsg(@Body RequestBody requestBody, @Url() String url);

    /**
     * 获取反馈列表
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST
    Observable<BaseObjectBean<GetRecordListBean>> getRecordList(@Body RequestBody requestBody, @Url() String url);

    /**
     * 反馈详情
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("sys/dev/soft/product/download")
    Observable<BaseObjectBean<GetRecordDetailBean>> getRecordDetail(@Body RequestBody requestBody);

    /**
     * 获取最新的版本信息
     *
     * @param requestBody
     * @param url
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST
    Observable<BaseArrayBean<GetNewVersionBean>> getNewVersionInfo(@Body RequestBody requestBody, @Url() String url);

    /**
     * 设备更新
     *
     * @param requestBody
     * @param url
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST
    Observable<BaseObjectBean<UpdateDeviceBean>> updateDevice(@Body RequestBody requestBody, @Url() String url);

    /**
     * 设备Ip地址上报
     *
     * @param requestBody
     * @param url
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST
    Observable<BaseObjectBean<UploadDeviceIpBean>> uploadDeviceIp(@Body RequestBody requestBody, @Url() String url);

    public static final String BASE_URL = "http://acc-huahui.oss-cn-shenzhen.aliyuncs.com/";
//    public static final String BASE_URL = "http://dldir1.qq.com";

    /**
     * 下载
     *
     * @param range
     * @param url
     * @return
     */
    @Streaming
    @GET
    Observable<ResponseBody> executeDownload(@Header("Range") String range, @Url() String url);

}
