package org.bigdata.zczw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.bigdata.zczw.R;

import java.util.ArrayList;

public class ExamAnsweredPageAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Boolean> dataList;

    public ExamAnsweredPageAdapter(Context context,ArrayList<Boolean> dataList){
        this.context = context;
        this.dataList = dataList;
    }



    @Override
    public int getCount() {
        if (dataList != null){
            return dataList.size();
        }else {
            return 0;
        }

    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_answered_page,null);
            viewHolder.img = (ImageView)convertView.findViewById( R.id.item_answered_bgimg);
            viewHolder.num = (TextView)convertView.findViewById(R.id.item_answered_bgNum);
            convertView.setTag(viewHolder);
        }

        viewHolder = (ViewHolder) convertView.getTag();
        Boolean isRight = dataList.get(position);
        if (isRight){
            viewHolder.img.setImageResource(R.drawable.exam_qiu_selection);
        }else {
            viewHolder.img.setImageResource(R.drawable.exam_qiu_unchecked);
        }

        viewHolder.num.setText(position+1+"");

        return convertView;
    }

    private class ViewHolder{
        private ImageView img;
        private TextView num;
    }
}
