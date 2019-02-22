package org.bigdata.zczw.entity;

/**
 * Created by darg on 2017/4/27.
 */

public class NumBean {

    /**
     * status : 200
     * msg : OK
     * data : {"pNum":0,"cNum":0,"caNum":0,"flag":false}
     */

    private int status;
    private String msg;
    private int data;

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

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

}
