package org.bigdata.zczw.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.bigdata.zczw.App;
import org.bigdata.zczw.R;
import org.bigdata.zczw.entity.Bean;
import org.bigdata.zczw.entity.UpdateInfo;
import org.bigdata.zczw.ui.WinToast;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.DemoApi;
import org.bigdata.zczw.utils.HttpUtil;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.ParseXmlService;
import org.bigdata.zczw.utils.ServerUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/*
* 关于张承
* */

public class AppInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView versionTxt,appInfo,question;
    private RelativeLayout update;
    private ImageView imgNew;

    private ProgressBar mUpdateProgressBar;
    private int progress;
    private Dialog mDownloadDialog;
    // 下载状态--下载中
    private int DOWNLOAD_ING = 10;
    // 下载状态--下载成功
    private int DOWNLOAD_SUCCESS = 20;
    // 下载状态--下载失败
    private int DOWNLOAD_FAIL = 30;
    private boolean cancelUpdate = false;
    private String mSavePath;

    public static final int CONNECT_SUCCESS = 0;
    public static final int CONNECT_ERROR = 2;
    private UpdateInfo info;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case CONNECT_SUCCESS:
                    info = (UpdateInfo) msg.obj;
                    if (info != null) {
                        versionName = info.getVersionName();
//                        Log.e("1111", "handleMessage: " + versionName + "getVersionName()" + getVersionName());
                        // 比较当前版本与服务器的版本
                        if (!getVersionName().equals(versionName)) {
                            imgNew.setVisibility(View.VISIBLE);
                        }else{
                            imgNew.setVisibility(View.GONE);
                        }
                    }
                    break;
                case CONNECT_ERROR:
                    WinToast.makeText(AppInfoActivity.this, "网络连接错误");
                    break;
                default:
                    break;
            }
        }
    };

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    mUpdateProgressBar.setProgress(progress);
                    break;
                case 20:
                    // 安装APK
                    installApk();
                    break;
                case 30:
                    Toast.makeText(AppInfoActivity.this, "文件下载失败！", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };
    private String versionName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);
        AppManager.getAppManager().addActivity(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);

        initView();

        checkUpdate();

    }

    private void initView() {
        versionTxt = (TextView) findViewById(R.id.txt_version);
        appInfo = (TextView) findViewById(R.id.txt_app_info);
        question = (TextView) findViewById(R.id.txt_app_question);
        update = (RelativeLayout) findViewById(R.id.app_version_update);
        imgNew = (ImageView) findViewById(R.id.img_version_new);

        //版本显示代码
        String versionName = null;
        try {
            versionName = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(versionName)) {
            versionTxt.setText("张承政务 " + versionName);
        }

        appInfo.setOnClickListener(this);
        update.setOnClickListener(this);
        question.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_app_info:
                ServerUtils.getAppAbout(requestCallBack);
                break;
            case R.id.txt_app_question:
                startActivity(new Intent(AppInfoActivity.this,QuestionListActivity.class));
                break;
            case R.id.app_version_update:
                // 检测软件版本
                if(imgNew.getVisibility()==View.GONE){
                    Toast.makeText(AppInfoActivity.this,"当前已是最新版本",Toast.LENGTH_SHORT).show();
                }else{
                    showNoticeDialog();
                }
                break;
        }
    }

    private RequestCallBack<String> requestCallBack = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            Log.e("11", "onSuccess: "+json);
            Bean bean = JsonUtils.jsonToPojo(json, Bean.class);
            if (bean != null && bean.getStatus() == 200) {
                String url = bean.getData();
                Intent intent = new Intent(AppInfoActivity.this,WebInfoActivity.class);
                intent.putExtra("about",url);
                startActivity(intent);
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
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
        File apk = new File(mSavePath, "zczw.apk");
        if (!apk.exists()) {
            return;
        }
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(Uri.fromFile(apk), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("软件有新版本 "+versionName+"，要更新吗？");
        builder.setTitle("软件更新：");
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 显示软件下载对话框
                Toast.makeText(AppInfoActivity.this,"开始下载",Toast.LENGTH_SHORT).show();
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
            dir = AppInfoActivity.this.getFilesDir();
        }

        //创建出通知对象
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(AppInfoActivity.this);
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
                        File file=new File(finalDir,"zczw.apk");

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
                    Toast.makeText(AppInfoActivity.this, "下载失败",Toast.LENGTH_SHORT).show();
                }
            }
        }).start();
    }

    /**
     * 下载APK文件
     */
    private void downloadApk() {
        // 启动下载APK线程
        new DownloadApkThread().start();
    }

    /**
     * 下载APK文件线程
     */
    private class DownloadApkThread extends Thread {

        @Override
        public void run() {
            try {
                // 判断SD卡是否存在，并且是否有读写权限
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    // 获取SD卡路径
                    mSavePath =Environment.getExternalStorageDirectory().getPath() + "/download";
                    URL url = new URL(info.getUrl());
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    // 获取文件大小
                    int fileSize = conn.getContentLength();
                    // 创建输入流
                    InputStream inStream = conn.getInputStream();
                    File file = new File(mSavePath);
                    // 判断文件目录是否存在,不存在则创建该目录
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    File apkFile = new File(mSavePath, "zczw.apk");
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    // 缓存
                    byte[] b = new byte[1024];
                    do {
                        int numRead = inStream.read(b);
                        count += numRead;
                        // 计算进度条位置
                        progress = (int) (((float) count / fileSize) * 100);
                        // 更新进度
                        mHandler.sendEmptyMessage(DOWNLOAD_ING);
                        if (numRead <= 0) {// 下载完成
                            mHandler.sendEmptyMessage(DOWNLOAD_SUCCESS);
                            break;
                        }
                        // 写入文件
                        fos.write(b, 0, numRead);
                    } while (!cancelUpdate);
                }
            } catch (Exception e) {
                e.printStackTrace();
                mHandler.sendEmptyMessage(DOWNLOAD_FAIL);
            } finally {
                mDownloadDialog.dismiss();
            }
        }
    }
}
