package org.bigdata.zczw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.bigdata.zczw.R;
import org.bigdata.zczw.entity.AttendanceHistory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChecksAdapter extends BaseAdapter {
    private String[] checkType = new String[]{"","未签到","在岗","上路","出差","休假","调休","请假","其他"};
    private String[] leaveType = new String[]{"事假","病假","年休假","婚假","预产假","产假","陪产假","哺乳假","延长哺乳假","丧假","工伤"};
    private Context context;
    private ArrayList<AttendanceHistory> arrayList;
    private long create;

    public ChecksAdapter(Context context, ArrayList<AttendanceHistory> arrayList,long create) {
        this.context = context;
        this.arrayList = arrayList;
        this.create = create;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.checks_item,null);

            viewHolder.type = (TextView) convertView.findViewById(R.id.txt_type_pop_check);
            viewHolder.location = (TextView) convertView.findViewById(R.id.txt_location_pop_check);

            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();

        AttendanceHistory attendanceHistory = arrayList.get(position);
        Date date ;
        if (position == arrayList.size()-1) {
            date = new Date(create);
        }else {
            date = new Date(arrayList.get(position+1).getCreateDate());
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        if (attendanceHistory.getFromAttendType() == 6) {
            viewHolder.type.setText("请假："+leaveType[attendanceHistory.getFromAttendSubType()-1] + "   " +simpleDateFormat.format(date));
        }else if (attendanceHistory.getFromAttendType() == 7) {
            viewHolder.type.setText("其他："+ attendanceHistory.getFromOtherTypeName() + "   " +simpleDateFormat.format(date));
        }else {
            viewHolder.type.setText(checkType[attendanceHistory.getFromAttendType()+1] + "   " +simpleDateFormat.format(date));
        }

        viewHolder.location.setText(attendanceHistory.getLocationName());

        return convertView;
    }

    private class ViewHolder{
        private TextView type,location;
    }
}
