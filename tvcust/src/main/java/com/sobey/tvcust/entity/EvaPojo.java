package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class EvaPojo {
    @SerializedName("jsServiceData")
    private Eva serviceData ;
    @SerializedName("jsTSCData")
    private Eva tscdata ;
    @SerializedName("jsHeadTechData")
    private Eva headTechData ;
    @SerializedName("jsHeadDevelopData")
    private Eva headDevelopData ;
    @SerializedName("jsUserServiceData")
    private Eva userServerData ;
    @SerializedName("jsUserTechData")
    private Eva userTechData ;
    @SerializedName("commentContent")
    private String commentContent ;


    public Eva getUserServerData() {
        return userServerData;
    }

    public void setUserServerData(Eva userServerData) {
        this.userServerData = userServerData;
    }

    public Eva getUserTechData() {
        return userTechData;
    }

    public void setUserTechData(Eva userTechData) {
        this.userTechData = userTechData;
    }

    public Eva getServiceData() {
        return serviceData;
    }

    public void setServiceData(Eva serviceData) {
        this.serviceData = serviceData;
    }

    public Eva getTscdata() {
        return tscdata;
    }

    public void setTscdata(Eva tscdata) {
        this.tscdata = tscdata;
    }

    public Eva getHeadTechData() {
        return headTechData;
    }

    public void setHeadTechData(Eva headTechData) {
        this.headTechData = headTechData;
    }

    public Eva getHeadDevelopData() {
        return headDevelopData;
    }

    public void setHeadDevelopData(Eva headDevelopData) {
        this.headDevelopData = headDevelopData;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    @Override
    public String toString() {
        return "EvaPojo{" +
                "serviceData=" + serviceData +
                ", tscdata=" + tscdata +
                ", headTechData=" + headTechData +
                ", headDevelopData=" + headDevelopData +
                ", userServerData=" + userServerData +
                ", userTechData=" + userTechData +
                ", commentContent='" + commentContent + '\'' +
                '}';
    }
}
