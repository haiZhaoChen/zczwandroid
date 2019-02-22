package org.bigdata.zczw.SendGroupMsg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import org.bigdata.zczw.R;
import org.bigdata.zczw.entity.Friend;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by darg on 2016/9/21.
 */
public class SendGroupMsgAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Friend> friends;

    public SendGroupMsgAdapter(Context context, List<Friend> friends){
        this.context = context;
        this.friends = (ArrayList<Friend>) friends;
    }

    @Override
    public int getCount() {
        return friends.size();
    }

    @Override
    public Object getItem(int position) {
        return friends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder myHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.de_item_sendgrouplist, null);
            myHolder = new ViewHolder();
            myHolder.checkBox = (CheckBox) convertView.findViewById(R.id.de_checkbox);
            myHolder.photo = (ImageView) convertView.findViewById(R.id.de_ui_friend_icon);
            myHolder.name = (TextView) convertView.findViewById(R.id.de_ui_friend_name);
            convertView.setTag(myHolder);
        } else {
            myHolder = (ViewHolder) convertView.getTag();
        }
        Friend friend = friends.get(position);

        return convertView;
    }

    private class ViewHolder {
        private TextView name;
        private ImageView photo;

        private String userId;
        private CheckBox checkBox;
    }
}
