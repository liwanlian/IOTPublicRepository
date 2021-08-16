package com.srthink.iotpublicproject.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.srthink.iotboxaar.utils.FileOperation;
import com.srthink.iotboxaar.utils.LogUtil;
import com.srthink.iotboxaar.utils.MD5Utils;
import com.srthink.iotengravingmachinelibrary.callbacks.DownloadNewCallback;
import com.srthink.iotengravingmachinelibrary.core.RxNet;
import com.srthink.iotpublicproject.IOTPublicApplication;
import com.srthink.iotpublicproject.events.UpdateEvent;
import com.srthink.iotpublicproject.utils.AppContants;
import com.srthink.iotpublicproject.utils.AppSpUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.math.BigDecimal;

import io.reactivex.rxjava3.disposables.Disposable;


/**
 * Created by liwanlian
 * on 2021/6/18 15:42
 * 支持断续下载
 **/
public class DownloadIntentNewService extends IntentService {

    private String mDownloadFileName;
    private Disposable mDownloadTask;

    private static final String TAG = "DownloadIntentNewService";

    public DownloadIntentNewService() {
        super("InitializeService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        final int downloadId = intent.getExtras().getInt("download_id");
        String downloadUrl = intent.getExtras().getString("download_url");
        mDownloadFileName = intent.getExtras().getString("download_file");
        String updateType = intent.getExtras().getString("updateType");
        String mid = intent.getExtras().getString("msgid");


        String path = FileOperation.getDownloadPath(IOTPublicApplication.getApp(), mDownloadFileName);
        RxNet.download(downloadUrl, path, mDownloadFileName, new DownloadNewCallback() {
            @Override
            public void onStart(Disposable d) {
                mDownloadTask = d;
                LogUtil.logInfo("onStart " + d);
            }

            @Override
            public void onProgress(long totalByte, long currentByte, float progress) {
                LogUtil.logInfo("onProgress---> " + progress);
            }

            @Override
            public void onFinish(File file) {
                LogUtil.logInfo("onFinish " + file.getAbsolutePath());

                if (updateType.equals(AppContants.UPDATE_TYPE_SOFTWARE)) {
                    installAPK(file, mid, updateType);
                } else {
                    testFirmware(file, mid, updateType);
                }
            }

            @Override
            public void onError(String msg) {
                LogUtil.logInfo("onError " + msg);
            }
        });

        //取消下载
//        RetrofitFactory.cancel(mDownloadTask);
    }

    /**
     * 检测下载回来的固件
     *
     * @param firmware
     * @param mid
     * @param updateType
     */
    private void testFirmware(File firmware, String mid, String updateType) {
        String md5 = MD5Utils.getMd5ByFile(firmware);
        LogUtil.logInfo(TAG + "下载回来文件的md5是：------》" + md5);
        String serverMd5 = AppSpUtil.getCacheUpdateInfo(IOTPublicApplication.getApp()).updateItemInfos.get(0).updateInfo.getBody().getSignCode();
        LogUtil.logInfo(TAG + "服务端传送回来的md5：------》" + serverMd5);
        if (!md5.equalsIgnoreCase(serverMd5)) {
            LogUtil.logError(TAG + "文件md5不对");
            EventBus.getDefault().post(new UpdateEvent(false, false, updateType, false, mid));
        } else {
            EventBus.getDefault().post(new UpdateEvent(true, false, updateType, false, mid));
        }
    }

    //适用大部分机型的应用安装
    private void installAPK(File apkFile, String mid, String updateType) {
        if (!apkFile.exists()) {
            LogUtil.logError(TAG + "安装包文件不存在");
            return;
        }
        String md5 = MD5Utils.getMd5ByFile(apkFile);
        LogUtil.logInfo(TAG + "下载回来文件的md5是：------》" + md5);
        String serverMd5 = AppSpUtil.getCacheUpdateInfo(IOTPublicApplication.getApp()).updateItemInfos.get(0).updateInfo.getBody().getSignCode();
        LogUtil.logInfo(TAG + "服务端传送回来的md5：------》" + serverMd5);
        if (!md5.equalsIgnoreCase(serverMd5)) {
            LogUtil.logError(TAG + "文件md5不对");
            EventBus.getDefault().post(new UpdateEvent(false, false, updateType, false, mid));
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //安装完成后，启动app
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = FileProvider.getUriForFile(this, this.getPackageName() + ".fileprovider", apkFile);//第二个参数要和Mainfest中<provider>内的android:authorities 保持一致
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        startActivity(intent);
    }

    private String byteFormat(long bytes) {
        BigDecimal fileSize = new BigDecimal(bytes);
        BigDecimal megabyte = new BigDecimal(1024 * 1024);
        float returnValue = fileSize.divide(megabyte, 2, BigDecimal.ROUND_UP).floatValue();
        if (returnValue > 1) {
            return (returnValue + "MB");
        }
        BigDecimal kilobyte = new BigDecimal(1024);
        returnValue = fileSize.divide(kilobyte, 2, BigDecimal.ROUND_UP).floatValue();
        return (returnValue + "KB");
    }

}
