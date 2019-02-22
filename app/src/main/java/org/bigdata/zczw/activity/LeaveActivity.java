package org.bigdata.zczw.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.bigdata.zczw.R;
import org.bigdata.zczw.entity.Bean;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.ServerUtils;
import org.bigdata.zczw.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

/*
* 请假
* */
public class LeaveActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgBack;
    private EditText editText;
    private TextView textView,btnInput;
    private TextView beginDate,btnOk,txtType;
    private RelativeLayout rlDay,rlBegin,rlType,rlInput;
    private View view;

    private String days;
    private String begin;
    private int attendSubType;
    private String subTypeName;

    private TimePickerView pvTime;
    private Date repeatDate;
    private OptionsPickerView pvCustomOptions;

    private String[] leaveType = new String[]{"事假","病假","年休假","婚假","预产假","产假","陪产假","哺乳假","延长哺乳假","丧假","工伤"};
    private ArrayList<String> leaveList;


    private double longitude;// 经度
    private double latitude;// 纬度
    private String location ;//地理位置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave);
        AppManager.getAppManager().addActivity(this);

        getSupportActionBar().hide();

        initView();
        initData();
    }

    private void initView() {
        imgBack = (ImageView) findViewById(R.id.img_back);

        editText = (EditText) findViewById(R.id.edit_input_days);
        btnInput = (TextView) findViewById(R.id.txt_ok_days);

        textView = (TextView) findViewById(R.id.txt_leave_day);
        beginDate = (TextView) findViewById(R.id.txt_begin_date);
        btnOk = (TextView) findViewById(R.id.btn_ok_leave);
        txtType = (TextView) findViewById(R.id.txt_leave_type);

        rlDay = (RelativeLayout) findViewById(R.id.rl_days);
        rlBegin = (RelativeLayout) findViewById(R.id.rl_begin);
        rlType = (RelativeLayout) findViewById(R.id.rl_type);

        rlInput = (RelativeLayout) findViewById(R.id.rl_edit_check_act);
        view = findViewById(R.id.view_black);

        btnOk.setOnClickListener(this);
        rlBegin.setOnClickListener(this);
        rlType.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        rlDay.setOnClickListener(this);
        btnInput.setOnClickListener(this);
        view.setOnClickListener(this);
    }

    private void initData() {
        latitude = getIntent().getDoubleExtra("latitude",0);
        longitude = getIntent().getDoubleExtra("longitude",0);
        location = getIntent().getStringExtra("location");

        repeatDate = new Date();
        beginDate.setText(getDate(repeatDate));
        days = "1";
        leaveList = new ArrayList<>();
        for (int i = 0; i < leaveType.length; i++) {
            leaveList.add(leaveType[i]);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.rl_days:
                rlInput.setVisibility(View.VISIBLE);
                editText.setFocusable(true);
                editText.setFocusableInTouchMode(true);
                editText.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText,InputMethodManager.SHOW_FORCED);
                break;
            case R.id.view_black:
                rlInput.setVisibility(View.GONE);
                break;
            case R.id.txt_ok_days:
                if (!TextUtils.isEmpty(editText.getText().toString().trim())) {
                    days = editText.getText().toString().trim();
                    if (!isInteger(days)) {
                        Utils.showToast(LeaveActivity.this,"请输入数字");
                        return;
                    }
                }else {
                    Utils.showToast(LeaveActivity.this,"请输入请假总天数");
                    return;
                }
                InputMethodManager imm1 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(editText.getWindowToken(),0);
                rlInput.setVisibility(View.GONE);
                editText.setText("");
                textView.setText(days);
                break;
            case R.id.rl_begin:
                initTimePicker();
                pvTime.show();
                break;
            case R.id.rl_type:
                initCustomOptionPicker();
                pvCustomOptions.show();
                break;
            case R.id.btn_ok_leave:

                if (TextUtils.isEmpty(subTypeName)) {
                    Utils.showToast(LeaveActivity.this,"请选择请假类型");
                    return;
                }

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                begin =  simpleDateFormat.format(repeatDate);
                ServerUtils.leave(days,begin,attendSubType+1,latitude,longitude,location,callback);
                break;
        }
    }


    private RequestCallBack<String> callback = new RequestCallBack<String>() {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            Bean bean = JsonUtils.jsonToPojo(json,Bean.class);
            if (bean != null && bean.getStatus() == 200) {
                Utils.showToast(LeaveActivity.this,"请假成功");
                setResult(201,new Intent());
                finish();
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
            Log.e("1111", "onFailure: "+e);
        }
    };



    private void initTimePicker() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();

        //正确设置方式 原因：注意事项有说明
        endDate.set(2100,11,31);

        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date,View v) {//选中事件回调
                beginDate.setText(getDate(date));
                repeatDate = date;
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("完成")//确认按钮文字
                .setContentTextSize(18)
                .setTitleSize(18)//标题文字大小
                .setTitleText("起始日期")//标题文字
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


    private void initCustomOptionPicker() {//条件选择器初始化，自定义布局
        pvCustomOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                attendSubType = options1;
            }
        })
                .setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        TextView ivCancel = (TextView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.returnData();
                                subTypeName = leaveList.get(attendSubType);
                                txtType.setText(subTypeName);
                                pvCustomOptions.dismiss();
                            }
                        });

                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.dismiss();
                            }
                        });

                    }
                })
                .setSelectOptions(0)
                .isDialog(false)
                .setContentTextSize(18)
                .setLineSpacingMultiplier(2.4f)
                .build();

        pvCustomOptions.setPicker(leaveList);//添加数据

    }

    private String getDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return simpleDateFormat.format(date);
    }

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
}
