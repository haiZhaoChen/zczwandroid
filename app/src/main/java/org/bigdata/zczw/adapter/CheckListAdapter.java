package org.bigdata.zczw.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.bigdata.zczw.R;
import org.bigdata.zczw.entity.AttendTypeCount;
import org.bigdata.zczw.entity.Author;
import org.bigdata.zczw.entity.Check;

import java.util.ArrayList;

/**
 * Created by darg on 2017/4/20.
 */

public class CheckListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Check> checkList;
    private String[] checkType = new String[]{"","未签到","在岗","上路","出差","休假","调休","请假","其他"};
    private String[] leaveType = new String[]{"事假","病假","年休假","婚假","预产假","产假","陪产假","哺乳假","延长哺乳假","丧假","工伤"};

    public CheckListAdapter(Context context, ArrayList<Check> checkList){
        this.context = context;
        this.checkList = checkList;
    }

    @Override
    public int getCount() {
        if (checkList != null) {
            return checkList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return checkList.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.check_list_item,null);

            viewHolder.txtZaiGang = (TextView) convertView.findViewById(R.id.txt_zaigang_list_item);
            viewHolder.txtShangLu = (TextView) convertView.findViewById(R.id.txt_shanglu_list_item);
            viewHolder.txtChuChai = (TextView) convertView.findViewById(R.id.txt_chuchai_list_item);
            viewHolder.txtXiuJia = (TextView) convertView.findViewById(R.id.txt_xiujia_list_item);
            viewHolder.txtTiaoXiu = (TextView) convertView.findViewById(R.id.txt_tiaoxiu_list_item);
            viewHolder.txtQingJia = (TextView) convertView.findViewById(R.id.txt_qingjia_list_item);
            viewHolder.txtQiTa = (TextView) convertView.findViewById(R.id.txt_qita_list_item);

            viewHolder.name = (TextView) convertView.findViewById(R.id.txt_user_name_check_item);
            viewHolder.job = (TextView) convertView.findViewById(R.id.txt_job_check_item);
            viewHolder.type = (TextView) convertView.findViewById(R.id.txt_type_check_item);

            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.img_user_check_item);

            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();

        Check check = checkList.get(position);
        Author user = check.getUser();
        if (!TextUtils.isEmpty(user.getPortrait())) {
            Picasso.with(context).load(user.getPortrait()).into(viewHolder.imageView);
        }else {
            viewHolder.imageView.setImageResource(R.drawable.de_default_portrait);
        }
        viewHolder.name.setText(user.getName());

        viewHolder.job.setText(user.getUnitsName()+ "."+user.getJobsName());
        if (check.getTodayAttend()!=null) {
            viewHolder.type.setText(checkType[check.getTodayAttend().getAttendType()+1]);
        }else {
            viewHolder.type.setText("未签到");
        }

        ArrayList<AttendTypeCount> countList = (ArrayList<AttendTypeCount>) check.getAttendTypeCountList();
        viewHolder.txtZaiGang.setText(Html.fromHtml(countList.get(0).getCount()+"<font ><small><small>天</small></small></font>"));
        viewHolder.txtShangLu.setText(Html.fromHtml(countList.get(1).getCount()+"<font ><small><small>天</small></small></font>"));
        viewHolder.txtChuChai.setText(Html.fromHtml(countList.get(2).getCount()+"<font ><small><small>天</small></small></font>"));
        viewHolder.txtXiuJia.setText(Html.fromHtml(countList.get(3).getCount()+"<font ><small><small>天</small></small></font>"));
        viewHolder.txtTiaoXiu.setText(Html.fromHtml(countList.get(4).getCount()+"<font ><small><small>天</small></small></font>"));
        viewHolder.txtQingJia.setText(Html.fromHtml(countList.get(5).getCount()+"<font ><small><small>天</small></small></font>"));
        viewHolder.txtQiTa.setText(Html.fromHtml(countList.get(6).getCount()+"<font ><small><small>天</small></small></font>"));
        return convertView;
    }

    private class ViewHolder{
        private ImageView imageView;
        private TextView name,job,type;
        private TextView txtZaiGang,txtShangLu,txtChuChai,txtXiuJia,txtTiaoXiu,txtQingJia,txtQiTa;

    }
}
