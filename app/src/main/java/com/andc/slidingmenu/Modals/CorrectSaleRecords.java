package com.andc.slidingmenu.Modals;

/**
 * Created by win on 4/4/2015.
 */
public class CorrectSaleRecords {
    public int updateTime;
    public int currentMonitoringTime;
    public int activeUsage;
    public int reactiveUsage;
    public int salePrice;

    public CorrectSaleRecords(int updateTime, int currentMonitoringTime, int activeUsage, int reactiveUsage, int salePrice) {
        this.updateTime = updateTime;
        this.currentMonitoringTime = currentMonitoringTime;
        this.activeUsage = activeUsage;
        this.reactiveUsage = reactiveUsage;
        this.salePrice = salePrice;
    }

    public int getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(int updateTime) {
        this.updateTime = updateTime;
    }

    public int getCurrentMonitoringTime() {
        return currentMonitoringTime;
    }

    public void setCurrentMonitoringTime(int currentMonitoringTime) {
        this.currentMonitoringTime = currentMonitoringTime;
    }

    public int getActiveUsage() {
        return activeUsage;
    }

    public void setActiveUsage(int activeUsage) {
        this.activeUsage = activeUsage;
    }

    public int getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(int salePrice) {
        this.salePrice = salePrice;
    }

    public int getReactiveUsage() {
        return reactiveUsage;
    }

    public void setReactiveUsage(int reactiveUsage) {
        this.reactiveUsage = reactiveUsage;
    }

}
