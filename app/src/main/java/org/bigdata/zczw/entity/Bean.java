package org.bigdata.zczw.entity;

/**
 * Created by z on 2017/8/21.
 */

public class Bean {


    /**
     * status : 200
     * msg : OK
     * data : http://a3.rabbitpre.com/m/bib6A2z7a
     */

    private int status;
    private String msg;
    private String data;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
