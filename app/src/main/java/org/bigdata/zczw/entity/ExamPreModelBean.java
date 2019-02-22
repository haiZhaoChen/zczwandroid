package org.bigdata.zczw.entity;

import java.util.ArrayList;

public class ExamPreModelBean {

    private int status;
    private String msg;
    private ArrayList<ExamPreModel> data;

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

    public ArrayList<ExamPreModel> getData() {
        return data;
    }

    public void setData(ArrayList<ExamPreModel> data) {
        this.data = data;
    }
}
