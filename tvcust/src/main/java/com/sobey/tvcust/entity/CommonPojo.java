package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class CommonPojo {
    @SerializedName("lists")
    private List<CommonEntity> dataList;

    public List<CommonEntity> getDataList() {
        return dataList;
    }

    public void setDataList(List<CommonEntity> dataList) {
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        return "CommonPojo{" +
                "dataList=" + dataList +
                '}';
    }
}
