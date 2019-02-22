package org.bigdata.zczw.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.bigdata.zczw.R;
import org.bigdata.zczw.entity.Friend;

import java.util.ArrayList;
import java.util.List;

public class DeAddressMultiChoiceAdapter extends DeAddressListAdapter {

    private static final String TAG = DeAddressMultiChoiceAdapter.class.getSimpleName();
    private ArrayList<Friend> mFriends;


    public DeAddressMultiChoiceAdapter(Context context, List<Friend> friends) {
        super(context, friends);
        this.mFriends = (ArrayList<Friend>) friends;
    }

    @Override
    protected void bindView(View v, int partition, List<Friend> data, int position) {
        super.bindView(v, partition, data, position);

        ViewHolder holder = (ViewHolder) v.getTag();
        TextView name = holder.name;
        TextView phone = holder.phone;
        ImageView photo = holder.photo;
        RelativeLayout rl = holder.rl;
        TextView group = holder.group;
        TextView job = holder.unreadnum;

        Friend friend = data.get(position);
        name.setText(friend.getNickname());
        phone.setText(friend.getPhone());
        rl.setVisibility(View.VISIBLE);
        job.setVisibility(View.VISIBLE);
        group.setVisibility(View.GONE);
// TODO: 2016/5/27 0027 设置好友头像

        if(friend.getUserId().equals("★002")){
            photo.setImageResource(R.drawable.de_address_group);
            rl.setVisibility(View.GONE);
            job.setVisibility(View.GONE);
            group.setVisibility(View.VISIBLE);
        }
        if(friend.getUserId().equals("★003")){
            photo.setImageResource(R.drawable.de_tag_default_portrait);
        }
        String userId = friend.getUserId();
        holder.userId = userId;

    }

    @Override
    protected void newSetTag(View view, ViewHolder holder, int position, List<Friend> data) {
        super.newSetTag(view, holder, position, data);
    }

    @Override
    public void onItemClick(String friendId) {


    }


}
