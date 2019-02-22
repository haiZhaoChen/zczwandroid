package org.bigdata.zczw.entity;

/**
 * Created by SVN on 2017/9/30.
 */

public class ComInfo {

    /**
     * status : 200
     * msg : OK
     * data : {"commentsid":1507779875903005700,"commentscontent":"测试","rangestr":"","commentstime":1507779875893,"commentsedmessageid":1506323478391092500,"originalmessageid":1506323478391092500,"parentCommentsId":null,"rootCommentsId":null,"deleteflag":"1","createid":6,"createtime":1507779875893,"updid":null,"updtime":null,"ifnotsee":"0"}
     */

    private int status;
    private String msg;
    private Comment data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Comment getData() {
        return data;
    }

    public void setData(Comment data) {
        this.data = data;
    }

}