package org.bigdata.zczw.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.bigdata.zczw.App;
import org.bigdata.zczw.R;
import org.bigdata.zczw.activity.PersonalActivity;
import org.bigdata.zczw.activity.UserInfoActivity;
import org.bigdata.zczw.entity.Author;
import org.bigdata.zczw.entity.BoxMsg;
import org.bigdata.zczw.entity.Record;
import org.bigdata.zczw.utils.SPUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by SVN on 2018/3/5.
 */

public class AllNoticeAdapter extends BaseAdapter {

    private ArrayList<BoxMsg> arrayList;
    private Context context;

    public AllNoticeAdapter(ArrayList<BoxMsg> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_all_notice,null);

            viewHolder.header = (ImageView) convertView.findViewById(R.id.img_user_por_all_notice_item);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.img_record_all_notice_item);
            viewHolder.name1 = (TextView) convertView.findViewById(R.id.txt_user_name_all_notice_item);
            viewHolder.name2 = (TextView) convertView.findViewById(R.id.txt_name_all_notice_item);
            viewHolder.info = (TextView) convertView.findViewById(R.id.txt_info_all_notice_item);
            viewHolder.time = (TextView) convertView.findViewById(R.id.txt_time_all_notice_item);
            viewHolder.content = (TextView) convertView.findViewById(R.id.txt_content_all_notice_item);
            viewHolder.record = (TextView) convertView.findViewById(R.id.txt_record_all_notice_item);

            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();

        final BoxMsg boxMsg = arrayList.get(position);

        if (TextUtils.isEmpty(boxMsg.getCreator().getPortrait())) {
            viewHolder.header.setImageResource(R.drawable.de_default_portrait);
        }else {
            Picasso.with(context).load(boxMsg.getCreator().getPortrait()).resize(200,200).into(viewHolder.header);
        }
        viewHolder.header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String authorId =boxMsg.getCreator().getId()+"";
                if(authorId.equals(SPUtil.getString(context, App.USER_ID))){
                    context.startActivity(new Intent(context, UserInfoActivity.class));
                }else{
                    Intent authorIntent = new Intent(context, PersonalActivity.class);
                    authorIntent.putExtra("PERSONAL",authorId);
                    context.startActivity(authorIntent);
                }
            }
        });

        viewHolder.name1.setText(boxMsg.getCreator().getName());
        Record record = boxMsg.getMessage();
        if (record.getPictures()!=null && record.getPictures().size()>0) {
            viewHolder.imageView.setVisibility(View.VISIBLE);
            viewHolder.record.setVisibility(View.GONE);
            Picasso.with(context).load(record.getPictures().get(0).getUrl()).resize(200,200).into(viewHolder.imageView);
        }else {
            viewHolder.imageView.setVisibility(View.GONE);
            viewHolder.record.setVisibility(View.VISIBLE);
            viewHolder.record.setText(record.getContent());
        }

        long old = boxMsg.getCreateDate();
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
                String dealtTime = sdf.format(new Date(old));
                viewHolder.time.setText(dealtTime);
            }else if (isYesterday(old)) {
                SimpleDateFormat sdf = new SimpleDateFormat("昨天 HH:mm");
                String dealtTime = sdf.format(new Date(old));
                viewHolder.time.setText(dealtTime);
            }else {
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
                String dealtTime = sdf.format(new Date(old));
                viewHolder.time.setText(dealtTime);
            }
        }


        if (boxMsg.getPraiseId() == 0) {//评论
            viewHolder.content.setVisibility(View.VISIBLE);

            ArrayList<String> numList = new ArrayList<>();
            if (!TextUtils.isEmpty(boxMsg.getCommentAtRange())) {
                String rangeStr = boxMsg.getCommentAtRange();
                String s[]=rangeStr.split("/");
                for (int i = 0; i < s.length; i++) {
                    String num[]=s[i].split("-");
                    numList.add(num[0]);
                    numList.add(num[1]);
                }
                SpannableStringBuilder builder = new SpannableStringBuilder(boxMsg.getCommentContent());
                //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
                int n = 0;
                while (n<numList.size()){

                    int a = Integer.parseInt(numList.get(n));
                    int b = Integer.parseInt(numList.get(n+1));
                    System.out.println("内容长度--"+boxMsg.getCommentContent().length()+"a长度--"+a+"b长度"+b);
                    builder.setSpan(new ForegroundColorSpan(Color.parseColor("#507daf")), a, b, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    n = n+2;
                }

                viewHolder.content.setText(builder);
            }else {
                if (!TextUtils.isEmpty(boxMsg.getCommentContent())) {
                    viewHolder.content.setText(boxMsg.getCommentContent());
                }
            }

            if (boxMsg.getParentCommentCreator()!=null) {
                Author author = boxMsg.getParentCommentCreator();
                if ( SPUtil.getString(context, App.USER_ID).equals(author.getId()+"")) {
                    viewHolder.info.setText("回复了我");
                    viewHolder.name2.setVisibility(View.GONE);
                }else{
                    viewHolder.name2.setVisibility(View.VISIBLE);
                    viewHolder.info.setText("回复了");
                    viewHolder.name2.setText(author.getName());
                }
            }else {
                viewHolder.info.setText("评论了这条动态");
                viewHolder.name2.setVisibility(View.GONE);
            }
        }else {
            viewHolder.content.setVisibility(View.GONE);
            viewHolder.name2.setVisibility(View.GONE);
            viewHolder.info.setText("赞了这条动态");
        }

        return convertView;
    }

    private class ViewHolder{
        private ImageView header,imageView;
        private TextView name1,name2,info,time,content,record;
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
