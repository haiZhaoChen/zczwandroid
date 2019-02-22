package org.bigdata.zczw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.bigdata.zczw.R;
import org.bigdata.zczw.entity.Catalog;

import java.util.ArrayList;

public class CatalogAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Catalog> arrayList;

    final int TYPE_1 = 0;
    final int TYPE_2 = 1;

    public CatalogAdapter(Context context, ArrayList<Catalog> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getItemViewType(int position) {
        //这里我们修改的是对应头item和底部item
        if (arrayList.get(position).getParentId() == 0)
            return TYPE_1;
        else
            return TYPE_2;
    }

    /**
     * 该方法返回多少个不同的布局
     */
    @Override
    public int getViewTypeCount() {
        return 2;
    }


    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder1 holder1 = null;
        ViewHolder2 holder2 = null;

        int type = getItemViewType(position);

        if (convertView == null) {

            switch (type){
                case TYPE_1:
                    holder1 = new ViewHolder1();
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_catalog,null);
                    holder1.name = (TextView) convertView.findViewById(R.id.txt_name_regulation);
                    holder1.imageView = (ImageView) convertView.findViewById(R.id.img_show_regulation);
                    convertView.setTag(holder1);
                    break;
                case TYPE_2:
                    holder2 = new ViewHolder2();
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_regulation_name,null);
                    holder2.name = (TextView) convertView.findViewById(R.id.txt_name);
                    convertView.setTag(holder2);
                    break;
            }
        } else {
            switch (type) {
                case TYPE_1:
                    holder1 = (ViewHolder1) convertView.getTag();
                    break;
                case TYPE_2:
                    holder2 = (ViewHolder2) convertView.getTag();
                    break;
            }
        }

        switch (type) {
            case TYPE_1:
                holder1 = (ViewHolder1) convertView.getTag();
                holder1.name.setText(arrayList.get(position).getName());
                if (arrayList.get(position).isShow()) {
                    holder1.imageView.setImageResource(R.drawable.icon_doc_up);
                }else {
                    holder1.imageView.setImageResource(R.drawable.icon_doc_down);
                }
                break;
            case TYPE_2:
                holder2 = (ViewHolder2) convertView.getTag();
                holder2.name.setText(arrayList.get(position).getName());
                break;
        }

        return convertView;
    }

    private class ViewHolder1 {
        private TextView name;
        private ImageView imageView;
    }
    private class ViewHolder2 {
        private TextView name;
    }
}
