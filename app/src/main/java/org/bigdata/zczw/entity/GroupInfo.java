package org.bigdata.zczw.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/5/16 0016.
 */
public class GroupInfo implements Serializable{
    private String id;
    private String name;
    private int totle;
    private List<User> users;
    private int type;

    public GroupInfo() {
    }

    public GroupInfo(String id, String name, int totle, List<User> users) {
        this.id = id;
        this.name = name;
        this.totle = totle;
        this.users = users;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotle() {
        return totle;
    }

    public void setTotle(int totle) {
        this.totle = totle;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
