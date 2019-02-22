package org.bigdata.zczw.entity;

/**
 * Created by SVN on 2018/3/5.
 */

public class BoxMsg {
    /**
     * id : 1
     * creator : {"id":1110,"name":"陈海召","portrait":"http://zczwstorage.ewonline.org:8094/images/2017/07/05/1499220134519199.jpg","phone":"18032098867","unitsName":"系统运维","jobsName":"其他"}
     * createDate : 1519869021000
     * commentId : null
     * commentContent : null
     * commentAtRange : null
     * commentAtUserId : null
     * praiseId : 50951
     * message : {"author":{"id":2067,"name":"张涛","portrait":"http://zczwstorage.ewonline.org:8094/images/2017/11/13/1510558987873374.jpg","phone":"18032098869","unitsName":"系统运维","jobsName":"其他"},"content":"测试","pictures":[],"video":null,"commentNum":1,"collectNum":0,"praiseNum":2,"sameUnit":1,"longitude":"114.521335","latitude":"38.038359","location":"中国河北省石家庄市裕华区槐北路39号在河北科技大学附近","collect":0,"praise":1,"publishedTime":1519868767000,"messageId":"1519868767409092757","viewNum":2,"forwardMessage":null,"forwardNum":0,"forwardMessageId":null,"publicScope":1,"priority":0}
     * parentCommentCreator : null
     */

    private int id;
    private Author creator;
    private long createDate;
    private long commentId;
    private String commentContent;
    private String commentAtRange;
    private String commentAtUserId;
    private int praiseId;
    private Record message;
    private Author parentCommentCreator;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Author getCreator() {
        return creator;
    }

    public void setCreator(Author creator) {
        this.creator = creator;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getCommentAtRange() {
        return commentAtRange;
    }

    public void setCommentAtRange(String commentAtRange) {
        this.commentAtRange = commentAtRange;
    }

    public String getCommentAtUserId() {
        return commentAtUserId;
    }

    public void setCommentAtUserId(String commentAtUserId) {
        this.commentAtUserId = commentAtUserId;
    }

    public int getPraiseId() {
        return praiseId;
    }

    public void setPraiseId(int praiseId) {
        this.praiseId = praiseId;
    }

    public Record getMessage() {
        return message;
    }

    public void setMessage(Record message) {
        this.message = message;
    }

    public Author getParentCommentCreator() {
        return parentCommentCreator;
    }

    public void setParentCommentCreator(Author parentCommentCreator) {
        this.parentCommentCreator = parentCommentCreator;
    }
}
