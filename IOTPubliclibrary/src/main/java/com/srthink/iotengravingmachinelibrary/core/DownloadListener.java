package com.srthink.iotengravingmachinelibrary.core;

import okhttp3.ResponseBody;


public interface DownloadListener {
    void onStart(ResponseBody responseBody);
}
