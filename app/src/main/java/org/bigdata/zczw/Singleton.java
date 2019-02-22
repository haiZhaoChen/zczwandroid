package org.bigdata.zczw;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import org.bigdata.zczw.entity.Friend;
import org.bigdata.zczw.entity.GroupInfo;
import org.bigdata.zczw.entity.User;
import org.bigdata.zczw.utils.SPUtil;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;

/**
 * Created by darg on 2016/8/15.
 */
public class Singleton {
    private static Singleton instance = new Singleton();
    private Singleton (){}
    public static Singleton getInstance() {
        return instance;
    }


    private List<User> friendlist;
    private List<Friend> select;
    private List<Group> grouplist;
    private List<GroupInfo> myGroups;
    private List<Friend> mFriendsList;
    private HashMap<String,String> userSelect;

    private boolean sendGroupMsg;
    private boolean change;

    public boolean isChange() {
        return change;
    }

    public void setChange(boolean change) {
        this.change = change;
    }

    public boolean isSendGroupMsg() {
        return sendGroupMsg;
    }

    public void setSendGroupMsg(boolean sendGroupMsg) {
        this.sendGroupMsg = sendGroupMsg;
    }

    public List<Friend> getmFriendsList() {
        return mFriendsList;
    }

    public void setmFriendsList(List<Friend> mFriendsList,Context context) {
        this.mFriendsList = mFriendsList;
    }

    private RongIM.LocationProvider.LocationCallback mLastLocationCallback;

    public HashMap<String,String> getUserSelect() {
        return userSelect;
    }

    public void setUserSelect(HashMap<String,String> userSelect) {
        this.userSelect = userSelect;
    }

    public List<User> getFriendlist() {
        return friendlist;
    }

    public void setFriendlist(List<User> friendlist) {
        this.friendlist = friendlist;
    }

    public List<Group> getGrouplist() {
        return grouplist;
    }

    public void setGrouplist(List<Group> grouplist) {
        this.grouplist = grouplist;
    }

    public RongIM.LocationProvider.LocationCallback getmLastLocationCallback() {
        return mLastLocationCallback;
    }

    public void setmLastLocationCallback(RongIM.LocationProvider.LocationCallback mLastLocationCallback) {
        this.mLastLocationCallback = mLastLocationCallback;
    }

    public List<GroupInfo> getMyGroups() {
        return myGroups;
    }

    public void setMyGroups(List<GroupInfo> myGroups) {
        this.myGroups = myGroups;
    }

    /**
     * 根据好友id获取好友信息
     *
     * @param friendid
     * @return
     */
    public User getFriendById(String friendid) {
        User userInfo = null;
        for (User user:friendlist) {
            if (friendid.equals(user.getUserid() + "")){
                userInfo = user;
                return userInfo;
            }
        }
        if (userInfo == null) {
            userInfo = new User();
            userInfo.setUsername("USER"+friendid);
        }
        return userInfo;
    }
    /**
     * 根据group获取好友列表
     * @return
     */
    public List<User> getUserByGroup(String group) {
        List<User> userList = new ArrayList<>();
        for (User user:friendlist) {
            if (group.equals(user.getUnitsName())){
                userList.add(user);
            }
        }
        return userList;
    }

    /**
     * 通过userid 获得username
     *
     * @param userId
     * @return
     */
    public String getUserNameByUserId(String userId) {
        String name = null;
        for (User user:friendlist) {
            if (userId.equals(user.getUserid() + "")){
                name = user.getUsername();
                return name;
            }
        }
        return name;
    }

    public String getGroupNameById(String groupid) {
        String name = null;
        for(Group group:grouplist){
            if(group.getId().equals(groupid)){
                name=group.getName();
                return name;
            }
        }
        return name;
    }

    public GroupInfo getGroupInfoById(String groupid) {
        GroupInfo groupInfo = null;
        for(GroupInfo group:myGroups){
            if(group.getId().equals(groupid)){
                groupInfo=group;
                return groupInfo;
            }
        }
        return groupInfo;
    }

    public GroupInfo getGroupInfoByName(String name) {
        GroupInfo groupInfo = null;
        for(GroupInfo group:myGroups){
            if(group.getName().equals(name)){
                groupInfo=group;
                return groupInfo;
            }
        }
        return groupInfo;
    }
    public int getGroupTypeById(String groupid) {
        int type;
        for(GroupInfo group:myGroups){
            if(group.getId().equals(groupid)){
                type=group.getType();
                return type;
            }
        }
        return 0;
    }
    public String getGroupIdByName(String name) {
        String id = null;
        for(Group group:grouplist){
            if(group.getName().equals(name)){
                id=group.getId();
                return id;
            }
        }
        return id;
    }

    /**
     * 获取用户信息
     *
     * @param userId
     * @return
     */
    public UserInfo getUserInfoById(Context context, String userId) {
        if (userId == null)
            return null;

        if(SPUtil.getString(context, App.USER_ID).equals(userId)){
            String name = SPUtil.getString(context, App.USER_NAME);
            String position = SPUtil.getString(context, App.IMAGE_POSITION);
            return new UserInfo(userId, name, Uri.parse(position));
        }

        User userInfos = null;
        if (friendlist != null && friendlist.size() > 0) {
            for (User u:friendlist) {
                if (userId.equals(u.getUserid() + "")){
                    userInfos = u;
                }
            }
        }

        if (userInfos == null) {
            return  new UserInfo(userId,"USER"+userId,Uri.parse(""));
        }

        if (!TextUtils.isEmpty(userInfos.getImagePosition()) && !userInfos.getImagePosition().contains("null")) {
            return new UserInfo(userInfos.getUserid() + "", userInfos.getUsername()+"-"+userInfos.getUnitsName(), Uri.parse(userInfos.getImagePosition()));
        } else {
            userInfos.setImagePosition("");
            return new UserInfo(userInfos.getUserid() + "", userInfos.getUsername()+"-"+userInfos.getUnitsName(), Uri.parse(userInfos.getImagePosition()));
        }

    }

    public Group getGroupById(String groupid) {
        ArrayList<Group> groupList = (ArrayList<Group>) Singleton.getInstance().getGrouplist();
        Group groupInfo =null;
        for(Group group:groupList){
            if(group.getId().equals(groupid)){
                groupInfo=group;
            }
        }
        return groupInfo;
    }



    public ArrayList<Friend> sort(List<Friend> friends,Context context){
        ArrayList<Friend> friendsArrayList = new ArrayList<Friend>();
        HashMap<String, Friend> userMap = new HashMap<String, Friend>();
        String [] name = new String [friends.size()];
        int i = 0;
        for(Friend friend : friends){
            userMap.put(friend.getNickname()+friend.getUserId(),friend);
            name[i] = friend.getNickname()+friend.getUserId();
            i++;
        }
        String [] nameNew = getSortOfChinese(name);
        Friend group = new Friend("★002", "群组", context.getResources().getResourceName(R.drawable.de_group_default_portrait));
        userMap.put("★", group);
        friendsArrayList.add(group);
//        Friend tag = new Friend("★003", "标签", context.getResources().getResourceName(R.drawable.de_tag_default_portrait));
//        userMap.put("★", tag);
//        friendsArrayList.add(tag);
        for (int j = 0; j < nameNew.length ; j++) {
            friendsArrayList.add(userMap.get(nameNew[j]));
        }
        return  friendsArrayList;
    }

    public String[] getSortOfChinese(String[] a) {
        // Collator 类是用来执行区分语言环境这里使用CHINA
        Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);
        // JDKz自带对数组进行排序。
        Arrays.sort(a, cmp);
        return a;
    }

    public List<Friend> getSelect() {
        return select;
    }

    public void setSelect(List<Friend> select) {
        this.select = select;
    }
}
