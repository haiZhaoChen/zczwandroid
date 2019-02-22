package org.bigdata.zczw.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
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
import org.bigdata.zczw.entity.User;

import java.util.List;

/**
 * Created by darg on 2016/9/13.
 */
public class GridViewAdapter extends BaseAdapter {

    private List<User> list;
    private Context context;

    public GridViewAdapter(List<User> groupMemberList, Context context){
        this.context = context;
        this.list = groupMemberList;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_grid, null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.name_grid_item);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.img_grid_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        User user = list.get(position);
        switch (user.getUsername()){
            case "add":
                viewHolder.img.setImageResource(R.drawable.user_add);
                viewHolder.img.setScaleType(ImageView.ScaleType.FIT_XY);
                viewHolder.img.setPadding(16,16,16,16);
                viewHolder.name.setText("");
                break;
            case "del":
                viewHolder.img.setImageResource(R.drawable.user_del);
                viewHolder.img.setScaleType(ImageView.ScaleType.FIT_XY);
                viewHolder.img.setPadding(16,16,16,16);
                viewHolder.name.setText("");
                break;
            default:
                viewHolder.name.setText(list.get(position).getUsername());
                viewHolder.img.setImageResource(R.drawable.de_default_portrait);
                if (!TextUtils.isEmpty(list.get(position).getImagePosition()) && list.get(position).getImagePosition().length()>5) {
                    Picasso.with(context).load(list.get(position).getImagePosition()).resize(200,200).into(viewHolder.img);
                }
                break;
        }

        return convertView;

    }

    private class ViewHolder{
        private ImageView img;
        private TextView name;
    }
}
