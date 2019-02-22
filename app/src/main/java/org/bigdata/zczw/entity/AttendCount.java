package org.bigdata.zczw.entity;

import java.io.Serializable;

public class AttendCount implements Serializable{

    /**
     * leaveCount : 0
     * xiaoJiaCount : 0
     * tiaoXiuCount : 0
     */

    private int leaveCount;
    private int xiaoJiaCount;
    private int tiaoXiuCount;

    public int getLeaveCount() {
        return leaveCount;
    }

    public void setLeaveCount(int leaveCount) {
        this.leaveCount = leaveCount;
    }

    public int getXiaoJiaCount() {
        return xiaoJiaCount;
    }

    public void setXiaoJiaCount(int xiaoJiaCount) {
        this.xiaoJiaCount = xiaoJiaCount;
    }

    public int getTiaoXiuCount() {
        return tiaoXiuCount;
    }

    public void setTiaoXiuCount(int tiaoXiuCount) {
        this.tiaoXiuCount = tiaoXiuCount;
    }
}
