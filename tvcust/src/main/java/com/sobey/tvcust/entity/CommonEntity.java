package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class CommonEntity {

    private int id;
    private String officeName;
    private String tvstationName;

    @SerializedName("valicode")
    private String valicode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getTvstationName() {
        return tvstationName;
    }

    public void setTvstationName(String tvstationName) {
        this.tvstationName = tvstationName;
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
                ", officeName='" + officeName + '\'' +
                ", tvstationName='" + tvstationName + '\'' +
                ", valicode='" + valicode + '\'' +
                '}';
    }
}
