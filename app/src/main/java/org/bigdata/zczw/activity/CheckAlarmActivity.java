package org.bigdata.zczw.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.bigdata.zczw.R;
import org.bigdata.zczw.adapter.AlarmAdapter;
import org.bigdata.zczw.entity.AttendCount;
import org.bigdata.zczw.entity.AttendMessage;
import org.bigdata.zczw.entity.AttendMessages;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.ServerUtils;
import org.bigdata.zczw.utils.Utils;
import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
/*
* 签到提醒
* */
public class CheckAlarmActivity extends AppCompatActivity implements View.OnClickListener, PullToRefreshBase.OnRefreshListener<ListView>,RadioGroup.OnCheckedChangeListener {

    private ImageView back,qingjia,tiaoxiu,xiaojia;
    private RadioGroup radioGroup;
    private PullToRefreshListView listView;

    private int sourceType;
    private ArrayList<AttendMessage> msgList;
    private AlarmAdapter alarmAdapter;
    private AttendCount attendCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_alarm);
        AppManager.getAppManager().addActivity(this);
        getSupportActionBar().hide();

        initView();
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.img_back_check_alarm);

        qingjia = (ImageView) findViewById(R.id.img_qingjia_point);
        tiaoxiu = (ImageView) findViewById(R.id.img_tiaoxiu_point);
        xiaojia = (ImageView) findViewById(R.id.img_xiaojia_point);
        radioGroup = (RadioGroup) findViewById(R.id.rg_alarm);
        listView = (PullToRefreshListView) findViewById(R.id.list_alarm);

        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        listView.setOnRefreshListener(this);

        back.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(this);

        sourceType = 1;
        msgList = new ArrayList<>();

        ServerUtils.attendMsgList(sourceType,"","false",callBack);

        if (getIntent().hasExtra("count")) {
            attendCount = (AttendCount) getIntent().getSerializableExtra("count");
            if (attendCount != null) {
                if (attendCount.getTiaoXiuCount()>0) {
                    tiaoxiu.setVisibility(View.VISIBLE);
                }
                if (attendCount.getXiaoJiaCount()>0) {
                    xiaojia.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {

        String str = DateUtils.formatDateTime(CheckAlarmActivity.this,
                System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

        // 上拉加载更多 业务代码
        if (refreshView.isShownFooter()) {
            listView.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
            listView.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
            listView.getLoadingLayoutProxy().setReleaseLabel("释放开始加载");
            refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后加载时间:" + str);
            String id = "";
            if (msgList != null && msgList.size()>0) {
                id = msgList.get(msgList.size()-1).getId()+"";
            }

            ServerUtils.attendMsgList(sourceType,id,"false",addCallBack);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back_check_alarm:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.rb_alarm_left:
                if (qingjia.getVisibility() == View.VISIBLE) {
                    qingjia.setVisibility(View.GONE);
                }
                sourceType = 1;
                ServerUtils.attendMsgList(sourceType,"","false",callBack);
                break;
            case R.id.rb_alarm_center:
                sourceType = 3;
                if (tiaoxiu.getVisibility() == View.VISIBLE) {
                    tiaoxiu.setVisibility(View.GONE);
                }
                ServerUtils.attendMsgList(sourceType,"","false",callBack);
                break;
            case R.id.rb_alarm_right:
                sourceType = 2;
                if (xiaojia.getVisibility() == View.VISIBLE) {
                    xiaojia.setVisibility(View.GONE);
                }
                ServerUtils.attendMsgList(sourceType,"","false",callBack);
                break;
        }
    }


    private RequestCallBack<String> callBack = new RequestCallBack<String>() {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            AttendMessages bean = JsonUtils.jsonToPojo(json,AttendMessages.class);
            if (bean != null && bean.getStatus() == 200) {
                if (bean.getData() != null && bean.getData().size()>0) {
                    msgList = (ArrayList<AttendMessage>) bean.getData();
                    alarmAdapter = new AlarmAdapter(CheckAlarmActivity.this,msgList,sourceType);
                    listView.setAdapter(alarmAdapter);
                }else {
                    Utils.showToast(CheckAlarmActivity.this,"暂无信息");
                }
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };

    private RequestCallBack<String> addCallBack = new RequestCallBack<String>() {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            AttendMessages bean = JsonUtils.jsonToPojo(json,AttendMessages.class);
            if (bean != null && bean.getStatus() == 200) {
                listView.onRefreshComplete();
                if (bean.getData() != null && bean.getData().size()>0) {
                    msgList.addAll(bean.getData());
                    alarmAdapter.notifyDataSetChanged();
                }else {
                    Utils.showToast(CheckAlarmActivity.this,"暂无更多信息");
                }
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };
}
