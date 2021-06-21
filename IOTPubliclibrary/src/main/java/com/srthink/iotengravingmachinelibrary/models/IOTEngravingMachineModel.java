package com.srthink.iotengravingmachinelibrary.models;

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
import com.srthink.iotengravingmachinelibrary.contract.MainContract;
import com.srthink.iotengravingmachinelibrary.networkservice.RetrofitClient;
import com.srthink.iotengravingmachinelibrary.networkservice.RetrofitDownload;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.RequestBody;


/**
 * @author liwanlian
 * @date 2021/5/13 18:22
 */
public class IOTEngravingMachineModel implements MainContract.IIOTCrashRegisterModel {
    @Override
    public Observable<BaseObjectBean<GetAlarmListBean>> getAlarmListMsg(RequestBody requestBod, String urly, String token) {
        return RetrofitClient.getInstance(token).getApi().getAlarmListMsg(requestBod, urly);
    }

    @Override
    public Observable<BaseObjectBean<GetTokenBean>> getEquipMentToken(RequestBody tokenReqBody, String token) {
        return RetrofitClient.getInstance(token).getApi().getEquipmentToken(tokenReqBody);
    }

    @Override
    public Observable<BaseObjectBean<ActiveBean>> activeEquipment(RequestBody activeReqBody, String url, String token) {
        return RetrofitClient.getInstance(token).getApi().activeEquipment(activeReqBody, url);
    }

    @Override
    public Observable<BaseObjectBean<UploadErrorBean>> uploadErrorMsg(RequestBody uploadErrReqBody, String url, String token) {
        return RetrofitClient.getInstance(token).getApi().uploadErrorMsg(uploadErrReqBody, url);
    }

    @Override
    public Observable<BaseObjectBean<AlarmDetailBean>> getAlarmDetailMsg(RequestBody requestBody, String urly, String token) {
        return RetrofitClient.getInstance(token).getApi().getAlarmDetailMsg(requestBody, urly);
    }

    @Override
    public Observable<BaseArrayBean<GetNewVersionBean>> getNewVersionInfo(RequestBody requestBody, String urly, String token) {
        return RetrofitClient.getInstance(token).getApi().getNewVersionInfo(requestBody, urly);
    }

    @Override
    public Observable<BaseObjectBean<UpdateDeviceBean>> updateDevice(RequestBody requestBody, String urly, String token) {
        return RetrofitClient.getInstance(token).getApi().updateDevice(requestBody, urly);
    }

    @Override
    public Observable<BaseObjectBean<UploadDeviceIpBean>> uploadDeviceIP(RequestBody requestBody, String urly, String token) {
        return RetrofitClient.getInstance(token).getApi().uploadDeviceIp(requestBody, urly);
    }

    @Override
    public Observable<BaseObjectBean<DownloadFileBean>> downloadServerPushFile(String range, String url) {
//        return RetrofitDownload.getInstance().getApiService().executeDownload(range, url);
        return null;
    }

    @Override
    public Observable<BaseObjectBean<UploadRecordBean>> uploadRecordMsg(RequestBody uploadRecordReqBody, String url, String token) {
        return RetrofitClient.getInstance(token).getApi().uploadRecordMsg(uploadRecordReqBody, url);
    }

    @Override
    public Observable<BaseObjectBean<GetRecordListBean>> getRecordList(RequestBody getRecordListReqBody, String url, String token) {
        return RetrofitClient.getInstance(token).getApi().getRecordList(getRecordListReqBody, url);
    }

    @Override
    public Observable<BaseObjectBean<GetRecordDetailBean>> getRecordDetail(RequestBody getRecordDetailReqBody, String token) {
        return RetrofitClient.getInstance(token).getApi().getRecordDetail(getRecordDetailReqBody);
    }

    @Override
    public Observable<BaseObjectBean<GetDataBySnBean>> getEquipDataBySn(RequestBody requestBody, String token) {
        return RetrofitClient.getInstance(token).getApi().getEquipDataBySn(requestBody);
    }
}
