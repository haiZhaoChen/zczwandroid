package org.bigdata.zczw.entity;

import java.util.List;

/**
 * Created by SVN on 2017/12/29.
 */

public class Attend {
    private List<AttendTypeCount> left;
    private List<Attendance> right;

    public List<AttendTypeCount> getLeft() {
        return left;
    }

    public void setLeft(List<AttendTypeCount> left) {
        this.left = left;
    }

    public List<Attendance> getRight() {
        return right;
    }

    public void setRight(List<Attendance> right) {
        this.right = right;
    }
}
