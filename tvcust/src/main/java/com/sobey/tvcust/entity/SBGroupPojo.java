package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class SBGroupPojo {
    @SerializedName("GroupList")
    private List<SBGroup> GroupList;

    public List<SBGroup> getGroupList() {
        return GroupList;
    }

    public void setGroupList(List<SBGroup> groupList) {
        GroupList = groupList;
    }

    @Override
    public String toString() {
        return "SBGroupPojo{" +
                "GroupList=" + GroupList +
                '}';
    }
}
