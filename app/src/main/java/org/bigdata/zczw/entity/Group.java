package org.bigdata.zczw.entity;

import java.io.Serializable;

public class Group implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3758608554620217466L;

	private String groupId;
	/*
	 * 群组名称
	 */
	private String groupName;

	/**
	 * 群组介绍
	 */
	private String introduce;
	/**
	 * 群组当前人数
	 */
	private String number;

	private String portrai;

	public String getGroupId() {
		return groupId;
	}

	
	
	public Group(String groupId, String groupName, String introduce,
				 String portrai) {
		super();
		this.groupId = groupId;
		this.groupName = groupName;
		this.introduce = introduce;
		this.portrai = portrai;
	}


	public Group(String groupId, String groupName, String portrai) {
		this.groupId = groupId;
		this.groupName = groupName;
		this.portrai = portrai;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getPortrai() {
		return portrai;
	}

	public void setPortrai(String portrai) {
		this.portrai = portrai;
	}

}
