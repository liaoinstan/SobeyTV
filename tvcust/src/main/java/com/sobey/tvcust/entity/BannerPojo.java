package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;
import com.sobey.common.entity.Images;

import java.util.List;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class BannerPojo {
    @SerializedName("lists")
    private List<Images> dataList;

    public List<Images> getDataList() {
        return dataList;
    }

    public void setDataList(List<Images> dataList) {
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        return "BannerPojo{" +
                "dataList=" + dataList +
                '}';
    }
}

