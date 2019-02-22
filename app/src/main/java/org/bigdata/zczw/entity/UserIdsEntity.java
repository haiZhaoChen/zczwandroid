package org.bigdata.zczw.entity;

import java.io.Serializable;

/**
 * Created by darg on 2016/11/10.
 */
public class UserIdsEntity implements Serializable {
    /**
     * userId : 1108
     */
    private int userId;

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }
}