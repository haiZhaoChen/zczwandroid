package org.bigdata.zczw.entity;

import java.util.List;

/**
 * Created by SVN on 2018/1/2.
 */

public class Check {

    /**
     * user : {"userid":1110,"username":null,"usersex":null,"userphone":null,"token":null,"imagePosition":null,"positionName":null,"categoryName":null,"jobsName":"副处长","unitsName":"系统运维","prilLevelName":null,"prilLevel":0,"deleteflag":null}
     * todayAttend : null
     * attendTypeCountList : [{"attendType":1,"count":0},{"attendType":2,"count":0},{"attendType":3,"count":0},{"attendType":4,"count":0},{"attendType":5,"count":0},{"attendType":6,"count":0}]
     */

    private Author user;
    private Attendance todayAttend;
    private List<AttendTypeCount> attendTypeCountList;

    public Author getUser() {
        return user;
    }

    public void setUser(Author user) {
        this.user = user;
    }

    public Attendance getTodayAttend() {
        return todayAttend;
    }

    public void setTodayAttend(Attendance todayAttend) {
        this.todayAttend = todayAttend;
    }

    public List<AttendTypeCount> getAttendTypeCountList() {
        return attendTypeCountList;
    }

    public void setAttendTypeCountList(List<AttendTypeCount> attendTypeCountList) {
        this.attendTypeCountList = attendTypeCountList;
    }
}
