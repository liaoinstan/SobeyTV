package com.sobey.tvcust.entity;

import java.io.Serializable;
import java.util.List;


public class Eva implements Serializable {

	private static final long serialVersionUID = 1727269244728865753L;
	
	private int serviceAttitude;
	
	private int disposeSpeed;
	
	private int productComment;
	
	private List<Integer> commentLableIds;

	private List<Lable> lables;

	public List<Lable> getLables() {
		return lables;
	}

	public void setLables(List<Lable> lables) {
		this.lables = lables;
	}

	public int getServiceAttitude() {
		return serviceAttitude;
	}

	public void setServiceAttitude(Integer serviceAttitude) {
		this.serviceAttitude = serviceAttitude;
	}

	public int getDisposeSpeed() {
		return disposeSpeed;
	}

	public void setDisposeSpeed(int disposeSpeed) {
		this.disposeSpeed = disposeSpeed;
	}

	public int getProductComment() {
		return productComment;
	}

	public void setProductComment(int productComment) {
		this.productComment = productComment;
	}

	public List<Integer> getCommentLableIds() {
		return commentLableIds;
	}

	public void setCommentLableIds(List<Integer> commentLableIds) {
		this.commentLableIds = commentLableIds;
	}

	@Override
	public String toString() {
		return "Eva{" +
				"serviceAttitude=" + serviceAttitude +
				", disposeSpeed=" + disposeSpeed +
				", productComment=" + productComment +
				", commentLableIds=" + commentLableIds +
				", lables=" + lables +
				'}';
	}
}
