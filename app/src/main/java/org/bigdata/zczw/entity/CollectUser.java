package org.bigdata.zczw.entity;

/**
 * Created by darg on 2016/10/28.
 */
public class CollectUser {
    /**
     * collectTime : 1477554965000
     * collectId : 415
     * imagePosition : null
     * userName : 赵阳
     * userId : 1109
     */
    private String collectTime;
    private String collectId;
    private String imagePosition;
    private String userName;
    private int userId;
    private String unitsName;
    private String jobsName;

    public void setCollectTime(String collectTime) {
        this.collectTime = collectTime;
    }

    public void setCollectId(String collectId) {
        this.collectId = collectId;
    }

    public void setImagePosition(String imagePosition) {
        this.imagePosition = imagePosition;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCollectTime() {
        return collectTime;
    }

    public String getCollectId() {
        return collectId;
    }

    public String getImagePosition() {
        return imagePosition;
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
