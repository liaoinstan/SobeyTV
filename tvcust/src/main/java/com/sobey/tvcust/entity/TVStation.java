package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class TVStation {

    @SerializedName("id")
    private Integer id;
    @SerializedName("tvName")
    private String car_title;   //显示的数据
    private String sortLetters;  //显示数据拼音的首字母

    private String car_title_html;

    public TVStation() {
    }

    public TVStation(Integer id) {
        this.id = id;
    }

    public TVStation(String car_title) {
        this.car_title = car_title;
    }

    public TVStation(Integer id, String car_title) {
        this.id = id;
        this.car_title = car_title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCar_title() {
        return car_title;
    }

    public void setCar_title(String car_title) {
        this.car_title = car_title;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public String getCar_title_html() {
        return car_title_html;
    }

    public void setCar_title_html(String car_title_html) {
        this.car_title_html = car_title_html;
    }

    @Override
    public String toString() {
        return "Office{" +
                "id=" + id +
                ", car_title='" + car_title + '\'' +
                ", sortLetters='" + sortLetters + '\'' +
                ", car_title_html='" + car_title_html + '\'' +
                '}';
    }
}
