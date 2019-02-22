package org.bigdata.zczw.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.bigdata.zczw.App;
import org.bigdata.zczw.R;
import org.bigdata.zczw.Singleton;
import org.bigdata.zczw.adapter.DeAddressMultiChoiceAdapter;
import org.bigdata.zczw.entity.DemoApiJSON;
import org.bigdata.zczw.entity.Friend;
import org.bigdata.zczw.entity.User;
import org.bigdata.zczw.ui.DePinnedHeaderListView;
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

import io.rong.imkit.mention.RongMentionManager;
import io.rong.imlib.model.UserInfo;

/*
* 选择联系人
* */

public class MentionUserActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, SearchView.OnQueryTextListener {


    protected DeAddressMultiChoiceAdapter mAdapter;
    private DePinnedHeaderListView mListView;

    private String targetId;
    private int type;
    /**
     * 好友list
     */
    private List<User> groupMemberList;
    protected List<Friend> mFriendsList;
    private List<Friend> mSearchFriendsList;
    private TextView textViwe;

    private static View view;
    //搜索框
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mention_user);

        getSupportActionBar().setTitle("选择联系人");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);

        initView();
        initData();
    }

    private void initView() {
        searchView = (SearchView) findViewById(R.id.seacher_friends_mention);
        mListView = (DePinnedHeaderListView) findViewById(R.id.de_ui_friend_list_mention);

        mListView.setFastScrollEnabled(false);
        mListView.setOnItemClickListener(this);

        mListView.setHeaderDividersEnabled(false);
        mListView.setFooterDividersEnabled(false);
        searchView.setIconifiedByDefault(false);//自动缩小为图标
        searchView.setOnQueryTextListener(this);//事件监听器
        searchView.setSubmitButtonEnabled(false);//不显示搜索按钮
        searchView.setQueryHint("搜索");//默认显示文本
    }
    private void initData() {
        targetId = getIntent().getStringExtra("TargetId");
        groupMemberList = new ArrayList<>();
        type = Singleton.getInstance().getGroupTypeById(targetId);
        if (type == 1 ) {
            ServerUtils.mGroupInfoById(targetId, groupInfo);
        }else{
            ServerUtils.myGroupInfos(targetId, groupInfo);
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
                    mFriendsList = new ArrayList<>();
                    mSearchFriendsList = new ArrayList<>();
                    if (groupMemberList != null) {
                        for (User userInfo : groupMemberList) {
                            String id = userInfo.getUserid() + "";
                            if (!SPUtil.getString(MentionUserActivity.this, App.USER_ID).equals(id)) {
                                User user = Singleton.getInstance().getFriendById(id);
                                Friend friend = new Friend();
                                friend.setNickname(userInfo.getUsername());
                                friend.setGroupname(user.getUnitsName()+ "."+  user.getPositionName() +"."+user.getJobsName());
                                friend.setPortrait(userInfo.getImagePosition());
                                friend.setUserId(id);
                                friend.setIsSelected(false);
                                mFriendsList.add(friend);
                            }

                        }
                    }
                    mAdapter = new DeAddressMultiChoiceAdapter(MentionUserActivity.this, mFriendsList);
                    mListView.setAdapter(mAdapter);
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

    private final void fillData() {
        mAdapter.setAdapterData(mFriendsList);
        mAdapter.notifyDataSetChanged();
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object tagObj = view.getTag();
        if (tagObj != null && tagObj instanceof DeAddressMultiChoiceAdapter.ViewHolder) {
            DeAddressMultiChoiceAdapter.ViewHolder viewHolder = (DeAddressMultiChoiceAdapter.ViewHolder) tagObj;
            String friendId = viewHolder.friend.getUserId();
            UserInfo userInfo = Singleton.getInstance().getUserInfoById(MentionUserActivity.this,friendId);
            RongMentionManager.getInstance().mentionMember(userInfo);
            finish();
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
            mSearchFriendsList = searchItem(newText, mFriendsList);
            mAdapter.setAdapterData(mSearchFriendsList);
            mAdapter.notifyDataSetChanged();
        }
        return false;
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
