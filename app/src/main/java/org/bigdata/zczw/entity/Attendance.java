package org.bigdata.zczw.entity;

import java.io.Serializable;

/**
 * Created by SVN on 2017/12/29.
 */

public class Attendance implements Serializable{

    /**
     * id : 26669
     * userId : 2067
     * attendType : 1
     * attendDate : 1525363200000
     * attendSubType : 0
     * otherTypeName : null
     * createDate : 1525337649000
     * updateDate : 1525337649000
     * leave : null
     * tiaoXiu : null
     */

    private long id;
    private long userId;
    private int attendType;
    private long attendDate;
    private int attendSubType;
    private String otherTypeName;
    private long createDate;
    private long updateDate;
    private Leave leave;
    private TiaoXiu tiaoXiu;

    private String latitude;
    private String longitude;
    private String locationName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getAttendType() {
        return attendType;
    }

    public void setAttendType(int attendType) {
        this.attendType = attendType;
    }

    public long getAttendDate() {
        return attendDate;
    }

    public void setAttendDate(long attendDate) {
        this.attendDate = attendDate;
    }

    public int getAttendSubType() {
        return attendSubType;
    }

    public void setAttendSubType(int attendSubType) {
        this.attendSubType = attendSubType;
    }

    public String getOtherTypeName() {
        return otherTypeName;
    }

    public void setOtherTypeName(String otherTypeName) {
        this.otherTypeName = otherTypeName;
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

    public Leave getLeave() {
        return leave;
    }

    public void setLeave(Leave leave) {
        this.leave = leave;
    }

    public TiaoXiu getTiaoXiu() {
        return tiaoXiu;
    }

    public void setTiaoXiu(TiaoXiu tiaoXiu) {
        this.tiaoXiu = tiaoXiu;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
