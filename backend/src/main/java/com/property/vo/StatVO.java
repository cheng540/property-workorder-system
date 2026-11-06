package com.property.vo;

/**
 * 首页统计
 */
public class StatVO {

    private long pendingCount;
    private long handlingCount;
    private long finishedCount;
    private long todayCount;
    private long residentCount;
    private long workerCount;

    public long getPendingCount() {
        return pendingCount;
    }

    public void setPendingCount(long pendingCount) {
        this.pendingCount = pendingCount;
    }

    public long getHandlingCount() {
        return handlingCount;
    }

    public void setHandlingCount(long handlingCount) {
        this.handlingCount = handlingCount;
    }

    public long getFinishedCount() {
        return finishedCount;
    }

    public void setFinishedCount(long finishedCount) {
        this.finishedCount = finishedCount;
    }

    public long getTodayCount() {
        return todayCount;
    }

    public void setTodayCount(long todayCount) {
        this.todayCount = todayCount;
    }

    public long getResidentCount() {
        return residentCount;
    }

    public void setResidentCount(long residentCount) {
        this.residentCount = residentCount;
    }

    public long getWorkerCount() {
        return workerCount;
    }

    public void setWorkerCount(long workerCount) {
        this.workerCount = workerCount;
    }
}
