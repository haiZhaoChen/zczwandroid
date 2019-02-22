package org.bigdata.zczw.entity;

/**
 * 评论部分字段和用户部分字段
 * @author srrenyu
 *
 */
public class CommentsUser {
	private Integer userId;//评论人id
	//private String userName;//评论人名字

	private String commentsId;//评论id
	//private String commentsContent;//评论内容
	//private Timestamp commentsTime;//评论时间
	
	private User user;
	private Comments comments;			
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public String getCommentsId() {
		return commentsId;
	}
	public void setCommentsId(String commentsId) {
		this.commentsId = commentsId;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Comments getComments() {
		return comments;
	}
	public void setComments(Comments comments) {
		this.comments = comments;
	}


	
	
}
