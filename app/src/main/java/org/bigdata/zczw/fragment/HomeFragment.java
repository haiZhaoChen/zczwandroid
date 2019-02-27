package org.bigdata.zczw.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.mob.MobSDK;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.platformtools.Util;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.bigdata.zczw.App;
import org.bigdata.zczw.R;
import org.bigdata.zczw.SendGroupMsg.SendGroupActivity;
import org.bigdata.zczw.activity.CommentsActivity;
import org.bigdata.zczw.activity.GroupListActivity;
import org.bigdata.zczw.activity.LoginActivity;
import org.bigdata.zczw.activity.MessageActivity;
import org.bigdata.zczw.activity.MsgPowerActivity;
import org.bigdata.zczw.activity.PersonalActivity;
import org.bigdata.zczw.activity.SearchActivity;
import org.bigdata.zczw.activity.ThemeListActivity;
import org.bigdata.zczw.activity.UserInfoActivity;
import org.bigdata.zczw.adapter.HomeListAdapter;
import org.bigdata.zczw.entity.Author;
import org.bigdata.zczw.entity.Bean;
import org.bigdata.zczw.entity.DemoApiJSON;
import org.bigdata.zczw.entity.FeedEntity;
import org.bigdata.zczw.entity.Flags;
import org.bigdata.zczw.entity.MsgTag;
import org.bigdata.zczw.entity.MsgTags;
import org.bigdata.zczw.entity.Record;
import org.bigdata.zczw.ui.LoadingDialog;
import org.bigdata.zczw.ui.WinToast;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.SPUtil;
import org.bigdata.zczw.utils.ServerUtils;
import org.bigdata.zczw.utils.Utils;
import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.handmark.pulltorefresh.library.PullToRefreshListView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qq.QQ;

/**
 * A simple {@link Fragment} subclass.
 * 主页面 动态列表
 */
public class HomeFragment extends Fragment implements PullToRefreshBase.OnRefreshListener<ListView>, View.OnClickListener {

    private Timer mTimer;// 计时器
    private ImageView imgMsg;
//    private RadioButton group,all;
    private ImageView search;
    private TextView themeTitle,themeNote;

    private View convertView;
    private ImageView headerView;
    private ListView mlistView;

    private ImageView icon;
    private LinearLayout llType;
    private TextView txtTitle;
    private Toolbar toolbar;

    private PullToRefreshListView pListView;// 动态列表
    private LoadingDialog mDialog;
    private List<Record> recordList = new ArrayList<>();
    private HomeListAdapter itemAdapter;//适配器
    public static int TYPE = 1;
    public static Map<Integer, Boolean> checkMap;
    //类型(上拉,下拉,默认)
    private int type = 2;
    private String messageidbefore;
    //点赞操作的评论的id
    private long messageId;
    private int prasieCount;
    private int collectCount;
    private String sameUnit;

    private Author author;

    private boolean autoRefresh = false;
    private boolean header = false;
    private PopupWindow popupWindow,sharepop;
    private View contentView,shareView;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 11){
                itemAdapter.notifyDataSetChanged();
                SPUtil.put(getContext(), "firstinit", true);
            }else if (msg.what == 101) {
                pListView.onRefreshComplete();
            }
        }
    };
    private Record record;
    private String url;
    private IWXAPI api;

    private TagFlowLayout tagFlowLayout;
    private String [] mVals ;
    private LayoutInflater mInflater;
    private ArrayList<MsgTag> arrayList;
    private TagAdapter<String> mAdapter;
    private int setTag;
    private String tagId;
    private boolean isDiscuss = false;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        imgMsg = (ImageView) rootView.findViewById(R.id.img_add_msg_home_frg);
        search = (ImageView) rootView.findViewById(R.id.img_search_home_frg);

        llType = (LinearLayout) rootView.findViewById(R.id.ll_type_feed_frg);
        icon = (ImageView) rootView.findViewById(R.id.img_icon_feed_frg);
        txtTitle = (TextView) rootView.findViewById(R.id.txt_type_feed_frg);

        pListView = (PullToRefreshListView) rootView.findViewById(R.id.plistview);
        toolbar = (Toolbar) rootView.findViewById(R.id.tool_feed_frg);

        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_header,null);
        themeTitle = (TextView) convertView.findViewById(R.id.txt_theme_title);
        themeNote = (TextView) convertView.findViewById(R.id.txt_theme_note);
        mlistView = pListView.getRefreshableView();

        checkMap = new HashMap<>();

        boolean firstinit = SPUtil.getBoolean(getContext(),"firstinit");
        if(!firstinit){
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(11);
                }
            }, 3000);
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);// 使得音量键控制媒体声音

        MobSDK.init(getContext(),"23fb848228b3c","e40c45495b17d674849f2ac9cd3d87e4");
        api = WXAPIFactory.createWXAPI(getContext(),"wx40ff322a1e3a5848",true);

        if (mDialog != null) {
            mDialog.show();
        } else {
            mDialog = new LoadingDialog(this.getActivity());
            mDialog.show();
        }

        contentView = LayoutInflater.from(getContext()).inflate(R.layout.pop_feed_type, null);

        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        ServerUtils.getThemeFlag(flag);
        //初始获取动态数据

        /*
         * 设置PullToRefresh刷新模式 BOTH:上拉刷新和下拉刷新都支持 DISABLED：禁用上拉下拉刷新
		 * PULL_FROM_START:仅支持下拉刷新（默认） PULL_FROM_END：仅支持上拉刷新
		 * MANUAL_REFRESH_ONLY：只允许手动触发
		 */
        pListView.setMode(PullToRefreshBase.Mode.BOTH);
        pListView.setOnRefreshListener(this);
        pListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (header) {
                    if (position == 1) {
                        Intent intent = new Intent(getContext(), ThemeListActivity.class);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(getContext(), MessageActivity.class);
                        intent.putExtra("msg",recordList.get(position-2).getMessageId());
                        intent.putExtra("record",recordList.get(position-2));
                        startActivityForResult(intent, 101);
                    }
                }else {
                    Intent intent = new Intent(getContext(), MessageActivity.class);
                    intent.putExtra("msg",recordList.get(position-1).getMessageId());
                    intent.putExtra("record",recordList.get(position-1));
                    startActivityForResult(intent, 101);
                }

            }
        });
        sameUnit = "";
        imgMsg.setOnClickListener(this);
        search.setOnClickListener(this);
        llType.setOnClickListener(this);

        mInflater = LayoutInflater.from(getContext());
        setTag = 0;
        tagId = "";
        ServerUtils.getMsgTag(requestCallBack);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_add_msg_home_frg:
                showPopupMenu(imgMsg);
                break;
            case R.id.img_search_home_frg:
                startActivity(new Intent(getContext(), SearchActivity.class));
                break;
            case R.id.ll_type_feed_frg:
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    icon.setImageResource(R.drawable.icon_down);
                }else {
                    showPopWindow(sameUnit);
                    icon.setImageResource(R.drawable.icon_up);
                }
                break;

        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public RequestCallBack<String> flag = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            Flags flags = JsonUtils.jsonToPojo(json,Flags.class);
            if (flags != null) {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                if (flags.getStatus() == 200) {
                    if (flags.getData().getFlag().equals("1") && !header) {
                        header = true;
                        themeTitle.setText(flags.getData().getThemeTitle());
                        themeNote.setText(flags.getData().getThemeOutline());
                        mlistView.addHeaderView(convertView);
                    }
                    if (flags.getData().getFlag().equals("0") && header) {
                        mlistView.removeHeaderView(convertView);
                    }
                    ServerUtils.getMessageListFirst("","","",message);
                }
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };

    public RequestCallBack<String> message = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            FeedEntity feedEntity = JsonUtils.jsonToPojo(json, FeedEntity.class);
            recordList = feedEntity.getData();
            if ( recordList == null || recordList.size()==0 ) {
                pListView.onRefreshComplete();
                WinToast.toast(getActivity(), "暂无新数据！");
                //为FeedEntity装载数据
                itemAdapter = new HomeListAdapter(getContext(), recordList,"home");
                // 绑定适配器
                pListView.setAdapter(itemAdapter);
            }
            switch (feedEntity.getStatus()) {
                case 200://请求成功
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    //为FeedEntity装载数据
                    itemAdapter = new HomeListAdapter(getContext(), recordList,"home");
                    // 绑定适配器
                    pListView.setAdapter(itemAdapter);
                    itemAdapter.setOnCheckBarClickListener(onCheckBarClickListener);
                    pListView.onRefreshComplete();
                    break;
                case 400://客户端请求错误
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    pListView.onRefreshComplete();
                    break;
                case 444://session过期,跳转到登录界面
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    pListView.onRefreshComplete();
                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                    WinToast.toast(getActivity(), "登录过期,请重新登录");
                    getActivity().finish();
                    break;
                case 500://服务端错误
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    WinToast.toast(getActivity(), feedEntity.getMsg());
                    pListView.onRefreshComplete();
                    break;
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
            if (mDialog != null)
                mDialog.dismiss();
            pListView.onRefreshComplete();
            WinToast.toast(getActivity(), "动态信息获取失败");
        }
    };

    public RequestCallBack<String> messageRefresh = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            FeedEntity feedEntity = JsonUtils.jsonToPojo(json, FeedEntity.class);
            List<Record> list = feedEntity.getData();
            switch (feedEntity.getStatus()) {
                case 200://请求成功
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    if(TYPE ==0){
                        if (list.size()>0) {
                            recordList.addAll(0,list);
                            itemAdapter.notifyDataSetChanged();
                        }else{
                            if(!autoRefresh){
                                WinToast.toast(getActivity(), "没有更新的数据啦");
                            }
                        }
                    }else{
                        if (list.size()>0) {
                            recordList.addAll(list);
                            itemAdapter.notifyDataSetChanged();
                        }else{
                            WinToast.toast(getActivity(), "已经到最后一页了");
                        }
                    }
                    autoRefresh = false;
                    pListView.onRefreshComplete();
                    break;
                case 400://客户端请求错误
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    pListView.onRefreshComplete();
                    break;
                case 444://session过期,跳转到登录界面
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    pListView.onRefreshComplete();
                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                    WinToast.toast(getActivity(), "登录过期,请重新登录");
                    getActivity().finish();
                    break;
                case 500://服务端错误
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
//                    WinToast.toast(getActivity(), feedEntity.getMsg());
                    pListView.onRefreshComplete();
                    break;
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
            if (mDialog != null)
                mDialog.dismiss();
            pListView.onRefreshComplete();
            WinToast.toast(getActivity(), "动态信息获取失败");
        }
    };

    private RequestCallBack<String> requestCallBack = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            MsgTags bean = JsonUtils.jsonToPojo(json,MsgTags.class);
            if (bean != null && bean.getStatus() == 200 && bean.getData()!=null && bean.getData().size()>0) {
                arrayList = (ArrayList<MsgTag>) bean.getData();
                mVals = new String[arrayList.size()+1];
                mVals[0] = "全部";
                for (int i = 0; i < arrayList.size(); i++) {
                    MsgTag msgTag = arrayList.get(i);
                    if (msgTag.getId() == 100002) continue;
                    mVals[i+1] = arrayList.get(i).getName();
                }

                mAdapter = new TagAdapter<String>(mVals) {

                    @Override
                    public View getView(FlowLayout flowLayout, int i, String s) {
                        TextView tv = (TextView) mInflater.inflate(R.layout.pop_tag_text, tagFlowLayout, false);
                        tv.setText(s);
                        return tv;
                    }

                    @Override
                    public void onSelected(int position, View view) {
                        view.setBackground(getResources().getDrawable(R.drawable.frame_radius_yellow));
                        ((TextView)view).setTextColor(Color.parseColor("#e6731c"));
                        super.onSelected(position, view);
                    }

                    @Override
                    public void unSelected(int position, View view) {
                        view.setBackground(getResources().getDrawable(R.drawable.frame_radius_gray0));
                        ((TextView)view).setTextColor(Color.parseColor("#1e1e1e"));
                        super.unSelected(position, view);
                    }
                };
            }else {
                Utils.showToast(getContext(),"服务器异常");
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };


    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {

        String str = DateUtils.formatDateTime(getActivity(),
                System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
        // 下拉刷新 业务代码
        if (refreshView.isShownHeader()) {
            pListView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
            pListView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
            pListView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
            refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新时间:" + str);

            if (sameUnit.equals("3")) {
                ServerUtils.mGetMessageHotList("", "20", "1" , sameUnit,"",message);
            }else if (sameUnit.equals("2")) {
                ServerUtils.getMessageListFirst(tagId,sameUnit,"1",message);
            }else {
                ServerUtils.getMessageListFirst(tagId,sameUnit,"",message);
            }
        }

        // 上拉加载更多 业务代码
        if (refreshView.isShownFooter()) {
            pListView.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
            pListView.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
            pListView.getLoadingLayoutProxy().setReleaseLabel("释放开始加载");
            refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后加载时间:" + str);
            String lastMessageId = null;
            if (itemAdapter != null) {
                lastMessageId = itemAdapter.getLastMessageId();
            }
            if (lastMessageId != null) {
                messageidbefore = lastMessageId;
                TYPE = 1;
                if (sameUnit.equals("3")) {
//                    WinToast.toast(getActivity(), "没有更多数据啦");
//                    handler.sendEmptyMessage(101);
                    ServerUtils.mGetMessageHotList(lastMessageId, "20", "1" , sameUnit,"",messageRefresh);
                }else if (sameUnit.equals("2")) {
                    ServerUtils.mGetMessageList(tagId,lastMessageId, "20", "1",sameUnit,"1", messageRefresh);
                }else {
                    ServerUtils.mGetMessageList(tagId,lastMessageId, "20", 1 + "",sameUnit,"", messageRefresh);
                }
            } else {
                pListView.onRefreshComplete();
            }
        }
    }

    RequestCallBack<String> messageCollect = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
//            Log.e("1111", "onSuccess: "+json);
            DemoApiJSON demoApiJSON = JsonUtils.jsonToPojo(json, DemoApiJSON.class);
            if(demoApiJSON.getStatus() != 500){
                itemAdapter.refreshCollectCount(messageId, collectCount);
            }else{
                if (collectCount <=record.getCollectNum()) {//执行取消收藏操作
//                    Log.e("1111", "执行取消收藏操作: ");
                    itemAdapter.refreshCollectCount(messageId, collectCount);
                }else{//执行收藏操作
//                    Log.e("1111", "执行收藏操作: ");
                    itemAdapter.refreshCollectCount(messageId, collectCount);
                }
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
            WinToast.toast(getActivity(), "收藏失败");
        }
    };

    RequestCallBack<String> messagePraise = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            DemoApiJSON demoApiJSON = JsonUtils.jsonToPojo(json, DemoApiJSON.class);
            if(demoApiJSON.getStatus() != 500){
                itemAdapter.refreshPraiseCount(messageId, prasieCount);
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
            WinToast.toast(getActivity(), e.getMessage());

        }
    };

    RequestCallBack<String> messageDelete = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            DemoApiJSON demoApiJSON = JsonUtils.jsonToPojo(json, DemoApiJSON.class);
            if(demoApiJSON.getMsg().equals("OK")){
                itemAdapter.refreshMsg(messageId);
            }else{
                WinToast.toast(getActivity(), demoApiJSON.getMsg());
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
            WinToast.toast(getActivity(), e.getMessage());

        }
    };

    RequestCallBack<String> shareUrl = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            Bean bean = JsonUtils.jsonToPojo(json, Bean.class);
            if(bean.getMsg().equals("OK") && !TextUtils.isEmpty(bean.getData())){
                url = bean.getData();
                showSharePop(record);
            }else{
                Utils.showToast(getContext(),"原动态已被删除");
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
            WinToast.toast(getActivity(), e.getMessage());

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getActivity().getIntent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if (data == null) {
            return;
        }
        if (requestCode == 2) {
            boolean change = data.getBooleanExtra("change",false);
            if (change){
                String messageId = data.getStringExtra("messageId");
                int commentCount = data.getIntExtra("commentNum",0);
                itemAdapter.refreshCommentCount(Long.parseLong(messageId), commentCount+"");
            }
        }

        if (resultCode == 101) {
            Record record = (Record) data.getSerializableExtra("msg");
            itemAdapter.refreshMsg(record.getMessageId(),record);
        }
        if (resultCode == 2001) {
            long id = data.getLongExtra("delete",0);
            for (int i = 0; i < recordList.size(); i++) {
                if (recordList.get(i).getMessageId() == id) {
                    recordList.remove(i);
                }
            }
            itemAdapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void callDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("联系人："+ author.getName()+"\n联系电话："+ author.getPhone());
        builder.setTitle("是否拨打电话联系此人？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String phoneStr = author.getPhone();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phoneStr.substring(0, 11)));
                //开启系统拨号器
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    private void deleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("是否删除本动态？");
        builder.setTitle("删除动态提醒：");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ServerUtils.deleteMessage(messageId,messageDelete);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    private void errorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("如此用户发送的内容含有非法、暴力、色情等不合规信息，可点击提交，管理人员会在24小时内进行审核并对不合规内容删除处理。");
        builder.setTitle("提示：");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

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

    private void showPopWindow(String str1 ) {
        // 一个自定义的布局，作为显示的内容
        TextView reset = (TextView) contentView.findViewById(R.id.txt_reset_feed_pop);
        TextView ok = (TextView) contentView.findViewById(R.id.txt_ok_feed_pop);
        final RadioGroup rgfeed = (RadioGroup) contentView.findViewById(R.id.rg_feed);
        LinearLayout linearLayout = (LinearLayout) contentView.findViewById(R.id.ll_black);
        tagFlowLayout = (TagFlowLayout) contentView.findViewById(R.id.id_flow_layout);
        if (isDiscuss){
            rgfeed.check(R.id.rb_feed_discusion);
        }else {
            switch (str1){
                case "":
                    rgfeed.check(R.id.rb_feed_all);
                    break;
                case "1":
                    rgfeed.check(R.id.rb_feed_bm);
                    break;
                case "2":
                    rgfeed.check(R.id.rb_feed_dx);
                    break;
                case "4":
                    rgfeed.check(R.id.rb_feed_hot);
                    break;
            }
        }


        if (mAdapter != null) {
            mAdapter.setSelectedList(setTag);
            tagFlowLayout.setAdapter(mAdapter);
        }

        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                icon.setImageResource(R.drawable.icon_down);
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });


        rgfeed.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.rb_feed_all:
                        sameUnit = "";
                        isDiscuss = false;
                        break;
                    case R.id.rb_feed_bm:
                        sameUnit = "1";
                        isDiscuss = false;
                        break;
                    case R.id.rb_feed_dx:
                        sameUnit = "2";
                        isDiscuss = false;
                        break;
                    case R.id.rb_feed_hot:
                        sameUnit = "3";
                        isDiscuss = false;
                        break;

                    case R.id.rb_feed_discusion:
                        sameUnit = "";
                        isDiscuss = true;
                        //标签选择为全部
                        mAdapter.setSelectedList(0);
                        tagFlowLayout.setAdapter(mAdapter);
                        setTag = 0;
                        tagId="";
                        break;
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.setSelectedList(0);
                tagFlowLayout.setAdapter(mAdapter);

                rgfeed.check(R.id.rb_feed_all);
                sameUnit = "";
                setTag = 0;
                tagId="";
                isDiscuss = false;
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<Integer> set = tagFlowLayout.getSelectedList();

                for (int index : set) {
                    setTag = index;
                }
                if (setTag  == 0) {
                    if (isDiscuss){
                        tagId = "100002";
                    }else {
                        tagId = "";
                    }

                }else {
                    tagId = arrayList.get(setTag-1).getId()+"";
                }
                String tagDis="";
                if (isDiscuss){
                    tagDis=arrayList.get(arrayList.size()-1)+"";
                }
                switch (sameUnit){
                    case "":
                        sameUnit = "";
                        txtTitle.setText("全部动态");
                        ServerUtils.getMessageListFirst(tagId,sameUnit,"",message);
                        popupWindow.dismiss();
                        break;
                    case "1":
                        sameUnit = "1";
                        txtTitle.setText("部门动态");
                        ServerUtils.getMessageListFirst(tagId,sameUnit,"",message);
                        popupWindow.dismiss();
                        break;
                    case "2":
                        sameUnit = "2";
                        txtTitle.setText("定向动态");
                        ServerUtils.getMessageListFirst(tagId,sameUnit,"1",message);
                        popupWindow.dismiss();
                        break;
                    case "3":
                        sameUnit = "3";
                        txtTitle.setText("每日热帖");
                        ServerUtils.mGetMessageHotList("", "20", "1" , sameUnit,"",message);
                        popupWindow.dismiss();
                        break;
                }
            }
        });

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                icon.setImageResource(R.drawable.icon_down);
            }
        });
        popupWindow.setAnimationStyle(R.style.anim_pop_down_dir);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        backgroundAlpha(0.6f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                icon.setImageResource(R.drawable.icon_down);
                backgroundAlpha(1f);
            }
        });

        // 设置好参数之后再show
        popupWindow.showAsDropDown(toolbar);

    }

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }

    private void showSharePop(final Record record) {
        shareView = LayoutInflater.from(getContext()).inflate(R.layout.pop_share, null);

        sharepop = new PopupWindow(this.shareView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        LinearLayout llZc = (LinearLayout) shareView.findViewById(R.id.ll_zc_share_pop);
        LinearLayout llQQ = (LinearLayout) shareView.findViewById(R.id.ll_qq_share_pop);
        LinearLayout llWeChat = (LinearLayout) shareView.findViewById(R.id.ll_weChat_share_pop);
        LinearLayout llCircle = (LinearLayout) shareView.findViewById(R.id.ll_circle_share_pop);
        TextView cancel = (TextView) shareView.findViewById(R.id.txt_cancel_share_pop);


        View.OnClickListener popListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    //取消
                    case R.id.txt_cancel_share_pop:
                        sharepop.dismiss();
                        break;
                    //张承
                    case R.id.ll_zc_share_pop:
//                        startActivity(new Intent(MainActivity.this, MsgPowerActivity.class));
                        Intent intent = new Intent(getContext(), MsgPowerActivity.class);
                        intent.putExtra("record",record);
                        intent.putExtra("share",0);
                        startActivity(intent);
                        sharepop.dismiss();
                        break;
                    //QQ
                    case R.id.ll_qq_share_pop:
                        shareQQ(record);
                        sharepop.dismiss();
                        break;
                    //微信
                    case R.id.ll_weChat_share_pop:
                        if(!api.isWXAppInstalled()){
                            Utils.showToast(getContext(),"您没有安装微信");
                            return ;
                        }
                        shareWeChat(record);
                        sharepop.dismiss();
                        break;
                    //朋友圈
                    case R.id.ll_circle_share_pop:
                        if(!api.isWXAppInstalled()){
                            Utils.showToast(getContext(),"您没有安装微信");
                            return ;
                        }
                        shareWeChatCircle(record);
                        sharepop.dismiss();
                        break;
                }
            }
        };

        llZc.setOnClickListener(popListener);
        llQQ.setOnClickListener(popListener);
        llWeChat.setOnClickListener(popListener);
        llCircle.setOnClickListener(popListener);
        cancel.setOnClickListener(popListener);

        sharepop.setTouchable(true);

        sharepop.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
            }
        });

        sharepop.setAnimationStyle(R.style.anim_popup_dir);

        // 设置好参数之后再show
        sharepop.showAtLocation(getActivity().findViewById(R.id.home_fragment), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void shareQQ(Record record) {
        OnekeyShare oks = new OnekeyShare();

        if (record.getPictures() != null && record.getPictures().size()>0) {
            oks.setImageUrl(record.getPictures().get(0).getUrl());
        }else {
            oks.setImageUrl("http://zczw.ewonline.org:8093/images/ic_launcher.png");
        }
        oks.setTitleUrl(url);
        oks.setText(record.getContent());
        oks.setTitle("张承政务APP-内容分享");

        oks.setPlatform(QQ.NAME);
        oks.show(getContext());
    }

    private void shareWeChat(Record record) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "张承政务APP-内容分享";
        if (record.getContent().length()>100) {
            msg.description = record.getContent().substring(0,99);
        }else {
            msg.description = record.getContent();
        }

        if (record.getPictures()!=null && record.getPictures().size()>0) {
            try{
                Bitmap thumb = BitmapFactory.decodeStream(new URL(record.getPictures().get(0).getUrl()).openStream());
                //注意下面的这句压缩，120，150是长宽。
                // 一定要压缩，不然会分享失败
                Bitmap thumbBmp = Bitmap.createScaledBitmap(thumb,120,120,true);
                //Bitmap回收
                thumb.recycle();
                msg.thumbData= Util.bmpToByteArray(thumbBmp,true);
            }catch(IOException e) {
                e.printStackTrace();
            }

        }else {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.logo);
            msg.thumbData = Util.bmpToByteArray(bitmap,true);
        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);

    }

    private void shareWeChatCircle(Record record) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "张承政务APP-内容分享";
        if (record.getContent().length()>100) {
            msg.description = record.getContent().substring(0,99);
        }else {
            msg.description = record.getContent();
        }
        if (record.getPictures()!=null && record.getPictures().size()>0) {
            try{
                Bitmap thumb = BitmapFactory.decodeStream(new URL(record.getPictures().get(0).getUrl()).openStream());
                //注意下面的这句压缩，120，150是长宽。
                // 一定要压缩，不然会分享失败
                Bitmap thumbBmp = Bitmap.createScaledBitmap(thumb,120,120,true);
                //Bitmap回收
                thumb.recycle();
                msg.thumbData= Util.bmpToByteArray(thumbBmp,true);
            }catch(IOException e) {
                e.printStackTrace();
            }

        }else {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.logo);
            msg.thumbData = Util.bmpToByteArray(bitmap,true);
        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }


    HomeListAdapter.onCheckBarClickListener onCheckBarClickListener = new HomeListAdapter.onCheckBarClickListener() {
        @Override
        public void onCommentsClick(String type, Record recordMessage) {
            record = recordMessage;
            switch (type) {
                case "0"://评论
                    Intent intent = new Intent(getActivity(), CommentsActivity.class);//查看评论
                    intent.putExtra("messageId", recordMessage.getMessageId() + "");
                    intent.putExtra("commentCount", recordMessage.getCommentNum() + "");
                    startActivityForResult(intent, 2);
                    break;
                case "1"://收藏
                    messageId = recordMessage.getMessageId();
                    if (recordMessage.getCollectNum() < 0) {
                        collectCount = 1;
                    }else{
                        collectCount = recordMessage.getCollectNum()+1;
                    }
                    ServerUtils.collectMessage(messageId, 1, messageCollect);
                    break;
                case "12"://取消收藏
                    messageId = recordMessage.getMessageId();
                    if (recordMessage.getCollectNum() == 0) {
                        collectCount = 0;
                    }else{
                        collectCount = recordMessage.getCollectNum()-1;
                    }
                    ServerUtils.collectMessage(messageId, 2, messageCollect);
                    break;
                case "2"://赞
                    messageId = recordMessage.getMessageId();
                    prasieCount = recordMessage.getPraiseNum()+1;
                    ServerUtils.prasieMessage(messageId, 1, messagePraise);
                    break;
                case "22"://取消赞
                    messageId = recordMessage.getMessageId();
                    prasieCount = recordMessage.getPraiseNum()-1;
                    ServerUtils.prasieMessage(messageId, 2, messagePraise);
                    break;
                case "3"://点击头像
                    String authorId =recordMessage.getAuthor().getId()+"";
                    if(authorId.equals(SPUtil.getString(getContext(), App.USER_ID))){
                        startActivity(new Intent(getContext(), UserInfoActivity.class));
                    }else{
                        Intent authorIntent = new Intent(getContext(), PersonalActivity.class);
                        authorIntent.putExtra("PERSONAL",authorId);
                        startActivity(authorIntent);
                    }
                    break;
                case "41"://删除
                    messageId = recordMessage.getMessageId();
                    deleteDialog();
                    break;
                case "42"://举报
                    errorDialog();
                    break;
                case "43"://联系此人
                    author = recordMessage.getAuthor();
                    callDialog();
                    break;
                case "44"://分享
                    author = recordMessage.getAuthor();
                    if (recordMessage.getForwardMessage() != null) {
                        if (recordMessage.getForwardMessage().getPublicScope() == 10) {
                            ServerUtils.getShareUrl(recordMessage.getMessageId()+"",shareUrl);
                        }else {
                            Utils.showToast(getContext(),"因权限限制，无法转发本动态。");
                        }
                    }else {
                        if (recordMessage.getPublicScope() == 10) {
                            ServerUtils.getShareUrl(recordMessage.getMessageId()+"",shareUrl);
                        }else {
                            Utils.showToast(getContext(),"因权限限制，无法转发本动态。");
                        }
                    }

                    break;
                case "6"://详情
                    Intent intentContent = new Intent(getContext(), MessageActivity.class);
                    intentContent.putExtra("msg",recordMessage.getMessageId());
                    startActivityForResult(intentContent,101);
                    break;
                case "7"://详情
                    Intent intent7 = new Intent(getContext(), MessageActivity.class);
                    intent7.putExtra("msg",record.getMessageId());
                    intent7.putExtra("record",record);
                    startActivityForResult(intent7,101);
                    break;
                case "8"://详情
                    Record shareRecord = record.getForwardMessage();
                    Intent intent8 = new Intent(getContext(), MessageActivity.class);
                    intent8.putExtra("msg",shareRecord.getMessageId());
                    intent8.putExtra("record",shareRecord);
                    startActivityForResult(intent8,111);
                    break;
            }
        }
    };

}
