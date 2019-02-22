package org.bigdata.zczw.entity;

import java.util.List;

/**
 * Created by darg on 2016/11/14.
 */
public class TestData {

    /**
     * msg : OK
     * data : {"pageCount":1,"recordList":[{"updid":null,"deleteflag":"1","createtime":1479107598000,"examname":"2","createid":2,"examid":2,"updtime":null,"url":"2"},{"updid":null,"deleteflag":"1","createtime":1479094039000,"examname":"发给","createid":1,"examid":1,"updtime":null,"url":"www.baidu.com"}],"recordCount":2,"pageSize":20,"currentPage":1}
     * status : 200
     */
    private String msg;
    private TestDataEntity data;
    private int status;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(TestDataEntity data) {
        this.data = data;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public TestDataEntity getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }

}
