package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class SBDevicePojo {
    @SerializedName("HostList")
    private List<SBDevice> HostList;

    public List<SBDevice> getHostList() {
        return HostList;
    }

    public void setHostList(List<SBDevice> hostList) {
        HostList = hostList;
    }

    @Override
    public String toString() {
        return "SBDevicePojo{" +
                "HostList=" + HostList +
                '}';
    }
}
