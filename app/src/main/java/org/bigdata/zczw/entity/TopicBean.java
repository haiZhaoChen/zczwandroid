package org.bigdata.zczw.entity;

import java.util.List;

/**
 * Created by SVN on 2018/3/12.
 */

public class TopicBean {

    /**
     * status : 200
     * msg : OK
     * data : [{"id":1,"name":"我"}]
     */

    private int status;
    private String msg;
    private List<Topic> data;

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

    public List<Topic> getData() {
        return data;
    }

    public void setData(List<Topic> data) {
        this.data = data;
    }

}
