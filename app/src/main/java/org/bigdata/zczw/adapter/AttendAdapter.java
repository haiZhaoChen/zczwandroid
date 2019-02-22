package org.bigdata.zczw.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.bigdata.zczw.R;
import org.bigdata.zczw.entity.Attendance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by darg on 2017/4/20.
 * 考勤类型。
 * -1:时间未到
 * 0：未签到
 * 1：在岗
 * 2：上路
 * 3：出差
 * 4：休假
 * 5: 调休
 * 6：请假
 * 7：其他
 */

public class AttendAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Attendance> attendanceList;
    private String[] checkType = new String[]{"","未签","在岗","上路","出差","休假","调休","请假","其他"};
    private String[] nightType = new String[]{"","中班","白班","夜班","夜/中"};
    private int day,month;

    public AttendAdapter(Context context, ArrayList<Attendance> attendanceList,int month){
        this.context = context;
        this.attendanceList = attendanceList;
        Calendar calendar = Calendar.getInstance();
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        this.month = month;
    }

    @Override
    public int getCount() {
        if (attendanceList != null) {
            return attendanceList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return attendanceList.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_check_date,null);
            viewHolder.day = (TextView) convertView.findViewById(R.id.txt_day_check_item);
            viewHolder.type = (TextView) convertView.findViewById(R.id.txt_type_check_item);
            viewHolder.back = (TextView) convertView.findViewById(R.id.back_check_item);

            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();

        Attendance attendance = attendanceList.get(position);
        Date date = new Date(attendance.getAttendDate());

        SimpleDateFormat spfDay = new SimpleDateFormat("d");
        String strDay = spfDay.format(date);
        SimpleDateFormat spfMonth = new SimpleDateFormat("M");
        String strMonth = spfMonth.format(date);

        viewHolder.day.setText(strDay);

        if (strDay.equals(day+"") && strMonth.equals(month+"") && month == (Calendar.getInstance().get(Calendar.MONTH)+1)) {
            viewHolder.back.setVisibility(View.VISIBLE);
            viewHolder.day.setTextColor(Color.parseColor("#ffffff"));
            viewHolder.type.setTextColor(Color.parseColor("#ffffff"));
        }else {
            viewHolder.back.setVisibility(View.GONE);
            if (strMonth.equals(month+"")) {
                if (attendance.getAttendType() == 4 || attendance.getAttendType() == 5) {
                    viewHolder.day.setTextColor(Color.parseColor("#595959"));
                    viewHolder.type.setTextColor(Color.parseColor("#e6731c"));
                }else if (attendance.getAttendType() ==0 ) {
                    viewHolder.day.setTextColor(Color.parseColor("#595959"));
                    viewHolder.type.setTextColor(Color.parseColor("#b6b6b6"));
                }else {
                    viewHolder.day.setTextColor(Color.parseColor("#595959"));
                    viewHolder.type.setTextColor(Color.parseColor("#0092fa"));
                }
            }else {
                viewHolder.day.setTextColor(Color.parseColor("#aba7af"));
                viewHolder.type.setTextColor(Color.parseColor("#d1d1d1"));
            }
        }
//        if (attendance.getAttendType() == 1 && attendance.getAttendSubType() != 0) {
//            viewHolder.type.setText(nightType[attendance.getAttendSubType()-1]);
//        }else {
//            viewHolder.type.setText(checkType[attendance.getAttendType()+1]);
//        }

        if (attendance.getAttendType() == 1){
            if (attendance.getAttendSubType()>0){
                viewHolder.type.setText(nightType[attendance.getAttendSubType()]);
            }else {
                viewHolder.type.setText(checkType[attendance.getAttendType()+1]);
            }

        }else {
            viewHolder.type.setText(checkType[attendance.getAttendType()+1]);
        }


        return convertView;
    }

    private class ViewHolder{
        private TextView day,type,back;
    }
}
