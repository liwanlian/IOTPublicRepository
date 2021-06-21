package com.srthink.iotpublicproject.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

import com.srthink.iotboxaar.utils.FileOperation;
import com.srthink.iotboxaar.utils.LogUtil;
import com.srthink.iotboxaar.utils.MD5Utils;
import com.srthink.iotengravingmachinelibrary.ExternalCallEntry;
import com.srthink.iotengravingmachinelibrary.callbacks.ExternalAccessDownloadCallback;
import com.srthink.iotengravingmachinelibrary.models.DownloadInfo;
import com.srthink.iotengravingmachinelibrary.utils.NetUtils;
import com.srthink.iotpublicproject.IOTPublicApplication;
import com.srthink.iotpublicproject.R;
import com.srthink.iotpublicproject.events.UpdateEvent;
import com.srthink.iotpublicproject.utils.AppContants;
import com.srthink.iotpublicproject.utils.AppSpUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;


/**
 * Created by liwanlian
 * on 2021/5/22 22:42
 * 不支持断续下载
 **/
public class DownloadIntentService extends IntentService {

    private NotificationManager mNotifyManager;
    private String mDownloadFileName;
    private Notification mNotification;

    private static final String TAG = "DownloadIntentService";

    public DownloadIntentService() {
        super("InitializeService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        final int downloadId = intent.getExtras().getInt("download_id");
        String downloadUrl = intent.getExtras().getString("download_url");
        mDownloadFileName = intent.getExtras().getString("download_file");
        String updateType = intent.getExtras().getString("updateType");
        String mid = intent.getExtras().getString("msgid");

        final File file = new File(FileOperation.getSaveFile(this, AppContants.FOLDERNAME) + "/" + mDownloadFileName);
        long range = 0;
        int progress = 0;
        if (file.exists()) {
            range = SPDownloadUtil.getInstance().get(downloadUrl, 0);
            progress = (int) (range * 100 / file.length());
            LogUtil.logInfo(TAG + "文件的路径" + file.getPath() + "文件的名字：" + file.getName() + "目录" + file.getParentFile());
            LogUtil.logInfo(TAG + "range = " + range + "progress======>" + progress + "文件长度是：-----》" + file.length());

            if (range == file.length()) {
                installAPK(file, mid, updateType);
                return;
            }
        }
        LogUtil.logInfo(TAG + "range = " + range);

        final RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notify_download);
        remoteViews.setProgressBar(R.id.pb_progress, 100, progress, false);
        remoteViews.setTextViewText(R.id.tv_progress, "已下载" + progress + "%");

        final NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setContent(remoteViews)
                        .setTicker("正在下载")
                        .setSmallIcon(R.mipmap.ic_launcher);

        mNotification = builder.build();

        mNotifyManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyManager.notify(downloadId, mNotification);
        File folder = FileOperation.getSaveFile(IOTPublicApplication.getApp(), AppContants.FOLDERNAME);
        new ExternalCallEntry(AppSpUtil.getAccessToken(IOTPublicApplication.getApp()), AppSpUtil.getEquipmentInfo(IOTPublicApplication.getApp()))
                .downloadServerPushFile(folder, range, mDownloadFileName, downloadUrl, new ExternalAccessDownloadCallback() {
                    @Override
                    public void onProgress(DownloadInfo downloadInfo) {
                        int curProgress = 0;
                        curProgress = downloadInfo.value;
                        remoteViews.setProgressBar(R.id.pb_progress, 100, curProgress, false);
                        remoteViews.setTextViewText(R.id.tv_progress, "已下载" + curProgress + "%");
                        mNotifyManager.notify(downloadId, mNotification);
                        LogUtil.logInfo(TAG + "已下载-----" + curProgress + "%");
                    }

                    @Override
                    public void onCompleted() {
                        LogUtil.logInfo(TAG + "下载完成");
                        mNotifyManager.cancel(downloadId);
                        if (updateType.equals(AppContants.UPDATE_TYPE_SOFTWARE)) {
                            installAPK(file, mid, updateType);
                        } else {
                            EventBus.getDefault().post(new UpdateEvent(true, false, updateType, false, mid));
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        mNotifyManager.cancel(downloadId);
                        LogUtil.logInfo(TAG + "下载发生错误--" + msg);
                        if (NetUtils.isNetWorkCanUse(IOTPublicApplication.getApp())) {
                            SPDownloadUtil.getInstance().clear();
                            if (file.exists())
                                file.delete();
                            EventBus.getDefault().post(new UpdateEvent(false, false, updateType, false, mid));
                            LogUtil.logInfo(TAG + "网络可用--");
                        } else {
                            LogUtil.logInfo(TAG + "网络不可用--");
                        }

                    }

                    @Override
                    public void interrupt() {
                        LogUtil.logInfo(TAG + "下载超时---中断了");
                        mNotifyManager.cancel(downloadId);
                        EventBus.getDefault().post(new UpdateEvent(false, true, updateType, false, mid));
                    }

                    @Override
                    public void saveData(String url, long total) {
                        SPDownloadUtil.getInstance().save(url, total);
                    }
                });
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

}
