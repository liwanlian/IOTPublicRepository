package com.srthink.iotengravingmachinelibrary.presenters;

import android.util.Log;

//import androidx.annotation.NonNull;
import io.reactivex.rxjava3.annotations.NonNull;

import com.srthink.iotboxaar.utils.FileOperation;
import com.srthink.iotboxaar.utils.JsonUtil;
import com.srthink.iotboxaar.utils.LogUtil;
import com.srthink.iotengravingmachinelibrary.bean.AlarmDetailBean;
import com.srthink.iotengravingmachinelibrary.bean.BaseObjectBean;
import com.srthink.iotengravingmachinelibrary.bean.DownloadFileBean;
import com.srthink.iotengravingmachinelibrary.bean.GetAlarmListBean;
import com.srthink.iotengravingmachinelibrary.bean.GetDataBySnBean;
import com.srthink.iotengravingmachinelibrary.bean.GetNewVersionBean;
import com.srthink.iotengravingmachinelibrary.bean.GetRecordListBean;
import com.srthink.iotengravingmachinelibrary.bean.GetTokenBean;
import com.srthink.iotengravingmachinelibrary.bean.NetworkRequest.ActiveReq;
import com.srthink.iotengravingmachinelibrary.bean.NetworkRequest.GetAlarmDetailReq;
import com.srthink.iotengravingmachinelibrary.bean.NetworkRequest.GetAlarmListReq;
import com.srthink.iotengravingmachinelibrary.bean.NetworkRequest.GetInfoBySn;
import com.srthink.iotengravingmachinelibrary.bean.NetworkRequest.GetNewVersionReq;
import com.srthink.iotengravingmachinelibrary.bean.NetworkRequest.UpdateDeviceReq;
import com.srthink.iotengravingmachinelibrary.bean.NetworkRequest.UploadDeviceIpReq;
import com.srthink.iotengravingmachinelibrary.bean.NetworkRequest.UploadErrReq;
import com.srthink.iotengravingmachinelibrary.bean.NetworkRequest.UploadRecordReq;
import com.srthink.iotengravingmachinelibrary.callbacks.DownloadCallBack;
import com.srthink.iotengravingmachinelibrary.callbacks.InternalCallback;
import com.srthink.iotengravingmachinelibrary.contract.MainContract;
import com.srthink.iotengravingmachinelibrary.models.IOTEngravingMachineModel;
import com.srthink.iotengravingmachinelibrary.networkservice.RetrofitDownload;
import com.srthink.iotengravingmachinelibrary.utils.HandleExceptionUtil;
import com.srthink.iotengravingmachinelibrary.utils.RetryWithDelay;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.List;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * @author liwanlian
 * @date 2021/5/13 18:22
 * 不用基于view的
 */
public class IOTEngravingMachineNewPresenter implements MainContract.IIOTCrashRegisterPresenter {
    private static final String TAG = "IOTEngravingMachineNewPresenter";

    @Override
    public void getEquipMentToken(@NonNull String autobody, InternalCallback<GetTokenBean> internalCallback, String token) {
        new IOTEngravingMachineModel().getEquipMentToken(RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                autobody), token)
                .retryWhen(new RetryWithDelay())
                .subscribe(result -> {
                            if (result == null) {
                                internalCallback.fail("");
                            } else {
                                if (result.getCode().equals("0000")) {
                                    String nToken = result.getData().token;
                                    LogUtil.logInfo(TAG + "获取成功：" + nToken);
                                    internalCallback.success(result.getData());
                                } else {
                                    internalCallback.fail(result.getMsg());
                                }
                            }
                        },
                        error -> {
                            LogUtil.logInfo(TAG + "异常为" + error.toString());
                            HandleExceptionUtil.handleException(error);
                            internalCallback.fail("");
                        });
    }

    @Override
    public void getEquipDataBySn(@NonNull String devSn, String token, InternalCallback<GetDataBySnBean> internalCallback) {
        new IOTEngravingMachineModel().getEquipDataBySn(RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                JsonUtil.getJsonString(new GetInfoBySn(devSn))), token)
                .retryWhen(new RetryWithDelay())
                .subscribe(result -> {
                            if (result == null) {
                                internalCallback.fail("");
                            } else {
                                if (result.getCode().equals("200")) {
                                    internalCallback.success(result.getData());
                                } else {
                                    internalCallback.fail(result.getMsg());
                                }
                            }
                        },
                        error -> {
                            LogUtil.logInfo(TAG + "异常为" + error.toString());
                            HandleExceptionUtil.handleException(error);
                            internalCallback.fail("");
                        });
    }

    @Override
    public void getAlarmListMsg(@NonNull String autobody, @NonNull String productKey, @NonNull String deviceSecret, @NonNull String deviceName, int currentPage, int pageSize, String url, String token, InternalCallback<GetAlarmListBean> internalCallback) {
        new IOTEngravingMachineModel().getAlarmListMsg(RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                JsonUtil.getJsonString(new GetAlarmListReq(token, autobody, deviceName, productKey, currentPage, pageSize))), url, token)
                .retryWhen(new RetryWithDelay())
                .subscribe(result -> {
                            if (result == null) {
                                internalCallback.fail("");
                            } else {
                                if (result.getCode().equals("200")) {
                                    internalCallback.success(result.getData());
                                } else {
                                    internalCallback.fail(result.getMsg());
                                }
                            }
                        },
                        error -> {
                            LogUtil.logInfo(TAG + "异常为" + error.toString());
                            HandleExceptionUtil.handleException(error);
                            internalCallback.fail("");
                        });
    }

    @Override
    public void getAlarmDetailMsg(@NonNull String autobody, @NonNull String productKey, @NonNull String deviceName, @NonNull String alarmID, String url, String token, InternalCallback<AlarmDetailBean> internalCallback) {
        new IOTEngravingMachineModel().getAlarmDetailMsg(RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                JsonUtil.getJsonString(new GetAlarmDetailReq(autobody, deviceName, productKey, alarmID))), url, token)
                .retryWhen(new RetryWithDelay())
                .subscribe(result -> {
                            if (result == null) {
                                internalCallback.fail("");
                            } else {
                                if (result.getCode().equals("200")) {
                                    internalCallback.success(result.getData());
                                } else {
                                    internalCallback.fail(result.getMsg());
                                }
                            }
                        },
                        error -> {
                            LogUtil.logInfo(TAG + "异常为" + error.toString());
                            HandleExceptionUtil.handleException(error);
                            internalCallback.fail("");
                        });
    }

    @Override
    public void getNewVersionInfo(@NonNull String productKey, @NonNull String deviceName, @NonNull String softCategory, @NonNull String softProductName, String url, String token, InternalCallback<List<GetNewVersionBean>> internalCallback) {
        new IOTEngravingMachineModel().getNewVersionInfo(RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                JsonUtil.getJsonString(new GetNewVersionReq(deviceName, productKey, softCategory, softProductName))), url, token)
                .retryWhen(new RetryWithDelay())
                .subscribe(result -> {
                            if (result == null) {
                                internalCallback.fail("");
                            } else {
                                if (result.getCode().equals("200")) {
                                    internalCallback.success(result.getData());
                                } else {
                                    internalCallback.fail(result.getMsg());
                                }
                            }
                        },
                        error -> {
                            LogUtil.logInfo(TAG + "异常为" + error.toString());
                            HandleExceptionUtil.handleException(error);
                            internalCallback.fail("");
                        });
    }

    @Override
    public void updateDevice(@NonNull String productKey, @NonNull String deviceName, @NonNull String appkey, @NonNull String taskId, @NonNull String updateFirmType, @NonNull String upgradeTime, @NonNull String version, String url, String token, InternalCallback<String> internalCallback) {
        new IOTEngravingMachineModel().updateDevice(RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                JsonUtil.getJsonString(new UpdateDeviceReq(appkey, deviceName, productKey, taskId, updateFirmType, upgradeTime, version))), url, token)
                .retryWhen(new RetryWithDelay())
                .subscribe(result -> {
                            if (result == null) {
                                internalCallback.fail("");
                            } else {
                                if (result.getCode().equals("200")) {
                                    internalCallback.success(result.getMsg());
                                } else {
                                    internalCallback.fail(result.getMsg());
                                }
                            }
                        },
                        error -> {
                            LogUtil.logInfo(TAG + "异常为" + error.toString());
                            HandleExceptionUtil.handleException(error);
                            internalCallback.fail("");
                        });
    }

    @Override
    public void uploadDeviceIP(@NonNull String productKey, @NonNull String deviceName, @NonNull String ip, String url, String token, InternalCallback<String> internalCallback) {
        new IOTEngravingMachineModel().uploadDeviceIP(RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                JsonUtil.getJsonString(new UploadDeviceIpReq(deviceName, ip, productKey))), url, token)
                .retryWhen(new RetryWithDelay())
                .subscribe(result -> {
                            if (result == null) {
                                internalCallback.fail("");
                            } else {
                                if (result.getCode().equals("200")) {
                                    internalCallback.success(result.getMsg());
                                } else {
                                    internalCallback.fail(result.getMsg());
                                }
                            }
                        },
                        error -> {
                            LogUtil.logInfo(TAG + "异常为" + error.toString());
                            HandleExceptionUtil.handleException(error);
                            internalCallback.fail("");
                        });
    }

    @Override
    public void downloadServerPushFile(@NonNull long range, @NonNull String url, @NonNull String fileName, @NonNull File downloadFile, DownloadCallBack downloadCallback) {
        File file = new File(downloadFile, fileName);
        String totalLength = "-";
        if (file.exists()) {
            totalLength += file.length();
            LogUtil.logInfo(TAG + "已有下载记录" + "当前的已下载的长度是：" + range);
        } else {
            LogUtil.logInfo(TAG + "还没下载的记录");
        }

        LogUtil.logInfo(TAG + "当前文件的长度：" + totalLength);
        LogUtil.logInfo(TAG + "url是：------->>" + url);
        LogUtil.logInfo(TAG + "还需下载的长度是：" + (Long.toString(range) + totalLength));
        RetrofitDownload.getInstance().getApiService().executeDownload("bytes=" + Long.toString(range) + totalLength, url)
                .retryWhen(new RetryWithDelay())
                .subscribe(new Observer<ResponseBody>() {

                    @Override
                    public void onSubscribe(io.reactivex.rxjava3.disposables.@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        RandomAccessFile randomAccessFile = null;
                        InputStream inputStream = null;
                        long total = range;
                        long responseLength = 0;
                        try {
                            byte[] buf = new byte[2048];
                            int len = 0;
                            responseLength = responseBody.contentLength();
                            inputStream = responseBody.byteStream();
                            String filePath = downloadFile.getPath() + "/";
                            File file = new File(filePath, fileName);
                            File dir = new File(filePath);
                            if (!dir.exists()) {
                                dir.mkdirs();
                            }
                            randomAccessFile = new RandomAccessFile(file, "rwd");
                            if (range == 0) {
                                randomAccessFile.setLength(responseLength);
                            }
                            randomAccessFile.seek(range);

                            LogUtil.logInfo(TAG + "range的长度是------>：" + range);
                            int progress = 0;
                            int lastProgress = 0;

                            while ((len = inputStream.read(buf)) != -1) {
                                randomAccessFile.write(buf, 0, len);

                                total += len;
                                lastProgress = progress;
                                progress = (int) (total * 100 / randomAccessFile.length());
                                if (progress > 0 && progress != lastProgress) {
                                    downloadCallback.onProgress(progress);
                                    downloadCallback.saveData(url, total);
//                                    SPDownloadUtil.getInstance().save(url, total);//新加的

                                }
                            }
                            downloadCallback.onCompleted();
                        } catch (Exception e) {
                            Log.d(TAG, e.getMessage());
                            if (e.getMessage().equals("time out")) {
                                downloadCallback.interrupt();
                            } else {
                                downloadCallback.onError(e.getMessage());
                            }
                            e.printStackTrace();
                        } finally {
                            try {
//                                SPDownloadUtil.getInstance().save(url, total);
                                downloadCallback.saveData(url, total);
                                if (randomAccessFile != null) {
                                    randomAccessFile.close();
                                }

                                if (inputStream != null) {
                                    inputStream.close();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        downloadCallback.onError(e.toString());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void activeEquipment(@NonNull String autobody, @NonNull String productKey, @NonNull String deviceSecret, @NonNull String deviceName, String url, String token, InternalCallback<String> internalCallback) {
        new IOTEngravingMachineModel().activeEquipment(RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                JsonUtil.getJsonString(new ActiveReq(autobody, deviceName, productKey))), url, token)
                .retryWhen(new RetryWithDelay())
                .subscribe(result -> {
                            if (result == null) {
                                internalCallback.fail("");
                            } else {
                                if (result.getCode().equals("200")) {
                                    internalCallback.success(result.getMsg());
                                } else {
                                    internalCallback.fail(result.getMsg());
                                }
                            }
                        },
                        error -> {
                            LogUtil.logInfo(TAG + "异常为" + error.toString());
                            HandleExceptionUtil.handleException(error);
                            internalCallback.fail("");
                        });
    }

    @Override
    public void uploadRecordMsg(@NonNull String autobody, @NonNull String productKey, @NonNull String deviceSecret, @NonNull String deviceName, String feedbackContent, String feedbackTime, String url, String token, InternalCallback<String> internalCallback) {
        new IOTEngravingMachineModel().uploadRecordMsg(RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                JsonUtil.getJsonString(new UploadRecordReq(token, autobody, deviceName, productKey, feedbackContent, feedbackTime))), url, token)
                .retryWhen(new RetryWithDelay())
                .subscribe(result -> {
                            if (result == null) {
                                internalCallback.fail("");
                            } else {
                                if (result.getCode().equals("200")) {
                                    internalCallback.success(result.getMsg());
                                } else {
                                    internalCallback.fail(result.getMsg());
                                }
                            }
                        },
                        error -> {
                            LogUtil.logInfo(TAG + "异常为" + error.toString());
                            HandleExceptionUtil.handleException(error);
                            internalCallback.fail("");
                        });
    }

    @Override
    public void uploadErrorMsg(@NonNull String autobody, @NonNull String productKey, @NonNull String deviceSecret, @NonNull String deviceName, int alarmStatus, String alarmTime, String url, String token, InternalCallback<String> internalCallback) {
        new IOTEngravingMachineModel().uploadErrorMsg(RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                JsonUtil.getJsonString(new UploadErrReq(token, autobody, deviceName, productKey, alarmStatus, alarmTime))), url, token)
                .retryWhen(new RetryWithDelay())
                .subscribe(result -> {
                            if (result == null) {
                                internalCallback.fail("");
                            } else {
                                if (result.getCode().equals("200")) {
                                    internalCallback.success(result.getMsg());
                                } else {
                                    internalCallback.fail(result.getMsg());
                                }
                            }
                        },
                        error -> {
                            LogUtil.logInfo(TAG + "异常为" + error.toString());
                            HandleExceptionUtil.handleException(error);
                            internalCallback.fail("");
                        });
    }


    @Override
    public void getRecordList(@NonNull String autobody, @NonNull String productKey, @NonNull String deviceSecret, @NonNull String deviceName, int currentPage, int pageSize, String url, String token, InternalCallback<GetRecordListBean> internalCallback) {
        new IOTEngravingMachineModel().getRecordList(RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                JsonUtil.getJsonString(new GetAlarmListReq(token, autobody, deviceName, productKey, currentPage, pageSize))), url, token)
                .retryWhen(new RetryWithDelay())
                .subscribe(result -> {
                            if (result == null) {
                                internalCallback.fail("");
                            } else {
                                if (result.getCode().equals("200")) {
                                    internalCallback.success(result.getData());
                                } else {
                                    internalCallback.fail(result.getMsg());
                                }
                            }
                        },
                        error -> {
                            LogUtil.logInfo(TAG + "异常为" + error.toString());
                            HandleExceptionUtil.handleException(error);
                            internalCallback.fail("");
                        });
    }

    @Override
    public void getRecordDetail(@NonNull String autobody, String token) {

    }

}
