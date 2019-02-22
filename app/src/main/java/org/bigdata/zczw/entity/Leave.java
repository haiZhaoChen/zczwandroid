package org.bigdata.zczw.entity;

import java.io.Serializable;

public class Leave implements Serializable{

    /**
     * id : 4
     * userId : 2067
     * beginDate : 1525795200000
     * days : 1
     * endDate : 1525795200000
     * attendSubType : 2
     * status : 0
     * createDate : 1525853727000
     * updateDate : 1525853727000
     */

    private int id;
    private int userId;
    private long beginDate;
    private int days;
    private long endDate;
    private int attendSubType;
    private int status;
    private long createDate;
    private long updateDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(long beginDate) {
        this.beginDate = beginDate;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public int getAttendSubType() {
        return attendSubType;
    }

    public void setAttendSubType(int attendSubType) {
        this.attendSubType = attendSubType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }


}
