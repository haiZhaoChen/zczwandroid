package org.bigdata.zczw.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 评论消息列表
 * @author srrenyu
 *
 */
public class DynamicComments implements Serializable{
	private int code;
	private String message;		
	private Messages dynamicMessage;//原始动态消息
	private List<CommentsUser> commentsUserList;//评论部分字段和用户部分字段
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<CommentsUser> getCommentsUserList() {
		return commentsUserList;
	}
	public void setCommentsUserList(List<CommentsUser> commentsUserList) {
		this.commentsUserList = commentsUserList;
	}
	public Messages getDynamicMessage() {
		return dynamicMessage;
	}
	public void setDynamicMessage(Messages dynamicMessage) {
		this.dynamicMessage = dynamicMessage;
	}

	

	
}
