package org.bigdata.zczw.entity;

import android.text.TextUtils;

import org.bigdata.zczw.utils.DePinyinHelper;

import java.io.Serializable;


/**
 * Created by Bob on 2015/3/24.
 */
public class Friend implements Serializable, IFilterModel {

    private String userId;
    private String nickname;
    private String nicknamePinyin;
    private String portrait;
    private String groupname;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private String phone;
    private char searchKey;

    private boolean isSelected = false;

    private boolean isCall = false;

    private boolean isAdd = false;
    private boolean isSub = false;
    private boolean isDel = false;

    public Friend(){

    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public Friend(String userId, String nickname, String portrait) {
        this.userId = userId;
        this.nickname = nickname;
        this.portrait = portrait;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }


    private final void createSeachKey(String nickname) {

        if (TextUtils.isEmpty(nickname)) {
            return;
        }

        nicknamePinyin = DePinyinHelper.getInstance().getPinyins(nickname, "");

        if (nicknamePinyin != null && nicknamePinyin.length() > 0) {
            char key = nicknamePinyin.charAt(0);
            if (key >= 'A' && key <= 'Z') {

            } else if (key >= 'a' && key <= 'z') {
                key -= 32;
            } else if (key == '★' ) {
                key = '★';
            }else {
                key = '#';
            }
            searchKey = key;
        } else {
            searchKey = '#';
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
        createSeachKey(nickname);
    }

    public String getNicknamePinyin() {
        return nicknamePinyin;
    }

    public void setNicknamePinyin(String nicknamePinyin) {
        this.nicknamePinyin = nicknamePinyin;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public char getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(char searchKey) {
        this.searchKey = searchKey;
    }
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public String getFilterKey() {
        return getNickname() + getNicknamePinyin();
    }

    public boolean isCall() {
        return isCall;
    }

    public void setCall(boolean isCall) {
        this.isCall = isCall;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean isAdd) {
        this.isAdd = isAdd;
    }

    public boolean isSub() {
        return isSub;
    }

    public void setSub(boolean isSub) {
        this.isSub = isSub;
    }

    public boolean isDel() {
        return isDel;
    }

    public void setDel(boolean isDel) {
        this.isDel = isDel;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "userId='" + userId + '\'' +
                ", nickname='" + nickname + '\'' +
                ", nicknamePinyin='" + nicknamePinyin + '\'' +
                ", portrait='" + portrait + '\'' +
                ", searchKey=" + searchKey +
                ", isSelected=" + isSelected +
                ", isCall=" + isCall +
                ", isAdd=" + isAdd +
                ", isSub=" + isSub +
                ", isDel=" + isDel +
                '}';
    }
}
