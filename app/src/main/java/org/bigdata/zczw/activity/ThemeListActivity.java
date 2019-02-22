package org.bigdata.zczw.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.bigdata.zczw.R;
import org.bigdata.zczw.adapter.ThemeListAdapter;
import org.bigdata.zczw.entity.Theme;
import org.bigdata.zczw.entity.Themes;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.ServerUtils;
import org.bigdata.zczw.utils.Utils;
import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

/*
* 我的主题列表
* */

public class ThemeListActivity extends AppCompatActivity implements PullToRefreshBase.OnRefreshListener<ListView>{

    private PullToRefreshListView listView;
    private ArrayList<Theme> themeArrayList;
    private ThemeListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_list);

        getSupportActionBar().setTitle("我的主题");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);
        AppManager.getAppManager().addActivity(this);

        listView = (PullToRefreshListView) findViewById(R.id.list_theme_list_act);

        themeArrayList= new ArrayList<>();

        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ThemeListActivity.this, ThemeActivity.class);
                intent.putExtra("msg",themeArrayList.get(position-1).getId());
                startActivity(intent);
            }
        });

        ServerUtils.getThemeList("","20","0",callBack);
    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        // 下拉刷新 业务代码
        if (refreshView.isShownHeader()) {
            listView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
            listView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
            listView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
            String firstId = null;
            if (themeArrayList != null) {
                firstId = themeArrayList.get(0).getId();
            }
            if (!TextUtils.isEmpty(firstId)) {
                ServerUtils.getThemeList(firstId,"20","0",freshCallBack);
            } else {
                ServerUtils.getThemeList("","20","0",callBack);
            }
        }

        // 上拉加载更多 业务代码
        if (refreshView.isShownFooter()) {
            listView.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
            listView.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
            listView.getLoadingLayoutProxy().setReleaseLabel("释放开始加载");
            String lastId = null;
            if (themeArrayList != null) {
                lastId = themeArrayList.get(themeArrayList.size()-1).getId();
            }
            if (!TextUtils.isEmpty(lastId)) {
                ServerUtils.getThemeList(lastId,"20","1",addCallBack);
            } else {
                Utils.showToast(ThemeListActivity.this,"暂无更多");
            }
        }
    }


    private RequestCallBack<String> callBack = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            Themes themes = JsonUtils.jsonToPojo(json,Themes.class);
            listView.onRefreshComplete();
            if (themes != null) {
                if (themes.getStatus() == 200) {
                    themeArrayList = (ArrayList<Theme>) themes.getData();
                    adapter = new ThemeListAdapter(ThemeListActivity.this,themeArrayList);
                    listView.setAdapter(adapter);

                }else if (themes.getStatus() == 400) {
                    Utils.showToast(ThemeListActivity.this,"没有更新的数据了");
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
            Themes themes = JsonUtils.jsonToPojo(json,Themes.class);
            listView.onRefreshComplete();
            if (themes != null) {
                if (themes.getStatus() == 200) {
                    ArrayList<Theme> list = (ArrayList<Theme>) themes.getData();
                    themeArrayList.addAll(0,list);
                    adapter.notifyDataSetChanged();
                }else if (themes.getStatus() == 400) {
                    Utils.showToast(ThemeListActivity.this,"没有更新的数据了");
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
            Themes themes = JsonUtils.jsonToPojo(json,Themes.class);
            listView.onRefreshComplete();
            if (themes != null) {
                if (themes.getStatus() == 200) {
                    if (themeArrayList != null) {
                        ArrayList<Theme> list = (ArrayList<Theme>) themes.getData();
                        themeArrayList.addAll(list);
                        adapter.notifyDataSetChanged();
                    }
                }else {
                    Utils.showToast(ThemeListActivity.this,"没有更多数据了");
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
