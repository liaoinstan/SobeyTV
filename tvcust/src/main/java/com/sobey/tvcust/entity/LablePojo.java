package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class LablePojo {
    @SerializedName("serviceLable")
    private List<Lable> serviceLable;
    @SerializedName("tscLable")
    private List<Lable> tscLable;
    @SerializedName("headTechLable")
    private List<Lable> headTechLable;
    @SerializedName("headDevelopLable")
    private List<Lable> headDevelopLable;

    public List<Lable> getServiceLable() {
        return serviceLable;
    }

    public void setServiceLable(List<Lable> serviceLable) {
        this.serviceLable = serviceLable;
    }

    public List<Lable> getTscLable() {
        return tscLable;
    }

    public void setTscLable(List<Lable> tscLable) {
        this.tscLable = tscLable;
    }

    public List<Lable> getHeadTechLable() {
        return headTechLable;
    }

    public void setHeadTechLable(List<Lable> headTechLable) {
        this.headTechLable = headTechLable;
    }

    public List<Lable> getHeadDevelopLable() {
        return headDevelopLable;
    }

    public void setHeadDevelopLable(List<Lable> headDevelopLable) {
        this.headDevelopLable = headDevelopLable;
    }

    @Override
    public String toString() {
        return "LablePojo{" +
                "serviceLable=" + serviceLable +
                ", tscLable=" + tscLable +
                ", headTechLable=" + headTechLable +
                ", headDevelopLable=" + headDevelopLable +
                '}';
    }
}
