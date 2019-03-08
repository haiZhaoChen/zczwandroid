package org.bigdata.zczw.activity;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

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
import org.bigdata.zczw.ui.LoadingDialog;
import org.bigdata.zczw.ui.WinToast;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.DateUtils;
import org.bigdata.zczw.utils.DemoApi;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.SPUtil;
import org.bigdata.zczw.utils.ServerUtils;
import org.bigdata.zczw.utils.Utils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;
import okhttp3.Call;

/*
* 张承政务 登录页
* */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout loginRoot;
    private RelativeLayout login;
    private Button btnLogin;
    private EditText nameEdit,passEdit;
    private TextView forget;
    private LoadingDialog mDialog;

    private String userName,passWord,key;
    private boolean isChange = true;
    private int index;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 55){
                /**
                 * 设置用户信息的提供者，供 RongIM 调用获取用户名称和头像信息。
                 *
                 * @param userInfoProvider 用户信息提供者。
                 * @param isCacheUserInfo  设置是否由 IMKit 来缓存用户信息。<br>
                 *                         如果 App 提供的 UserInfoProvider
                 *                         每次都需要通过网络请求用户数据，而不是将用户数据缓存到本地内存，会影响用户信息的加载速度；<br>
                 *                         此时最好将本参数设置为 true，由 IMKit 将用户信息缓存到本地内存中。
                 * @see UserInfoProvider
                 */
                RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {

                    @Override
                    public UserInfo getUserInfo(String userId) {
                        return Singleton.getInstance().getUserInfoById(LoginActivity.this, userId);//根据 userId 去你的用户系统里查询对应的用户信息返回给融云 SDK。
                    }

                }, true);
                RongIM.setConversationBehaviorListener(new MyConversationBehaviorListener());
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
                        }else {
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
        }
    };
    private String userId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        AppManager.getAppManager().addActivity(this);
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(LoginActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA,
                                Manifest.permission.RECORD_AUDIO}, 1);
            }
        }

        //设置输入框不遮蔽
        initRoot();
        //初始化控件
        initView();


    }

    private void initView() {
        nameEdit = (EditText) findViewById(R.id.login_user_edit);
        passEdit = (EditText) findViewById(R.id.login_passwd_edit);
        forget = (TextView) findViewById(R.id.forget_key);

        mDialog = new LoadingDialog(this);
        btnLogin.setOnClickListener(this);
        forget.setOnClickListener(this);

        if (!TextUtils.isEmpty(SPUtil.getString(this, App.USER_PHONE))) {
            nameEdit.setText(SPUtil.getString(this, App.USER_PHONE));
            nameEdit.setSelection(SPUtil.getString(this, App.USER_PHONE).length());
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.login_login_btn:

                if (!Utils.judgeNetConnected(this)) {
                    Utils.showToast(this, "请连接网络");
                    return;
                }
                userName = nameEdit.getEditableText().toString();
                passWord = passEdit.getEditableText().toString();
                key = passWord;
                if (passWord.equals("izc000")) {
                    isChange = true;
                }else {
                    isChange = false;
                }


                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(passWord)) {
                    Utils.showToast(this, "用户名或密码不能为空");
                    return;
                }
                if (passWord.length() != 32) {
                    passWord = DateUtils.string2MD5(passWord.trim());
                }
                if (mDialog != null && !mDialog.isShowing()) {
                    mDialog.show();
                } else {
                    mDialog = new LoadingDialog(this);
                    mDialog.show();
                }
                RequestParams params = new RequestParams();
                params.addBodyParameter("username", userName);
                params.addBodyParameter("password", passWord);
                String login = DemoApi.HOST + DemoApi.DEMO_LOGIN_EMAIL;
                ServerUtils.getServerDatasPost(login, params, callback);
//                ServerUtils.getServerDatasPost(userName,passWord,tokenCallBack);

                break;
            case R.id.forget_key:
                startActivity(new Intent(LoginActivity.this,ForgetPawActivity.class));
                break;
        }
    }

    private StringCallback tokenCallBack = new StringCallback() {

        @Override
        public void onError(Call call, Exception e) {

        }

        @Override
        public void onResponse(String response) {

            DemoApiJSON demoApiJSON = JsonUtils.jsonToPojo(response, DemoApiJSON.class);
            switch (demoApiJSON.getStatus()) {
                case 200://登录成功
                    //登录后zw_token 保存为常量
                    WinToast.toast(getApplicationContext(), "正在登录");
                    App.ZCZW_TOKEN = (String) demoApiJSON.getData();
                    SPUtil.put(LoginActivity.this, App.ZW_TOKEN, App.ZCZW_TOKEN);
                    //获取用户信息
                    String url = DemoApi.HOST + DemoApi.GET_USERINFO + "";
                    ServerUtils.getServerDatasGet(myInfo);
                    break;
                case 400://客户端请求错误
                    if (mDialog != null) {
                        mDialog.dismiss();
                        mDialog = null;
                    }
                    WinToast.toast(getApplicationContext(), demoApiJSON.getMsg());

                    break;
                case 444://session过期
                    //非登录界面跳转到登录界面
                    if (mDialog != null) {
                        mDialog.dismiss();
                        mDialog = null;
                    }
                    WinToast.toast(getApplicationContext(), "登录过期");
                    break;
                case 500://服务器错误
                    if (mDialog != null) {
                        mDialog.dismiss();
                        mDialog = null;
                    }
                    WinToast.toast(getApplicationContext(), "服务器错误");
                    break;
            }
        }

    };

    private StringCallback myInfo = new StringCallback() {
        @Override
        public void onError(Call call, Exception e) {

        }

        @Override
        public void onResponse(String response) {

            DemoApiJSON demoApiJSON = JsonUtils.jsonToPojo(response, DemoApiJSON.class);
            switch (demoApiJSON.getStatus()) {
                case 200:
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject data = jsonObject.getJSONObject("data");
                        String jsondata = data.toString();
                        User user = JsonUtils.jsonToPojo(jsondata, User.class);
                        if (user != null) {

                            SPUtil.put(LoginActivity.this, App.USER_ID, user.getUserid()+"");
                            SPUtil.put(LoginActivity.this, App.USER_NAME, user.getUsername()+"");
                            SPUtil.put(LoginActivity.this, App.USER_TOKEN, user.getToken()+"");
                            SPUtil.put(LoginActivity.this, App.ZW_TOKEN, App.ZCZW_TOKEN);
                            SPUtil.put(LoginActivity.this, App.POSITION_NAME, user.getPositionName()+"");
                            SPUtil.put(LoginActivity.this, App.USER_SEX, user.getUsersex()+"");
                            SPUtil.put(LoginActivity.this, App.USER_PHONE, user.getUserphone()+"");
                            SPUtil.put(LoginActivity.this, App.CATEGORY_NAME, user.getCategoryName()+"");
                            SPUtil.put(LoginActivity.this, App.IMAGE_POSITION, user.getImagePosition()+"");
                            SPUtil.put(LoginActivity.this, App.JOBSNAME, user.getJobsName()+"");
                            SPUtil.put(LoginActivity.this, App.UNITSNAME, user.getUnitsName()+"");
                            SPUtil.put(LoginActivity.this, App.PRILLEVELNAME, user.getPrilLevelName()+"");
                            SPUtil.put(LoginActivity.this, App.PRILLEVEL, user.getPrilLevel()+"");
                            SPUtil.put(LoginActivity.this, "passWord",key);
                        } else {
                            WinToast.toast(getApplicationContext(), "用户信息解析错误");
                        }

                        //获取好友列表
                        userId = SPUtil.getString(LoginActivity.this, App.USER_ID);
                        ServerUtils.mFirends(userId, 1, "", friendsCallBack);
                        ServerUtils.getMyCircle(0, group);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                case 400:
                    WinToast.toast(getApplicationContext(), "用户信息获取失败");
                    if (mDialog != null) {
                        mDialog.dismiss();
                        mDialog = null;
                    }
                    break;
                case 444:
                    WinToast.toast(getApplicationContext(), "登录过期");
                    if (mDialog != null) {
                        mDialog.dismiss();
                        mDialog = null;
                    }
                    break;
                case 500:
                    WinToast.toast(getApplicationContext(), "用户信息:系统错误");
                    if (mDialog != null) {
                        mDialog.dismiss();
                        mDialog = null;
                    }
                    break;
            }
        }
    };

    private StringCallback friendsCallBack = new StringCallback() {
        @Override
        public void onError(Call call, Exception e) {

        }

        @Override
        public void onResponse(String response) {

            DemoApiJSON demoApiJSON = JsonUtils.jsonToPojo(response, DemoApiJSON.class);
            switch (demoApiJSON.getStatus()) {
                case 200:
                    Friends friends = JsonUtils.jsonToPojo(response, Friends.class);
                    Singleton.getInstance().setFriendlist(friends.getData());
                    ArrayList<User> friendList = (ArrayList<User>) friends.getData();
                    List<Friend> mFriendsList = new ArrayList<>();
                    if (friendList != null) {
                        for (User userInfo : friendList) {
                            Friend friend = new Friend();
                            friend.setNickname(userInfo.getUsername());
                            friend.setGroupname(userInfo.getUnitsName()+ "."+ userInfo.getJobsName());
                            friend.setPortrait(userInfo.getImagePosition());
                            friend.setUserId(userInfo.getUserid() + "");
                            friend.setPhone(userInfo.getUserphone());
                            friend.setIsSelected(false);
                            mFriendsList.add(friend);
                        }
                        Singleton.getInstance().setmFriendsList(mFriendsList,LoginActivity.this);
                    }

//                    WinToast.toast(getApplicationContext(), "正在加载好友信息");
                    handler.sendEmptyMessage(55);
                    if (mDialog != null)
                        mDialog.dismiss();
                    if(isChange){
                        dialog();
                    }else{
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                    break;
                case 400:
                    WinToast.toast(getApplicationContext(), "好友列表加载失败");

                    break;
                case 444:
                    WinToast.toast(getApplicationContext(), "登录过期");
                    break;
                case 500:
                    WinToast.toast(getApplicationContext(), demoApiJSON.getMsg());
                    break;
            }
        }
    };



    private RequestCallBack<String> callback = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            DemoApiJSON demoApiJSON = JsonUtils.jsonToPojo(json, DemoApiJSON.class);
            switch (demoApiJSON.getStatus()) {
                case 200://登录成功
                    //登录后zw_token 保存为常量
                    WinToast.toast(getApplicationContext(), "正在登录");
                    App.ZCZW_TOKEN = (String) demoApiJSON.getData();
                    SPUtil.put(LoginActivity.this, App.ZW_TOKEN, App.ZCZW_TOKEN);
                    //获取用户信息
                    String url = DemoApi.HOST +DemoApi.GET_USERINFO +"";
                    ServerUtils.getServerDatasGet(url,mUserInfo);
                    break;
                case 400://客户端请求错误
                    if (mDialog != null) {
                        mDialog.dismiss();
                        mDialog = null;
                    }
                    WinToast.toast(getApplicationContext(), demoApiJSON.getMsg());

                    break;
                case 444://session过期
                    //非登录界面跳转到登录界面
                    if (mDialog != null) {
                        mDialog.dismiss();
                        mDialog = null;
                    }
                    WinToast.toast(getApplicationContext(), "登录过期");
                    break;
                case 500://服务器错误
                    if (mDialog != null) {
                        mDialog.dismiss();
                        mDialog = null;
                    }
                    WinToast.toast(getApplicationContext(), "服务器错误");
                    break;
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
            Log.e("test", e.getMessage());
            if (mDialog != null) {
                mDialog.dismiss();
                mDialog = null;
            }
        }
    };

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

                            SPUtil.put(LoginActivity.this, App.USER_ID, user.getUserid()+"");
                            SPUtil.put(LoginActivity.this, App.USER_NAME, user.getUsername()+"");
                            SPUtil.put(LoginActivity.this, App.USER_TOKEN, user.getToken()+"");
                            SPUtil.put(LoginActivity.this, App.ZW_TOKEN, App.ZCZW_TOKEN);
                            SPUtil.put(LoginActivity.this, App.POSITION_NAME, user.getPositionName()+"");
                            SPUtil.put(LoginActivity.this, App.USER_SEX, user.getUsersex()+"");
                            SPUtil.put(LoginActivity.this, App.USER_PHONE, user.getUserphone()+"");
                            SPUtil.put(LoginActivity.this, App.CATEGORY_NAME, user.getCategoryName()+"");
                            SPUtil.put(LoginActivity.this, App.IMAGE_POSITION, user.getImagePosition()+"");
                            SPUtil.put(LoginActivity.this, App.JOBSNAME, user.getJobsName()+"");
                            SPUtil.put(LoginActivity.this, App.UNITSNAME, user.getUnitsName()+"");
                            SPUtil.put(LoginActivity.this, App.PRILLEVELNAME, user.getPrilLevelName()+"");
                            SPUtil.put(LoginActivity.this, App.PRILLEVEL, user.getPrilLevel()+"");
                            SPUtil.put(LoginActivity.this, "passWord",key);
                        } else {
                            WinToast.toast(getApplicationContext(), "用户信息解析错误");
                        }

                        //获取好友列表
                        userId = SPUtil.getString(LoginActivity.this, App.USER_ID);
                        ServerUtils.mFirends(userId, 1, "", friends);
                        ServerUtils.getMyCircle(0, group);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                case 400:
                    WinToast.toast(getApplicationContext(), "用户信息获取失败");
                    if (mDialog != null) {
                        mDialog.dismiss();
                        mDialog = null;
                    }
                    break;
                case 444:
                    WinToast.toast(getApplicationContext(), "登录过期");
                    if (mDialog != null) {
                        mDialog.dismiss();
                        mDialog = null;
                    }
                    break;
                case 500:
                    WinToast.toast(getApplicationContext(), "用户信息:系统错误");
                    if (mDialog != null) {
                        mDialog.dismiss();
                        mDialog = null;
                    }
                    break;
            }
        }
        @Override
        public void onFailure(HttpException e, String s) {
            if (mDialog != null) {
                mDialog.dismiss();
                mDialog = null;
            }
            WinToast.toast(getApplicationContext(), "用户信息获取失败");
        }
    };

    RequestCallBack<String> friends = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
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
                            friend.setGroupname(userInfo.getUnitsName()+ "."+ userInfo.getJobsName());
                            friend.setPortrait(userInfo.getImagePosition());
                            friend.setUserId(userInfo.getUserid() + "");
                            friend.setPhone(userInfo.getUserphone());
                            friend.setIsSelected(false);
                            mFriendsList.add(friend);
                        }
                        Singleton.getInstance().setmFriendsList(mFriendsList,LoginActivity.this);
                    }

//                    WinToast.toast(getApplicationContext(), "正在加载好友信息");
                    handler.sendEmptyMessage(55);
                    if (mDialog != null)
                        mDialog.dismiss();
                    if(isChange){
                        dialog();
                    }else{
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                    break;
                case 400:
                    WinToast.toast(getApplicationContext(), "好友列表加载失败");

                    break;
                case 444:
                    WinToast.toast(getApplicationContext(), "登录过期");
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
                    connect(SPUtil.getString(LoginActivity.this, App.USER_TOKEN));
                    handler.sendEmptyMessage(77);
                    break;
                case 400://客户端错误
                    WinToast.toast(getApplicationContext(), "客户端错误");
                    break;
                case 444://登陆过期
                    WinToast.toast(getApplicationContext(), "登录过期");
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
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage("使用默认密码，是否修改？");
        builder.setTitle("安全提示：");
        builder.setPositiveButton("前往修改", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(LoginActivity.this, ChangeActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                connect(SPUtil.getString(LoginActivity.this,App.USER_TOKEN));
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
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
                    Log.e("LoginActivity", "--onTokenIncorrect");
                }
                /**
                 * 连接融云成功
                 * @param userid 当前 token
                 */
                @Override
                public void onSuccess(String userid) {
                    Log.e("LoginActivity", "--onSuccess:" + userid);
//                    handler.sendEmptyMessage(88);
                }
                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.e("LoginActivity", "--onError:" + errorCode);
                }
            });
        }
    }

    private void initRoot() {
        loginRoot = (RelativeLayout) findViewById(R.id.login_root);
        login = (RelativeLayout) findViewById(R.id.login_login_act);
        btnLogin = (Button) findViewById(R.id.login_login_btn);
        controlKeyboardLayout(loginRoot, login);

        loginRoot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                return imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });

    }
    /**
     * @param root
     *            最外层布局，需要调整的布局
     * @param scrollToView
     *            被键盘遮挡的scrollToView，滚动root,使scrollToView在root可视区域的底部
     */
    private void controlKeyboardLayout(final View root, final View scrollToView) {
        // 注册一个回调函数，当在一个视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变时调用这个回调函数。
        root.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Rect rect = new Rect();
                        // 获取root在窗体的可视区域
                        root.getWindowVisibleDisplayFrame(rect);
                        // 当前视图最外层的高度减去现在所看到的视图的最底部的y坐标
                        int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
                        // 若rootInvisibleHeight高度大于100，则说明当前视图上移了，说明软键盘弹出了
                        if (rootInvisibleHeight > 250) {
                            //软键盘弹出来的时候
                            int[] location = new int[2];
                            // 获取scrollToView在窗体的坐标
                            scrollToView.getLocationInWindow(location);
                            // 计算root滚动高度，使scrollToView在可见区域的底部
                            int srollHeight = (location[1] + scrollToView.getHeight()) - rect.bottom;
                            root.scrollTo(0, srollHeight);
//                            Log.e("1111", "srollHeight: " + srollHeight);
                        } else {
                            // 软键盘没有弹出来的时候
//                            Log.e("111", "no: ");
                            root.scrollTo(0, 0);
                        }
                    }
                });
    }

    private long mkeyTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(System.currentTimeMillis() - mkeyTime >3000){
                mkeyTime = System.currentTimeMillis();
                WinToast.toast(getApplicationContext(), "再按一次退出");
                return  true;
            }else{
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancelAll();
                AppManager.getAppManager().finishAllActivity();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
