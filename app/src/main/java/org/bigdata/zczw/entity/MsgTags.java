package org.bigdata.zczw.entity;

import java.util.List;

public class MsgTags {


    /**
     * status : 200
     * msg : OK
     * data : [{"id":1,"name":"诚信建设"},{"id":2,"name":"精细化管理"},{"id":3,"name":"志愿服务"},{"id":4,"name":"治理偷逃费"},{"id":5,"name":"幸福站区 温馨家园"},{"id":6,"name":"爱高速 做贡献"},{"id":7,"name":"降本增效"},{"id":8,"name":"学习宣传十九大"},{"id":9,"name":"他山之石"},{"id":10,"name":"职工风采"},{"id":11,"name":"大学习"},{"id":12,"name":"自修自养"},{"id":13,"name":"安全提醒"},{"id":14,"name":"安全保畅通"},{"id":15,"name":"双创（全员创新 全面创新）"},{"id":16,"name":"党史学习"},{"id":17,"name":"每周倡廉"},{"id":100001,"name":"其他"}]
     */

    private int status;
    private String msg;
    private List<MsgTag> data;

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

    public List<MsgTag> getData() {
        return data;
    }

    public void setData(List<MsgTag> data) {
        this.data = data;
    }

}
