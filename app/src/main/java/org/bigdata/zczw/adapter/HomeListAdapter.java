package org.bigdata.zczw.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.util.Linkify;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.bigdata.zczw.App;
import org.bigdata.zczw.R;
import org.bigdata.zczw.activity.FileActivity;
import org.bigdata.zczw.activity.ImagePagerActivity;
import org.bigdata.zczw.entity.Author;
import org.bigdata.zczw.entity.FileBean;
import org.bigdata.zczw.entity.Pictures;
import org.bigdata.zczw.entity.Record;
import org.bigdata.zczw.entity.Video;
import org.bigdata.zczw.ui.AlignTextView;
import org.bigdata.zczw.ui.NoScrollGridView;
import org.bigdata.zczw.ui.NoScrollListView;
import org.bigdata.zczw.utils.NoUnderlineSpan;
import org.bigdata.zczw.utils.SPUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by darg on 2016/8/9.
 */
public class HomeListAdapter extends BaseAdapter{

    private Context context;
    private List<Record> recordList ;
    private String type;
    private LayoutInflater mInflater;
    private onCheckBarClickListener onCheckBarClickListener;

    public HomeListAdapter(Context context, List<Record> recordList ,String type){
        this.context = context;
        this.recordList = recordList;
        this.type = type;
        mInflater = LayoutInflater.from(context);
    }


        @Override
    public int getCount() {
        if (recordList != null) {
            return recordList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return recordList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_feed,null);
            viewHolder.userName = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.location = (TextView) convertView.findViewById(R.id.tv_location);
            viewHolder.messageContent = (TextView) convertView.findViewById(R.id.message_content);
            viewHolder.publishTime = (TextView) convertView.findViewById(R.id.tv_publish_time);
            viewHolder.userProfile = (ImageView) convertView.findViewById(R.id.iv_userProfile);
            viewHolder.video = (ImageView) convertView.findViewById(R.id.video_img);
            viewHolder.videoAuto = (ImageView) convertView.findViewById(R.id.auto_video_img);
            viewHolder.imagesGridview = (NoScrollGridView) convertView.findViewById(R.id.img_gridview);
            viewHolder.btn_comment = (LinearLayout) convertView.findViewById(R.id.line_comment);
            viewHolder.btn_prasie = (LinearLayout) convertView.findViewById(R.id.line_prasie);
            viewHolder.llShare = (LinearLayout) convertView.findViewById(R.id.ll_record_share);
            viewHolder.videoContent = (RelativeLayout) convertView.findViewById(R.id.video_content);
            viewHolder.relativeInfo = (RelativeLayout) convertView.findViewById(R.id.relative_name);
            viewHolder.videoSize = (TextView) convertView.findViewById(R.id.txt_video_size);
            viewHolder.videoLen = (TextView) convertView.findViewById(R.id.txt_video_len);
            viewHolder.readNum = (TextView) convertView.findViewById(R.id.tv_read_num);
            viewHolder.top = (TextView) convertView.findViewById(R.id.txt_first_room_item);
            viewHolder.more = (ImageView) convertView.findViewById(R.id.more);
            viewHolder.type = (ImageView) convertView.findViewById(R.id.img_feed_type);

            viewHolder.praise = (ImageView) convertView.findViewById(R.id.img_praise);

            viewHolder.collectNum = (TextView) convertView.findViewById(R.id.txt_collectNum);
            viewHolder.praiseNum = (TextView) convertView.findViewById(R.id.txt_praiseNum);
            viewHolder.commentNum = (TextView) convertView.findViewById(R.id.txt_commentNum);
            viewHolder.save = (ImageView) convertView.findViewById(R.id.check_save);

            viewHolder.txtRecord = (AlignTextView) convertView.findViewById(R.id.record_message_content);

            viewHolder.videoSizeRecord = (TextView) convertView.findViewById(R.id.record_txt_video_size);
            viewHolder.videoLenRecord = (TextView) convertView.findViewById(R.id.record_txt_video_len);
            viewHolder.videoRecord = (ImageView) convertView.findViewById(R.id.record_video_img);
            viewHolder.videoAutoRecord = (ImageView) convertView.findViewById(R.id.record_auto_video_img);

            viewHolder.recordGridView = (NoScrollGridView) convertView.findViewById(R.id.record_img_gridview);
            viewHolder.flowLayout = (TagFlowLayout) convertView.findViewById(R.id.id_flow_tag);

            viewHolder.listView = (NoScrollListView) convertView.findViewById(R.id.list_word_file_feed);

            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            int screenWidth = dm.widthPixels;//屏幕宽度
            LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) viewHolder.messageContent.getLayoutParams(); //取控件textView当前的布局参数
            linearParams.width = screenWidth-40;// 控件的宽强制设成30
            viewHolder.messageContent.setLayoutParams(linearParams); //使设置好的布局参数应用到控件


            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();

        final Record record = recordList.get(position);

        if (record.getFiles()!=null && record.getFiles().size()>0) {
            viewHolder.listView.setVisibility(View.VISIBLE);
            FileListAdapter fileListAdapter = new FileListAdapter(context, (ArrayList<FileBean>) record.getFiles());
            viewHolder.listView.setAdapter(fileListAdapter);
        }else {
            viewHolder.listView.setVisibility(View.GONE);
        }

        viewHolder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent1 = new Intent(context,FileActivity.class);
                intent1.putExtra("file",record.getFiles().get(position));
                intent1.putExtra("id",record.getMessageId());
                context.startActivity(intent1);
            }
        });

        if (record.getTags()!=null && record.getTags().size()>0) {
            viewHolder.flowLayout.setVisibility(View.VISIBLE);
            String [] mVals = new String[record.getTags().size()];
            for (int i = 0; i < record.getTags().size(); i++) {
                mVals[i] = record.getTags().get(i).getName();
            }

            final ViewHolder finalViewHolder3 = viewHolder;
            viewHolder.flowLayout.setAdapter(new TagAdapter<String>(mVals) {

                @Override
                public View getView(FlowLayout flowLayout, int i, String s) {
                    TextView tv = (TextView) mInflater.inflate(R.layout.show_tag_text, finalViewHolder3.flowLayout, false);
                    tv.setText(s);
                    return tv;
                }
            });
        }else {
            viewHolder.flowLayout.setVisibility(View.GONE);
        }




        if (record.getPriority() == 100) {
            viewHolder.top.setVisibility(View.VISIBLE);
        }else {
            viewHolder.top.setVisibility(View.GONE);
        }

        viewHolder.relativeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCheckBarClickListener != null) {
                    onCheckBarClickListener.onCommentsClick("7", record);//交给前面的fragment处理
                }
            }
        });

        viewHolder.messageContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCheckBarClickListener != null) {
                    onCheckBarClickListener.onCommentsClick("7", record);//交给前面的fragment处理
                }
            }
        });

        if (record.getForwardMessage()!=null) {
            final Record shareRecord = record.getForwardMessage();
            viewHolder.llShare.setVisibility(View.VISIBLE);

            viewHolder.txtRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onCheckBarClickListener != null) {
                        onCheckBarClickListener.onCommentsClick("8", record);//交给前面的fragment处理
                    }
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
            viewHolder.txtRecord.setText(builder);

            final List<Pictures> recordPicturesList = shareRecord.getPictures();//图片列表
            if (recordPicturesList == null || recordPicturesList.size() == 0) { // 没有图片资源就隐藏GridView
                viewHolder.recordGridView.setVisibility(View.GONE);
            } else {
                viewHolder.recordGridView.setVisibility(View.VISIBLE);
                viewHolder.recordGridView.setAdapter(new NoScrollGridAdapter(context, recordPicturesList));
            }
            // 点击回帖九宫格，查看大图
            viewHolder.recordGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    imageBrower(position, recordPicturesList);
                }
            });

            final Video recordVideo = shareRecord.getVideo();
            if (recordVideo != null) {
                viewHolder.videoContentRecord.setVisibility(View.VISIBLE);
                viewHolder.videoAutoRecord.setImageResource(android.R.drawable.ic_media_play);
                Picasso.with(context).load(recordVideo.getThumbnail()).into(viewHolder.videoRecord);
                String size = recordVideo.getSize()+"Mb";
                viewHolder.videoSizeRecord.setText(size);
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
                viewHolder.videoLenRecord.setText(len);
                viewHolder.videoRecord.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String videoPath = recordVideo.getPath();
                        Uri uri = Uri.parse(videoPath);
                        Intent intentPlayer = new Intent(Intent.ACTION_VIEW);
                        intentPlayer.setDataAndType(uri, "video/mp4");
                        context.startActivity(intentPlayer);
                    }
                });
            }else{
                viewHolder.videoContent.setVisibility(View.GONE);
            }




        }else {
            viewHolder.llShare.setVisibility(View.GONE);
        }

        switch (record.getPublicScope()){//0:未知; 1:特定; 5:部门; 10:全部;
            case 0:
                viewHolder.type.setVisibility(View.GONE);
                break;
            case 1:
                viewHolder.type.setImageResource(R.drawable.icon_feed_type_dx);
                break;
            case 5:
                viewHolder.type.setImageResource(R.drawable.icon_feed_type_bm);
                break;
            case 10:
                viewHolder.type.setImageResource(R.drawable.icon_feed_type_all);
                break;
        }

        Author author = record.getAuthor();//动态作者
        if (!TextUtils.isEmpty(author.getUnitsName())) {
            viewHolder.userName.setText(author.getName()+"-"+author.getUnitsName());
        }else {
            viewHolder.userName.setText(author.getName());
        }
        viewHolder.readNum.setText(record.getViewNum()+"阅读");


        final List<Pictures> picturesList = record.getPictures();//图片列表
        if (picturesList == null || picturesList.size() == 0) { // 没有图片资源就隐藏GridView
            viewHolder.imagesGridview.setVisibility(View.GONE);
        } else {
            viewHolder.imagesGridview.setVisibility(View.VISIBLE);
            viewHolder.imagesGridview.setAdapter(new NoScrollGridAdapter(context, picturesList));
        }
        // 点击回帖九宫格，查看大图
        viewHolder.imagesGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imageBrower(position, picturesList);
            }
        });

        final Video video = record.getVideo();
        if (video != null) {
            viewHolder.videoContent.setVisibility(View.VISIBLE);
            viewHolder.videoAuto.setImageResource(android.R.drawable.ic_media_play);
            Picasso.with(context).load(video.getThumbnail()).into(viewHolder.video);
            String size = video.getSize()+"Mb";
            viewHolder.videoSize.setText(size);
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
            viewHolder.videoLen.setText(len);
            viewHolder.video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String videoPath = video.getPath();
                    Uri uri = Uri.parse(videoPath);
                    Intent intentPlayer = new Intent(Intent.ACTION_VIEW);
                    intentPlayer.setDataAndType(uri, "video/mp4");
                    context.startActivity(intentPlayer);
                }
            });
        }else{
            viewHolder.videoContent.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(record.getLocation()) || record.getLocation().equals("未定位") || record.getLocation().equals("位置不明确")) {
            viewHolder.location.setText("未定位");
            viewHolder.location.setVisibility(View.GONE);
        } else {
            viewHolder.location.setVisibility(View.VISIBLE);
            viewHolder.location.setText(record.getLocation() + "");
        }

        String time = record.getPublishedTime();

        long old = Long.parseLong(time);
        Date date = new Date();
        long now = date.getTime();
        long t = now-old;
        if (t < 60*1000*5) {
            viewHolder.publishTime.setText("刚刚");
        }else if (t <60*1000*60) {
            int s= (int) (t/60/1000);
            viewHolder.publishTime.setText(s+"分钟前");
        }else {
            if (isToday(old)) {
                SimpleDateFormat sdf = new SimpleDateFormat("今天 HH:mm");
                String dealtTime = sdf.format(new Date(Long.parseLong(time)));
                viewHolder.publishTime.setText(dealtTime);
            }else if (isYesterday(old)) {
                SimpleDateFormat sdf = new SimpleDateFormat("昨天 HH:mm");
                String dealtTime = sdf.format(new Date(Long.parseLong(time)));
                viewHolder.publishTime.setText(dealtTime);
            }else {
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
                String dealtTime = sdf.format(new Date(Long.parseLong(time)));
                viewHolder.publishTime.setText(dealtTime);
            }
        }

        viewHolder.messageContent.setText(record.getContent());

        if (!TextUtils.isEmpty(record.getTopicRangeStr())) {
            viewHolder.messageContent.setAutoLinkMask(0);
            Linkify.addLinks(viewHolder.messageContent,Linkify.WEB_URLS);
            Pattern trendsPattern = Pattern.compile("(#[\\S][^#]{0,28}#)");
            String trendsScheme = String.format("%s/?%s=", App.TOPIC_SCHEMA, App.PARAM_UID);
            Linkify.addLinks(viewHolder.messageContent, trendsPattern, trendsScheme, null, new
                    Linkify.TransformFilter() {
                        @Override
                        public String transformUrl(Matcher match, String url) {
                            Log.d("1111", match.group(1));
                            return match.group(1);
                        }
                    });
            removeHyperLinkUnderline(viewHolder.messageContent);
        }

        viewHolder.messageContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Record recordMessage = recordList.get(position);
                if (onCheckBarClickListener != null) {
                    onCheckBarClickListener.onCommentsClick("6", recordMessage);//交给前面的fragment处理
                }

            }
        });
        if(!TextUtils.isEmpty(record.getAuthor().getPortrait())) {
            Picasso.with(context).load(record.getAuthor().getPortrait()).into(viewHolder.userProfile);
        }else{
            viewHolder.userProfile.setImageResource(R.drawable.de_default_portrait);
        }

        viewHolder.userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Record recordMessage = recordList.get(position);
                if (onCheckBarClickListener != null) {
                    onCheckBarClickListener.onCommentsClick("3", recordMessage);//交给前面的fragment处理
                }
            }
        });

        final ViewHolder finalViewHolder1 = viewHolder;
        viewHolder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Record recordMessage = recordList.get(position);
                showPopupWindow(finalViewHolder1.more, recordMessage);
            }
        });

        viewHolder.collectNum.setText(record.getCollectNum() + "");
        viewHolder.praiseNum.setText(record.getPraiseNum() + "");
        viewHolder.commentNum.setText(record.getCommentNum() + "");

        if (record.getCollect() == 1 && record.getCollectNum() !=0 ) {
            viewHolder.save.setImageResource(R.drawable.ic_heart_red);
        }else {
            viewHolder.save.setImageResource(R.drawable.ic_heart_outline_grey);
        }

        final ViewHolder finalViewHolder = viewHolder;

        viewHolder.btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Record recordMessage = recordList.get(position);
                if (onCheckBarClickListener != null) {
                    onCheckBarClickListener.onCommentsClick("0", recordMessage);//交给前面的fragment处理
                }
            }
        });

        if (record.getCollect() == 1 && record.getCollectNum() !=0 ) {
            viewHolder.save.setImageResource(R.drawable.ic_heart_red);
            viewHolder.collectNum.setTextColor(context.getResources().getColor(R.color.barcolor1));
        }else {
            viewHolder.save.setImageResource(R.drawable.ic_heart_outline_grey);
            viewHolder.collectNum.setTextColor(context.getResources().getColor(R.color.barcolor2));
        }
        if (record.getPraise() ==1 && record.getPraiseNum() !=0) {
            viewHolder.praise.setImageResource(R.drawable.ic_praise_select);
            viewHolder.praiseNum.setTextColor(context.getResources().getColor(R.color.barcolor1));
        }else {
            viewHolder.praise.setImageResource(R.drawable.ic_praise_normal);
            viewHolder.praiseNum.setTextColor(context.getResources().getColor(R.color.barcolor2));
        }

        viewHolder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Record recordMessage = recordList.get(position);
                if (record.getCollect() == 0) {
                    finalViewHolder.save.setImageResource(R.drawable.ic_heart_red);
                    finalViewHolder.collectNum.setTextColor(context.getResources().getColor(R.color.barcolor1));
                    finalViewHolder.collectNum.setText(record.getCollectNum() + 1 +"");
                    onCheckBarClickListener.onCommentsClick("1", recordMessage);
                } else {
                    finalViewHolder.save.setImageResource(R.drawable.ic_heart_outline_grey);
                    finalViewHolder.collectNum.setTextColor(context.getResources().getColor(R.color.barcolor2));
                    if (record.getCollectNum() > 0) {
                        finalViewHolder.collectNum.setText(record.getCollectNum() - 1 +"");
                    }else{
                        finalViewHolder.collectNum.setText("0");                    }

                    onCheckBarClickListener.onCommentsClick("12", recordMessage);
                }
            }
        });

        final ViewHolder finalViewHolder2 = viewHolder;
        viewHolder.btn_prasie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Record recordMessage = recordList.get(position);
                if (record.getPraise() == 0) {
//                    finalViewHolder2.btn_prasie.setClickable(false);
                    finalViewHolder.praise.setImageResource(R.drawable.ic_praise_select);
                    finalViewHolder.praiseNum.setTextColor(context.getResources().getColor(R.color.barcolor1));
                    if (record.getPraiseNum() < 0) {
                        finalViewHolder.praiseNum.setText("1");
                    }else{
                        finalViewHolder.praiseNum.setText(record.getPraiseNum() + 1 +"");
                    }
                    onCheckBarClickListener.onCommentsClick("2", recordMessage);
                } else {
                    finalViewHolder.praise.setImageResource(R.drawable.ic_praise_normal);
                    finalViewHolder.praiseNum.setTextColor(context.getResources().getColor(R.color.barcolor2));
                    if (record.getPraiseNum() > 0) {
                        finalViewHolder.praiseNum.setText(record.getPraiseNum() - 1 +"");
                    }else{
                        finalViewHolder.praiseNum.setText("0");
                    }
                    onCheckBarClickListener.onCommentsClick("22", recordMessage);
                }
            }
        });

        return convertView;
    }

    protected void imageBrower(int position, List<Pictures> images) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        ArrayList<String> urls = new ArrayList<>();
        for (Pictures i : images) {
            urls.add(i.getUrl());
        }
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        context.startActivity(intent);
    }

    public String getFirstMessageId() {
        String messageId = null;
        if (this.recordList != null && this.recordList.size() > 0) {
            messageId = recordList.get(0).getMessageId() + "";
        }
        return messageId;
    }

    public String getLastMessageId() {
        String messageId = null;
        if (this.recordList != null && this.recordList.size() > 0) {
            messageId = this.recordList.get(this.recordList.size() - 1).getMessageId() + "";
        }
        return messageId;
    }

    class ViewHolder{
        private ImageView userProfile,video,videoAuto,more;
        private RelativeLayout videoContent,relativeInfo;
        private TextView userName,publishTime,location;
        private NoScrollGridView imagesGridview;
        private TextView videoSize,videoLen;
        private TextView messageContent;
        private LinearLayout btn_comment,btn_prasie,llShare;
        private TextView commentNum,praiseNum,collectNum,readNum,top;
        private ImageView praise;
        private ImageView save,type;

        private TextView videoSizeRecord,videoLenRecord;
        private AlignTextView txtRecord;
        private NoScrollGridView recordGridView;
        private RelativeLayout videoContentRecord;
        private ImageView videoRecord,videoAutoRecord;

        private TagFlowLayout flowLayout;

        private NoScrollListView listView;

    }

    public void setOnCheckBarClickListener(onCheckBarClickListener onItemClickListener) {
        this.onCheckBarClickListener = onItemClickListener;
    }

    public interface onCheckBarClickListener {
        void onCommentsClick(String type, Record recordMessage);
    }

    /**
     * 更新评论数量
     *
     * @param messageId
     * @param commentCount
     */
    public void refreshCommentCount(Long messageId, String commentCount) {
        if (recordList != null && !recordList.isEmpty()) {
            for (int i = 0; i < recordList.size(); i++) {
                if (recordList.get(i).getMessageId()==messageId) {
                    Record record = recordList.get(i);
                    record.setCommentNum(Integer.parseInt(commentCount));
                    recordList.set(i, record);
                    this.notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    public void refreshPraiseCount(Long messageId, int num) {
        if (recordList != null && !recordList.isEmpty()) {
            for (int i = 0; i < recordList.size(); i++) {
                if (recordList.get(i).getMessageId()==messageId) {
                    Record record = recordList.get(i);
                    int old = record.getPraiseNum();
                    if(old>num){
                        record.setPraise(0);
                        Toast.makeText(context,"取消点赞",Toast.LENGTH_SHORT).show();
                    }else{
                        record.setPraise(1);
                        Toast.makeText(context,"点赞成功",Toast.LENGTH_SHORT).show();
                    }
                    if (num < 0) {
                        record.setPraiseNum(0);
                    }else{
                        record.setPraiseNum(num);
                    }
                    recordList.set(i, record);
                    this.notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    public void refreshCollectCount(Long messageId, int num) {
        if (recordList != null && !recordList.isEmpty()) {
            for (int i = 0; i < recordList.size(); i++) {
                if (recordList.get(i).getMessageId()==messageId) {
                    Record record = recordList.get(i);
                    int old = record.getCollectNum();
                    if(old>num){
                        record.setCollect(0);
                        Toast.makeText(context,"取消收藏",Toast.LENGTH_SHORT).show();
                    }else{
                        record.setCollect(1);
                        Toast.makeText(context,"收藏成功",Toast.LENGTH_SHORT).show();
                    }
                    if (num < 0) {
                        record.setCollectNum(0);
                    }else{
                        record.setCollectNum(num);
                    }
                    recordList.set(i, record);
                    this.notifyDataSetChanged();
                    break;
                }
            }
        }
    }
    public void refreshMsg(Long messageId) {
        if (recordList != null && !recordList.isEmpty()) {
            for (int i = 0; i < recordList.size(); i++) {
                if (recordList.get(i).getMessageId()==messageId) {
                    recordList.remove(i);
                    this.notifyDataSetChanged();
                    break;
                }
            }
        }
    }
    public void refreshMsg(Long messageId,Record msg) {
        if (recordList != null && !recordList.isEmpty()) {
            for (int i = 0; i < recordList.size(); i++) {
                if (recordList.get(i).getMessageId()==messageId) {
                    recordList.set(i,msg);
                    this.notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    private void showPopupWindow(View view, final Record recordMessage) {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(context).inflate(R.layout.pop_window, null);
        // 设置按钮的点击事件
        LinearLayout delete = (LinearLayout) contentView.findViewById(R.id.more_delete);
        LinearLayout error = (LinearLayout) contentView.findViewById(R.id.more_error);
        LinearLayout call = (LinearLayout) contentView.findViewById(R.id.more_call);
        LinearLayout share = (LinearLayout) contentView.findViewById(R.id.more_share);

        if (SPUtil.getString(context, App.USER_ID).equals(recordMessage.getAuthor().getId()+"")) {
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
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框

        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCheckBarClickListener != null) {
                    popupWindow.dismiss();
                    onCheckBarClickListener.onCommentsClick("41", recordMessage);//交给前面的fragment处理
                }
            }
        });
        error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCheckBarClickListener != null) {
                    popupWindow.dismiss();
                    onCheckBarClickListener.onCommentsClick("42", recordMessage);//交给前面的fragment处理
                }
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCheckBarClickListener != null) {
                    popupWindow.dismiss();
                    onCheckBarClickListener.onCommentsClick("43", recordMessage);//交给前面的fragment处理
                }
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCheckBarClickListener != null) {
                    popupWindow.dismiss();
                    onCheckBarClickListener.onCommentsClick("44", recordMessage);//交给前面的fragment处理
                }
            }
        });
        // 设置好参数之后再show
        popupWindow.showAsDropDown(view);

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

    private void removeHyperLinkUnderline(TextView tv) {

        CharSequence text = tv.getText();

        if(text instanceof Spannable){
            Spannable spannable = (Spannable) tv.getText();

            NoUnderlineSpan noUnderlineSpan = new NoUnderlineSpan();

            spannable.setSpan(noUnderlineSpan,0,text.length(), Spanned.SPAN_MARK_MARK);

        }

    }

}
