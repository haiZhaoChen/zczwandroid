package org.bigdata.zczw.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by SVN on 2017/9/27.
 */

public class DetailList implements Serializable {

    private int status;
    private String msg;
    private List<List<Record>> data ;

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

    public List<List<Record>> getData() {
        return data;
    }

    public void setData(List<List<Record>> data) {
        this.data = data;
    }


}
