package org.bigdata.zczw.entity;


/**
 * Created by darg on 2017/4/20.
 */

public class Zan {



    /**
     * id : 391
     * ifnotsee : 1
     * createtime : 1490867967000
     * user : {"id":1109,"name":"赵阳","portrait":"http://150od17078.51mypc.cn:9002/images/2017/04/01/1491030833837262.jpg","phone":null}
     * message : {"content":"测试","commentNum":0,"collectNum":0,"praiseNum":1,"longitude":null,"latitude":null,"location":"河北省石家庄市裕华区方村镇南岭小区(西区)南岭小区西区","collect":0,"praise":1,"publishedTime":null,"messageId":"1490860742694614066"}
     * author : {"id":1109,"name":"赵阳","portrait":"http://150od17078.51mypc.cn:9002/images/2017/04/01/1491030833837262.jpg","phone":null}
     * pictures : []
     * video : null
     */

    private String id;
    private String ifnotsee;
    private String createtime;
    private String commentsContent;
    private String rangeStr;
    private Author user;
    private Record message;
    private Author author;
    private Zan parentComment;

    public Zan getParentComment() {
        return parentComment;
    }

    public void setParentComment(Zan parentComment) {
        this.parentComment = parentComment;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIfnotsee() {
        return ifnotsee;
    }

    public void setIfnotsee(String ifnotsee) {
        this.ifnotsee = ifnotsee;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public Author getUser() {
        return user;
    }

    public void setUser(Author user) {
        this.user = user;
    }

    public Record getMessage() {
        return message;
    }

    public void setMessage(Record message) {
        this.message = message;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
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
}
