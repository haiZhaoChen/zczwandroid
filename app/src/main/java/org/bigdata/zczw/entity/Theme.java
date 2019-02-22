package org.bigdata.zczw.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by darg on 2017/6/12.
 */

public class Theme implements Serializable {
    /**
     * id : 3
     * title : 777234566
     * outline : 88882
     * deleteflag : 1
     * createid : 1111
     * createtime : 1496821441000
     * updid : 1111
     * updtime : 1496971380000
     * content : <p>9999</p>
     */

    private String id;
    private String title;
    private String outline;
    private String deleteflag;
    private String createid;
    private long createtime;
    private String updid;
    private long updtime;
    private String content;
    private List<TCom> comments;
    private Author author;
    private int viewCount;

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public List<TCom> getComments() {
        return comments;
    }

    public void setComments(List<TCom> comments) {
        this.comments = comments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOutline() {
        return outline;
    }

    public void setOutline(String outline) {
        this.outline = outline;
    }

    public String getDeleteflag() {
        return deleteflag;
    }

    public void setDeleteflag(String deleteflag) {
        this.deleteflag = deleteflag;
    }

    public String getCreateid() {
        return createid;
    }

    public void setCreateid(String createid) {
        this.createid = createid;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public String getUpdid() {
        return updid;
    }

    public void setUpdid(String updid) {
        this.updid = updid;
    }

    public long getUpdtime() {
        return updtime;
    }

    public void setUpdtime(long updtime) {
        this.updtime = updtime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }
}
