package com.srthink.iotboxaar.models;

import java.util.ArrayList;
import java.util.List;

/**
 * 总的 内存信息
 */
public class TotalStorageInfo {
    public StorageInfo storageInfo;//内置内存
    public List<StorageInfo> storageInfos = new ArrayList<>();//外置内存
}
