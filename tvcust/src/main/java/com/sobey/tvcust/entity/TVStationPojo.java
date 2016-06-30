package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class TVStationPojo {
    @SerializedName("lists")
    private List<TVStation> dataList;

    public List<TVStation> getDataList() {
        return dataList;
    }

    public void setDataList(List<TVStation> dataList) {
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        return "TVStationPojo{" +
                "dataList=" + dataList +
                '}';
    }
}
