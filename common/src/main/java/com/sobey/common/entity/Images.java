package com.sobey.common.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by wpy on 15/9/2.
 */
public class Images implements Serializable {
//    public String mTitle;
//    public int mImageResourceId;
//
//    public Images(String mTitle, int mImageResourceId) {
//        this.mTitle = mTitle;
//        this.mImageResourceId = mImageResourceId;
//    }
//
//    public static List<Images> createSampleImages() {
//        final List<Images> images = new ArrayList<Images>();
//        images.add(new Images("banner1", R.drawable.banner1));
//        images.add(new Images("banner2", R.drawable.banner2));
//        images.add(new Images("banner3", R.drawable.banner3));
//        images.add(new Images("banner4", R.drawable.banner4));
//        return images;
//    }

    private int id;
    private String title;

    @SerializedName("image")
    private String img;

    @SerializedName("imgUrl")
    private String url;

    //是否是内部文章， 0 表示 不是内部文章 1表示 是内部文章
    @SerializedName("isInside")
    private int isInside;

    @SerializedName("newsId")
    private int newsId;

    @SerializedName("news")
    private Article article;

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public int getIsInside() {
        return isInside;
    }

    public void setIsInside(int isInside) {
        this.isInside = isInside;
    }

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    public Images(int id, String img) {
        this.id = id;
        this.img = img;
    }
    public Images(String img) {
        this.img = img;
    }
    public Images(int id, String title, String img) {
        this.id = id;
        this.title = title;
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }


    @Override
    public String toString() {
        return "Images{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", img='" + img + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
