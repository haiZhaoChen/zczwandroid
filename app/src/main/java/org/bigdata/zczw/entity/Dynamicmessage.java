package org.bigdata.zczw.entity;

import java.sql.Timestamp;

/**
 * Dynamicmessage entity. @author MyEclipse Persistence Tools
 */

public class Dynamicmessage implements java.io.Serializable {

	// Fields

	private String messageId;
	private Integer userId;
	private String messageContent;
	private Timestamp publishTime;
	private String collectNum;
	private String commentsNum;
	private String gpsX;
	private String gpsY;
	private String publishAddressTab;
	private String visible;
	private String deleteflag;
	private String createid;
//	private Timestamp createtime;
	private String updid;
	private Timestamp updtime;
	
	
	/* 额外字段 */
	private String imagePosition;// 图片位置(zufei)
	private String PictureNum;// 图片数量(zufei)
	private String userName;// 用户名(zufei)
	
	
	private String showTime;//前台显示时间
	

	// Constructors

	/** default constructor */
	public Dynamicmessage() {
	}

	/** minimal constructor */
	public Dynamicmessage(String messageId, Integer userId,
						  String messageContent, Timestamp publishTime, String collectNum,
						  String commentsNum, String visible, String deleteflag,
						  String createid, Timestamp createtime) {
		this.messageId = messageId;
		this.userId = userId;
		this.messageContent = messageContent;
		this.publishTime = publishTime;
		this.collectNum = collectNum;
		this.commentsNum = commentsNum;
		this.visible = visible;
		this.deleteflag = deleteflag;
		this.createid = createid;
//		this.createtime = createtime;
	}

	/** full constructor */
	public Dynamicmessage(String messageId, Integer userId,
						  String messageContent, Timestamp publishTime, String collectNum,
						  String commentsNum, String gpsX, String gpsY,
						  String publishAddressTab, String visible, String deleteflag,
						  String createid, Timestamp createtime, String updid,
						  Timestamp updtime) {
		this.messageId = messageId;
		this.userId = userId;
		this.messageContent = messageContent;
		this.publishTime = publishTime;
		this.collectNum = collectNum;
		this.commentsNum = commentsNum;
		this.gpsX = gpsX;
		this.gpsY = gpsY;
		this.publishAddressTab = publishAddressTab;
		this.visible = visible;
		this.deleteflag = deleteflag;
		this.createid = createid;
//		this.createtime = createtime;
		this.updid = updid;
		this.updtime = updtime;
	}

	// Property accessors


	public String getMessageId() {
		return this.messageId;
	}



	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getMessageContent() {
		return this.messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public Timestamp getPublishTime() {
		return this.publishTime;
	}

	public void setPublishTime(Timestamp publishTime) {
		this.publishTime = publishTime;
	}

	public String getCollectNum() {
		return this.collectNum;
	}

	public void setCollectNum(String collectNum) {
		this.collectNum = collectNum;
	}

	public String getCommentsNum() {
		return this.commentsNum;
	}

	public void setCommentsNum(String commentsNum) {
		this.commentsNum = commentsNum;
	}

	public String getGpsX() {
		return this.gpsX;
	}

	public void setGpsX(String gpsX) {
		this.gpsX = gpsX;
	}

	public String getGpsY() {
		return this.gpsY;
	}

	public void setGpsY(String gpsY) {
		this.gpsY = gpsY;
	}

	public String getPublishAddressTab() {
		return this.publishAddressTab;
	}

	public void setPublishAddressTab(String publishAddressTab) {
		this.publishAddressTab = publishAddressTab;
	}

	public String getVisible() {
		return this.visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
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

//	public Timestamp getCreatetime() {
//		return this.createtime;
//	}
//
//	public void setCreatetime(Timestamp createtime) {
//		this.createtime = createtime;
//	}

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

	public String getImagePosition() {
		return imagePosition;
	}

	public void setImagePosition(String imagePosition) {
		this.imagePosition = imagePosition;
	}

	public String getPictureNum() {
		return PictureNum;
	}

	public void setPictureNum(String pictureNum) {
		PictureNum = pictureNum;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getShowTime() {
		return showTime;
	}

	public void setShowTime(String showTime) {
		this.showTime = showTime;
	}
	
	

}