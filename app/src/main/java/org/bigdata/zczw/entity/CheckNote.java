package org.bigdata.zczw.entity;

/**
 * Created by SVN on 2018/1/2.
 */

public class CheckNote {
    /**
     * id : 1
     * attendanceId : 17
     * remark : 001
     * createUserId : 2067
     * createDate : 1514875382000
     */

    private int id;
    private int attendanceId;
    private String remark;
    private int createUserId;
    private long createDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(int attendanceId) {
        this.attendanceId = attendanceId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(int createUserId) {
        this.createUserId = createUserId;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }
}
