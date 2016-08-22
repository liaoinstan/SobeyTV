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

    //是否点赞
    @SerializedName("isLikes")
    private boolean isZan;
    //是否签到
    @SerializedName("isSign")
    private int isSign;
    //签到天数
    @SerializedName("signs")
    private int signDays;
    //积分总数
    @SerializedName("bonus")
    private int signGrades;
    //总积分
    @SerializedName("countBonus")
    private int countBonus;

    public int getCountBonus() {
        return countBonus;
    }

    public void setCountBonus(int countBonus) {
        this.countBonus = countBonus;
    }

    public int getIsSign() {
        return isSign;
    }

    public void setIsSign(int isSign) {
        this.isSign = isSign;
    }

    public int getSignDays() {
        return signDays;
    }

    public void setSignDays(int signDays) {
        this.signDays = signDays;
    }

    public int getSignGrades() {
        return signGrades;
    }

    public void setSignGrades(int signGrades) {
        this.signGrades = signGrades;
    }

    public boolean isZan() {
        return isZan;
    }

    public void setZan(boolean zan) {
        isZan = zan;
    }

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
