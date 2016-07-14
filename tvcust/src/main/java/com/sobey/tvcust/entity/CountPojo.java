package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class CountPojo {
    @SerializedName("lists")
    private Map<String,Integer> dataList;

    public Map<String,Integer> getDataList() {
        return dataList;
    }

    public void setDataList(Map<String,Integer> dataList) {
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        return "CountPojo{" +
                "dataList=" + dataList +
                '}';
    }
}
