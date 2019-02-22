package org.bigdata.zczw.entity;

/**
 * Created by darg on 2017/6/13.
 */

public class TCom {

    /**
     * c_id : 1
     * c_content : 123
     * c_createtime : 1497261077000
     * c_updtime : 1497261077000
     * c_userId : 1108
     * c_userName : 任忠
     * c_userPhone : 18032098865
     * c_portrait : http://150od17078.51mypc.cn:9002/images/2017/05/24/1495636364245664.jpg
     */

    private String c_id;
    private String c_content;
    private long c_createtime;
    private long c_updtime;
    private String c_userId;
    private String c_userName;
    private String c_userPhone;
    private String c_portrait;
    private String unitsName;

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getC_content() {
        return c_content;
    }

    public void setC_content(String c_content) {
        this.c_content = c_content;
    }

    public long getC_createtime() {
        return c_createtime;
    }

    public void setC_createtime(long c_createtime) {
        this.c_createtime = c_createtime;
    }

    public long getC_updtime() {
        return c_updtime;
    }

    public void setC_updtime(long c_updtime) {
        this.c_updtime = c_updtime;
    }

    public String getC_userId() {
        return c_userId;
    }

    public void setC_userId(String c_userId) {
        this.c_userId = c_userId;
    }

    public String getC_userName() {
        return c_userName;
    }

    public void setC_userName(String c_userName) {
        this.c_userName = c_userName;
    }

    public String getC_userPhone() {
        return c_userPhone;
    }

    public void setC_userPhone(String c_userPhone) {
        this.c_userPhone = c_userPhone;
    }

    public String getC_portrait() {
        return c_portrait;
    }

    public void setC_portrait(String c_portrait) {
        this.c_portrait = c_portrait;
    }

    public String getUnitsName() {
        return unitsName;
    }

    public void setUnitsName(String unitsName) {
        this.unitsName = unitsName;
    }
}
