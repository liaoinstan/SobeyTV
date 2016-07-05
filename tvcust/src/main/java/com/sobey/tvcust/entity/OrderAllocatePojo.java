package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class OrderAllocatePojo {

    @SerializedName("user")
    private User user;

    @SerializedName("order")
    private Order order;

    @SerializedName("feibian")
    private List<User> feibian;

    @SerializedName("tsc")
    private List<User> tsc;

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

    public List<User> getFeibian() {
        return feibian;
    }

    public void setFeibian(List<User> feibian) {
        this.feibian = feibian;
    }

    public List<User> getTsc() {
        return tsc;
    }

    public void setTsc(List<User> tsc) {
        this.tsc = tsc;
    }

    @Override
    public String toString() {
        return "OrderAllocatePojo{" +
                "user=" + user +
                ", order=" + order +
                ", feibian=" + feibian +
                ", tsc=" + tsc +
                '}';
    }
}
