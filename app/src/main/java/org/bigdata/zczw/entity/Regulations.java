package org.bigdata.zczw.entity;

import java.util.List;

public class Regulations {

    /**
     * status : 200
     * msg : OK
     * data : [{"id":493,"name":"管理手册","code":"","catalogId":138,"url":"http://zczwstorage.ewonline.org:8094/regulations/0管理手册2-1PM/0管理手册2-1PM_管理手册_管理手册.PDF"}]
     */

    private int status;
    private String msg;
    private List<Regulation> data;

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

    public List<Regulation> getData() {
        return data;
    }

    public void setData(List<Regulation> data) {
        this.data = data;
    }

}
