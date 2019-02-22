package org.bigdata.zczw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.bigdata.zczw.R;
import org.bigdata.zczw.entity.Question;
import org.bigdata.zczw.entity.Test;
import org.bigdata.zczw.utils.DateUtils;

import java.util.List;

/**
 * Created by darg on 2017/4/20.
 */

public class QuestionAdapter extends BaseAdapter{
    private Context context;
    private List<Question> questionList;

    public QuestionAdapter(Context context,List<Question> questionList){
        this.context = context;
        this.questionList = questionList;
    }

    @Override
    public int getCount() {
        return questionList.size();
    }

    @Override
    public Object getItem(int position) {
        return questionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_exam_list,null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.exam_list_name);
            viewHolder.time = (TextView) convertView.findViewById(R.id.exam_list_time);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.name.setText(questionList.get(position).getTitle());

        viewHolder.time.setText(DateUtils.convertToDate2(questionList.get(position).getCreatetime()));

        return convertView;
    }

    private class ViewHolder{
        private TextView name;
        private TextView time;
    }
}
