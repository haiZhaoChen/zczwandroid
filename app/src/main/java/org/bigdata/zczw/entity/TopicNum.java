package org.bigdata.zczw.entity;

import java.util.List;

/**
 * Created by SVN on 2018/3/16.
 */

public class TopicNum {

    /**
     * status : 200
     * msg : OK
     * data : [5,3]
     */

    private int status;
    private String msg;
    private List<Integer> data;

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

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }
}
