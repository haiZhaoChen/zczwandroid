package org.bigdata.zczw.entity;

public class CheckStatus {

    /**
     * status : 200
     * msg : OK
     * data : {"isNightShift":false,"needShowStat":false,"needXiaoJia":false}
     */

    private int status;
    private String msg;
    private AttendStatus data;

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

    public AttendStatus getData() {
        return data;
    }

    public void setData(AttendStatus data) {
        this.data = data;
    }

}
