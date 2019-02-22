package org.bigdata.zczw.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.bigdata.zczw.App;
import org.bigdata.zczw.R;
import org.bigdata.zczw.entity.Pictures;
import org.bigdata.zczw.entity.Record;
import org.bigdata.zczw.entity.Video;
import org.bigdata.zczw.utils.NoUnderlineSpan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SVN on 2018/3/13.
 */

public class TopicAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Record> arrayList;

    public TopicAdapter(Context context, ArrayList<Record> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_topic,null);
            viewHolder.content = (TextView) convertView.findViewById(R.id.txt_content_topic_item);
            viewHolder.name = (TextView) convertView.findViewById(R.id.txt_name_topic_item);
            viewHolder.read = (TextView) convertView.findViewById(R.id.txt_read_topic_item);
            viewHolder.length = (TextView) convertView.findViewById(R.id.record_txt_video_len);
            viewHolder.time = (TextView) convertView.findViewById(R.id.txt_time_topic_item);

            viewHolder.header = (ImageView) convertView.findViewById(R.id.img_header_topic_item);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.img_topic_item);
            viewHolder.play = (ImageView) convertView.findViewById(R.id.img_video_play_topic_item);
            viewHolder.linearLayout = (LinearLayout) convertView.findViewById(R.id.ll_topic_item);
            viewHolder.relativeLayout = (RelativeLayout) convertView.findViewById(R.id.rl_topic);


            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();

        final Record record = arrayList.get(position);

        viewHolder.content.setText(record.getContent());

        viewHolder.content.setAutoLinkMask(0);
        Linkify.addLinks(viewHolder.content,Linkify.WEB_URLS);

        Pattern trendsPattern = Pattern.compile("#(\\w+?)#");
        String trendsScheme = String.format("%s/?%s=", App.TOPIC_SCHEMA, App.PARAM_UID);
        Log.e("1111", "getView: "+trendsScheme);
        Linkify.addLinks(viewHolder.content, trendsPattern, trendsScheme, null, new
                Linkify.TransformFilter() {
                    @Override
                    public String transformUrl(Matcher match, String url) {
                        return match.group(1);
                    }
                });
        removeHyperLinkUnderline(viewHolder.content);

        if (!TextUtils.isEmpty(record.getAuthor().getUnitsName())) {
            viewHolder.name.setText(record.getAuthor().getName()+"-"+record.getAuthor().getUnitsName());
        }else {
            viewHolder.name.setText(record.getAuthor().getName());
        }

        viewHolder.read.setText(record.getViewNum()+"人看过");

        if(!TextUtils.isEmpty(record.getAuthor().getPortrait())) {
            Picasso.with(context).load(record.getAuthor().getPortrait()).into(viewHolder.header);
        }else{
            viewHolder.header.setImageResource(R.drawable.de_default_portrait);
        }

        final List<Pictures> picturesList = record.getPictures();//图片列表
        if (picturesList == null || picturesList.size() == 0) { // 没有图片资源就隐藏GridView
            viewHolder.relativeLayout.setVisibility(View.GONE);
            viewHolder.length.setVisibility(View.GONE);
            viewHolder.play.setVisibility(View.GONE);
        } else {
            viewHolder.relativeLayout.setVisibility(View.VISIBLE);
            viewHolder.length.setVisibility(View.GONE);
            viewHolder.play.setVisibility(View.GONE);
            Picasso.with(context).load(picturesList.get(0).getUrl()).into(viewHolder.imageView);
        }

        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFeedClickListener.onFeedClick(position);

            }
        });


        viewHolder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFeedClickListener.onFeedClick(position);
            }
        });


        final Video recordVideo = record.getVideo();
        if (recordVideo != null) {
            viewHolder.relativeLayout.setVisibility(View.VISIBLE);
            viewHolder.length.setVisibility(View.VISIBLE);
            viewHolder.play.setVisibility(View.VISIBLE);
            viewHolder.play.setImageResource(android.R.drawable.ic_media_play);
            Picasso.with(context).load(recordVideo.getThumbnail()).into(viewHolder.imageView);

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
            viewHolder.length.setText(len);
            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String videoPath = recordVideo.getPath();
                    Uri uri = Uri.parse(videoPath);
                    Intent intentPlayer = new Intent(Intent.ACTION_VIEW);
                    intentPlayer.setDataAndType(uri, "video/mp4");
                    context.startActivity(intentPlayer);
                }
            });
        }

        String time = record.getPublishedTime();

        long old = Long.parseLong(time);
        Date date = new Date();
        long now = date.getTime();
        long t = now-old;
        if (t < 60*1000*5) {
            viewHolder.time.setText("刚刚");
        }else if (t <60*1000*60) {
            int s= (int) (t/60/1000);
            viewHolder.time.setText(s+"分钟前");
        }else {
            if (isToday(old)) {
                SimpleDateFormat sdf = new SimpleDateFormat("今天 HH:mm");
                String dealtTime = sdf.format(new Date(Long.parseLong(time)));
                viewHolder.time.setText(dealtTime);
            }else if (isYesterday(old)) {
                SimpleDateFormat sdf = new SimpleDateFormat("昨天 HH:mm");
                String dealtTime = sdf.format(new Date(Long.parseLong(time)));
                viewHolder.time.setText(dealtTime);
            }else {
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
                String dealtTime = sdf.format(new Date(Long.parseLong(time)));
                viewHolder.time.setText(dealtTime);
            }
        }


        return convertView;
    }

    private onFeedClickListener onFeedClickListener;
    public void setOnFeedClickListener(onFeedClickListener onFeedClickListener) {
        this.onFeedClickListener = onFeedClickListener;
    }

    public interface onFeedClickListener {
        void onFeedClick(int position );
    }

    private class ViewHolder{
        private TextView content,name,read,time,length;
        private ImageView header,imageView,play;
        private LinearLayout linearLayout;
        private RelativeLayout relativeLayout;
    }

    private void removeHyperLinkUnderline(TextView tv) {

        CharSequence text = tv.getText();

        if(text instanceof Spannable){
            Spannable spannable = (Spannable) tv.getText();

            NoUnderlineSpan noUnderlineSpan = new NoUnderlineSpan();

            spannable.setSpan(noUnderlineSpan,0,text.length(), Spanned.SPAN_MARK_MARK);

        }

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
}
