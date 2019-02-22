package org.bigdata.zczw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.bigdata.zczw.App;
import org.bigdata.zczw.R;
import org.bigdata.zczw.Singleton;
import org.bigdata.zczw.adapter.GridViewAdapter;
import org.bigdata.zczw.entity.DemoApiJSON;
import org.bigdata.zczw.entity.User;
import org.bigdata.zczw.ui.WinToast;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.SPUtil;
import org.bigdata.zczw.utils.ServerUtils;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
/*
* 群成员列表
* */
public class GroupMemberActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private GridViewAdapter adapter;
    private GridView gridView;

    private String targetId;
    private int type;
    private List<User> groupMemberList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member);

        init();
    }

    private void init() {
        targetId = getIntent().getStringExtra("id");
        type = getIntent().getIntExtra("type",1);

        if (type == 1 ) {
            ServerUtils.mGroupInfoById(targetId, groupInfo);
        }else{
            ServerUtils.myGroupInfos(targetId, myGroupInfo);
        }

        getSupportActionBar().setTitle(Singleton.getInstance().getGroupNameById(targetId));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);

        gridView = (GridView) findViewById(R.id.member_group_act);


        gridView.setOnItemClickListener(this);
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
                    adapter = new GridViewAdapter(groupMemberList,GroupMemberActivity.this);
                    gridView.setAdapter(adapter);
                    break;
                case 444://登录过期
                    WinToast.toast(getApplicationContext(), "登陆过期");
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
                    groupMemberList = sort(groupMemberList);
                    adapter = new GridViewAdapter(groupMemberList,GroupMemberActivity.this);
                    gridView.setAdapter(adapter);
                    break;
                case 444://登录过期
                    WinToast.toast(getApplicationContext(), "登陆过期");
                    break;
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!SPUtil.getString(GroupMemberActivity.this, App.USER_ID).equals(groupMemberList.get(position).getUserid()+"")) {
            Intent intent = new Intent(GroupMemberActivity.this, PersonalActivity.class);
            intent.putExtra("PERSONAL",groupMemberList.get(position).getUserid()+"");
            startActivity(intent);
        }else{
            startActivity(new Intent(GroupMemberActivity.this, UserInfoActivity.class));
        }
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

}
