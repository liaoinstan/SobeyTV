package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class Order {

    //待处理
    public static final int ORDER_UNDEAL = 2001;
    //处理中
    public static final int ORDER_INDEAL = 2002;
    //待验证
    public static final int ORDER_UNVALI = 2003;
    //待评价
    public static final int ORDER_UNEVA = 2004;
    //已解决
    public static final int ORDER_FINSH = 2005;

    @SerializedName("id")
    private Integer id;

    @SerializedName("orderCategory")
    private Integer orderCategory;

    @SerializedName("orderDescri")
    private String orderDescri;

    @SerializedName("images")
    private String[] images;

    @SerializedName("videos")
    private String[] videos;

    @SerializedName("voices")
    private String[] voices;

    @SerializedName("serviceId")
    private Integer serviceId;

    @SerializedName("status")
    private Integer status;

    @SerializedName("serviceCheck")
    private String serviceCheck;

    @SerializedName("techCheck")
    private String techCheck;

    @SerializedName("headTechId")
    private Integer headTechId;

    @SerializedName("tscId")
    private Integer tscId;

    @SerializedName("decelopId")
    private Integer decelopId;

    @SerializedName("createTime")
    private Long createTime;

    @SerializedName("userId")
    private Integer userId;

    @SerializedName("count")
    private Integer count;

    @SerializedName("orderNumber")
    private String orderNumber;

    @SerializedName("category")
    private OrderCategory category;

    public OrderCategory getCategory() {
        return category;
    }

    public void setCategory(OrderCategory category) {
        this.category = category;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderCategory() {
        return orderCategory;
    }

    public void setOrderCategory(Integer orderCategory) {
        this.orderCategory = orderCategory;
    }

    public String getOrderDescri() {
        return orderDescri;
    }

    public void setOrderDescri(String orderDescri) {
        this.orderDescri = orderDescri;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public String[] getVideos() {
        return videos;
    }

    public void setVideos(String[] videos) {
        this.videos = videos;
    }

    public String[] getVoices() {
        return voices;
    }

    public void setVoices(String[] voices) {
        this.voices = voices;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getServiceCheck() {
        return serviceCheck;
    }

    public void setServiceCheck(String serviceCheck) {
        this.serviceCheck = serviceCheck;
    }

    public String getTechCheck() {
        return techCheck;
    }

    public void setTechCheck(String techCheck) {
        this.techCheck = techCheck;
    }

    public Integer getHeadTechId() {
        return headTechId;
    }

    public void setHeadTechId(Integer headTechId) {
        this.headTechId = headTechId;
    }

    public Integer getTscId() {
        return tscId;
    }

    public void setTscId(Integer tscId) {
        this.tscId = tscId;
    }

    public Integer getDecelopId() {
        return decelopId;
    }

    public void setDecelopId(Integer decelopId) {
        this.decelopId = decelopId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderCategory=" + orderCategory +
                ", orderDescri='" + orderDescri + '\'' +
                ", images=" + Arrays.toString(images) +
                ", videos=" + Arrays.toString(videos) +
                ", voices=" + Arrays.toString(voices) +
                ", serviceId=" + serviceId +
                ", status=" + status +
                ", serviceCheck='" + serviceCheck + '\'' +
                ", techCheck='" + techCheck + '\'' +
                ", headTechId=" + headTechId +
                ", tscId=" + tscId +
                ", decelopId=" + decelopId +
                ", createTime=" + createTime +
                ", userId=" + userId +
                ", count=" + count +
                ", orderNumber='" + orderNumber + '\'' +
                ", category=" + category +
                '}';
    }
}
