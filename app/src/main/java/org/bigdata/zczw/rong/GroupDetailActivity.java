package org.bigdata.zczw.rong;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.bigdata.zczw.App;
import org.bigdata.zczw.R;
import org.bigdata.zczw.Singleton;
import org.bigdata.zczw.activity.GroupListActivity;
import org.bigdata.zczw.activity.GroupMemberActivity;
import org.bigdata.zczw.activity.PersonalActivity;
import org.bigdata.zczw.activity.UserDelActivity;
import org.bigdata.zczw.activity.UserInfoActivity;
import org.bigdata.zczw.adapter.GridViewAdapter;
import org.bigdata.zczw.entity.DemoApiJSON;
import org.bigdata.zczw.entity.User;
import org.bigdata.zczw.ui.MyGridView;
import org.bigdata.zczw.ui.WinToast;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.SPUtil;
import org.bigdata.zczw.utils.ServerUtils;

import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

public class GroupDetailActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, AdapterView.OnItemClickListener {

    private Switch notify,top;
    private TextView clean,msgData,num,nameNote;
    private MyGridView gridView;
    private Button btnOut;

    private String targetId;
    private List<User> groupMemberList;
    private List<User> memberShowList;
    private GridViewAdapter adapter;
    private int type;
    private String creatId;
    private boolean isGroupLeader;
    private String name;
    private String loginId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group_detail);

        initView();
    }

    @Override
    protected void onStart() {
        initDate();
        super.onStart();
    }

    private void initView() {
        notify = (Switch) findViewById(R.id.switch_notify_group);
        top = (Switch) findViewById(R.id.switch_top_group);
        clean = (TextView) findViewById(R.id.msg_clean_group);
        msgData = (TextView) findViewById(R.id.msg_data);
        nameNote = (TextView) findViewById(R.id.name_note);
        num = (TextView) findViewById(R.id.user_num);
        gridView = (MyGridView) findViewById(R.id.member_group);
        btnOut = (Button) findViewById(R.id.btn_out);
    }
    private void initDate() {
        targetId = getIntent().getStringExtra("TargetId");
        name = Singleton.getInstance().getGroupNameById(targetId);
        groupMemberList = new ArrayList<>();
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);
        initSwitch();

        msgData.setText(name);
        type = Singleton.getInstance().getGroupTypeById(targetId);
        if (type == 1 ) {
            ServerUtils.mGroupInfoById(targetId, groupInfo);
            btnOut.setVisibility(View.GONE);
        }else{
            ServerUtils.myGroupInfos(targetId, myGroupInfo);
        }

        notify.setOnCheckedChangeListener(this);
        top.setOnCheckedChangeListener(this);
        clean.setOnClickListener(this);
        num.setOnClickListener(this);
        btnOut.setOnClickListener(this);
        msgData.setOnClickListener(this);

        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (type ==1) {
            if (!SPUtil.getString(GroupDetailActivity.this, App.USER_ID).equals(memberShowList.get(position).getUserid()+"")) {
                Intent intent = new Intent(GroupDetailActivity.this, PersonalActivity.class);
                intent.putExtra("PERSONAL",memberShowList.get(position).getUserid()+"");
                startActivity(intent);
            }else{
                startActivity(new Intent(GroupDetailActivity.this, UserInfoActivity.class));
            }
        }else{
            if (isGroupLeader) {
                if(position == memberShowList.size()-1){//del
                    Intent intent = new Intent(GroupDetailActivity.this, UserDelActivity.class);
                    intent.putExtra("users", (Serializable) groupMemberList);
                    intent.putExtra("targetId",targetId);
                    intent.putExtra("name",name);
                    intent.putExtra("type","del");
                    startActivity(intent);
                }else if(position == memberShowList.size()-2){//add
                    Intent intent = new Intent(GroupDetailActivity.this, UserDelActivity.class);
                    intent.putExtra("users", (Serializable) groupMemberList);
                    intent.putExtra("targetId",targetId);
                    intent.putExtra("name",name);
                    intent.putExtra("type","add");
                    startActivity(intent);
                } else {
                    if (!SPUtil.getString(GroupDetailActivity.this, App.USER_ID).equals(memberShowList.get(position).getUserid()+"")) {
                        Intent intent = new Intent(GroupDetailActivity.this, PersonalActivity.class);
                        intent.putExtra("PERSONAL",memberShowList.get(position).getUserid()+"");
                        startActivity(intent);
                    }else{
                        startActivity(new Intent(GroupDetailActivity.this, UserInfoActivity.class));
                    }
                }
            }else{
                if(position == memberShowList.size()-1){//add
                    Intent intent = new Intent(GroupDetailActivity.this, UserDelActivity.class);
                    intent.putExtra("users", (Serializable) groupMemberList);
                    intent.putExtra("targetId",targetId);
                    intent.putExtra("name",name);
                    intent.putExtra("type","add");
                    startActivity(intent);
                }else {
                    if (!SPUtil.getString(GroupDetailActivity.this, App.USER_ID).equals(memberShowList.get(position).getUserid()+"")) {
                        Intent intent = new Intent(GroupDetailActivity.this, PersonalActivity.class);
                        intent.putExtra("PERSONAL",memberShowList.get(position).getUserid()+"");
                        startActivity(intent);
                    }else{
                        startActivity(new Intent(GroupDetailActivity.this, UserInfoActivity.class));
                    }
                }
            }
        }

    }

    private RequestCallBack<String> groupInfo = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            DemoApiJSON demoApiJSON = JsonUtils.jsonToPojo(json, DemoApiJSON.class);
            switch (demoApiJSON.getStatus()) {
                case 200://群组信息获取成功
                    groupMemberList = JsonUtils.getGroup(json);
                    groupMemberList = sort(groupMemberList);
                    int size = groupMemberList.size();
                    if (size >14) {
                        memberShowList = new ArrayList<>();
                        for (int i = 0; i < 14; i++) {
                            memberShowList.add(groupMemberList.get(i));
                        }
                    }else{
                        memberShowList = new ArrayList<>();
                        for (int i = 0; i < groupMemberList.size(); i++) {
                            memberShowList.add(groupMemberList.get(i));
                        }
                    }
                    adapter = new GridViewAdapter(memberShowList, GroupDetailActivity.this);
                    gridView.setAdapter(adapter);
                    num.setText("全部群成员（"+groupMemberList.size()+"）");
                    break;
                case 444://登录过期
                    WinToast.toast(getApplicationContext(), "登录过期");
                    break;
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };
    //自由建群
    private RequestCallBack<String> myGroupInfo = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            DemoApiJSON demoApiJSON = JsonUtils.jsonToPojo(json, DemoApiJSON.class);
            switch (demoApiJSON.getStatus()) {
                case 200://群组信息获取成功
                    groupMemberList = JsonUtils.getGroup(json);
                    creatId = JsonUtils.getId(json);
                    groupMemberList = sort(groupMemberList);
                    int size = groupMemberList.size();
                    if (size >14) {
                        memberShowList = new ArrayList<>();
                        for (int i = 0; i < 14; i++) {
                            memberShowList.add(groupMemberList.get(i));
                        }
                    }else{
                        memberShowList = new ArrayList<>();
                        for (int i = 0; i < groupMemberList.size(); i++) {
                            memberShowList.add(groupMemberList.get(i));
                        }
                    }
                    loginId = SPUtil.getString(GroupDetailActivity.this, App.USER_ID);
                    isGroupLeader = loginId.equals(creatId);
                    if (isGroupLeader) {
                        msgData.setClickable(true);
                        nameNote.setVisibility(View.VISIBLE);
                        User add = new User("add");
                        User del = new User("del");
                        memberShowList.add(add);
                        memberShowList.add(del);
                        btnOut.setText("解散并退出");
                    }else {
                        msgData.setClickable(false);
                        nameNote.setVisibility(View.GONE);
                        User add = new User("add");
                        memberShowList.add(add);
                        btnOut.setText("删除并退出");
                    }

                    adapter = new GridViewAdapter(memberShowList, GroupDetailActivity.this);
                    gridView.setAdapter(adapter);
                    num.setText("全部群成员（"+groupMemberList.size()+"）");
                    break;
                case 444://登录过期
                    WinToast.toast(getApplicationContext(), "登录过期");
                    break;
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };

    private void initSwitch() {
        if (RongIM.getInstance() != null) {
            RongIM.getInstance().getConversation(Conversation.ConversationType.GROUP, targetId, new RongIMClient.ResultCallback<Conversation>() {
                @Override
                public void onSuccess(Conversation conversation) {
                    if (conversation == null) {
                        return;
                    }
                    if (conversation.isTop()) {
                        top.setChecked(true);
                    } else {
                        top.setChecked(false);
                    }

                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });

            RongIM.getInstance().getConversationNotificationStatus(Conversation.ConversationType.GROUP, targetId, new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
                @Override
                public void onSuccess(Conversation.ConversationNotificationStatus conversationNotificationStatus) {

                    if (conversationNotificationStatus == Conversation.ConversationNotificationStatus.DO_NOT_DISTURB ? true : false) {
                        notify.setChecked(true);
                    } else {
                        notify.setChecked(false);
                    }
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.switch_notify_group:

                Conversation.ConversationNotificationStatus cns;
                if (isChecked) {
                    cns = Conversation.ConversationNotificationStatus.DO_NOT_DISTURB;
                } else {
                    cns = Conversation.ConversationNotificationStatus.NOTIFY;
                }
                RongIM.getInstance().setConversationNotificationStatus(Conversation.ConversationType.GROUP,
                        targetId,
                        cns,
                        new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
                            @Override
                            public void onSuccess(Conversation.ConversationNotificationStatus conversationNotificationStatus) {
                                if (conversationNotificationStatus == Conversation.ConversationNotificationStatus.DO_NOT_DISTURB) {

                                } else if (conversationNotificationStatus == Conversation.ConversationNotificationStatus.NOTIFY) {

                                }
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {
                            }
                        }
                );

                break;
            case R.id.switch_top_group:
                RongIM.getInstance().setConversationToTop(Conversation.ConversationType.GROUP,
                        targetId,
                        isChecked,
                        new RongIMClient.ResultCallback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean aBoolean) {
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {
                            }
                        }
                );
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.msg_clean_group:
                cleanMsg();
                break;
            case R.id.msg_data:
                showChangeDialog();
//                Intent intent = new Intent(GroupDetailActivity.this, MessageDataActivity.class);
//                intent.putExtra("name",name);
//                intent.putExtra("targetId",targetId);
//                startActivity(intent);
                break;
            case R.id.user_num:
                Intent intent2 = new Intent(GroupDetailActivity.this, GroupMemberActivity.class);
                intent2.putExtra("id",targetId);
                intent2.putExtra("type",type);
                startActivity(intent2);
                break;
            case R.id.btn_out:
                RongIM.getInstance().removeConversation(Conversation.ConversationType.GROUP, targetId,remove );
                if (isGroupLeader) {//解散并退出
                    ServerUtils.myGroupDismiss(targetId,groupDismiss);
                }else {//删除并退出
                    ServerUtils.myGroupQuit(targetId,name,loginId,groupQuit);
                }
                break;
        }
    }
    private RongIMClient.ResultCallback<Boolean> remove =  new RongIMClient.ResultCallback<Boolean>() {
        @Override
        public void onSuccess(Boolean aBoolean) {
            Log.e("1111", "onSuccess: reMove" );
        }

        @Override
        public void onError(RongIMClient.ErrorCode errorCode) {

        }
    };

    private RequestCallBack<String> groupDismiss = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            DemoApiJSON demoApiJSON = JsonUtils.jsonToPojo(json,DemoApiJSON.class);
            switch (demoApiJSON.getStatus()){
                case 200:
                    Toast.makeText(GroupDetailActivity.this, "解散群组成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(GroupDetailActivity.this, GroupListActivity.class));
                    finish();
                    break;
                case 404:
                    Toast.makeText(GroupDetailActivity.this, "权限没有权限解散该群组", Toast.LENGTH_SHORT).show();
                    break;
                case 405:
                    Toast.makeText(GroupDetailActivity.this, "融云出错", Toast.LENGTH_SHORT).show();
                    break;
                case 500:
                    Toast.makeText(GroupDetailActivity.this, "系统错误", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };
    private RequestCallBack<String> groupQuit = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            DemoApiJSON demoApiJSON = JsonUtils.jsonToPojo(json,DemoApiJSON.class);
            switch (demoApiJSON.getStatus()){
                case 200:
                    Toast.makeText(GroupDetailActivity.this, "退出群组", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(GroupDetailActivity.this, GroupListActivity.class));
                    finish();
                    break;
                case 404:
                    Toast.makeText(GroupDetailActivity.this, "群组不存在", Toast.LENGTH_SHORT).show();
                    break;
                case 405:
//                    Toast.makeText(GroupDetailActivity.this, "融云出错", Toast.LENGTH_SHORT).show();
                    break;
                case 500:
//                    Toast.makeText(GroupDetailActivity.this, "系统错误", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };

    private void cleanMsg() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("是否清空聊天信息？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RongIM.getInstance().clearMessages(Conversation.ConversationType.GROUP,
                        targetId, new RongIMClient.ResultCallback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean aBoolean) {
                                Toast.makeText(GroupDetailActivity.this, "清除成功", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {
                                Toast.makeText(GroupDetailActivity.this,"清除失败",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }


    private ArrayList<User> sort(List<User> friends){
        ArrayList<User> friendsArrayList = new ArrayList<User>();
        HashMap<String, User> userMap = new HashMap<String, User>();
        String [] name = new String [friends.size()];
        int i = 0;
        for(User friend : friends){
            userMap.put(friend.getUsername()+friend.getUserid(),friend);
            name[i] = friend.getUsername()+friend.getUserid();
            i++;
        }
        String [] nameNew = getSortOfChinese(name);
        for (int j = 0; j < nameNew.length ; j++) {
            friendsArrayList.add(userMap.get(nameNew[j]));
        }
        return  friendsArrayList;
    }

    private String[] getSortOfChinese(String[] a) {
        // Collator 类是用来执行区分语言环境这里使用CHINA
        Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);
        // JDKz自带对数组进行排序。
        Arrays.sort(a, cmp);
        return a;
    }


    private void showChangeDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View myView = layoutInflater.inflate(R.layout.change_name, null);
        final EditText editText = (EditText) myView.findViewById(R.id.name_change_dialog);

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setView(myView);
        builder.setTitle("修改群名称");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String change = editText.getText().toString();
                ServerUtils.myGroupNAME(targetId, change, changeName);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    private RequestCallBack<String> changeName = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            DemoApiJSON demoApiJSON = JsonUtils.jsonToPojo(json,DemoApiJSON.class);
            switch (demoApiJSON.getStatus()){
                case 200:
                    Toast.makeText(GroupDetailActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(GroupDetailActivity.this, GroupListActivity.class));
                    finish();
                    break;
                case 404:
                    Toast.makeText(GroupDetailActivity.this, "您没有权限", Toast.LENGTH_SHORT).show();
                    break;
                case 405:
//                    Toast.makeText(GroupDetailActivity.this, "融云出错", Toast.LENGTH_SHORT).show();
                    break;
                case 500:
//                    Toast.makeText(GroupDetailActivity.this, "系统错误", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };
}


