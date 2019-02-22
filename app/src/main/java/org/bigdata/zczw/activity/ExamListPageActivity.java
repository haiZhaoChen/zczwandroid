package org.bigdata.zczw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.bigdata.zczw.R;
import org.bigdata.zczw.adapter.ExamPreAdapter;
import org.bigdata.zczw.entity.ExamPreModel;
import org.bigdata.zczw.entity.ExamPreModelBean;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.ServerUtils;
import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

public class ExamListPageActivity extends AppCompatActivity implements PullToRefreshBase.OnRefreshListener<ListView> {


    private PullToRefreshListView listView;
    private ArrayList<ExamPreModel> dataSource;
    private ExamPreAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_list);

        //设置属性
        getSupportActionBar().setTitle("考试列表");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);
        AppManager.getAppManager().addActivity(this);
        //初始化页面和数据
        initView();
        initData();

    }



    private void initView() {

        listView = (PullToRefreshListView) findViewById(R.id.list_exam_act);
        dataSource = new ArrayList<ExamPreModel>();

        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        listView.setOnRefreshListener(this);
        listView.setScrollingWhileRefreshingEnabled(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                Intent intent = new Intent(ExamListPageActivity.this,ExamInfoPageActivity.class);
                                                ExamPreModel exam = dataSource.get(position-1);
                                                intent.putExtra("exam",exam);
                                                startActivity(intent);
                                            }
                                        }

        );
    }


    private void initData() {

        ServerUtils.getExamPageList(callback);

    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {

        // 下拉刷新 业务代码
        if (refreshView.isShownHeader()) {
            listView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
            listView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
            listView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");

            ServerUtils.getExamPageList(callback);
        }
    }


    private RequestCallBack<String> callback = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String jsonStr = responseInfo.result;
            ExamPreModelBean modelBean = JsonUtils.jsonToPojo(jsonStr,ExamPreModelBean.class);
            listView.onRefreshComplete();
            if (modelBean != null){
                if (modelBean.getStatus() == 200){
                    dataSource = (ArrayList<ExamPreModel>) modelBean.getData();
                    adapter = new ExamPreAdapter(ExamListPageActivity.this,dataSource);
                    listView.setAdapter(adapter);
                }
            }

        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }


}
