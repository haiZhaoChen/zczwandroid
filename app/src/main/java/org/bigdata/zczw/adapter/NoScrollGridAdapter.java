package org.bigdata.zczw.adapter;

import android.content.Context;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.squareup.picasso.Picasso;

import org.bigdata.zczw.R;
import org.bigdata.zczw.entity.Pictures;

import java.util.List;

public class NoScrollGridAdapter extends BaseAdapter {

    /**
     * 上下文
     */
    private Context ctx;
    /**
     * 图片Url集合
     */
    private List<Pictures> imageUrls;

    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.ic_empty)
            .showImageForEmptyUri(R.drawable.no_media)
            .showImageOnFail(R.drawable.ic_empty)
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .considerExifParams(true)
            .displayer(new RoundedBitmapDisplayer(20))
            .build();

    public NoScrollGridAdapter(Context ctx, List<Pictures> imageUrls) {
        this.ctx = ctx;
        this.imageUrls = imageUrls;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return imageUrls == null ? 0 : imageUrls.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return imageUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(ctx, R.layout.item_gridview, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_image);
        Picasso.with(ctx).load(imageUrls.get(position).getUrl()).resize(300,300).centerCrop().into(imageView);
//        ImageLoader.getInstance().displayImage(imageUrls.get(position).getUrl(), imageView, options);
        return view;
    }

}
