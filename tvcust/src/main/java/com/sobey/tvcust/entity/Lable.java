package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class Lable {
    @SerializedName("id")
    private Integer id;
    @SerializedName("lable")
    private String lable;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    @Override
    public String toString() {
        return "Lable{" +
                "id=" + id +
                ", lable=" + lable +
                '}';
    }
}
