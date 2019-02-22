package org.bigdata.zczw.entity;

import java.sql.Timestamp;

/**
 * Image entity. @author MyEclipse Persistence Tools
 */

public class Image implements java.io.Serializable {

	// Fields

	private String imageId;
	private String imagePosition;
	private String imageSize;
	private Integer ftpId;
	private String dishId;
	private String deleteflag;
	private String createid;
	private Timestamp createtime;
	private String updid;
	private Timestamp updtime;

	// Constructors

	/** default constructor */
	public Image() {
	}

	/** minimal constructor */
	public Image(String imageId, Integer ftpId, String dishId,
				 String deleteflag, String createid, Timestamp createtime) {
		this.imageId = imageId;
		this.ftpId = ftpId;
		this.dishId = dishId;
		this.deleteflag = deleteflag;
		this.createid = createid;
		this.createtime = createtime;
	}

	/** full constructor */
	public Image(String imageId, String imagePosition, String imageSize,
				 Integer ftpId, String dishId, String deleteflag, String createid,
				 Timestamp createtime, String updid, Timestamp updtime) {
		this.imageId = imageId;
		this.imagePosition = imagePosition;
		this.imageSize = imageSize;
		this.ftpId = ftpId;
		this.dishId = dishId;
		this.deleteflag = deleteflag;
		this.createid = createid;
		this.createtime = createtime;
		this.updid = updid;
		this.updtime = updtime;
	}

	// Property accessors

	public String getImageId() {
		return this.imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getImagePosition() {
		return this.imagePosition;
	}

	public void setImagePosition(String imagePosition) {
		this.imagePosition = imagePosition;
	}

	public String getImageSize() {
		return this.imageSize;
	}

	public void setImageSize(String imageSize) {
		this.imageSize = imageSize;
	}

	public Integer getFtpId() {
		return this.ftpId;
	}

	public void setFtpId(Integer ftpId) {
		this.ftpId = ftpId;
	}

	public String getDishId() {
		return this.dishId;
	}

	public void setDishId(String dishId) {
		this.dishId = dishId;
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