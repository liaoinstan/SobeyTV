package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class AssisterPojo {

    @SerializedName("lists")
    private List<User> dataList;

    public List<User> getDataList() {
        return dataList;
    }

    public void setDataList(List<User> dataList) {
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        return "AssisterPojo{" +
                "dataList=" + dataList +
                '}';
    }
}
