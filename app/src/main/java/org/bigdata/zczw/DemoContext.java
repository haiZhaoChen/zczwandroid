package org.bigdata.zczw;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.bigdata.zczw.entity.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;

/**
 * Created by Bob on 2015/1/30.
 */
public class DemoContext {

    public static boolean TEST = false;//测试功能
    private static DemoContext mDemoContext;
    public Context mContext;
    private HashMap<String, Group> groupMap;

    private List<Group> groupList;

    private ArrayList<UserInfo> mUserInfos;
    private List<User> mFriendInfos;
    private SharedPreferences mPreferences;
    private RongIM.LocationProvider.LocationCallback mLastLocationCallback;


    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private User loginUser;

    private boolean isGroup;

    private HttpUtils mhttpUtils;


    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.ic_empty)
            .showImageForEmptyUri(R.drawable.ic_empty)
            .showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
            .cacheOnDisc(true).considerExifParams(true)
            .displayer(new RoundedBitmapDisplayer(20)).build();


    public void setIsGroup (boolean isGroup){
        this.isGroup = isGroup;
    }
    public boolean getIsGroup (){
        return isGroup;
    }

    public void setLoginUser(User user) {
        this.loginUser = user;
    }

    public User getLoginUser() {
        return this.loginUser;
    }



    public HttpUtils getHttpUtils() {
        return mhttpUtils;
    }



    public static DemoContext getInstance() {

        return mDemoContext;
    }

    public DisplayImageOptions getDisplayImageOptions() {
        return options;
    }

    public SimpleDateFormat getDateFormatter() {
        return dateFormatter;
    }

    private DemoContext() {
    }

    private DemoContext(Context context) {
        mContext = context;
        mDemoContext = this;
        // http初始化 用于登录、注册使用
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        RongIM.setLocationProvider(new LocationProvider());
        mhttpUtils = new HttpUtils();

        mUserInfos = new ArrayList<>();

    }

    public void addUserinfo(Collection<UserInfo> userInfos) {
        this.mUserInfos.addAll(userInfos);
    }

    public void addUserinfo(UserInfo userInfo) {
        this.mUserInfos.add(userInfo);
    }

    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
    }


    public static void init(Context context) {
        mDemoContext = new DemoContext(context);
    }

    public SharedPreferences getSharedPreferences() {
        return mPreferences;
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.mPreferences = sharedPreferences;
    }

    public void setGroupMap(HashMap<String, Group> groupMap) {
        this.groupMap = groupMap;
    }

    public HashMap<String, Group> getGroupMap() {
        return groupMap;
    }

    public ArrayList<UserInfo> getUserInfos() {
        return mUserInfos;
    }

    public void setUserInfos(ArrayList<UserInfo> userInfos) {
        mUserInfos = userInfos;
    }

    /**
     * 临时存放用户数据
     *
     * @param result
     */
    public void setFriends(List<User> result) {

        this.mFriendInfos = result;
    }


    /**
     * 获取用户信息列表
     *
     * @param userIds
     * @return
     */
    public List<UserInfo> getUserInfoByIds(String[] userIds) {

        List<UserInfo> userInfoList = new ArrayList<UserInfo>();

        if (userIds != null && userIds.length > 0) {
            for (String userId : userIds) {
                for (UserInfo userInfo : mUserInfos) {
                    Log.e("",
                            "0409-------getUserInfoByIds-"
                                    + userInfo.getUserId() + "---userid;"
                                    + userId);
                    if (userId.equals(userInfo.getUserId())) {
                        Log.e("",
                                "0409-------getUserInfoByIds-"
                                        + userInfo.getName());
                        userInfoList.add(userInfo);
                    }
                }
            }
        }
        return userInfoList;
    }


    public RongIM.LocationProvider.LocationCallback getLastLocationCallback() {
        return mLastLocationCallback;
    }

    public void setLastLocationCallback(RongIM.LocationProvider.LocationCallback lastLocationCallback) {
        this.mLastLocationCallback = lastLocationCallback;
    }


    class LocationProvider implements RongIM.LocationProvider {

        /**
         * 位置信息提供者:LocationProvider 的回调方法，打开第三方地图页面。
         *
         * @param context  上下文
         * @param callback 回调
         */
        @Override
        public void onStartLocation(Context context,
                                    RongIM.LocationProvider.LocationCallback callback) {
            /**
             * demo 代码 开发者需替换成自己的代码。
             */
//            DemoContext.getInstance().setLastLocationCallback(callback);
//            Intent intent = new Intent(context, SOSOLocationActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);// SOSO地图
        }
    }



}
