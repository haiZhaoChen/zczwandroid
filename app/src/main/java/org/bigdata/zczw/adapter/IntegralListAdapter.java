package org.bigdata.zczw.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.bigdata.zczw.R;
import org.bigdata.zczw.entity.IntegralListModel;

import java.util.ArrayList;

public class IntegralListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<IntegralListModel> list;


    public IntegralListAdapter(Context context,ArrayList<IntegralListModel> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list.size()>3){
            return list.size()-2;
        }else if(list.size()==0){
            return 0;
        }else {
            return 1;
        }

    }

    public String getLastId() {
        String lastId = null;
        if (this.list != null && this.list.size() > 0) {
            lastId = this.list.get(this.list.size() - 1).getId() + "";
        }
        return lastId;
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
        ViewHolder1 viewHolder1 = null;
        ViewHolder2 viewHolder2 = null;
        View convertView1 = null;
        View convertView2 = null;

        if (position==0){

            if (convertView1 ==null){

                convertView1 = LayoutInflater.from(context).inflate(R.layout.item_integral_list_top,null);
                viewHolder1 = new ViewHolder1();
                viewHolder1.iconImg1 = (ImageView)convertView1.findViewById(R.id.top_icon_img1);
                viewHolder1.iconImg2 = (ImageView)convertView1.findViewById(R.id.top_icon_img2);
                viewHolder1.iconImg3 = (ImageView)convertView1.findViewById(R.id.top_icon_img3);
                viewHolder1.scoreLB1 = (TextView)convertView1.findViewById(R.id.integral_score1);
                viewHolder1.scoreLB2 = (TextView)convertView1.findViewById(R.id.integral_score2);
                viewHolder1.scoreLB3 = (TextView)convertView1.findViewById(R.id.integral_score3);
                viewHolder1.companyLB1 = (TextView)convertView1.findViewById(R.id.integral_unit1);
                viewHolder1.companyLB2 = (TextView)convertView1.findViewById(R.id.integral_unit2);
                viewHolder1.companyLB3 = (TextView)convertView1.findViewById(R.id.integral_unit3);
                viewHolder1.nameLB1 = (TextView)convertView1.findViewById(R.id.integral_name1);
                viewHolder1.nameLB2 = (TextView)convertView1.findViewById(R.id.integral_name2);
                viewHolder1.nameLB3 = (TextView)convertView1.findViewById(R.id.integral_name3);

                convertView1.setTag(viewHolder1);

            }
            viewHolder1 = (ViewHolder1) convertView1.getTag();
            for (int i=0;i<list.size();i++){
                //调试闪退bug,控制前三名数据
                if (i>=3) break;
                IntegralListModel model = list.get(i);
                if (i==0){
                    viewHolder1.scoreLB1.setText(model.getPublicIntegral()+"");
                    viewHolder1.companyLB1.setText(model.getUnitsName());
                    viewHolder1.nameLB1.setText(model.getName());
                    if (!TextUtils.isEmpty(model.getPortrait())) {
                        Picasso.with(context).load(model.getPortrait()).into(viewHolder1.iconImg1);
                    }else {
                        viewHolder1.iconImg1.setImageResource(R.drawable.de_default_portrait);
                    }
                }else if (i==1){
                    viewHolder1.scoreLB2.setText(model.getPublicIntegral()+"");
                    viewHolder1.companyLB2.setText(model.getUnitsName());
                    viewHolder1.nameLB2.setText(model.getName());
                    if (!TextUtils.isEmpty(model.getPortrait())) {
                        Picasso.with(context).load(model.getPortrait()).into(viewHolder1.iconImg2);
                    }else {
                        viewHolder1.iconImg2.setImageResource(R.drawable.de_default_portrait);
                    }
                }else if (i==2){
                    viewHolder1.scoreLB3.setText(model.getPublicIntegral()+"");
                    viewHolder1.companyLB3.setText(model.getUnitsName());
                    viewHolder1.nameLB3.setText(model.getName());
                    if (!TextUtils.isEmpty(model.getPortrait())) {
                        Picasso.with(context).load(model.getPortrait()).into(viewHolder1.iconImg3);
                    }else {
                        viewHolder1.iconImg3.setImageResource(R.drawable.de_default_portrait);
                    }
                }

            }

            convertView = convertView1;

        }else {
            if (convertView2 == null){

                convertView2 = LayoutInflater.from(context).inflate(R.layout.item_integral_list,null);
                viewHolder2 = new ViewHolder2();
                viewHolder2.iconImg = (ImageView)convertView2.findViewById(R.id.integral_icon_img);
                viewHolder2.indexLB =  (TextView)convertView2.findViewById(R.id.integral_index_num);
                viewHolder2.scoreLB = (TextView)convertView2.findViewById(R.id.integral_score);
                viewHolder2.nameLB = (TextView)convertView2.findViewById(R.id.integral_name);
                viewHolder2.companyLB = (TextView)convertView2.findViewById(R.id.integral_company);
                convertView2.setTag(viewHolder2);

            }
            viewHolder2 = (ViewHolder2) convertView2.getTag();
            IntegralListModel model = (IntegralListModel)this.list.get(position+2);

            viewHolder2.scoreLB.setText(model.getPublicIntegral()+"");
            viewHolder2.indexLB.setText(model.getId()+"");
            viewHolder2.nameLB.setText(model.getName());
            viewHolder2.companyLB.setText(model.getUnitsName());
            if (!TextUtils.isEmpty(model.getPortrait())) {
                Picasso.with(context).load(model.getPortrait()).into(viewHolder2.iconImg);
            }else {
                viewHolder2.iconImg.setImageResource(R.drawable.de_default_portrait);
            }

            convertView = convertView2;
        }


        return convertView;
    }

    private class ViewHolder1{
        private ImageView iconImg1;
        private ImageView iconImg2;
        private ImageView iconImg3;

        private TextView scoreLB1;
        private TextView scoreLB2;
        private TextView scoreLB3;

        private TextView companyLB1;
        private TextView companyLB2;
        private TextView companyLB3;

        private TextView nameLB1;
        private TextView nameLB2;
        private TextView nameLB3;

    }
    private class ViewHolder2{
        private ImageView iconImg;

        private TextView scoreLB;

        private TextView companyLB;

        private TextView indexLB;

        private TextView nameLB;

    }

}
