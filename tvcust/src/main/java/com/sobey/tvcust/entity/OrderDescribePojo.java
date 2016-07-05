package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class OrderDescribePojo {

    @SerializedName("tsc")
    private User tsc;

    @SerializedName("user")
    private User user;

    @SerializedName("order")
    private Order order;

    @SerializedName("lists")
    private List<OrderDescribe> dataList;

    public User getTsc() {
        return tsc;
    }

    public void setTsc(User tsc) {
        this.tsc = tsc;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<OrderDescribe> getDataList() {
        return dataList;
    }

    public void setDataList(List<OrderDescribe> dataList) {
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        return "OrderDescribePojo{" +
                "tsc=" + tsc +
                ", user=" + user +
                ", order=" + order +
                ", dataList=" + dataList +
                '}';
    }
}
