package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class SBDeviceDetailPojo {
    @SerializedName("HostInfo")
    private SBDevice HostInfo;

    public SBDevice getHostInfo() {
        return HostInfo;
    }

    public void setHostInfo(SBDevice hostInfo) {
        HostInfo = hostInfo;
    }

    @Override
    public String toString() {
        return "SBDeviceDetailPojo{" +
                "HostInfo=" + HostInfo +
                '}';
    }
}
