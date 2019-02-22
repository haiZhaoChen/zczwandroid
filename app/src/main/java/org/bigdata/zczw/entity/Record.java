package org.bigdata.zczw.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/5/6 0006.
 */
public class Record implements Serializable{
    /**
     * author : {"id":2069,"name":"宁培培","portrait":"http://zczwstorage.ewonline.org:8094/images/2017/10/26/1508990118920137.jpg","phone":"15097328990","unitsName":"系统运维","jobsName":"其他"}
     * content : 123测试
     * pictures : []
     * video : null
     * commentNum : 0
     * collectNum : 0
     * praiseNum : 0
     * sameUnit : 1
     * longitude : 114.521332
     * latitude : 38.038456
     * location : 中国河北省石家庄市裕华区槐北路39号在河北科技大学附近
     * collect : 0
     * praise : 0
     * publishedTime : 1516174792000
     * messageId : 1516174791967404113
     * viewNum : 0
     * forwardMessage : {"author":{"id":2069,"name":"宁培培","portrait":"http://zczwstorage.ewonline.org:8094/images/2017/10/26/1508990118920137.jpg","phone":null,"unitsName":"系统运维","jobsName":null},"content":"测试一下","pictures":[],"video":null,"commentNum":0,"collectNum":0,"praiseNum":0,"sameUnit":1,"longitude":"114.521382","latitude":"38.038397","location":"中国河北省石家庄市裕华区槐北路39号在河北科技大学附近","collect":0,"praise":0,"publishedTime":1516161235000,"messageId":"1516161235281470492","viewNum":0,"forwardMessage":null,"forwardNum":1,"forwardMessageId":null,"publicScope":10}
     * forwardNum : 0
     * forwardMessageId : 1516161235281470492
     * publicScope : 10
     * "listTopicId": "1",
     * "topicRangeStr": "0-3"
     */
    private Author author;
    private String content;
    private Video video;
    private int commentNum;
    private int collectNum;
    private int praiseNum;
    private int sameUnit;
    private String longitude;
    private String latitude;
    private String location;
    private int collect;
    private int praise;
    private String publishedTime;
    private long messageId;
    private int viewNum;
    private Record forwardMessage;
    private int forwardNum;
    private String forwardMessageId;
    private int publicScope;
    private int priority;
    private List<Pictures> pictures;

    private String listTopicId;
    private String topicRangeStr;

    private boolean isShow;

    private List<MsgTag> tags;
    private List<FileBean> files;

    public Record() {
    }

    public Record(long messageId, Author author, String content, List<Pictures> pictures, Video video, int commentNum, int collectNum, int praiseNum, String longitude, String latitude, String location, String publishedTime, int collect, int praise) {
        this.messageId = messageId;
        this.author = author;
        this.content = content;
        this.pictures = pictures;
        this.video = video;
        this.commentNum = commentNum;
        this.collectNum = collectNum;
        this.praiseNum = praiseNum;
        this.longitude = longitude;
        this.latitude = latitude;
        this.location = location;
        this.publishedTime = publishedTime;
        this.collect = collect;
        this.praise = praise;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public int getCollectNum() {
        return collectNum;
    }

    public void setCollectNum(int collectNum) {
        this.collectNum = collectNum;
    }

    public int getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(int praiseNum) {
        this.praiseNum = praiseNum;
    }

    public int getSameUnit() {
        return sameUnit;
    }

    public void setSameUnit(int sameUnit) {
        this.sameUnit = sameUnit;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCollect() {
        return collect;
    }

    public void setCollect(int collect) {
        this.collect = collect;
    }

    public int getPraise() {
        return praise;
    }

    public void setPraise(int praise) {
        this.praise = praise;
    }

    public String getPublishedTime() {
        return publishedTime;
    }

    public void setPublishedTime(String publishedTime) {
        this.publishedTime = publishedTime;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public int getViewNum() {
        return viewNum;
    }

    public void setViewNum(int viewNum) {
        this.viewNum = viewNum;
    }

    public Record getForwardMessage() {
        return forwardMessage;
    }

    public void setForwardMessage(Record forwardMessage) {
        this.forwardMessage = forwardMessage;
    }

    public int getForwardNum() {
        return forwardNum;
    }

    public void setForwardNum(int forwardNum) {
        this.forwardNum = forwardNum;
    }

    public String getForwardMessageId() {
        return forwardMessageId;
    }

    public void setForwardMessageId(String forwardMessageId) {
        this.forwardMessageId = forwardMessageId;
    }

    public int getPublicScope() {
        return publicScope;
    }

    public void setPublicScope(int publicScope) {
        this.publicScope = publicScope;
    }

    public List<Pictures> getPictures() {
        return pictures;
    }

    public void setPictures(List<Pictures> pictures) {
        this.pictures = pictures;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getListTopicId() {
        return listTopicId;
    }

    public void setListTopicId(String listTopicId) {
        this.listTopicId = listTopicId;
    }

    public String getTopicRangeStr() {
        return topicRangeStr;
    }

    public void setTopicRangeStr(String topicRangeStr) {
        this.topicRangeStr = topicRangeStr;
    }

    public List<MsgTag> getTags() {
        return tags;
    }

    public void setTags(List<MsgTag> tags) {
        this.tags = tags;
    }

    public List<FileBean> getFiles() {
        return files;
    }

    public void setFiles(List<FileBean> files) {
        this.files = files;
    }

    @Override
    public String toString() {
        return "Record{" +
                "messageId=" + messageId +
                ", author=" + author +
                ", content='" + content + '\'' +
                ", pictures=" + pictures +
                ", video='" + video + '\'' +
                ", commentNum=" + commentNum +
                ", collectNum=" + collectNum +
                ", praiseNum=" + praiseNum +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", location='" + location + '\'' +
                ", publishedTime='" + publishedTime + '\'' +
                ", collect=" + collect +
                ", praise=" + praise +
                '}';
    }
}

