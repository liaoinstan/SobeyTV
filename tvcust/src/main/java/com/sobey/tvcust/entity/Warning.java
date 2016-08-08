package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class Warning {

    @SerializedName("hostKey")
    private String HostKey;

    @SerializedName("hostName")
    private String HostName;

    @SerializedName("interIPAddress")
    private String InterIPAddress;

    @SerializedName("time")
    private long time;

    @SerializedName("reason")
    private String reason;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getHostKey() {
        return HostKey;
    }

    public void setHostKey(String hostKey) {
        HostKey = hostKey;
    }

    public String getHostName() {
        return HostName;
    }

    public void setHostName(String hostName) {
        HostName = hostName;
    }

    public String getInterIPAddress() {
        return InterIPAddress;
    }

    public void setInterIPAddress(String interIPAddress) {
        InterIPAddress = interIPAddress;
    }

    @Override
    public String toString() {
        return "Warning{" +
                "HostKey='" + HostKey + '\'' +
                ", HostName='" + HostName + '\'' +
                ", InterIPAddress='" + InterIPAddress + '\'' +
                ", time=" + time +
                ", reason='" + reason + '\'' +
                '}';
    }
}
