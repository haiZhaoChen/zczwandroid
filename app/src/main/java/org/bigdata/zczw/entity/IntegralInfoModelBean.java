package org.bigdata.zczw.entity;

import java.util.ArrayList;

public class IntegralInfoModelBean {
    private int status;
    private String msg;
    private ArrayList<IntegralInfoModel> data;

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

    public ArrayList<IntegralInfoModel> getData() {
        return data;
    }

    public void setData(ArrayList<IntegralInfoModel> data) {
        this.data = data;
    }
}
