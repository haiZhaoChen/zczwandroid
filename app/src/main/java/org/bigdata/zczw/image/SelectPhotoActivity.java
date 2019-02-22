package org.bigdata.zczw.image;

import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.TextView;

import org.bigdata.zczw.App;
import org.bigdata.zczw.R;
import org.bigdata.zczw.ZCZWConstants;
import org.bigdata.zczw.utils.SPUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SelectPhotoActivity extends AppCompatActivity implements SelectPhotoAdapter.CallBackActivity,View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    SelectPhotoAdapter allPhotoAdapter =  null;
    private int num = 0;
    TextView tv_done;
    AlxPermissionHelper permissionHelper = new AlxPermissionHelper();
    ArrayList<SelectPhotoAdapter.SelectPhotoEntity> selectedPhotoList = null;//用于放置即将要发送的photo
    private List<AlbumBean> albumList = new ArrayList<>();//相册列表
    public static final int SELECT_PHOTO_OK = 20;//选择照片成功的result code
    private GridView gvPhotoList;

    private CheckBox checkBox;
    private boolean isFormal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photo);
        getSupportActionBar().hide();

        gvPhotoList = (GridView) findViewById(R.id.gv_photo);
        tv_done = (TextView) findViewById(R.id.tv_done);
        checkBox = (CheckBox) findViewById(R.id.check_box_photo_select);

        findViewById(R.id.tv_cancel).setOnClickListener(this);
        tv_done.setOnClickListener(this);
        checkBox.setOnCheckedChangeListener(this);
        isFormal = false;
        num = getIntent().getIntExtra("num",0);
        tv_done.setText("确定 "+num+"/9");
    }

    @Override
    protected void onStart() {
        super.onStart();
        initView();
    }


    private void initView(){
        allPhotoAdapter = new SelectPhotoAdapter(num,this, new ArrayList<SelectPhotoAdapter.SelectPhotoEntity>());
        gvPhotoList.setAdapter(allPhotoAdapter);

        //检查权限,获取权限之后将手机所有注册图片搜索出来，并按照相册进行分类
        permissionHelper.checkPermission(this, new AlxPermissionHelper.AskPermissionCallBack() {
            @Override
            public void onSuccess() {
                //扫描手机上500张最近注册过的图片
                SelectPhotoAdapter.get500PhotoFromLocalStorage(SelectPhotoActivity.this, new SelectPhotoAdapter.LookUpPhotosCallback() {
                    @Override
                    public void onSuccess(ArrayList<SelectPhotoAdapter.SelectPhotoEntity> photoArrayList) {
                        if(photoArrayList == null || photoArrayList.size() ==0)return;
//                        Log.i("Alex","查找500张图片成功,数量是"+photoArrayList.size());
                        allPhotoAdapter.allPhotoList.clear();
                        allPhotoAdapter.allPhotoList.addAll(photoArrayList);
                        allPhotoAdapter.notifyDataSetChanged();
                        //添加一个默认的相册用来存放这最近的500张图片
                        AlbumBean defaultAlbum = new AlbumBean();
                        defaultAlbum.albumFolder = Environment.getExternalStorageDirectory();
//                        Log.i("Alex","folder是"+defaultAlbum.albumFolder.getAbsolutePath());
                        defaultAlbum.topImagePath = photoArrayList.get(0).url;
                        defaultAlbum.imageCounts = photoArrayList.size();
                        defaultAlbum.folderName = "Recently";
                        albumList.add(0,defaultAlbum);
                    }
                });
            }

            @Override
            public void onFailed() {
                SelectPhotoActivity.this.finish();
            }
        });
    }

    @Override
    public void updateSelectActivityViewUI() {
        if (allPhotoAdapter.selectedPhotosSet != null && allPhotoAdapter.selectedPhotosSet.size()>0) {
            int code = allPhotoAdapter.selectedPhotosSet.size()+num;
            tv_done.setText("确定 "+code+"/9");
            tv_done.setClickable(true);
            tv_done.setEnabled(true);
        } else {
            tv_done.setClickable(false);
            tv_done.setEnabled(false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.registActivityResult(requestCode,permissions,grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
        super.onActivityResult(requestCode, resultCode, dataIntent);
        switch (requestCode) {
            case SelectPhotoAdapter.REQ_CAMARA: {//1000如果是相机发来的
                Log.i("Alex", "现在是相机拍照完毕");
                if (RESULT_OK == resultCode) {//系统默认值
                    String cameraPhotoUrl = SPUtil.getString(this, App.NAME_PATH);

                    File dir = new File(ZCZWConstants.STORAGE_IMAGE_PATH);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }

                    File file = new File(dir, cameraPhotoUrl);//这个文件指针指向拍好的图片文件
                    final String imageFilePath = file.getAbsolutePath();
//                    Log.i("Alex", "拍摄图片暂存到了" + imageFilePath + "  角度是" + AlxImageLoader.readPictureDegree(imageFilePath));
                    //把图片压缩成指定宽高并且保存到本地
                    file = new File(imageFilePath);
                    String albumUrl = null;
                    if (!file.exists()) break;
//                    Log.i("Alex", "准备存储到相册");
                    try {
                        ContentResolver cr = SelectPhotoActivity.this.getContentResolver();
                        //在往相册存储的时候返回url是DCIM的url，不是原来的了，而且exif信息也全都没了
                        // /storage/sdcard0/Alex/camera/1461321361499.jpg
                        // /storage/sdcard0/DCIM/Camera/1461321370065.jpg
                        //下面这句是把相机返回的文件拷贝到系统相册里面去,并且生产缩略图存在相册里，然后发送广播更新图片list
                        albumUrl = AlxBitmapUtils.insertImage(this, cr, file, true);//返回值为要发送图片的url
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // 提交数据，和选择图片用的同一个ArrayList
                    SelectPhotoAdapter.SelectPhotoEntity photoEntity = new SelectPhotoAdapter.SelectPhotoEntity();
                    //因为存储到相册之后exif全都没了，所以应该传源文件的路径
                    photoEntity.url = albumUrl;
                    if(selectedPhotoList == null)selectedPhotoList = new ArrayList<>(1);
                    selectedPhotoList.add(photoEntity);
                    Intent intent = new Intent();
                    intent.putExtra("selectPhotos", selectedPhotoList);//把获取到图片交给别的Activity
                    intent.putExtra("isFromCamera", true);
                    setResult(SELECT_PHOTO_OK, intent);
                    finish();
                    break;
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_done:
                if(selectedPhotoList == null)selectedPhotoList = new ArrayList<>(9);
                for(SelectPhotoAdapter.SelectPhotoEntity p:allPhotoAdapter.selectedPhotosSet)
                    selectedPhotoList.add(p);
                Intent intent = new Intent();
                intent.putExtra("selectPhotos", selectedPhotoList);//把获取到图片交给别的Activity
                intent.putExtra("isFromCamera", false);
                intent.putExtra("isFormal", isFormal);
                setResult(SELECT_PHOTO_OK,intent);
                finish();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        isFormal = isChecked;
    }
}
