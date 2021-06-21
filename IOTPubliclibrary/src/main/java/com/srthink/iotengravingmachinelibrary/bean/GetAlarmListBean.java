package com.srthink.iotengravingmachinelibrary.bean;

import java.util.List;

/**
 * @author liwanlian
 * @date 2021/5/28 14:15
 * 故障列表
 */
public class GetAlarmListBean {


    /**
     * page : {"curPageSize":15,"currentPage":1,"nextPage":1,"pageSize":15,"prePage":1,"start":1,"totalPage":1,"totalRow":3}
     * list : [{"agentId":"","alarmId":"1165983851382571009","alarmStatus":0,"alarmTime":"2021-05-28 18:01:46","alarmType":5,"createTime":null,"currentPage":1,"deleted":0,"description":"","devSn":"34466798","deviceId":"1164580960889470976","deviceModel":"T5","disposeContent":"","disposeId":"","disposeImgNames":"","disposeName":"","disposeTime":"","disposeVideoName":"","handelTime":"2021-05-28 18:01:46","id":"19","informStatus":0,"orderBy":"","pageSize":15,"productName":"收银机3","totalPage":0,"totalRow":0,"updateTime":null,"userId":""},{"agentId":"","alarmId":"1165983825327554561","alarmStatus":0,"alarmTime":"2021-05-28 18:01:43","alarmType":4,"createTime":null,"currentPage":1,"deleted":0,"description":"","devSn":"34466798","deviceId":"1164580960889470976","deviceModel":"T5","disposeContent":"","disposeId":"","disposeImgNames":"","disposeName":"","disposeTime":"","disposeVideoName":"","handelTime":"2021-05-28 18:01:43","id":"18","informStatus":0,"orderBy":"","pageSize":15,"productName":"收银机3","totalPage":0,"totalRow":0,"updateTime":null,"userId":""},{"agentId":"","alarmId":"1165983793761222657","alarmStatus":0,"alarmTime":"2021-05-28 18:01:39","alarmType":2,"createTime":null,"currentPage":1,"deleted":0,"description":"","devSn":"34466798","deviceId":"1164580960889470976","deviceModel":"T5","disposeContent":"","disposeId":"","disposeImgNames":"","disposeName":"","disposeTime":"","disposeVideoName":"","handelTime":"2021-05-28 18:01:39","id":"17","informStatus":0,"orderBy":"","pageSize":15,"productName":"收银机3","totalPage":0,"totalRow":0,"updateTime":null,"userId":""}]
     */

    private PageBean page;
    private List<ListBean> list;

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class PageBean {
        /**
         * curPageSize : 15
         * currentPage : 1
         * nextPage : 1
         * pageSize : 15
         * prePage : 1
         * start : 1
         * totalPage : 1
         * totalRow : 3
         */

        private Integer curPageSize;
        private Integer currentPage;
        private Integer nextPage;
        private Integer pageSize;
        private Integer prePage;
        private Integer start;
        private Integer totalPage;
        private Integer totalRow;

        public Integer getCurPageSize() {
            return curPageSize;
        }

        public void setCurPageSize(Integer curPageSize) {
            this.curPageSize = curPageSize;
        }

        public Integer getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(Integer currentPage) {
            this.currentPage = currentPage;
        }

        public Integer getNextPage() {
            return nextPage;
        }

        public void setNextPage(Integer nextPage) {
            this.nextPage = nextPage;
        }

        public Integer getPageSize() {
            return pageSize;
        }

        public void setPageSize(Integer pageSize) {
            this.pageSize = pageSize;
        }

        public Integer getPrePage() {
            return prePage;
        }

        public void setPrePage(Integer prePage) {
            this.prePage = prePage;
        }

        public Integer getStart() {
            return start;
        }

        public void setStart(Integer start) {
            this.start = start;
        }

        public Integer getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(Integer totalPage) {
            this.totalPage = totalPage;
        }

        public Integer getTotalRow() {
            return totalRow;
        }

        public void setTotalRow(Integer totalRow) {
            this.totalRow = totalRow;
        }
    }

    public static class ListBean {
        /**
         * agentId :
         * alarmId : 1165983851382571009
         * alarmStatus : 0
         * alarmTime : 2021-05-28 18:01:46
         * alarmType : 5
         * createTime : null
         * currentPage : 1
         * deleted : 0
         * description :
         * devSn : 34466798
         * deviceId : 1164580960889470976
         * deviceModel : T5
         * disposeContent :
         * disposeId :
         * disposeImgNames :
         * disposeName :
         * disposeTime :
         * disposeVideoName :
         * handelTime : 2021-05-28 18:01:46
         * id : 19
         * informStatus : 0
         * orderBy :
         * pageSize : 15
         * productName : 收银机3
         * totalPage : 0
         * totalRow : 0
         * updateTime : null
         * userId :
         */

        private String agentId;
        private String alarmId;
        private Integer alarmStatus;
        private String alarmTime;
        private Integer alarmType;
        private Object createTime;
        private Integer currentPage;
        private Integer deleted;
        private String description;
        private String devSn;
        private String deviceId;
        private String deviceModel;
        private String disposeContent;
        private String disposeId;
        private String disposeImgNames;
        private String disposeName;
        private String disposeTime;
        private String disposeVideoName;
        private String handelTime;
        private String id;
        private Integer informStatus;
        private String orderBy;
        private Integer pageSize;
        private String productName;
        private Integer totalPage;
        private Integer totalRow;
        private Object updateTime;
        private String userId;

        public String getAgentId() {
            return agentId;
        }

        public void setAgentId(String agentId) {
            this.agentId = agentId;
        }

        public String getAlarmId() {
            return alarmId;
        }

        public void setAlarmId(String alarmId) {
            this.alarmId = alarmId;
        }

        public Integer getAlarmStatus() {
            return alarmStatus;
        }

        public void setAlarmStatus(Integer alarmStatus) {
            this.alarmStatus = alarmStatus;
        }

        public String getAlarmTime() {
            return alarmTime;
        }

        public void setAlarmTime(String alarmTime) {
            this.alarmTime = alarmTime;
        }

        public Integer getAlarmType() {
            return alarmType;
        }

        public void setAlarmType(Integer alarmType) {
            this.alarmType = alarmType;
        }

        public Object getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Object createTime) {
            this.createTime = createTime;
        }

        public Integer getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(Integer currentPage) {
            this.currentPage = currentPage;
        }

        public Integer getDeleted() {
            return deleted;
        }

        public void setDeleted(Integer deleted) {
            this.deleted = deleted;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDevSn() {
            return devSn;
        }

        public void setDevSn(String devSn) {
            this.devSn = devSn;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getDeviceModel() {
            return deviceModel;
        }

        public void setDeviceModel(String deviceModel) {
            this.deviceModel = deviceModel;
        }

        public String getDisposeContent() {
            return disposeContent;
        }

        public void setDisposeContent(String disposeContent) {
            this.disposeContent = disposeContent;
        }

        public String getDisposeId() {
            return disposeId;
        }

        public void setDisposeId(String disposeId) {
            this.disposeId = disposeId;
        }

        public String getDisposeImgNames() {
            return disposeImgNames;
        }

        public void setDisposeImgNames(String disposeImgNames) {
            this.disposeImgNames = disposeImgNames;
        }

        public String getDisposeName() {
            return disposeName;
        }

        public void setDisposeName(String disposeName) {
            this.disposeName = disposeName;
        }

        public String getDisposeTime() {
            return disposeTime;
        }

        public void setDisposeTime(String disposeTime) {
            this.disposeTime = disposeTime;
        }

        public String getDisposeVideoName() {
            return disposeVideoName;
        }

        public void setDisposeVideoName(String disposeVideoName) {
            this.disposeVideoName = disposeVideoName;
        }

        public String getHandelTime() {
            return handelTime;
        }

        public void setHandelTime(String handelTime) {
            this.handelTime = handelTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Integer getInformStatus() {
            return informStatus;
        }

        public void setInformStatus(Integer informStatus) {
            this.informStatus = informStatus;
        }

        public String getOrderBy() {
            return orderBy;
        }

        public void setOrderBy(String orderBy) {
            this.orderBy = orderBy;
        }

        public Integer getPageSize() {
            return pageSize;
        }

        public void setPageSize(Integer pageSize) {
            this.pageSize = pageSize;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public Integer getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(Integer totalPage) {
            this.totalPage = totalPage;
        }

        public Integer getTotalRow() {
            return totalRow;
        }

        public void setTotalRow(Integer totalRow) {
            this.totalRow = totalRow;
        }

        public Object getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Object updateTime) {
            this.updateTime = updateTime;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
