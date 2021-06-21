package com.srthink.iotengravingmachinelibrary.callbacks;

import java.io.File;

import io.reactivex.rxjava3.disposables.Disposable;

//import io.reactivex.disposables.Disposable;


/**
 * @author liwanlian
 * @date 2021/6/18 14:42
 */
public interface DownloadNewCallback {
    void onStart(Disposable d);

    void onProgress(long totalByte, long currentByte, int progress);

    void onFinish(File file);

    void onError(String msg);
}
