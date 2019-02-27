package org.bigdata.zczw.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.bigdata.zczw.App;
import org.bigdata.zczw.R;
import org.bigdata.zczw.SendGroupMsg.SendGroupActivity;
import org.bigdata.zczw.adapter.ComAdapter;
import org.bigdata.zczw.entity.ComInfo;
import org.bigdata.zczw.entity.Comment;
import org.bigdata.zczw.entity.DemoApiJSON;
import org.bigdata.zczw.ui.LoadingDialog;
import org.bigdata.zczw.ui.WinToast;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.SPUtil;
import org.bigdata.zczw.utils.ScreenUtils;
import org.bigdata.zczw.utils.ServerUtils;
import org.bigdata.zczw.utils.Utils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
* 动态评论列表
* */

public class CommentsActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    @ViewInject(R.id.rvComments)
    RecyclerView rvComments;
    @ViewInject(R.id.etComment)
    EditText etComment;
    @ViewInject(R.id.btnSendComment)
    Button btnSendComment;
    @ViewInject(R.id.llAddComment)
    LinearLayout llCom;
    @ViewInject(R.id.ll_reply_comment)
    LinearLayout llReply;
    @ViewInject(R.id.contentRoot)
    LinearLayout root;


    @ViewInject(R.id.et_reply)
    EditText etReply;
    @ViewInject(R.id.btnSendReply)
    Button btnSendReply;

    private ComAdapter commentAdapter;
    private LoadingDialog mDialog;

    private String messageId;
    private String commentCount;
    private String userIds;
    private String rangeStr;
    private ArrayList<String> nameList = new ArrayList<>();
    private ArrayList<Comment> comments;
    private HashMap<String,String> dataMap = new HashMap<>();

    //评论id
    private String  commentId;
    private int prasieCount;

    //保存评论的内容
    private HashMap<String,String> commentContentMap = new HashMap<>();

    //保存回复的内容
    private HashMap<String,String> replayMap = new HashMap<>();

    private Comment replyCom;
    private boolean isShow;

    private PopupWindow popupWindow;
    private View contentView;
    private Comment comment1;

    private List<Comment> commentList;
    private String commentContext;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 112:
                    etReply.setText("");
                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etReply.getWindowToken(), 0);
                    llCom.setVisibility(View.VISIBLE);
                    llReply.setVisibility(View.GONE);
                    commentList.clear();
                    comments.clear();
                    ServerUtils.getCommentList(messageId+"", 1 , 20 ,comment);
                    break;
            }

        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        //页面消失，如果有数据保存下来
        if (etComment.getText().length()>0){
            App.commentContent.put(messageId,etComment.getText().toString());
            if (!dataMap.isEmpty()){
                App.commentUserData.put(messageId,dataMap);
            }
        }else {
            App.commentContent.remove(messageId);
            App.commentUserData.remove(messageId);
        }

        if (!replayMap.isEmpty()){
            App.replyComment = replayMap;
        }else {
            App.replyComment.clear();
        }

    }


    private ComAdapter.OnCheckPraiseClickListener onCheckPraiseClickListener = new ComAdapter.OnCheckPraiseClickListener() {
        @Override
        public void onPraiseClick(String type, Comment com) {
            commentId = com.getCommentsId();
            if (type.equals("1")){
                prasieCount = com.getPraiseNum()+1;
            }else {
                prasieCount = com.getPraiseNum()-1;
            }

            ServerUtils.sendCommentPraise(com.getCommentsId()+"",type,commentPraiseCallBack);
        }
    };

    private RequestCallBack<String> commentPraiseCallBack = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {

            String json = responseInfo.result;
            DemoApiJSON demoApiJSON = JsonUtils.jsonToPojo(json, DemoApiJSON.class);
            if(demoApiJSON.getStatus() != 500){
                commentAdapter.refreshPraiseCount(commentId, prasieCount);
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

            System.out.println(e.getMessage());
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        AppManager.getAppManager().addActivity(this);

        initView();
        initDate();

        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = root.getRootView().getHeight() - root.getHeight();
                if (heightDiff > 500) { // 如果高度差超过100像素，就很有可能是有软键盘...
                    isShow = true;
                } else {
                    if (isShow  && replyCom!=null) {
                        isShow = false;


                        //退回键盘保存字符，如果再次点击会赋值

                        if (llReply.getVisibility() == View.VISIBLE) {
                            if (etReply.getText().toString().trim().length() > 0) {
                                replayMap.put(replyCom.getCommentsId(), etReply.getText().toString());
                            } else {
                                replayMap.remove(replyCom.getCommentsId());
                            }
                        }

                        llCom.setVisibility(View.VISIBLE);
                        llReply.setVisibility(View.GONE);
                        etReply.setText("");
                    }
                }
            }
        });
    }

    private void initView() {
        ViewUtils.inject(this);
        getSupportActionBar().setTitle("评论");
        setVolumeControlStream(AudioManager.STREAM_MUSIC);// 使得音量键控制媒体声音
        getSupportActionBar().setLogo(R.drawable.empty_logo);// actionbar 添加logo
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);
        if (mDialog == null) {
            mDialog = new LoadingDialog(this);
            mDialog.show();
        }
        btnSendComment.setClickable(false);

        contentView = LayoutInflater.from(this).inflate(R.layout.pop_com_del, null);

        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        llCom.setVisibility(View.VISIBLE);
        llReply.setVisibility(View.GONE);
    }

    private void initDate() {
        userIds = "";
        rangeStr = "";
        commentList = new ArrayList<>();
        comments = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvComments.setLayoutManager(linearLayoutManager);
        rvComments.setHasFixedSize(true);
        rvComments.setOverScrollMode(View.OVER_SCROLL_NEVER);
        messageId = getIntent().getStringExtra("messageId");
        commentCount = getIntent().getStringExtra("commentCount");

        String lastContent = App.commentContent.get(messageId);

        if (lastContent != null){
            etComment.setText(lastContent);
            HashMap<String,String> lastUserDataMap = App.commentUserData.get(messageId);
            if (lastUserDataMap != null && !lastUserDataMap.isEmpty()){
                dataMap = lastUserDataMap;
            }
        }

        if (!App.replyComment.isEmpty()){
            replayMap = App.replyComment;
        }

        if (getIntent().hasExtra("com")) {
            replyCom = (Comment) getIntent().getSerializableExtra("com");
            llCom.setVisibility(View.GONE);
            llReply.setVisibility(View.VISIBLE);
            etReply.setHint("回复："+replyCom.getUserName());


            String valueStr = replayMap.get(replyCom.getCommentsId());
            if (valueStr != null && valueStr.length()>0){
                etReply.setText(valueStr);
            }else {
                etReply.setText("");
            }

            etReply.setFocusableInTouchMode(true);
            etReply.setFocusable(true);
            etReply.requestFocus();

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }

        if (TextUtils.isEmpty(messageId) || TextUtils.isEmpty(commentCount)) {
            if (mDialog != null) {
                mDialog.dismiss();
            }
            WinToast.toast(this, "获取信息错误");
            finish();
            return;
        }

//        ServerUtils.getCommentList(messageId, comment);
        ServerUtils.getCommentList(messageId+"", 1 , 20 ,comment);
        btnSendComment.setOnClickListener(this);
        btnSendReply.setOnClickListener(this);
        etComment.addTextChangedListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnSendComment:
                commentContext = etComment.getText().toString();
                if (TextUtils.isEmpty(commentContext) || commentContext.trim().length() < 1) {
                    WinToast.toast(this, "请输入文字");
                    return;
                }
                if (mDialog != null) {
                    mDialog.show();
                }
                int index = 0;

                for (String key : dataMap.keySet()) {
                    while((index = commentContext.indexOf(key, index)) != -1){
                        rangeStr = rangeStr+(index-1)+"-"+(index + key.length())+"/";
                        userIds = userIds + dataMap.get(key)+"/";
                        index = index + key.length();
                    }
                }
//                if (!TextUtils.isEmpty(rangeStr) && rangeStr.length()>1) {
//                    rangeStr = rangeStr.substring(0,rangeStr.length()-1);
//                }
                if (!TextUtils.isEmpty(userIds) && userIds.length()>1) {
                    userIds = userIds.substring(0,userIds.length()-1);
                }

                ServerUtils.addComment(messageId, messageId, commentContext,userIds,rangeStr,sendComment);
                break;
            case R.id.btnSendReply:
                String reply = etReply.getText().toString().trim();
                if (TextUtils.isEmpty(reply)) {
                    WinToast.toast(this, "请输入文字");
                    return;
                }
                if (replyCom != null) {
                    if (!TextUtils.isEmpty(replyCom.getRootCommentsId()) ) {
                        ServerUtils.replyComment(messageId,replyCom.getRootCommentsId(),replyCom.getCommentsId(),reply,replyComment);

                    }else {
                        ServerUtils.replyComment(messageId,replyCom.getCommentsId(),replyCom.getCommentsId(),reply,replyComment);
                    }
                }
                break;
            case R.id.tv_delete_com_del:
                String user = SPUtil.getString(CommentsActivity.this, App.USER_NAME);
                if (!TextUtils.isEmpty(user) && user.equals(comment1.getUserName())) {
                    ServerUtils.delcm(comment1.getCommentsId(),delComment);
                }else {
                    Utils.showToast(CommentsActivity.this,"无法删除他人评论");
                }
                break;
            case R.id.tv_copy_com_del:
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(comment1.getCommentsContent());
                Utils.showToast(CommentsActivity.this,"复制成功");
                popupWindow.dismiss();
                break;
            case R.id.tv_cancel_com_del:
                popupWindow.dismiss();
                break;
        }

    }

    private RequestCallBack<String> comment = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            DemoApiJSON demoApiJSON = JsonUtils.jsonToPojo(json, DemoApiJSON.class);
            switch (demoApiJSON.getStatus()) {
                case 200://查询成功
                    commentList.clear();
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        String data = jsonObject.getString("data");
                        comments = (ArrayList<Comment>) JsonUtils.jsonToList(data, Comment.class);

                        for (int i = 0; i < comments.size(); i++) {
                            commentList.add(comments.get(i));
                            if (comments.get(i).getListComentReplyInfo()!= null && comments.get(i).getListComentReplyInfo().size()>0) {
                                commentList.addAll(comments.get(i).getListComentReplyInfo());
                            }
                        }

                        commentAdapter = new ComAdapter(CommentsActivity.this, commentList);
                        rvComments.setAdapter(commentAdapter);

                        commentAdapter.setOnLongListener(onLongListener);
                        commentAdapter.setOnCheckPraiseClickListener(onCheckPraiseClickListener);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

                case 400://没有评论
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    commentAdapter = new ComAdapter(CommentsActivity.this, commentList);
                    rvComments.setAdapter(commentAdapter);
                    llCom.setVisibility(View.VISIBLE);
                    WinToast.toast(CommentsActivity.this, "还没有评论哦!");
                    break;
                case 444://登录过期
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    commentAdapter = new ComAdapter(CommentsActivity.this, commentList);
                    rvComments.setAdapter(commentAdapter);
                    WinToast.toast(CommentsActivity.this, "登录过期,请重新登录");
                    startActivity(new Intent(CommentsActivity.this, LoginActivity.class));
                    break;
                case 500://系统错误
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    commentAdapter = new ComAdapter(CommentsActivity.this, commentList);
                    rvComments.setAdapter(commentAdapter);
                    break;

            }

        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };


    private RequestCallBack<String> sendComment = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {


            String json = responseInfo.result;
            ComInfo comInfo = JsonUtils.jsonToPojo(json, ComInfo.class);
            switch (comInfo.getStatus()) {
                case 200:
                    Comment comment = comInfo.getData();
                    commentList.add(0,comment);
                    commentAdapter.notifyDataSetChanged();
                    WinToast.toast(getApplicationContext(), "评论成功");
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    etComment.setText("");
                    userIds= "";
                    rangeStr = "";
                    //清除缓存
                    App.commentContent.remove(messageId);
                    App.commentUserData.remove(messageId);
                    break;
                case 400://客户端错误
                    userIds= "";
                    rangeStr = "";
                    WinToast.toast(getApplicationContext(), "评论出错");
                    mDialog.dismiss();
                    break;
                case 444://登录过期
                    userIds= "";
                    rangeStr = "";
                    WinToast.toast(getApplicationContext(), "登陆过期");
                    break;
                case 500://服务器错误
                    userIds= "";
                    rangeStr = "";
                    WinToast.toast(getApplicationContext(), "评论出错");
                    mDialog.dismiss();
                    break;
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
            Log.e("1111", "onFailure: "+e.toString() );
            WinToast.toast(getApplicationContext(), "评论出错");
            mDialog.dismiss();


        }
    };

    private RequestCallBack<String> replyComment = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            DemoApiJSON demoApiJSON = JsonUtils.jsonToPojo(json, DemoApiJSON.class);
            switch (demoApiJSON.getStatus()) {
                case 200:
                    WinToast.toast(getApplicationContext(), "回复成功");
                    replayMap.remove(replyCom.getCommentsId());
                    handler.sendEmptyMessage(112);
                    break;
                case 400://客户端错误
                    mDialog.dismiss();
                    break;
                case 444://登录过期
                    WinToast.toast(getApplicationContext(), "登陆过期");
                    break;
                case 500://服务器错误
                    break;
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };

    RequestCallBack<String> delComment = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            ComInfo comInfo = JsonUtils.jsonToPojo(json, ComInfo.class);
            switch (comInfo.getStatus()) {
                case 200:
                    WinToast.toast(getApplicationContext(), "删除成功");
                    popupWindow.dismiss();
                    ServerUtils.getCommentList(messageId+"", 1 , 20 ,comment);
                    break;
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        back();
    }
    private void back() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
        Intent intent = new Intent();
        int count = commentAdapter.getItemCount();
        int num;
        if (commentList == null) {
            num = 0;
        }else{
            num = commentList.size();
        }
        if(num == 0){
            intent.putExtra("change",false);
        }else{
            intent.putExtra("change",true);
            intent.putExtra("commentNum",num+"");
            intent.putExtra("messageId", messageId);
            intent.putExtra("commentCount", count + "");
        }
        setResult(2, intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String msg = s.toString();
        if (msg.endsWith("@")) {
            Intent intentSel = new Intent(CommentsActivity.this,SendGroupActivity.class);
            intentSel.putExtra("type","com");
            intentSel.putExtra("msg",msg);
            intentSel.putExtra("messageId", messageId);
            intentSel.putExtra("commentCount", commentContext);
            Bundle bundle = new Bundle();
            bundle.putSerializable("list",dataMap);
            intentSel.putExtra("bundle",bundle);
            startActivityForResult(intentSel,111);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 222 && data!=null) {
            String s = data.getStringExtra("names");
            Bundle bundle = data.getBundleExtra("bundle");
            dataMap = (HashMap<String, String>) bundle.getSerializable("list");
            etComment.setText(s);
            etComment.setSelection(s.length());
        }
    }


    private ComAdapter.OnLongListener onLongListener = new ComAdapter.OnLongListener() {
        @Override
        public void onContentClick(int type,View v, Comment com) {
            if (type == 1) {
                llCom.setVisibility(View.GONE);
                llReply.setVisibility(View.VISIBLE);
                replyCom = com;
                etReply.setHint("回复："+replyCom.getUserName());
                //如果字典里面有这个值就要显示出来
                String valueStr = replayMap.get(replyCom.getCommentsId());
                if (valueStr != null && valueStr.length()>0){
                    etReply.setText(valueStr);
                }else {
                    etReply.setText("");
                }
                etReply.setFocusableInTouchMode(true);
                etReply.setFocusable(true);
                etReply.requestFocus();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }else {
                comment1 = com;
                showWindow(v);
            }
        }
    };

    private void showWindow(View v) {
        TextView textView0 = (TextView) contentView.findViewById(R.id.tv_copy_com_del);
        TextView textView1 = (TextView) contentView.findViewById(R.id.tv_delete_com_del);
        TextView textView2 = (TextView) contentView.findViewById(R.id.tv_cancel_com_del);

        textView0.setOnClickListener(this);
        textView1.setOnClickListener(this);
        textView2.setOnClickListener(this);

        popupWindow.setTouchable(true);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
            }
        });

        View windowContentViewRoot = findViewById(R.id.rlll_com_act);
        int windowPos[] = calculatePopWindowPos(v, windowContentViewRoot);
        int xOff = 240;// 可以自己调整偏移
        windowPos[0] -= xOff;
        popupWindow.showAtLocation(v, Gravity.TOP | Gravity.START, 0-windowPos[0], windowPos[1]);

        // 设置好参数之后再show
//        popupWindow.showAsDropDown(v);
    }

    /**
     * 计算出来的位置，y方向就在anchorView的上面和下面对齐显示，x方向就是与屏幕右边对齐显示
     * 如果anchorView的位置有变化，就可以适当自己额外加入偏移来修正
     * @param anchorView  呼出window的view
     * @param contentView   window的内容布局
     * @return window显示的左上角的xOff,yOff坐标
     */
    private static int[] calculatePopWindowPos(final View anchorView, final View contentView) {
        final int windowPos[] = new int[2];
        final int anchorLoc[] = new int[2];
        // 获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = anchorView.getHeight();
        // 获取屏幕的高宽
        final int screenHeight = ScreenUtils.getScreenHeight(anchorView.getContext());
        final int screenWidth = ScreenUtils.getScreenWidth(anchorView.getContext());
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 计算contentView的高宽
        final int windowHeight = contentView.getMeasuredHeight();
        final int windowWidth = contentView.getMeasuredWidth();

        windowPos[0] = screenWidth - windowWidth;
        windowPos[1] = anchorLoc[1] - 150;

//        // 判断需要向上弹出还是向下弹出显示
//        final boolean isNeedShowUp = (screenHeight - anchorLoc[1] - anchorHeight < windowHeight);
//        if (isNeedShowUp) {
//
//        } else {
//            windowPos[0] = screenWidth - windowWidth;
//            windowPos[1] = anchorLoc[1] + anchorHeight;
//        }
        return windowPos;
    }
}
