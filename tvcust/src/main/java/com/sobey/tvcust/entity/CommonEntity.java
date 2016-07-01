package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class CommonEntity {

    @SerializedName("id")
    private int id;

    @SerializedName("valicode")
    private String valicode;

    @SerializedName("categoryName")
    private String categoryName;

    @SerializedName("type")
    private int type;

    private String filePath;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValicode() {
        return valicode;
    }

    public void setValicode(String valicode) {
        this.valicode = valicode;
    }

    @Override
    public String toString() {
        return "CommonEntity{" +
                "id=" + id +
                ", valicode='" + valicode + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", type=" + type +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
