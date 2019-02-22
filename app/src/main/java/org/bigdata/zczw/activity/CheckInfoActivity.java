package org.bigdata.zczw.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.bigdata.zczw.R;
import org.bigdata.zczw.adapter.AttendAdapter;
import org.bigdata.zczw.adapter.CheckDateAdapter;
import org.bigdata.zczw.adapter.CheckNoteAdapter;
import org.bigdata.zczw.adapter.ChecksAdapter;
import org.bigdata.zczw.entity.AttendDateBean;
import org.bigdata.zczw.entity.AttendTypeCount;
import org.bigdata.zczw.entity.Attendance;
import org.bigdata.zczw.entity.AttendanceHistory;
import org.bigdata.zczw.entity.Author;
import org.bigdata.zczw.entity.CheckNote;
import org.bigdata.zczw.entity.CheckNoteBean;
import org.bigdata.zczw.entity.HistoryBean;
import org.bigdata.zczw.ui.DateScrollGridView;
import org.bigdata.zczw.ui.NoScrollListView;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.ServerUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.TreeMap;
/*
* 员工签到详情
* */
public class CheckInfoActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ScrollView scrollView;

    private TextView llTxtType;
    private TextView txtMonth;
    //备注列表
    private LinearLayout llNotes;
    private NoScrollListView noteListView;
    //签到统计
    private TextView txtZaiGang,txtShangLu,txtChuChai,txtXiuJia,txtTiaoXiu,txtQingJia,txtQiTa;

    private TextView txtDate;
    private ImageView monthLeft,monthRight;
    private DateScrollGridView gridView;

    private PopupWindow tipWindow;
    private View tipView;
    private NoScrollListView tipListView;

    private NoScrollListView lvCheck;
    private ArrayList<AttendanceHistory> historyArrayList;
    private ChecksAdapter checksAdapter;

    private String[] checkType = new String[]{"","未签到","在岗","上路","出差","休假","调休","请假","其他"};
    private String[] nightType = new String[]{"中班","白班","夜班","夜班/中班"};
    private String[] leaveType = new String[]{"事假","病假","年休假","婚假","预产假","产假","陪产假","哺乳假","延长哺乳假","丧假","工伤"};

    private ArrayList<Attendance> attendanceList;
    private ArrayList<AttendTypeCount> countList;
    private AttendAdapter attendAdapter;

    public int year,month;
    private String monthDate;

    private ArrayList<CheckNote> checkNoteList;
    private ArrayList<CheckNote> noteList;
    private CheckNoteAdapter checkNoteAdapter;
    private Author user;
    private Attendance attendance;
    private Date date;
    private TreeMap<Object, Object> treeMap;
//    private HashMap<Object, Object> sortMap;
    private Attendance dateAttendance;

    private String strDate;

    private ArrayList<Object> allArrayList;
    private CheckDateAdapter checkDateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_info);

        AppManager.getAppManager().addActivity(this);

        user = (Author) getIntent().getSerializableExtra("user");
        if (getIntent().hasExtra("attendance")) {
            attendance = (Attendance) getIntent().getSerializableExtra("attendance");
        }
        date = (Date) getIntent().getSerializableExtra("time");

        getSupportActionBar().setTitle(user.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);

        initView();
        initData();
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

        gridView = (DateScrollGridView) findViewById(R.id.date_grid_check_act);
        llNotes = (LinearLayout) findViewById(R.id.ll_check_note_check_act);

        noteListView = (NoScrollListView) findViewById(R.id.note_list_check_act);

        llTxtType = (TextView) findViewById(R.id.txt_unCheck_check_act);
        txtZaiGang = (TextView) findViewById(R.id.txt_zaigang_num_check_act);
        txtShangLu = (TextView) findViewById(R.id.txt_shanglu_num_check_act);
        txtChuChai = (TextView) findViewById(R.id.txt_chuchai_num_check_act);
        txtXiuJia = (TextView) findViewById(R.id.txt_xiujia_num_check_act);
        txtTiaoXiu = (TextView) findViewById(R.id.txt_tiaoxiu_num_check_act);
        txtQingJia = (TextView) findViewById(R.id.txt_qingjia_num_check_act);
        txtQiTa = (TextView) findViewById(R.id.txt_qita_num_check_act);

        txtMonth = (TextView) findViewById(R.id.txt_month);

        monthLeft.setOnClickListener(this);
        monthRight.setOnClickListener(this);
        gridView.setOnItemClickListener(this);
    }

    private void initData() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        if (month>9) {
            monthDate = year+"-"+month;
            txtDate.setText(year+"年"+month+"月");
        }else {
            monthDate = year+"-0"+month;
            txtDate.setText(year+"年0"+month+"月");
        }

        attendanceList = new ArrayList<>();
        countList = new ArrayList<>();
        noteList = new ArrayList<>();
        allArrayList = new ArrayList<>();
        checkNoteList = new ArrayList<>();
        historyArrayList = new ArrayList<>();
        treeMap = new TreeMap<>(Collections.reverseOrder());

//        sortMap = new HashMap<>();
        SimpleDateFormat spt1 = new SimpleDateFormat("yyyy年MM月签到统计");
        txtMonth.setText(spt1.format(new Date()));

        if (attendance != null) {
            if (attendance.getAttendType() == 6) {
                llTxtType.setText(leaveType[attendance.getAttendSubType()-1]);
            }else if (attendance.getAttendType() == 7) {
                llTxtType.setText("其他："+ attendance.getOtherTypeName());
            }else {
                llTxtType.setText(checkType[attendance.getAttendType()+1]);
            }
        }



        ServerUtils.attendUser(user.getId()+"",monthDate,attendUserCallBack);
        String s = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        if (attendance!=null && attendance.getAttendType() > 0) {
            ServerUtils.attendRemarkList(user.getId()+"",s,remarkListCallBack);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_left_one:
                if(month == 1){//若果是1月份，则变成12月份
                    year = year-1;
                    month = 12;
                }else{
                    month = month-1;
                }
                if (month>9) {
                    txtDate.setText(year+"年"+month+"月");
                }else {
                    txtDate.setText(year+"年0"+month+"月");
                }
                if (month>9) {
                    monthDate = year+"-"+month;
                }else {
                    monthDate = year+"-0"+month;
                }
                ServerUtils.attendUser(user.getId()+"",monthDate,attendUserCallBack);
                break;
            case R.id.iv_right_one:
                if(month == 12){//若果是12月份，则变成1月份
                    year = year+1;
                    month = 1;
                }else{
                    month = month + 1;
                }
                if (month>9) {
                    txtDate.setText(year+"年"+month+"月");
                }else {
                    txtDate.setText(year+"年0"+month+"月");
                }
                if (month>9) {
                    monthDate = year+"-"+month;
                }else {
                    monthDate = year+"-0"+month;
                }
                ServerUtils.attendUser(user.getId()+"",monthDate,attendUserCallBack);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (attendanceList.get(position).getAttendType()>0) {
            showTipDialog(attendanceList.get(position));
        }
    }


    private RequestCallBack<String> attendUserCallBack = new RequestCallBack<String>() {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            AttendDateBean bean = JsonUtils.jsonToPojo(json,AttendDateBean.class);
            if (bean != null && bean.getStatus() == 200) {
                countList = (ArrayList<AttendTypeCount>) bean.getData().getLeft();
                countList = (ArrayList<AttendTypeCount>) bean.getData().getLeft();

                txtZaiGang.setText(countList.get(0).getCount()+"");
                txtShangLu.setText(countList.get(1).getCount()+"");
                txtChuChai.setText(countList.get(2).getCount()+"");
                txtXiuJia.setText(countList.get(3).getCount()+"");
                txtTiaoXiu.setText(countList.get(4).getCount()+"");
                txtQingJia.setText(countList.get(5).getCount()+"");
                txtQiTa.setText(countList.get(6).getCount()+"");

                attendanceList = (ArrayList<Attendance>) bean.getData().getRight();
                attendAdapter = new AttendAdapter(CheckInfoActivity.this,attendanceList,month);
                gridView.setAdapter(attendAdapter);

            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
        }
    };

    private void showTipDialog(Attendance attendance){
        tipView = LayoutInflater.from(this).inflate(R.layout.check_pop, null);

        tipWindow = new PopupWindow(tipView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

        TextView time = (TextView) tipView.findViewById(R.id.txt_time_pop);
        lvCheck = (NoScrollListView) tipView.findViewById(R.id.check_list_check_pop);

        TextView ok = (TextView) tipView.findViewById(R.id.txt_pop_ok);

        Date date = new Date(attendance.getUpdateDate());

        treeMap.clear();
        treeMap.put(attendance.getUpdateDate(),attendance);

        dateAttendance = attendance;

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日 E");
        time.setText(sdf1.format(date));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        strDate = sdf.format(date);

        ServerUtils.attendRemarkList(user.getId()+"",strDate,popRemarkCallBack);


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
        tipWindow.showAtLocation(this.findViewById(R.id.activity_check_info), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);

    }

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
                System.out.println(checkNoteList.size());
                ServerUtils.checksList(user.getId()+"",strDate,checksCallBack);
            }
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
                System.out.println(checkNoteList.size());
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

                checkDateAdapter = new CheckDateAdapter(CheckInfoActivity.this,dateAttendance,allArrayList);
                lvCheck.setAdapter(checkDateAdapter);
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
                    checkNoteAdapter = new CheckNoteAdapter(CheckInfoActivity.this,noteList);
                    noteListView.setAdapter(checkNoteAdapter);
                }
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
