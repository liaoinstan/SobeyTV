package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;
import com.sobey.common.entity.Article;

import java.util.List;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class ArticlePojo {
    @SerializedName("lists")
    private List<Article> dataList;

    public List<Article> getDataList() {
        return dataList;
    }

    public void setDataList(List<Article> dataList) {
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        return "ArticlePojo{" +
                "dataList=" + dataList +
                '}';
    }
}

