package org.bigdata.zczw.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.bigdata.zczw.R;
import org.bigdata.zczw.activity.UserSelectActivity;
import org.bigdata.zczw.entity.Friend;

import java.util.ArrayList;

public class SelectAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Friend> mFriends;

    public SelectAdapter(Context context, ArrayList<Friend> mFriends) {
        this.context = context;
        this.mFriends = mFriends;
    }

    @Override
    public int getCount() {
        return mFriends.size();
    }

    @Override
    public Object getItem(int position) {
        return mFriends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.de_item_sendgrouplist, null);
            viewHolder = new ViewHolder();
            viewHolder.photo = (ImageView) convertView.findViewById(R.id.de_ui_friend_icon);

            viewHolder.name = (TextView) convertView.findViewById(R.id.de_ui_friend_name);
            viewHolder.phone = (TextView) convertView.findViewById(R.id.txt_ui_friend_phone);
            viewHolder.job = (TextView) convertView.findViewById(R.id.de_unread_num);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.de_checkbox);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Friend friend = mFriends.get(position);
        viewHolder.name.setText(friend.getNickname());
        viewHolder.phone.setText(friend.getPhone());
        viewHolder.job.setText(friend.getGroupname());

        viewHolder.checkBox.setChecked(friend.isSelected());
        if (friend.isSelected()) {
            UserSelectActivity.IS_CHECK = true;
        }

        if (!TextUtils.isEmpty(friend.getPortrait()) ) {
            Picasso.with(context).load(friend.getPortrait()).into(viewHolder.photo);
        }else {
            viewHolder.photo.setImageResource(R.drawable.de_default_portrait);
        }


        return convertView;
    }

    private class ViewHolder{
        private TextView name,job,phone;
        private CheckBox checkBox;
        private ImageView photo;
    }
}
