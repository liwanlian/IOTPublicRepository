package com.srthink.iotengravingmachinelibrary.contract;

import androidx.annotation.NonNull;

import com.srthink.iotengravingmachinelibrary.bean.ActiveBean;
import com.srthink.iotengravingmachinelibrary.bean.AlarmDetailBean;
import com.srthink.iotengravingmachinelibrary.bean.BaseArrayBean;
import com.srthink.iotengravingmachinelibrary.bean.BaseObjectBean;
import com.srthink.iotengravingmachinelibrary.bean.DownloadFileBean;
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
import com.srthink.iotengravingmachinelibrary.callbacks.DownloadCallBack;
import com.srthink.iotengravingmachinelibrary.callbacks.InternalCallback;

import java.io.File;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;

public interface MainContract {

    interface IIOTCrashRegisterModel {
        Observable<BaseObjectBean<GetTokenBean>> getEquipMentToken(RequestBody tokenReqBody, String token);//获取token

        Observable<BaseObjectBean<ActiveBean>> activeEquipment(RequestBody activeReqBody, String url, String token);//激活设备

        Observable<BaseObjectBean<UploadErrorBean>> uploadErrorMsg(RequestBody uploadErrReqBody, String url, String token);//故障上报

        Observable<BaseObjectBean<UploadRecordBean>> uploadRecordMsg(RequestBody uploadRecordReqBody, String url, String token);//反馈上报

        Observable<BaseObjectBean<GetRecordListBean>> getRecordList(RequestBody getRecordListReqBody, String url, String token);//获取反馈记录列表

        Observable<BaseObjectBean<GetRecordDetailBean>> getRecordDetail(RequestBody getRecordDetailReqBody, String token);//获取反馈详情

        Observable<BaseObjectBean<GetDataBySnBean>> getEquipDataBySn(@Body RequestBody requestBody, String token);//通过sn获取设备信息

        Observable<BaseObjectBean<GetAlarmListBean>> getAlarmListMsg(@Body RequestBody requestBody, String urly, String token);//获取故障记录

        Observable<BaseObjectBean<AlarmDetailBean>> getAlarmDetailMsg(@Body RequestBody requestBody, String urly, String token);//获取故障详情

        Observable<BaseArrayBean<GetNewVersionBean>> getNewVersionInfo(@Body RequestBody requestBody, String urly, String token);//获取最新的版本信息

        Observable<BaseObjectBean<UpdateDeviceBean>> updateDevice(@Body RequestBody requestBody, String urly, String token);//更新设备

        Observable<BaseObjectBean<UploadDeviceIpBean>> uploadDeviceIP(@Body RequestBody requestBody, String urly, String token);//上报设备的外网地址

        Observable<BaseObjectBean<DownloadFileBean>> downloadServerPushFile(String range, String url);//下载服务端推送过来的文件
    }


    interface IIOTCrashRegisterPresenter {
        void getEquipMentToken(@NonNull String autobody, InternalCallback<GetTokenBean> internalCallback, String token);

        void activeEquipment(@NonNull String autobody, @NonNull String productKey, @NonNull String deviceSecret, @NonNull String deviceName, String url, String token, InternalCallback<String> internalCallback);

        void uploadErrorMsg(@NonNull String autobody, @NonNull String productKey, @NonNull String deviceSecret, @NonNull String deviceName, int alarmStatus, String alarmTime, String ur, String token, InternalCallback<String> internalCallback);

        void uploadRecordMsg(@NonNull String autobody, @NonNull String productKey, @NonNull String deviceSecret, @NonNull String deviceName, String feedbackContent, String feedbackTime, String url, String token, InternalCallback<String> internalCallback);

        void getRecordList(@NonNull String autobody, @NonNull String productKey, @NonNull String deviceSecret, @NonNull String deviceName, int currentPage, int pageSize, String url, String token, InternalCallback<GetRecordListBean> internalCallback);

        void getRecordDetail(@NonNull String autobody, String token);

        void getEquipDataBySn(@NonNull String devSn, String token, InternalCallback<GetDataBySnBean> internalCallback);

        void getAlarmListMsg(@NonNull String autobody, @NonNull String productKey, @NonNull String deviceSecret, @NonNull String deviceName, int currentPage, int pageSize, String url, String token, InternalCallback<GetAlarmListBean> internalCallback);

        void getAlarmDetailMsg(@NonNull String autobody, @NonNull String productKey, @NonNull String deviceSecret, @NonNull String alarmID, String url, String token, InternalCallback<AlarmDetailBean> internalCallback);

        void getNewVersionInfo(@NonNull String productKey, @NonNull String deviceName, @NonNull String softCategory, @NonNull String softProductName, String url, String token, InternalCallback<List<GetNewVersionBean>> internalCallback);

        void updateDevice(@NonNull String productKey, @NonNull String deviceName, @NonNull String appkey, @NonNull String taskId, @NonNull String updateFirmType, @NonNull String upgradeTime, @NonNull String version, String url, String token, InternalCallback<String> internalCallback);

        void uploadDeviceIP(@NonNull String productKey, @NonNull String deviceName, @NonNull String ip, String url, String token, InternalCallback<String> internalCallback);

        void downloadServerPushFile(@NonNull long range, @NonNull String url, @NonNull String fileName, @NonNull File downloadFile, DownloadCallBack downloadCallback);
    }
}
