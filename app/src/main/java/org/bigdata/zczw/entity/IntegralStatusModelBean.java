package org.bigdata.zczw.entity;

public class IntegralStatusModelBean {
    private int status;
    private String msg;
    private IntegralStatusModel data;

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

    public IntegralStatusModel getData() {
        return data;
    }

    public void setData(IntegralStatusModel data) {
        this.data = data;
    }
}
