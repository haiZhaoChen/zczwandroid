package org.bigdata.zczw.entity;

/**
 * Created by darg on 2017/6/20.
 */

public class Flags {


    /**
     * status : 200
     * msg : OK
     * data : {"id":1,"flag":"1","themeId":14,"themeOutline":"（2017.11.25-2017.12.25）","themeTitle":"张承政务-立伟关注"}
     */

    private int status;
    private String msg;
    private ThemeFlag data;

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

    public ThemeFlag getData() {
        return data;
    }

    public void setData(ThemeFlag data) {
        this.data = data;
    }

}
