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
import org.bigdata.zczw.entity.Zan;
import org.bigdata.zczw.utils.DateUtils;
import org.bigdata.zczw.utils.SPUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by darg on 2017/4/20.
 */

public class TrendAdapter extends BaseAdapter {

    private Context context;
    private List<Zan> recordList;
    private int type;

    public TrendAdapter(Context context, List<Zan> recordList ,int type){
        this.context = context;
        this.recordList = recordList;
        this.type = type;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_trend,null);
            viewHolder.hostName = (TextView) convertView.findViewById(R.id.txt_host_name_trend_item);
            viewHolder.name = (TextView) convertView.findViewById(R.id.txt_name_trend_item);
            viewHolder.time = (TextView) convertView.findViewById(R.id.txt_time_trend_item);
            viewHolder.comment = (TextView) convertView.findViewById(R.id.comment_content_trend_item);
            viewHolder.content = (TextView) convertView.findViewById(R.id.content_trend_item);

            viewHolder.header = (ImageView) convertView.findViewById(R.id.img_head_trend_item);
            viewHolder.hostHeader = (ImageView) convertView.findViewById(R.id.img_host_head_trend_item);


            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();

        final String id = SPUtil.getString(context, App.USER_ID);
        final Zan zan = recordList.get(position);

        if (!TextUtils.isEmpty(zan.getUser().getPortrait())) {
            String url = zan.getUser().getPortrait();
            Picasso.with(context).load(url).into(viewHolder.header);
        }
        viewHolder.header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id.equals(zan.getUser().getId()+"")) {
                    context.startActivity(new Intent(context, UserInfoActivity.class));
                }else {
                    Intent authorIntent = new Intent(context, PersonalActivity.class);
                    authorIntent.putExtra("PERSONAL",zan.getUser().getId()+"");
                    context.startActivity(authorIntent);
                }
            }
        });
        viewHolder.hostHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id.equals(zan.getAuthor().getId()+"")) {
                    context.startActivity(new Intent(context, UserInfoActivity.class));
                }else {
                    Intent authorIntent = new Intent(context, PersonalActivity.class);
                    authorIntent.putExtra("PERSONAL",zan.getAuthor().getId()+"");
                    context.startActivity(authorIntent);
                }
            }
        });

        if (!TextUtils.isEmpty(zan.getAuthor().getPortrait())) {
            String img = zan.getAuthor().getPortrait();
            Picasso.with(context).load(img).into(viewHolder.hostHeader);
        }
        if (!TextUtils.isEmpty(zan.getAuthor().getUnitsName())) {
            viewHolder.hostName.setText(zan.getAuthor().getName()+"-"+zan.getAuthor().getUnitsName());
        }else {
            viewHolder.hostName.setText(zan.getAuthor().getName());
        }
        if (!TextUtils.isEmpty(zan.getUser().getUnitsName())) {
            viewHolder.name.setText(zan.getUser().getName()+"-"+zan.getUser().getUnitsName());
        }else {
            viewHolder.name.setText(zan.getUser().getName());
        }

        viewHolder.time.setText(DateUtils.convertToDate(zan.getCreatetime()));

        ArrayList<String> numList = new ArrayList<>();
        if (!TextUtils.isEmpty(zan.getRangeStr())) {
            String rangeStr = zan.getRangeStr();
            String s[]=rangeStr.split("/");
            for (int i = 0; i < s.length; i++) {
                String num[]=s[i].split("-");
                numList.add(num[0]);
                numList.add(num[1]);
            }
            SpannableStringBuilder builder = new SpannableStringBuilder(zan.getCommentsContent());
            //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
            int n = 0;
            while (n<numList.size()){
                int a = Integer.parseInt(numList.get(n));
                int b = Integer.parseInt(numList.get(n+1));
                builder.setSpan(new ForegroundColorSpan(Color.BLUE), a, b, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                n = n+2;
            }

            viewHolder.comment.setText(builder);
        }else {
            if (!TextUtils.isEmpty(zan.getCommentsContent())) {
                viewHolder.comment.setText(zan.getCommentsContent());
            }
        }

        viewHolder.content.setText(zan.getMessage().getContent());

        return convertView;
    }

    private class ViewHolder{
        private TextView hostName,name,time,comment,content;
        private ImageView header,hostHeader;
    }
}
