package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class OrderCategoryPojo {
    @SerializedName("lists")
    private List<OrderCategory> dataList;

    public List<OrderCategory> getDataList() {
        return dataList;
    }

    public void setDataList(List<OrderCategory> dataList) {
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        return "OrderCategory{" +
                "dataList=" + dataList +
                '}';
    }
}
