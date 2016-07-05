package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class OrderPojo {
    @SerializedName("lists")
    private List<Order> dataList;

    @SerializedName("count")
    private Integer count;

    @SerializedName("totalPage")
    private Integer totalPage;

    public List<Order> getDataList() {
        return dataList;
    }

    public void setDataList(List<Order> dataList) {
        this.dataList = dataList;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    @Override
    public String toString() {
        return "OrderPojo{" +
                "dataList=" + dataList +
                ", count=" + count +
                ", totalPage=" + totalPage +
                '}';
    }
}
