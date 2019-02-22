package org.bigdata.zczw.entity;

import java.io.Serializable;

public class MsgTag implements Serializable{
    /**
     * id : 1
     * name : 诚信建设
     */

    private int id;
    private String name;
    private boolean isCheck;

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

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
