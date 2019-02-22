package org.bigdata.zczw.entity;

/**
 * Created by SVN on 2018/3/12.
 */

public class Topic {
    /**
     * id : 1
     * name : 我
     */

    private int id;
    private String name;
    private boolean newb;

    public Topic() {
    }

    public Topic(int id, String name) {
        this.id = id;
        this.name = name;
    }

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

    public boolean isNewb() {
        return newb;
    }

    public void setNewb(boolean newb) {
        this.newb = newb;
    }
}
