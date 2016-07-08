package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class OrderTrackPojo {
    @SerializedName("lists")
    private List<OrderTrack> dataList;
    @SerializedName("order")
    private Order order;

    public List<OrderTrack> getDataList() {
        return dataList;
    }

    public void setDataList(List<OrderTrack> dataList) {
        this.dataList = dataList;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "OrderTrackPojo{" +
                "dataList=" + dataList +
                ", order=" + order +
                '}';
    }
}
