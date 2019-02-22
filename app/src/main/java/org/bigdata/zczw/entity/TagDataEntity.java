package org.bigdata.zczw.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by darg on 2016/11/10.
 */

public class TagDataEntity implements Serializable {
    /**
     * labelId : 0
     * isMy : true
     * userIds : [{"userId":1108},{"userId":1109},{"userId":1110},{"userId":1111}]
     * labelName : 默认标签
     */
    private int labelId;
    private boolean isMy;
    private List<UserIdsEntity> userIds;
    private String labelName;

    public void setLabelId(int labelId) {
        this.labelId = labelId;
    }

    public void setIsMy(boolean isMy) {
        this.isMy = isMy;
    }

    public void setUserIds(List<UserIdsEntity> userIds) {
        this.userIds = userIds;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public int getLabelId() {
        return labelId;
    }

    public boolean isIsMy() {
        return isMy;
    }

    public List<UserIdsEntity> getUserIds() {
        return userIds;
    }

    public String getLabelName() {
        return labelName;
    }

}