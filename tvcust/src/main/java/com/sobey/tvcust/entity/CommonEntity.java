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

    @SerializedName("url")
    private String filePath;

    @SerializedName("count")
    private int count;

    @SerializedName("finished")
    private int finished;

    @SerializedName("nonFinished")
    private int nonFinished;

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    public int getNonFinished() {
        return nonFinished;
    }

    public void setNonFinished(int nonFinished) {
        this.nonFinished = nonFinished;
    }

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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "CommonEntity{" +
                "id=" + id +
                ", valicode='" + valicode + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", type=" + type +
                ", filePath='" + filePath + '\'' +
                ", count=" + count +
                '}';
    }
}
