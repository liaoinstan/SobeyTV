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
    //待验收
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

    @SerializedName("jsImages")
    private String[] images;

    @SerializedName("jsVideos")
    private String[] videos;

    @SerializedName("jsVoices")
    private String[] voices;

    @SerializedName("serviceId")
    private Integer serviceId;

    @SerializedName("status")
    private Integer status;

    @SerializedName("serviceCheck")
    private Integer serviceCheck;

    @SerializedName("techCheck")
    private Integer techCheck;

    @SerializedName("developCheck")
    private Integer developCheck;

    @SerializedName("headTechCheck")
    private Integer headTechCheck;

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

    /**总技术是否 评论 0:未评论 1 已评论 2表示没权限评论*/
    @SerializedName("isHeadTechComment")
    private Integer isHeadTechComment;

    /**客服是否评论*/
    @SerializedName("isServiceComment")
    private Integer isServiceComment;

    /**TSC是否评论*/
    @SerializedName("isTSCComment")
    private Integer isTSCComment;

    /**研发是否评论*/
    @SerializedName("isHeadDevelopComment")
    private Integer isHeadDevelopComment;

    /**是否 直接分配给总公司技术*/
    @SerializedName("isHeadTech")
    private Integer isHeadTech;

    /**用户是否评价*/
    private Integer isUsercomment;

    public Integer getIsUsercomment() {
        return isUsercomment;
    }

    public void setIsUsercomment(Integer isUsercomment) {
        this.isUsercomment = isUsercomment;
    }

    public Integer getIsHeadTechComment() {
        return isHeadTechComment;
    }

    public void setIsHeadTechComment(Integer isHeadTechComment) {
        this.isHeadTechComment = isHeadTechComment;
    }

    public Integer getIsServiceComment() {
        return isServiceComment;
    }

    public void setIsServiceComment(Integer isServiceComment) {
        this.isServiceComment = isServiceComment;
    }

    public Integer getIsTSCComment() {
        return isTSCComment;
    }

    public void setIsTSCComment(Integer isTSCComment) {
        this.isTSCComment = isTSCComment;
    }

    public Integer getIsHeadDevelopComment() {
        return isHeadDevelopComment;
    }

    public void setIsHeadDevelopComment(Integer isHeadDevelopComment) {
        this.isHeadDevelopComment = isHeadDevelopComment;
    }

    public Integer getIsHeadTech() {
        return isHeadTech;
    }

    public void setIsHeadTech(Integer isHeadTech) {
        this.isHeadTech = isHeadTech;
    }

    public Integer getDevelopCheck() {
        return developCheck;
    }

    public void setDevelopCheck(Integer developCheck) {
        this.developCheck = developCheck;
    }

    public Integer getHeadTechCheck() {
        return headTechCheck;
    }

    public void setHeadTechCheck(Integer headTechCheck) {
        this.headTechCheck = headTechCheck;
    }

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

    public Integer getServiceCheck() {
        return serviceCheck;
    }

    public void setServiceCheck(Integer serviceCheck) {
        this.serviceCheck = serviceCheck;
    }

    public Integer getTechCheck() {
        return techCheck;
    }

    public void setTechCheck(Integer techCheck) {
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
                ", serviceCheck=" + serviceCheck +
                ", techCheck=" + techCheck +
                ", developCheck=" + developCheck +
                ", headTechCheck=" + headTechCheck +
                ", headTechId=" + headTechId +
                ", tscId=" + tscId +
                ", decelopId=" + decelopId +
                ", createTime=" + createTime +
                ", userId=" + userId +
                ", count=" + count +
                ", orderNumber='" + orderNumber + '\'' +
                ", category=" + category +
                ", isHeadTechComment=" + isHeadTechComment +
                ", isServiceComment=" + isServiceComment +
                ", isTSCComment=" + isTSCComment +
                ", isHeadDevelopComment=" + isHeadDevelopComment +
                ", isHeadTech=" + isHeadTech +
                ", isUsercomment=" + isUsercomment +
                '}';
    }
}
