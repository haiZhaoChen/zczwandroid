package org.bigdata.zczw.entity;

import java.util.List;

/**
 * Created by SVN on 2018/1/2.
 */

public class CheckBean {
    private int status;
    private String msg;
    private List<Check> data;

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

    public List<Check> getData() {
        return data;
    }

    public void setData(List<Check> data) {
        this.data = data;
    }
}
