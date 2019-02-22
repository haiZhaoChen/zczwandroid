package org.bigdata.zczw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.bigdata.zczw.R;
import org.bigdata.zczw.adapter.TrendComAdapter;
import org.bigdata.zczw.entity.Zan;
import org.bigdata.zczw.entity.ZanBean;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.ServerUtils;
import org.bigdata.zczw.utils.Utils;
import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;
/*
* buyongle
* */
public class TrendComActivity extends AppCompatActivity implements PullToRefreshBase.OnRefreshListener<ListView>{


    private PullToRefreshListView listView;
    private String type;
    private List<Zan> zanList;
//    private TrendAdapter trendAdapter;
    private TrendComAdapter trendComAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trend);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);
        AppManager.getAppManager().addActivity(this);

        initView();
    }

    private void initView() {
        listView = (PullToRefreshListView) findViewById(R.id.listView_trend_act);

        zanList = new ArrayList<>();
        type = getIntent().getStringExtra("type");
        if (type .equals("zan")) {
            getSupportActionBar().setTitle("赞");
            ServerUtils.getZanList("","0","20",zanCallBack);
        }else if(type.equals("ping")){
            getSupportActionBar().setTitle("评论");
            ServerUtils.getComList("","0","20",zanCallBack);
        }else{
            getSupportActionBar().setTitle("@我的");
            ServerUtils.getAtMe("","0","20",zanCallBack);
        }

        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TrendComActivity.this,MessageActivity.class);
                intent.putExtra("msg",zanList.get(position-1).getMessage().getMessageId());
                intent.putExtra("record",zanList.get(position-1).getMessage());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        // 下拉刷新 业务代码
        if (refreshView.isShownHeader()) {
            listView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
            listView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
            listView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
            if (zanList != null && zanList.size()>0) {
                if (type .equals("zan")) {
                    ServerUtils.getZanList(zanList.get(0).getId(),"0","20",freshCallBack);
                }else if(type.equals("ping")){
                    ServerUtils.getComList(zanList.get(0).getId(),"0","20",freshCallBack);
                }else{
                    ServerUtils.getAtMe(zanList.get(0).getId(),"0","20",freshCallBack);
                }
            }else {
                if (type .equals("zan")) {
                    ServerUtils.getZanList("","0","20",freshCallBack);
                }else if(type.equals("ping")){
                    ServerUtils.getComList("","0","20",freshCallBack);
                }else{
                    ServerUtils.getAtMe("","0","20",freshCallBack);
                }
            }
        }

        // 上拉加载更多 业务代码
        if (refreshView.isShownFooter()) {
            listView.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
            listView.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
            listView.getLoadingLayoutProxy().setReleaseLabel("释放开始加载");
            if (zanList != null && zanList.size()>0) {
                if (type .equals("zan")) {
                    ServerUtils.getZanList(zanList.get(zanList.size()-1).getId(),"1","20",addCallBack);
                }else if(type.equals("ping")){
                    ServerUtils.getComList(zanList.get(zanList.size()-1).getId(),"1","20",addCallBack);
                }else{
                    ServerUtils.getAtMe(zanList.get(zanList.size()-1).getId(),"1","20",addCallBack);
                }
            }else {
                Utils.showToast(TrendComActivity.this,"没有更多消息");
            }
        }
    }


    private RequestCallBack<String> zanCallBack = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            ZanBean zanBean = JsonUtils.jsonToPojo(json,ZanBean.class);
            if (zanBean != null && zanBean.getStatus() == 200) {
                zanList = (ArrayList<Zan>) zanBean.getData();
//                for (int i = 0; i < list.size(); i++) {
//                    zanList.add(list.get(i));
//                    if (list.get(i).getParentComment() != null) {
//                        zanList.add(list.get(i).getParentComment());
//                    }
//                }

                trendComAdapter = new TrendComAdapter(TrendComActivity.this,zanList,2);
                listView.setAdapter(trendComAdapter);
            }else{
                Utils.showToast(TrendComActivity.this,"暂无数据");
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
        }
    };
 private RequestCallBack<String> freshCallBack = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            listView.onRefreshComplete();
            String json = responseInfo.result;
            ZanBean zanBean = JsonUtils.jsonToPojo(json,ZanBean.class);
            if (zanBean != null && zanBean.getStatus() == 200) {
                ArrayList<Zan> list = (ArrayList<Zan>) zanBean.getData();
                zanList.addAll(0,list);
                trendComAdapter.notifyDataSetChanged();
            }else{
                Utils.showToast(TrendComActivity.this,"暂无数据");
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
        }
    };
 private RequestCallBack<String> addCallBack = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            listView.onRefreshComplete();
            String json = responseInfo.result;
            ZanBean zanBean = JsonUtils.jsonToPojo(json,ZanBean.class);
            if (zanBean != null && zanBean.getStatus() == 200) {
                ArrayList<Zan> list = (ArrayList<Zan>) zanBean.getData();
                zanList.addAll(list);
                trendComAdapter.notifyDataSetChanged();
            }else{
                Utils.showToast(TrendComActivity.this,"暂无数据");
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
