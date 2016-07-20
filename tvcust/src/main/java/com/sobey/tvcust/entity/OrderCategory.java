package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/4 0004.
 */
public class OrderCategory implements Serializable{
    @SerializedName("id")
    private int id;

    @SerializedName("categoryName")
    private String categoryName;

    @SerializedName("type")
    private int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "OrderCategory{" +
                "id=" + id +
                ", categoryName='" + categoryName + '\'' +
                ", type=" + type +
                '}';
    }
}
