package org.bigdata.zczw.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import org.bigdata.zczw.App;
import org.bigdata.zczw.R;
import org.bigdata.zczw.ZCZWConstants;
import org.bigdata.zczw.entity.Bean;
import org.bigdata.zczw.entity.User;
import org.bigdata.zczw.fragment.ImageDetailFragment;
import org.bigdata.zczw.ui.HackyViewPager;
import org.bigdata.zczw.ui.WinToast;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.CommonUtils;
import org.bigdata.zczw.utils.DemoApi;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.SPUtil;
import org.bigdata.zczw.utils.Utils;
import org.json.JSONException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;
import okhttp3.Call;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;
/*
* 用户头像修改
* */
public class PortraitActivity extends AppCompatActivity{

    private HackyViewPager mPager;

    private String filepath = "";
    private String takePhoto = "";
    public static final int TAKE_PHOTO = 1;  //拍照
    public static final int SELECT_IMG  = 2;   //相册
    public static final int CROP_IMG  = 3; //结果

    private final String ACTION_NAME = "image/changeImg";
    private CharSequence[] items = {"拍照", "从相册中选择"};
    //头像是否修改成功标志
    private boolean modifyFlag = false;
    private Uri cropUri;
    private ArrayList<String> urls;
    private ImagePagerAdapter mAdapter;
    private String imgPath;
    private PostFormBuilder postFormBuilder;
    private ProgressDialog mypDialog;


    public Uri imageUriFromCamera;
    public Uri cropImageUri;
    public final String USER_IMAGE_NAME = "image.png";
    public final String USER_CROP_IMAGE_NAME = "temporary.png";
    public final int GET_IMAGE_BY_CAMERA_U = 5001;
    public final int CROP_IMAGE_U = 5003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portrait);
        AppManager.getAppManager().addActivity(this);
        initView();
    }

    private void initView() {
        getSupportActionBar().setTitle("头像");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);


        urls = new ArrayList<>();
        String img = SPUtil.getString(this, App.IMAGE_POSITION, "");
        if (!TextUtils.isEmpty(img) && img.length()>5) {
            urls.add(img);
        }else {
            urls.add("http://zczw.ewonline.org:8093/images/de_default_portrait.png");
        }

        mPager = (HackyViewPager) findViewById(R.id.pager_portrait);
        mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), urls);
        mPager.setAdapter(mAdapter);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private Bitmap head;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case SELECT_IMG:
                if (null != data) {//为了取消选取不报空指针用的
                    Uri uri = data.getData();
                    cropPhoto(uri);
                }
                break;
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    File temp = new File(takePhoto);
                    cropPhoto(Uri.fromFile(temp));// 裁剪图片
                }
                break;
            case CROP_IMG:
                if (data == null) {
                    return;
                }

                try {
                    head = BitmapFactory.decodeStream(getContentResolver().openInputStream(cropUri));
                    if (head != null) {
                        Bitmap ss = comp(head);
                        try {
                            commitUserInfo();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;

            case GET_IMAGE_BY_CAMERA_U:
                    /*
                    * 这里我做了一下调用系统切图，高版本也有需要注意的地方
                    * */
                if (imageUriFromCamera != null) {
                    cropImage(imageUriFromCamera, 1, 1, CROP_IMAGE_U);
                    break;
                }
                break;
            case CROP_IMAGE_U:
                try {
                    head = BitmapFactory.decodeStream(getContentResolver().openInputStream(cropImageUri));
                    if (head != null) {
                        try {
                            commitUserInfo();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void cropPhoto(Uri uri) {
        String filename = CommonUtils.getCurrentTime() + "pic.jpg";
        File file = new File(ZCZWConstants.STORAGE_IMAGE_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        File image = new File(ZCZWConstants.STORAGE_IMAGE_PATH, filename);
        filepath = image.getAbsolutePath();
        cropUri = Uri.fromFile(image);
        Intent intent = new Intent("com.android.camera.action.CROP");

        if (Build.VERSION.SDK_INT >= 24) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 400);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG);
        intent.putExtra("noFaceDetection", false); // no face detection

        //将存储图片的uri读写权限授权给剪裁工具应用
        List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        startActivityForResult(intent, CROP_IMG);
    }

    public void cropImage(Uri imageUri, int aspectX, int aspectY,
                          int return_flag) {
        File file = new File(this.getExternalCacheDir(), USER_CROP_IMAGE_NAME);
        filepath = file.getAbsolutePath();
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= 24) {
            //高版本一定要加上这两句话，做一下临时的Uri
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            FileProvider.getUriForFile(PortraitActivity.this, "org.bigdata.zczw.FileProvider", file);
        }
        cropImageUri = Uri.fromFile(file);

        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropImageUri);

        startActivityForResult(intent, return_flag);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            //手机相册中选择
            case R.id.change_item2:
                intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, SELECT_IMG);
                break;
            //拍照
            case R.id.change_item1:

                if (Build.VERSION.SDK_INT >= 24) {  // 或者 android.os.Build.VERSION_CODES.KITKAT这个常量的值是19
                    Intent intent0 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File photoFile = createImagePathFile(PortraitActivity.this);
                    intent0.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

                    /*
                      * 这里就是高版本需要注意的，需用使用FileProvider来获取Uri，同时需要注意getUriForFile
                      * 方法第二个参数要与AndroidManifest.xml中provider的里面的属性authorities的值一致
                     * */
                    intent0.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    imageUriFromCamera = FileProvider.getUriForFile(PortraitActivity.this,
                            "org.bigdata.zczw.FileProvider", photoFile);
                    intent0.putExtra(MediaStore.EXTRA_OUTPUT, imageUriFromCamera);

                    startActivityForResult(intent0, GET_IMAGE_BY_CAMERA_U);

                } else {
                    imageUriFromCamera = createImagePathUri(PortraitActivity.this);
                    Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent1.putExtra(MediaStore.EXTRA_OUTPUT,
                            imageUriFromCamera);
                    startActivityForResult(intent1, GET_IMAGE_BY_CAMERA_U);
                }


//                if (!CommonUtils.isSDCardExist()) {
//                    //SD卡不存在则提示不能拍照
//                    Toast.makeText(PortraitActivity.this, "SD卡不存在", Toast.LENGTH_LONG).show();
//                    break;
//                }
//                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                String filename = CommonUtils.getCurrentTime() + ".jpg";
//                FileBean file = new FileBean(ZCZWConstants.STORAGE_IMAGE_PATH);
//                if (!file.exists()) {
//                    file.mkdirs();
//                }
//                FileBean image = new FileBean(ZCZWConstants.STORAGE_IMAGE_PATH, filename);
//                takePhoto = image.getAbsolutePath();
//
//                Uri imgUri = Uri.fromFile(image);
//
//                /*获取当前系统的android版本号*/
//                int currentApiVersion = android.os.Build.VERSION.SDK_INT;
//                if (currentApiVersion<24){
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT,imgUri);
//                    startActivityForResult(intent, TAKE_PHOTO);
//                }else {
//                    ContentValues contentValues = new ContentValues(1);
//                    contentValues.put(MediaStore.Images.Media.DATA, takePhoto);
//                    Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//                    startActivityForResult(intent, TAKE_PHOTO);
//                }

                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.de_userportrait_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public Uri createImagePathUri(Activity activity) {
        //文件目录可以根据自己的需要自行定义
        Uri imageFilePath;
        File file = new File(activity.getExternalCacheDir(), USER_IMAGE_NAME);
        imageFilePath = Uri.fromFile(file);
        return imageFilePath;
    }
    public File createImagePathFile(Activity activity) {
        //文件目录可以根据自己的需要自行定义
        Uri imageFilePath;
        File file = new File(activity.getExternalCacheDir(), USER_IMAGE_NAME);
        imageFilePath = Uri.fromFile(file);
        return file;
    }

    /**
     * 提交用户数据
     *
     * @throws UnsupportedEncodingException
     * @throws JSONException
     */
    public void commitUserInfo() throws UnsupportedEncodingException, JSONException {
        // 如果没有网络则返回
        if (!CommonUtils.isNetworkConnected(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), ZCZWConstants.NETWORK_INVALID, Toast.LENGTH_SHORT).show();
            return;
        }
        mypDialog = new ProgressDialog(this);
        // 设置进度条风格，风格为圆形，旋转的
        mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // 设置ProgressDialog 标题
        mypDialog.setTitle("张承政务");
        // 设置ProgressDialog 提示信息
        mypDialog.setMessage("正在保存修改");
        // 设置ProgressDialog 标题图标
        // mypDialog.setIcon(R.drawable.logo); //之后可以放logo
        // 设置ProgressDialog 的进度条是否不明确
        mypDialog.setIndeterminate(false);
        // 设置ProgressDialog 是否可以按退回按键取消
        mypDialog.setCancelable(true);
        mypDialog.show();


        String url = DemoApi.HOST + ACTION_NAME;
        String userId = SPUtil.getString(PortraitActivity.this, App.USER_ID);
        postFormBuilder = OkHttpUtils.getInstance().post().url(url);

        postFormBuilder.addHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN);
        postFormBuilder.addParams("userId", userId);
        Luban.with(PortraitActivity.this)
                .load(new File(filepath))
                .setCompressListener(compressListener)
                .launch();

    }

    private OnCompressListener compressListener = new OnCompressListener() {
        @Override
        public void onStart() {

        }

        @Override
        public void onSuccess(File file) {
            postFormBuilder.addFile("uploadFile",file.getName()+".jpg",file);
            postFormBuilder.build().execute(sendCallback);
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

            if (bean != null && bean.getStatus() == 200) {
                imgPath = DemoApi.IMAGE + bean.getData();
                SPUtil.remove(PortraitActivity.this, App.IMAGE_POSITION);
                SPUtil.put(PortraitActivity.this, App.IMAGE_POSITION, imgPath);
                User user = new User();
                user.setUserid(Long.getLong(SPUtil.getString(PortraitActivity.this,App.USER_ID)));
                user.setUsername(SPUtil.getString(PortraitActivity.this, App.USER_NAME));
                user.setImagePosition(imgPath);
                RongIM.getInstance().refreshUserInfoCache(new UserInfo(user.getUserid()+"",user.getUsername(),Uri.parse(user.getImagePosition())));

                WinToast.toast(getApplicationContext(), "修改成功");
                urls.clear();
                urls.add(imgPath);
                mPager = (HackyViewPager) findViewById(R.id.pager_portrait);
                mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), urls);
                mPager.setAdapter(mAdapter);

                modifyFlag = true;
                // 删除拍照的图片
                File file = new File(filepath);
                if (file.exists()) {
                    file.delete();
                }
            }else {
                Utils.showToast(PortraitActivity.this,"发布失败");
            }
        }
    };

    public static Bitmap comp(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        float ww = 480f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;// 降低图片从ARGB888到RGB565
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }
    public  static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            options -= 10;// 每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中

        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {

        public ArrayList<String> fileList;

        public ImagePagerAdapter(FragmentManager fm, ArrayList<String> fileList) {
            super(fm);
            this.fileList = fileList;
        }

        @Override
        public int getCount() {
            return fileList == null ? 0 : fileList.size();
        }

        @Override
        public Fragment getItem(int position) {
            String url = fileList.get(position);
            return ImageDetailFragment.newInstance(url);
        }
    }
}
