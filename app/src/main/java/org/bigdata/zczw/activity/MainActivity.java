package org.bigdata.zczw.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.aliyun.common.httpfinal.QupaiHttpFinal;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.networkbench.agent.impl.NBSAppAgent;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.bigdata.zczw.App;
import org.bigdata.zczw.R;
import org.bigdata.zczw.Singleton;
import org.bigdata.zczw.entity.AttendCount;
import org.bigdata.zczw.entity.AttendCountBean;
import org.bigdata.zczw.entity.NumBean;
import org.bigdata.zczw.entity.UpdateInfo;
import org.bigdata.zczw.fragment.HomeFragment;
import org.bigdata.zczw.fragment.MsgFragment;
import org.bigdata.zczw.fragment.PaiFragment;
import org.bigdata.zczw.fragment.SetFragment;
import org.bigdata.zczw.rong.MyReceiveUnreadCountChangedListener;
import org.bigdata.zczw.ui.BadgeView;
import org.bigdata.zczw.ui.WinToast;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.DatabaseHelper;
import org.bigdata.zczw.utils.DemoApi;
import org.bigdata.zczw.utils.HttpUtil;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.ParseXmlService;
import org.bigdata.zczw.utils.SPUtil;
import org.bigdata.zczw.utils.ServerUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;

/*
*主页面
* 四个页面切换
* 版本检测
* 初始化融云
* 初始化极光
* 融云未读消息监听
* */

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private BadgeView badge;//显示未读消息的控件（自定义）
    private View target;
    private RadioGroup rgmaintab;
    private RadioButton home,msg,friend,set,add;
    private Menu mMenu;

    private BadgeView badgeView;
    private View aim;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private HomeFragment homeFragment;//动态
    private SetFragment setFragment;//我
    private PaiFragment paiFragment;//随手拍
    private MsgFragment msgFragment;//消息

    private String mSavePath;
    public static boolean isForeground = false;
    public static final int CONNECT_SUCCESS = 11;
    public static final int CONNECT_ERROR = 22;
    public static final int KICKED_OFFLINE_BY_OTHER_CLIENT = 3;

    private boolean isShow;

    private UpdateInfo info;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case CONNECT_SUCCESS:
                    info = (UpdateInfo) msg.obj;
                    if (info != null) {
                        versionName = info.getVersionName();
                        Log.e("1111", "handleMessage: " + versionName + "getVersionName()" + getVersionName());
                        // 比较当前版本与服务器的版本
                        if (!getVersionName().equals(versionName)) {
                            showNoticeDialog();
                        }
                    }
                    break;
                case CONNECT_ERROR:
                    WinToast.makeText(MainActivity.this, "网络连接错误");
                    break;
                case KICKED_OFFLINE_BY_OTHER_CLIENT:
                    showStatusDialog();
                    break;
                default:
                    break;
            }

        }
    };

    private String versionName;
    private Uri uri;
    private AttendCount attendCount;

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        AppManager.getAppManager().addActivity(this);
        //申请权限
        init();

        NBSAppAgent.setLicenseKey("9031b88035814a85ad017a95a9a1e208").withLocationServiceEnabled(true).start(this.getApplicationContext());

        initView();
        if (RongIM.getInstance() != null) {
            RongIM.getInstance().addUnReadMessageCountChangedObserver(
                    new MyReceiveUnreadCountChangedListener(badge, target, MainActivity.this),
                    Conversation.ConversationType.PRIVATE, Conversation.ConversationType.GROUP);

            RongIM.setGroupInfoProvider(new RongIM.GroupInfoProvider() {
                @Override
                public Group getGroupInfo(String s) {
                    return Singleton.getInstance().getGroupById(s);
                }
            }, true);
        }

        String id = SPUtil.getString(this, App.USER_ID);
        JPushInterface.setAliasAndTags(this, id, null, new TagAliasCallback() {
            @Override
            public void gotResult(int code, String s, Set<String> set) {
                switch (code) {
                    case 0:
                        break;
                }
            }
        });

        aim = findViewById(R.id.set_main_act);
        badgeView = new BadgeView(this, aim);
        badgeView.setBadgePosition(2);

        //初始化碎片
        initFragments();
        //版本检测
        checkUpdate();

        registerMessageReceiver();

        QupaiHttpFinal.getInstance().initOkHttpFinal();


//        databaseHelper = new DatabaseHelper(this,"zc_friend.db",null,1);
//        databaseHelper.getWritableDatabase();

//        //打开或创建test.db数据库
//        db = openOrCreateDatabase(Environment.getExternalStorageDirectory().getPath() + "/zczw/"+"zc_friend.db", Context.MODE_PRIVATE, null);
//        db.execSQL("DROP TABLE IF EXISTS person");
//        //创建person表
//        db.execSQL("CREATE TABLE person (_id INTEGER PRIMARY KEY AUTOINCREMENT, token VARCHAR)");
//        //插入数据
//        db.execSQL("INSERT INTO person VALUES (NULL, ?)", new Object[]{App.ZCZW_TOKEN});
//
//        //ContentValues以键值对的形式存放数据
//        ContentValues cv = new ContentValues();
//        cv.put("token", App.ZCZW_TOKEN);
//        //插入ContentValues中的数据
//        db.insert("person", null, cv);
//        db.close();


    }

    private void init() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA,
                                Manifest.permission.RECORD_AUDIO}, 1);
            }
        }

        RongIM.getInstance().setConnectionStatusListener(new RongIMClient.ConnectionStatusListener() {
            @Override
            public void onChanged(ConnectionStatus connectionStatus) {
                if (connectionStatus.getValue() == 3) {
                    handler.sendEmptyMessage(KICKED_OFFLINE_BY_OTHER_CLIENT);
                }
            }
        });//设置连接状态监听器。

    }

    @Override
    protected void onStart() {

        super.onStart();
        Singleton.getInstance().setUserSelect(null);
        RongIM.getInstance().removeConversation(io.rong.imlib.model.Conversation.ConversationType.PRIVATE,
                SPUtil.getString(this, App.USER_ID), new RongIMClient.ResultCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {

                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });

        RongIM.getInstance().clearMessages(io.rong.imlib.model.Conversation.ConversationType.PRIVATE,
                SPUtil.getString(this, App.USER_ID), new RongIMClient.ResultCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {

                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });
    }

    private void initView() {
        home = (RadioButton) findViewById(R.id.btn_news_main);
        msg = (RadioButton) findViewById(R.id.btn_msg_main);
        friend = (RadioButton) findViewById(R.id.btn_friend_main);
        set = (RadioButton) findViewById(R.id.btn_set_main);
        add = (RadioButton) findViewById(R.id.btn_addmsg_main);
        target = findViewById(R.id.target);

        add.setOnClickListener(this);

        //获取提醒数据
        ServerUtils.attendMsgCount(countCallBack);

    }


    private RequestCallBack<String> countCallBack = new RequestCallBack<String>() {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            AttendCountBean bean = JsonUtils.jsonToPojo(json,AttendCountBean.class);
            if (bean != null && bean.getData()!=null) {
                attendCount = bean.getData();
                ServerUtils.getBoxUnreadCount(zanCallBack);
            }

        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };


    private RequestCallBack<String> zanCallBack = new RequestCallBack<String>() {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            NumBean numBean = JsonUtils.jsonToPojo(json,NumBean.class);
            if (numBean != null && numBean.getStatus() == 200) {
                int num = numBean.getData()+attendCount.getLeaveCount()+attendCount.getTiaoXiuCount()+attendCount.getXiaoJiaCount();
                if (num >0) {
                    badgeView.setText(num+"");
                    badgeView.show();
                }
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_addmsg_main:
                startActivity(new Intent(MainActivity.this, MsgTagActivity.class));
                break;
        }
    }

    private void initFragments() {
        rgmaintab = (RadioGroup) findViewById(R.id.main_radiogroup);

        fragmentManager = getSupportFragmentManager();

        fragmentTransaction = fragmentManager.beginTransaction();
        homeFragment = new HomeFragment();
        fragmentTransaction.add(R.id.main_container, homeFragment);
        fragmentTransaction.commit();
        setListener();
    }

    private void hideFragment(FragmentTransaction fragmentTransaction){
        if(fragmentTransaction!=null){
            if(homeFragment!=null){
                fragmentTransaction.hide(homeFragment);
            }
            if(msgFragment!=null){
                fragmentTransaction.hide(msgFragment);
            }
            if(setFragment!=null){
                fragmentTransaction.hide(setFragment);
            }
            if(paiFragment!=null){
                fragmentTransaction.hide(paiFragment);
            }
//            if(myListFragment!=null){
//                fragmentTransaction.hide(myListFragment);
//            }
        }
    }

    private void setListener() {
        rgmaintab.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        fragmentTransaction = fragmentManager.beginTransaction();

        switch (checkedId){
            case R.id.btn_news_main:
                isShow = false;
                hideFragment(fragmentTransaction);
                if (homeFragment != null) {
                    fragmentTransaction.show(homeFragment);
                }else{
                    homeFragment = new HomeFragment();
                    fragmentTransaction.add(R.id.main_container, homeFragment);
                }
                break;
            case R.id.btn_msg_main:
                hideFragment(fragmentTransaction);
                isShow = false;
//                if (myListFragment != null) {
//                    fragmentTransaction.show(myListFragment);
//                }else{
//                    myListFragment = new MyListFragment();
//                    fragmentTransaction.add(R.id.main_container, myListFragment);
//                }
                if (msgFragment != null) {
                    fragmentTransaction.show(msgFragment);
                }else{
                    msgFragment = new MsgFragment();
                    fragmentTransaction.add(R.id.main_container, msgFragment);
                }
                break;
            case R.id.btn_friend_main:
                hideFragment(fragmentTransaction);
                isShow = false;
                if (paiFragment != null) {
                    fragmentTransaction.show(paiFragment);
                }else{
                    paiFragment = new PaiFragment();
                    fragmentTransaction.add(R.id.main_container, paiFragment);
                }
                break;
            case R.id.btn_set_main:
                hideFragment(fragmentTransaction);
                badgeView.hide();
                if (setFragment != null) {
                    fragmentTransaction.show(setFragment);
                }else{
                    setFragment = new SetFragment();
                    fragmentTransaction.add(R.id.main_container, setFragment);
                }
                break;
        }
        fragmentTransaction.commit();
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
                RongIM.getInstance().disconnect();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 检查更新
     */
    private void checkUpdate() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    InputStream inStream = HttpUtil.getInputStream(DemoApi.HOST
                            + "apk/version.xml?zw_token=" + App.ZCZW_TOKEN, null, HttpUtil.METHOD_GET);
                    // 解析xml文件。由于XML文件较小，我们采用DOM方式进行解析
                    ParseXmlService service = new ParseXmlService();// 这个类是自己写的解析XML的工具类
                    HashMap<String, String> map = service.parseXml(inStream);
                    UpdateInfo info = new UpdateInfo();
                    info.setName(map.get("name"));
                    info.setUrl(map.get("url"));
                    info.setVersionName(map.get("version"));
                    Message msg = new Message();
                    msg.what = CONNECT_SUCCESS;
                    msg.obj = info;
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(CONNECT_ERROR);
                }
            }
        }).start();
    }

    /**
     * 安装APK
     */
    private void installApk() {

        Intent installApkIntent = new Intent();
        installApkIntent.setAction(Intent.ACTION_VIEW);
        installApkIntent.addCategory(Intent.CATEGORY_DEFAULT);
        installApkIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            installApkIntent.setDataAndType(uri, "application/vnd.android.package-archive");
            installApkIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            installApkIntent.setDataAndType(uri, "application/vnd.android.package-archive");
        }

        if (getPackageManager().queryIntentActivities(installApkIntent, 0).size() > 0) {
            startActivity(installApkIntent);
        }

//        Intent intent=new Intent();
//        intent.setAction(Intent.ACTION_VIEW);
//        intent.addCategory(Intent.CATEGORY_DEFAULT);
//        intent.setDataAndType(uri, "application/vnd.android.package-archive");
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
    }

    /**
     * 获取版本号
     *
     * @return
     */
    private String getVersionName() {
        // 获取包管理者
        PackageManager pm = this.getPackageManager();
        // 参数1:应用的包名
        try {
            PackageInfo packageInfo = pm.getPackageInfo(this.getPackageName(), 0);
            String versionName = packageInfo.versionName;
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 显示软件更新对话框
     */
    private void showNoticeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("软件有新版本 "+versionName+"，要更新吗？");
        builder.setTitle("软件更新：");
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 显示软件下载对话框
                Toast.makeText(MainActivity.this,"开始下载",Toast.LENGTH_SHORT).show();
                createNotify();
            }
        });
        builder.setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 异地登陆
     */
    private void showStatusDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("您的帐号已在其他设备登录！");
        builder.setTitle("安全提示：");
        builder.setPositiveButton("重新登录", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                SPUtil.remove(MainActivity.this, App.ZW_TOKEN);//清空token
                SPUtil.remove(MainActivity.this, App.USER_TOKEN);//清空token
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });
        builder.setCancelable(false);//点返回键不消失Sta
        builder.create().show();
    }

    /**
     * 显示软件下载对话框
     */
    private void createNotify() {
        File dir = null;
        // 判断sd卡是否处于挂载状态
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            // /mnt/sdcard
            mSavePath =Environment.getExternalStorageDirectory().getPath() + "/download";
            dir= new File(mSavePath);
            // 判断文件目录是否存在,不存在则创建该目录
            if (!dir.exists()) {
                dir.mkdir();
            }
        } else {
            // /data/data/包名/files
            dir = MainActivity.this.getFilesDir();
        }

        //创建出通知对象
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this);
        // 必有属性
        builder.setContentTitle("张承政务");// 标题
        builder.setSmallIcon(R.mipmap.ic_launcher);// 图片

        // 可有属性
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        builder.setLargeIcon(bitmap);
        builder.setWhen(System.currentTimeMillis());// 消息发送的时间

        // 获取管理者对象
        final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // 通过管理者 展示消息 参数1:消息的id 参数2:消息对象

        //下载
        final File finalDir = dir;
        final String url = info.getUrl();
        new Thread(new Runnable() {
            InputStream inputStream ;
            @Override
            public void run() {
                HttpClient client=new DefaultHttpClient();
                HttpGet httpGet=new HttpGet(url);
                try {
                    HttpResponse response = client.execute(httpGet);
                    if(response.getStatusLine().getStatusCode()==200){
                        HttpEntity entity = response.getEntity();
                        inputStream= entity.getContent();
                        File file=new File(MainActivity.this.getExternalCacheDir(),"zczw.apk");

                        if (Build.VERSION.SDK_INT >= 24) {
                            uri = FileProvider.getUriForFile(MainActivity.this, "org.bigdata.zczw.FileProvider", file);
                        } else {
                            uri = Uri.fromFile(file);
                        }

                        FileOutputStream fos=new FileOutputStream(file);
                        int len=0;

                        //获取文件的总的长度
                        long all = response.getEntity().getContentLength();
                        //当前每次读取的长度
                        int current_len = 0;

                        byte[] buffer=new byte[1024*100];
                        while((len=inputStream.read(buffer))!=-1){
                            current_len+=len;
                            //获取当前进度的百分比
                            int progress = (int) ((current_len/(float)all)*100);
                            //将进度发送到notify
                            builder.setProgress(100,progress, false);
                            builder.setContentText(progress+"%");
                            manager.notify(1, builder.build());
                            fos.write(buffer,0,len);
                        }
                        fos.close();
                        builder.setContentText("下载完毕");
                        manager.notify(1, builder.build());
                        installApk();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("download", "Exception: " + e);
                    Toast.makeText(MainActivity.this, "下载失败",Toast.LENGTH_SHORT).show();
                }
            }
        }).start();
    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }


    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String extras = intent.getStringExtra(KEY_EXTRAS);
                if (!TextUtils.isEmpty(extras) && extras.contains("MessageBox")) {
                    ServerUtils.attendMsgCount(countCallBack);
                }else if (!TextUtils.isEmpty(extras) && extras.contains("AttendV2Message")) {
                    ServerUtils.attendMsgCount(countCallBack);
                }
            }
        }
    }
}
