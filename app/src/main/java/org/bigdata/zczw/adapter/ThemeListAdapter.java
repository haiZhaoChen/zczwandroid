package org.bigdata.zczw.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.bigdata.zczw.R;
import org.bigdata.zczw.entity.TagDataEntity;
import org.bigdata.zczw.entity.Theme;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by darg on 2016/11/9.
 */
public class ThemeListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Theme> themeArrayList;
    private int[] icon;

    public ThemeListAdapter(Context context, ArrayList<Theme> themeArrayList){
        this.context = context;
        this.themeArrayList = themeArrayList;
        icon = new int[]{R.drawable.theme_icon1,R.drawable.theme_icon2,R.drawable.theme_icon3,R.drawable.theme_icon4};
    }

    @Override
    public int getCount() {
        return themeArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return themeArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView =LayoutInflater.from(context).inflate(R.layout.item_theme,null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.txt_title_theme_item);
            viewHolder.content = (TextView) convertView.findViewById(R.id.txt_content_theme_item);
            viewHolder.date = (TextView) convertView.findViewById(R.id.txt_date_theme_item);
            viewHolder.num = (TextView) convertView.findViewById(R.id.txt_num_theme_item);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.img_theme_item);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();


        viewHolder.title.setText(themeArrayList.get(position).getTitle());
        viewHolder.content.setText(themeArrayList.get(position).getOutline());

//        String s = themeArrayList.get(position).getOutline().substring(0,1);
//        Bitmap bitmap = Bitmap.createBitmap(100,100, Bitmap.Config.ARGB_8888);//创建一个宽度和高度都是400、32位ARGB图
//        Canvas canvas =new Canvas(bitmap);//初始化画布绘制的图像到icon上
//        canvas.drawColor(context.getResources().getColor(R.color.darkorange));//图层的背景色
//        Paint paint =new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DEV_KERN_TEXT_FLAG);//创建画笔
//        paint.setTextSize(60.0f);//设置文字的大小
//        paint.setTypeface(Typeface.DEFAULT_BOLD);//文字的样式(加粗)
////        paint.setTextAlign(Paint.Align.CENTER);
//        paint.setColor(Color.WHITE);//文字的颜色
//        canvas.drawText(s,20, 70, paint);//将文字写入。这里面的（120，130）代表着文字在图层上的初始位置
//        canvas.save(canvas.ALL_SAVE_FLAG);//保存所有图层
//        canvas.restore();
//        viewHolder.imageView.setImageBitmap(bitmap);
        viewHolder.imageView.setImageResource(icon[position%4]);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dealtTime = sdf.format(new Date(themeArrayList.get(position).getUpdtime()));

        viewHolder.date.setText(dealtTime);

        return convertView;
    }

     private class ViewHolder{
         private TextView title,content,date,num;
         private ImageView imageView;
     }
}
