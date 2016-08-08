package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class SBCountWarningPojo {
    @SerializedName("StatsList")
    private List<SBCountWarningStates> StatsList;

    public List<SBCountWarningStates> getStatsList() {
        return StatsList;
    }

    public void setStatsList(List<SBCountWarningStates> statsList) {
        StatsList = statsList;
    }

    @Override
    public String toString() {
        return "SBCountWarningPojo{" +
                "StatsList=" + StatsList +
                '}';
    }
}
