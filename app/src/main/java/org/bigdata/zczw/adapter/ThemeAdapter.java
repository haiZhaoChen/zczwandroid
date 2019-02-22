package org.bigdata.zczw.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.bigdata.zczw.R;
import org.bigdata.zczw.entity.TCom;
import org.bigdata.zczw.entity.Theme;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by darg on 2016/11/9.
 */
public class ThemeAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<TCom> comList;

    public ThemeAdapter(Context context, ArrayList<TCom> comList){
        this.context = context;
        this.comList = comList;
    }

    @Override
    public int getCount() {
        return comList.size();
    }

    @Override
    public Object getItem(int position) {
        return comList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView =LayoutInflater.from(context).inflate(R.layout.item_theme_com,null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.txt_name_theme_com);
            viewHolder.content = (TextView) convertView.findViewById(R.id.txt_com_theme_com);
            viewHolder.date = (TextView) convertView.findViewById(R.id.txt_time_theme_com);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.img_theme_com);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();

        TCom com = comList.get(position);

        viewHolder.title.setText(com.getC_userName()+"-"+com.getUnitsName());

        viewHolder.content.setText(com.getC_content());
        if (!TextUtils.isEmpty(com.getC_portrait()) ) {
            Picasso.with(context).load(com.getC_portrait()).into(viewHolder.imageView);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        String dealtTime = sdf.format(new Date(com.getC_createtime()));
        viewHolder.date.setText(dealtTime);


        return convertView;
    }

     private class ViewHolder{
         private TextView title,content,date;
         private ImageView imageView;
     }
}
