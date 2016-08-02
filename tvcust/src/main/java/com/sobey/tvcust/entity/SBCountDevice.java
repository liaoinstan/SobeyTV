package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class SBCountDevice {

    @SerializedName("id")
    private int id;

    @SerializedName("Total")
    private int Total;

    @SerializedName("Online")
    private int Online;

    @SerializedName("Offline")
    private int Offline;

    @SerializedName("TotalOnlineTime")
    private int TotalOnlineTime;

    private int days;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotal() {
        return Total;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public void setTotal(int total) {
        Total = total;
    }

    public int getOnline() {
        return Online;
    }

    public void setOnline(int online) {
        Online = online;
    }

    public int getOffline() {
        return Offline;
    }

    public void setOffline(int offline) {
        Offline = offline;
    }

    public int getTotalOnlineTime() {
        return TotalOnlineTime;
    }

    public void setTotalOnlineTime(int totalOnlineTime) {
        TotalOnlineTime = totalOnlineTime;
    }

    @Override
    public String toString() {
        return "SBCountDevice{" +
                "id=" + id +
                ", Total=" + Total +
                ", Online=" + Online +
                ", Offline=" + Offline +
                ", TotalOnlineTime=" + TotalOnlineTime +
                '}';
    }
}
