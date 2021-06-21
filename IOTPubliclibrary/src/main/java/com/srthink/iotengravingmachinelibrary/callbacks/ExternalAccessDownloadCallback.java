package com.srthink.iotengravingmachinelibrary.callbacks;

import com.srthink.iotengravingmachinelibrary.models.DownloadInfo;

/**
 * @author liwanlian
 * @date 2021/6/17 14:05
 */
public interface ExternalAccessDownloadCallback {
    void onProgress(DownloadInfo downloadInfo);

    void onCompleted();

    void onError(String msg);

    void interrupt();//下载被中断

    void saveData(String url, long total);
}

