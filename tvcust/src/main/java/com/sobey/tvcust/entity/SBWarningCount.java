package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class SBWarningCount {

    @SerializedName("StationCode")
    private String StationCode;

    @SerializedName("GroupCode")
    private String GroupCode;

    @SerializedName("GroupName")
    private String GroupName;

    @SerializedName("Count")
    private int Count;

    public String getStationCode() {
        return StationCode;
    }

    public void setStationCode(String stationCode) {
        StationCode = stationCode;
    }

    public String getGroupCode() {
        return GroupCode;
    }

    public void setGroupCode(String groupCode) {
        GroupCode = groupCode;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    @Override
    public String toString() {
        return "SBWarningCount{" +
                "StationCode='" + StationCode + '\'' +
                ", GroupCode='" + GroupCode + '\'' +
                ", GroupName='" + GroupName + '\'' +
                ", Count='" + Count + '\'' +
                '}';
    }
}
