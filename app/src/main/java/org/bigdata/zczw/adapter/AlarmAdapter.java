package org.bigdata.zczw.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.bigdata.zczw.R;
import org.bigdata.zczw.entity.AttendMessage;
import org.bigdata.zczw.entity.Author;
import org.bigdata.zczw.entity.Leave;
import org.bigdata.zczw.entity.TiaoXiu;
import org.bigdata.zczw.ui.CircleImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AlarmAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<AttendMessage> arrayList;
    private int sourceType;

    private String[] checkType = new String[]{"","未签到","在岗","上路","出差","休假","调休","请假","其他"};
    private String[] leaveType = new String[]{"事假","病假","年休假","婚假","预产假","产假","陪产假","哺乳假","延长哺乳假","丧假","工伤"};

    public AlarmAdapter(Context context, ArrayList<AttendMessage> arrayList, int sourceType) {
        this.context = context;
        this.arrayList = arrayList;
        this.sourceType = sourceType;
    }


    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_attend_alarm,null);

            viewHolder.header = (CircleImageView) convertView.findViewById(R.id.img_user_alarm_item);
            viewHolder.mark = (ImageView) convertView.findViewById(R.id.img_type);

            viewHolder.name = (TextView) convertView.findViewById(R.id.txt_user_name_alarm_item);
            viewHolder.job = (TextView) convertView.findViewById(R.id.txt_job_alarm_item);
            viewHolder.type = (TextView) convertView.findViewById(R.id.txt_leave_type);
            viewHolder.time = (TextView) convertView.findViewById(R.id.txt_leave_begin_time);
            viewHolder.xiaojia = (TextView) convertView.findViewById(R.id.txt_xiaojia_time);

            viewHolder.tvXiaojiaType = (TextView) convertView.findViewById(R.id.txt_leave_xiaojia);
            viewHolder.tvXiaojiaTime = (TextView) convertView.findViewById(R.id.txt_leave_begin_time_xiaojia);

            viewHolder.llLeave = (LinearLayout) convertView.findViewById(R.id.ll_leave_xiaojia);
            viewHolder.llXiaojia = (LinearLayout) convertView.findViewById(R.id.ll_xiaojia);

            viewHolder.rlJia = (RelativeLayout) convertView.findViewById(R.id.rl_type);

            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();

        AttendMessage attendMessage = arrayList.get(position);
        Author user = attendMessage.getUser();
        if (!TextUtils.isEmpty(user.getPortrait())) {
            Picasso.with(context).load(user.getPortrait()).into(viewHolder.header);
        }else {
            viewHolder.header.setImageResource(R.drawable.de_default_portrait);
        }
        viewHolder.name.setText(user.getName());

        viewHolder.job.setText(user.getUnitsName()+ "."+user.getJobsName());

        if (sourceType == 1) {
            viewHolder.llXiaojia.setVisibility(View.GONE);
            viewHolder.llLeave.setVisibility(View.GONE);
            viewHolder.rlJia.setVisibility(View.VISIBLE);


            Leave leave = attendMessage.getLeave();
            if (leave.getStatus()==0) {
                viewHolder.mark.setImageResource(R.drawable.icon_leave_on);
            }else {
                viewHolder.mark.setImageResource(R.drawable.icon_xiaojia_off);
            }

            SimpleDateFormat sdf0 = new SimpleDateFormat("MM/dd    HH:mm");
            viewHolder.xiaojia.setText(sdf0.format(attendMessage.getCreateDate()));

            SimpleDateFormat sdf = new SimpleDateFormat("起始：yyyy/MM/dd");
            viewHolder.time.setText(sdf.format(leave.getBeginDate()));
            viewHolder.type.setText("类型："+leaveType[leave.getAttendSubType()-1]+"    |    天数："+leave.getDays()+"天");
        }else if (sourceType == 3) {
            viewHolder.llXiaojia.setVisibility(View.GONE);
            viewHolder.llLeave.setVisibility(View.GONE);
            viewHolder.rlJia.setVisibility(View.VISIBLE);

            TiaoXiu tiaoXiu = attendMessage.getTiaoXiu();
            if (tiaoXiu.getStatus()==0) {
                viewHolder.mark.setImageResource(R.drawable.icon_tiaoxiu_on);
            }else {
                viewHolder.mark.setImageResource(R.drawable.icon_xiaojia_off);
            }

            SimpleDateFormat sdf0 = new SimpleDateFormat("MM/dd    HH:mm");
            viewHolder.xiaojia.setText(sdf0.format(attendMessage.getCreateDate()));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            viewHolder.type.setText("起始："+sdf.format(tiaoXiu.getBeginDate())+"    |    天数："+tiaoXiu.getDays()+"天");
            viewHolder.time.setText("加班："+String.format(sdf.format(tiaoXiu.getOvertimeBeginDate()) + "—" + sdf.format(tiaoXiu.getOvertimeEndDate())));
        }else {
            viewHolder.llXiaojia.setVisibility(View.VISIBLE);
            viewHolder.llLeave.setVisibility(View.VISIBLE);
            viewHolder.rlJia.setVisibility(View.GONE);

            SimpleDateFormat sdf0 = new SimpleDateFormat("MM/dd    HH:mm");
            viewHolder.xiaojia.setText(sdf0.format(attendMessage.getCreateDate()));
            if (attendMessage.getLeave() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("起始：yyyy/MM/dd");
                viewHolder.tvXiaojiaTime.setText(sdf.format(attendMessage.getLeave().getBeginDate()));
                viewHolder.tvXiaojiaType.setText("类型："+leaveType[attendMessage.getLeave().getAttendSubType()-1]+"    |    天数："+attendMessage.getLeave().getDays()+"天");
            }else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                viewHolder.tvXiaojiaType.setText("类型：调休    |    天数："+attendMessage.getTiaoXiu().getDays()+"天");
                viewHolder.tvXiaojiaTime.setText("起始："+sdf.format(attendMessage.getTiaoXiu().getBeginDate()));

            }
        }

        return convertView;
    }

    private class ViewHolder{

        private CircleImageView header;
        private ImageView mark;
        private TextView name,job,type,time,xiaojia,tvXiaojiaType,tvXiaojiaTime;
        private LinearLayout llLeave,llXiaojia;
        private RelativeLayout rlJia;

    }

}
