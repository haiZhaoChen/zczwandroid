package org.bigdata.zczw.entity;

import java.util.List;

/**
 * Created by SVN on 2017/10/17.
 */

public class Pai {
    /**
     * id : 4
     * content : 测试
     * images : null
     * video : {"id":null,"thumbnail":"http://zczwstorage.ewonline.org:8094/images/2017/10/19/1508379185105988.png","length":7659,"size":8597657,"path":"http://zczwstorage.ewonline.org:8094/videos/2017/10/19/1508379179229202.mp4"}
     * category : 1
     * tag : 1
     * createUserId : 2067
     * createDate : 1508379179000
     * author : {"id":2067,"name":"张涛","portrait":"http://zczwstorage.ewonline.org:8094/images/2017/09/15/1505445058650387.jpg","phone":null,"unitsName":null}
     */

    private int id;
    private String content;
    private List<String> images;
    private Video video;
    private int category;
    private int tag;
    private int createUserId;
    private long createDate;
    private Author author;
    private int workType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(int createUserId) {
        this.createUserId = createUserId;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public int getWorkType() {
        return workType;
    }

    public void setWorkType(int workType) {
        this.workType = workType;
    }
}
