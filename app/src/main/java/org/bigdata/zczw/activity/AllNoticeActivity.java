package org.bigdata.zczw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.bigdata.zczw.R;
import org.bigdata.zczw.adapter.AllNoticeAdapter;
import org.bigdata.zczw.entity.BoxMsg;
import org.bigdata.zczw.entity.BoxMsgBean;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.ServerUtils;
import org.bigdata.zczw.utils.Utils;
import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

/*
* 设置 与我相关
* */

public class AllNoticeActivity extends AppCompatActivity implements PullToRefreshBase.OnRefreshListener<ListView>,AdapterView.OnItemClickListener {

    private PullToRefreshListView listView;
    private AllNoticeAdapter adapter;
    private ArrayList<BoxMsg> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_notice);
        getSupportActionBar().setTitle("与我相关");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);
        AppManager.getAppManager().addActivity(this);

        listView = (PullToRefreshListView) findViewById(R.id.list_all_notice_act);
        ServerUtils.getBoxMessageList("","true",callBack);

        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnItemClickListener(this);
        listView.setOnRefreshListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(AllNoticeActivity.this, MessageActivity.class);
        intent.putExtra("msg",arrayList.get(position-1).getMessage().getMessageId());
        intent.putExtra("record",arrayList.get(position-1).getMessage());
        startActivityForResult(intent, 101);
    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        String str = DateUtils.formatDateTime(AllNoticeActivity.this,
                System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
        // 下拉刷新 业务代码
        if (refreshView.isShownHeader()) {
            listView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
            listView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
            listView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
            refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新时间:" + str);

            if (arrayList != null && arrayList.size()>0) {
                ServerUtils.getBoxMessageList(arrayList.get(0).getId()+"","true",freshCallBack);
            }else {
                ServerUtils.getBoxMessageList("","true",callBack);
            }
        }

        // 上拉加载更多 业务代码
        if (refreshView.isShownFooter()) {
            listView.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
            listView.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
            listView.getLoadingLayoutProxy().setReleaseLabel("释放开始加载");
            refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后加载时间:" + str);

            if (arrayList != null && arrayList.size()>0) {
                ServerUtils.getBoxMessageList(arrayList.get(arrayList.size()-1).getId()+"","false",addCallBack);
            }else {
                ServerUtils.getBoxMessageList("","true",callBack);
            }
        }
    }

    private RequestCallBack<String> callBack = new RequestCallBack<String>() {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            BoxMsgBean bean = JsonUtils.jsonToPojo(json,BoxMsgBean.class);
            if (bean != null) {
                if (bean.getStatus() == 200) {
                    if (bean.getData() != null && bean.getData().size()>0) {
                        arrayList = (ArrayList<BoxMsg>) bean.getData();
                        adapter = new AllNoticeAdapter(arrayList,AllNoticeActivity.this);
                        listView.setAdapter(adapter);
                    }
                }
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
        }
    };

    private RequestCallBack<String> freshCallBack = new RequestCallBack<String>() {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            BoxMsgBean bean = JsonUtils.jsonToPojo(json,BoxMsgBean.class);
            listView.onRefreshComplete();
            if (bean != null) {
                if (bean.getStatus() == 200) {
                    if (bean.getData() != null && bean.getData().size()>0) {
                        arrayList.addAll(0,bean.getData()) ;
                        adapter.notifyDataSetChanged();
                        listView.onRefreshComplete();
                    }else {
                        Utils.showToast(AllNoticeActivity.this,"没有更新的消息了");
                        listView.onRefreshComplete();
                    }
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
            BoxMsgBean bean = JsonUtils.jsonToPojo(json,BoxMsgBean.class);
            listView.onRefreshComplete();
            if (bean != null) {
                if (bean.getStatus() == 200) {
                    if (bean.getData() != null && bean.getData().size()>0) {
                        arrayList.addAll(bean.getData());
                        adapter.notifyDataSetChanged();
                        listView.onRefreshComplete();
                    }else {
                        Utils.showToast(AllNoticeActivity.this,"没有更多的消息了");
                        listView.onRefreshComplete();
                    }
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
