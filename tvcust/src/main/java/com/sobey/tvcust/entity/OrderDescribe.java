package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * Created by Administrator on 2016/7/4 0004.
 */
public class OrderDescribe {

    @SerializedName("id")
    private Integer id;
    @SerializedName("content")
    private String detail;
    @SerializedName("createTime")
    private Long time;
    @SerializedName("jsImages")
    private String[] pathphotos;
    @SerializedName("jsVideos")
    private String[] pathvideos;
    @SerializedName("jsVoices")
    private String[] pathvoices;
    @SerializedName("from")
    private int from;
    @SerializedName("to")
    private int to;

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String[] getPathphotos() {
        return pathphotos;
    }

    public void setPathphotos(String[] pathphotos) {
        this.pathphotos = pathphotos;
    }

    public String[] getPathvideos() {
        return pathvideos;
    }

    public void setPathvideos(String[] pathvideos) {
        this.pathvideos = pathvideos;
    }

    public String[] getPathvoices() {
        return pathvoices;
    }

    public void setPathvoices(String[] pathvoices) {
        this.pathvoices = pathvoices;
    }

    @Override
    public String toString() {
        return "OrderDescribe{" +
                "id=" + id +
                ", detail='" + detail + '\'' +
                ", time=" + time +
                ", pathphotos=" + Arrays.toString(pathphotos) +
                ", pathvideos=" + Arrays.toString(pathvideos) +
                ", pathvoices=" + Arrays.toString(pathvoices) +
                ", from=" + from +
                ", to=" + to +
                '}';
    }
}
