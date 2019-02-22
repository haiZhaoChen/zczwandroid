package org.bigdata.zczw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.bigdata.zczw.App;
import org.bigdata.zczw.R;
import org.bigdata.zczw.Singleton;
import org.bigdata.zczw.adapter.TagGridViewAdapter;
import org.bigdata.zczw.entity.DemoApiJSON;
import org.bigdata.zczw.entity.Friend;
import org.bigdata.zczw.entity.User;
import org.bigdata.zczw.entity.UserIdsEntity;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.SPUtil;
import org.bigdata.zczw.utils.ServerUtils;

import java.util.ArrayList;
import java.util.List;
/*
* 新建群组
* */
public class GroupCreateActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private GridView gridView;
    private EditText editText;
    private Button create;

    private List<Friend> friendList;
    private List<User> userSelect;

    private TagGridViewAdapter adapter;

    private String newName = "";
    private boolean isMy;
    private int labelId;
    private int change;
    private List<UserIdsEntity> userIds;
    private List<Friend> mFriendsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);

        AppManager.getAppManager().addActivity(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("新建群组");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);
        initView();
        initData();
    }

    private void initView() {
        gridView = (GridView) findViewById(R.id.gridView_group);
        editText = (EditText) findViewById(R.id.tag_group_new);
        create = (Button) findViewById(R.id.btn_group_create);
    }

    private void initData() {
        Intent intent = getIntent();
        friendList = (List<Friend>) intent.getSerializableExtra("user");
        userSelect = new ArrayList<>();
        for (Friend friend : friendList) {
            if (friend.isSelected()) {
                userSelect.add(Singleton.getInstance().getFriendById(friend.getUserId()));
            }
        }
        User add = new User("add");
        User del = new User("del");
        userSelect.add(add);
        userSelect.add(del);
        adapter = new TagGridViewAdapter(userSelect,GroupCreateActivity.this);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(this);
        create.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position == userSelect.size()-1){
            Intent intent = new Intent(GroupCreateActivity.this,UserSelectActivity.class);
            intent.putExtra("type","gdel");
            startActivity(intent);
            finish();
        }else if(position == userSelect.size()-2){
            Intent intent = new Intent(GroupCreateActivity.this,UserSelectActivity.class);
            intent.putExtra("type","gadd");
            startActivity(intent);
            finish();
        }else{
            Intent authorIntent = new Intent(GroupCreateActivity.this, PersonalActivity.class);
            authorIntent.putExtra("PERSONAL",userSelect.get(position).getUserid()+"");
            startActivity(authorIntent);
        }
    }

    @Override
    public void onClick(View v) {
        save();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.de_save_tag, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            //保存标签
            case R.id.tag_save:
                save();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void save(){
        String userIds = SPUtil.getString(this, App.USER_ID)+"/";
        for (int i = 0; i < userSelect.size() - 2; i++) {
            userIds = userIds+userSelect.get(i).getUserid()+"/";
        }
        newName = editText.getText().toString();
        if (!TextUtils.isEmpty(newName)) {
        // "创建"
            ServerUtils.createGroup(userIds, newName, groupCreate);
            create.setClickable(false);
        }else{
            Toast.makeText(this, "请输入群组名称", Toast.LENGTH_SHORT).show();
        }
    }

    private RequestCallBack<String> groupCreate = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            DemoApiJSON demoApiJSON = JsonUtils.jsonToPojo(json,DemoApiJSON.class);
            switch (demoApiJSON.getStatus()){
                case 200:
                    Toast.makeText(GroupCreateActivity.this, "创建成功", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 404:
                    Toast.makeText(GroupCreateActivity.this, "群组名称已存在", Toast.LENGTH_SHORT).show();
                    break;
                case 405:
//                    Toast.makeText(GroupCreateActivity.this, "融云出错", Toast.LENGTH_SHORT).show();
                    break;
                case 500:
//                    Toast.makeText(GroupCreateActivity.this, "系统错误", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };

}
