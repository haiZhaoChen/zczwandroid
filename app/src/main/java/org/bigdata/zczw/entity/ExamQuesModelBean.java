package org.bigdata.zczw.entity;

import java.util.ArrayList;

public class ExamQuesModelBean {

    private int status;
    private String msg;
    private ArrayList<ExamQuesModel> data;

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

    public ArrayList<ExamQuesModel> getData() {
        return data;
    }

    public void setData(ArrayList<ExamQuesModel> data) {
        this.data = data;
    }
}
