package com.sobey.tvcust.entity;

import java.io.Serializable;
import java.util.List;


public class Article implements Serializable {

	private int id;
	private String title;
	private String content;
	private String introduction;
	private String imageUrl;
	private String typeId;
	private String likes;
	private String reads;
	private String isUrl;
	private String isShowIcon;
	private String createTime;
	private String UpdateTime;

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

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getLikes() {
		return likes;
	}

	public void setLikes(String likes) {
		this.likes = likes;
	}

	public String getReads() {
		return reads;
	}

	public void setReads(String reads) {
		this.reads = reads;
	}

	public String getIsUrl() {
		return isUrl;
	}

	public void setIsUrl(String isUrl) {
		this.isUrl = isUrl;
	}

	public String getIsShowIcon() {
		return isShowIcon;
	}

	public void setIsShowIcon(String isShowIcon) {
		this.isShowIcon = isShowIcon;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return UpdateTime;
	}

	public void setUpdateTime(String updateTime) {
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
				", typeId='" + typeId + '\'' +
				", likes='" + likes + '\'' +
				", reads='" + reads + '\'' +
				", isUrl='" + isUrl + '\'' +
				", isShowIcon='" + isShowIcon + '\'' +
				", createTime='" + createTime + '\'' +
				", UpdateTime='" + UpdateTime + '\'' +
				'}';
	}
}
