package com.sobey.tvcust.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Sign implements Serializable {

	@SerializedName("id")
	private int id;
	@SerializedName("bonus")
	private int grades;
	@SerializedName("createTime")
	private long time;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getGrades() {
		return grades;
	}

	public void setGrades(int grades) {
		this.grades = grades;
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
				", grades=" + grades +
				", time=" + time +
				'}';
	}
}
