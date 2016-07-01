package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class OrderPojo {
    @SerializedName("lists")
    private List<Order> dataList;

    public List<Order> getDataList() {
        return dataList;
    }

    public void setDataList(List<Order> dataList) {
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        return "OrderPojo{" +
                "dataList=" + dataList +
                '}';
    }
}
