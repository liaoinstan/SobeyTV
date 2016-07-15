package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Sign implements Serializable {

	@SerializedName("id")
	private int id;
	@SerializedName("title")
	private String title;
	@SerializedName("time")
	private long time;

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

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "Sign{" +
				"id=" + id +
				", title='" + title + '\'' +
				", time=" + time +
				'}';
	}
}
