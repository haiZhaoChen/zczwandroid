package org.bigdata.zczw.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.LexiconListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
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

import org.bigdata.zczw.App;
import org.bigdata.zczw.R;
import org.bigdata.zczw.adapter.HomeListAdapter;
import org.bigdata.zczw.entity.Author;
import org.bigdata.zczw.entity.Bean;
import org.bigdata.zczw.entity.DemoApiJSON;
import org.bigdata.zczw.entity.FeedEntity;
import org.bigdata.zczw.entity.Record;
import org.bigdata.zczw.ui.WinToast;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.SPUtil;
import org.bigdata.zczw.utils.ServerUtils;
import org.bigdata.zczw.utils.Utils;
import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.handmark.pulltorefresh.library.PullToRefreshListView;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qq.QQ;

/*
* 动态搜索
* */

public class SearchActivity extends AppCompatActivity implements View.OnClickListener,
         AdapterView.OnItemClickListener,PullToRefreshBase.OnRefreshListener<ListView>, TextWatcher, SearchView.OnQueryTextListener {

    private PullToRefreshListView pListView;
    private SearchView searchView;
    private TextView button;
    private RelativeLayout act;
    private ImageView voice;

    private List<Record> recordList = new ArrayList<>();
    private HomeListAdapter itemAdapter;//适配器

    private String word;
    private Author author;

    private Record record;
    private long messageId;
    private int prasieCount;
    private int collectCount;

    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private int count = 0;
    // 语音听写对象
    private SpeechRecognizer mIat;
    // 语音听写UI
    private RecognizerDialog mIatDialog;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    private String title;
    private String url;

    private PopupWindow popupWindow,sharepop;
    private View contentView,shareView;
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setTitle("动态搜索");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);
        AppManager.getAppManager().addActivity(this);

        pListView = (PullToRefreshListView) findViewById(R.id.listView_search_act);
        searchView = (SearchView) findViewById(R.id.search_search_act);
        button = (TextView) findViewById(R.id.btn_search_search_act);
        act = (RelativeLayout) findViewById(R.id.activity_search);
        voice = (ImageView) findViewById(R.id.img_voice_search_act);


        pListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        pListView.setOnRefreshListener(this);
        pListView.setOnItemClickListener(this);

        MobSDK.init(SearchActivity.this,"23fb848228b3c","e40c45495b17d674849f2ac9cd3d87e4");
        api = WXAPIFactory.createWXAPI(SearchActivity.this,"wx40ff322a1e3a5848",true);
        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(SearchActivity.this, mInitListener);

        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        mIatDialog = new RecognizerDialog(SearchActivity.this, mInitListener);

        button.setOnClickListener(this);
        voice.setOnClickListener(this);
        searchView.setOnQueryTextListener(this);;
    }

    @Override
    protected void onResume() {
        super.onResume();
        act.setFocusable(true);
        act.setFocusableInTouchMode(true);
        act.requestFocus();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(SearchActivity.this, MessageActivity.class);
        intent.putExtra("msg",recordList.get(position-1).getMessageId());
        startActivityForResult(intent, 101);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_search_search_act://搜索
                InputMethodManager imm = (InputMethodManager)SearchActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(button.getWindowToken(),0);
                searchView.setFocusable(false);
                if (recordList != null && recordList.size()>0) {
                    recordList.clear();
                    itemAdapter.notifyDataSetChanged();
                }
                if (!TextUtils.isEmpty(word)) {
                    ServerUtils.mFindMessageList("","0","20",word,message);
                }else {
                    Utils.showToast(SearchActivity.this,"请输入搜索内容");
                }
            break;
            case R.id.img_voice_search_act://搜索
                // 设置参数
                setParam();
                mIatDialog.setListener(mRecognizerDialogListener);
                mIatDialog.show();
            break;
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {

        // 上拉加载更多 业务代码
        if (refreshView.isShownFooter()) {
            pListView.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
            pListView.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
            pListView.getLoadingLayoutProxy().setReleaseLabel("释放开始加载");

            if (recordList != null && recordList.size()>0) {
                long id = recordList.get(recordList.size()-1).getMessageId();
                ServerUtils.mFindMessageList(""+id,"1","20",word,messageRefresh);
            }else {
                Utils.showToast(SearchActivity.this,"暂无更多");
            }
        }

    }

    public RequestCallBack<String> message = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            FeedEntity row = JsonUtils.jsonToPojo(json, FeedEntity.class);
            pListView.onRefreshComplete();
            if (row != null && row.getData().size() > 0) {
                recordList = row.getData();
                //为FeedEntity装载数据
                itemAdapter = new HomeListAdapter(SearchActivity.this, recordList,"home");
                // 绑定适配器
                pListView.setAdapter(itemAdapter);
                itemAdapter.setOnCheckBarClickListener(onCheckBarClickListener);
            }else {
                WinToast.toast(SearchActivity.this, "暂无数据！");
            }

        }

        @Override
        public void onFailure(HttpException e, String s) {
            pListView.onRefreshComplete();
            WinToast.toast(SearchActivity.this, "动态信息获取失败");
        }
    };

    public RequestCallBack<String> messageRefresh = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            FeedEntity row = JsonUtils.jsonToPojo(json, FeedEntity.class);
            pListView.onRefreshComplete();
            if (row!=null && row.getData().size()>0) {
                List<Record> list = row.getData();
                recordList.addAll(list);
                itemAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
            pListView.onRefreshComplete();
            WinToast.toast(SearchActivity.this, "动态信息获取失败");
        }
    };


    HomeListAdapter.onCheckBarClickListener onCheckBarClickListener = new HomeListAdapter.onCheckBarClickListener() {
        @Override
        public void onCommentsClick(String type, Record recordMessage) {
            record = recordMessage;
            switch (type) {
                case "0"://评论
                    Intent intent = new Intent(SearchActivity.this, CommentsActivity.class);//查看评论
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
                    if(authorId.equals(SPUtil.getString(SearchActivity.this, App.USER_ID))){
                        startActivity(new Intent(SearchActivity.this, UserInfoActivity.class));
                    }else{
                        Intent authorIntent = new Intent(SearchActivity.this, PersonalActivity.class);
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
                            Utils.showToast(SearchActivity.this,"因权限限制，无法转发本动态。");
                        }
                    }else {
                        if (recordMessage.getPublicScope() == 10) {
                            ServerUtils.getShareUrl(recordMessage.getMessageId()+"",shareUrl);
                        }else {
                            Utils.showToast(SearchActivity.this,"因权限限制，无法转发本动态。");
                        }
                    }

                    break;
                case "6"://详情
                    Intent intentContent = new Intent(SearchActivity.this, MessageActivity.class);
                    intentContent.putExtra("msg",recordMessage.getMessageId());
                    intentContent.putExtra("record",recordMessage);
                    startActivityForResult(intentContent,101);
                    break;
            }
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
                Utils.showToast(SearchActivity.this,"原动态已被删除");
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
            WinToast.toast(SearchActivity.this, e.getMessage());

        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getIntent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
        super.onActivityResult(requestCode, resultCode, data);
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
            WinToast.toast(SearchActivity.this, "收藏失败");
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
            WinToast.toast(SearchActivity.this, e.getMessage());

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
                WinToast.toast(SearchActivity.this, demoApiJSON.getMsg());
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
            WinToast.toast(SearchActivity.this, e.getMessage());

        }
    };

    private void callDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(s.toString())) {
            word = s.toString();
        }else {
            word = "";
            if (recordList != null && recordList.size()>0) {
                recordList.clear();
                itemAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 参数设置
     */
    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT,"0");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/zczw/iat.wav");
    }
    //上传用户词表监听器。
    private LexiconListener lexiconListener = new LexiconListener() {
        @Override
        public void onLexiconUpdated(String lexiconId, SpeechError error) {
            if(error != null){
                Log.d("voice",error.toString());
            }else{
                Log.d("voice","上传成功！");
            }
        }
    };

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                Toast.makeText(SearchActivity.this,"语音初始化失败",Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            printResult(results);
            count++;
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            error.getPlainDescription(true);
        }

    };

    private void printResult(RecognizerResult results) {
        String text = JsonUtils.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }
        if (count %2 ==1) {
            title = resultBuffer.toString();
            word = resultBuffer.toString();
            if (!TextUtils.isEmpty(title)) {
//                searchView.setQueryHint(title);
                int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
                TextView textView = (TextView) searchView.findViewById(id);
                textView.setText(title);
            }
        }
    }


    private void showSharePop(final Record record) {
        shareView = LayoutInflater.from(SearchActivity.this).inflate(R.layout.pop_share, null);

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
                        Intent intent = new Intent(SearchActivity.this, MsgPowerActivity.class);
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
                            Utils.showToast(SearchActivity.this,"您没有安装微信");
                            return ;
                        }
                        shareWeChat(record);
                        sharepop.dismiss();
                        break;
                    //朋友圈
                    case R.id.ll_circle_share_pop:
                        if(!api.isWXAppInstalled()){
                            Utils.showToast(SearchActivity.this,"您没有安装微信");
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
        sharepop.showAtLocation(findViewById(R.id.activity_search), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
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
        oks.show(SearchActivity.this);
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!TextUtils.isEmpty(newText)) {
            word = newText;
        }else {
            word = "";
            if (recordList != null && recordList.size()>0) {
                recordList.clear();
                itemAdapter.notifyDataSetChanged();
            }
        }
        return false;
    }
}
