package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class MsgSysPojo {
    @SerializedName("lists")
    private List<MsgSys> dataList;

    public List<MsgSys> getDataList() {
        return dataList;
    }

    public void setDataList(List<MsgSys> dataList) {
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        return "MsgSysPojo{" +
                "dataList=" + dataList +
                '}';
    }
}
