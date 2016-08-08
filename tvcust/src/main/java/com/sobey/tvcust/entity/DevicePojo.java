package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class DevicePojo {
    @SerializedName("lists")
    private List<Device> lists;

    public List<Device> getLists() {
        return lists;
    }

    public void setLists(List<Device> lists) {
        this.lists = lists;
    }

    @Override
    public String toString() {
        return "DevicePojo{" +
                "lists=" + lists +
                '}';
    }
}
