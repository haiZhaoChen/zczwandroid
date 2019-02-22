package org.bigdata.zczw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.bigdata.zczw.R;
import org.bigdata.zczw.entity.CheckNote;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by darg on 2017/4/20.
 */

public class PopNoteAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<CheckNote> checkNoteList;

    public PopNoteAdapter(Context context, ArrayList<CheckNote> checkNoteList){
        this.context = context;
        this.checkNoteList = checkNoteList;
    }

    @Override
    public int getCount() {
        if (checkNoteList != null) {
            return checkNoteList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return checkNoteList.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.check_note_item,null);
            viewHolder.note = (TextView) convertView.findViewById(R.id.txt_check_note);

            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        Date date = new Date(checkNoteList.get(position).getCreateDate());
        String time = new SimpleDateFormat("HH:mm").format(date);
        viewHolder.note.setText("["+time+"] "+checkNoteList.get(position).getRemark());

        return convertView;
    }

    private class ViewHolder{
        private TextView note;
    }
}
