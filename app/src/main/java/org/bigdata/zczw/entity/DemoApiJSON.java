package org.bigdata.zczw.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/3 0003.
 * 封装JSON返回通用对象
 *
 *  Status 200	正确返回
 *          400	客户端请求错误，错误信息在msg中
 *          444	session过期
 *          500	服务器端错误，错误信息在msg中
 */
public class DemoApiJSON implements Serializable {
    private int Status;
    private String msg;
    private Object data;

    public DemoApiJSON() {
    }

    public DemoApiJSON(int status, String msg, Object data) {
        this.Status = status;
        this.msg = msg;
        this.data = data;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        this.Status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
