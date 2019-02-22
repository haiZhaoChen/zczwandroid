package org.bigdata.zczw.entity;

/**
 * Created by darg on 2017/4/20.
 */

public class Question {
    /**
     * id : 1
     * title : 如何将微信等平台的文章分享到张承政务
     * url : http://150od17078.51mypc.cn:9003/qa/weixinfenxiang/weixinfenxiang.html
     * deleteflag : 1
     * createtime : 1492400546000
     * createid : null
     * updtime : null
     * updid : null
     */

    private String id;
    private String title;
    private String url;
    private int deleteflag;
    private String createtime;
    private String createid;
    private String updtime;
    private String updid;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getDeleteflag() {
        return deleteflag;
    }

    public void setDeleteflag(int deleteflag) {
        this.deleteflag = deleteflag;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getCreateid() {
        return createid;
    }

    public void setCreateid(String createid) {
        this.createid = createid;
    }

    public String getUpdtime() {
        return updtime;
    }

    public void setUpdtime(String updtime) {
        this.updtime = updtime;
    }

    public String getUpdid() {
        return updid;
    }

    public void setUpdid(String updid) {
        this.updid = updid;
    }
}
