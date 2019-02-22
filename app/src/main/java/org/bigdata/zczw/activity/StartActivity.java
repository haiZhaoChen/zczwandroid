package org.bigdata.zczw.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.bigdata.zczw.App;
import org.bigdata.zczw.R;
import org.bigdata.zczw.Singleton;
import org.bigdata.zczw.entity.DemoApiJSON;
import org.bigdata.zczw.entity.Friend;
import org.bigdata.zczw.entity.Friends;
import org.bigdata.zczw.entity.GroupInfo;
import org.bigdata.zczw.entity.Groups;
import org.bigdata.zczw.entity.User;
import org.bigdata.zczw.rong.MyConversationBehaviorListener;
import org.bigdata.zczw.ui.WinToast;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.DemoApi;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.SPUtil;
import org.bigdata.zczw.utils.ServerUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;

/*
* 启动页
* */

public class StartActivity extends AppCompatActivity {

    private boolean isChange = true;
    private String userName,passWord;
    private int index;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 66){
                /**
                 * 设置用户信息的提供者，供 RongIM 调用获取用户名称和头像信息。
                 */
                RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
                    @Override
                    public UserInfo getUserInfo(String userId) {
                        return Singleton.getInstance().getUserInfoById(StartActivity.this, userId);//根据 userId 去你的用户系统里查询对应的用户信息返回给融云 SDK。
                    }

                }, true);
            }else if(msg.what == 77){
                RongIM.setGroupInfoProvider(new RongIM.GroupInfoProvider() {
                    @Override
                    public Group getGroupInfo(String s) {
                        Group groupInfo =Singleton.getInstance().getGroupById(s);
                        index++;
                        if (groupInfo != null) {
                            return Singleton.getInstance().getGroupById(s);
                        }else if (index < 2){
                            ServerUtils.getMyCircle(0, group);
                        }else if (groupInfo == null) {
                            RongIM.getInstance().removeConversation(Conversation.ConversationType.GROUP, s, new RongIMClient.ResultCallback<Boolean>() {
                                @Override
                                public void onSuccess(Boolean aBoolean) {
                                    Log.e("1111", "onSuccess: ");
                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {
                                    Log.e("1111", "onError: ");
                                }
                            });
                        }
                        return null;
                    }
                },true);
            }
            RongIM.setConversationBehaviorListener(new MyConversationBehaviorListener());

            if (msg.what == 101) {
                startActivity(new Intent(StartActivity.this, MainActivity.class));
                finish();
            }
        }
    };
    private String userId;
    private String token_sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getSupportActionBar().hide();
        AppManager.getAppManager().addActivity(this);

        String token = SPUtil.getString(this, App.ZW_TOKEN,"");

        if (!TextUtils.isEmpty(token)) {
            App.ZCZW_TOKEN =token;
            String url = DemoApi.HOST +DemoApi. GET_USERINFO +"";
            ServerUtils.getServerDatasGet(url,mUserInfo);
        }else{
            startActivity(new Intent(StartActivity.this, LoginActivity.class));
            finish();

//            SQLiteDatabase db = openOrCreateDatabase("data.db", Context.MODE_PRIVATE, null);
//            Cursor cursor = db.rawQuery("select * from person", null);
//
//            while (cursor.moveToNext()) {
//                //获取第二列的值
//                token_sql = cursor.getString(1);
//            }
//            if (!TextUtils.isEmpty(token_sql)) {
//                Log.e("1111", "token_sql: "+token_sql);
//                App.ZCZW_TOKEN =token_sql;
//                String url = DemoApi.HOST +DemoApi. GET_USERINFO +"";
//                ServerUtils.getServerDatasGet(url,mUserInfo);
//            }else{
//
//            }
        }
    }

    private RequestCallBack<String> mUserInfo = new RequestCallBack<String>() {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            DemoApiJSON demoApiJSON = JsonUtils.jsonToPojo(json, DemoApiJSON.class);
            switch (demoApiJSON.getStatus()) {
                case 200:
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONObject data = jsonObject.getJSONObject("data");
                        String jsondata = data.toString();
                        User user = JsonUtils.jsonToPojo(jsondata, User.class);
                        if (user != null) {
                            SPUtil.put(StartActivity.this, App.USER_ID, user.getUserid()+"");
                            SPUtil.put(StartActivity.this, App.USER_NAME, user.getUsername()+"");
                            SPUtil.put(StartActivity.this, App.USER_TOKEN, user.getToken()+"");
                            SPUtil.put(StartActivity.this, App.POSITION_NAME, user.getPositionName()+"");
                            SPUtil.put(StartActivity.this, App.USER_SEX, user.getUsersex()+"");
                            SPUtil.put(StartActivity.this, App.USER_PHONE, user.getUserphone()+"");
                            SPUtil.put(StartActivity.this, App.CATEGORY_NAME, user.getCategoryName()+"");
                            SPUtil.put(StartActivity.this, App.IMAGE_POSITION, user.getImagePosition()+"");
                            SPUtil.put(StartActivity.this, App.JOBSNAME, user.getJobsName()+"");
                            SPUtil.put(StartActivity.this, App.UNITSNAME, user.getUnitsName()+"");
                            SPUtil.put(StartActivity.this, App.PRILLEVELNAME, user.getPrilLevelName()+"");
                            SPUtil.put(StartActivity.this, App.PRILLEVEL, user.getPrilLevel()+"");
                        } else {
                            WinToast.toast(getApplicationContext(), "用户信息解析错误");
                        }

                        //获取好友列表
                        userId = SPUtil.getString(StartActivity.this, App.USER_ID);

                        ServerUtils.mFirends(userId, 1, "", friends);
                        ServerUtils.getMyCircle(0, group);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 400:
                    WinToast.toast(getApplicationContext(), "用户信息获取失败");
                    SPUtil.remove(StartActivity.this, App.ZW_TOKEN);//清空token
                    SPUtil.remove(StartActivity.this, App.USER_TOKEN);//清空token
                    startActivity(new Intent(StartActivity.this, LoginActivity.class));
                    finish();
                    break;
                case 444:
                    WinToast.toast(getApplicationContext(), "登录过期");
                    SPUtil.remove(StartActivity.this, App.ZW_TOKEN);//清空token
                    SPUtil.remove(StartActivity.this, App.USER_TOKEN);//清空token
                    startActivity(new Intent(StartActivity.this, LoginActivity.class));
                    finish();
                    break;
                case 500:
                    WinToast.toast(getApplicationContext(), "用户信息:系统错误");
                    break;
            }
        }
        @Override
        public void onFailure(HttpException e, String s) {
            WinToast.toast(getApplicationContext(), "用户信息获取失败");
            SPUtil.remove(StartActivity.this, App.ZW_TOKEN);//清空token
            SPUtil.remove(StartActivity.this, App.USER_TOKEN);//清空token
            startActivity(new Intent(StartActivity.this, LoginActivity.class));
            finish();
        }
    };

    RequestCallBack<String> friends = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
//            Log.e("1111", "json: "+json);
            DemoApiJSON demoApiJSON = JsonUtils.jsonToPojo(json, DemoApiJSON.class);
            switch (demoApiJSON.getStatus()) {
                case 200:
                    Friends friends = JsonUtils.jsonToPojo(json, Friends.class);
                    Singleton.getInstance().setFriendlist(friends.getData());
                    ArrayList<User> friendList = (ArrayList<User>) friends.getData();

                    List<Friend> mFriendsList = new ArrayList<>();
                    if (friendList != null) {
                        for (User userInfo : friendList) {
                            Friend friend = new Friend();
                            friend.setNickname(userInfo.getUsername());
                            friend.setGroupname(userInfo.getUnitsName()+ "."+userInfo.getJobsName());
                            friend.setPortrait(userInfo.getImagePosition());
                            friend.setUserId(userInfo.getUserid() + "");
                            friend.setPhone(userInfo.getUserphone());
                            friend.setIsSelected(false);
                            mFriendsList.add(friend);
                        }
                        Singleton.getInstance().setmFriendsList(mFriendsList,StartActivity.this);
                    }

//                    WinToast.toast(getApplicationContext(), "正在加载好友信息");
                    handler.sendEmptyMessage(66);
                    userName = SPUtil.getString(StartActivity.this, App.USER_PHONE);
                    passWord = SPUtil.getString(StartActivity.this, "passWord");
                    if (passWord.equals("izc000")) {
                        dialog();
                    }else {
                        handler.sendEmptyMessageAtTime(101,2000);

                    }
                    break;
                case 400:
                    WinToast.toast(getApplicationContext(), "好友列表加载失败");
                    break;
                case 444:
                    WinToast.toast(getApplicationContext(), "登陆过期");
                    SPUtil.remove(StartActivity.this, App.ZW_TOKEN);//清空token
                    SPUtil.remove(StartActivity.this, App.USER_TOKEN);//清空token
                    startActivity(new Intent(StartActivity.this,LoginActivity.class));
                    finish();
                    break;
                case 500:
                    WinToast.toast(getApplicationContext(), demoApiJSON.getMsg());
                    break;
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
        }
    };

    RequestCallBack<String> group = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
//            Log.e("1111", "group: "+json );
            Groups groups = JsonUtils.jsonToPojo(json, Groups.class);
            switch (groups.getStatus()) {
                case 200://获取成功
                    List<Group> groupList = new ArrayList<>();
                    List<GroupInfo> myGroupInfo = groups.getData();
                    for (int i = 0; i < myGroupInfo.size(); i++) {
                        groupList.add(new Group(myGroupInfo.get(i).getId() + "", myGroupInfo.get(i).getName(), null));
                    }
                    Singleton.getInstance().setGrouplist(groupList);
                    Singleton.getInstance().setMyGroups(myGroupInfo);
//                    RongIM.getInstance().refreshGroupInfoCache();
                    String token = SPUtil.getString(StartActivity.this, App.USER_TOKEN);
                    connect(token);
                    handler.sendEmptyMessage(77);
                    break;
                case 400://客户端错误
                    WinToast.toast(getApplicationContext(), "客户端错误");
                    break;
                case 444://登陆过期
                    WinToast.toast(getApplicationContext(), "登陆过期");
                    break;
                case 500://服务器错误
                    WinToast.toast(getApplicationContext(), "服务器错误");
                    break;
            }
        }
        @Override
        public void onFailure(HttpException e, String s) {

        }
    };

    private void dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
        builder.setMessage("使用默认密码，是否修改？");
        builder.setTitle("安全提示：");
        builder.setPositiveButton("前往修改", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(StartActivity.this, ChangeActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String token = SPUtil.getString(StartActivity.this, App.USER_TOKEN);
                connect(token);
                startActivity(new Intent(StartActivity.this, MainActivity.class));
                finish();
            }
        });
        builder.create().show();
    }

    /**
     * 建立与融云服务器的连接
     *
     * @param token
     */
    private void connect(String token) {
        if (getApplicationInfo().packageName.equals(App.getCurProcessName(getApplicationContext()))) {

            /**
             * IMKit SDK调用第二步,建立与服务器的连接
             */
            RongIM.connect(token, new RongIMClient.ConnectCallback() {
                /**
                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
                 */
                @Override
                public void onTokenIncorrect() {

                    Log.e("StartActivity", "--onTokenIncorrect");
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token
                 */
                @Override
                public void onSuccess(String userid) {
                    Log.e("StartActivity", "--onSuccess:" + userid);
//                    handler.sendEmptyMessage(88);
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.e("StartActivity", "--onError:" + errorCode);
                }
            });
        }
    }
}
