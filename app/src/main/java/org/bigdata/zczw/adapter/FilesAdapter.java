package org.bigdata.zczw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.bigdata.zczw.R;
import org.bigdata.zczw.utils.FileSizeUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class FilesAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<File> fileList;
    private onDelClickListener onDelClickListener;

    public FilesAdapter(Context context, ArrayList<File> fileList){
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView =LayoutInflater.from(context).inflate(R.layout.item_file_list,null);
            viewHolder = new ViewHolder();
            viewHolder.imgWord = (ImageView) convertView.findViewById(R.id.img_word_type);
            viewHolder.imgDel = (ImageView) convertView.findViewById(R.id.img_del_type);
            viewHolder.wordName = (TextView) convertView.findViewById(R.id.txt_word_name);
            viewHolder.wordSize = (TextView) convertView.findViewById(R.id.txt_word_size);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();

        File file = fileList.get(position);
        viewHolder.wordName.setText(file.getName());

        viewHolder.wordName.setText(file.getName());
        if (FileSizeUtil.getFileOrFilesSize(file, 2)>1024) {
            viewHolder.wordSize.setText(FileSizeUtil.getFileOrFilesSize(file, 3)+"MB");
        }else {
            viewHolder.wordSize.setText(FileSizeUtil.getFileOrFilesSize(file, 2)+"KB");
        }
        /* 取得扩展名 */
        String end = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).toLowerCase(Locale.getDefault());
        if (end.equals("ppt")|| end.equals("pptx")) {
            viewHolder.imgWord.setImageResource(R.drawable.icon_word_ppt);
        } else if (end.equals("xls")|| end.equals("xlsx")) {
            viewHolder.imgWord.setImageResource(R.drawable.icon_word_xls);
        } else if (end.equals("doc")|| end.equals("docx")) {
            viewHolder.imgWord.setImageResource(R.drawable.icon_word_doc);
        } else if (end.equals("pdf")) {
            viewHolder.imgWord.setImageResource(R.drawable.icon_word_pdf);
        } else if (end.equals("txt")) {
            viewHolder.imgWord.setImageResource(R.drawable.icon_word_txt);
        } else if (end.equals("zip")|| end.equals("rar")|| end.equals("7z")) {
            viewHolder.imgWord.setImageResource(R.drawable.icon_word_rar);
        } else {
            viewHolder.imgWord.setImageResource(R.drawable.icon_word_other);
        }

        viewHolder.imgDel.setVisibility(View.VISIBLE);
        viewHolder.imgDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDelClickListener!=null) {
                    onDelClickListener.onDelClick(position);
                }
            }
        });

        return convertView;
    }

    public void setOnDelClickListener(onDelClickListener onDelClickListener) {
        this.onDelClickListener = onDelClickListener;
    }

    public interface onDelClickListener {
        void onDelClick(int position);
    }

     private class ViewHolder{
         private TextView wordName,wordSize;
         private ImageView imgWord,imgDel;
     }
}
