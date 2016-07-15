package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class SignPojo {
    @SerializedName("lists")
    private List<Sign> dataList;

    public List<Sign> getDataList() {
        return dataList;
    }

    public void setDataList(List<Sign> dataList) {
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        return "SignPojo{" +
                "dataList=" + dataList +
                '}';
    }
}

