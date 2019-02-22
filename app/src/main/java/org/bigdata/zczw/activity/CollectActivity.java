package org.bigdata.zczw.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.bigdata.zczw.App;
import org.bigdata.zczw.R;
import org.bigdata.zczw.adapter.HomeListAdapter;
import org.bigdata.zczw.entity.Author;
import org.bigdata.zczw.entity.DemoApiJSON;
import org.bigdata.zczw.entity.FeedEntity;
import org.bigdata.zczw.entity.Record;
import org.bigdata.zczw.ui.LoadingDialog;
import org.bigdata.zczw.ui.WinToast;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.SPUtil;
import org.bigdata.zczw.utils.ServerUtils;
import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* 我的收藏
* */

public class CollectActivity extends AppCompatActivity implements PullToRefreshBase.OnRefreshListener<ListView> {

    private PullToRefreshListView pListView;// 动态列表
    private LoadingDialog mDialog;
    private List<Record> recordList = new ArrayList<>();
    private HomeListAdapter itemAdapter;//适配器

    public static int TYPE = 1;

    public static Map<Integer, Boolean> checkMap;

    //类型(上拉,下拉,默认)
    private int type = 2;
    private String messageidbefore;
    private String messageidlate;
    //点赞操作的评论的id
    private long messageId;
    private int prasieCount;
    private int collectCount;

    private Author author;
    private Record record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        AppManager.getAppManager().addActivity(this);

        initView();
    }

    private void initView() {
        getSupportActionBar().setTitle("收藏");
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);// 使得音量键控制媒体声音
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);

        pListView = (PullToRefreshListView) findViewById(R.id.plistview_collect);
        checkMap = new HashMap<Integer, Boolean>();

        if (mDialog != null) {
            mDialog.show();
        } else {
            mDialog = new LoadingDialog(this);
            mDialog.show();
        }
        //初始获取动态数据
        ServerUtils.getCollectList("", "", "", message);
        /*
         * 设置PullToRefresh刷新模式 BOTH:上拉刷新和下拉刷新都支持 DISABLED：禁用上拉下拉刷新
		 * PULL_FROM_START:仅支持下拉刷新（默认） PULL_FROM_END：仅支持上拉刷新
		 * MANUAL_REFRESH_ONLY：只允许手动触发
		 */
        pListView.setMode(PullToRefreshBase.Mode.BOTH);
        pListView.setOnRefreshListener(this);
        pListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CollectActivity.this, MessageActivity.class);
                intent.putExtra("msg", recordList.get(position - 1).getMessageId());
                intent.putExtra("record",recordList.get(position-1));
                startActivityForResult(intent, 101);
            }
        });
    }

    public RequestCallBack<String> message = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
//            Log.e("1111", "message: " + json);
            FeedEntity feedEntity = JsonUtils.jsonToPojo(json, FeedEntity.class);
            recordList = feedEntity.getData();
            switch (feedEntity.getStatus()) {
                case 200://请求成功
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    //为FeedEntity装载数据
                    itemAdapter = new HomeListAdapter(CollectActivity.this, recordList,"collect");
                    // 绑定适配器
                    pListView.setAdapter(itemAdapter);
                    itemAdapter.setOnCheckBarClickListener(onCheckBarClickListener);
                    break;
                case 400://客户端请求错误
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    pListView.onRefreshComplete();
                    break;
                case 444://session过期,跳转到登录界面
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    pListView.onRefreshComplete();
                    startActivity(new Intent(CollectActivity.this, LoginActivity.class));
                    WinToast.toast(CollectActivity.this, "登录过期,请重新登录");
                    finish();
                    break;
                case 500://服务端错误
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    WinToast.toast(CollectActivity.this, feedEntity.getMsg());
                    pListView.onRefreshComplete();
                    break;
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
            if (mDialog != null)
                mDialog.dismiss();
            pListView.onRefreshComplete();
            WinToast.toast(CollectActivity.this, "动态信息获取失败");
        }
    };

    public RequestCallBack<String> messageRefresh = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
//            Log.e("1111", "messageRefresh: "+json);
            FeedEntity feedEntity = JsonUtils.jsonToPojo(json, FeedEntity.class);
            List<Record> list = feedEntity.getData();
            switch (feedEntity.getStatus()) {
                case 200://请求成功
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    if(TYPE ==0){
                        if (list.size()>0) {
                            recordList.addAll(0,list);
                            itemAdapter.notifyDataSetChanged();
                        }else{
                            WinToast.toast(CollectActivity.this, "没有更新的数据啦");
                        }
                    }else{
                        if (list.size()>0) {
                            recordList.addAll(list);
                            itemAdapter.notifyDataSetChanged();
                        }else{
                            WinToast.toast(CollectActivity.this, "已经到最后一页了");
                        }
                    }
                    pListView.onRefreshComplete();
                    break;
                case 400://客户端请求错误
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    pListView.onRefreshComplete();
                    break;
                case 444://session过期,跳转到登录界面
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    pListView.onRefreshComplete();
                    startActivity(new Intent(CollectActivity.this, LoginActivity.class));
                    WinToast.toast(CollectActivity.this, "登录过期,请重新登录");
                    finish();
                    break;
                case 500://服务端错误
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    WinToast.toast(CollectActivity.this, feedEntity.getMsg());
                    pListView.onRefreshComplete();
                    break;
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
            if (mDialog != null)
                mDialog.dismiss();
            pListView.onRefreshComplete();
            WinToast.toast(CollectActivity.this, "动态信息获取失败");
        }
    };

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {

        String str = DateUtils.formatDateTime(CollectActivity.this,
                System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
        // 下拉刷新 业务代码
        if (refreshView.isShownHeader()) {
            pListView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
            pListView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
            pListView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
            refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新时间:" + str);
            String fristMessageId = null;
            if (itemAdapter != null) {
                fristMessageId = itemAdapter.getFirstMessageId();
            }
            if (fristMessageId != null) {
                messageidbefore = fristMessageId;
                TYPE = 0;
                ServerUtils.getCollectList(fristMessageId, "10", 0 + "", messageRefresh);
            } else {
                WinToast.toast(CollectActivity.this, "没有新数据");
                pListView.onRefreshComplete();
            }
        }

        // 上拉加载更多 业务代码
        if (refreshView.isShownFooter()) {
            pListView.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
            pListView.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
            pListView.getLoadingLayoutProxy().setReleaseLabel("释放开始加载");
            refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后加载时间:" + str);
            String lastMessageId = null;
            if (itemAdapter != null) {
                lastMessageId = itemAdapter.getLastMessageId();
            }
            if (lastMessageId != null) {
                messageidbefore = lastMessageId;
                TYPE = 1;
                ServerUtils.getCollectList(lastMessageId, "10", 1 + "", messageRefresh);
            } else {
                WinToast.toast(CollectActivity.this, "翻页出错");
                pListView.onRefreshComplete();
            }
        }
    }

    HomeListAdapter.onCheckBarClickListener onCheckBarClickListener = new HomeListAdapter.onCheckBarClickListener() {
        @Override
        public void onCommentsClick(String type, Record recordMessage) {
            record = recordMessage;
            switch (type) {
                case "0"://评论
                    Intent intent = new Intent(CollectActivity.this, CommentsActivity.class);//查看评论
                    intent.putExtra("messageId", recordMessage.getMessageId() + "");
                    intent.putExtra("commentCount", recordMessage.getCommentNum() + "");
                    startActivityForResult(intent, 2);
                    break;
                case "1"://收藏
                    messageId = recordMessage.getMessageId();
                    if (recordMessage.getCollectNum() < 0) {
                        collectCount = 1;
                    }else{
                        collectCount = recordMessage.getCollectNum()+1;
                    }
                    ServerUtils.collectMessage(messageId, 1, messageCollect);
                    break;
                case "12"://取消收藏
                    messageId = recordMessage.getMessageId();
                    if (recordMessage.getCollectNum() == 0) {
                        collectCount = 0;
                    }else{
                        collectCount = recordMessage.getCollectNum()-1;
                    }
                    ServerUtils.collectMessage(messageId, 2, messageCollect);
                    break;
                case "2"://赞
                    messageId = recordMessage.getMessageId();
                    prasieCount = recordMessage.getPraiseNum()+1;
                    ServerUtils.prasieMessage(messageId, 1, messagePraise);
                    break;
                case "22"://取消赞
                    messageId = recordMessage.getMessageId();
                    prasieCount = recordMessage.getPraiseNum()-1;
                    ServerUtils.prasieMessage(messageId, 2, messagePraise);
                    break;
                case "3"://点击头像
                    String authorId =recordMessage.getAuthor().getId()+"";
                    if(authorId.equals(SPUtil.getString(CollectActivity.this, App.USER_ID))){
                        startActivity(new Intent(CollectActivity.this, UserInfoActivity.class));
                    }else{
                        Intent authorIntent = new Intent(CollectActivity.this, PersonalActivity.class);
                        authorIntent.putExtra("PERSONAL",authorId);
                        startActivity(authorIntent);
                    }
                    break;
                case "41"://删除
                    messageId = recordMessage.getMessageId();
                    deleteDialog();
                    break;
                case "42"://举报
                    errorDialog();
                    break;
                case "43"://联系此人
                    author = recordMessage.getAuthor();
                    callDialog();
                    break;
                case "6"://详情
                    Intent intentContent = new Intent(CollectActivity.this, MessageActivity.class);
                    intentContent.putExtra("msg",recordMessage.getMessageId());
                    intentContent.putExtra("record",recordMessage);
                    startActivityForResult(intentContent, 101);
                    break;
        }
    }};

        RequestCallBack<String> messageCollect = new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String json = responseInfo.result;
//                Log.e("1111", "onSuccess: "+json);
                DemoApiJSON demoApiJSON = JsonUtils.jsonToPojo(json, DemoApiJSON.class);
                if(demoApiJSON.getStatus() != 500){
                    itemAdapter.refreshCollectCount(messageId, collectCount);
                }else{
                    if (collectCount <=record.getCollectNum()) {//执行取消收藏操作
                        Log.d("1111", "执行取消收藏操作: ");
                        itemAdapter.refreshCollectCount(messageId, collectCount);
                    }else{//执行收藏操作
                        Log.d("1111", "执行收藏操作: ");
                        itemAdapter.refreshCollectCount(messageId, collectCount);
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                WinToast.toast(CollectActivity.this, "收藏失败");
            }
        };

    RequestCallBack<String> messagePraise = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            DemoApiJSON demoApiJSON = JsonUtils.jsonToPojo(json, DemoApiJSON.class);
            switch (demoApiJSON.getStatus()) {
                case 200://成功
                    itemAdapter.refreshPraiseCount(messageId, prasieCount);
                    break;
                case 400://已经赞过
                    itemAdapter.refreshPraiseCount(messageId, prasieCount);
                    break;
                case 444://登录过期
                    WinToast.toast(CollectActivity.this, "登录过期");
                    break;
                case 500://服务器错误
                    WinToast.toast(CollectActivity.this, "服务器错误");
                    break;
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
            WinToast.toast(CollectActivity.this, e.getMessage());

        }
    };

    RequestCallBack<String> messageDelete = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            DemoApiJSON demoApiJSON = JsonUtils.jsonToPojo(json, DemoApiJSON.class);
            if(demoApiJSON.getMsg().equals("OK")){
                itemAdapter.refreshMsg(messageId);
            }else{
                WinToast.toast(CollectActivity.this, demoApiJSON.getMsg());
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
            WinToast.toast(CollectActivity.this, e.getMessage());

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        CollectActivity.this.getIntent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if (data == null) {
            return;
        }
        boolean change = data.getBooleanExtra("change",false);
        if (change){
            switch (requestCode) {
                case 2:
                    String messageId = data.getStringExtra("messageId");
                    String commentCount = data.getStringExtra("commentNum");
                    itemAdapter.refreshCommentCount(Long.parseLong(messageId), commentCount);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void callDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CollectActivity.this);
        builder.setMessage("联系人："+ author.getName()+"\n联系电话："+ author.getPhone());
        builder.setTitle("是否拨打电话联系此人？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String phoneStr = author.getPhone();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phoneStr.substring(0, 11)));
                //开启系统拨号器
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }
    private void deleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CollectActivity.this);
        builder.setMessage("是否删除本动态？");
        builder.setTitle("删除动态提醒：");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ServerUtils.deleteMessage(messageId,messageDelete);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }
    private void errorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CollectActivity.this);
        builder.setMessage("如此用户发送的内容含有非法、暴力、色情等不合规信息，可点击提交，管理人员会在24小时内进行审核并对不合规内容删除处理。");
        builder.setTitle("提示：");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }
}
