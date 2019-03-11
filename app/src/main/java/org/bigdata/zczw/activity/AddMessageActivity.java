package org.bigdata.zczw.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.svideo.sdk.external.struct.common.CropKey;
import com.aliyun.svideo.sdk.external.struct.common.VideoDisplayMode;
import com.aliyun.svideo.sdk.external.struct.common.VideoQuality;
import com.aliyun.svideo.sdk.external.struct.encoder.VideoCodecs;
import com.aliyun.svideo.sdk.external.struct.recorder.CameraType;
import com.aliyun.svideo.sdk.external.struct.recorder.FlashType;
import com.aliyun.svideo.sdk.external.struct.snap.AliyunSnapVideoParam;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.vincent.videocompressor.VideoCompress;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import org.bigdata.zczw.App;
import org.bigdata.zczw.R;
import org.bigdata.zczw.ZCZWConstants;
import org.bigdata.zczw.adapter.FilesAdapter;
import org.bigdata.zczw.adapter.GalleryAdapter;
import org.bigdata.zczw.entity.Bean;
import org.bigdata.zczw.entity.CustomGallery;
import org.bigdata.zczw.entity.MsgTag;
import org.bigdata.zczw.image.SelectPhotoActivity;
import org.bigdata.zczw.image.SelectPhotoAdapter;
import org.bigdata.zczw.ui.NoScrollListView;
import org.bigdata.zczw.ui.WinToast;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.CommonUtils;
import org.bigdata.zczw.utils.DemoApi;
import org.bigdata.zczw.utils.EmojiFilter;
import org.bigdata.zczw.utils.FileSizeUtil;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.NoUnderlineSpan;
import org.bigdata.zczw.utils.SPUtil;
import org.bigdata.zczw.utils.Utils;
import org.bigdata.zczw.video.AliyunVideoRecorder;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/*
* 发动态
* */

public class AddMessageActivity extends AppCompatActivity
        implements View.OnClickListener,BDLocationListener, TextWatcher, AdapterView.OnItemClickListener {

    private boolean isVideo = false;
    //视频转码后存放地址
    private String outputDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
//    private String inputPath;
    private String outVideoPath;

    public final int MEDIA_TYPE_VIDEO = 2;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
//    private static String VIDEO_URL = "http://zczw.ewonline.org:8093/message/addvideo";

    private double longitude;// 经度
    private double latitude;// 纬度
    private String location = "未定位";//地理位置

    private RelativeLayout addPic,addCam,addVideo,addRec,addTopic;

    private EditText messageContext;// 动态内容
    private NoScrollListView listView;

    private TextView tv_location;
    private ImageView imgVideo,play;
    private RelativeLayout videoContent;
    private TextView names;

    private Bitmap videoPhoto;
    private String videoPath;
    private Uri fileUri;
    private String ACTION_NAME = "message/add";

    private String type;
    private String userIds = "";
    private String userNames;
    private String publicScope;

    // 发布动态
    private String topicMsg;
    private String topic;
    private ArrayList<String> topicName;
    private String mFile,edit;
    private String topicRangeStr;

    final String orderId = UUID.randomUUID().toString().replaceAll("-", "");
    private LocationClient mLocationClient;

    private ArrayList<String> picList;
    private ArrayList<File> fileList;
    private ArrayList<File> wordFileList;
    private ArrayList<Uri> uriList;
    private FilesAdapter filesAdapter;

    private PostFormBuilder postFormBuilder;

    private boolean isFormal;
    private int s;
    private File videoThumbnail;

    private Handler handlerTime = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 55){
                mypDialog.dismiss();
                Toast.makeText(AddMessageActivity.this, "动态发布成功", Toast.LENGTH_SHORT).show();
                messageContext.setText("");
                finish();
            }
        }
    };

    private Runnable runnable;
    private int length;
    private String beforeStr,tagIds;

    private ArrayList<MsgTag> tagList;
    private int editPosition;
    private int picIndex;

    //发布动态添加时间和加分部分类型
    private String increaseType;
    private String increaseTime;


    @Override
    protected void onPause() {
        super.onPause();
        //页面消失，如果有数据保存下来
        if (messageContext.getText().toString().trim().length()>0){
            App.PUBLISH_CONTENT = messageContext.getText().toString();
        }else {
            App.PUBLISH_CONTENT = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_message);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);// 使得音量键控制媒体声音
        getSupportActionBar().setLogo(R.drawable.empty_logo);// actionbar 添加logo
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);

        AppManager.getAppManager().addActivity(this);

        initView();
        initDate();
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(AddMessageActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(AddMessageActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }else {
                initMyLocation();
            }
        }else {
            initMyLocation();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 101: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initMyLocation();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    tv_location.setVisibility(View.GONE);
                    location = "未定位";
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    private void initView() {

        messageContext = (EditText) findViewById(R.id.message_detailed_info);
        tv_location = (TextView) findViewById(R.id.tv_location);
        names = (TextView) findViewById(R.id.txt_see_names_add_act);

        videoContent = (RelativeLayout) findViewById(R.id.add_video_content);

        imgVideo = (ImageView) findViewById(R.id.img_video);

        addPic = (RelativeLayout) findViewById(R.id.rl_add_pic_act);
        addCam = (RelativeLayout) findViewById(R.id.rl_add_cam_act);
        addVideo = (RelativeLayout) findViewById(R.id.rl_add_video_act);
        addRec = (RelativeLayout) findViewById(R.id.rl_add_rec_act);
        addTopic = (RelativeLayout) findViewById(R.id.rl_add_topic_act);

        listView = (NoScrollListView) findViewById(R.id.file_list_feed);

        play = (ImageView) findViewById(R.id.video_play);

        publicScope = getIntent().getStringExtra("publicScope");

        topicRangeStr = "";
        topicName = new ArrayList<>();

        if (getIntent().hasExtra("type")) {
            type = getIntent().getStringExtra("type");
            if (type.equals("1") || type.equals("2")) {
                userIds = getIntent().getStringExtra("ids");
                userNames = getIntent().getStringExtra("names");
                names.setText(userNames);
            }else {
                names.setText("公开");
            }
        }else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                type = bundle.getString("type");
                if (type.equals("1")  || type.equals("2")) {
                    userIds = bundle.getString("ids");
                    userNames = bundle.getString("names");
                    names.setText(userNames);
                }else {
                    names.setText("公开");
                }
                edit = bundle.getString("edit");
                mFile = bundle.getString("file");
                isVideo = bundle.getBoolean("isVideo");
                videoPath = mFile;
                videoPhoto = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Images.Thumbnails.MINI_KIND);

                videoPhoto = ThumbnailUtils.extractThumbnail(videoPhoto, 100, 160, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
                videoContent.setVisibility(View.VISIBLE);
                imgVideo.setImageBitmap(videoPhoto);
                videoThumbnail = saveBitmap(videoPhoto);
                play.setImageResource(android.R.drawable.ic_media_play);
                messageContext.setText(edit);
                messageContext.setSelection(edit.length());
            }
        }


    }

    private void initDate() {
        mLocationClient = new LocationClient(getApplicationContext());
        addPic.setOnClickListener(this);
        addCam.setOnClickListener(this);
        addVideo.setOnClickListener(this);
        addRec.setOnClickListener(this);
        addTopic.setOnClickListener(this);
        imgVideo.setOnClickListener(this);

        messageContext.setOnClickListener(this);

        listView.setOnItemClickListener(this);

        messageContext.addTextChangedListener(this);

        picList = new ArrayList<>();
        fileList = new ArrayList<>();
        wordFileList = new ArrayList<>();
        uriList = new ArrayList<>();
        tagIds = "";
        if (getIntent().hasExtra("tag")) {
            tagList = (ArrayList<MsgTag>) getIntent().getSerializableExtra("tag");
            for (int i = 0; i < tagList.size(); i++) {
                if (tagList.get(i).isCheck()) {
                    tagIds = tagIds + tagList.get(i).getId() + ",";
                }
            }
        }

        if (getIntent().hasExtra("increaseTime")){
            increaseTime = getIntent().getStringExtra("increaseTime");
        }
        if (getIntent().hasExtra("increaseType")){
            increaseType =  getIntent().getStringExtra("increaseType");
        }

        // TODO Auto-generated method stub
        runnable = new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                String str = messageContext.getText().toString();
                Linkify.addLinks(messageContext,Linkify.WEB_URLS);

                Pattern trendsPattern = Pattern.compile("(#[\\S][^#]{0,28}#)");
                String trendsScheme = String.format("%s/?%s=", "", App.PARAM_UID);
                Linkify.addLinks(messageContext, trendsPattern, trendsScheme, null, new
                        Linkify.TransformFilter() {
                            @Override
                            public String transformUrl(Matcher match, String url) {
                                return match.group(1);
                            }
                        });
                removeHyperLinkUnderline(messageContext);

                messageContext.setSelection(str.length());

            }
        };

        // 设置已选择的图片为0
        ZCZWConstants.SELECTED_IAMGE = 0;
        // 初始化ImageLoader，ImageLoader是个开源库，用于异步加载大量图片。
        initImageLoader();
        // 初始化选择多个图片功能
        initMultiPick();

        if (getIntent().hasExtra("topic")) {
            topic = getIntent().getStringExtra("topic");
            topicName.add("#"+topic+"#");
            messageContext.setText("#"+topic+"# ");
            editPosition = messageContext.getSelectionEnd();
            handler.post(runnable);
        }

        //如果有之前编辑的数据赋值
        if (App.PUBLISH_CONTENT != null) messageContext.setText(App.PUBLISH_CONTENT);




    }

    /**
     * 初始化定位相关代码
     */
    private void initMyLocation() {
        // 定位初始化
        mLocationClient.registerLocationListener(this);
        // 设置定位的相关配置
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span=1000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    ImageLoader imageLoader;
    ProgressDialog mypDialog;
    static Handler handler = new Handler();
    CharSequence[] items = {"拍照", "从图库中选择"};
    public static final int SELECT_Video  = 10;   //相册
//    CharSequence[] videoItem = { "从相册中选择"};
    CharSequence[] videoItem = {"录像", "从相册中选择"};
    String filePath;
    GalleryAdapter adapter;
    GridView gridGallery;

    private void initImageLoader() {
        @SuppressWarnings("deprecation")
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                this).defaultDisplayImageOptions(defaultOptions).memoryCache(
                new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }

    private void initMultiPick() {

        gridGallery = (GridView) findViewById(R.id.gridGallery);
        adapter = new GalleryAdapter(getApplicationContext(), imageLoader);
        adapter.setResourceId(R.layout.selected_image);
        adapter.setMultiplePick(false);
        gridGallery.setAdapter(adapter);
        adapter.setCancelBtnVisible(true);
        gridGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File file = new File(adapter.getItem(position).sdcardPath);
                Uri uri = Uri.fromFile(file);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "image/*");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ZCZWConstants.SELECTED_IAMGE = 0;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Utils.openFile(AddMessageActivity.this,wordFileList.get(position),uriList.get(position));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 107 && resultCode == Activity.RESULT_OK) {
            listView.setVisibility(View.VISIBLE);
            gridGallery.setVisibility(View.GONE);
            String wordPath;
            File wordFile;
            Uri wordUri;

            wordUri = data.getData();

            uriList.add(wordUri);
            if (wordUri != null) {
                String string =wordUri.toString();
                try {
                    string = URLDecoder.decode(string,"utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (string.contains("storage")) {
                    //对Uri进行切割
                    String a[] =string.split("storage");
                    //获取到file
                    wordPath = "storage"+a[1];
                }else {
                    String[] filePathColumn = { MediaStore.Video.Media.DATA };

                    Cursor cursor = getContentResolver().query(wordUri, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    wordPath = cursor.getString(columnIndex);
                    cursor.close();
                }

            }else {
                Utils.showToast(AddMessageActivity.this,"选取文件失败");
                return;
            }

            for (int i = 0; i < wordFileList.size(); i++) {
                if (wordPath.equals(wordFileList.get(i).getAbsolutePath())) {
                    Utils.showToast(AddMessageActivity.this,"选取文件重复");
                    return;
                }
            }
            wordFile = new File(wordPath);
            wordFileList.add(wordFile);
            filesAdapter.notifyDataSetChanged();
        }

        if (5 == requestCode && RESULT_OK == resultCode) {

            // 将拍照的图片更新到创建动态中
            CustomGallery item = new CustomGallery();
            // item.sdcardPath = getRealPathFromURI(imageUri);
            item.sdcardPath = filePath;
            adapter.add(item);
            gridGallery.setVisibility(View.VISIBLE);

        } else if (requestCode == 23) {

            if (data != null) {
                ArrayList<SelectPhotoAdapter.SelectPhotoEntity> selectedPhotos = data.getParcelableArrayListExtra("selectPhotos");
                isFormal = data.getBooleanExtra("isFormal",false);
                for (int i = 0; i < selectedPhotos.size(); i++) {
                    CustomGallery item = new CustomGallery();
                    item.sdcardPath = selectedPhotos.get(i).url;
                    adapter.add(item);
                }

                gridGallery.setVisibility(View.VISIBLE);
                picList = adapter.getPath();
            }
        } else if (7 == requestCode && RESULT_OK == resultCode) {
            WinToast.toast(AddMessageActivity.this, "图片位置不存在，请重新选择");
            adapter.clear();
            adapter.clearCache();
        }
        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                videoPhoto = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Images.Thumbnails.MINI_KIND);
                videoPhoto = ThumbnailUtils.extractThumbnail(videoPhoto, 100, 160, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
                videoContent.setVisibility(View.VISIBLE);
                imgVideo.setImageBitmap(videoPhoto);
                videoThumbnail = saveBitmap(videoPhoto);
                play.setImageResource(android.R.drawable.ic_media_play);

            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the video capture
            } else {
                // Video capture failed, advise user
            }
        }
        if (requestCode == 1033 && resultCode == RESULT_OK) {
            isVideo = true;
            gridGallery.setVisibility(View.GONE);
            Uri selectedVideo = data.getData();
            String[] filePathColumn = { MediaStore.Video.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedVideo ,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            videoPath = cursor.getString(columnIndex);
            cursor.close();

            double fileSize = FileSizeUtil.getFileOrFilesSize(videoPath, 3);
            if (fileSize <80) {
                videoPhoto = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Images.Thumbnails.MINI_KIND);
                videoPhoto = ThumbnailUtils.extractThumbnail(videoPhoto, 100, 160, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
                videoContent.setVisibility(View.VISIBLE);
                imgVideo.setImageBitmap(videoPhoto);
                videoThumbnail = saveBitmap(videoPhoto);
                play.setImageResource(android.R.drawable.ic_media_play);
            }else{
                Toast.makeText(AddMessageActivity.this,"目标文件为"+fileSize+"Mb,超过默认80Mb限制，不支持上传",Toast.LENGTH_LONG).show();
                isVideo = false;
            }
        }
        if (requestCode == 1031 && resultCode == 105) {
            if (data .hasExtra("videoPath")) {
                isVideo = true;
                videoPath = data.getStringExtra("videoPath");
                videoPhoto = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Images.Thumbnails.MINI_KIND);
                videoPhoto = ThumbnailUtils.extractThumbnail(videoPhoto, 100, 160, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
                videoContent.setVisibility(View.VISIBLE);
                imgVideo.setImageBitmap(videoPhoto);
                videoThumbnail = saveBitmap(videoPhoto);
                play.setImageResource(android.R.drawable.ic_media_play);
            }else {
                isVideo = false;
            }
        }
        if(requestCode == 1035){
            isVideo = true;
            gridGallery.setVisibility(View.GONE);
            if(resultCode == Activity.RESULT_OK && data!= null){
                int type = data.getIntExtra(AliyunVideoRecorder.RESULT_TYPE,0);
                if(type ==  AliyunVideoRecorder.RESULT_TYPE_CROP){
                    videoPath = data.getStringExtra(CropKey.RESULT_KEY_CROP_PATH);
                    Log.e("1111", "文件路径为 "+ videoPath + " 时长为 " + data.getLongExtra(CropKey.RESULT_KEY_DURATION,0) );
                }else if(type ==  AliyunVideoRecorder.RESULT_TYPE_RECORD){
                    Log.e("1111","文件路径为 "+ data.getStringExtra(AliyunVideoRecorder.OUTPUT_PATH ));
                    videoPath = data.getStringExtra(AliyunVideoRecorder.OUTPUT_PATH );
                }
                videoPhoto = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Images.Thumbnails.MINI_KIND);
                videoPhoto = ThumbnailUtils.extractThumbnail(videoPhoto, 100, 160, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
                videoContent.setVisibility(View.VISIBLE);
                imgVideo.setImageBitmap(videoPhoto);
                videoThumbnail = saveBitmap(videoPhoto);
                play.setImageResource(android.R.drawable.ic_media_play);
            }else if(resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(this,"用户取消录制",Toast.LENGTH_SHORT).show();
                isVideo = false;
            }
        }
        if (requestCode == 102) {
            if (data != null) {
                topicMsg = data.getStringExtra("msg");
                messageContext.setText(topicMsg);
                editPosition = topicMsg.length();
                if (!TextUtils.isEmpty(topicMsg)) {
                    handler.post(runnable);
                }

            }
        }
    }

    private void removeHyperLinkUnderline(TextView tv) {

        CharSequence text = tv.getText();

        if(text instanceof Spannable){
            Spannable spannable = (Spannable) tv.getText();

            NoUnderlineSpan noUnderlineSpan = new NoUnderlineSpan();

            spannable.setSpan(noUnderlineSpan,0,text.length(), Spanned.SPAN_MARK_MARK);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.de_add_dongtai, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.icon:
                String comsg = messageContext.getText().toString();
                if (TextUtils.isEmpty(comsg)) {
                    WinToast.toast(AddMessageActivity.this, "请输入动态信息。");
                    break;
                }
                topicRangeStr = "";
                topicName.clear();

                mypDialog = new ProgressDialog(this);
                // 设置进度条风格，风格为圆形，旋转的
                mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                // 设置ProgressDialog 标题
                mypDialog.setTitle("张承政务");
                // 设置ProgressDialog 提示信息
                mypDialog.setMessage("动态发布中，请稍等...");
                // 设置ProgressDialog 标题图标
                // mypDialog.setIcon(R.drawable.logo); //之后可以放logo
                // 设置ProgressDialog 的进度条是否不明确
                mypDialog.setIndeterminate(false);
                // 设置ProgressDialog 是否可以按退回按键取消
                mypDialog.setCancelable(false);
                mypDialog.show();

                Pattern p = Pattern.compile("(#[\\S][^#]{0,28}#)");
                Matcher m = p.matcher(comsg);
                while(m.find()){
                    boolean isNew = true;
                    for (int i = 0; i < topicName.size(); i++) {
                        if (m.group().equals(topicName.get(i))) {
                            isNew = false;
                            break;
                        }
                    }
                    if (isNew) {
                        topicName.add(m.group());
                    }
                }

                int index = 0;
                for (int j = 0; j < topicName.size(); j++) {
                    String name = topicName.get(j);
                    while((index = comsg.indexOf(name, index)) != -1){
                        topicRangeStr = topicRangeStr+(index)+"-"+(index + name.length())+"/";
                        index = index + name.length();
                    }
                }

                if (wordFileList.size() > 0) {
                    long length = 0;
                    for (int i = 0; i < wordFileList.size(); i++) {
                        length = length+wordFileList.get(i).length();
                    }
                    if (length>1024*1024*40) {
                        Utils.showToast(AddMessageActivity.this,"超过默认40Mb限制");
                        break;
                    }else {
                        sendFile();
                    }
                } else if (isVideo){
                    sendVideoMessage();
                }else{
                    try {
                        if (picList != null &&picList.size()>0) {
                            fileList.clear();
                            picIndex = 0;
                            Luban.with(AddMessageActivity.this)
                                    .load(new File(picList.get(picIndex)))
                                    .setCompressListener(compressListener)
                                    .launch();
                        }else {
                            createMessage();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendFile() {
        // 如果没有网络则返回
        if (!CommonUtils.isNetworkConnected(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), ZCZWConstants.NETWORK_INVALID, Toast.LENGTH_SHORT).show();
            return;
        }

        String url = DemoApi.HOST + ACTION_NAME;
        postFormBuilder = OkHttpUtils.getInstance().post().url(url);

        postFormBuilder.addHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN);
        postFormBuilder.addParams("content",EmojiFilter.filterEmoji(messageContext.getText().toString()));
        postFormBuilder.addParams("latitude",latitude+"");
        postFormBuilder.addParams("longitude",longitude+"");
        postFormBuilder.addParams("location",location);
        postFormBuilder.addParams("publicScope",publicScope);
        postFormBuilder.addParams("forwardMessageId","");
        postFormBuilder.addParams("topicRangeStr",topicRangeStr);
        postFormBuilder.addParams("tagIds",tagIds);
        if (increaseTime!=null){
            postFormBuilder.addParams("increaseTime",increaseTime);
        }
        if (increaseType != null){
            postFormBuilder.addParams("increaseType",increaseType);
        }
        if (type.equals("0")) {
            postFormBuilder.addParams("userIds","");
        }else {
            postFormBuilder.addParams("userIds",userIds);
        }
        for (int i = 0; i < wordFileList.size(); i++) {
            postFormBuilder.addFile("files",wordFileList.get(i).getName(),wordFileList.get(i));
        }

        postFormBuilder.build().execute(sendCallback);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_add_pic_act://图库
                if (isVideo) {
                    Utils.showToast(AddMessageActivity.this,"不能同时发布图片、视频或文件");
                    return;
                }
                if (wordFileList.size()>0) {
                    Utils.showToast(AddMessageActivity.this,"不能同时发布图片、视频或文件");
                    return;
                }
                isVideo = false;
                ZCZWConstants.SELECTED_IAMGE = adapter.getCount();
                if (ZCZWConstants.SELECTED_IAMGE < ZCZWConstants.IMAGE_ALLOW) {
                    Intent intentPic = new Intent(AddMessageActivity.this, SelectPhotoActivity.class);
                    intentPic.putExtra("num", picList.size());
                    startActivityForResult(intentPic, 23);
                } else if (ZCZWConstants.SELECTED_IAMGE == ZCZWConstants.IMAGE_ALLOW) {
                    WinToast.toast(AddMessageActivity.this, "最多只能发布九张图片");
                }

                break;
            case R.id.rl_add_cam_act://文件
                if (isVideo || adapter.getCount() >0) {
                    Utils.showToast(AddMessageActivity.this,"不能同时发布图片、视频或文件");
                    return;
                }
                if (wordFileList.size()>= 5) {
                    Utils.showToast(AddMessageActivity.this,"最多只能发布五个文件");
                    return;
                }

                if (Build.VERSION.SDK_INT >= 24) {
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());
                }

                filesAdapter = new FilesAdapter(AddMessageActivity.this,wordFileList);
                filesAdapter.setOnDelClickListener(new FilesAdapter.onDelClickListener() {
                    @Override
                    public void onDelClick(int position) {
                        wordFileList.remove(position);
                        uriList.remove(position);
                        filesAdapter.notifyDataSetChanged();
                    }
                });
                listView.setAdapter(filesAdapter);

                //intent.setType(“image/*”);//选择图片
                //intent.setType(“audio/*”); //选择音频
                //intent.setType(“video/*”); //选择视频 （mp4 3gp 是android支持的视频格式）
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent,107);

                break;
            case R.id.rl_add_video_act://摄像
                if (adapter.getCount()>0 || isVideo || wordFileList.size()>0) {
                    Utils.showToast(AddMessageActivity.this,"不能同时发布图片、视频或文件");
                    return;
                }

                String[] effectDirs = {"炽黄","粉桃","海蓝","红润","灰白","经典","麦茶","浓烈","柔柔","闪耀","鲜果","雪梨","阳光","优雅","朝阳"};



                AliyunSnapVideoParam recordParam = new AliyunSnapVideoParam.Builder()
                        .setResolutionMode(AliyunSnapVideoParam.RESOLUTION_540P)
                        .setRatioMode(AliyunSnapVideoParam.RATIO_MODE_3_4)
                        .setRecordMode(AliyunSnapVideoParam.RECORD_MODE_AUTO)
                        .setFilterList(effectDirs)
                        .setBeautyLevel(80)
                        .setBeautyStatus(false)
                        .setCameraType(CameraType.BACK)
                        .setFlashType(FlashType.OFF)
                        .setNeedClip(true)
                        .setMaxDuration(30000)
                        .setMinDuration(2000)
                        .setVideoQuality(VideoQuality.HD)
                        .setGop(5)
                        .setVideoCodec(VideoCodecs.H264_HARDWARE)

                        /**
                         * 裁剪参数
                         */
                        .setFrameRate(25)
                        .setCropMode(VideoDisplayMode.FILL)
                        //显示分类SORT_MODE_VIDEO视频;SORT_MODE_PHOTO图片;SORT_MODE_MERGE图片和视频
                        .setSortMode(AliyunSnapVideoParam.SORT_MODE_VIDEO)
                        .build();

                AliyunVideoRecorder.startRecordForResult(AddMessageActivity.this,1035,recordParam);
                break;
            case R.id.rl_add_rec_act://视频
                if (adapter.getCount()>0 || isVideo || wordFileList.size()>0) {
                    Utils.showToast(AddMessageActivity.this,"不能同时发布图片、视频或文件");
                    return;
                }
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1033);
                break;
            case R.id.img_video:
                Uri uri = Uri.parse(videoPath);
                Intent intentPlayer = new Intent(Intent.ACTION_VIEW);
                intentPlayer.setDataAndType(uri, "video/*");
                startActivity(intentPlayer);
                break;
            case R.id.rl_add_topic_act://话题
                String msg = messageContext.getText().toString().trim();
                Intent topicIntent = new Intent(AddMessageActivity.this,TopicSearchActivity.class);
                topicIntent.putExtra("msg",msg+"#");
                startActivityForResult(topicIntent,102);
                break;
            case R.id.message_detailed_info://输入
                editPosition = messageContext.getSelectionEnd();
                break;
            default:
                break;
        }
    }

    private OnCompressListener compressListener = new OnCompressListener() {
        @Override
        public void onStart() {

        }

        @Override
        public void onSuccess(File file) {
            fileList.add(file);
            if (picIndex+1 < picList.size()) {
                picIndex++;
                Luban.with(AddMessageActivity.this)
                        .load(new File(picList.get(picIndex)))
                        .setCompressListener(compressListener)
                        .launch();
            }else {
                try {
                    createMessage();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onError(Throwable e) {

        }
    };

    /**
     * 创建动态消息
     *
     * @throws UnsupportedEncodingException
     * @throws JSONException
     */
    public void createMessage() throws UnsupportedEncodingException, JSONException {
        // 如果没有网络则返回
        if (!CommonUtils.isNetworkConnected(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), ZCZWConstants.NETWORK_INVALID, Toast.LENGTH_SHORT).show();
            return;
        }

        picList = adapter.getPath();
        if (picList != null && picList.size()>0) {
            String url = DemoApi.HOST + ACTION_NAME;
            postFormBuilder = OkHttpUtils.getInstance().post().url(url);

            postFormBuilder.addHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN);
            postFormBuilder.addParams("content",EmojiFilter.filterEmoji(messageContext.getText().toString()));
            postFormBuilder.addParams("latitude",latitude+"");
            postFormBuilder.addParams("longitude",longitude+"");
            postFormBuilder.addParams("location",location);
            postFormBuilder.addParams("publicScope",publicScope);
            postFormBuilder.addParams("forwardMessageId","");
            postFormBuilder.addParams("topicRangeStr",topicRangeStr);
            postFormBuilder.addParams("tagIds",tagIds);
            if (increaseTime!=null){
                postFormBuilder.addParams("increaseTime",increaseTime);
            }
            if (increaseType != null){
                postFormBuilder.addParams("increaseType",increaseType);
            }
            if (type.equals("0")) {
                postFormBuilder.addParams("userIds","");
            }else {
                postFormBuilder.addParams("userIds",userIds);
            }

            if (isFormal) {
                for (int i = 0; i < picList.size(); i++) {
                    postFormBuilder.addFile("pictures",new File(picList.get(i)).getName()+".jpg",new File(picList.get(i)));
                }

                postFormBuilder.build().execute(sendCallback);
            }else {
                for (int i = 0; i < fileList.size(); i++) {
                    postFormBuilder.addFile("pictures",fileList.get(i).getName()+".jpg",fileList.get(i));
                }
                postFormBuilder.build().execute(sendCallback);
            }

        }else {
            new Thread(new Runnable() {

                @Override
                public void run() {

                    HttpURLConnection connection = null;
                    DataOutputStream outputStream = null;
                    ByteArrayInputStream inputStream = null;
                    String urlServer = DemoApi.HOST + ACTION_NAME;
                    String lineEnd = "\r\n";
                    String twoHyphens = "--";
                    String boundary = "*****";

                    Map<String, Object> params = new HashMap<String, Object>();
                    String userId = null;
                    userId = SPUtil.getString(AddMessageActivity.this, App.USER_ID);
                    String es = messageContext.getText().toString();
                    //params.put("userId", userId);
                    // params.put("messageId", userId);
                    params.put("content", EmojiFilter.filterEmoji(messageContext.getText().toString()));
                    params.put("latitude", latitude);
                    params.put("longitude", longitude);
                    params.put("location", location);
                    params.put("publicScope", publicScope);
                    params.put("forwardMessageId", "");
                    params.put("topicRangeStr",topicRangeStr);
                    params.put("tagIds",tagIds);
                    if (increaseTime!=null){
                        params.put("increaseTime",increaseTime);
                    }
                    if (increaseType != null){
                        params.put("increaseType",increaseType);
                    }
                    if (type.equals("0")) {
                        params.put("userIds", "");
                    }else {
                        params.put("userIds", userIds);
                    }

                    int bytesRead, bytesAvailable, bufferSize;
                    byte[] buffer;
                    int maxBufferSize = 1 * 1024 * 1024;

                    // String pathToOurFile = selectedItems.get(0);
                    try {

                        URL url = new URL(urlServer);
                        connection = (HttpURLConnection) url.openConnection();

                        // Allow Inputs &amp; Outputs.
                        connection.setDoInput(true);
                        connection.setDoOutput(true);
                        connection.setUseCaches(false);

                        // Set HTTP method to POST.
                        connection.setRequestMethod("POST");
                        connection.setRequestProperty("Connection", "Keep-Alive");
                        connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                        connection.setRequestProperty("Cookie", "zw_token=" + App.ZCZW_TOKEN);

                        StringBuilder sb = new StringBuilder();
                        // 上传的表单参数部分，格式请参考文章
                        for (Map.Entry<String, Object> entry : params.entrySet()) {// 构建表单字段内容
                            sb.append(twoHyphens);
                            sb.append(boundary);
                            sb.append(lineEnd);
                            sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + lineEnd + lineEnd);
                            sb.append(entry.getValue());
                            sb.append(lineEnd);
                        }

                        outputStream = new DataOutputStream(connection.getOutputStream());
                        // 发送文字信息
                        outputStream.write(sb.toString().getBytes());


                        outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                        // Responses from the server (code and message)
                        int serverResponseCode = connection.getResponseCode();

                        outputStream.flush();
                        outputStream.close();
                        if (200 == serverResponseCode) {

                            InputStream is = connection.getInputStream();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            buffer = new byte[1024];
                            int len = 0;
                            while ((len = is.read(buffer)) != -1) {
                                baos.write(buffer, 0, len);
                            }
                            String returnString = baos.toString();
                            baos.close();
                            is.close();
                            JSONObject returnInfo = null;
                            try {
                                // 转换成json数据
                                returnInfo = new JSONObject(returnString);
                            } catch (JSONException e) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "发布动态失败，服务器异常", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            if (returnInfo != null) {

                                int info = returnInfo.getInt("status");
                                if (200 == info) {
                                    // Activity关闭前先关闭进度条，不然会内存泄露
                                    handlerTime.sendEmptyMessageAtTime(55,1000);
                                } else if (400 == info) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(AddMessageActivity.this, "动态创建失败", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        } else if (!CommonUtils.isNetworkConnected(getApplicationContext())) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(AddMessageActivity.this, ZCZWConstants.NETWORK_INVALID, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(AddMessageActivity.this, "创建动态失败，服务器异常", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                WinToast.makeText(AddMessageActivity.this, "图片不存在，请重新选择");
                                adapter.clear();
                                adapter.clearCache();
                            }
                        });

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
                        mypDialog.dismiss();
                    }
                }
            }).start();

        }

    }

    private Callback  sendCallback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e) {
            mypDialog.dismiss();
            mypDialog.hide();
            Utils.showToast(AddMessageActivity.this,"网络异常");
        }

        @Override
        public void onResponse(String response) {
            mypDialog.dismiss();
            mypDialog.hide();
            Bean bean = JsonUtils.jsonToPojo(response,Bean.class);
            if (bean != null && bean.getStatus() == 200) {
                Utils.showToast(AddMessageActivity.this,"发布成功");
                App.PUBLISH_CONTENT = null;
                messageContext.setText("");

                if (videoThumbnail != null) {
                    videoThumbnail.delete();
                }
                finish();
            }else {
                Utils.showToast(AddMessageActivity.this,"发布失败");
            }
        }
    };



    private void sendVideoMessage() {

        if (!CommonUtils.isNetworkConnected(getApplicationContext())) {
            Toast.makeText(getApplicationContext(),
                    ZCZWConstants.NETWORK_INVALID, Toast.LENGTH_SHORT).show();
            return;
        }

        //转码
        outVideoPath = outputDir + File.separator + "out_VID_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".mp4";
        VideoCompress.compressVideoLow(videoPath, outVideoPath, new VideoCompress.CompressListener() {
            @Override
            public void onStart() {
                mypDialog.setMessage("动态视频正在压缩转码中...");

            }

            @Override
            public void onSuccess() {
                //这里发送转码后视频
                mypDialog.setMessage("视频转码成功，正在发布...");
                sendVideoFile(outVideoPath);
            }

            @Override
            public void onFail() {

                mypDialog.setMessage("视频转码失败，原动态发布中...");
                //失败就会发送原视频，并删除目标转码文件
                sendVideoFile(videoPath);
                delete(outVideoPath);
            }

            @Override
            public void onProgress(float percent) {

            }
        });


    }

    /** 删除文件，可以是文件或文件夹
     * @param delFile 要删除的文件夹或文件名
     * @return 删除成功返回true，否则返回false
     */
    private void delete(String delFile) {
        File file = new File(delFile);
        if (!file.exists()) {

        } else {
            if (file.isFile()){
                deleteSingleFile(delFile);
            }

        }
    }

    /** 删除单个文件
     * @param filePath$Name 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    private boolean deleteSingleFile(String filePath$Name) {
        File file = new File(filePath$Name);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                Log.e("--Method--", "Copy_Delete.deleteSingleFile: 删除单个文件" + filePath$Name + "成功！");
                return true;
            } else {
//                Toast.makeText(getApplicationContext(), "删除单个文件" + filePath$Name + "失败！", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
//            Toast.makeText(getApplicationContext(), "删除单个文件失败：" + filePath$Name + "不存在！", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    public File saveBitmap(Bitmap bm) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imgPath = "IMG_"+ timeStamp + ".png";
        File f = new File("/mnt/sdcard/zczw", imgPath);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 100 , out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return f;
    }


    private void sendVideoFile(String videoFilePath){

        String url = DemoApi.VIDEO_URL;
        postFormBuilder = OkHttpUtils.getInstance().post().url(url);

        postFormBuilder.addHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN);
        postFormBuilder.addParams("content",EmojiFilter.filterEmoji(messageContext.getText().toString()));
        postFormBuilder.addParams("latitude",latitude+"");
        postFormBuilder.addParams("longitude",longitude+"");
        postFormBuilder.addParams("location",location);
        postFormBuilder.addParams("publicScope",publicScope);
        postFormBuilder.addParams("forwardMessageId","");
        postFormBuilder.addParams("topicRangeStr",topicRangeStr);
        postFormBuilder.addParams("tagIds",tagIds);
        if (increaseTime!=null){
            postFormBuilder.addParams("increaseTime",increaseTime);
        }
        if (increaseType != null){
            postFormBuilder.addParams("increaseType",increaseType);
        }
        if (type.equals("0")) {
            postFormBuilder.addParams("userIds","");
        }else {
            postFormBuilder.addParams("userIds",userIds);
        }

        MediaPlayer mediaPlayer = new MediaPlayer();
        int fileLen = 10;
        try {
            mediaPlayer.setDataSource(videoFilePath);
            mediaPlayer.prepare();

            s = mediaPlayer.getDuration();
            fileLen = Integer.valueOf(s);
            fileLen = fileLen/1000+1;
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file = new File(videoFilePath);

        double dfileSize = FileSizeUtil.getFileOrFilesSize(videoFilePath, 3);
        int fileSize= (int) (dfileSize+1);

        postFormBuilder.addFile("videos",file.getName()+".mp4",file);
        postFormBuilder.addFile("pictures",videoThumbnail.getName()+".png",videoThumbnail);
        postFormBuilder.addParams("fileLen",fileLen+"");
        postFormBuilder.addParams("fileSize",fileSize+"");
        postFormBuilder.build().execute(sendCallback);

    }


    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if (bdLocation == null) {
            return;
        }
        latitude = bdLocation.getLatitude();
        longitude = bdLocation.getLongitude();
        if (!TextUtils.isEmpty(bdLocation.getAddrStr()) && !TextUtils.isEmpty(bdLocation.getLocationDescribe())) {

            location = bdLocation.getProvince()+bdLocation.getCity()+bdLocation.getDistrict()+bdLocation.getStreet()+bdLocation.getStreetNumber()+bdLocation.getLocationDescribe();
        }
        if (TextUtils.isEmpty(location) || location == null) {
            location = "未定位";
            tv_location.setText(location);
        }
        tv_location.setText(location);
        mLocationClient.stop();

        if (bdLocation.getLocType() == BDLocation.TypeGpsLocation){

            //当前为GPS定位结果，可获取以下信息
            bdLocation.getSpeed();    //获取当前速度，单位：公里每小时
            bdLocation.getSatelliteNumber();    //获取当前卫星数
            bdLocation.getAltitude();    //获取海拔高度信息，单位米
            bdLocation.getDirection();    //获取方向信息，单位度

        } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation){

            //当前为网络定位结果，可获取以下信息
            bdLocation.getOperators();    //获取运营商信息

        } else if (bdLocation.getLocType() == BDLocation.TypeOffLineLocation) {

            //当前为网络定位结果

        } else if (bdLocation.getLocType() == BDLocation.TypeServerError) {

            //当前网络定位失败
            //可将定位唯一ID、IMEI、定位失败时间反馈至loc-bugs@baidu.com

        } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkException) {

            //当前网络不通

        } else if (bdLocation.getLocType() == BDLocation.TypeCriteriaException) {

            //当前缺少定位依据，可能是用户没有授权，建议弹出提示框让用户开启权限
            //可进一步参考onLocDiagnosticMessage中的错误返回码

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mLocationClient.isStarted() && mLocationClient != null) {
            mLocationClient.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mLocationClient.isStarted() && mLocationClient != null) {
            mLocationClient.stop();
        }
    }

    private Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }
    /** Create a FileBean for saving an image or video */
    private  File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getPath() + "/zczw/videos");

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            mediaStorageDir.mkdir();
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }
        videoPath = mediaFile.getPath();
        return mediaFile;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //s:变化前的所有字符； start:字符开始的位置； count:变化前的总字节数；after:变化后的字节数
        beforeStr = s.toString();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String msg = s.toString();

        if (msg.length()>beforeStr.length()) {
            if (msg.endsWith("#")) {
                Intent topicIntent = new Intent(AddMessageActivity.this,TopicSearchActivity.class);
                topicIntent.putExtra("msg",msg);
                startActivityForResult(topicIntent,102);
            }
        }
        editPosition = messageContext.getSelectionEnd();
        //S：变化后的所有字符；start：字符起始的位置；before: 变化之前的总字节数；count:变化后的字节数
    }

    @Override
    public void afterTextChanged(Editable s) {
    }


}
