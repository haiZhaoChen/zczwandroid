package org.bigdata.zczw.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.MenuItem;
import android.widget.ListView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.bigdata.zczw.R;
import org.bigdata.zczw.adapter.IntegralScoreListAdapter;
import org.bigdata.zczw.entity.IntegralInfoModel;
import org.bigdata.zczw.entity.IntegralInfoModelBean;
import org.bigdata.zczw.entity.IntegralListModel;
import org.bigdata.zczw.ui.WinToast;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.ServerUtils;
import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

public class IntegralScoreActivity extends AppCompatActivity implements PullToRefreshBase.OnRefreshListener<ListView> {

    private PullToRefreshListView listView;
    private IntegralListModel integralModel;
    private ArrayList<IntegralInfoModel> dataList;
    private IntegralScoreListAdapter scoreAdapter;

    public static int TYPE = 1;
    private String messageidbefore;
    private String type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_integral_score);

        //设置属性
        getSupportActionBar().setTitle("积分明细");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);
        AppManager.getAppManager().addActivity(this);
        //初始化页面和数据

        initView();
        initData();

    }

    private void initView(){

        listView = (PullToRefreshListView)findViewById(R.id.list_integral_score);
        /*
         * 设置PullToRefresh刷新模式 BOTH:上拉刷新和下拉刷新都支持 DISABLED：禁用上拉下拉刷新
         * PULL_FROM_START:仅支持下拉刷新（默认） PULL_FROM_END：仅支持上拉刷新
         * MANUAL_REFRESH_ONLY：只允许手动触发
         */
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(this);


    }

    private void initData(){
        //请求数据
        integralModel = (IntegralListModel)getIntent().getSerializableExtra("integralModel");
        type = (int)getIntent().getIntExtra("type",1) +"";


        ServerUtils.getIntegralInfo("",true,type,infoCallBack);


    }

    private RequestCallBack<String> infoCallBack = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            IntegralInfoModelBean bean = JsonUtils.jsonToPojo(json,IntegralInfoModelBean.class);
            if (bean != null && bean.getStatus() == 200 && bean.getData()!=null){
                dataList = bean.getData();

                if (dataList.size()>0){
                    scoreAdapter = new IntegralScoreListAdapter(IntegralScoreActivity.this,dataList);
                    listView.setAdapter(scoreAdapter);

                }else {
                    WinToast.toast(getApplicationContext(), "没有积分数据");
                }

            }else {

                WinToast.toast(getApplicationContext(), "服务器错误");
            }

        }

        @Override
        public void onFailure(HttpException e, String s) {
            System.out.println(s+"==="+e.getLocalizedMessage());

        }
    };
    public RequestCallBack<String> messageRefresh = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            IntegralInfoModelBean integralBean = JsonUtils.jsonToPojo(json, IntegralInfoModelBean.class);
            List<IntegralInfoModel> list = integralBean.getData();
            switch (integralBean.getStatus()) {
                case 200://请求成功

                    if(TYPE ==0){
                        if (list.size()>0) {
                            dataList.addAll(0,list);
                            scoreAdapter.notifyDataSetChanged();
                        }else{
                            WinToast.toast(IntegralScoreActivity.this, "没有更新的数据啦");
                        }
                    }else{
                        if (list.size()>0) {
                            dataList.addAll(list);
                            scoreAdapter.notifyDataSetChanged();
                        }else{
                            WinToast.toast(IntegralScoreActivity.this, "已经到最后一页了");
                        }
                    }

                    listView.onRefreshComplete();
                    break;
                case 400://客户端请求错误

                    listView.onRefreshComplete();
                    break;

                case 500://服务端错误

                    listView.onRefreshComplete();
                    break;
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

            listView.onRefreshComplete();
            WinToast.toast(IntegralScoreActivity.this, "信息获取失败");
        }
    };

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {

        String str = DateUtils.formatDateTime(IntegralScoreActivity.this,
                System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
        // 下拉刷新 业务代码

        if (refreshView.isShownHeader()) {
            listView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
            listView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
            listView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
            refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新时间:" + str);

            TYPE = 0;

            if (dataList.size()>0){
                IntegralInfoModel model = dataList.get(0);
                ServerUtils.getIntegralInfo(model.getId()+"",true,type,messageRefresh);
            }else {
                ServerUtils.getIntegralInfo("",true,type,messageRefresh);
            }

        }

        // 上拉加载更多 业务代码
        if (refreshView.isShownFooter()) {
            listView.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
            listView.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
            listView.getLoadingLayoutProxy().setReleaseLabel("释放开始加载");
            refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后加载时间:" + str);
            String lastMessageId = null;
            if (scoreAdapter != null) {
                lastMessageId = scoreAdapter.getScoreListLastId();
            }
            if (lastMessageId != null) {

                TYPE = 1;

                messageidbefore = lastMessageId;
                ServerUtils.getIntegralInfo(messageidbefore,false,type,messageRefresh);
            } else {
                listView.onRefreshComplete();
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

}



