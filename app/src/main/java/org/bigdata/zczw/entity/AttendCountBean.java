package org.bigdata.zczw.entity;

public class AttendCountBean {

    /**
     * status : 200
     * msg : OK
     * data : {"leaveCount":0,"xiaoJiaCount":0,"tiaoXiuCount":0}
     */

    private int status;
    private String msg;
    private AttendCount data;

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

    public AttendCount getData() {
        return data;
    }

    public void setData(AttendCount data) {
        this.data = data;
    }

}
