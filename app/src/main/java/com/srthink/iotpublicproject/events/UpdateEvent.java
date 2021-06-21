package com.srthink.iotpublicproject.events;

import com.srthink.iotpublicproject.models.UpdateInfo;

/**
 * @author liwanlian
 * @date 2021/6/1 11:23
 */
public class UpdateEvent extends PublicEvent {
    public int type;
    public UpdateInfo updateInfo;
    public String msg;
    public String updateType;
    public boolean isNeedUpdate;
    public String mid;
    public String url;


    public UpdateEvent(boolean isSuccess, boolean isTimeout, UpdateInfo updateInfo, boolean isNeedUpdate, String url) {
        super(isSuccess, isTimeout);
        this.updateInfo = updateInfo;
        this.isNeedUpdate = isNeedUpdate;
        this.url = url;
    }

    public UpdateEvent(boolean isSuccess, boolean isTimeout, String updateType, boolean isNeedUpdate, String mid) {
        super(isSuccess, isTimeout);
        this.updateType = updateType;
        this.isNeedUpdate = isNeedUpdate;
        this.mid = mid;
    }

    public UpdateEvent(boolean isSuccess, boolean isTimeout, boolean isNeedUpdate, String mid) {
        super(isSuccess, isTimeout);
        this.isNeedUpdate = isNeedUpdate;
        this.mid = mid;
    }

    public UpdateEvent(boolean isSuccess, boolean isTimeout, boolean isNeedUpdate, String url, String test) {
        super(isSuccess, isTimeout);
        this.isNeedUpdate = isNeedUpdate;
        this.url = url;
    }
}
