package org.bigdata.zczw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.bigdata.zczw.R;
import org.bigdata.zczw.adapter.DetailAdapter;
import org.bigdata.zczw.entity.DetailList;
import org.bigdata.zczw.entity.Record;
import org.bigdata.zczw.ui.WinToast;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.ServerUtils;
import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/*
* 个人所有动态列表
* */

public class DetailActivity extends AppCompatActivity implements PullToRefreshBase.OnRefreshListener<ListView>,
        AdapterView.OnItemClickListener {

    private LinearLayout linearLayout;
    private PullToRefreshListView listView;
    private String name;
    private String id;

    private List<Record> recordList;
    private DetailAdapter adapter;
    private String firstId,lastId;
    private ArrayList<Integer> num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);

        initView();
        initData();
    }

    private void initView() {
        listView = (PullToRefreshListView) findViewById(R.id.listView_detail_act);
        linearLayout = (LinearLayout) findViewById(R.id.ll_null);

        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        listView.setOnRefreshListener(this);
        listView.setOnItemClickListener(this);
    }

    private void initData() {
        name = getIntent().getStringExtra("name");
        id = getIntent().getStringExtra("id");

        if (!TextUtils.isEmpty(name)) {
            getSupportActionBar().setTitle(name);
        }

        recordList = new ArrayList<>();
        num = new ArrayList<>();

        ServerUtils.mGetDetailList(id,"","0","",message);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(DetailActivity.this, MessageActivity.class);
        intent.putExtra("msg",recordList.get(position-1).getMessageId());
        intent.putExtra("record",recordList.get(position-1));
        startActivityForResult(intent, 101);
    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {

        String str = DateUtils.formatDateTime(DetailActivity.this,
                System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
//        // 下拉刷新 业务代码
//        if (refreshView.isShownHeader()) {
//            listView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
//            listView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
//            listView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
//            refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新时间:" + str);
//
//            if (recordList!=null && recordList.size()>0) {
//                firstId = recordList.get(0).getMessageId()+"";
//            }else {
//                firstId = "";
//            }
//
//            ServerUtils.mGetDetailList(id+"",firstId,"0","",freshCallBack);
//
//        }

        // 上拉加载更多 业务代码
        if (refreshView.isShownFooter()) {
            listView.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
            listView.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
            listView.getLoadingLayoutProxy().setReleaseLabel("释放开始加载");
            refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后加载时间:" + str);

            if (recordList!=null && recordList.size()>0) {
                lastId = recordList.get(recordList.size()-1).getMessageId()+"";
            }else {
                lastId = "";
            }

            ServerUtils.mGetDetailList(id,lastId,"1","",addCallBack);
        }
    }


    public RequestCallBack<String> message = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            DetailList details = JsonUtils.jsonToPojo(json, DetailList.class);

            if (details != null && details.getData()!=null) {
                List<List<Record>> listlist = details.getData();

                for(List<Record> list : listlist) {
                    for (int i = 0; i < list.size(); i++) {
                        Record record = list.get(i);
                        if (i == 0) {
                            record.setShow(true);
                        }else {
                            record.setShow(false);
                        }
                        recordList.add(record);
                    }
                }


                switch (details.getStatus()) {
                    case 200://请求成功
                        if (recordList != null && recordList.size() > 0) {
                            if (adapter == null) {
                                listView.setVisibility(View.VISIBLE);
                                linearLayout.setVisibility(View.GONE);
                                adapter = new DetailAdapter(DetailActivity.this,recordList);
                                listView.setAdapter(adapter);
                            }
                        }else {
                            listView.setVisibility(View.GONE);
                            linearLayout.setVisibility(View.VISIBLE);
                        }
                        break;
                    case 400://客户端请求错误
                        listView.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.VISIBLE);
                        listView.onRefreshComplete();
                        break;
                    case 444://session过期,跳转到登录界面
                        listView.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.VISIBLE);
                        listView.onRefreshComplete();
                        WinToast.toast(DetailActivity.this, "登录过期,请重新登录");
                        finish();
                        break;
                    case 500://服务端错误
                        listView.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.VISIBLE);
                        listView.onRefreshComplete();
                        break;
                }
            }
            if ( recordList == null || recordList.size()==0 ) {
                listView.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                listView.onRefreshComplete();
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
            WinToast.toast(DetailActivity.this, "动态信息获取失败");
        }
    };


    public RequestCallBack<String> addCallBack = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            DetailList details = JsonUtils.jsonToPojo(json, DetailList.class);

            switch (details.getStatus()) {
                case 200://请求成功
                    listView.onRefreshComplete();
                    if (details != null && details.getData()!=null) {
                        List<List<Record>> listlist = details.getData();
                        if (listlist != null && listlist.size()>0) {
                            for(List<Record> list : listlist) {
                                for (int i = 0; i < list.size(); i++) {
                                    Record record = list.get(i);
                                    if (i == 0) {
                                        record.setShow(true);
                                    }else {
                                        record.setShow(false);
                                    }
                                    recordList.add(record);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }else {
                            WinToast.toast(DetailActivity.this, "没有更多动态信息");
                        }
                    }

                    break;
                case 400://客户端请求错误
                    listView.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                    listView.onRefreshComplete();
                    break;
                case 444://session过期,跳转到登录界面
                    listView.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                    listView.onRefreshComplete();
                    WinToast.toast(DetailActivity.this, "登录过期,请重新登录");
                    finish();
                    break;
                case 500://服务端错误
                    listView.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                    listView.onRefreshComplete();
                    break;
            }

        }

        @Override
        public void onFailure(HttpException e, String s) {
            WinToast.toast(DetailActivity.this, "动态信息获取失败");
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
