package org.bigdata.zczw.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.demo.crop.AliyunVideoCrop;
import com.aliyun.demo.recorder.AliyunVideoRecorder;
import com.aliyun.struct.common.VideoQuality;
import com.aliyun.struct.recorder.CameraType;
import com.aliyun.struct.recorder.FlashType;
import com.aliyun.struct.snap.AliyunSnapVideoParam;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import org.bigdata.zczw.App;
import org.bigdata.zczw.R;
import org.bigdata.zczw.ZCZWConstants;
import org.bigdata.zczw.adapter.GalleryAdapter;
import org.bigdata.zczw.entity.Bean;
import org.bigdata.zczw.entity.CustomGallery;
import org.bigdata.zczw.image.SelectPhotoActivity;
import org.bigdata.zczw.image.SelectPhotoAdapter;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.CommonUtils;
import org.bigdata.zczw.utils.DemoApi;
import org.bigdata.zczw.utils.FileSizeUtil;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/*
* 发布随手怕
* */

public class AddPaiActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private RelativeLayout addPic,addCam,addVideo,addRec;
    private RelativeLayout rlVideo;
    private ImageView imgVideo,imgPlay;
    private TextView cancel,push,title;
    private GridView gridView;
    private EditText editText;
    private ProgressDialog mypDialog;
    private RadioGroup rgPai;

    private GalleryAdapter adapter;
    private ImageLoader imageLoader;
    private ArrayList<String> picList;
    private ArrayList<File> fileList;
    private String filePath;
    private String videoPath;
    private Bitmap videoPhoto;
    private File videoThumbnail;

    private String content;
    private String tag = "1";
    private String category,workType,timeType;

    private PostFormBuilder postFormBuilder;
    private int s;
    private boolean isSend;

    private boolean isFormal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pai);
        getSupportActionBar().hide();
        AppManager.getAppManager().addActivity(this);

        initView();
        // 初始化ImageLoader，ImageLoader是个开源库，用于异步加载大量图片。
        initImageLoader();
        // 初始化选择多个图片功能
        initMultiPick();
    }

    private void initView() {
        addPic = (RelativeLayout) findViewById(R.id.rl_add_pic_act);
        addCam = (RelativeLayout) findViewById(R.id.rl_add_cam_act);
        addVideo = (RelativeLayout) findViewById(R.id.rl_add_video_act);
        addRec = (RelativeLayout) findViewById(R.id.rl_add_rec_act);
        rlVideo = (RelativeLayout) findViewById(R.id.video_content_pai);

        imgVideo = (ImageView) findViewById(R.id.video_img_pai);
        imgPlay = (ImageView) findViewById(R.id.auto_video_img_pai);

        cancel = (TextView) findViewById(R.id.txt_cancel_pai);
        push = (TextView) findViewById(R.id.txt_push_pai);
        title = (TextView) findViewById(R.id.txt_title_pai_act);

        gridView = (GridView) findViewById(R.id.gridGallery_pai_act);

        editText = (EditText) findViewById(R.id.edit_add_pai_act);

        rgPai = (RadioGroup) findViewById(R.id.rg_tag_pai_act);

        category = getIntent().getStringExtra("category");
        workType = getIntent().getStringExtra("workType");
        switch (workType){
            case "1":
                timeType = "(岗前)";
                break;
            case "2":
                timeType = "(岗中)";
                break;
            case "3":
                timeType = "(岗后)";
                break;
            case "4":
                timeType = "(其他)";
                break;
        }

        if (category.equals("1")) {
            title.setText("安全生产"+timeType);
        }else {
            title.setText("直通一线"+timeType);
        }

        addPic.setOnClickListener(this);
        addCam.setOnClickListener(this);
        addVideo.setOnClickListener(this);
        addRec.setOnClickListener(this);
        cancel.setOnClickListener(this);
        push.setOnClickListener(this);
        push.setOnClickListener(this);
        rgPai.setOnCheckedChangeListener(this);
    }

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
        picList = new ArrayList<>();
        fileList = new ArrayList<>();
        adapter = new GalleryAdapter(getApplicationContext(), imageLoader);
        adapter.setResourceId(R.layout.selected_image);
        adapter.setMultiplePick(false);
        gridView.setAdapter(adapter);
        adapter.setCancelBtnVisible(true);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId){
            case R.id.rb_tag_zy:
                tag = "1";
                break;
            case R.id.rb_tag_wt:
                tag = "2";
                break;
            case R.id.rb_tag_jy:
                tag = "3";
                break;
        }
    }

    @Override
    public void onClick(View v) {
        picList = adapter.getPath();
        switch (v.getId()){
            case R.id.rl_add_pic_act://图库
                if (picList.size() == 9) {
                    Utils.showToast(AddPaiActivity.this,"最多发布九张图片");
                    break;
                }
                if (picList.size()<9 && TextUtils.isEmpty(videoPath)) {
                    Intent intentPic = new Intent(AddPaiActivity.this, SelectPhotoActivity.class);
                    intentPic.putExtra("num", picList.size());
                    startActivityForResult(intentPic, 101);
                }else {
                    Utils.showToast(AddPaiActivity.this,"只能发布一个视频或九张图片");
                }
                break;
            case R.id.rl_add_cam_act://拍照
                if (picList.size() == 9) {
                    Utils.showToast(AddPaiActivity.this,"最多发布九张图片");
                    break;
                }
                if (picList.size()<9 &&  TextUtils.isEmpty(videoPath)) {
                    if (!CommonUtils.isSDCardExist()) {
                        Toast.makeText(getApplicationContext(), "SD卡不存在", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    Intent intentCam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intentCam.addCategory(Intent.CATEGORY_DEFAULT);
                    String fileName = CommonUtils.getCurrentTime() + ".jpg";
                    File file = new File(ZCZWConstants.STORAGE_IMAGE_PATH);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    File image = new File(ZCZWConstants.STORAGE_IMAGE_PATH, fileName);
                    filePath = image.getAbsolutePath();
                    Uri imageUri = Uri.fromFile(image);
                    intentCam.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intentCam, 102);
                }else {
                    Utils.showToast(AddPaiActivity.this,"只能发布一个视频或九张图片");
                }
                break;
            case R.id.rl_add_video_act://摄像
                if (picList.size()==0 && TextUtils.isEmpty(videoPath)) {
                    AliyunSnapVideoParam recordParam = new AliyunSnapVideoParam.Builder()
                            //设置录制分辨率，目前支持360p，480p，540p，720p
                            .setResulutionMode(AliyunSnapVideoParam.RESOLUTION_540P)
                            //设置视频比例，目前支持1:1,3:4,9:16
                            .setRatioMode(AliyunSnapVideoParam.RATIO_MODE_9_16)
                            .setRecordMode(AliyunSnapVideoParam.RECORD_MODE_AUTO) //设置录制模式，目前支持按录，点录和混合模式
                            .setBeautyLevel(80) //设置美颜度
                            .setBeautyStatus(false) //设置美颜开关
                            .setCameraType(CameraType.BACK) //设置前后置摄像头
                            .setFlashType(FlashType.OFF) // 设置闪光灯模式
                            .setNeedClip(true) //设置是否需要支持片段录制
                            .setMaxDuration(30000) //设置最大录制时长 单位毫秒
                            .setMinDuration(2000) //设置最小录制时长 单位毫秒
                            .setVideQuality(VideoQuality.HD) //设置视频质量
                            .setGop(125) //设置关键帧间隔
                            .build();

                    AliyunVideoRecorder.startRecordForResult(AddPaiActivity.this,103,recordParam);
                }else {
                    Utils.showToast(AddPaiActivity.this,"只能发布一个视频或九张图片");
                }
                break;
            case R.id.rl_add_rec_act://视频
                if (picList.size()==0 && TextUtils.isEmpty(videoPath)) {
                    Intent intentVideo = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intentVideo, 104);
                }else{
                    Utils.showToast(AddPaiActivity.this,"只能发布一个视频或九张图片");
                }
                break;
            case R.id.txt_cancel_pai://取消
                onBackPressed();
                break;
            case R.id.txt_push_pai://发布
                content = editText.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    Utils.showToast(AddPaiActivity.this,"请输入描述信息");
                    break;
                }
                if (TextUtils.isEmpty(videoPath) && picList.size()==0) {
                    Utils.showToast(AddPaiActivity.this,"请选择图片或视频文件");
                    break;
                }
                sendPai();
                break;
            case R.id.video_content_pai://播放

                break;
        }
    }

    private void sendPai() {
        mypDialog = new ProgressDialog(this);
        // 设置进度条风格，风格为圆形，旋转的
        mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // 设置ProgressDialog 标题
        mypDialog.setTitle("张承政务");
        // 设置ProgressDialog 提示信息
        mypDialog.setMessage("发布中，请稍等...");
        // 设置ProgressDialog 标题图标
        // mypDialog.setIcon(R.drawable.logo); //之后可以放logo
        // 设置ProgressDialog 的进度条是否不明确
        mypDialog.setIndeterminate(false);
        // 设置ProgressDialog 是否可以按退回按键取消
        mypDialog.setCancelable(false);
        mypDialog.show();

        String url = DemoApi.HOST+DemoApi.PAI_PUBLISH;
        postFormBuilder = OkHttpUtils.getInstance().post().url(url);

        postFormBuilder.addHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN);
        postFormBuilder.addParams("content",content);
        postFormBuilder.addParams("category",category);
        postFormBuilder.addParams("workType",workType);
        postFormBuilder.addParams("tag",tag);

        if (picList.size()>0) {
            fileList.clear();
            if (isFormal) {
                for (int i = 0; i < picList.size(); i++) {
                    postFormBuilder.addFile("pictures",new File(picList.get(i)).getName()+".jpg",new File(picList.get(i)));
                }
                postFormBuilder.build().execute(sendCallback);
            }else {
                for (int i = 0; i < picList.size(); i++) {
                    Luban.with(AddPaiActivity.this)
                            .load(new File(picList.get(i)))
                            .setCompressListener(compressListener)
                            .launch();
                }
            }
        }else {
            MediaPlayer mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(videoPath);
                mediaPlayer.prepare();
                s = mediaPlayer.getDuration();
            } catch (IOException e) {
                e.printStackTrace();
            }

            File file = new File(videoPath);

            postFormBuilder.addFile("video",file.getName()+".mp4",file);
            postFormBuilder.addFile("videoThumbnail",videoThumbnail.getName()+".png",videoThumbnail);
            postFormBuilder.addParams("videoFileLen",s+"");
            postFormBuilder.addParams("videoFileSize",file.length()+"");
            postFormBuilder.build().execute(sendCallback);
        }
    }

    private OnCompressListener compressListener = new OnCompressListener() {
        @Override
        public void onStart() {

        }

        @Override
        public void onSuccess(File file) {
            fileList.add(file);
            if (fileList.size() == picList.size()) {
                for (int i = 0; i < fileList.size(); i++) {
                    postFormBuilder.addFile("pictures",fileList.get(i).getName()+".jpg",fileList.get(i));
                }
                postFormBuilder.build().execute(sendCallback);
            }
        }

        @Override
        public void onError(Throwable e) {

        }
    };


    private Callback sendCallback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e) {

        }

        @Override
        public void onResponse(String response) {
            mypDialog.dismiss();
            mypDialog.hide();
            Bean bean = JsonUtils.jsonToPojo(response,Bean.class);
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(),0);
            if (bean != null && bean.getStatus() == 200) {
                Utils.showToast(AddPaiActivity.this,"发布成功");
                isSend = true;
                for (int i = 0; i < fileList.size(); i++) {
                    fileList.get(i).delete();
                }
                if (videoThumbnail != null) {
                    videoThumbnail.delete();
                }
                onBackPressed();
            }else {
                Utils.showToast(AddPaiActivity.this,"发布失败");
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 101://图库
                if (data != null) {
                    ArrayList<SelectPhotoAdapter.SelectPhotoEntity> selectedPhotos = data.getParcelableArrayListExtra("selectPhotos");
                    isFormal = data.getBooleanExtra("isFormal",false);
                    for (int i = 0; i < selectedPhotos.size(); i++) {
                        CustomGallery item = new CustomGallery();
                        item.sdcardPath = selectedPhotos.get(i).url;
                        adapter.add(item);
                    }

                    gridView.setVisibility(View.VISIBLE);
                    picList = adapter.getPath();
                }
                break;
            case 102://拍照
                if (RESULT_OK == resultCode ) {
                    // 将拍照的图片更新到创建动态中
                    CustomGallery item = new CustomGallery();
                    // item.sdcardPath = getRealPathFromURI(imageUri);
                    item.sdcardPath = filePath;
                    adapter.add(item);
                    gridView.setVisibility(View.VISIBLE);
                    picList = adapter.getPath();
                }
                break;
            case 103://摄像
                if(resultCode == Activity.RESULT_OK && data!= null){
                    int type = data.getIntExtra(AliyunVideoRecorder.RESULT_TYPE,0);
                    if(type ==  AliyunVideoRecorder.RESULT_TYPE_CROP){
                        videoPath = data.getStringExtra(AliyunVideoCrop.RESULT_KEY_CROP_PATH);
                    }else if(type ==  AliyunVideoRecorder.RESULT_TYPE_RECORD){
                        videoPath = data.getStringExtra(AliyunVideoRecorder.OUTPUT_PATH );
                    }
                    videoPhoto = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Images.Thumbnails.MINI_KIND);
                    videoPhoto = ThumbnailUtils.extractThumbnail(videoPhoto, 1080, 1920, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
                    videoThumbnail = saveBitmap(videoPhoto);
                    rlVideo.setVisibility(View.VISIBLE);
                    imgVideo.setImageBitmap(videoPhoto);
                    imgPlay.setImageResource(android.R.drawable.ic_media_play);
                }else if(resultCode == Activity.RESULT_CANCELED){
                    Toast.makeText(this,"用户取消录制",Toast.LENGTH_SHORT).show();
                }
                break;
            case 104://视频
                if (data != null && data.getData()!=null) {
                    Uri selectedVideo = data.getData();
                    String[] filePathColumn = { MediaStore.Video.Media.DATA };
                    Cursor cursor = getContentResolver().query(selectedVideo , filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    videoPath = cursor.getString(columnIndex);
                    cursor.close();
                    double fileSize = FileSizeUtil.getFileOrFilesSize(videoPath, 3);
                    if (fileSize <40) {
                        videoPhoto = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Images.Thumbnails.MINI_KIND);
                        videoPhoto = ThumbnailUtils.extractThumbnail(videoPhoto, 1080, 1920, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
                        videoThumbnail = saveBitmap(videoPhoto);
                        rlVideo.setVisibility(View.VISIBLE);
                        imgVideo.setImageBitmap(videoPhoto);
                        imgPlay.setImageResource(android.R.drawable.ic_media_play);
                    }else{
                        Toast.makeText(AddPaiActivity.this,"目标文件为"+fileSize+"Mb,超过默认40Mb限制，不支持上传",Toast.LENGTH_LONG).show();
                    }
                }
                break;
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

    @Override
    public void onBackPressed() {

        Intent intent = new Intent();
        intent.putExtra("isSend", isSend);//把获取到图片交给别的Activity
        setResult(103,intent);
        super.onBackPressed();
    }
}
