package org.bigdata.zczw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.bigdata.zczw.R;
import org.bigdata.zczw.entity.FileBean;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by darg on 2016/11/9.
 */
public class FileListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<FileBean> fileList;

    public FileListAdapter(Context context, ArrayList<FileBean> fileList){
        this.context = context;
        this.fileList = fileList;
    }

    @Override
    public int getCount() {
        return fileList.size();
    }

    @Override
    public Object getItem(int position) {
        return fileList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView =LayoutInflater.from(context).inflate(R.layout.item_file_list,null);
            viewHolder = new ViewHolder();
            viewHolder.imgWord = (ImageView) convertView.findViewById(R.id.img_word_type);
            viewHolder.wordName = (TextView) convertView.findViewById(R.id.txt_word_name);
            viewHolder.wordSize = (TextView) convertView.findViewById(R.id.txt_word_size);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();

        FileBean fileBean = fileList.get(position);
        viewHolder.wordName.setText(fileBean.getName());
        if (fileBean.getSize()>1024*1024) {
            String string1 = new DecimalFormat("0.00").format(fileBean.getSize()/1024.00/1024.00);
            viewHolder.wordSize.setText(string1 +"MB");
        }else {
            String string1 = new DecimalFormat("0.00").format(fileBean.getSize()/1024.00);
            viewHolder.wordSize.setText(string1+"KB");
        }
        switch (fileBean.getFileType()){
            case "txt":
                viewHolder.imgWord.setImageResource(R.drawable.icon_word_txt);
                break;
            case "doc":
            case "docx":
                viewHolder.imgWord.setImageResource(R.drawable.icon_word_doc);
                break;
            case "pdf":
                viewHolder.imgWord.setImageResource(R.drawable.icon_word_pdf);
                break;
            case "ppt":
            case "pptx":
                viewHolder.imgWord.setImageResource(R.drawable.icon_word_ppt);
                break;
            case "xls":
            case "xlsx":
                viewHolder.imgWord.setImageResource(R.drawable.icon_word_xls);
                break;
            case "rar":
            case "7z":
            case "zip":
                viewHolder.imgWord.setImageResource(R.drawable.icon_word_rar);
                break;
            default:
                viewHolder.imgWord.setImageResource(R.drawable.icon_word_other);
                break;
        }

        return convertView;
    }

     private class ViewHolder{
         private TextView wordName,wordSize;
         private ImageView imgWord;
     }
}
