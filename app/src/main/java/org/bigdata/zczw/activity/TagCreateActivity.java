package org.bigdata.zczw.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import org.bigdata.zczw.entity.Friend;
import org.bigdata.zczw.entity.User;
import org.bigdata.zczw.entity.UserIdsEntity;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.SPUtil;
import org.bigdata.zczw.utils.ServerUtils;

import java.util.ArrayList;
import java.util.List;

/*
* 张承 群组标签
* */

public class TagCreateActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private GridView gridView;
    private EditText editText;
    private Button delete;

    private List<Friend> friendList;
    private List<User> userSelect;

    private TagGridViewAdapter adapter;

    private String name;
    private String newName = "";
    private boolean isMy;
    private int labelId;
    private int change;
    private List<UserIdsEntity> userIds;
    private List<Friend> mFriendsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_create);

        AppManager.getAppManager().addActivity(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);
        initView();
        initData();
    }

    private void initView() {
        gridView = (GridView) findViewById(R.id.gridView_tag);
        editText = (EditText) findViewById(R.id.tag_name_new);
        delete = (Button) findViewById(R.id.btn_tag_delete);
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent.hasExtra("change")) {
            change = intent.getIntExtra("change",0);
        }else {
            change = 0;
        }
        if (change == 0) {
            name = intent.getStringExtra("name");
            editText.setText(name);
            getSupportActionBar().setTitle("编辑标签");
            userIds = (List<UserIdsEntity>) intent.getSerializableExtra("tagDate");
            labelId = intent.getIntExtra("labelId",0);
            isMy = intent.getBooleanExtra("isMy",false);
            if(!isMy){
                delete.setVisibility(View.VISIBLE);
            }
            userSelect = new ArrayList<>();
            for (UserIdsEntity userIdsEntity : userIds){
                if (!(userIdsEntity.getUserId()+"").equals(SPUtil.getString(this,App.USER_ID)) ){
                    userSelect.add(Singleton.getInstance().getFriendById(userIdsEntity.getUserId() + ""));
                }
            }

            // 获取好友列表
            ArrayList<User> userInfos = (ArrayList<User>) Singleton.getInstance().getFriendlist();
            mFriendsList = new ArrayList<Friend>();
            if (userInfos != null) {
                for (User userInfo : userInfos) {
                    Friend friend = new Friend();
                    friend.setNickname(userInfo.getUsername());
                    friend.setPortrait(userInfo.getImagePosition());
                    friend.setGroupname(userInfo.getUnitsName() + "." + userInfo.getPositionName() + "." + userInfo.getJobsName());
                    friend.setUserId(userInfo.getUserid() + "");
                    mFriendsList.add(friend);
                }
            }
            for (int j = 0; j < mFriendsList.size(); j++) {
                for (int i = 0; i < userSelect.size(); i++) {
                    if((userSelect.get(i).getUserid() +"").equals(mFriendsList.get(j).getUserId()) ){
                        mFriendsList.get(j).setIsSelected(true);
                    }
                }
            }

            Singleton.getInstance().setSelect(mFriendsList);
        }else{
            if (intent.hasExtra("name")) {
                name = intent.getStringExtra("name");
                editText.setText(name);
                getSupportActionBar().setTitle("编辑标签");
                isMy = intent.getBooleanExtra("isMy", false);
                labelId = intent.getIntExtra("labelId",0);
                if(!isMy){
                    delete.setVisibility(View.VISIBLE);
                }
            }else{
                getSupportActionBar().setTitle("保存为标签");
            }
            friendList = (List<Friend>) getIntent().getSerializableExtra("user");
            userSelect = new ArrayList<>();
            for (Friend friend : friendList) {
                if (friend.isSelected()) {
                    userSelect.add(Singleton.getInstance().getFriendById(friend.getUserId()));
                }
            }
        }
        User add = new User("add");
        User del = new User("del");
        userSelect.add(add);
        userSelect.add(del);
        adapter = new TagGridViewAdapter(userSelect,TagCreateActivity.this);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(this);
        delete.setOnClickListener(this);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if(position == userSelect.size()-1){
            Intent intent = new Intent(TagCreateActivity.this,UserSelectActivity.class);
            if (name != null) {
                intent.putExtra("name",name);
                intent.putExtra("labelId",labelId);
                intent.putExtra("isMy", isMy);
                intent.putExtra("change",1);
            }
            intent.putExtra("type","del4");
            startActivity(intent);
            finish();
        }else if(position == userSelect.size()-2){
            Intent intent = new Intent(TagCreateActivity.this,UserSelectActivity.class);
            if (name != null) {
                intent.putExtra("name",name);
                intent.putExtra("labelId",labelId);
                intent.putExtra("change",1);
                intent.putExtra("isMy", isMy);
            }
            intent.putExtra("type","add");
            startActivity(intent);
            finish();
        }else{
            Intent authorIntent = new Intent(TagCreateActivity.this, PersonalActivity.class);
            authorIntent.putExtra("PERSONAL",userSelect.get(position).getUserid()+"");
            startActivity(authorIntent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_tag_delete://删除标签
                ServerUtils.deleteTagLabel(labelId,tagDel);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (change == 1) {
            showNoticeDialog();
        }else {
            super.onBackPressed();
        }
    }

    /**
     * 显示对话框
     */
    private void showNoticeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TagCreateActivity.this);

        builder.setTitle("是否保存本次编辑？");
        builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                save();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("不保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.create().show();
    }

    private void save(){
        String userIds = "";
        for (int i = 0; i < userSelect.size() - 2; i++) {
            userIds = userIds+userSelect.get(i).getUserid()+"/";
        }
        newName = editText.getText().toString();
        if (name != null) {//修改标签
//            Log.e("1111", "修改");
            ServerUtils.changeTagLabel(labelId,name,userIds,tagLabel);
        }else{//创建 标签
            if (!TextUtils.isEmpty(newName)) {
//                Log.e("1111", "创建" +newName);
                ServerUtils.createTagLabel(userIds,newName,tagLabel);
            }else{
                Toast.makeText(this,"请输入标签名",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private RequestCallBack<String> tagLabel = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            if (json.contains("OK")) {
                Toast.makeText(TagCreateActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };
    private RequestCallBack<String> tagDel = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            if (json.contains("OK")) {
                Toast.makeText(TagCreateActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };


}
