package org.bigdata.zczw.SendGroupMsg;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.squareup.picasso.Picasso;

import org.bigdata.zczw.R;
import org.bigdata.zczw.activity.UserSelectActivity;
import org.bigdata.zczw.entity.Friend;

import java.util.ArrayList;
import java.util.List;


/**
 * 群发消息列表适配器
 */
public class SendGroupAdapter extends SendGroupListAdapter {

    private static final String TAG = SendGroupAdapter.class.getSimpleName();
    private ArrayList<Friend> mFriends;

    public SendGroupAdapter(Context context, List<Friend> friends) {
        super(context, friends);
        this.mFriends = (ArrayList<Friend>) friends;
    }

    @Override
    protected void bindView(View v, int partition, List<Friend> data, int position) {
        super.bindView(v, partition, data, position);
        try {
            ViewHolder holder = (ViewHolder) v.getTag();

            Friend friend = data.get(position);
            holder.group.setVisibility(View.GONE);
            holder.name.setText(friend.getNickname());
            holder.phone.setText(friend.getPhone());
            holder.unreadnum.setText(friend.getGroupname());
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.userId = friend.getUserId();
            if (!TextUtils.isEmpty(friend.getPortrait()) && holder.photo!=null) {
                Picasso.with(getContext()).load(friend.getPortrait()).into(holder.photo);
            }else {
                holder.photo.setImageResource(R.drawable.de_default_portrait);
            }
            holder.checkBox.setChecked(friend.isSelected());
            if (friend.isSelected()) {
                UserSelectActivity.IS_CHECK = true;
            }
        } catch (OutOfMemoryError e) {
            // 这里就是当内存泄露时 需要做的事情
            e.printStackTrace();

            Log.e("1111", "out");

        }

    }



    @Override
    protected void newSetTag(View view, ViewHolder holder, int position, List<Friend> data) {
        super.newSetTag(view, holder, position, data);
    }

    @Override
    public void onItemClick(String friendId) {


    }


}
