package org.bigdata.zczw.rong;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.bigdata.zczw.R;
import org.bigdata.zczw.Singleton;
import org.bigdata.zczw.activity.PersonalActivity;
import org.bigdata.zczw.entity.User;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

public class FriendDetailActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Switch no,top;
    private ImageView img;
    private TextView name,clean;

    private String userId;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);
        getSupportActionBar().setTitle("用户详情");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);
        initView();
        initDate();
    }
    private void initView() {
        no = (Switch) findViewById(R.id.switch_no_note);
        top = (Switch) findViewById(R.id.switch_msg_top);
        img = (ImageView) findViewById(R.id.img_friend_detail);
        name = (TextView) findViewById(R.id.txt_name_detail);
        clean = (TextView) findViewById(R.id.msg_clean);
    }
    private void initDate() {
        userId = getIntent().getStringExtra("TargetId");
        user = Singleton.getInstance().getFriendById(userId);
        if (!TextUtils.isEmpty(user.getImagePosition())) {
            Picasso.with(this).load(user.getImagePosition()).into(img);
        }
        name.setText(user.getUsername());

        initSwitch();

        no.setOnCheckedChangeListener(this);
        top.setOnCheckedChangeListener(this);

        clean.setOnClickListener(this);
        img.setOnClickListener(this);
    }

    private void initSwitch() {
        if (RongIM.getInstance() != null) {
            RongIM.getInstance().getConversation(Conversation.ConversationType.PRIVATE,
                    userId,
                    new RongIMClient.ResultCallback<Conversation>() {
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
                    }
            );

            RongIM.getInstance().getConversationNotificationStatus(Conversation.ConversationType.PRIVATE,
                    userId,
                    new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
                        @Override
                        public void onSuccess(Conversation.ConversationNotificationStatus conversationNotificationStatus) {

                            if (conversationNotificationStatus == Conversation.ConversationNotificationStatus.DO_NOT_DISTURB ? true : false) {
                                no.setChecked(true);
                            } else {
                                no.setChecked(false);
                            }
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {

                        }
                    }
            );
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.switch_no_note:

                Conversation.ConversationNotificationStatus cns;
                if (isChecked) {
                    cns = Conversation.ConversationNotificationStatus.DO_NOT_DISTURB;
                } else {
                    cns = Conversation.ConversationNotificationStatus.NOTIFY;
                }

                if(isChecked){
                    RongIM.getInstance().setConversationNotificationStatus(Conversation.ConversationType.PRIVATE,
                            userId,
                            cns,
                            new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
                                @Override
                                public void onSuccess(Conversation.ConversationNotificationStatus conversationNotificationStatus) {

                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {

                                }
                            });
                }
                break;
            case R.id.switch_msg_top:

                RongIM.getInstance().setConversationToTop(Conversation.ConversationType.PRIVATE,
                        userId,
                        isChecked,
                        new RongIMClient.ResultCallback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean aBoolean) {

                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {

                            }
                        });
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.msg_clean:
                cleanMsg();
                break;
            case R.id.img_friend_detail:
                Intent authorIntent = new Intent(FriendDetailActivity.this, PersonalActivity.class);
                authorIntent.putExtra("PERSONAL",userId);
                authorIntent.putExtra("tag",1);//1聊天 0其他 2
                authorIntent.putExtra("type",2);//1群聊 2 私聊
                startActivity(authorIntent);
                finish();
                break;
        }
    }

    private void cleanMsg() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("是否清空聊天信息？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                RongIM.getInstance().deleteMessages(io.rong.imlib.model.Conversation.ConversationType.PRIVATE,
                        userId, new RongIMClient.ResultCallback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean aBoolean) {
                                Toast.makeText(FriendDetailActivity.this, "清除成功", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {
                                Toast.makeText(FriendDetailActivity.this, "清除失败", Toast.LENGTH_SHORT).show();
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
}
