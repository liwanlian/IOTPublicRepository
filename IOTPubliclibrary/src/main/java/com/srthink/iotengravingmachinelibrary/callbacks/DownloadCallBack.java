package com.srthink.iotengravingmachinelibrary.callbacks;

/**
 * Created by liwanlian
 * on 2021/5/22 22:46
 **/
public interface DownloadCallBack {

    void onProgress(int progress);

    void onCompleted();

    void onError(String msg);

    void interrupt();//下载被中断

    void saveData(String url, long total);

}
