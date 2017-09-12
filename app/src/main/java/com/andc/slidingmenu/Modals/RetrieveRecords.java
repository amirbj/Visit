package com.andc.slidingmenu.Modals;

/**
 * Created by win on 4/4/2015.
 */
public class RetrieveRecords {
    public int updateTime;
    public int retrievePrice;
    public int bankName;
    public int retrieveTime;
    public int period;

    public RetrieveRecords(){}

    public RetrieveRecords(int updateTime, int retrievePrice, int bankName, int retrieveTime, int period) {
        this.updateTime = updateTime;
        this.retrievePrice = retrievePrice;
        this.bankName = bankName;
        this.retrieveTime = retrieveTime;
        this.period = period;
    }

    public int getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(int updateTime) {
        this.updateTime = updateTime;
    }

    public int getRetrievePrice() {
        return retrievePrice;
    }

    public void setRetrievePrice(int retrievePrice) {
        this.retrievePrice = retrievePrice;
    }

    public int getBankName() {
        return bankName;
    }

    public void setBankName(int bankName) {
        this.bankName = bankName;
    }

    public int getRetrieveTime() {
        return retrieveTime;
    }

    public void setRetrieveTime(int retrieveTime) {
        this.retrieveTime = retrieveTime;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }
}

