package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class MsgOrderPojo {
    @SerializedName("lists")
    private List<MsgOrder> dataList;

    public List<MsgOrder> getDataList() {
        return dataList;
    }

    public void setDataList(List<MsgOrder> dataList) {
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        return "MsgOrderPojo{" +
                "dataList=" + dataList +
                '}';
    }
}
