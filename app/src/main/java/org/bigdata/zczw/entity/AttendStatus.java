package org.bigdata.zczw.entity;

public class AttendStatus {
    /**
     * isNightShift : false
     * needShowStat : false
     * needXiaoJia : false
     */

    private boolean isNightShift;
    private boolean needShowStat;
    private boolean needXiaoJia;

    public boolean isIsNightShift() {
        return isNightShift;
    }

    public void setIsNightShift(boolean isNightShift) {
        this.isNightShift = isNightShift;
    }

    public boolean isNeedShowStat() {
        return needShowStat;
    }

    public void setNeedShowStat(boolean needShowStat) {
        this.needShowStat = needShowStat;
    }

    public boolean isNeedXiaoJia() {
        return needXiaoJia;
    }

    public void setNeedXiaoJia(boolean needXiaoJia) {
        this.needXiaoJia = needXiaoJia;
    }
}
