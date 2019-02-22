package org.bigdata.zczw.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.bigdata.zczw.App;
import org.bigdata.zczw.R;
import org.bigdata.zczw.adapter.AttendAdapter;
import org.bigdata.zczw.adapter.CheckDateAdapter;
import org.bigdata.zczw.adapter.CheckNoteAdapter;
import org.bigdata.zczw.entity.AttendDateBean;
import org.bigdata.zczw.entity.AttendStatus;
import org.bigdata.zczw.entity.AttendTypeCount;
import org.bigdata.zczw.entity.Attendance;
import org.bigdata.zczw.entity.AttendanceBean;
import org.bigdata.zczw.entity.AttendanceHistory;
import org.bigdata.zczw.entity.Bean;
import org.bigdata.zczw.entity.BeanLong;
import org.bigdata.zczw.entity.CheckNote;
import org.bigdata.zczw.entity.CheckNoteBean;
import org.bigdata.zczw.entity.CheckStatus;
import org.bigdata.zczw.entity.HistoryBean;
import org.bigdata.zczw.ui.CustomRadioGroup;
import org.bigdata.zczw.ui.DateScrollGridView;
import org.bigdata.zczw.ui.NoScrollListView;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.SPUtil;
import org.bigdata.zczw.utils.ServerUtils;
import org.bigdata.zczw.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.TreeMap;

/*
* 签到
* */

public class CheckActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, BDLocationListener {

    private ScrollView scrollView;

    private TextView unCheck;
    private TextView btnCheck;
    //添加备注
    private RelativeLayout relativeLayout;
    private EditText editText;
    private TextView btnOk;

    private TextView qingJia,tiaoxiu,xiaoJia;
    private ImageView imgQingJia,imgTiaoXiu,imgXiaoJia;
    //在岗状态
    private LinearLayout llTitleType;
    private TextView llTxtType,llTxtTime;
    //请假状态
    private RelativeLayout rlTitleType;
    private TextView rlTxtType,rlTxtTime,txtMonth;
    //头部四个按钮
    private LinearLayout btnNote,btnQingJia,btnTiaoXiu,btnXiaoJia;
    //备注列表
    private LinearLayout llNotes;
    private NoScrollListView noteListView;
    //签到统计
    private TextView txtZaiGang,txtShangLu,txtChuChai,txtXiuJia,txtTiaoXiu,txtQingJia,txtQiTa;
    private View black;

    private TextView txtDate;
    private ImageView monthLeft,monthRight;
    private DateScrollGridView gridView;

    private ArrayList<AttendTypeCount> countList;
    private ArrayList<Attendance> attendanceList;
    private AttendAdapter attendAdapter;

    private String id;
    private int year;
    private int month;
    private String monthDate;
    private AttendStatus status;


    private String[] checkType = new String[]{"","未签到","在岗","上路","出差","休假","调休","请假","其他"};
    private String[] nightType = new String[]{"中班","白班","夜班","夜班/中班"};
    private String[] leaveType = new String[]{"事假","病假","年休假","婚假","预产假","产假","陪产假","哺乳假","延长哺乳假","丧假","工伤"};
    private int attendSubType;
    private int attendType;
    private String otherTypeName;
    private String nowDate;
    private boolean isBackNormal;
    private Attendance attendance;

    private ArrayList<CheckNote> checkNoteList;
    private ArrayList<CheckNote> noteList;
    private CheckNoteAdapter checkNoteAdapter;
    private ProgressDialog progressDialog;

    private PopupWindow tipWindow;
    private View tipView;

    private LocationClient mLocationClient;
    private double longitude;// 经度
    private double latitude;// 纬度
    private String location = "未定位";//地理位置
    private NoScrollListView lvCheck;
    private ArrayList<AttendanceHistory> historyArrayList;
    private String strDate;

    private ArrayList<Object> allArrayList;
    private CheckDateAdapter checkDateAdapter;
    private TreeMap<Long, Object> treeMap;
    private Attendance dateAttendance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        AppManager.getAppManager().addActivity(this);

        getSupportActionBar().setTitle("签到");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);

        progressDialog = new ProgressDialog(CheckActivity.this);
        progressDialog.setMessage("正在加载中，请稍等......");//3.设置显示内容
        progressDialog.setCancelable(true);//4.设置可否用back键关闭对话框
        progressDialog.show();//5.将ProgressDialog显示出来

        initView();
        initData();
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(CheckActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(CheckActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }else {
                initMyLocation();//定位
            }
        }else {
            initMyLocation();//定位
        }

    }

    @Override
    protected void onResume() {
        gridView.setFocusable(false);
        scrollView.setFocusable(true);
        scrollView.setFocusableInTouchMode(true);
        scrollView.requestFocus();
        super.onResume();
    }

    private void initView() {
        scrollView = (ScrollView) findViewById(R.id.scroll_view_check_act);

        txtDate = (TextView) findViewById(R.id.date_text_one);
        monthLeft = (ImageView) findViewById(R.id.iv_left_one);
        monthRight = (ImageView) findViewById(R.id.iv_right_one);

        qingJia = (TextView) findViewById(R.id.txt_qingjia);
        imgQingJia = (ImageView) findViewById(R.id.img_qingjia);
        tiaoxiu = (TextView) findViewById(R.id.txt_tiaoxiu);
        imgTiaoXiu = (ImageView) findViewById(R.id.img_tiaoxiu);
        xiaoJia = (TextView) findViewById(R.id.txt_xiaojia);
        imgXiaoJia = (ImageView) findViewById(R.id.img_xiaojia);

        gridView = (DateScrollGridView) findViewById(R.id.date_grid_check_act);

        llTitleType = (LinearLayout) findViewById(R.id.ll_type_check_act);

        btnNote = (LinearLayout) findViewById(R.id.ll_note_check_act);
        btnQingJia = (LinearLayout) findViewById(R.id.ll_qingjia_check_act);
        btnTiaoXiu = (LinearLayout) findViewById(R.id.ll_tiaoxiu_check_act);
        btnXiaoJia = (LinearLayout) findViewById(R.id.ll_xiaojia_check_act);
        llNotes = (LinearLayout) findViewById(R.id.ll_check_note_check_act);

        noteListView = (NoScrollListView) findViewById(R.id.note_list_check_act);

        rlTitleType = (RelativeLayout) findViewById(R.id.rl_check_type_check_act);

        unCheck = (TextView) findViewById(R.id.txt_unCheck_check_act);
        btnCheck = (TextView) findViewById(R.id.btn_add_check_act);
        llTxtType = (TextView) findViewById(R.id.txt_title_type_check_act);
        llTxtTime = (TextView) findViewById(R.id.txt_title_time_check_act);
        rlTxtType = (TextView) findViewById(R.id.txt_type_check_act);
        rlTxtTime = (TextView) findViewById(R.id.txt_check_time);

        txtZaiGang = (TextView) findViewById(R.id.txt_zaigang_num_check_act);
        txtShangLu = (TextView) findViewById(R.id.txt_shanglu_num_check_act);
        txtChuChai = (TextView) findViewById(R.id.txt_chuchai_num_check_act);
        txtXiuJia = (TextView) findViewById(R.id.txt_xiujia_num_check_act);
        txtTiaoXiu = (TextView) findViewById(R.id.txt_tiaoxiu_num_check_act);
        txtQingJia = (TextView) findViewById(R.id.txt_qingjia_num_check_act);
        txtQiTa = (TextView) findViewById(R.id.txt_qita_num_check_act);

        relativeLayout = (RelativeLayout) findViewById(R.id.rl_edit_check_act);
        editText = (EditText) findViewById(R.id.edit_check_act);
        btnOk = (TextView) findViewById(R.id.txt_ok_check_act);

        txtMonth = (TextView) findViewById(R.id.txt_month);

        black = findViewById(R.id.view_black);

        unCheck.setVisibility(View.VISIBLE);
        btnCheck.setVisibility(View.VISIBLE);
        llTitleType.setVisibility(View.GONE);
        rlTitleType.setVisibility(View.GONE);

        monthLeft.setOnClickListener(this);
        monthRight.setOnClickListener(this);

        btnCheck.setOnClickListener(this);
        btnNote.setOnClickListener(this);
        btnQingJia.setOnClickListener(this);
        btnTiaoXiu.setOnClickListener(this);
        btnXiaoJia.setOnClickListener(this);
        llTitleType.setOnClickListener(this);
        black.setOnClickListener(this);
        btnOk.setOnClickListener(this);

        gridView.setOnItemClickListener(this);
    }

    private void initData() {
        id = SPUtil.getString(CheckActivity.this, App.USER_ID);
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        if (month >9) {
            txtDate.setText(year +"年"+ month +"月");
        }else {
            txtDate.setText(year +"年0"+ month +"月");
        }

        attendSubType = 0;
        attendType = 1;
        otherTypeName = "";

        countList = new ArrayList<>();
        attendanceList = new ArrayList<>();
        allArrayList = new ArrayList<>();
        checkNoteList = new ArrayList<>();
        historyArrayList = new ArrayList<>();

        SimpleDateFormat spt = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat spt1 = new SimpleDateFormat("yyyy年MM月签到统计");
        nowDate = spt.format(new Date());
        txtMonth.setText(spt1.format(new Date()));
        ServerUtils.checkStatus(callBack);
    }


    /**
     * 初始化定位相关代码
     */
    private void initMyLocation() {
        mLocationClient = new LocationClient(getApplicationContext());
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
        }
        Log.e("111", "onReceiveLocation: "+location );
        mLocationClient.stop();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 101://请假
                ServerUtils.checkStatus(callBack);
                break;
            case 102://调休
                ServerUtils.checkStatus(callBack);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_left_one:
                if(month == 1){//若果是1月份，则变成12月份
                    year = year -1;
                    month = 12;
                }else{
                    month = month -1;
                }
                if (month >9) {
                    txtDate.setText(year +"年"+ month +"月");
                }else {
                    txtDate.setText(year +"年0"+ month +"月");
                }
                if (month >9) {
                    monthDate = year +"-"+ month;
                }else {
                    monthDate = year +"-0"+ month;
                }

                if (!TextUtils.isEmpty(id)) {
                    ServerUtils.attendUser(id, monthDate,attendUserCallBack);
                }
                break;
            case R.id.iv_right_one:
                if(month == 12){//若果是12月份，则变成1月份
                    year = year +1;
                    month = 1;
                }else{
                    month = month + 1;
                }
                if (month >9) {
                    txtDate.setText(year +"年"+ month +"月");
                }else {
                    txtDate.setText(year +"年0"+ month +"月");
                }
                if (month >9) {
                    monthDate = year +"-"+ month;
                }else {
                    monthDate = year +"-0"+ month;
                }
                if (!TextUtils.isEmpty(id)) {
                    ServerUtils.attendUser(id, monthDate,attendUserCallBack);
                }
                break;
            case R.id.btn_add_check_act://签到
            case R.id.ll_type_check_act://修改
                if (status != null && status.isNeedXiaoJia()) {
                    Utils.showToast(this,"您还有请假未销，请及时销假。");
                }else {
                    if (status != null && status.isIsNightShift()) {
                        showNightCheckDialog();
                    }else {
                        showCheckDialog();
                    }
                }

                break;
            case R.id.ll_note_check_act://备注
                if (attendance.getAttendType()>0) {
                    if (noteList == null || noteList.size()<5) {
                        relativeLayout.setVisibility(View.VISIBLE);
                        relativeLayout.setClickable(true);
                        editText.setFocusable(true);
                        editText.setFocusableInTouchMode(true);
                        editText.requestFocus();
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    }else {
                        Utils.showToast(this,"最多发布五条备注");
                    }
                }else {
                    Utils.showToast(this,"暂未签到");
                }

                break;
            case R.id.view_black://备注
                relativeLayout.setVisibility(View.GONE);
                break;
            case R.id.txt_ok_check_act://添加备注
                String noteContent = editText.getText().toString().trim();
                if (TextUtils.isEmpty(noteContent)) {
                    Utils.showToast(this,"请输入详情");
                    return;
                }
                Date date = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String str = simpleDateFormat.format(date);
                ServerUtils.attendRemark(str,noteContent,remarkCallBack);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(),0);
                relativeLayout.setVisibility(View.GONE);
                editText.setText("");
                break;
            case R.id.ll_qingjia_check_act://请假
                if (!status.isNeedXiaoJia()) {
                    Intent intent = new Intent(CheckActivity.this,LeaveActivity.class);
                    intent.putExtra("latitude",latitude);
                    intent.putExtra("longitude",longitude);
                    intent.putExtra("location",location);
                    startActivityForResult(intent,101);
                }
                break;
            case R.id.ll_tiaoxiu_check_act://调休
                if (!status.isNeedXiaoJia()) {
                    Intent intent = new Intent(CheckActivity.this,TiaoXiuActivity.class);
                    intent.putExtra("latitude",latitude);
                    intent.putExtra("longitude",longitude);
                    intent.putExtra("location",location);
                    startActivityForResult(intent,102);
                }
                break;
            case R.id.ll_xiaojia_check_act://销假
                if (status != null && status.isNeedXiaoJia()) {
                    showXiaoJiaDialog();
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (attendanceList.get(position).getAttendType()>0) {
            showTipDialog(attendanceList.get(position));
        }
    }

    private RequestCallBack<String> remarkCallBack = new RequestCallBack<String>() {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            BeanLong bean = JsonUtils.jsonToPojo(json,BeanLong.class);
            if (bean != null && bean.getStatus() == 200) {
                if (bean.getData()>0) {
                    ServerUtils.attendRemarkList(id,nowDate,remarkListCallBack);
                }else {
                    Utils.showToast(CheckActivity.this,"添加失败");
                }
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
        }
    };

    private RequestCallBack<String> remarkListCallBack = new RequestCallBack<String>() {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            CheckNoteBean bean = JsonUtils.jsonToPojo(json,CheckNoteBean.class);
            if (bean != null && bean.getStatus() == 200) {
                if (bean.getData()!=null && bean.getData().size()>0) {
                    llNotes.setVisibility(View.VISIBLE);
                    noteList = (ArrayList<CheckNote>) bean.getData();
                    checkNoteAdapter = new CheckNoteAdapter(CheckActivity.this,noteList);
                    noteListView.setAdapter(checkNoteAdapter);
                }
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
        }
    };

    private RequestCallBack<String> popRemarkCallBack = new RequestCallBack<String>() {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            CheckNoteBean bean = JsonUtils.jsonToPojo(json,CheckNoteBean.class);
            if (bean != null && bean.getStatus() == 200) {
                checkNoteList.clear();
                if (bean.getData()!=null && bean.getData().size()>0) {
                    checkNoteList = (ArrayList<CheckNote>) bean.getData();
                }
            }

            ServerUtils.checksList(id,strDate,checksCallBack);
        }

        @Override
        public void onFailure(HttpException e, String s) {
        }
    };

    private RequestCallBack<String> checksCallBack = new RequestCallBack<String>() {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            HistoryBean bean = JsonUtils.jsonToPojo(json,HistoryBean.class);
            if (bean != null && bean.getStatus() == 200) {
                historyArrayList.clear();
                if (bean.getData()!=null && bean.getData().size()>0) {
                    historyArrayList = (ArrayList<AttendanceHistory>) bean.getData();
                }

                for (int i = 0; i < checkNoteList.size(); i++) {
                    treeMap.put(checkNoteList.get(i).getCreateDate(),checkNoteList.get(i));
                }
                for (int i = 0; i < historyArrayList.size(); i++) {
                    if (i == historyArrayList.size()-1) {
                        historyArrayList.get(i).setDate(dateAttendance.getCreateDate());
                    }else {
                        historyArrayList.get(i).setDate(historyArrayList.get(i+1).getCreateDate());
                    }

                    treeMap.put(historyArrayList.get(i).getDate(),historyArrayList.get(i));
                }
                allArrayList.clear();
                allArrayList.addAll(treeMap.values());
                checkDateAdapter = new CheckDateAdapter(CheckActivity.this,dateAttendance,allArrayList);
                lvCheck.setAdapter(checkDateAdapter);
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
        }
    };

    private RequestCallBack<String> xiaoJiaCallBack = new RequestCallBack<String>() {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            Bean bean = JsonUtils.jsonToPojo(json,Bean.class);
            if (bean != null && bean.getStatus() == 200) {
               Utils.showToast(CheckActivity.this,"销假成功");
                ServerUtils.checkStatus(callBack);
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
        }
    };

    private RequestCallBack<String> attendCallBack = new RequestCallBack<String>() {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            Bean bean = JsonUtils.jsonToPojo(json,Bean.class);
            if (bean != null && bean.getStatus() == 200) {
                Utils.showToast(CheckActivity.this,"签到成功");
                ServerUtils.checkStatus(callBack);
            }else {
                Utils.showToast(CheckActivity.this,"操作失败，请稍后再试。");
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
            Utils.showToast(CheckActivity.this,"操作失败，请稍后再试。");
        }
    };

    private RequestCallBack<String> callBack = new RequestCallBack<String>() {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            CheckStatus bean = JsonUtils.jsonToPojo(json,CheckStatus.class);
            if (bean != null && bean.getStatus() == 200) {
                status = bean.getData();
                if (status.isIsNightShift()) {
                    attendSubType = 2;
                }

                if (status.isNeedXiaoJia()) {
                    xiaoJia.setTextColor(Color.parseColor("#666666"));
                    imgXiaoJia.setImageResource(R.drawable.icon_check_xiaojia);
                    tiaoxiu.setTextColor(Color.parseColor("#ababab"));
                    imgTiaoXiu.setImageResource(R.drawable.icon_check_tiaoxiu_gray);
                    qingJia.setTextColor(Color.parseColor("#ababab"));
                    imgQingJia.setImageResource(R.drawable.icon_check_qingjia_gray);
                }else {
                    xiaoJia.setTextColor(Color.parseColor("#ababab"));
                    imgXiaoJia.setImageResource(R.drawable.icon_check_xiaojia_gray);
                    tiaoxiu.setTextColor(Color.parseColor("#666666"));
                    imgTiaoXiu.setImageResource(R.drawable.icon_check_tiaoxiu);
                    qingJia.setTextColor(Color.parseColor("#666666"));
                    imgQingJia.setImageResource(R.drawable.icon_check_qingjia);
                }

                ServerUtils.isAttend(nowDate,isAttendCallBack);
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
        }
    };

    private RequestCallBack<String> isAttendCallBack = new RequestCallBack<String>() {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            AttendanceBean bean = JsonUtils.jsonToPojo(json,AttendanceBean.class);
            if (bean != null && bean.getStatus() == 200) {
                if (bean.getData() != null) {
                    attendance = bean.getData();
                    switch (attendance.getAttendType()){
                        case -1://未签
                        case 0:
                            unCheck.setVisibility(View.VISIBLE);
                            btnCheck.setVisibility(View.VISIBLE);
                            llTitleType.setVisibility(View.GONE);
                            rlTitleType.setVisibility(View.GONE);

                            if (status.isNeedXiaoJia()&&attendance.getLeave() != null && attendance.getLeave().getId()>0) {
                                unCheck.setVisibility(View.GONE);
                                btnCheck.setVisibility(View.GONE);
                                llTitleType.setVisibility(View.GONE);
                                rlTitleType.setVisibility(View.VISIBLE);
                                rlTxtType.setText(leaveType[attendance.getLeave().getAttendSubType()-1]+"（已到期）");
                                SimpleDateFormat spt2 = new SimpleDateFormat("yyyy/MM/dd");
                                rlTxtTime.setText(spt2.format(new Date(attendance.getLeave().getBeginDate())));
                            }

                            if (status.isNeedXiaoJia() && attendance.getTiaoXiu() != null && attendance.getTiaoXiu().getId()>0) {
                                unCheck.setVisibility(View.GONE);
                                btnCheck.setVisibility(View.GONE);
                                llTitleType.setVisibility(View.GONE);
                                rlTitleType.setVisibility(View.VISIBLE);
                                rlTxtType.setText("调休（已到期）");
                                SimpleDateFormat spt2 = new SimpleDateFormat("yyyy/MM/dd");
                                rlTxtTime.setText(spt2.format(new Date(attendance.getTiaoXiu().getBeginDate())));
                            }
                            break;
                        case 1://1：在岗 2：上路 3：出差 4：休假
                        case 2:
                        case 3:
                        case 4:
                            unCheck.setVisibility(View.GONE);
                            btnCheck.setVisibility(View.GONE);
                            llTitleType.setVisibility(View.VISIBLE);
                            rlTitleType.setVisibility(View.GONE);
                            if (attendance.getAttendType() == 1 && attendance.getAttendSubType() != 0) {
                                llTxtType.setText(nightType[attendance.getAttendSubType()-1]);
                            }else {
                                llTxtType.setText(checkType[attendance.getAttendType()+1]);
                            }

                            SimpleDateFormat spt1 = new SimpleDateFormat("yyyy年MM月dd日");
                            llTxtTime.setText(spt1.format(new Date()));
                            break;
                        case 5://5: 调休 6：请假
                        case 6:
                            unCheck.setVisibility(View.GONE);
                            btnCheck.setVisibility(View.GONE);
                            llTitleType.setVisibility(View.GONE);
                            rlTitleType.setVisibility(View.VISIBLE);
                            if (attendance.getAttendType() == 5) {
                                rlTxtType.setText(checkType[attendance.getAttendType()+1]+"（"+attendance.getTiaoXiu().getDays()+"天）");
                            }else {
                                rlTxtType.setText(leaveType[attendance.getLeave().getAttendSubType()-1]+"（"+attendance.getLeave().getDays()+"天）");
                            }

                            tiaoxiu.setTextColor(Color.parseColor("#ababab"));
                            imgTiaoXiu.setImageResource(R.drawable.icon_check_tiaoxiu_gray);
                            qingJia.setTextColor(Color.parseColor("#ababab"));
                            imgQingJia.setImageResource(R.drawable.icon_check_qingjia_gray);

                            if (attendance.getAttendType() == 5) {
                                SimpleDateFormat spt2 = new SimpleDateFormat("yyyy/MM/dd");
                                rlTxtTime.setText(spt2.format(new Date(attendance.getTiaoXiu().getBeginDate())));
                            }else {
                                SimpleDateFormat spt2 = new SimpleDateFormat("yyyy/MM/dd");
                                rlTxtTime.setText(spt2.format(new Date(attendance.getLeave().getBeginDate())));
                            }


                            break;
                        case 7://7：其他
                            unCheck.setVisibility(View.GONE);
                            btnCheck.setVisibility(View.GONE);
                            llTitleType.setVisibility(View.VISIBLE);
                            rlTitleType.setVisibility(View.GONE);
                            llTxtType.setText(checkType[attendance.getAttendType()+1]+"："+attendance.getOtherTypeName());
                            SimpleDateFormat spt3 = new SimpleDateFormat("yyyy年MM月dd日");
//                            llTxtTime.setText(spt3.format(new Date()));
                            llTxtTime.setText("");
                            break;
                    }
                }
                if (month >9) {
                    monthDate = year +"-"+ month;
                }else {
                    monthDate = year +"-0"+ month;
                }
                if (!TextUtils.isEmpty(id)) {
                    ServerUtils.attendUser(id, monthDate,attendUserCallBack);
                }
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
        }
    };

    private RequestCallBack<String> attendUserCallBack = new RequestCallBack<String>() {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            AttendDateBean bean = JsonUtils.jsonToPojo(json,AttendDateBean.class);
            if (bean != null && bean.getStatus() == 200) {
                countList = (ArrayList<AttendTypeCount>) bean.getData().getLeft();

                txtZaiGang.setText(countList.get(0).getCount()+"");
                txtShangLu.setText(countList.get(1).getCount()+"");
                txtChuChai.setText(countList.get(2).getCount()+"");
                txtXiuJia.setText(countList.get(3).getCount()+"");
                txtTiaoXiu.setText(countList.get(4).getCount()+"");
                txtQingJia.setText(countList.get(5).getCount()+"");
                txtQiTa.setText(countList.get(6).getCount()+"");

                attendanceList = (ArrayList<Attendance>) bean.getData().getRight();
                attendAdapter = new AttendAdapter(CheckActivity.this,attendanceList,month);
                gridView.setAdapter(attendAdapter);

                if (attendance.getAttendType() > 0) {
                    ServerUtils.attendRemarkList(id,nowDate,remarkListCallBack);
                }else {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                }
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
        }
    };

    private void showCheckDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();

        View view = View.inflate(this, R.layout.dialog_check_normal, null);
        dialog.setView(view, 0, 0, 0, 0);// 设置边距为0,保证在2.x的版本上运行没问题

        final TextView back = (TextView) view.findViewById(R.id.txt_back);
        TextView check = (TextView) view.findViewById(R.id.txt_ok_check_dialog);
        final EditText editText = (EditText) view.findViewById(R.id.edit_qita_check_normal);
        final CustomRadioGroup radioGroup = (CustomRadioGroup) view.findViewById(R.id.my_rg_check_act);

        switch (attendType){
            case 1:
                radioGroup.check(R.id.rb_zaigang);
                break;
            case 2:
                radioGroup.check(R.id.rb_shanglu);
                break;
            case 3:
                radioGroup.check(R.id.rb_chuchai);
                break;
            case 4:
                radioGroup.check(R.id.rb_xiujia);
                break;
            case 7:
                radioGroup.check(R.id.rb_qita);
                break;
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_zaigang:
                        attendType = 1;
                        break;
                    case R.id.rb_shanglu:
                        attendType = 2;
                        break;
                    case R.id.rb_chuchai:
                        attendType = 3;
                        break;
                    case R.id.rb_xiujia:
                        attendType = 4;
                        break;
                    case R.id.rb_qita:
                        attendType = 7;
                        radioGroup.setVisibility(View.INVISIBLE);
                        editText.setVisibility(View.VISIBLE);
                        back.setText("返回");
                        isBackNormal = true;
                        break;
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBackNormal) {
                    radioGroup.setVisibility(View.VISIBLE);
                    editText.setVisibility(View.GONE);
                    back.setText("取消");
                    editText.setText("");
                    isBackNormal = false;
                }else {
                    dialog.dismiss();
                }
            }
        });
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (attendType == 7) {
                    if (!TextUtils.isEmpty(editText.getText().toString().trim())) {
                        otherTypeName = editText.getText().toString().trim();
                    }else {
                        Utils.showToast(CheckActivity.this,"输入其他情况内容");
                        return;
                    }

                }
                ServerUtils.attend(attendType,otherTypeName,attendSubType,nowDate,latitude,longitude,location,attendCallBack);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showNightCheckDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();

        View view = View.inflate(this, R.layout.dialog_check_ban, null);
        dialog.setView(view, 0, 0, 0, 0);// 设置边距为0,保证在2.x的版本上运行没问题

        final TextView back = (TextView) view.findViewById(R.id.txt_back);
        TextView check = (TextView) view.findViewById(R.id.txt_ok_check_dialog);
        final EditText editText = (EditText) view.findViewById(R.id.edit_qita_check_normal);
        final CustomRadioGroup radioGroup = (CustomRadioGroup) view.findViewById(R.id.my_rg_check_act);

        switch (attendType){
            case 3:
                radioGroup.check(R.id.rb_chuchai);
                break;
            case 4:
                radioGroup.check(R.id.rb_xiujia);
                break;
            case 7:
                radioGroup.check(R.id.rb_qita);
                break;
        }

        switch (attendSubType){
            case 1:
                radioGroup.check(R.id.rb_zhongban);
                break;
            case 2:
                radioGroup.check(R.id.rb_baiban);
                break;
            case 3:
                radioGroup.check(R.id.rb_yeban);
                break;
            case 4:
                radioGroup.check(R.id.rb_yezhongban);
                break;
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_baiban:
                        attendType = 1;
                        attendSubType = 2;
                        break;
                    case R.id.rb_zhongban:
                        attendType = 1;
                        attendSubType = 1;
                        break;
                    case R.id.rb_yeban:
                        attendType = 1;
                        attendSubType = 3;
                        break;
                    case R.id.rb_yezhongban:
                        attendType = 1;
                        attendSubType = 4;
                        break;
                    case R.id.rb_chuchai:
                        attendType = 3;
                        attendSubType = 0;
                        break;
                    case R.id.rb_xiujia:
                        attendType = 4;
                        attendSubType = 0;
                        break;
                    case R.id.rb_qita:
                        attendType = 7;
                        attendSubType = 0;
                        radioGroup.setVisibility(View.INVISIBLE);
                        editText.setVisibility(View.VISIBLE);
                        back.setText("返回");
                        isBackNormal = true;
                        break;
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBackNormal) {
                    radioGroup.setVisibility(View.VISIBLE);
                    editText.setVisibility(View.GONE);
                    back.setText("取消");
                    editText.setText("");
                    isBackNormal = false;
                }else {
                    dialog.dismiss();
                }
            }
        });
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (attendType == 7) {
                    if (!TextUtils.isEmpty(editText.getText().toString().trim())) {
                        otherTypeName = editText.getText().toString().trim();
                    }else {
                        Utils.showToast(CheckActivity.this,"输入其他情况内容");
                        return;
                    }

                }
                ServerUtils.attend(attendType,otherTypeName,attendSubType,nowDate,latitude,longitude,location,attendCallBack);
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    private void showXiaoJiaDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();

        View view = View.inflate(this, R.layout.dialog_xiaojia, null);
        dialog.setView(view, 0, 0, 0, 0);// 设置边距为0,保证在2.x的版本上运行没问题

        TextView days = (TextView) view.findViewById(R.id.txt_days_xiaojia);
        TextView day = (TextView) view.findViewById(R.id.txt_day_xiaojia);
        TextView time = (TextView) view.findViewById(R.id.txt_time_xiaojia);
        TextView type = (TextView) view.findViewById(R.id.txt_type_xiaojia);
        TextView cancel = (TextView) view.findViewById(R.id.txt_cancel_xiaojia);
        TextView ok = (TextView) view.findViewById(R.id.txt_ok_xiaojia);
        Date beginDate = null ;
        if (attendance.getLeave() != null) {
            days.setText(attendance.getLeave().getDays()+"");
            type.setText("请假类型："+leaveType[attendance.getLeave().getAttendSubType()-1]);
            beginDate = new Date(attendance.getLeave().getBeginDate());
        }else if (attendance.getTiaoXiu() != null) {
            days.setText(attendance.getTiaoXiu().getDays()+"");
            type.setText("调休");
            beginDate = new Date(attendance.getTiaoXiu().getBeginDate());
        }
        SimpleDateFormat spt = new SimpleDateFormat("起始日期：yyyy年MM月dd日");
        time.setText(spt.format(beginDate));

        Date date = new Date();
        int x = (int) ((date.getTime() - beginDate.getTime())/(24*60*60*1000))+1;

        day.setText("今天是第"+x+"天");

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerUtils.checkXiaoJia(xiaoJiaCallBack);
                dialog.dismiss();

            }
        });

        dialog.show();
    }

    private void showTipDialog(Attendance attendance){
        tipView = LayoutInflater.from(this).inflate(R.layout.check_pop, null);

        tipWindow = new PopupWindow(tipView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

        TextView time = (TextView) tipView.findViewById(R.id.txt_time_pop);
        lvCheck = (NoScrollListView) tipView.findViewById(R.id.check_list_check_pop);

        TextView ok = (TextView) tipView.findViewById(R.id.txt_pop_ok);

        Date date = new Date(attendance.getUpdateDate());

        treeMap = new TreeMap<>(Collections.reverseOrder());

        treeMap.put(attendance.getUpdateDate(),attendance);
        dateAttendance = attendance;

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日 E");
        time.setText(sdf1.format(date));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        strDate = sdf.format(date);
        ServerUtils.attendRemarkList(id,strDate,popRemarkCallBack);

        tipWindow.setTouchable(true);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipWindow.dismiss();
            }
        });

        tipWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
            }
        });

        tipWindow.setAnimationStyle(R.style.anim_popup_dir);

        // 设置好参数之后再show
        tipWindow.showAtLocation(this.findViewById(R.id.activity_check), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (relativeLayout.getVisibility() == View.VISIBLE) {
            relativeLayout.setVisibility(View.GONE);
        }else {
            super.onBackPressed();
        }

    }


}
