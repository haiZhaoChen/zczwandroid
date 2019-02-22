package org.bigdata.zczw.entity;

public class Catalog {

    /**
     * id : 100
     * parentId : 0
     * name : 管理手册2-1PM
     */

    private int id;
    private int parentId;
    private String name;
    private boolean isShow;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
}
