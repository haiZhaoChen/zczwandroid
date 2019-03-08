package org.bigdata.zczw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.bigdata.zczw.R;
import org.bigdata.zczw.Singleton;
import org.bigdata.zczw.adapter.DeGroupListAdapter;
import org.bigdata.zczw.entity.GroupInfo;
import org.bigdata.zczw.entity.Groups;
import org.bigdata.zczw.rong.MyConversationBehaviorListener;
import org.bigdata.zczw.ui.LoadingDialog;
import org.bigdata.zczw.ui.WinToast;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.ServerUtils;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Group;
/*
* 群组列表
* */
public class GroupListActivity extends AppCompatActivity {

    private ListView mGroupListView;
    private DeGroupListAdapter mDemoGroupListAdapter;
    private LoadingDialog mDialog;
    private List<Group> allGropulist;//融云

    private List<GroupInfo> myGroupInfo;//本地

    private boolean allgroup;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 100){
                RongIM.setGroupInfoProvider(new RongIM.GroupInfoProvider() {
                    @Override
                    public Group getGroupInfo(String s) {
                        return Singleton.getInstance().getGroupById(s);
                    }
                },true);
            }
            RongIM.setConversationBehaviorListener(new MyConversationBehaviorListener());
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        AppManager.getAppManager().addActivity(this);
        initView();
//        initDate();

        RongIM.setGroupInfoProvider(new RongIM.GroupInfoProvider() {
            @Override
            public Group getGroupInfo(String s) {
                return Singleton.getInstance().getGroupById(s);
            }
        }, true);

    }

    @Override
    protected void onStart() {
        initDate();
        super.onStart();
    }

    private void initView() {
        getSupportActionBar().setTitle("选择群组");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);
        mGroupListView = (ListView) findViewById(R.id.de_group_list);
        mDialog = new LoadingDialog(this);

        mGroupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Group result = allGropulist.get(position);
                if (result != null) {
                    RongIM.getInstance().startGroupChat(GroupListActivity.this, result.getId(), result.getName());
                }
            }
        });
    }

    private void initDate() {
        if (mDialog != null) {
            mDialog.show();
        }
        allgroup = getIntent().getBooleanExtra("group", false);
        ServerUtils.getMyCircle(1, group);

    }

    RequestCallBack<String> group = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            Groups groups = JsonUtils.jsonToPojo(json, Groups.class);
            switch (groups.getStatus()) {
                case 200://获取成功
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    allGropulist = new ArrayList<>();
                    myGroupInfo = groups.getData();
                    for (int i = 0; i < myGroupInfo.size(); i++) {
                        allGropulist.add(new Group(myGroupInfo.get(i).getId() + "", myGroupInfo.get(i).getName(), null));
                    }
                    Singleton.getInstance().setGrouplist(allGropulist);
                    Singleton.getInstance().setMyGroups(myGroupInfo);
                    handler.sendEmptyMessage(100);
                    adapterInit();
//                    RongIM.getInstance().syncGroup(allGropulist, new RongIMClient.OperationCallback() {
//
//                        @Override
//                        public void onSuccess() {
//                            Log.e("GroupListActivity", "同步群组信息成功！");
//
//                        }
//
//                        @Override
//                        public void onError(RongIMClient.ErrorCode arg0) {
//                            Log.e("GroupListActivity", "同步群组信息错误！");
//                        }
//                    });
//                    adapterInit();
                    break;
                case 400://客户端错误
                    WinToast.toast(getApplicationContext(), "客户端错误");
                    break;
                case 444://登陆过期
                    WinToast.toast(getApplicationContext(), "登录过期");
                    break;
                case 500://服务器错误
                    WinToast.toast(getApplicationContext(), "服务器错误");
                    break;
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };

    private void adapterInit() {
        mDemoGroupListAdapter = new DeGroupListAdapter(this, myGroupInfo);
        mDemoGroupListAdapter.setOnItemButtonClick(new DeGroupListAdapter.OnItemButtonClick() {
            @Override
            public boolean onButtonClick(int position, View view) {
                GroupInfo result = mDemoGroupListAdapter.getItem(position);
                if (result == null) {
                    return false;
                }
                RongIM.getInstance().startGroupChat(GroupListActivity.this, result.getId(), result.getName());
                return true;
            }
        });
        mGroupListView.setAdapter(mDemoGroupListAdapter);
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.de_add_tag, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.tag_new:
                Intent intent = new Intent(GroupListActivity.this,UserSelectActivity.class);
                intent.putExtra("type","group");
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MainActivity.class));
        super.onBackPressed();
    }
}
