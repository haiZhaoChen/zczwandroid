package org.bigdata.zczw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.bigdata.zczw.R;
import org.bigdata.zczw.entity.Topic;

import java.util.ArrayList;

/**
 * Created by SVN on 2018/3/12.
 */

public class TopicNameAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<Topic> topicList;

    public TopicNameAdapter(Context context, ArrayList<Topic> topicList) {
        this.context = context;
        this.topicList = topicList;
    }

    @Override
    public int getCount() {
        return topicList.size();
    }

    @Override
    public Object getItem(int position) {
        return topicList.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_topic_name,null);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.txt_item_topic_name);
            viewHolder.txtNew = (TextView) convertView.findViewById(R.id.txt_new);

            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.textView.setText("#"+topicList.get(position).getName()+"#");
        if (topicList.get(position).isNewb()) {
            viewHolder.txtNew.setVisibility(View.VISIBLE);
        }else {
            viewHolder.txtNew.setVisibility(View.GONE);
        }

        return convertView;
    }

    private class ViewHolder{
        private TextView textView,txtNew;
    }
}
