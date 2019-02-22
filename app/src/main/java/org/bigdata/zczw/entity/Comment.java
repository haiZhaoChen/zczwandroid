package org.bigdata.zczw.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/5/6 0006.
 */
public class Comment implements Serializable{


    /**
     * rootCommentsId : null
     * userId : 5
     * userName : 杨立伟
     * commentsId : 1506408129836194404
     * commentsContent : 1
     * rangeStr : 12/18/36
     * commentsedMessageId : 1477313481574687893
     * originalMessageId : 1477313481574687893
     * commentsTime : 1506408130000
     * imagePosition : http://zczwstorage.ewonline.org:8094/images/2017/04/02/1491097577781228.jpg
     * listComentReplyInfo : [{"userId":5,"userName":"杨立伟","commentsContent":"huifu1","commentsedMessageId":"1477313481574687893","originalMessageId":"1477313481574687893","commentsTime":1506645708000,"imagePosition":"http://zczwstorage.ewonline.org:8094/images/2017/04/02/1491097577781228.jpg"},{"userId":5,"userName":"杨立伟","commentsContent":"huifu2","commentsedMessageId":"1477313481574687893","originalMessageId":"1477313481574687893","commentsTime":1506646193000,"imagePosition":"http://zczwstorage.ewonline.org:8094/images/2017/04/02/1491097577781228.jpg"},{"userId":5,"userName":"杨立伟","commentsContent":"huifu3","commentsedMessageId":"1477313481574687893","originalMessageId":"1477313481574687893","commentsTime":1506646197000,"imagePosition":"http://zczwstorage.ewonline.org:8094/images/2017/04/02/1491097577781228.jpg"},{"userId":5,"userName":"杨立伟","commentsContent":"huifu4","commentsedMessageId":"1477313481574687893","originalMessageId":"1477313481574687893","commentsTime":1506646203000,"imagePosition":"http://zczwstorage.ewonline.org:8094/images/2017/04/02/1491097577781228.jpg"},{"userId":5,"userName":"杨立伟","commentsContent":"huifu5","commentsedMessageId":"1477313481574687893","originalMessageId":"1477313481574687893","commentsTime":1506646208000,"imagePosition":"http://zczwstorage.ewonline.org:8094/images/2017/04/02/1491097577781228.jpg"}]
     */

    private int userId;
    private String userName;
    private String commentsId;
    private String commentsContent;
    private String rangeStr;
    private String commentsedMessageId;
    private String originalMessageId;
    private String commentsTime;
    private String imagePosition;

    private String parentCommentsId;
    private String rootCommentsId;
    private String buserName;
    private String unitsName;
    private String jobsName;


    private List<Comment> listComentReplyInfo;

    public Comment() {
    }

    public Comment(String userName, String commentsTime, String imagePosition, String commentsContent,String rangeStr) {
        this.userName = userName;
        this.commentsTime = commentsTime;
        this.imagePosition = imagePosition;
        this.commentsContent = commentsContent;
        this.rangeStr = rangeStr;
    }

    public String getRootCommentsId() {
        return rootCommentsId;
    }

    public void setRootCommentsId(String rootCommentsId) {
        this.rootCommentsId = rootCommentsId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCommentsId() {
        return commentsId;
    }

    public void setCommentsId(String commentsId) {
        this.commentsId = commentsId;
    }

    public String getCommentsContent() {
        return commentsContent;
    }

    public void setCommentsContent(String commentsContent) {
        this.commentsContent = commentsContent;
    }

    public String getRangeStr() {
        return rangeStr;
    }

    public void setRangeStr(String rangeStr) {
        this.rangeStr = rangeStr;
    }

    public String getCommentsedMessageId() {
        return commentsedMessageId;
    }

    public void setCommentsedMessageId(String commentsedMessageId) {
        this.commentsedMessageId = commentsedMessageId;
    }

    public String getOriginalMessageId() {
        return originalMessageId;
    }

    public void setOriginalMessageId(String originalMessageId) {
        this.originalMessageId = originalMessageId;
    }

    public String getCommentsTime() {
        return commentsTime;
    }

    public void setCommentsTime(String commentsTime) {
        this.commentsTime = commentsTime;
    }

    public String getImagePosition() {
        return imagePosition;
    }

    public void setImagePosition(String imagePosition) {
        this.imagePosition = imagePosition;
    }

    public List<Comment> getListComentReplyInfo() {
        return listComentReplyInfo;
    }

    public void setListComentReplyInfo(List<Comment> listComentReplyInfo) {
        this.listComentReplyInfo = listComentReplyInfo;
    }

    public String getParentCommentsId() {
        return parentCommentsId;
    }

    public void setParentCommentsId(String parentCommentsId) {
        this.parentCommentsId = parentCommentsId;
    }

    public String getBuserName() {
        return buserName;
    }

    public void setBuserName(String buserName) {
        this.buserName = buserName;
    }

    public String getUnitsName() {
        return unitsName;
    }

    public void setUnitsName(String unitsName) {
        this.unitsName = unitsName;
    }

    public String getJobsName() {
        return jobsName;
    }

    public void setJobsName(String jobsName) {
        this.jobsName = jobsName;
    }
}
