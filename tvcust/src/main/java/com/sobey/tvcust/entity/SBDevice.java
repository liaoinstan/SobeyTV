package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class SBDevice {

    @SerializedName("HostKey")
    private String HostKey;

    @SerializedName("HostName")
    private String HostName;

    @SerializedName("InterIPAddress")
    private String InterIPAddress;

    @SerializedName("Attributes")
    private List<SBKeyValue> Attributes;

    @SerializedName("CoreProcess")
    private List<SBKeyValue> CoreProcess;

    @SerializedName("Status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<SBKeyValue> getAttributes() {
        return Attributes;
    }

    public void setAttributes(List<SBKeyValue> attributes) {
        Attributes = attributes;
    }

    public List<SBKeyValue> getCoreProcess() {
        return CoreProcess;
    }

    public void setCoreProcess(List<SBKeyValue> coreProcess) {
        CoreProcess = coreProcess;
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
        return "SBDevice{" +
                "HostKey='" + HostKey + '\'' +
                ", HostName='" + HostName + '\'' +
                ", InterIPAddress='" + InterIPAddress + '\'' +
                ", Attributes=" + Attributes +
                ", CoreProcess=" + CoreProcess +
                '}';
    }
}
