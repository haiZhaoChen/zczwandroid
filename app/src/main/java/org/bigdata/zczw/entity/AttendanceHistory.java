package org.bigdata.zczw.entity;

public class AttendanceHistory {


    /**
     * id : 6
     * userId : 2067
     * attendDate : 1527004800000
     * fromAttendType : 1
     * fromAttendSubType : 0
     * fromOtherTypeName : null
     * toAttendType : 2
     * toAttendSubType : 0
     * toOtherTypeName : null
     * latitude : 38.038442
     * longitude : 114.52145
     * locationName : 河北省石家庄市裕华区槐北路41号
     * createDate : 1527067425000
     */

    private int id;
    private int userId;
    private long attendDate;
    private int fromAttendType;
    private int fromAttendSubType;
    private String fromOtherTypeName;
    private int toAttendType;
    private int toAttendSubType;
    private String toOtherTypeName;
    private String latitude;
    private String longitude;
    private String locationName;
    private long createDate;
    private long date;

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

    public long getAttendDate() {
        return attendDate;
    }

    public void setAttendDate(long attendDate) {
        this.attendDate = attendDate;
    }

    public int getFromAttendType() {
        return fromAttendType;
    }

    public void setFromAttendType(int fromAttendType) {
        this.fromAttendType = fromAttendType;
    }

    public int getFromAttendSubType() {
        return fromAttendSubType;
    }

    public void setFromAttendSubType(int fromAttendSubType) {
        this.fromAttendSubType = fromAttendSubType;
    }

    public String getFromOtherTypeName() {
        return fromOtherTypeName;
    }

    public void setFromOtherTypeName(String fromOtherTypeName) {
        this.fromOtherTypeName = fromOtherTypeName;
    }

    public int getToAttendType() {
        return toAttendType;
    }

    public void setToAttendType(int toAttendType) {
        this.toAttendType = toAttendType;
    }

    public int getToAttendSubType() {
        return toAttendSubType;
    }

    public void setToAttendSubType(int toAttendSubType) {
        this.toAttendSubType = toAttendSubType;
    }

    public String getToOtherTypeName() {
        return toOtherTypeName;
    }

    public void setToOtherTypeName(String toOtherTypeName) {
        this.toOtherTypeName = toOtherTypeName;
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

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
