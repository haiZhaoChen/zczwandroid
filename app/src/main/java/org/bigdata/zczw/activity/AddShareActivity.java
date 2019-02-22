package org.bigdata.zczw.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import org.bigdata.zczw.App;
import org.bigdata.zczw.R;
import org.bigdata.zczw.ZCZWConstants;
import org.bigdata.zczw.entity.Bean;
import org.bigdata.zczw.entity.Record;
import org.bigdata.zczw.ui.WinToast;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.CommonUtils;
import org.bigdata.zczw.utils.DemoApi;
import org.bigdata.zczw.utils.EmojiFilter;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.Utils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/*
* 分享到张承
* */

public class AddShareActivity extends AppCompatActivity implements BDLocationListener {

    private EditText editText;
    private TextView recordContent,txtLocation,txtType;
    private ImageView recordImg;

    private LocationClient mLocationClient;
    private double longitude;// 经度
    private double latitude;// 纬度
    private String location = "未定位";//地理位置

    private String content;

    private String type;
    private String userIds = "";
    private String userNames;
    private String publicScope;
    private Record record;

    private ProgressDialog mypDialog;
    private PostFormBuilder postFormBuilder;
    static Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_share);
        getSupportActionBar().setLogo(R.drawable.empty_logo);// actionbar 添加logo
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);
        getSupportActionBar().setTitle("转发动态");
        AppManager.getAppManager().addActivity(this);

        initView();
        initData();
        initLocation();
    }

    private void initView() {
        editText = (EditText) findViewById(R.id.edit_content_share_frg);
        recordImg = (ImageView) findViewById(R.id.img_share_frg);

        recordContent = (TextView) findViewById(R.id.txt_share_content_frg);
        txtLocation = (TextView) findViewById(R.id.txt_location_share_act);
        txtType = (TextView) findViewById(R.id.txt_type_share_act);
    }

    private void initData() {
        type = getIntent().getStringExtra("type");
        publicScope = getIntent().getStringExtra("publicScope");
        record = (Record) getIntent().getSerializableExtra("record");
        if (type.equals("1")) {
            userIds = getIntent().getStringExtra("ids");
            userNames = getIntent().getStringExtra("names");
            txtType.setText("本部门可见");
        }else if (type.equals("2")) {
            userIds = getIntent().getStringExtra("ids");
            userNames = getIntent().getStringExtra("names");
            txtType.setText("指定人员可见");
        }else {
            txtType.setText("所有人可见");
        }

        recordContent.setText(record.getContent());

        if (record.getPictures()!= null && record.getPictures().size()>0) {
            Picasso.with(AddShareActivity.this).load(record.getPictures().get(0).getUrl()).into(recordImg);
        }else {
            recordImg.setVisibility(View.GONE);
        }
    }


    private void initLocation() {
// 定位初始化
        mLocationClient = new LocationClient(getApplicationContext());
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

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if (bdLocation == null) {
            return;
        }
        String str = "";
        latitude = bdLocation.getLatitude();
        longitude = bdLocation.getLongitude();
        if (!TextUtils.isEmpty(bdLocation.getAddrStr()) && !TextUtils.isEmpty(bdLocation.getLocationDescribe())) {
            location = bdLocation.getAddrStr()+bdLocation.getLocationDescribe();
            str = bdLocation.getCity()+bdLocation.getDistrict();
        }
        if (TextUtils.isEmpty(location) || location == null) {
            location = "未定位";
            txtLocation.setText(location);
        }else {
            txtLocation.setText(str);
            mLocationClient.stop();
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
                content = editText.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    Utils.showToast(this,"请输入内容");
                    return super.onOptionsItemSelected(item);
                }
                createShare();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createShare() {
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
        mypDialog.setMessage("动态发布中，请稍等...");
        // 设置ProgressDialog 标题图标
        // mypDialog.setIcon(R.drawable.logo); //之后可以放logo
        // 设置ProgressDialog 的进度条是否不明确
        mypDialog.setIndeterminate(false);
        // 设置ProgressDialog 是否可以按退回按键取消
        mypDialog.setCancelable(false);

        new Thread(new Runnable() {

            @Override
            public void run() {

                HttpURLConnection connection = null;
                DataOutputStream outputStream = null;
                ByteArrayInputStream inputStream = null;
                String urlServer = DemoApi.HOST + "message/add";
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";

                Map<String, Object> params = new HashMap<String, Object>();
                //params.put("userId", userId);
                // params.put("messageId", userId);
                params.put("content", EmojiFilter.filterEmoji(content));
                params.put("latitude", latitude);
                params.put("longitude", longitude);
                params.put("location", location);
                params.put("publicScope", publicScope);
                if (record.getForwardMessage() != null) {
                    params.put("forwardMessageId", record.getForwardMessage().getMessageId()+"");
                }else {
                    params.put("forwardMessageId", record.getMessageId()+"");
                }

                if (type.equals("0")) {
                    params.put("userIds", "");
                }else {
                    params.put("userIds", userIds);
                }

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
                                    Toast.makeText(AddShareActivity.this, "发布动态失败，服务器异常", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        if (returnInfo != null) {

                            int info = returnInfo.getInt("status");
                            if (200 == info) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mypDialog.dismiss();
                                        Toast.makeText(AddShareActivity.this, "动态发布成功", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });

                            } else if (400 == info) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(AddShareActivity.this, "动态创建失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    } else if (!CommonUtils.isNetworkConnected(getApplicationContext())) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AddShareActivity.this, ZCZWConstants.NETWORK_INVALID, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AddShareActivity.this, "创建动态失败，服务器异常", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            WinToast.makeText(AddShareActivity.this, "图片不存在，请重新选择");
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

    private Callback sendCallback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e) {
            mypDialog.dismiss();
            mypDialog.hide();
            Log.e("111", "onError: "+e);
            Utils.showToast(AddShareActivity.this,"网络异常");
        }

        @Override
        public void onResponse(String response) {
            mypDialog.dismiss();
            mypDialog.hide();
            Bean bean = JsonUtils.jsonToPojo(response,Bean.class);
            if (bean != null && bean.getStatus() == 200) {
                Utils.showToast(AddShareActivity.this,"发布成功");
                finish();
            }else {
                Utils.showToast(AddShareActivity.this,"发布失败");
            }
        }
    };

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

}
