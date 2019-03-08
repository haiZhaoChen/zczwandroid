package org.bigdata.zczw.SendGroupMsg;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.bigdata.zczw.App;
import org.bigdata.zczw.R;
import org.bigdata.zczw.Singleton;
import org.bigdata.zczw.entity.DemoApiJSON;
import org.bigdata.zczw.entity.Friend;
import org.bigdata.zczw.entity.GroupInfo;
import org.bigdata.zczw.entity.User;
import org.bigdata.zczw.ui.WinToast;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.SPUtil;
import org.bigdata.zczw.utils.ServerUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;

public class SendGroupMesActivity extends AppCompatActivity {

    private static final int GROUP = 33;

    private TextView tv_allname;
    private Button btn_send;
    private List<Friend> friendList;
    private Map<String, Boolean> groupSelect = null;
//    private ArrayList<String> userSelect;//群发用户list
    private HashMap<String,String> userSelect;
    private List<User> groupMemberList;
    private Context context;
    private String targetUserId;
    private String title;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                default:
                    Singleton.getInstance().setSendGroupMsg(true);
                    RongIM.getInstance().startPrivateChat(SendGroupMesActivity.this, targetUserId, title);
                    finish();
                    break;
            }
        }
    };
    private int i,j;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_group_mes);

        initView();
        initDate();
    }

    private void initView() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("群发消息");
        tv_allname = (TextView) findViewById(R.id.tv_allname);
        btn_send = (Button) findViewById(R.id.btn_send);
        context = SendGroupMesActivity.this;
        targetUserId = SPUtil.getString(context, App.USER_ID);
        title = "group";
    }

    private void initDate() {
        Singleton sObject = Singleton.getInstance();
        friendList = (List<Friend>) getIntent().getSerializableExtra("Message");
        Bundle bundle = getIntent().getExtras();
        if (bundle.get("group") != null) {
            MyMap myMap = (MyMap) bundle.get("group");
            groupSelect = myMap.getBooleanMap();
        }
        userSelect = new HashMap<>();
        groupMemberList = new ArrayList<>();
        StringBuffer str = new StringBuffer();//群发消息目标人群显示
        if(groupSelect!=null) {
            for (String key : groupSelect.keySet()) {
                if (groupSelect.get(key)) {
                    str.append(sObject.getGroupNameById(key) + ".");
                }
            }
        }
        for (Friend friend : friendList) {
            if (friend.isSelected()) {
                userSelect.put(friend.getUserId(),friend.getUserId());
                str.append(friend.getNickname() + ".");
            }
        }
        tv_allname.setText(str + "");
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (groupSelect != null) {
                    i = 0;
                    j = 0;
                    for (String key : groupSelect.keySet()) {
                        if (groupSelect.get(key)) {
                            i++;
                        }
                    }
                    for (String key : groupSelect.keySet()) {
                        if (groupSelect.get(key)) {
                            GroupInfo group = Singleton.getInstance().getGroupInfoById(key);
                            if(group.getType() == 1){
                                ServerUtils.mGroupInfoById(group.getId(), groupInfo);
                            }else{
                                ServerUtils.myGroupInfos(group.getId(),groupInfo);
                            }
//                            String targetId = Singleton.getInstance().getGroupIdByName(key);
//                            ServerUtils.mGroupInfoById(targetId, groupInfo);
                        }
                    }

                }else{
                    Singleton.getInstance().setUserSelect(userSelect);
                    Singleton.getInstance().setSendGroupMsg(true);
                    RongIM.getInstance().startPrivateChat(SendGroupMesActivity.this, targetUserId, title);
                    finish();
                }
            }
        });
    }

    private RequestCallBack<String> groupInfo = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            DemoApiJSON demoApiJSON = JsonUtils.jsonToPojo(json, DemoApiJSON.class);
            switch (demoApiJSON.getStatus()) {
                case 200://群组信息获取成功
                    groupMemberList = JsonUtils.getGroup(json);
                    for (int i = 0; i < groupMemberList.size(); i++) {
                        if (!(groupMemberList.get(i).getUserid()+"").equals(SPUtil.getString(SendGroupMesActivity.this,App.USER_ID))) {
                            userSelect.put(groupMemberList.get(i).getUserid()+"",groupMemberList.get(i).getUserid()+"");
                        }
                    }
                    Singleton.getInstance().setUserSelect(userSelect);
                    j++;
                    if (i == j) {
                        handler.sendEmptyMessage(j);
                    }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
