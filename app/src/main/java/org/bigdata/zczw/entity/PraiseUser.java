package org.bigdata.zczw.entity;

/**
 * Created by darg on 2016/10/28.
 */
public class PraiseUser {

    /**
     * praiseTime : 1477554911000
     * imagePosition : null
     * praiseId : 315
     * userName : 赵阳
     * userId : 1109
     */
    private String praiseTime;
    private String imagePosition;
    private String praiseId;
    private String userName;
    private int userId;
    private String unitsName;
    private String jobsName;

    public void setPraiseTime(String praiseTime) {
        this.praiseTime = praiseTime;
    }

    public void setImagePosition(String imagePosition) {
        this.imagePosition = imagePosition;
    }

    public void setPraiseId(String praiseId) {
        this.praiseId = praiseId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPraiseTime() {
        return praiseTime;
    }

    public String getImagePosition() {
        return imagePosition;
    }

    public String getPraiseId() {
        return praiseId;
    }

    public String getUserName() {
        return userName;
    }

    public int getUserId() {
        return userId;
    }

    public String getUnitsName() {
        return unitsName;
    }

    public void setUnitsName(String unitsName) {
        this.unitsName = unitsName;
    }

    public String getJobsName() {
        return jobsName;
    }

    public void setJobsName(String jobsName) {
        this.jobsName = jobsName;
    }
}
