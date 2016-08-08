package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class WarningPojo {
    @SerializedName("lists")
    private List<Warning> lists;

    public List<Warning> getLists() {
        return lists;
    }

    public void setLists(List<Warning> lists) {
        this.lists = lists;
    }

    @Override
    public String toString() {
        return "WarningPojo{" +
                "lists=" + lists +
                '}';
    }
}
