package org.bigdata.zczw;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.aliyun.common.httpfinal.QupaiHttpFinal;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.bigdata.zczw.rong.MyConversationBehaviorListener;
import org.bigdata.zczw.rong.MyReceiveMessageListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;

/**
 * Created by darg on 2016/8/6.
 */
public class App extends MultiDexApplication {

    public static String ZCZW_TOKEN = null;
    //评论缓存、动态缓存
    public static HashMap<String,String> commentContent = new HashMap<>();
    public static String PUBLISH_CONTENT = null;
    public static HashMap<String,HashMap<String,String>> commentUserData = new HashMap<>();
    public static HashMap<String,String> replyComment = new HashMap<>();
    public static int NUM1 = 0;
    public static int NUM2 = 0;
    public static int NUM3 = 0;

    public static final String USER_NAME = "username";
    public static final String USER_TOKEN = "user_token";
    public static final String ZW_TOKEN = "zw_token";
    public static final String USER_ID = "user_id";
    public static final String POSITION_NAME = "positionName";
    public static final String USER_SEX = "userSex";
    public static final String USER_PHONE = "userPhone";
    public static final String PASSWORD = "password";
    public static final String IMAGE_POSITION = "imagePosition";
    public static final String CATEGORY_NAME = "categoryName";
    public static final String JOBSNAME = "jobsName";
    public static final String UNITSNAME = "unitsName";
    public static final String PRILLEVELNAME = "prilLevelName";
    public static final String PRILLEVEL = "prilLevel";


    public static final String MENTIONS_SCHEMA ="devdiv://zczw_mentions";
    public static final String TOPIC_SCHEMA ="devdiv://zczw_topic";
    public static final String PARAM_UID ="uid";

    public static final String NAME_PATH ="path";

    private static DisplayImageOptions options;

    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIMClient 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        Thread.currentThread().setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        QupaiHttpFinal.getInstance().initOkHttpFinal();

        final IWXAPI msgApi = WXAPIFactory.createWXAPI(getApplicationContext(), "wx40ff322a1e3a5848",true);

        // 将该app注册到微信
        msgApi.registerApp("wx40ff322a1e3a5848");

        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {
            RongIM.init(this);

            //初始化异步加载插件
            initImageLoader();
            SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID + "=57ff3705");
            RongIM.setConversationBehaviorListener(new MyConversationBehaviorListener());
            RongIM.setOnReceiveMessageListener(new MyReceiveMessageListener());
        }

    }


    class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
        //当有未捕获的异常的调用这个方法
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            Log.e("1111", "未捕获的异常");
            //对异常异常
            ex.printStackTrace();
            try {
                ex.printStackTrace(new PrintStream(new File("/mnt/sdcard/wtf.txt")));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //将我们的应用程序杀死,自杀
            //pid : 进程pid
            //myPid() : 获取当前进程pid
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    private void initImageLoader() {
        File cacheDir = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + File.separator + "ZczwCache");
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getBaseContext())
                .memoryCacheExtraOptions(480, 800)
                        // max width, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(3)
                        // 线程池内线程的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                        // 将保存的时候的URI名称用MD5 加密
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024) // 内存缓存的最大值
                .discCacheSize(50 * 1024 * 1024) // SD卡缓存的最大值
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                        // 由原先的discCache -> diskCache
                .discCache(new UnlimitedDiscCache(cacheDir))
                        // 自定义缓存路径
                .imageDownloader(
                        new BaseImageDownloader(getBaseContext(), 5 * 1000,
                                30 * 1000)) // connectTimeout (5 s), readTimeout
                        // (30 s)超时时间
                .writeDebugLogs() // Remove for release app
                .build();
        // 全局初始化此配置
        ImageLoader.getInstance().init(config);
    }

}
