package com.sobey.tvcust.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class User implements Serializable {

    //管理员
    public static final int ROLE_MANAGER = 1;
    //技术人员
    public static final int ROLE_TECH_PERSON = 2;
    //客服
    public static final int ROLE_CUSTOMER = 3;
    //普通用户
    public static final int ROLE_COMMOM = 4;
    //售前
    public static final int ROLE_PRE_SALE = 5;
    //销售
    public static final int ROLE_SALE = 6;
    //运营
    public static final int ROLE_OPERATION = 7;
    //研发
    public static final int ROLE_INVENT = 8;
    //领导
    public static final int ROLE_LEADER = 9;
    //分公司技术支持
    public static final int ROLE_FILIALETECH = 10;
    //总部技术支持
    public static final int ROLE_HEADCOMTECH = 11;


    private int id;
    private String realName;
    private String avatar;
    private String jobTitle;
    private String introduce;
    private String mobile;
    private String email;
    private int status;
    private String token;
    private Long createDate;
    private Long updateDate;
    private String qq;
    private int officeId;
    private String officeName;
    private int roleType;
    private String tvName;

    //本地实例字段

    //是否被选中
    private boolean select;

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public String getTvName() {
        return tvName;
    }

    public void setTvName(String tvName) {
        this.tvName = tvName;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public Long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public int getOfficeId() {
        return officeId;
    }

    public void setOfficeId(int officeId) {
        this.officeId = officeId;
    }

    public int getRoleType() {
        return roleType;
    }

    public void setRoleType(int roleType) {
        this.roleType = roleType;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", realName='" + realName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", introduce='" + introduce + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", status=" + status +
                ", token='" + token + '\'' +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", qq='" + qq + '\'' +
                ", officeId=" + officeId +
                ", officeName='" + officeName + '\'' +
                ", roleType=" + roleType +
                ", tvName='" + tvName + '\'' +
                '}';
    }
}
