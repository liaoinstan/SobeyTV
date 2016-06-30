package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class OfficePojo {
    @SerializedName("lists")
    private List<Office> dataList;

    public List<Office> getDataList() {
        return dataList;
    }

    public void setDataList(List<Office> dataList) {
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        return "OfficePojo{" +
                "dataList=" + dataList +
                '}';
    }
}
