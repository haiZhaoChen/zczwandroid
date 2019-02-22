package org.bigdata.zczw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.bigdata.zczw.App;
import org.bigdata.zczw.R;
import org.bigdata.zczw.SendGroupMsg.SendGroupAdapter;
import org.bigdata.zczw.Singleton;
import org.bigdata.zczw.adapter.DeFriendListAdapter;
import org.bigdata.zczw.entity.DemoApiJSON;
import org.bigdata.zczw.entity.Friend;
import org.bigdata.zczw.entity.User;
import org.bigdata.zczw.ui.DePinnedHeaderListView;
import org.bigdata.zczw.ui.DeSwitchGroup;
import org.bigdata.zczw.ui.DeSwitchItemView;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.SPUtil;
import org.bigdata.zczw.utils.ServerUtils;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* 选择联系人
* */

public class UserDelActivity extends AppCompatActivity implements
        DeSwitchGroup.ItemHander, View.OnClickListener, TextWatcher,
        DeFriendListAdapter.OnFilterFinished, AdapterView.OnItemClickListener, SearchView.OnQueryTextListener{

    protected SendGroupAdapter mAdapter;
    private DePinnedHeaderListView mListView;
    private DeSwitchGroup mSwitchGroup;

    protected List<Friend> mFriendsList;
    private List<Friend> mSearchFriendsList;
    private List<User> userSelect;
    private TextView textViwe;

    private List<User> groupMemberList;
    private Map<String,User> userMap;

    private String type;
    private String name;
    private String targetId;
    private boolean isMy;
    private int labelId;
    //搜索框
    private SearchView searchView;

    public static boolean IS_CHECK = false;
    private String userIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_del);

        AppManager.getAppManager().addActivity(this);
        getSupportActionBar().setTitle("选择联系人");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);

        initView();
        initDate();
    }

    private void initView() {
        searchView = (SearchView)findViewById(R.id.seacher_friends_del);
        mListView = (DePinnedHeaderListView) findViewById(R.id.de_ui_friend_list_del);
        mSwitchGroup = (DeSwitchGroup) findViewById(R.id.de_ui_friend_message_del);
        mListView.setPinnedHeaderView(LayoutInflater.from(this)
                .inflate(R.layout.de_item_friend_index, mListView, false));
        textViwe = (TextView) mListView.getPinnedHeaderView();

        mListView.setFastScrollEnabled(false);

        mListView.setOnItemClickListener(this);
        mSwitchGroup.setItemHander(this);
        mListView.setHeaderDividersEnabled(false);
        mListView.setFooterDividersEnabled(false);
        searchView.setIconifiedByDefault(false);//自动缩小为图标
        searchView.setOnQueryTextListener(this);//事件监听器
        searchView.setSubmitButtonEnabled(false);//不显示搜索按钮
        searchView.setQueryHint("搜索");//默认显示文本
    }

    private void initDate() {
        userSelect = new ArrayList<>();
        userMap = new HashMap<>();
        Intent boot = getIntent();
        type = boot.getStringExtra("type");
        name = boot.getStringExtra("name");
        targetId = boot.getStringExtra("targetId");
        groupMemberList = (List<User>) boot.getSerializableExtra("users");
        if (groupMemberList != null) {
            for (int i = 0; i < groupMemberList.size(); i++) {
                userMap.put(groupMemberList.get(i).getUserid()+"",groupMemberList.get(i));
            }
        }
        switch (type){
            case "del":
                mFriendsList = new ArrayList<Friend>();
                if (groupMemberList != null) {
                    for (User userInfo : groupMemberList) {
                        if (!SPUtil.getString(this, App.USER_ID).equals(userInfo.getUserid()+"")) {
                            Friend friend = new Friend();
                            User user = Singleton.getInstance().getFriendById(userInfo.getUserid()+"");
                            friend.setNickname(user.getUsername());
                            friend.setPortrait(user.getImagePosition());
                            friend.setGroupname(user.getUnitsName() + "." + user.getPositionName() + "." + user.getJobsName());
                            friend.setUserId(user.getUserid() + "");
                            mFriendsList.add(friend);
                        }
                    }
                }
                break;
            case "add":
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
                        if (userMap.containsKey(userInfo.getUserid() + "")) {
                            friend.setIsSelected(true);
                        }
                        mFriendsList.add(friend);
                    }
                }
                break;
        }

        //搜索用的list
        mSearchFriendsList = new ArrayList<>();
        // 获取好友列表
        mFriendsList = sort(mFriendsList);
        mAdapter = new SendGroupAdapter(this, mFriendsList);
        mListView.setAdapter(mAdapter);

        fillData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.de_group_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()){
            case R.id.action_yes_conversation:
                if (IS_CHECK) {
                    switch (type){
                        case "add":
                            //添加
                            for (Friend friend : mFriendsList) {
                                if (friend.isSelected() && !userMap.containsKey(friend.getUserId())) {
                                    userSelect.add(Singleton.getInstance().getFriendById(friend.getUserId()));
                                }
                            }
                            userIds = "";
                            for (int i = 0; i < userSelect.size(); i++) {
                                userIds = userIds +userSelect.get(i).getUserid()+"/";
                            }
                            ServerUtils.myGroupJoin(targetId,name,userIds,groupJoin);

                            break;
                        case "del":
                            //删除
                            for (Friend friend : mFriendsList) {
                                if (friend.isSelected()) {
                                    userSelect.add(Singleton.getInstance().getFriendById(friend.getUserId()));
                                }
                            }
                            userIds ="";
                            for (int i = 0; i < userSelect.size(); i++) {
                                userIds = userIds +userSelect.get(i).getUserid()+"/";
                            }
                            ServerUtils.myGroupQuit(targetId,name, userIds,groupQuit);
                            break;
                    }

                }else{
                    Toast.makeText(UserDelActivity.this, "请选择联系人", Toast.LENGTH_SHORT).show();
                }
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private RequestCallBack<String> groupQuit = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            DemoApiJSON demoApiJSON = JsonUtils.jsonToPojo(json, DemoApiJSON.class);
            switch (demoApiJSON.getStatus()){
                case 200:
                    Toast.makeText(UserDelActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 404:
                    Toast.makeText(UserDelActivity.this, "群组不存在", Toast.LENGTH_SHORT).show();
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

    private RequestCallBack<String> groupJoin = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            DemoApiJSON demoApiJSON = JsonUtils.jsonToPojo(json, DemoApiJSON.class);
            switch (demoApiJSON.getStatus()){
                case 200:
                    Toast.makeText(UserDelActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 404:
                    Toast.makeText(UserDelActivity.this, "群组不存在", Toast.LENGTH_SHORT).show();
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

    //搜索 根据某一字符匹配数据源
    public static List<Friend> searchItem(String name, List<Friend> friendslist) {
        List<Friend> searchList = new ArrayList<Friend>();
        for (int i = 0; i < friendslist.size(); i++) {
            int index = friendslist.get(i).getNickname().indexOf(name);
            // 存在匹配的数据
            if (index != -1) {
                searchList.add(friendslist.get(i));
            }
        }
        return searchList;
    }

    @Override
    public void onClick(View v) {
        if (v instanceof DeSwitchItemView) {
            CharSequence tag = ((DeSwitchItemView) v).getText();

            if (mAdapter != null && mAdapter.getSectionIndexer() != null) {
                Object[] sections = mAdapter.getSectionIndexer().getSections();
                int size = sections.length;

                for (int i = 0; i < size; i++) {
                    if (tag.equals(sections[i])) {
                        int index = mAdapter.getPositionForSection(i);
                        mListView.setSelection(index + mListView.getHeaderViewsCount());
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onFilterFinished() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object tagObj = view.getTag();
        if (tagObj != null && tagObj instanceof SendGroupAdapter.ViewHolder) {
            SendGroupAdapter.ViewHolder viewHolder = (SendGroupAdapter.ViewHolder) tagObj;
            String friendId = viewHolder.friend.getUserId();
            for(Friend friend : mFriendsList){
                if(friend.getUserId() == friendId){
                    if(friend.isSelected()){
                        friend.setIsSelected(false);
                    }else{
                        friend.setIsSelected(true);
                        IS_CHECK = true;
                    }
                }
            }
            fillData();
            return;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            //清除好友列表的过滤操作
            fillData();
        } else {
            //对好友列表进行过滤
            mSearchFriendsList = searchItem(newText, mFriendsList);
            mAdapter.setAdapterData(mSearchFriendsList);
            mAdapter.notifyDataSetChanged();
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter = null;
        }
    }

    private ArrayList<Friend> sort(List<Friend> friends){
        ArrayList<Friend> friendsArrayList = new ArrayList<Friend>();
        HashMap<String, Friend> userMap = new HashMap<String, Friend>();
        String [] name = new String [friends.size()];
        int i = 0;
        for(Friend friend : friends){
//            friend.setPortrait(getResources().getResourceName(R.drawable.de_default_portrait));
            userMap.put(friend.getNickname(),friend);
            name[i] = friend.getNickname();
            i++;
        }
        String [] nameNew = getSortOfChinese(name);
        for (int j = 0; j < nameNew.length ; j++) {
            friendsArrayList.add(userMap.get(nameNew[j]));
        }
        return  friendsArrayList;
    }

    public static String[] getSortOfChinese(String[] a) {
        // Collator 类是用来执行区分语言环境这里使用CHINA
        Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);
        // JDKz自带对数组进行排序。
        Arrays.sort(a, cmp);
        return a;
    }

    private final void fillData() {
        mAdapter.setAdapterData(mFriendsList);
        mAdapter.notifyDataSetChanged();
    }
}
