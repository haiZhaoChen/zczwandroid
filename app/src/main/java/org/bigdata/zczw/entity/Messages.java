package org.bigdata.zczw.entity;

import java.util.List;

/**
 * 动态消息和图片和用户
 *
 * @author srrenyu
 */
public class Messages {
    private int code;
    private String message;
    private String type = "";

    public Messages(int code, String message, String type, User user, Dynamicmessage dynamicmessage, List<Image> imageList) {
        this.code = code;
        this.message = message;
        this.type = type;
        this.user = user;
        this.dynamicmessage = dynamicmessage;
        this.imageList = imageList;
    }

    private User user;
    private Dynamicmessage dynamicmessage;
    private List<Image> imageList;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Dynamicmessage getDynamicmessage() {
        return dynamicmessage;
    }

    public void setDynamicmessage(Dynamicmessage dynamicmessage) {
        this.dynamicmessage = dynamicmessage;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }


    public static String PUBLIC_FIGURE = "public_figure";

    public Messages(String type) {
        this.type = type;
    }

    public Messages() {
    }

    @Override
    public String toString() {
        return "Messages{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", type='" + type + '\'' +
                ", user=" + user +
                ", dynamicmessage=" + dynamicmessage +
                ", imageList=" + imageList +
                '}';
    }
}
