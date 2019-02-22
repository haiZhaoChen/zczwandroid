package org.bigdata.zczw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.bigdata.zczw.R;
import org.bigdata.zczw.entity.Regulation;

import java.util.ArrayList;

public class RegulationAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Regulation> arrayList;


    public RegulationAdapter(Context context, ArrayList<Regulation> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
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
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_regulation,null);
            holder.name = (TextView) convertView.findViewById(R.id.txt_name_regulation_item);
            holder.num = (TextView) convertView.findViewById(R.id.txt_num_regulation_item);
            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();
        holder.name.setText(arrayList.get(position).getName());
        holder.num.setText("第"+(position+1)+"节");

        return convertView;
    }

    private class ViewHolder {
        private TextView name,num;
    }
}
