package com.sobey.common.entity;

import java.io.Serializable;
import java.util.List;


public class Article implements Serializable {

	private int id;
	private String title;
	private String content;
	private String introduction;
	private String imageUrl;
	private int typeId;
	private int likes;
	private int reads;
	private int isUrl;
	private String linkUrl;
	private int isShowIcon;
	private long createTime;
	private long UpdateTime;

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public int getReads() {
		return reads;
	}

	public void setReads(int reads) {
		this.reads = reads;
	}

	public int getIsUrl() {
		return isUrl;
	}

	public void setIsUrl(int isUrl) {
		this.isUrl = isUrl;
	}

	public int getIsShowIcon() {
		return isShowIcon;
	}

	public void setIsShowIcon(int isShowIcon) {
		this.isShowIcon = isShowIcon;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return UpdateTime;
	}

	public void setUpdateTime(long updateTime) {
		UpdateTime = updateTime;
	}

	@Override
	public String toString() {
		return "Article{" +
				"id=" + id +
				", title='" + title + '\'' +
				", content='" + content + '\'' +
				", introduction='" + introduction + '\'' +
				", imageUrl='" + imageUrl + '\'' +
				", typeId=" + typeId +
				", likes=" + likes +
				", reads=" + reads +
				", isUrl=" + isUrl +
				", isShowIcon=" + isShowIcon +
				", createTime=" + createTime +
				", UpdateTime=" + UpdateTime +
				'}';
	}
}
