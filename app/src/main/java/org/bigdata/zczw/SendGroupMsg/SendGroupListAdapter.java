package org.bigdata.zczw.SendGroupMsg;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import org.bigdata.zczw.R;
import org.bigdata.zczw.entity.Friend;
import org.bigdata.zczw.entity.FriendSectionIndexer;
import org.bigdata.zczw.ui.DePinnedHeaderAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint("UseSparseArrays")
public class SendGroupListAdapter extends DePinnedHeaderAdapter<Friend> implements Filterable {

    private static String TAG = SendGroupListAdapter.class.getSimpleName();
    private LayoutInflater mInflater;

    public SendGroupListAdapter(Context context, List<Friend> friends) {
        super(context);
        setAdapterData(friends);

        if (context != null)
            mInflater = LayoutInflater.from(context);
    }

    public void setAdapterData(List<Friend> friends) {
        HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
        List<List<Friend>> result = new ArrayList<List<Friend>>();
        int key = 0;
        for (Friend friend : friends) {
            key = friend.getSearchKey();

            if (hashMap.containsKey(key)) {
                int position = (Integer) hashMap.get(key);
                if (position <= result.size() - 1) {
                    result.get(position).add(friend);
                }
            }
            else {
                result.add(new ArrayList<Friend>());
                int length = result.size() - 1;
                result.get(length).add(friend);
                hashMap.put(key, length);
            }
        }
        updateCollection(result);

    }

    @Override
    protected View newView(Context context, int partition, List<Friend> data, int position, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.de_item_sendgrouplist, null);
        ViewHolder holder = new ViewHolder();
        newSetTag(view, holder, position, data);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void bindView(View v, int partition, List<Friend> data, int position) {
        ViewHolder holder = (ViewHolder) v.getTag();
        Friend friend = data.get(position);
        holder.photo.setTag(position);
        holder.friend = friend;
    }

    @Override
    protected View newHeaderView(Context context, int partition, List<Friend> data, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.de_item_friend_index, null);
        return view;
    }

    @Override
    protected void bindHeaderView(View view, int partition, List<Friend> data) {
        Object objTag = view.getTag();
        if (objTag != null) {
            ((TextView) objTag).setText(String.valueOf(data.get(0).getSearchKey()));
        }
    }


    @Override
    protected View newView(Context context, int position, ViewGroup group) {
        return null;
    }

    @Override
    protected void bindView(View v, int position, Friend data) {

    }

    class PinnedHeaderCache {
        TextView titleView;
        ColorStateList textColor;
        Drawable background;
    }

    @Override
    protected SectionIndexer updateIndexer(Partition<Friend>[] data) {
        return new FriendSectionIndexer(data);
    }

    @Override
    public void configurePinnedHeader(View header, int position, int alpha) {
        PinnedHeaderCache cache = (PinnedHeaderCache) header.getTag();
        if (cache == null) {
            cache = new PinnedHeaderCache();
            cache.titleView = (TextView) header.findViewById(R.id.index);
            cache.textColor = cache.titleView.getTextColors();
            cache.background = header.getBackground();
            header.setTag(cache);
        }
        int section = getSectionForPosition(position);
        if (section != -1) {
            if(section == 0){
                cache.titleView.setText("★");
            }else if(section > 0) {
                String title = (String) getSectionIndexer().getSections()[section];
                cache.titleView.setText(title);
            }
        }
    }

    public static class ViewHolder {
        public TextView name,group,phone;
        public ImageView photo;
        public String userId;
        public Friend friend;
        public TextView unreadnum;
        public RelativeLayout rl;
        public CheckBox checkBox;
    }

    protected void newSetTag(View view, ViewHolder holder, int position, List<Friend> data) {

        holder.group = (TextView) view.findViewById(R.id.txt_ui_friend_group);
        holder.rl = (RelativeLayout) view.findViewById(R.id.rl_ui_friend);

        holder.name = (TextView) view.findViewById(R.id.de_ui_friend_name);
        holder.phone = (TextView) view.findViewById(R.id.txt_ui_friend_phone);
        holder.unreadnum = (TextView) view.findViewById(R.id.de_unread_num);
        holder.checkBox = (CheckBox) view.findViewById(R.id.de_checkbox);
        holder.photo = (ImageView) view.findViewById(R.id.de_ui_friend_icon);
    }


    @Override
    public Filter getFilter() {
        return null;
    }

    public void onItemClick(String friendId) {

    }

}
