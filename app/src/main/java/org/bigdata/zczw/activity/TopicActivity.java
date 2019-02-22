package org.bigdata.zczw.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.bigdata.zczw.App;
import org.bigdata.zczw.R;
import org.bigdata.zczw.adapter.TopicAdapter;
import org.bigdata.zczw.entity.FeedEntity;
import org.bigdata.zczw.entity.Record;
import org.bigdata.zczw.entity.TopicNum;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.ServerUtils;
import org.bigdata.zczw.utils.Utils;
import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
/*
* 话题
* */
public class TopicActivity extends AppCompatActivity implements PullToRefreshBase.OnRefreshListener<ListView>, View.OnClickListener, AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    private static final String TAG ="TopicActivity";
    private static final Uri PROFILE_URI = Uri.parse(App.TOPIC_SCHEMA);

    private View convertView;
    private TextView title,num,read,name;
    private LinearLayout llBack,llAdd;

    private String uid;
    private PullToRefreshListView listView;
    private ArrayList<Record> recordList = new ArrayList<>();
    private TopicAdapter itemAdapter;//适配器

    private boolean isAlphaZero = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        getSupportActionBar().hide();
        AppManager.getAppManager().addActivity(this);

        listView = (PullToRefreshListView) findViewById(R.id.plistview_topic_act);
        llAdd = (LinearLayout) findViewById(R.id.ll_add_new_topic);

        convertView = LayoutInflater.from(this).inflate(R.layout.item_topic_header,null);

        title = (TextView) convertView.findViewById(R.id.txt_name_topic_header_item);
        num = (TextView) convertView.findViewById(R.id.txt_num_topic_header_item);
        read = (TextView) convertView.findViewById(R.id.txt_read_topic_header_item);

        name = (TextView) findViewById(R.id.txt_topic_name_topic_act);
        llBack = (LinearLayout) findViewById(R.id.ll_back);


        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(this);
        listView.setOnItemClickListener(this);
        listView.setOnScrollListener(this);
        llBack.setOnClickListener(this);
        llAdd.setOnClickListener(this);

        recordList = new ArrayList<>();

        Uri uri = getIntent().getData();
        if (uri !=null&& PROFILE_URI.getScheme().equals(uri.getScheme())) {

            String string = uri.toString();
            String str[] = string.split("#");

            uid = str[1];

            title.setText("话题讨论 | #"+uid+"#");
            name.setText(uid);

            ServerUtils.getTopicNum(uid,numCallback);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position >1) {
            Intent intent8 = new Intent(TopicActivity.this, MessageActivity.class);
            intent8.putExtra("msg",recordList.get(position-2).getMessageId());
            intent8.putExtra("record",recordList.get(position));
            startActivityForResult(intent8,101);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_add_new_topic:
                Intent intent = new Intent(TopicActivity.this, MsgTagActivity.class);
                intent.putExtra("topic",uid);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        String str = DateUtils.formatDateTime(TopicActivity.this, System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
        // 下拉刷新 业务代码
        if (refreshView.isShownHeader()) {
            listView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
            listView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
            listView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
            refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新时间:" + str);
            if (recordList != null && recordList.size()>0) {
                ServerUtils.getTopicList("",uid,recordList.get(0).getMessageId()+"","0","20","10",freshCallback);
            }else {
                ServerUtils.getTopicList("",uid,"","0","20","10",callback);
            }
        }

        // 上拉加载更多 业务代码
        if (refreshView.isShownFooter()) {
            listView.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
            listView.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
            listView.getLoadingLayoutProxy().setReleaseLabel("释放开始加载");
            refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后加载时间:" + str);
            if (recordList != null && recordList.size()>0) {
                ServerUtils.getTopicList("",uid,recordList.get(recordList.size()-1).getMessageId()+"","1","20","10",addCallback);
            }else {
                ServerUtils.getTopicList("",uid,"","0","20","10",callback);
            }
        }
    }

    private RequestCallBack<String> callback = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            FeedEntity feedEntity = JsonUtils.jsonToPojo(json, FeedEntity.class);
            if (feedEntity != null) {
                if (feedEntity.getData()!=null && feedEntity.getData().size()>0) {
                    recordList = (ArrayList<Record>) feedEntity.getData();
                }
                ListView list = listView.getRefreshableView();
                list.addHeaderView(convertView);

                itemAdapter = new TopicAdapter(TopicActivity.this, recordList);
                itemAdapter.setOnFeedClickListener(listener);

                // 绑定适配器
                listView.setAdapter(itemAdapter);
            }

        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };


    private RequestCallBack<String> callback0 = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            FeedEntity feedEntity = JsonUtils.jsonToPojo(json, FeedEntity.class);
            if (feedEntity != null) {
                if (feedEntity.getData()!=null && feedEntity.getData().size()>0) {
                    recordList.clear();
                    recordList.addAll(feedEntity.getData()) ;
                    itemAdapter.notifyDataSetChanged();
                }
            }

        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };

    private TopicAdapter.onFeedClickListener listener = new TopicAdapter.onFeedClickListener() {
        @Override
        public void onFeedClick(int position) {
            Intent intent8 = new Intent(TopicActivity.this, MessageActivity.class);
            intent8.putExtra("msg",recordList.get(position).getMessageId());
            intent8.putExtra("record",recordList.get(position));
            startActivityForResult(intent8,101);
        }
    };

    private RequestCallBack<String>  freshCallback= new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            FeedEntity feedEntity = JsonUtils.jsonToPojo(json, FeedEntity.class);
            listView.onRefreshComplete();
            if (feedEntity != null) {
                if (feedEntity.getData()!=null && feedEntity.getData().size()>0) {
                    recordList.addAll(0,feedEntity.getData()) ;
                    itemAdapter.notifyDataSetChanged();
                }else {
                    Utils.showToast(TopicActivity.this,"没有更新的动态了");
                }
            }

        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };

    private RequestCallBack<String> addCallback = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            FeedEntity feedEntity = JsonUtils.jsonToPojo(json, FeedEntity.class);
            listView.onRefreshComplete();
            if (feedEntity != null) {
                if (feedEntity.getData()!=null && feedEntity.getData().size()>0) {
                    recordList.addAll(feedEntity.getData()) ;
                    itemAdapter.notifyDataSetChanged();
                }else {
                    Utils.showToast(TopicActivity.this,"没有更多动态了");
                }
            }

        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };

    private RequestCallBack<String> numCallback = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            TopicNum topicNum = JsonUtils.jsonToPojo(json, TopicNum.class);
            if (topicNum != null) {
                if (topicNum.getData()!=null && topicNum.getData().size()>0) {
                    num.setText("帖子 "+topicNum.getData().get(0));
                    read.setText("阅读 "+topicNum.getData().get(1));

                    /**
                     * 话题动态列表
                     * topicId	Long	话题id
                     * id	Long	编号，用于分页对应MessageInfo.id
                     * type	int	0为下拉刷新，1为上拉加载更多，默认为0
                     * pageSize	int	每页显示数量（默认为20）
                     * publicScope	int	0:未知; 1:特定; 5:部门; 10:全部，默认0     */
                    ServerUtils.getTopicList("",uid,"","0","20","10",callback);
                }
            }

        }

        @Override
        public void onFailure(HttpException e, String s) {
            Log.e(TAG, "onFailure: "+s);
        }
    };

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem == 1 && !isAlphaZero) {
            AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);//初始化操作，参数传入0和1，即由透明度0变化到透明度为1
            name.startAnimation(alphaAnimation);//开始动画
            alphaAnimation.setFillAfter(true);//动画结束后保持状态
            alphaAnimation.setDuration(800);//动画持续时间，单位为毫秒
            isAlphaZero = true;//标识位
        }
        if (firstVisibleItem == 0 && isAlphaZero) {
            AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);//初始化操作，参数传入1和0，即由透明度1变化到透明度为0
            name.startAnimation(alphaAnimation);//开始动画
            isAlphaZero = false;//标识位
            alphaAnimation.setFillAfter(true);//动画结束后保持状态
            alphaAnimation.setDuration(800);//动画持续时间
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 2001 && data!=null) {
            ServerUtils.getTopicList("",uid,"","0","20","10",callback0);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
