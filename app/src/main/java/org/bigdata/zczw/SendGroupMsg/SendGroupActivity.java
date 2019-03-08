package org.bigdata.zczw.SendGroupMsg;

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

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.bigdata.zczw.App;
import org.bigdata.zczw.R;
import org.bigdata.zczw.Singleton;
import org.bigdata.zczw.activity.AddMessageActivity;
import org.bigdata.zczw.activity.AddShareActivity;
import org.bigdata.zczw.activity.CommentsActivity;
import org.bigdata.zczw.activity.GroupListActivity;
import org.bigdata.zczw.adapter.DeFriendListAdapter;
import org.bigdata.zczw.entity.DemoApiJSON;
import org.bigdata.zczw.entity.Friend;
import org.bigdata.zczw.entity.MsgTag;
import org.bigdata.zczw.entity.Record;
import org.bigdata.zczw.entity.User;
import org.bigdata.zczw.ui.DePinnedHeaderListView;
import org.bigdata.zczw.ui.DeSwitchGroup;
import org.bigdata.zczw.ui.DeSwitchItemView;
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
import java.util.Map;


public class SendGroupActivity extends AppCompatActivity implements
        DeSwitchGroup.ItemHander, View.OnClickListener, TextWatcher,
        DeFriendListAdapter.OnFilterFinished, AdapterView.OnItemClickListener, SearchView.OnQueryTextListener{

    private SendGroupAdapter mAdapter;
    private DePinnedHeaderListView mListView;
    private DeSwitchGroup mSwitchGroup;
    private LinearLayout line_qun;

    protected List<Friend> mFriendsList;
    private List<Friend> mSearchFriendsList;
    private TextView textViwe;
    private ReceiveMessageBroadcastReciver mBroadcastReciver;
    private MyMap myMap;//群列表选择界面传过来的

    private int groupSize;
    private Map<String, Boolean> groupSelect = null;
    private ArrayList<String> userSelect;//群发用户list

    //搜索框
    private SearchView searchView;
    private String type;
    private String msg;
    private int start;
    private String messageId;
    private String count;
    private HashMap<String,String> dataMap = new HashMap<>();

    private boolean isClick = false;

    private int index = 0;
    private ArrayList<User> userArrayList;
    private ArrayList<String> keys;
    private HashMap<String, String> data;
    private String names,topic;
    private String ids;
    private ArrayList<String> nameList;
    private HashMap<String, String> data1;

    private Record record;
    private ArrayList<MsgTag> tagList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_group);

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

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action_demo_agree_request");
        if (mBroadcastReciver == null) {
            mBroadcastReciver = new ReceiveMessageBroadcastReciver();
        }
        registerReceiver(mBroadcastReciver, intentFilter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);
        getSupportActionBar().setTitle("选择联系人");
    }

    private void initDate() {
        type = "";
        keys = new ArrayList<>();
        if (getIntent().hasExtra("type")) {
            type = getIntent().getStringExtra("type");
        }
        if (getIntent().hasExtra("record")) {
            record = (Record) getIntent().getSerializableExtra("record");
        }

        if (getIntent().hasExtra("topic")) {
            topic = getIntent().getStringExtra("topic");
        }
        if (getIntent().hasExtra("tag")) {
            tagList = (ArrayList<MsgTag>) getIntent().getSerializableExtra("tag");
        }

        if (type.equals("com")) {
            msg = getIntent().getStringExtra("msg");
            messageId = getIntent().getStringExtra("messageId");
            count = getIntent().getStringExtra("commentCount");
            Bundle bundle = getIntent().getBundleExtra("bundle");
            dataMap = (HashMap<String, String>) bundle.getSerializable("list");
            if (TextUtils.isEmpty(count)) {
                count = "0";
            }
        }
        //搜索用的list
        mSearchFriendsList = new ArrayList<>();
        // 获取好友列表
        String userId = SPUtil.getString(SendGroupActivity.this, App.USER_ID);
//        ServerUtils.mFirends(userId, 1, "", friends);

        ArrayList<User> userInfos = (ArrayList<User>) Singleton.getInstance().getFriendlist();
        mFriendsList = new ArrayList<Friend>();
        if (userInfos != null) {
            for (User userInfo : userInfos) {
                Friend friend = new Friend();
                friend.setNickname(userInfo.getUsername());
                friend.setGroupname(userInfo.getUnitsName()+ "."+userInfo.getJobsName());
                friend.setPortrait(userInfo.getImagePosition());
                friend.setUserId(userInfo.getUserid() + "");
                friend.setPhone(userInfo.getUserphone());
                friend.setIsSelected(false);
                mFriendsList.add(friend);
            }
        }
        mFriendsList = sort(mFriendsList);
        mAdapter = new SendGroupAdapter(SendGroupActivity.this, mFriendsList);
        mListView.setAdapter(mAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.de_group_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_yes_conversation:
                if (isClick) {
                    if (type.equals("addMsg")) {
                        userArrayList = new ArrayList<>();
                        if(myMap != null) {
                            groupSelect =  myMap.getBooleanMap();
                        }
                        StringBuffer str = new StringBuffer();//群发消息目标人群显示

                        data = new HashMap<>();
                        ids = "";
                        names = "";
                        for (Friend friend : mFriendsList) {
                            if (friend.isSelected()) {
                                data.put(friend.getNickname(),friend.getUserId());
                            }
                        }

                        if(groupSelect!=null) {
                            for (String key : groupSelect.keySet()) {
                                if (groupSelect.get(key)) {
                                    keys.add(key);
                                }
                            }
                            for (int i = 0; i < keys.size(); i++) {
                                if (keys.get(i).length()>11) {
                                    ServerUtils.mGroupInfoById(keys.get(i),myGroupInfo);
                                }else {
                                    ServerUtils.myGroupInfos(keys.get(i),myGroupInfo);
                                }
                            }
                        }else {
                            for (int i = 0; i < userArrayList.size(); i++) {
                                data.put(userArrayList.get(i).getUsername(),names+ userArrayList.get(i).getUserid()+"");
                            }

                            for (String key : data.keySet()) {
                                ids = ids + data.get(key)+"/";
                                names = names + key + "、";
                            }
                            Intent intent;
                            if (getIntent().hasExtra("share")) {
                                intent = new Intent(SendGroupActivity.this,AddShareActivity.class);
                                intent.putExtra("record",record);
                            }else {
                                intent = new Intent(SendGroupActivity.this,AddMessageActivity.class);
                                intent.putExtra("tag",tagList);
                            }
                            if (getIntent().hasExtra("topic")) {
                                intent.putExtra("topic",topic);
                            }
                            intent.putExtra("type","2");
                            intent.putExtra("ids",ids);
                            intent.putExtra("names",names);
                            intent.putExtra("publicScope","1");
                            startActivity(intent);
                            finish();
                        }

                    }else if (type.equals("com")) {
                        userArrayList = new ArrayList<>();
                        nameList = new ArrayList<>();
                        if(myMap != null) {
                            groupSelect =  myMap.getBooleanMap();
                        }
                        StringBuffer str = new StringBuffer();//群发消息目标人群显示
                        data1 = new HashMap<>();
                        for (Friend friend : mFriendsList) {
                            if (friend.isSelected()) {
                                data1.put(friend.getNickname(),friend.getUserId());
                                dataMap.put(friend.getNickname(),friend.getUserId());
                            }
                        }

                        if(groupSelect!=null) {
                            for (String key : groupSelect.keySet()) {
                                if (groupSelect.get(key)) {
                                    keys.add(key);
                                }
                            }
                            for (int i = 0; i < keys.size(); i++) {
                                if (keys.get(i).length()>11) {
                                    ServerUtils.mGroupInfoById(keys.get(i),myGroupInfo);
                                }else {
                                    ServerUtils.myGroupInfos(keys.get(i),myGroupInfo);
                                }
                            }
                        }else {
                            for (int i = 0; i < userArrayList.size(); i++) {
                                data1.put(userArrayList.get(i).getUsername(),msg+userArrayList.get(i).getUserid()+"");
                                dataMap.put(userArrayList.get(i).getUsername(),msg+userArrayList.get(i).getUserid()+"");
                            }

                            for (String key : data1.keySet()) {
                                nameList.add(key);
                                msg = msg + key + "、@";
                            }
                            if (msg.length()>3) {
                                msg = msg.substring(0,msg.length()-2);
                            }

                            Intent intent = new Intent(this,CommentsActivity.class);
                            intent.putExtra("names",msg);
                            intent.putExtra("messageId", messageId);
                            intent.putExtra("commentCount", count);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("list",dataMap);
                            intent.putExtra("bundle",bundle);
                            setResult(222,intent);
                            finish();
                        }

                    }else{
                        Intent intent = new Intent(this,SendGroupMesActivity.class);
                        Bundle bundle = new Bundle();
                        if(myMap != null) {
                            bundle.putSerializable("group", myMap);
                        }
                        bundle.putSerializable("Message", (Serializable) mFriendsList);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();

                    }
                }else{
                    Toast.makeText(SendGroupActivity.this,"请选择联系人",Toast.LENGTH_SHORT).show();
                }
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

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
        if(v.getId() == R.id.line_qun){
            startActivityForResult(new Intent(this, GroupSelectActivity.class), 101);
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
                        isClick = true;
                    }
                }
            }
            mAdapter.notifyDataSetChanged();
            if (friendId.equals("★002")) {
                startActivity(new Intent(this, GroupListActivity.class));
            }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1007) {
            updateDate();
        }

        if (requestCode == 20) {
            updateDate();
        }

        if(requestCode == 101){
            if (data != null) {
                myMap = (MyMap) data.getSerializableExtra("group");
                isClick = true;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
//        Friend group = new Friend("★002", "群组", getResources().getResourceName(R.drawable.de_group_default_portrait));
//        userMap.put("★", group);
//        friendsArrayList.add(group);
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
        ArrayList<User> userInfos = (ArrayList<User>) Singleton.getInstance().getFriendlist();;

        mFriendsList = new ArrayList<Friend>();

        if (userInfos != null) {
            for (User userInfo : userInfos) {
                Friend friend = new Friend();
                friend.setNickname(userInfo.getUsername());
                friend.setPortrait(userInfo.getImagePosition());
                friend.setUserId(userInfo.getUserid() + "");
                mFriendsList.add(friend);
            }
        }
        mFriendsList = sort(mFriendsList);
        mAdapter = new SendGroupAdapter(this, mFriendsList);
        mListView.setAdapter(mAdapter);
        fillData();
    }

    private final void fillData() {
        if (mFriendsList != null) {
            mAdapter.setAdapterData(mFriendsList);
            mAdapter.notifyDataSetChanged();
        }
    }

    //自由建群
    private RequestCallBack<String> myGroupInfo = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            DemoApiJSON demoApiJSON = JsonUtils.jsonToPojo(json, DemoApiJSON.class);
            switch (demoApiJSON.getStatus()) {
                case 200://群组信息获取成功
                    List<User> groupMemberList = JsonUtils.getGroup(json);

                    if (type.equals("addMsg")){
                        userArrayList.addAll(groupMemberList);
                        index++;
                        if (index == keys.size()) {
                            for (int i = 0; i < userArrayList.size(); i++) {
                                data.put(userArrayList.get(i).getUsername(),names+ userArrayList.get(i).getUserid()+"");
                            }

                            for (String key : data.keySet()) {
                                ids = ids + data.get(key)+"/";
                                names = names + key + "、";
                            }

                            Intent intent;
                            if (getIntent().hasExtra("share")) {
                                intent = new Intent(SendGroupActivity.this,AddShareActivity.class);
                                intent.putExtra("record",record);
                            }else {
                                intent = new Intent(SendGroupActivity.this,AddMessageActivity.class);
                                intent.putExtra("tag",tagList);
                            }
                            if (getIntent().hasExtra("topic")) {
                                intent.putExtra("topic",topic);
                            }
                            intent.putExtra("type","2");
                            intent.putExtra("ids",ids);
                            intent.putExtra("names",names);
                            intent.putExtra("publicScope","1");
                            startActivity(intent);
                            finish();

//                            Intent intent = new Intent(SendGroupActivity.this,AddMessageActivity.class);
//                            intent.putExtra("type","2");
//                            intent.putExtra("ids",ids);
//                            intent.putExtra("names",names);
//                            startActivity(intent);
//                            finish();
                        }
                    }else {
                        userArrayList.addAll(groupMemberList);
                        index++;
                        if (index == keys.size()) {
                            for (int i = 0; i < userArrayList.size(); i++) {
                                data1.put(userArrayList.get(i).getUsername(),msg+userArrayList.get(i).getUserid()+"");
                                dataMap.put(userArrayList.get(i).getUsername(),msg+userArrayList.get(i).getUserid()+"");
                            }

                            for (String key : data1.keySet()) {
                                nameList.add(key);
                                msg = msg + key + "、@";
                            }
                            if (msg.length()>3) {
                                msg = msg.substring(0,msg.length()-2);
                            }

                            Intent intent = new Intent(SendGroupActivity.this,CommentsActivity.class);
                            intent.putExtra("names",msg);
                            intent.putExtra("messageId", messageId);
                            intent.putExtra("commentCount", count);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("list",dataMap);
                            intent.putExtra("bundle",bundle);
                            setResult(222,intent);
                            finish();
                        }
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

}
