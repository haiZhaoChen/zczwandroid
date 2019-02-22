package org.bigdata.zczw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.bigdata.zczw.R;
import org.bigdata.zczw.entity.TagDataEntity;

import java.util.List;

/**
 * Created by darg on 2016/11/9.
 */
public class TagListAdapter extends BaseAdapter {

    private Context context;
    private List<TagDataEntity> dataEntities;

    public TagListAdapter(Context context,List<TagDataEntity> dataEntities){
        this.context = context;
        this.dataEntities = dataEntities;
    }

    @Override
    public int getCount() {
        return dataEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return dataEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView =LayoutInflater.from(context).inflate(R.layout.item_tag_list,null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.tag_list_name);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.textView.setText(dataEntities.get(position).getLabelName());

        return convertView;
    }

     private class ViewHolder{
         private TextView textView;
    }
}
