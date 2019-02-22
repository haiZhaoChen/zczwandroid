package org.bigdata.zczw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.bigdata.zczw.R;
import org.bigdata.zczw.entity.IntegralInfoModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class IntegralScoreListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<IntegralInfoModel> list;


    public IntegralScoreListAdapter(Context context,ArrayList<IntegralInfoModel> list){
        this.context = context;
        this.list = list;
    }

    public String getScoreListLastId() {
        String messageId = null;
        if (this.list != null && this.list.size() > 0) {
            messageId = this.list.get(this.list.size() - 1).getId() + "";
        }
        return messageId;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PageViewHolder pageViewHolder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_integral_info_list,null);
            pageViewHolder = new PageViewHolder();
            pageViewHolder.score = (TextView)convertView.findViewById(R.id.integral_info_score);
            pageViewHolder.timeStr = (TextView)convertView.findViewById(R.id.integral_info_time);
            pageViewHolder.titleName = (TextView)convertView.findViewById(R.id.integral_info_title);

            convertView.setTag(pageViewHolder);
        }

        pageViewHolder = (PageViewHolder) convertView.getTag();
        IntegralInfoModel model = this.list.get(position);
        String score = model.getValue()>0?"+"+model.getValue():model.getValue()+"";

        pageViewHolder.score.setText(score);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        pageViewHolder.timeStr.setText(sdf.format(model.getCreateDate()));
        pageViewHolder.titleName.setText(model.getType());



        return convertView;
    }

    private class PageViewHolder{
        private TextView titleName;
        private TextView timeStr;
        private TextView score;

    }

}
