package com.sobey.tvcust.entity;

import java.io.Serializable;

/**
 *  系统消息 实体
 * @author QimouXie
 *
 */
public class MsgSys implements Serializable {

	private static final long serialVersionUID = 6813911663821750275L;

	/**主键ID*/
	private Integer id;

	/**推送的人群 0 全部 1 用户 2 员工*/
	private Integer toGroup;

	/**Title*/
	private String title;

	/**内容*/
	private String content;

	private String brief;

	/**创建时间*/
	private long createTime;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getToGroup() {
		return toGroup;
	}

	public void setToGroup(Integer toGroup) {
		this.toGroup = toGroup;
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

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "MsgSys{" +
				"id=" + id +
				", toGroup=" + toGroup +
				", title='" + title + '\'' +
				", content='" + content + '\'' +
				", brief='" + brief + '\'' +
				", createTime=" + createTime +
				'}';
	}
}
