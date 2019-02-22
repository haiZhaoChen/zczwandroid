package org.bigdata.zczw.entity;

import java.util.List;

public class HistoryBean {
    private int status;
    private String msg;
    private List<AttendanceHistory> data;

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

    public List<AttendanceHistory> getData() {
        return data;
    }

    public void setData(List<AttendanceHistory> data) {
        this.data = data;
    }
}
