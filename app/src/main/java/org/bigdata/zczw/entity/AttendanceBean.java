package org.bigdata.zczw.entity;

/**
 * Created by SVN on 2017/12/29.
 */

public class AttendanceBean {
    /**
     * status : 200
     * msg : OK
     * data : {"id":1,"userId":2067,"attendType":1,"attendDate":1514476800000,"attendSubType":0,"otherTypeName":"","latitude":"38.038427","longitude":"114.521313","locationName":"河北省石家庄市裕华区槐北路39号在河北科技大学附近","createDate":1514527585000}
     */

    private int status;
    private String msg;
    private Attendance data;

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

    public Attendance getData() {
        return data;
    }

    public void setData(Attendance data) {
        this.data = data;
    }


}
