package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class SBCountWarningStates {

    @SerializedName("KitGroupDetail")
    private List<SBWarningCount> KitGroupDetail;

    public List<SBWarningCount> getKitGroupDetail() {
        return KitGroupDetail;
    }

    public void setKitGroupDetail(List<SBWarningCount> kitGroupDetail) {
        KitGroupDetail = kitGroupDetail;
    }

    @Override
    public String toString() {
        return "SBCountWarningStates{" +
                "KitGroupDetail=" + KitGroupDetail +
                '}';
    }
}
