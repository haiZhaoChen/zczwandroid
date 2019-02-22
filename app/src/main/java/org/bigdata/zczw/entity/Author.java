package org.bigdata.zczw.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/6 0006.
 */
public class Author implements Serializable {

    /**
     * id : 2067
     * name : 张涛
     * portrait : http://zczwstorage.ewonline.org:8094/images/2017/09/15/1505445058650387.jpg
     * phone : null
     * unitsName : null
     */

    private int id;
    private String name;
    private String portrait;
    private String phone;
    private String unitsName;
    private String jobsName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
