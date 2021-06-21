package com.srthink.iotengravingmachinelibrary.bean;

import java.util.List;

/**
 * Created by liwanlian
 * on 2021/5/16 0:05
 * 获取反馈记录列表
 **/
public class GetRecordListBean {

    /**
     * page : {"curPageSize":15,"currentPage":1,"nextPage":1,"pageSize":15,"prePage":1,"start":1,"totalPage":1,"totalRow":2}
     * list : [{"agentId":0,"createTime":null,"currentPage":1,"deleted":0,"description":"","deviceId":"1164580960889470976","feedbackContent":"副反应翻云覆雨抚养费国有股风高放火共和国会更好","feedbackId":"1165995452735160321","feedbackStatus":0,"feedbackTime":"2021-05-28 18:24:49","handelTime":"2021-05-28 18:24:49","id":"9","orderBy":"","pageSize":15,"replyContent":"","replyId":"","replyName":"","replyTime":"","totalPage":0,"totalRow":0,"updateTime":null},{"agentId":0,"createTime":null,"currentPage":1,"deleted":0,"description":"","deviceId":"1164580960889470976","feedbackContent":"HF韩国黄花岗营业费用抚养费官方官方更富有","feedbackId":"1165995361332887552","feedbackStatus":0,"feedbackTime":"2021-05-28 18:24:38","handelTime":"2021-05-28 18:24:38","id":"8","orderBy":"","pageSize":15,"replyContent":"","replyId":"","replyName":"","replyTime":"","totalPage":0,"totalRow":0,"updateTime":null}]
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
         * totalRow : 2
         */

        private int curPageSize;
        private int currentPage;
        private int nextPage;
        private int pageSize;
        private int prePage;
        private int start;
        private int totalPage;
        private int totalRow;

        public int getCurPageSize() {
            return curPageSize;
        }

        public void setCurPageSize(int curPageSize) {
            this.curPageSize = curPageSize;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getNextPage() {
            return nextPage;
        }

        public void setNextPage(int nextPage) {
            this.nextPage = nextPage;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getPrePage() {
            return prePage;
        }

        public void setPrePage(int prePage) {
            this.prePage = prePage;
        }

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public int getTotalRow() {
            return totalRow;
        }

        public void setTotalRow(int totalRow) {
            this.totalRow = totalRow;
        }
    }

    public static class ListBean {
        /**
         * agentId : 0
         * createTime : null
         * currentPage : 1
         * deleted : 0
         * description : 
         * deviceId : 1164580960889470976
         * feedbackContent : 副反应翻云覆雨抚养费国有股风高放火共和国会更好
         * feedbackId : 1165995452735160321
         * feedbackStatus : 0
         * feedbackTime : 2021-05-28 18:24:49
         * handelTime : 2021-05-28 18:24:49
         * id : 9
         * orderBy : 
         * pageSize : 15
         * replyContent : 
         * replyId : 
         * replyName : 
         * replyTime : 
         * totalPage : 0
         * totalRow : 0
         * updateTime : null
         */

        private int agentId;
        private Object createTime;
        private int currentPage;
        private int deleted;
        private String description;
        private String deviceId;
        private String feedbackContent;
        private String feedbackId;
        private int feedbackStatus;
        private String feedbackTime;
        private String handelTime;
        private String id;
        private String orderBy;
        private int pageSize;
        private String replyContent;
        private String replyId;
        private String replyName;
        private String replyTime;
        private int totalPage;
        private int totalRow;
        private Object updateTime;

        public int getAgentId() {
            return agentId;
        }

        public void setAgentId(int agentId) {
            this.agentId = agentId;
        }

        public Object getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Object createTime) {
            this.createTime = createTime;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getDeleted() {
            return deleted;
        }

        public void setDeleted(int deleted) {
            this.deleted = deleted;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getFeedbackContent() {
            return feedbackContent;
        }

        public void setFeedbackContent(String feedbackContent) {
            this.feedbackContent = feedbackContent;
        }

        public String getFeedbackId() {
            return feedbackId;
        }

        public void setFeedbackId(String feedbackId) {
            this.feedbackId = feedbackId;
        }

        public int getFeedbackStatus() {
            return feedbackStatus;
        }

        public void setFeedbackStatus(int feedbackStatus) {
            this.feedbackStatus = feedbackStatus;
        }

        public String getFeedbackTime() {
            return feedbackTime;
        }

        public void setFeedbackTime(String feedbackTime) {
            this.feedbackTime = feedbackTime;
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

        public String getOrderBy() {
            return orderBy;
        }

        public void setOrderBy(String orderBy) {
            this.orderBy = orderBy;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public String getReplyContent() {
            return replyContent;
        }

        public void setReplyContent(String replyContent) {
            this.replyContent = replyContent;
        }

        public String getReplyId() {
            return replyId;
        }

        public void setReplyId(String replyId) {
            this.replyId = replyId;
        }

        public String getReplyName() {
            return replyName;
        }

        public void setReplyName(String replyName) {
            this.replyName = replyName;
        }

        public String getReplyTime() {
            return replyTime;
        }

        public void setReplyTime(String replyTime) {
            this.replyTime = replyTime;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public int getTotalRow() {
            return totalRow;
        }

        public void setTotalRow(int totalRow) {
            this.totalRow = totalRow;
        }

        public Object getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Object updateTime) {
            this.updateTime = updateTime;
        }
    }
}
