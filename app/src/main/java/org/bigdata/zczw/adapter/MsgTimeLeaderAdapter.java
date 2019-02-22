package org.bigdata.zczw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.bigdata.zczw.R;

import java.util.ArrayList;

public class MsgTimeLeaderAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> list;


    public MsgTimeLeaderAdapter(Context context,ArrayList<String> list){
        this.context = context;
        this.list = list;
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
        ViewHolder viewHolder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_exampro,null);
            viewHolder = new ViewHolder();
            viewHolder.titleName = (TextView)convertView.findViewById(R.id.exam_go_name);

            convertView.setTag(viewHolder);
        }

        viewHolder = (ViewHolder) convertView.getTag();





        return convertView;
    }

    private class ViewHolder{

        private TextView titleName;


    }

}
