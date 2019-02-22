package org.bigdata.zczw.adapter;

import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.bigdata.zczw.R;
import org.bigdata.zczw.entity.Record;
import org.bigdata.zczw.entity.Zan;
import org.bigdata.zczw.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by darg on 2017/4/20.
 */

public class DetailAdapter extends BaseAdapter {

    private Context context;
    private List<Record> recordList;
    private String date;

    public DetailAdapter(Context context,List<Record> recordList ){
        this.context = context;
        this.recordList = recordList;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_detail,null);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time_detail_item);
            viewHolder.day = (TextView) convertView.findViewById(R.id.day_detail_item);
            viewHolder.month = (TextView) convertView.findViewById(R.id.month_detail_item);
            viewHolder.content = (TextView) convertView.findViewById(R.id.content_detail_item);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image_detail_item);
            viewHolder.view = convertView.findViewById(R.id.bar_detail_item);
            viewHolder.linearLayout = (LinearLayout) convertView.findViewById(R.id.ll_time);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();

        Record record = recordList.get(position);
        String time = record.getPublishedTime();
        long old = Long.parseLong(time);

        if (isToday(old)) {
            viewHolder.time.setVisibility(View.VISIBLE);
            viewHolder.day.setVisibility(View.GONE);
            viewHolder.month.setVisibility(View.GONE);
            viewHolder.time.setText("今天");
        }else if (isYesterday(old)) {
            viewHolder.time.setVisibility(View.VISIBLE);
            viewHolder.day.setVisibility(View.GONE);
            viewHolder.month.setVisibility(View.GONE);
            viewHolder.time.setText("昨天");
        }else {
            viewHolder.time.setVisibility(View.GONE);
            viewHolder.day.setVisibility(View.VISIBLE);
            viewHolder.month.setVisibility(View.VISIBLE);
            SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
            SimpleDateFormat sdfMon = new SimpleDateFormat("M月");
            String day = sdfDay.format(new Date(Long.parseLong(time)));
            String mon = sdfMon.format(new Date(Long.parseLong(time)));
            viewHolder.day.setText(day);
            viewHolder.month.setText(mon);
        }

        viewHolder.linearLayout.setVisibility(View.INVISIBLE);

        if (record.isShow()) {
            viewHolder.linearLayout.setVisibility(View.VISIBLE);
            viewHolder.view.setVisibility(View.VISIBLE);
        }else {
            viewHolder.linearLayout.setVisibility(View.INVISIBLE);
            viewHolder.view.setVisibility(View.GONE);
        }

//        if (TextUtils.isEmpty(date)) {
//            viewHolder.linearLayout.setVisibility(View.VISIBLE);
//            viewHolder.view.setVisibility(View.VISIBLE);
//            date = time;
//        }else {
//            SimpleDateFormat sdf = new SimpleDateFormat("MM_dd");
//            String newS = sdf.format(new Date(Long.parseLong(time)));
//            String oldS = sdf.format(new Date(Long.parseLong(date)));
//            date = time;
//            if (newS.equals(oldS)) {
//                viewHolder.linearLayout.setVisibility(View.INVISIBLE);
//                viewHolder.view.setVisibility(View.GONE);
//            }else {
//                viewHolder.linearLayout.setVisibility(View.VISIBLE);
//                viewHolder.view.setVisibility(View.VISIBLE);
//            }
//        }

        viewHolder.content.setText(record.getContent());

        if (record.getPictures() != null && record.getPictures().size()>0) {
            viewHolder.imageView.setVisibility(View.VISIBLE);
            Picasso.with(context).load(record.getPictures().get(0).getUrl()).resize(100,100).into(viewHolder.imageView);
        }else{
            viewHolder.imageView.setVisibility(View.GONE);
        }


        return convertView;
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

    private class ViewHolder{
        private TextView day,month,content,time;
        private ImageView imageView;
        private View view;
        private LinearLayout linearLayout;
    }
}
