package org.bigdata.zczw.entity;

import java.util.List;

/**
 * Created by SVN on 2018/1/2.
 */

public class CheckNoteBean {

    /**
     * status : 200
     * msg : OK
     * data : [{"id":1,"attendanceId":17,"remark":"001","createUserId":2067,"createDate":1514875382000}]
     */

    private int status;
    private String msg;
    private List<CheckNote> data;

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

    public List<CheckNote> getData() {
        return data;
    }

    public void setData(List<CheckNote> data) {
        this.data = data;
    }


}
