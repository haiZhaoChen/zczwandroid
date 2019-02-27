package org.bigdata.zczw.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.mob.MobSDK;
import com.squareup.picasso.Picasso;
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
import org.bigdata.zczw.adapter.FileListAdapter;
import org.bigdata.zczw.adapter.NoScrollGridAdapter;
import org.bigdata.zczw.adapter.ViewPagerAdapter;
import org.bigdata.zczw.entity.Author;
import org.bigdata.zczw.entity.Bean;
import org.bigdata.zczw.entity.DemoApiJSON;
import org.bigdata.zczw.entity.FileBean;
import org.bigdata.zczw.entity.MsgTag;
import org.bigdata.zczw.entity.Pictures;
import org.bigdata.zczw.entity.RecoedBean;
import org.bigdata.zczw.entity.Record;
import org.bigdata.zczw.entity.Video;
import org.bigdata.zczw.fragment.CommentFragment;
import org.bigdata.zczw.fragment.UserFragment;
import org.bigdata.zczw.ui.NoScrollGridView;
import org.bigdata.zczw.ui.NoScrollListView;
import org.bigdata.zczw.ui.WinToast;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.NoUnderlineSpan;
import org.bigdata.zczw.utils.SPUtil;
import org.bigdata.zczw.utils.ServerUtils;
import org.bigdata.zczw.utils.Utils;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qq.QQ;

/*


动态详情
*/

public class MessageActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener, AdapterView.OnItemClickListener {

    private ImageView userProfile,videoImg,videoAuto,more;
    private RelativeLayout videoContent,btnComment,btnSave,btnPraise;
    private TextView userName,content,publishTime,location;
    private NoScrollGridView imagesGridview;
    private TextView videoSize,videoLen,readNum;
    private ImageView heart,praise,type;
    private RadioGroup radioGroup;
    private RadioButton rbSave,rbComment,rbPraise;
    private ViewPager viewPager;

    private TextView videoSizeRecord,videoLenRecord;
    private TextView txtRecord;
    private NoScrollGridView recordGridView;
    private RelativeLayout videoContentRecord;
    private ImageView videoRecord,videoAutoRecord;
    private LinearLayout llShare;

    private NoScrollListView listView;

    private PopupWindow sharepop;
    private View shareView;

    private Record record;
    private Author author;

    private List<Fragment> fragmentList = new ArrayList<>();
    private UserFragment saveFragment,praiseFragment;
    private CommentFragment commentFragment;
    private Bundle saveBundle,praiseBundle,commentBundle;
    private ViewPagerAdapter pagerAdapter;
    private long messageId;
    private int collectCount;
    private int prasieCount;

    private String url;
    private IWXAPI api;
    private TagFlowLayout tagFlowLayout;
    private LayoutInflater mInflater;

    private int readTime;
    private RadioButton readBtn;

    private boolean isShowReadBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initView();
        initDate();
    }

    private void initView() {
        getSupportActionBar().setLogo(R.drawable.empty_logo);// actionbar 添加logo
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);
        getSupportActionBar().setTitle("动态详情");
        AppManager.getAppManager().addActivity(this);

        userName = (TextView) findViewById(R.id.msg_username);
        content = (TextView) findViewById(R.id.msg_content_act);
        publishTime = (TextView) findViewById(R.id.msg_publish_time);
        location = (TextView) findViewById(R.id.msg_location);
        videoSize = (TextView) findViewById(R.id.msg_video_size);
        videoLen = (TextView) findViewById(R.id.msg_video_len);
        readNum = (TextView) findViewById(R.id.msg_read_num_act);

        userProfile = (ImageView) findViewById(R.id.msg_userProfile);
        videoImg = (ImageView) findViewById(R.id.msg_video_img);
        videoAuto = (ImageView) findViewById(R.id.msg_auto_video_img);
        more = (ImageView) findViewById(R.id.msg_more_act);
        heart = (ImageView) findViewById(R.id.msg_img_heart);
        praise = (ImageView) findViewById(R.id.msg_img_praise);

        type = (ImageView) findViewById(R.id.img_feed_type);

        videoContent = (RelativeLayout) findViewById(R.id.msg_video_content);
        btnComment = (RelativeLayout) findViewById(R.id.msg_line_comment);
        btnSave = (RelativeLayout) findViewById(R.id.msg_line_like);
        btnPraise = (RelativeLayout) findViewById(R.id.msg_line_prasie);

        rbSave = (RadioButton) findViewById(R.id.msg_rb_save);
        rbComment = (RadioButton) findViewById(R.id.msg_rb_comment);
        rbPraise = (RadioButton) findViewById(R.id.msg_rb_praise);

        radioGroup = (RadioGroup) findViewById(R.id.msg_radioGroup);

        viewPager = (ViewPager) findViewById(R.id.msg_viewPager);

        imagesGridview = (NoScrollGridView)findViewById(R.id.msg_img_gridview);

        txtRecord = (TextView) findViewById(R.id.record_message_content);

        videoSizeRecord = (TextView) findViewById(R.id.record_txt_video_size);
        videoLenRecord = (TextView) findViewById(R.id.record_txt_video_len);
        videoRecord = (ImageView) findViewById(R.id.record_video_img);
        videoAutoRecord = (ImageView) findViewById(R.id.record_auto_video_img);

        recordGridView = (NoScrollGridView) findViewById(R.id.record_img_gridview);
        llShare = (LinearLayout) findViewById(R.id.ll_record_share);

        tagFlowLayout = (TagFlowLayout) findViewById(R.id.id_flow_layout);
        mInflater = LayoutInflater.from(MessageActivity.this);

        listView = (NoScrollListView) findViewById(R.id.list_word_file_feed);


        //找到控件
        readBtn = (RadioButton) findViewById(R.id.fab_btn);
        readBtn.setClickable(false);
        //设置监听
        readBtn.setOnClickListener(this);


        listView.setOnItemClickListener(this);
    }

    private void initDate() {
        MobSDK.init(this,"23fb848228b3c","e40c45495b17d674849f2ac9cd3d87e4");
        api = WXAPIFactory.createWXAPI(this,"wx40ff322a1e3a5848");
        messageId = getIntent().getLongExtra("msg",0L);
        Record record = (Record) getIntent().getSerializableExtra("record");
//        int textSize = record.getContent().length();

        ServerUtils.getMsgById(messageId+"",msgCallback);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent1 = new Intent(MessageActivity.this,FileActivity.class);
        intent1.putExtra("file",record.getFiles().get(position));
        intent1.putExtra("id",record.getMessageId());
        startActivity(intent1);
    }

    //开启时间倒计时
    private void startTimer(){

        CountDownTimer timer = new CountDownTimer(readTime*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                readBtn.setClickable(false);
                readBtn.setText(millisUntilFinished/1000+"");
            }

            @Override
            public void onFinish() {
                readBtn.setText("阅读");
                readBtn.setClickable(true);

            }
        }.start();

//        readBtnTimer = new Timer();
//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                readTime--;
//
//                if (readTime==0){
//                    readBtnTimer.cancel();
//                    readBtnTimer.purge();
//                    readBtn.setText("已读");
//                    readBtn.setClickable(true);
//
//                }
//
//            }
//        };

//        readBtnTimer.schedule(timerTask,0,1000);

    }

    private RequestCallBack<String> msgCallback = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            RecoedBean bean = JsonUtils.jsonToPojo(json,RecoedBean.class);
            if (bean != null && bean.getStatus() == 200 && bean.getData()!=null) {
                record = bean.getData();

                //计算出阅读时间
                //是否展示阅读
                isShowReadBtn = false;
                for (MsgTag tag : record.getTags()){
                    if (tag.getId()>100001){
                        isShowReadBtn = true;
                        break;
                    }
                }
                if (isShowReadBtn){
                    readBtn.setVisibility(View.VISIBLE);
                    if (record.getRead()==0){
                        readTime = record.getContent().length() / 20;
                        if (readTime < 30) readTime = 30;
                        if (readTime >= 120) readTime = 120;
                        startTimer();
                    }else {
                        readBtn.setText("已读");
                        readBtn.setChecked(true);
                    }
                }else {
                    readBtn.setVisibility(View.GONE);
                }


                if (record.getFiles()!=null && record.getFiles().size()>0) {
                    listView.setVisibility(View.VISIBLE);
                    FileListAdapter fileListAdapter = new FileListAdapter(MessageActivity.this, (ArrayList<FileBean>) record.getFiles());
                    listView.setAdapter(fileListAdapter);
                }else {
                    listView.setVisibility(View.GONE);
                }

                if (record.getForwardMessage()!=null) {
                    final Record shareRecord = record.getForwardMessage();
                    llShare.setVisibility(View.VISIBLE);

                    txtRecord.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MessageActivity.this, MessageActivity.class);
                            intent.putExtra("msg",shareRecord.getMessageId());
                            intent.putExtra("record",shareRecord);
                            startActivity(intent);
                        }
                    });

                    String name ;
                    if (!TextUtils.isEmpty(shareRecord.getAuthor().getUnitsName())) {
                        name = shareRecord.getAuthor().getName()+"-"+shareRecord.getAuthor().getUnitsName();
                    }else {
                        name = shareRecord.getAuthor().getName();
                    }
                    SpannableStringBuilder builder = new SpannableStringBuilder(name+"："+shareRecord.getContent());
                    //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色

                    builder.setSpan(new ForegroundColorSpan(Color.parseColor("#128ece")), 0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    txtRecord.setText(builder);

                    final List<Pictures> recordPicturesList = shareRecord.getPictures();//图片列表
                    if (recordPicturesList == null || recordPicturesList.size() == 0) { // 没有图片资源就隐藏GridView
                        recordGridView.setVisibility(View.GONE);
                    } else {
                        recordGridView.setVisibility(View.VISIBLE);
                        recordGridView.setAdapter(new NoScrollGridAdapter(MessageActivity.this, recordPicturesList));
                    }
                    // 点击回帖九宫格，查看大图
                    recordGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            imageBrower(position, recordPicturesList);
                        }
                    });

                    final Video recordVideo = shareRecord.getVideo();
                    if (recordVideo != null) {
                        videoContentRecord.setVisibility(View.VISIBLE);
                        videoAutoRecord.setImageResource(android.R.drawable.ic_media_play);
                        Picasso.with(MessageActivity.this).load(recordVideo.getThumbnail()).into(videoRecord);
                        String size = recordVideo.getSize()+"Mb";
                        videoSizeRecord.setText(size);
                        Long time = recordVideo.getLength();
                        String len;
                        int min = (int) (time/60);
                        int sec = (int) (time%60);
                        if(min == 0){
                            if(sec>=10){
                                len = "00:"+sec;
                            }else{
                                len = "00:0"+sec;
                            }
                        }else if(min<10){
                            if(sec>=10){
                                len = "0" + min + ":"+sec;
                            }else{
                                len = "0"+ min +":0"+sec;
                            }
                        }else{
                            if(sec>=10){
                                len = min + ":"+sec;
                            }else{
                                len = min +":0"+sec;
                            }
                        }
                        videoLenRecord.setText(len);
                        videoRecord.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String videoPath = recordVideo.getPath();
                                Uri uri = Uri.parse(videoPath);
                                Intent intentPlayer = new Intent(Intent.ACTION_VIEW);
                                intentPlayer.setDataAndType(uri, "video/mp4");
                                startActivity(intentPlayer);
                            }
                        });
                    }else{
                        videoContent.setVisibility(View.GONE);
                    }

                }else {
                    llShare.setVisibility(View.GONE);
                }

                if (record.getTags()!=null && record.getTags().size()>0) {
                    tagFlowLayout.setVisibility(View.VISIBLE);
                    String [] mVals = new String[record.getTags().size()];
                    for (int i = 0; i < record.getTags().size(); i++) {
                        mVals[i] = record.getTags().get(i).getName();
                    }

                    tagFlowLayout.setAdapter(new TagAdapter<String>(mVals) {

                        @Override
                        public View getView(FlowLayout flowLayout, int i, String s) {
                            TextView tv = (TextView) mInflater.inflate(R.layout.show_tag_text, tagFlowLayout, false);
                            tv.setText(s);
                            return tv;
                        }
                    });
                }else {
                    tagFlowLayout.setVisibility(View.GONE);
                }

                switch (record.getPublicScope()){//0:未知; 1:特定; 5:部门; 10:全部;
                    case 0:
                        type.setVisibility(View.GONE);
                        break;
                    case 1:
                        type.setImageResource(R.drawable.icon_feed_type_dx);
                        break;
                    case 5:
                        type.setImageResource(R.drawable.icon_feed_type_bm);
                        break;
                    case 10:
                        type.setImageResource(R.drawable.icon_feed_type_all);
                        break;
                }

                author = record.getAuthor();
                if (!TextUtils.isEmpty(author.getUnitsName())) {
                    userName.setText(author.getName()+"-"+author.getUnitsName());
                }else {
                    userName.setText(author.getName());
                }
                readNum.setText(record.getViewNum()+"阅读");

                if(!TextUtils.isEmpty(author.getPortrait())) {
                    Picasso.with(MessageActivity.this).load(author.getPortrait()).into(userProfile);
                }else{
                    userProfile.setImageResource(R.drawable.de_default_portrait);
                }

                final List<Pictures> picturesList = record.getPictures();//图片列表
                if (picturesList == null || picturesList.size() == 0) { // 没有图片资源就隐藏GridView
                    imagesGridview.setVisibility(View.GONE);
                } else {
                    imagesGridview.setVisibility(View.VISIBLE);
                    imagesGridview.setAdapter(new NoScrollGridAdapter(MessageActivity.this, picturesList));
                }
                // 点击回帖九宫格，查看大图
                imagesGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        imageBrower(position, picturesList);
                    }
                });

                final Video video = record.getVideo();
                if (video != null) {
                    videoContent.setVisibility(View.VISIBLE);
                    videoAuto.setImageResource(android.R.drawable.ic_media_play);
                    Picasso.with(MessageActivity.this).load(video.getThumbnail()).into(videoImg);
                    String size = video.getSize()+"Mb";
                    videoSize.setText(size);
                    Long time = video.getLength();
                    String len;
                    int min = (int) (time/60);
                    int sec = (int) (time%60);
                    if(min == 0){
                        if(sec>=10){
                            len = "00:"+sec;
                        }else{
                            len = "00:0"+sec;
                        }
                    }else if(min<10){
                        if(sec>=10){
                            len = "0" + min + ":"+sec;
                        }else{
                            len = "0"+ min +":0"+sec;
                        }
                    }else{
                        if(sec>=10){
                            len = min + ":"+sec;
                        }else{
                            len = min +":0"+sec;
                        }
                    }
                    videoLen.setText(len);
                    videoImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String videoPath = video.getPath();
                            Uri uri = Uri.parse(videoPath);
                            Intent intentPlayer = new Intent(Intent.ACTION_VIEW);
                            intentPlayer.setDataAndType(uri, "video/mp4");
                            startActivity(intentPlayer);
                        }
                    });
                }else{
                    videoContent.setVisibility(View.GONE);
                }

                if (TextUtils.isEmpty(record.getLocation()) || record.getLocation().equals("未定位") || record.getLocation().equals("位置不明确")) {
                    location.setText("未定位");
                    location.setVisibility(View.GONE);
                } else {
                    location.setVisibility(View.VISIBLE);
                    location.setText(record.getLocation() + "");
                }

                String time = record.getPublishedTime();

                long old = Long.parseLong(time);
                Date date = new Date();
                long now = date.getTime();
                long t = now-old;
                if (t < 60*1000*5) {
                    publishTime.setText("刚刚");
                }else if (t <60*1000*60) {
                    int s= (int) (t/60/1000);
                    publishTime.setText(s+"分钟前");
                }else {
                    if (isToday(old)) {
                        SimpleDateFormat sdf = new SimpleDateFormat("今天 HH:mm");
                        String dealtTime = sdf.format(new Date(Long.parseLong(time)));
                        publishTime.setText(dealtTime);
                    }else if (isYesterday(old)) {
                        SimpleDateFormat sdf = new SimpleDateFormat("昨天 HH:mm");
                        String dealtTime = sdf.format(new Date(Long.parseLong(time)));
                        publishTime.setText(dealtTime);
                    }else {
                        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
                        String dealtTime = sdf.format(new Date(Long.parseLong(time)));
                        publishTime.setText(dealtTime);
                    }
                }

                content.setText(record.getContent());

                if (!TextUtils.isEmpty(record.getTopicRangeStr())) {
                    content.setAutoLinkMask(0);
                    Linkify.addLinks(content,Linkify.WEB_URLS);

                    Pattern trendsPattern = Pattern.compile("(#[\\S][^#]{0,28}#)");
                    String trendsScheme = String.format("%s/?%s=", App.TOPIC_SCHEMA, App.PARAM_UID);
                    Linkify.addLinks(content, trendsPattern, trendsScheme, null, new
                            Linkify.TransformFilter() {
                                @Override
                                public String transformUrl(Matcher match, String url) {
                                    Log.d("1111", match.group(1));
                                    return match.group(1);
                                }
                            });
                    removeHyperLinkUnderline(content);
                }



                rbSave.setText("收藏 "+record.getCollectNum());
                rbComment.setText("评论 "+record.getCommentNum());
                rbPraise.setText("赞 "+record.getPraiseNum());
                if (record.getCollect() ==1) {
                    heart.setImageResource(R.drawable.ic_heart_red);
                }
                if (record.getPraise() ==1) {
                    praise.setImageResource(R.drawable.ic_praise_select);
                }
                initViewPager();
                initListen();
            }else {
                dialog();
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
        }
    };

    private void removeHyperLinkUnderline(TextView tv) {

        CharSequence text = tv.getText();

        if(text instanceof Spannable){
            Spannable spannable = (Spannable) tv.getText();

            NoUnderlineSpan noUnderlineSpan = new NoUnderlineSpan();

            spannable.setSpan(noUnderlineSpan,0,text.length(), Spanned.SPAN_MARK_MARK);

        }

    }

    private void initViewPager() {
        fragmentList.clear();
        saveFragment = new UserFragment();
        praiseFragment = new UserFragment();
        commentFragment = new CommentFragment();

        saveBundle = new Bundle();
        saveBundle.putString("type", "save");
        saveBundle.putLong("id", record.getMessageId());

        praiseBundle = new Bundle();
        praiseBundle.putString("type", "praise");
        praiseBundle.putLong("id", record.getMessageId());

        commentBundle = new Bundle();
        commentBundle.putLong("id", record.getMessageId());

        saveFragment.setArguments(saveBundle);
        praiseFragment.setArguments(praiseBundle);
        commentFragment.setArguments(commentBundle);

        fragmentList.add(saveFragment);
        fragmentList.add(commentFragment);
        fragmentList.add(praiseFragment);
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(pagerAdapter);

        if (rbComment.isChecked()) {
            rbSave.setTextColor(Color.GRAY);
            rbComment.setTextColor(Color.BLACK);
            rbPraise.setTextColor(Color.GRAY);
            viewPager.setCurrentItem(1);
        }
        if (rbSave.isChecked()) {
            rbSave.setTextColor(Color.BLACK);
            rbComment.setTextColor(Color.GRAY);
            rbPraise.setTextColor(Color.GRAY);
            viewPager.setCurrentItem(0);
        }
        if (rbPraise.isChecked()) {
            rbSave.setTextColor(Color.GRAY);
            rbComment.setTextColor(Color.GRAY);
            rbPraise.setTextColor(Color.BLACK);
            viewPager.setCurrentItem(2);
        }
    }

    private void initListen() {
        userProfile.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnComment.setOnClickListener(this);
        btnPraise.setOnClickListener(this);
        more.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(this);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //点击头像
            case R.id.msg_userProfile:
                String authorId =record.getAuthor().getId()+"";
                if(authorId.equals(SPUtil.getString(MessageActivity.this, App.USER_ID))){
                    startActivity(new Intent(MessageActivity.this, UserInfoActivity.class));
                }else{
                    Intent authorIntent = new Intent(MessageActivity.this, PersonalActivity.class);
                    authorIntent.putExtra("PERSONAL",authorId);
                    startActivity(authorIntent);
                }
                break;
            //收藏
            case R.id.msg_line_like:
                if (record.getCollect() == 0) {
                    heart.setImageResource(R.drawable.ic_heart_red);
                    rbSave.setText("收藏 " + (record.getCollectNum() + 1));
                    if (record.getCollectNum() < 0) {
                        collectCount = 1;
                    }else{
                        collectCount = record.getCollectNum()+1;
                    }
                    ServerUtils.collectMessage(messageId, 1, messageCollect);

                } else {
                    heart.setImageResource(R.drawable.ic_heart_outline_grey);
                    if (record.getCollectNum() > 0) {
                        rbSave.setText("收藏 "+(record.getCollectNum()-1));
                    }else{
                        rbSave.setText("收藏 0");
                    }
                    if (record.getCollectNum() == 0) {
                        collectCount = 0;
                    }else{
                        collectCount = record.getCollectNum()-1;
                    }
                    ServerUtils.collectMessage(messageId, 2, messageCollect);
                }
                break;
            //评论
            case R.id.msg_line_comment:
                Intent intent = new Intent(MessageActivity.this, CommentsActivity.class);//查看评论
                intent.putExtra("messageId", record.getMessageId() + "");
                intent.putExtra("commentCount", record.getCommentNum() + "");
                startActivityForResult(intent,2);
                break;
            //点赞
            case R.id.msg_line_prasie:
                if (record.getPraise() == 0) {
                    praise.setImageResource(R.drawable.ic_praise_select);
                    if (record.getPraiseNum() < 0) {
                        rbPraise.setText("赞 1");
                    }else{
                        rbPraise.setText("赞 "+(record.getPraiseNum()+1));
                    }
                    prasieCount = record.getPraiseNum()+1;
                    ServerUtils.prasieMessage(messageId, 1, messagePraise);

                } else {
                    praise.setImageResource(R.drawable.ic_praise_normal);
                    if (record.getPraiseNum() > 0) {
                        rbPraise.setText("赞 "+(record.getPraiseNum() - 1) +"");
                    } else{
                        rbPraise.setText("赞 0");
                    }
                    if (record.getPraiseNum() == 0) {
                        prasieCount = 0;
                    }else{
                        prasieCount = record.getPraiseNum()-1;
                    }
                    ServerUtils.prasieMessage(messageId, 2, messagePraise);
//                    Toast.makeText(MessageActivity.this,"已经赞过了！",Toast.LENGTH_SHORT).show();
                }
                break;
            //更多
            case R.id.msg_more_act:
                showPopupWindow(more);
                break;

            case R.id.fab_btn:
                //已读加分
                if (record.getRead()==0){
                    ServerUtils.msgRead(record.getMessageId()+"",msgReadCallBack);
                }
                break;
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.msg_rb_save:
                rbSave.setTextColor(Color.BLACK);
                rbComment.setTextColor(Color.GRAY);
                rbPraise.setTextColor(Color.GRAY);
                viewPager.setCurrentItem(0);
                break;
            case R.id.msg_rb_comment:
                rbSave.setTextColor(Color.GRAY);
                rbComment.setTextColor(Color.BLACK);
                rbPraise.setTextColor(Color.GRAY);
                viewPager.setCurrentItem(1);
                break;
            case R.id.msg_rb_praise:
                rbSave.setTextColor(Color.GRAY);
                rbComment.setTextColor(Color.GRAY);
                rbPraise.setTextColor(Color.BLACK);
                viewPager.setCurrentItem(2);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getIntent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if (data == null) {
            return;
        }
        boolean change = data.getBooleanExtra("change",false);
        if (change){
            switch (requestCode) {
                case 2:
                    int commentCount = data.getIntExtra("commentNum",0);
                    rbComment.setText("评论 " + commentCount);
                    record.setCommentNum(commentCount);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

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
        if (record != null){
            Intent intent = new Intent();
            intent.putExtra("msg", record);

            setResult(101, intent);
        }

        finish();
    }

    protected void imageBrower(int position, List<Pictures> images) {
        Intent intent = new Intent(MessageActivity.this, ImagePagerActivity.class);
        ArrayList<String> urls = new ArrayList<>();
        for (Pictures i : images) {
            urls.add(i.getUrl());
        }
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        startActivity(intent);
    }

    private void showPopupWindow(View view) {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_window, null);
        // 设置按钮的点击事件
        LinearLayout delete = (LinearLayout) contentView.findViewById(R.id.more_delete);
        LinearLayout error = (LinearLayout) contentView.findViewById(R.id.more_error);
        LinearLayout call = (LinearLayout) contentView.findViewById(R.id.more_call);
        LinearLayout share = (LinearLayout) contentView.findViewById(R.id.more_share);

        if (SPUtil.getString(MessageActivity.this, App.USER_ID).equals(record.getAuthor().getId()+"")) {
            delete.setVisibility(View.VISIBLE);
            error.setVisibility(View.GONE);
        }else {
            delete.setVisibility(View.GONE);
            error.setVisibility(View.VISIBLE);
        }

        final PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setTouchable(true);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果


        View.OnClickListener popListen = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    //删除
                    case R.id.more_delete:
                        popupWindow.dismiss();
                        deleteDialog();
                        break;
                    //举报
                    case R.id.more_error:
                        popupWindow.dismiss();
                        errorDialog();
                        break;
                    //联系此人
                    case R.id.more_call:
                        popupWindow.dismiss();
                        callDialog();
                        break;
                    //联系此人
                    case R.id.more_share:
                        if (record.getForwardMessage() != null) {
                            if (record.getForwardMessage().getPublicScope() == 10) {
                                ServerUtils.getShareUrl(record.getMessageId()+"",shareUrl);
                            }else {
                                Utils.showToast(MessageActivity.this,"因权限限制，无法转发本动态。");
                            }
                        }else {
                            if (record.getPublicScope() == 10) {
                                ServerUtils.getShareUrl(record.getMessageId()+"",shareUrl);
                            }else {
                                Utils.showToast(MessageActivity.this,"因权限限制，无法转发本动态。");
                            }
                        }
                        popupWindow.dismiss();
                        break;
                }
            }
        };
        delete.setOnClickListener(popListen);
        error.setOnClickListener(popListen);
        call.setOnClickListener(popListen);
        share.setOnClickListener(popListen);
        popupWindow.showAsDropDown(view);
    }

    private void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("此动态已被删除！");
        builder.setTitle("张承政务");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.putExtra("delete",messageId);
                setResult(2001,intent);
                finish();
            }
        });
        builder.create().show();
    }

    private void deleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("是否删除本动态？");
        builder.setTitle("删除动态提醒：");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ServerUtils.deleteMessage(record.getMessageId(), messageDelete);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    private void callDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("联系人：" + author.getName() + "\n联系电话：" + author.getPhone());
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

    //阅读加分接口回调
    private RequestCallBack<String> msgReadCallBack= new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            DemoApiJSON demoApiJSON = JsonUtils.jsonToPojo(json, DemoApiJSON.class);

            if (Integer.valueOf(demoApiJSON.getData().toString()).intValue()==1){
                readBtn.setText("已读");
                Toast.makeText(MessageActivity.this,"阅读成功",Toast.LENGTH_SHORT).show();
                record.setRead(1);
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };

    private RequestCallBack<String> messageDelete = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            DemoApiJSON demoApiJSON = JsonUtils.jsonToPojo(json, DemoApiJSON.class);
            if(demoApiJSON.getMsg().equals("OK")){
                Toast.makeText(MessageActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("delete",record.getMessageId());
                setResult(2001,intent);
                finish();
            }else{
                WinToast.toast(MessageActivity.this, demoApiJSON.getMsg());
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };

    private RequestCallBack<String> messageCollect = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            DemoApiJSON demoApiJSON = JsonUtils.jsonToPojo(json, DemoApiJSON.class);
            int old = record.getCollectNum();
            initViewPager();
            if(old>collectCount){
                record.setCollect(0);
                Toast.makeText(MessageActivity.this,"取消收藏",Toast.LENGTH_SHORT).show();
            }else{
                record.setCollect(1);
                Toast.makeText(MessageActivity.this,"收藏成功",Toast.LENGTH_SHORT).show();
            }
            if (collectCount < 0) {
                record.setCollectNum(0);
            }else{
                record.setCollectNum(collectCount);
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
            WinToast.toast(MessageActivity.this, "收藏失败");
        }
    };

    private RequestCallBack<String> messagePraise = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            DemoApiJSON demoApiJSON = JsonUtils.jsonToPojo(json, DemoApiJSON.class);
            int old = record.getPraiseNum();
            initViewPager();
            if(old>prasieCount){
                record.setPraise(0);
                Toast.makeText(MessageActivity.this,"取消点赞",Toast.LENGTH_SHORT).show();
            }else{
                record.setPraise(1);
                Toast.makeText(MessageActivity.this,"点赞成功",Toast.LENGTH_SHORT).show();
            }
            if (prasieCount <= 0) {
                record.setPraiseNum(0);
            }else{
                record.setPraiseNum(prasieCount);
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
            WinToast.toast(MessageActivity.this, e.getMessage());

        }
    };

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            rbSave.setChecked(true);
        }else if(position == 1){
            rbComment.setChecked(true);
        }else {
            rbPraise.setChecked(true);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public static boolean isYesterday(long timestamp) {
        Calendar c = Calendar.getInstance();
        clearCalendar(c, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND);
        c.add(Calendar.DAY_OF_MONTH, -1);
        long firstOfDay = c.getTimeInMillis(); // 昨天最早时间

        c.setTimeInMillis(timestamp);
        clearCalendar(c, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND); // 指定时间戳当天最早时间

        return firstOfDay == c.getTimeInMillis();
    }
    public static boolean isToday(long timestamp) {
        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);


        Calendar cal = Calendar.getInstance();
        Date date = new Date(timestamp);
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == 0) {
                return true;
            }
        }
        return false;
    }

    private static void clearCalendar(Calendar c, int... fields) {
        for (int f : fields) {
            c.set(f, 0);
        }
    }


    RequestCallBack<String> shareUrl = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            Bean bean = JsonUtils.jsonToPojo(json, Bean.class);
            if(bean.getMsg().equals("OK") && !TextUtils.isEmpty(bean.getData())){
                url = bean.getData();
                showSharePop(record);
            }else{
                Utils.showToast(MessageActivity.this,"原动态已被删除");
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
            WinToast.toast(MessageActivity.this, e.getMessage());

        }
    };

    private void showSharePop(final Record record) {
        shareView = LayoutInflater.from(MessageActivity.this).inflate(R.layout.pop_share, null);

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
                        Intent intent = new Intent(MessageActivity.this, MsgPowerActivity.class);
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
                            Utils.showToast(MessageActivity.this,"您没有安装微信");
                            return ;
                        }
                        shareWeChat(record);
                        sharepop.dismiss();
                        break;
                    //朋友圈
                    case R.id.ll_circle_share_pop:
                        if(!api.isWXAppInstalled()){
                            Utils.showToast(MessageActivity.this,"您没有安装微信");
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
        sharepop.showAtLocation(this.findViewById(R.id.msg_activity), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
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
        oks.show(this);
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

}
