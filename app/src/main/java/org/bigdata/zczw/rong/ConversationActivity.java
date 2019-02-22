package org.bigdata.zczw.rong;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.bigdata.zczw.App;
import org.bigdata.zczw.R;
import org.bigdata.zczw.Singleton;
import org.bigdata.zczw.activity.LoginActivity;
import org.bigdata.zczw.activity.MainActivity;
import org.bigdata.zczw.activity.MentionUserActivity;
import org.bigdata.zczw.entity.DemoApiJSON;
import org.bigdata.zczw.entity.User;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.SPUtil;
import org.bigdata.zczw.utils.ServerUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imkit.mention.IMentionedInputListener;
import io.rong.imkit.mention.RongMentionManager;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

public class ConversationActivity extends AppCompatActivity {
    /**
     * 目标 Id
     */
    private String mTargetId;
    /**
     * 刚刚创建完讨论组后获得讨论组的id 为targetIds，需要根据 为targetIds 获取 targetId
     */
    private String mTargetIds;

    /**
     * 会话类型
     */
    private Conversation.ConversationType mConversationType;

    private String title;
    private HashMap<String,String> userSelect;//群发用户list
    private RongIM.IGroupMemberCallback mMentionMemberCallback;

    private ArrayList<Message> msgList;
    private Message msg[];
    private boolean IsHave = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        mTargetId = getIntent().getData().getQueryParameter("targetId");
        mTargetIds = getIntent().getData().getQueryParameter("targetIds");
        title = getIntent().getData().getQueryParameter("title");
        mConversationType = Conversation.ConversationType.valueOf(getIntent().getData().getLastPathSegment().toUpperCase());

        initView();
        initList();
        isPushMessage(getIntent());
//        enterFragment(mConversationType, mTargetId);

        RongIM.getInstance().setGroupMembersProvider(new RongIM.IGroupMembersProvider() {
            @Override
            public void getGroupMembers(String groupId, RongIM.IGroupMemberCallback callback) {
                getGroupMembersForMention();
                mMentionMemberCallback = callback;
            }
        });

        RongIM.setConversationBehaviorListener(new MyConversationBehaviorListener());

    }

    private void getGroupMembersForMention() {
        if (mTargetId.length()>10) {
            ServerUtils.mGroupInfoById(mTargetId,  requestCallBack);
        }else {
            ServerUtils.myGroupInfos(mTargetId,  requestCallBack);
        }

    }

    private RequestCallBack requestCallBack = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            DemoApiJSON demoApiJSON = JsonUtils.jsonToPojo(json, DemoApiJSON.class);
            switch (demoApiJSON.getStatus()) {
                case 200://群组信息获取成功
                    List<User> groupMembers = JsonUtils.getGroup(json);
                    List<UserInfo> userInfos = new ArrayList<>();
                    if (groupMembers != null) {
                        for (User groupMember : groupMembers) {
                            if (groupMember != null) {
                                UserInfo userInfo = new UserInfo(groupMember.getUserid()+"", groupMember.getUsername(),
                                        Uri.parse(groupMember.getImagePosition()));
                                userInfos.add(userInfo);
                            }
                        }
                    }
                    mMentionMemberCallback.onGetGroupMembersResult(userInfos);
                    break;
                default:
                    mMentionMemberCallback.onGetGroupMembersResult(null);
                    break;
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
            mMentionMemberCallback.onGetGroupMembersResult(null);
        }
    };

    private void initView() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);

        if (mConversationType != null) {
            if (mConversationType.toString().equals("PRIVATE")) {
                if(title.equals("group")){
                    getSupportActionBar().setTitle("群发消息");
                }else{
                    getSupportActionBar().setTitle(title);
                }
            } else if (mConversationType.toString().equals("GROUP")) {
                getSupportActionBar().setTitle(title);
            }
        }
    }

    private void initList() {
        userSelect = Singleton.getInstance().getUserSelect();
        if (userSelect != null) {
            msgList = new ArrayList<>();
//            Log.e("1111", "userSelect: "+userSelect.size());
        }
        if (RongIM.getInstance() != null) {
            RongIM.getInstance().setSendMessageListener(new RongIM.OnSendMessageListener() {
                @Override
                public Message onSend(Message message) {

                    if (userSelect != null && userSelect.size()>0){
                        if (Singleton.getInstance().isSendGroupMsg()) {
                            msgList.add(message);
                        }
                    }
                    return message;
                }

                @Override
                public boolean onSent(Message message, RongIM.SentMessageErrorCode sentMessageErrorCode) {
                    return false;
                }
            });
        }
        RongMentionManager.getInstance().setMentionedInputListener(listener);
    }

    IMentionedInputListener listener = new IMentionedInputListener() {
        @Override
        public boolean onMentionedInput(Conversation.ConversationType conversationType, String s) {
            Intent intent = new Intent(ConversationActivity.this, MentionUserActivity.class);
            intent.putExtra("TargetId", mTargetId);
            startActivity(intent);
            return true;
        }
    };


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (!mTargetId.equals(SPUtil.getString(this,App.USER_ID))) {
            if (mConversationType.equals(Conversation.ConversationType.GROUP)) {
                inflater.inflate(R.menu.group_conversation_menu, menu);
            } else {
                inflater.inflate(R.menu.private_conversation_menu, menu);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Singleton.getInstance().setSendGroupMsg(false);
                Singleton.getInstance().setChange(true);
                if (userSelect != null && userSelect.size()>0 && msgList.size()>0){
                    SendThread thread = new SendThread(msgList,userSelect);
                    thread.start();
                }
                finish();
                break;
            case R.id.icon:
                if (mConversationType == null)
                    return true;
                enterSettingActivity();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 根据 targetid 和 ConversationType 进入到设置页面
     */
    private void enterSettingActivity() {
        if (mConversationType.toString().equals("GROUP")) {
            if(IsHave){
                Intent intent = new Intent(ConversationActivity.this, GroupDetailActivity.class);
                intent.putExtra("TargetId", mTargetId);
                startActivity(intent);
            }else{
                Toast.makeText(ConversationActivity.this, "群组已解散", Toast.LENGTH_SHORT).show();
            }

        } else if (mConversationType.toString().equals("PRIVATE")) {
            Intent intent = new Intent(this, FriendDetailActivity.class);
            intent.putExtra("TargetId", mTargetId);
            startActivity(intent);
        }

    }

    private RequestCallBack<String> groupInfo = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
//            Log.e("1111", "onSuccess: "+json);
            DemoApiJSON demoApiJSON = JsonUtils.jsonToPojo(json, DemoApiJSON.class);
            switch (demoApiJSON.getStatus()){
                case 404:
                    IsHave = false;
                    RongIM.getInstance().removeConversation(Conversation.ConversationType.GROUP, mTargetId, new RongIMClient.ResultCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean aBoolean) {
//                            Log.e("1111", "onSuccess: 群组已解散");
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {

                        }
                    });
                    break;
                case 200:
                    break;
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Singleton.getInstance().setSendGroupMsg(false);
            if (userSelect != null && userSelect.size()>0 && msgList.size()>0){
                SendThread thread = new SendThread(msgList,userSelect);
                thread.start();
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    private ConversationFragment fragment;
    /**
     * 加载会话页面 ConversationFragmentEx 继承自 ConversationFragment
     *
     * @param mConversationType 会话类型
     * @param mTargetId         会话 Id
     */
    private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {

        fragment = new ConversationFragment();

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", mTargetId).build();

        fragment.setUri(uri);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //xxx 为你要加载的 id
        transaction.add(R.id.rong_content, fragment);
        transaction.commitAllowingStateLoss();
    }


    /**
     * 判断是否是 Push 消息，判断是否需要做 connect 操作
     */
    private void isPushMessage(Intent intent) {

        if (intent == null || intent.getData() == null)
            return;
        //push
        if (intent.getData().getScheme().equals("rong") && intent.getData().getQueryParameter("isFromPush") != null) {

            //通过intent.getData().getQueryParameter("push") 为true，判断是否是push消息
           if (RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {

                if (intent.getData().getPath().contains("conversation/system")) {
                    Intent intent1 = new Intent(ConversationActivity.this, MainActivity.class);
                    intent1.putExtra("systemconversation", true);
                    startActivity(intent1);
                    return;
                }
//                else {
//                    Intent intent1 = new Intent(ConversationActivity.this, MainActivity.class);
//                    intent1.putExtra("systemconversation", true);
//                    startActivity(intent1);
//                }
                enterActivity();
            } else {
                if (intent.getData().getPath().contains("conversation/system")) {
                    Intent intent1 = new Intent(ConversationActivity.this, MainActivity.class);
                    intent1.putExtra("systemconversation", true);
                    startActivity(intent1);
                    return;
                }

                enterFragment(mConversationType, mTargetId);
            }

        } else {
            if (Singleton.getInstance().getUserInfoById(ConversationActivity.this,mTargetId) != null) {
                RongIM.getInstance().refreshUserInfoCache(Singleton.getInstance().getUserInfoById(ConversationActivity.this,mTargetId));
            }
            if (RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        enterActivity();
                    }
                }, 300);
            } else {
                enterFragment(mConversationType, mTargetId);
            }
        }
    }

    /**
     * 收到 push 消息后，选择进入哪个 Activity
     * 如果程序缓存未被清理，进入 MainActivity
     * 程序缓存被清理，进入 LoginActivity，重新获取token
     * <p>
     * 作用：由于在 manifest 中 intent-filter 是配置在 ConversationActivity 下面，所以收到消息后点击notifacition 会跳转到 DemoActivity。
     * 以跳到 MainActivity 为例：
     * 在 ConversationActivity 收到消息后，选择进入 MainActivity，这样就把 MainActivity 激活了，当你读完收到的消息点击 返回键 时，程序会退到
     * MainActivity 页面，而不是直接退回到 桌面。
     */
    private void enterActivity() {

        String token = SPUtil.getString(ConversationActivity.this, App.USER_TOKEN);

        if (TextUtils.isEmpty(token)) {
            startActivity(new Intent(ConversationActivity.this, LoginActivity.class));
        } else {
            reconnect(token);
        }
    }

    private void reconnect(String token) {
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {

            }

            @Override
            public void onSuccess(String s) {
                enterFragment(mConversationType, mTargetId);

            }

            @Override
            public void onError(RongIMClient.ErrorCode e) {
            }
        });

    }

}
