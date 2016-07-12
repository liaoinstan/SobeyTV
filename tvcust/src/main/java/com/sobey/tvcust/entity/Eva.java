package com.sobey.tvcust.entity;

import java.io.Serializable;
import java.util.List;


public class Eva implements Serializable {

	private static final long serialVersionUID = 1727269244728865753L;
	
	private Integer serviceAttitude;
	
	private Integer disposeSpeed;
	
	private Integer productComment;
	
	private List<Integer> commentLableIds;

	private List<Lable> lables;

	public List<Lable> getLables() {
		return lables;
	}

	public void setLables(List<Lable> lables) {
		this.lables = lables;
	}

	public Integer getServiceAttitude() {
		return serviceAttitude;
	}

	public void setServiceAttitude(Integer serviceAttitude) {
		this.serviceAttitude = serviceAttitude;
	}

	public Integer getDisposeSpeed() {
		return disposeSpeed;
	}

	public void setDisposeSpeed(Integer disposeSpeed) {
		this.disposeSpeed = disposeSpeed;
	}

	public Integer getProductComment() {
		return productComment;
	}

	public void setProductComment(Integer productComment) {
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
