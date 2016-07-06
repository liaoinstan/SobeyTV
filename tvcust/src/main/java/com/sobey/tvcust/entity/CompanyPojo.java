package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class CompanyPojo {
    @SerializedName("lists")
    private List<Company> dataList;

    public List<Company> getDataList() {
        return dataList;
    }

    public void setDataList(List<Company> dataList) {
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        return "CompanyPojo{" +
                "dataList=" + dataList +
                '}';
    }
}
