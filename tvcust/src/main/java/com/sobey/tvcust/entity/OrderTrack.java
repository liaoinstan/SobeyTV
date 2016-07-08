package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;
import com.sobey.tvcust.interfaces.CharSort;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class OrderTrack{

    @SerializedName("id")
    private Integer id;
    @SerializedName("status")
    private Integer status;
    @SerializedName("content")
    private String content;
    @SerializedName("createtime")
    private long createtime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    @Override
    public String toString() {
        return "OrderTrack{" +
                "id=" + id +
                ", status=" + status +
                ", content='" + content + '\'' +
                ", createtime=" + createtime +
                '}';
    }
}
