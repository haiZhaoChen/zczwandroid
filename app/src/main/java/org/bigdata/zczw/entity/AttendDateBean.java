package org.bigdata.zczw.entity;

/**
 * Created by SVN on 2017/12/29.
 */

public class AttendDateBean {

    private int status;
    private String msg;
    private Attend data;

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

    public Attend getData() {
        return data;
    }

    public void setData(Attend data) {
        this.data = data;
    }

}
