package org.bigdata.zczw.entity;

import java.util.List;

/**
 * Created by darg on 2017/4/20.
 */

public class Questions {

    /**
     * status : 200
     * msg : OK
     * data : [{"id":1,"title":"如何将微信等平台的文章分享到张承政务","url":"http://150od17078.51mypc.cn:9003/qa/weixinfenxiang/weixinfenxiang.html","deleteflag":1,"createtime":1492400546000,"createid":null,"updtime":null,"updid":null}]
     */

    private int status;
    private String msg;
    private List<Question> data;

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

    public List<Question> getData() {
        return data;
    }

    public void setData(List<Question> data) {
        this.data = data;
    }

}
