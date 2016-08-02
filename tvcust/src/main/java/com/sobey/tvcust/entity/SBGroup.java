package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class SBGroup {

    @SerializedName("StationCode")
    private String StationCode;

    @SerializedName("Code")
    private String Code;

    @SerializedName("Name")
    private String Name;

    @SerializedName("Prior")
    private int Prior;

    public String getStationCode() {
        return StationCode;
    }

    public void setStationCode(String stationCode) {
        StationCode = stationCode;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getPrior() {
        return Prior;
    }

    public void setPrior(int prior) {
        Prior = prior;
    }

    @Override
    public String toString() {
        return "SBGroup{" +
                "StationCode=" + StationCode +
                ", Code=" + Code +
                ", Name=" + Name +
                ", Prior=" + Prior +
                '}';
    }
}
