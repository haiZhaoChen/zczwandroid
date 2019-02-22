package org.bigdata.zczw.entity;

import java.sql.Timestamp;

/**
 * Comments entity. @author MyEclipse Persistence Tools
 */

public class Comments implements java.io.Serializable {

	// Fields

	private String commentsId;
	private String commentsContent;
	private Timestamp commentsTime;
	private String commentsedMessageId;
	private String originalMessageId;
	private String deleteflag;
	private String createid;
	private Timestamp createtime;
	private String updid;
	private Timestamp updtime;

	// Constructors

	/** default constructor */
	public Comments() {
	}

	private String publishTime;

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getPublishTime() {
		return publishTime;
	}

	/** minimal constructor */

	/** full constructor */
	public Comments(String commentsId, String commentsContent,
					Timestamp commentsTime, String commentsedMessageId,
					String originalMessageId, String deleteflag, String createid,
					Timestamp createtime, String updid, Timestamp updtime) {
		this.commentsId = commentsId;
		this.commentsContent = commentsContent;
		this.commentsTime = commentsTime;
		this.commentsedMessageId = commentsedMessageId;
		this.originalMessageId = originalMessageId;
		this.deleteflag = deleteflag;
		this.createid = createid;
		this.createtime = createtime;
		this.updid = updid;
		this.updtime = updtime;
	}

	// Property accessors

	@Override
	public String toString() {
		return "Comments [commentsId=" + commentsId + ", commentsContent="
				+ commentsContent + ", commentsTime=" + commentsTime
				+ ", commentsedMessageId=" + commentsedMessageId
				+ ", originalMessageId=" + originalMessageId + ", deleteflag="
				+ deleteflag + ", createid=" + createid + ", createtime="
				+ createtime + ", updid=" + updid + ", updtime=" + updtime
				+ ", publishTime=" + publishTime + "]";
	}

	public String getCommentsId() {
		return this.commentsId;
	}

	public void setCommentsId(String commentsId) {
		this.commentsId = commentsId;
	}

	public String getCommentsContent() {
		return this.commentsContent;
	}

	public void setCommentsContent(String commentsContent) {
		this.commentsContent = commentsContent;
	}

	public Timestamp getCommentsTime() {
		return this.commentsTime;
	}

	public void setCommentsTime(Timestamp commentsTime) {
		this.commentsTime = commentsTime;
	}

	public String getCommentsedMessageId() {
		return this.commentsedMessageId;
	}

	public void setCommentsedMessageId(String commentsedMessageId) {
		this.commentsedMessageId = commentsedMessageId;
	}

	public String getOriginalMessageId() {
		return this.originalMessageId;
	}

	public void setOriginalMessageId(String originalMessageId) {
		this.originalMessageId = originalMessageId;
	}

	public String getDeleteflag() {
		return this.deleteflag;
	}

	public void setDeleteflag(String deleteflag) {
		this.deleteflag = deleteflag;
	}

	public String getCreateid() {
		return this.createid;
	}

	public void setCreateid(String createid) {
		this.createid = createid;
	}

	public Timestamp getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

	public String getUpdid() {
		return this.updid;
	}

	public void setUpdid(String updid) {
		this.updid = updid;
	}

	public Timestamp getUpdtime() {
		return this.updtime;
	}

	public void setUpdtime(Timestamp updtime) {
		this.updtime = updtime;
	}

}