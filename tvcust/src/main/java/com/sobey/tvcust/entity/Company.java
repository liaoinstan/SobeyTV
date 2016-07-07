package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;
import com.sobey.tvcust.interfaces.CharSort;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class Company implements CharSort {

    @SerializedName("id")
    private Integer id;
    @SerializedName("companyName")
    private String car_title;   //显示的数据
    private String sortLetters;  //显示数据拼音的首字母

    private String car_title_html;
    @Override
    public Integer getId() {
        return id;
    }
    @Override
    public void setId(Integer id) {
        this.id = id;
    }
    @Override
    public String getCar_title() {
        return car_title;
    }
    @Override
    public void setCar_title(String car_title) {
        this.car_title = car_title;
    }
    @Override
    public String getSortLetters() {
        return sortLetters;
    }
    @Override
    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }
    @Override
    public String getCar_title_html() {
        return car_title_html;
    }
    @Override
    public void setCar_title_html(String car_title_html) {
        this.car_title_html = car_title_html;
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", car_title='" + car_title + '\'' +
                ", sortLetters='" + sortLetters + '\'' +
                ", car_title_html='" + car_title_html + '\'' +
                '}';
    }
}
