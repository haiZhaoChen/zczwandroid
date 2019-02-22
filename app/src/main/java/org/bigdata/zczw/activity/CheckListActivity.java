package org.bigdata.zczw.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.bigdata.zczw.App;
import org.bigdata.zczw.R;
import org.bigdata.zczw.adapter.CheckListAdapter;
import org.bigdata.zczw.entity.AttendCount;
import org.bigdata.zczw.entity.AttendCountBean;
import org.bigdata.zczw.entity.AttendTypeCount;
import org.bigdata.zczw.entity.AttendTypeCountBean;
import org.bigdata.zczw.entity.Check;
import org.bigdata.zczw.entity.CheckBean;
import org.bigdata.zczw.ui.BadgeView;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.SPUtil;
import org.bigdata.zczw.utils.ServerUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/*
* 员工签到列表
* */

public class CheckListActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    //签到统计
    private TextView txtZaiGang,txtShangLu,txtChuChai,txtXiuJia,txtTiaoXiu,txtQingJia,txtQiTa;

    private ImageView back;
    private ImageView alarm;

    private ListView listView;
    private View convertView;
    private TextView txtDay,txtMonth,alarmCount;
    private String day,day2,month,month2;
    private LinearLayout llDay,llMonth;

    private BadgeView badgeView;

    private ArrayList<Check> checkLists;
    private ArrayList<AttendTypeCount> countList;
    private CheckListAdapter checkListAdapter;

    private TimePickerView dayTime,monthTime;
    private Calendar calendar;
    private Date monthDate;
    private SimpleDateFormat spfDay;
    private SimpleDateFormat spfDay2;
    private SimpleDateFormat spfMonth;
    private SimpleDateFormat spfMonth2;
    private Date nowDate;
    private AttendCount attendCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);

        AppManager.getAppManager().addActivity(this);
        getSupportActionBar().hide();

        initView();

    }

    private void initView() {
        listView = (ListView) findViewById(R.id.list_check_list_act);

        convertView = LayoutInflater.from(this).inflate(R.layout.check_list_header,null);
        txtZaiGang = (TextView) convertView.findViewById(R.id.txt_zaigang_num_check_act);
        txtShangLu = (TextView) convertView.findViewById(R.id.txt_shanglu_num_check_act);
        txtChuChai = (TextView) convertView.findViewById(R.id.txt_chuchai_num_check_act);
        txtXiuJia = (TextView) convertView.findViewById(R.id.txt_xiujia_num_check_act);
        txtTiaoXiu = (TextView) convertView.findViewById(R.id.txt_tiaoxiu_num_check_act);
        txtQingJia = (TextView) convertView.findViewById(R.id.txt_qingjia_num_check_act);
        txtQiTa = (TextView) convertView.findViewById(R.id.txt_qita_num_check_act);

        llDay = (LinearLayout) convertView.findViewById(R.id.ll_select_day);
        llMonth = (LinearLayout) convertView.findViewById(R.id.ll_select_month);

        txtDay = (TextView) convertView.findViewById(R.id.txt_day_header);
        txtMonth = (TextView) convertView.findViewById(R.id.txt_month_header);

        alarmCount = (TextView) findViewById(R.id.alarm_count);
        back = (ImageView) findViewById(R.id.img_back_check_list);
        alarm = (ImageView) findViewById(R.id.img_alarm_check_list);

        nowDate = new Date();
        spfDay = new SimpleDateFormat("yyyy-MM-dd");
        spfDay2 = new SimpleDateFormat("MM月dd日");
        spfMonth = new SimpleDateFormat("yyyy-MM");
        spfMonth2 = new SimpleDateFormat("yyyy年MM月");
        day = spfDay.format(nowDate);
        day2 = spfDay2.format(nowDate);
        month = spfMonth.format(nowDate);
        month2 = spfMonth2.format(nowDate);

        monthDate = nowDate;

        txtDay.setText(day2);
        txtMonth.setText(month2);

        checkLists = new ArrayList<>();
        countList = new ArrayList<>();

        calendar = Calendar.getInstance();

        llDay.setOnClickListener(this);
        llMonth.setOnClickListener(this);
        back.setOnClickListener(this);
        alarm.setOnClickListener(this);
        listView.setOnItemClickListener(this);

        ServerUtils.attendDayList(day,dayCallBack);//yyyy-MM-dd格式
        ServerUtils.attendMsgCount(countCallBack);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position>0) {
            String userId = checkLists.get(position-1).getUser().getId()+"";
            String host = SPUtil.getString(CheckListActivity.this, App.USER_ID);
            if (userId.equals(host)) {
                startActivity(new Intent(CheckListActivity.this,CheckActivity.class));
            }else {
                Intent intent = new Intent(CheckListActivity.this,CheckInfoActivity.class);
                intent.putExtra("user",checkLists.get(position-1).getUser());
                intent.putExtra("time",monthDate);
                if (checkLists.get(position-1).getTodayAttend() != null) {
                    intent.putExtra("attendance",checkLists.get(position-1).getTodayAttend());
                }
                startActivity(intent);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_select_day:
                initDayPicker();
                dayTime.show();
                break;
            case R.id.ll_select_month:
                initMonthPicker();
                monthTime.show();
                break;
            case R.id.img_back_check_list:
                onBackPressed();
                break;
            case R.id.img_alarm_check_list:
                if (alarmCount.getVisibility() == View.VISIBLE) {
                    alarmCount.setVisibility(View.GONE);
                }
                Intent intent = new Intent(CheckListActivity.this,CheckAlarmActivity.class);
                if (attendCount != null) {
                    intent.putExtra("count",attendCount);
                }
                startActivity(intent);
                attendCount = null;
                break;
        }
    }

    private void initDayPicker() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();

        //正确设置方式 原因：注意事项有说明
        startDate.set(2000,0,1);

        dayTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date,View v) {//选中事件回调
                txtDay.setText(spfDay2.format(date));
                day = spfDay.format(date);
                ServerUtils.attendDayList(day,day2CallBack);
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("完成")//确认按钮文字
                .setContentTextSize(18)
                .setTitleSize(18)//标题文字大小
                .setTitleText("选择日期")//标题文字
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(false)//是否循环滚动
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.parseColor("#e6731c"))//确定按钮文字颜色
                .setCancelColor(Color.GRAY)//取消按钮文字颜色
                .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate,endDate)//起始终止年月日设定
                .setLabel("年","月","日","时","分","秒")//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式
                .setLineSpacingMultiplier(2.4f)
                .build();
    }

    private void initMonthPicker() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();

        //正确设置方式 原因：注意事项有说明
        startDate.set(2000,0,1);

        monthTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date,View v) {//选中事件回调
                txtMonth.setText(spfMonth2.format(date));
                month = spfMonth.format(date);
                monthDate = date;
                ServerUtils.attendMonthList(month,month2CallBack);//yyyy-MM-dd格式
            }
        })
                .setType(new boolean[]{true, true, false, false, false, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("完成")//确认按钮文字
                .setContentTextSize(18)
                .setTitleSize(18)//标题文字大小
                .setTitleText("选择日期")//标题文字
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(false)//是否循环滚动
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.parseColor("#e6731c"))//确定按钮文字颜色
                .setCancelColor(Color.GRAY)//取消按钮文字颜色
                .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate,endDate)//起始终止年月日设定
                .setLabel("年","月","日","时","分","秒")//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式
                .setLineSpacingMultiplier(2.4f)
                .build();
    }

    private RequestCallBack<String> countCallBack = new RequestCallBack<String>() {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            AttendCountBean bean = JsonUtils.jsonToPojo(json,AttendCountBean.class);
            if (bean != null && bean.getData()!=null) {
                attendCount = bean.getData();
                if (attendCount.getLeaveCount()+attendCount.getTiaoXiuCount()+attendCount.getXiaoJiaCount()>0) {
                    alarmCount.setVisibility(View.VISIBLE);
                    alarmCount.setText(attendCount.getLeaveCount()+attendCount.getTiaoXiuCount()+attendCount.getXiaoJiaCount()+"");
                }
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };


    private RequestCallBack<String> dayCallBack = new RequestCallBack<String>() {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            AttendTypeCountBean bean = JsonUtils.jsonToPojo(json,AttendTypeCountBean.class);
            if (bean != null && bean.getStatus() == 200) {
                if (bean.getData()!=null && bean.getData().size()>0) {
                    countList = (ArrayList<AttendTypeCount>) bean.getData();
                    txtZaiGang.setText(countList.get(0).getCount()+"");
                    txtShangLu.setText(countList.get(1).getCount()+"");
                    txtChuChai.setText(countList.get(2).getCount()+"");
                    txtXiuJia.setText(countList.get(3).getCount()+"");
                    txtTiaoXiu.setText(countList.get(4).getCount()+"");
                    txtQingJia.setText(countList.get(5).getCount()+"");
                    txtQiTa.setText(countList.get(6).getCount()+"");
                }
                listView.addHeaderView(convertView);
                ServerUtils.attendMonthList(month,monthCallBack);//yyyy-MM-dd格式
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
        }
    };

    private RequestCallBack<String> day2CallBack = new RequestCallBack<String>() {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            AttendTypeCountBean bean = JsonUtils.jsonToPojo(json,AttendTypeCountBean.class);
            if (bean != null && bean.getStatus() == 200) {
                if (bean.getData()!=null && bean.getData().size()>0) {
                    countList = (ArrayList<AttendTypeCount>) bean.getData();
                    txtZaiGang.setText(countList.get(0).getCount()+"");
                    txtShangLu.setText(countList.get(1).getCount()+"");
                    txtChuChai.setText(countList.get(2).getCount()+"");
                    txtXiuJia.setText(countList.get(3).getCount()+"");
                    txtTiaoXiu.setText(countList.get(4).getCount()+"");
                    txtQingJia.setText(countList.get(5).getCount()+"");
                    txtQiTa.setText(countList.get(6).getCount()+"");
                }
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
        }
    };

    private RequestCallBack<String> monthCallBack = new RequestCallBack<String>() {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            CheckBean bean = JsonUtils.jsonToPojo(json,CheckBean.class);
            if (bean != null && bean.getStatus() == 200) {
                if (bean.getData()!=null && bean.getData().size()>0) {
                    checkLists = (ArrayList<Check>) bean.getData();
                }
                checkListAdapter = new CheckListAdapter(CheckListActivity.this,checkLists);
                listView.setAdapter(checkListAdapter);
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
        }
    };

    private RequestCallBack<String> month2CallBack = new RequestCallBack<String>() {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            CheckBean bean = JsonUtils.jsonToPojo(json,CheckBean.class);
            if (bean != null && bean.getStatus() == 200) {
                if (bean.getData()!=null && bean.getData().size()>0) {
                    checkLists = (ArrayList<Check>) bean.getData();
                }
                checkListAdapter = new CheckListAdapter(CheckListActivity.this,checkLists);
                listView.setAdapter(checkListAdapter);
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }



}
