package org.bigdata.zczw.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.bigdata.zczw.R;
import org.bigdata.zczw.entity.Attendance;
import org.bigdata.zczw.entity.AttendanceHistory;
import org.bigdata.zczw.entity.CheckNote;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CheckDateAdapter extends BaseAdapter {

    private String[] checkType = new String[]{"","未签","在岗","上路","出差","休假","调休","请假","其他"};
    private String[] leaveType = new String[]{"事假","病假","年休假","婚假","预产假","产假","陪产假","哺乳假","延长哺乳假","丧假","工伤"};
    private String[] nightType = new String[]{"","中班","白班","夜班","夜/中"};

    private Context context;
    private Attendance attendance;
    private ArrayList<Object> arrayList;

    public CheckDateAdapter(Context context, Attendance attendance, ArrayList<Object> arrayList) {
        this.context = context;
        this.attendance = attendance;
        this.arrayList = arrayList;
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
        ViewHolder1 viewHolder1 = null;
        ViewHolder2 viewHolder2 = null;
        System.out.println(position);
        if (arrayList.get(position) instanceof CheckNote) {
            viewHolder1 = new ViewHolder1();
            convertView = LayoutInflater.from(context).inflate(R.layout.check_note_item,null);
            viewHolder1.note = (TextView) convertView.findViewById(R.id.txt_check_note);

            CheckNote checkNote = (CheckNote) arrayList.get(position);
            Date date = new Date(checkNote.getCreateDate());
            String time = new SimpleDateFormat("HH:mm").format(date);
            viewHolder1.note.setText("["+time+"] "+checkNote.getRemark());
        }else{
            viewHolder2 = new ViewHolder2();
            convertView = LayoutInflater.from(context).inflate(R.layout.checks_item,null);

            viewHolder2.type = (TextView) convertView.findViewById(R.id.txt_type_pop_check);
            viewHolder2.location = (TextView) convertView.findViewById(R.id.txt_location_pop_check);

            if (arrayList.get(position) instanceof Attendance) {
                Date date = new Date(attendance.getUpdateDate());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                if (attendance.getAttendType() == 6) {
                    viewHolder2.type.setText("请假："+leaveType[attendance.getAttendSubType()-1] + "   " +simpleDateFormat.format(date));
                }else if (attendance.getAttendType() == 7) {
                    viewHolder2.type.setText("其他："+ attendance.getOtherTypeName() + "   " +simpleDateFormat.format(date));
                }else if(attendance.getAttendType() == 1){
                    if (attendance.getAttendSubType()>0){
                        viewHolder2.type.setText(nightType[attendance.getAttendSubType()] + "   " +simpleDateFormat.format(date));
                    }else {
                        viewHolder2.type.setText(checkType[attendance.getAttendType()+1] + "   " +simpleDateFormat.format(date));
                    }

                }else {
                    viewHolder2.type.setText(checkType[attendance.getAttendType()+1] + "   " +simpleDateFormat.format(date));
                }
                if (TextUtils.isEmpty(attendance.getLocationName())) {
                    viewHolder2.location.setText("未定位");
                }else {
                    viewHolder2.location.setText(attendance.getLocationName());
                }
            }else {
                AttendanceHistory attendanceHistory = (AttendanceHistory) arrayList.get(position);
                Date date = new Date(attendanceHistory.getDate());

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                if (attendanceHistory.getFromAttendType() == 6) {
                    viewHolder2.type.setText("请假："+leaveType[attendanceHistory.getFromAttendSubType()-1] + "   " +simpleDateFormat.format(date));
                }else if (attendanceHistory.getFromAttendType() == 7) {
                    viewHolder2.type.setText("其他："+ attendanceHistory.getFromOtherTypeName() + "   " +simpleDateFormat.format(date));
                }else if(attendanceHistory.getFromAttendType() == 1){
                    if (attendanceHistory.getFromAttendSubType()>0){
                        viewHolder2.type.setText(nightType[attendanceHistory.getFromAttendSubType()] + "   " +simpleDateFormat.format(date));
                    }else {
                        viewHolder2.type.setText(checkType[attendanceHistory.getFromAttendType()+1] + "   " +simpleDateFormat.format(date));
                    }

                }else {
                    viewHolder2.type.setText(checkType[attendanceHistory.getFromAttendType()+1] + "   " +simpleDateFormat.format(date));
                }
//                viewHolder2.location.setText(attendanceHistory.getLocationName());
                if (TextUtils.isEmpty(attendanceHistory.getLocationName())) {
                    viewHolder2.location.setText("未定位");
                }else {
                    viewHolder2.location.setText(attendanceHistory.getLocationName());
                }
            }
        }


        return convertView;
    }

    private class ViewHolder1{
        private TextView note;
    }

    private class ViewHolder2{
        private TextView type,location;
    }
}
