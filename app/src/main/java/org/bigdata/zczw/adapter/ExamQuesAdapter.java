package org.bigdata.zczw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.bigdata.zczw.R;
import org.bigdata.zczw.entity.ExamPreModel;

import java.util.ArrayList;

public class ExamQuesAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ExamPreModel> list;


    public ExamQuesAdapter(Context context,ArrayList<ExamPreModel> list){
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
        ExamQuesAdapter.ViewHolder viewHolder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_exampro,null);
            viewHolder = new ExamQuesAdapter.ViewHolder();
            viewHolder.goText = (TextView)convertView.findViewById(R.id.exam_go_name);
            viewHolder.iconImg = (ImageView)convertView.findViewById(R.id.icon_img_bg);
            viewHolder.proSize = (TextView)convertView.findViewById(R.id.exam_total_pro);
            viewHolder.titleName = (TextView)convertView.findViewById(R.id.exam_title_type);
            viewHolder.linearLayout = (LinearLayout)convertView.findViewById(R.id.exam_img_bg);

            convertView.setTag(viewHolder);
        }

        viewHolder = (ExamQuesAdapter.ViewHolder) convertView.getTag();
        ExamPreModel model = this.list.get(position);
        viewHolder.titleName.setText(model.getTitle());
        if (model.getExamType()<10) {
            viewHolder.iconImg.setImageResource(R.drawable.exam_pro_list1);

            viewHolder.linearLayout.setBackground(convertView.getResources().getDrawable(R.drawable.solid_radius_exam_green));
        } else if (model.getExamType()<100) {

            viewHolder.iconImg.setImageResource(R.drawable.exam_pro_list2);
//            viewHolder.linearLayout.setBackgroundResource(R.drawable.solid_radius_exam_orange);
            viewHolder.linearLayout.setBackground(convertView.getResources().getDrawable(R.drawable.solid_radius_exam_orange));
        } else if (model.getExamType()<1000) {
            viewHolder.iconImg.setImageResource(R.drawable.exam_pro_list3);
//            viewHolder.linearLayout.setBackgroundResource(R.drawable.solid_radius_exam_bule);
            viewHolder.linearLayout.setBackground(convertView.getResources().getDrawable(R.drawable.solid_radius_exam_bule));
        } else {
            viewHolder.iconImg.setImageResource(R.drawable.exam_pro_list4);
//            viewHolder.linearLayout.setBackgroundResource(R.drawable.solid_radius_exam_gray);
            viewHolder.linearLayout.setBackground(convertView.getResources().getDrawable(R.drawable.solid_radius_exam_gray));

        }

        viewHolder.proSize.setText("" + model.getQuestionCount());

        if (model.isMeJoin()){
            viewHolder.goText.setText("看成绩");
        }else {
            if (model.getExamType()>=1000){
                viewHolder.goText.setText("去练习");
            }else{
                viewHolder.goText.setText("去考试");
            }

        }


        return convertView;
    }

    private class ViewHolder{
        private ImageView iconImg;

        private LinearLayout linearLayout;
        private TextView titleName;
        private TextView proSize;
        private TextView goText;

    }


}
