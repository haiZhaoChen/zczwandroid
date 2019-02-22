package org.bigdata.zczw.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.bigdata.zczw.R;
import org.bigdata.zczw.SendGroupMsg.SendGroupAdapter;
import org.bigdata.zczw.Singleton;
import org.bigdata.zczw.adapter.DeFriendListAdapter;
import org.bigdata.zczw.entity.Friend;
import org.bigdata.zczw.entity.User;
import org.bigdata.zczw.ui.DePinnedHeaderListView;
import org.bigdata.zczw.ui.DeSwitchGroup;
import org.bigdata.zczw.ui.DeSwitchItemView;
import org.bigdata.zczw.utils.AppManager;

import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/*
* 创建群组
* 选择联系人
*
* */

public class UserSelectActivity extends AppCompatActivity implements
        DeSwitchGroup.ItemHander, View.OnClickListener, TextWatcher,
        DeFriendListAdapter.OnFilterFinished, AdapterView.OnItemClickListener, SearchView.OnQueryTextListener {

    protected SendGroupAdapter mAdapter;
    private DePinnedHeaderListView mListView;
    private DeSwitchGroup mSwitchGroup;
    private LinearLayout line_qun;

    protected List<Friend> mFriendsList;
    private List<Friend> mSearchFriendsList;
    private List<User> select;
    private TextView textViwe;
    private ReceiveMessageBroadcastReciver mBroadcastReciver;

    private String type;
    private String name;
    private boolean isMy;
    private int labelId;
    //搜索框
    private SearchView searchView;

    public static boolean IS_CHECK = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_group);

        AppManager.getAppManager().addActivity(this);
        getSupportActionBar().setTitle("选择联系人");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);

        initView();
        initDate();
    }

    private void initView() {
        searchView = (SearchView)findViewById(R.id.seacher_friends);
        mListView = (DePinnedHeaderListView) findViewById(R.id.de_ui_friend_list);
        mSwitchGroup = (DeSwitchGroup) findViewById(R.id.de_ui_friend_message);
        line_qun = (LinearLayout) findViewById(R.id.line_qun);
        mListView.setPinnedHeaderView(LayoutInflater.from(this)
                .inflate(R.layout.de_item_friend_index, mListView, false));
        textViwe = (TextView) mListView.getPinnedHeaderView();

        mListView.setFastScrollEnabled(false);

        mListView.setOnItemClickListener(this);
        mSwitchGroup.setItemHander(this);
        line_qun.setOnClickListener(this);
        mListView.setHeaderDividersEnabled(false);
        mListView.setFooterDividersEnabled(false);
        searchView.setIconifiedByDefault(false);//自动缩小为图标
        searchView.setOnQueryTextListener(this);//事件监听器
        searchView.setSubmitButtonEnabled(false);//不显示搜索按钮
        searchView.setQueryHint("搜索");//默认显示文本

        line_qun.setVisibility(View.GONE);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action_demo_agree_request");
        if (mBroadcastReciver == null) {
            mBroadcastReciver = new ReceiveMessageBroadcastReciver();
        }
        registerReceiver(mBroadcastReciver, intentFilter);

    }

    private void initDate() {
        type = getIntent().getStringExtra("type");
        if (getIntent().hasExtra("name")) {
            name = getIntent().getStringExtra("name");
            labelId = getIntent().getIntExtra("labelId", 0);
            isMy = getIntent().getBooleanExtra("isMy",false);
        }
        switch (type){
            case "new":
                // 获取好友列表
                ArrayList<User> userInfos = (ArrayList<User>) Singleton.getInstance().getFriendlist();
                mFriendsList = new ArrayList<Friend>();
                if (userInfos != null) {
                    for (User userInfo : userInfos) {
                        Friend friend = new Friend();
                        friend.setNickname(userInfo.getUsername());
                        friend.setPortrait(userInfo.getImagePosition());
                        friend.setGroupname(userInfo.getUnitsName() + "." + userInfo.getJobsName());
                        friend.setUserId(userInfo.getUserid() + "");
                        friend.setPhone(userInfo.getUserphone() + "");
                        mFriendsList.add(friend);
                    }
                }
                break;
            case "group":
                // 获取好友列表
                ArrayList<User> users = (ArrayList<User>) Singleton.getInstance().getFriendlist();
                mFriendsList = new ArrayList<Friend>();
                if (users != null) {
                    for (User userInfo : users) {
                        Friend friend = new Friend();
                        friend.setNickname(userInfo.getUsername());
                        friend.setPortrait(userInfo.getImagePosition());
                        friend.setGroupname(userInfo.getUnitsName() + "." + userInfo.getJobsName());
                        friend.setUserId(userInfo.getUserid() + "");
                        friend.setPhone(userInfo.getUserphone() + "");
                        mFriendsList.add(friend);
                    }
                }
                break;
            case "add":
                mFriendsList =Singleton.getInstance().getSelect();
                break;
            case "del":
                mFriendsList =Singleton.getInstance().getSelect();
                break;
            case "gadd":
                mFriendsList =Singleton.getInstance().getSelect();
                break;
            case "gdel":
                mFriendsList =Singleton.getInstance().getSelect();
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
                        case "new":
                            tagIntent();
                            break;
                        case "group":
                            groupIntent();
                            break;
                        case "add":
                            tagIntent();
                            break;
                        case "del":
                            tagIntent();
                            break;
                        case "gadd":
                            groupIntent();
                            break;
                        case "gdel":
                            groupIntent();
                            break;
                    }

                }else{
                    Toast.makeText(UserSelectActivity.this, "请选择联系人", Toast.LENGTH_SHORT).show();
                }
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void tagIntent(){
        Intent intent = new Intent(this,TagCreateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", (Serializable) mFriendsList);
        Singleton.getInstance().setSelect(mFriendsList);
        intent.putExtras(bundle);
        if (name != null) {
            intent.putExtra("name", name);
            intent.putExtra("labelId",labelId);
            intent.putExtra("isMy", isMy);
        }
        intent.putExtra("change",1);
        startActivity(intent);
        finish();
    }

    private void groupIntent(){
        Intent intent = new Intent(this,GroupCreateActivity.class);
        Bundle bundle2 = new Bundle();
        bundle2.putSerializable("user", (Serializable) mFriendsList);
        Singleton.getInstance().setSelect(mFriendsList);
        intent.putExtras(bundle2);
        startActivity(intent);
        finish();
    }

    //搜索 根据某一字符匹配数据源
    public static List<Friend> searchItem(String name, List<Friend> friendslist) {
        List<Friend> searchList = new ArrayList<Friend>();
        for (int i = 0; i < friendslist.size(); i++) {
            String s = friendslist.get(i).getNickname() + friendslist.get(i).getGroupname();
            int index = s.indexOf(name);
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
        unregisterReceiver(mBroadcastReciver);
    }

    private ArrayList<Friend> sort(List<Friend> friends){
        ArrayList<Friend> friendsArrayList = new ArrayList<Friend>();
        HashMap<String, Friend> userMap = new HashMap<String, Friend>();
        String [] name = new String [friends.size()];
        int i = 0;
        for(Friend friend : friends){
            userMap.put(friend.getNickname()+friend.getUserId(),friend);
            name[i] = friend.getNickname()+friend.getUserId();
            i++;
        }
        String [] nameNew = getSortOfChinese(name);
        Friend group = new Friend("★002", "群组", getResources().getResourceName(R.drawable.de_group_default_portrait));
        userMap.put("★", group);
        friendsArrayList.add(group);
        for (int j = 0; j < nameNew.length ; j++) {
            friendsArrayList.add(userMap.get(nameNew[j]));
        }
        return  friendsArrayList;


//        ArrayList<Friend> friendsArrayList = new ArrayList<Friend>();
//        HashMap<String, Friend> userMap = new HashMap<String, Friend>();
//        String [] name = new String [friends.size()];
//        int i = 0;
//        for(Friend friend : friends){
////            friend.setPortrait(getResources().getResourceName(R.drawable.de_default_portrait));
//            userMap.put(friend.getNickname(),friend);
//            name[i] = friend.getNickname();
//            i++;
//        }
//        String [] nameNew = getSortOfChinese(name);
//        for (int j = 0; j < nameNew.length ; j++) {
//            friendsArrayList.add(userMap.get(nameNew[j]));
//        }
//        return  friendsArrayList;
    }

    public static String[] getSortOfChinese(String[] a) {
        // Collator 类是用来执行区分语言环境这里使用CHINA
        Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);
        // JDKz自带对数组进行排序。
        Arrays.sort(a, cmp);
        return a;
    }

    private class ReceiveMessageBroadcastReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("action_demo_agree_request")) {
                updateDate();
            }
        }
    }

    private void updateDate() {
        if (mAdapter != null) {
            mAdapter = null;
        }
        // 获取好友列表
        ArrayList<User> userInfos = (ArrayList<User>) Singleton.getInstance().getFriendlist();

        mFriendsList = new ArrayList<Friend>();

        if (userInfos != null) {
            for (User userInfo : userInfos) {
                Friend friend = new Friend();
                friend.setNickname(userInfo.getUsername());
                friend.setPortrait(userInfo.getImagePosition());
                friend.setUserId(userInfo.getUserid() + "");
                friend.setPhone(userInfo.getUserphone() + "");
                mFriendsList.add(friend);
            }
        }
        mFriendsList = sort(mFriendsList);
        mAdapter = new SendGroupAdapter(this, mFriendsList);
        mListView.setAdapter(mAdapter);
        fillData();
    }

    private final void fillData() {
        mAdapter.setAdapterData(mFriendsList);
        mAdapter.notifyDataSetChanged();
    }
}
