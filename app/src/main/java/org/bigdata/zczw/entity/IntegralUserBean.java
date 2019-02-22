package org.bigdata.zczw.entity;

public class IntegralUserBean {

    private int status;
    private String msg;
    private IntegralMapModel data;

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

    public IntegralMapModel getData() {
        return data;
    }

    public void setData(IntegralMapModel data) {
        this.data = data;
    }
}
