package org.bigdata.zczw.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.bigdata.zczw.App;
import org.bigdata.zczw.R;
import org.bigdata.zczw.SendGroupMsg.SendGroupActivity;
import org.bigdata.zczw.Singleton;
import org.bigdata.zczw.activity.GroupListActivity;
import org.bigdata.zczw.activity.StartActivity;
import org.bigdata.zczw.entity.GroupInfo;
import org.bigdata.zczw.entity.Groups;
import org.bigdata.zczw.rong.MyConversationBehaviorListener;
import org.bigdata.zczw.rong.MyConversationListAdapter;
import org.bigdata.zczw.ui.WinToast;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.SPUtil;
import org.bigdata.zczw.utils.ServerUtils;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.widget.adapter.ConversationListAdapter;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyListFragment extends Fragment {

    private ImageView imgMsg;
    private ConversationListFragment fragment;
    private int index;

    public MyListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_list,container,false);
        imgMsg = (ImageView) view.findViewById(R.id.img_add_msg_list_frg);
        fragment = (ConversationListFragment) getChildFragmentManager().findFragmentById(R.id.conversationlist);

        fragment.setAdapter(new MyConversationListAdapter(getContext()));
        Uri uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")
                .build();

        fragment.setUri(uri);

        imgMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(imgMsg);
            }
        });
        return  view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        RongIM.setGroupInfoProvider(new RongIM.GroupInfoProvider() {
            @Override
            public Group getGroupInfo(String s) {
                Group groupInfo =Singleton.getInstance().getGroupById(s);
                index++;
                if (groupInfo != null) {
                    return Singleton.getInstance().getGroupById(s);
                }else if (index < 2){
                    ServerUtils.getMyCircle(0, group);
                }else if (index>1) {
                    RongIM.getInstance().removeConversation(Conversation.ConversationType.GROUP, s, new RongIMClient.ResultCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean aBoolean) {
                            Log.e("1111", "onSuccess: ");
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {
                            Log.e("1111", "onError: ");
                        }
                    });
                }
                return null;
            }
        },true);
    }

    RequestCallBack<String> group = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
//            Log.e("1111", "group: "+json );
            Groups groups = JsonUtils.jsonToPojo(json, Groups.class);
            switch (groups.getStatus()) {
                case 200://获取成功
                    List<Group> groupList = new ArrayList<>();
                    List<GroupInfo> myGroupInfo = groups.getData();
                    for (int i = 0; i < myGroupInfo.size(); i++) {
                        groupList.add(new Group(myGroupInfo.get(i).getId() + "", myGroupInfo.get(i).getName(), null));
                    }
                    Singleton.getInstance().setGrouplist(groupList);
                    Singleton.getInstance().setMyGroups(myGroupInfo);
                    break;
                case 400://客户端错误
                    WinToast.toast(getContext(), "客户端错误");
                    break;
                case 444://登陆过期
                    WinToast.toast(getContext(), "登陆过期");
                    break;
                case 500://服务器错误
                    WinToast.toast(getContext(), "服务器错误");
                    break;
            }
        }
        @Override
        public void onFailure(HttpException e, String s) {

        }
    };

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.de_main_menu, popupMenu.getMenu());      // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add_item1:// 群发消息
                        startActivity(new Intent(getContext(), SendGroupActivity.class));
                        break;
                    case R.id.add_item2://发起群聊
                        Intent intent = new Intent(getContext(),GroupListActivity.class);
                        intent.putExtra("group",true);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

}
