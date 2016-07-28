package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class CountTabPojo {
    @SerializedName("lists")
    private List<CountTab> dataList;

    public List<CountTab> getDataList() {
        return dataList;
    }

    public void setDataList(List<CountTab> dataList) {
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        return "CountTabPojo{" +
                "dataList=" + dataList +
                '}';
    }
}
