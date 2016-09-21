package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class Device {

    @SerializedName("hostKey")
    private String HostKey;

    @SerializedName("hostName")
    private String HostName;

    @SerializedName("interIPAddress")
    private String InterIPAddress;

    @SerializedName("status")
    private String status;
    //是否已经加载过了
    private boolean hasLoad = false;

    public boolean isHasLoad() {
        return hasLoad;
    }

    public void setHasLoad(boolean hasLoad) {
        this.hasLoad = hasLoad;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        status = status;
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
        return "Device{" +
                "HostKey='" + HostKey + '\'' +
                ", HostName='" + HostName + '\'' +
                ", InterIPAddress='" + InterIPAddress + '\'' +
                ", Status='" + status + '\'' +
                '}';
    }
}
