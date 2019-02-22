package org.bigdata.zczw.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.bigdata.zczw.App;
import org.bigdata.zczw.R;
import org.bigdata.zczw.activity.ImagePagerActivity;
import org.bigdata.zczw.activity.PersonalActivity;
import org.bigdata.zczw.activity.UserInfoActivity;
import org.bigdata.zczw.entity.Author;
import org.bigdata.zczw.entity.Pai;
import org.bigdata.zczw.entity.Video;
import org.bigdata.zczw.ui.NoScrollGridView;
import org.bigdata.zczw.utils.SPUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by SVN on 2017/10/17.
 */

public class PaiAdapter extends BaseAdapter {

    private ArrayList<Pai> paiArrayList;
    private Context context;

    public PaiAdapter(Context context,ArrayList<Pai> paiArrayList) {
        this.context = context;
        this.paiArrayList = paiArrayList;
    }

    @Override
    public int getCount() {
        return paiArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return paiArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_pai,null);
            viewHolder = new ViewHolder();
            viewHolder.header = (ImageView) convertView.findViewById(R.id.iv_userProfile_pai);
            viewHolder.menu = (ImageView) convertView.findViewById(R.id.more_pai);
            viewHolder.video = (ImageView) convertView.findViewById(R.id.video_img_pai_item);
            viewHolder.play = (ImageView) convertView.findViewById(R.id.auto_video_img_pai);
            viewHolder.type = (ImageView) convertView.findViewById(R.id.img_pai_type);

            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_name_pai);
            viewHolder.time = (TextView) convertView.findViewById(R.id.tv_publish_time_pai);
            viewHolder.addTime = (TextView) convertView.findViewById(R.id.tv_add_time_pai);
            viewHolder.content = (TextView) convertView.findViewById(R.id.txt_content_pai);

            viewHolder.imagesGridview = (NoScrollGridView) convertView.findViewById(R.id.img_grid_view_pai);

            viewHolder.rlVideo = (RelativeLayout) convertView.findViewById(R.id.video_content_pai);

            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();

        final Pai pai = paiArrayList.get(position);
        Author author = pai.getAuthor();
        if (!TextUtils.isEmpty(author.getUnitsName())) {
            viewHolder.name.setText(author.getName()+"-"+author.getUnitsName());
        }else {
            viewHolder.name.setText(author.getName());
        }

        if(!TextUtils.isEmpty(author.getPortrait())) {
            Picasso.with(context).load(author.getPortrait()).into(viewHolder.header);
        }else{
            viewHolder.header.setImageResource(R.drawable.de_default_portrait);
        }

        viewHolder.header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String authorId = pai.getAuthor().getId()+"";
                if(authorId.equals(SPUtil.getString(context, App.USER_ID))){
                    context.startActivity(new Intent(context, UserInfoActivity.class));
                }else{
                    Intent authorIntent = new Intent(context, PersonalActivity.class);
                    authorIntent.putExtra("PERSONAL",authorId);
                    context.startActivity(authorIntent);
                }
            }
        });

        switch (pai.getWorkType()){
            case 0:
                viewHolder.addTime.setVisibility(View.GONE);
                break;
            case 1:
                viewHolder.addTime.setText("岗前");
                break;
            case 2:
                viewHolder.addTime.setText("岗中");
                break;
            case 3:
                viewHolder.addTime.setText("岗后");
                break;
            case 4:
                viewHolder.addTime.setText("其他");
                break;
        }


        long old = pai.getCreateDate();
        Date date = new Date();
        long now = date.getTime();
        long t = now-old;
        if (t < 60*1000*5) {
            viewHolder.time.setText("刚刚");
        }else if (t <60*1000*60) {
            int s= (int) (t/60/1000);
            viewHolder.time.setText(s+"分钟前");
        }else {
            if (isToday(old)) {
                SimpleDateFormat sdf = new SimpleDateFormat("今天 HH:mm");
                String dealtTime = sdf.format(new Date(old));
                viewHolder.time.setText(dealtTime);
            }else if (isYesterday(old)) {
                SimpleDateFormat sdf = new SimpleDateFormat("昨天 HH:mm");
                String dealtTime = sdf.format(new Date(old));
                viewHolder.time.setText(dealtTime);
            }else {
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
                String dealtTime = sdf.format(new Date(old));
                viewHolder.time.setText(dealtTime);
            }
        }

        viewHolder.content.setText(pai.getContent());

        if (pai.getImages()!=null && pai.getImages().size()>0) {
            viewHolder.imagesGridview.setVisibility(View.VISIBLE);
            viewHolder.rlVideo.setVisibility(View.GONE);

            viewHolder.imagesGridview.setAdapter(new PaiGridAdapter(context, pai.getImages()));
        }else {
            viewHolder.imagesGridview.setVisibility(View.GONE);
            viewHolder.rlVideo.setVisibility(View.VISIBLE);

            final Video video = pai.getVideo();
            Picasso.with(context).load(video.getThumbnail()).resize(720,400).centerCrop().into(viewHolder.video);
            viewHolder.video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String videoPath = video.getPath();
                    Uri uri = Uri.parse(videoPath);
                    Intent intentPlayer = new Intent(Intent.ACTION_VIEW);
                    intentPlayer.setDataAndType(uri, "video/mp4");
                    context.startActivity(intentPlayer);
                }
            });

            viewHolder.play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String videoPath = video.getPath();
                    Uri uri = Uri.parse(videoPath);
                    Intent intentPlayer = new Intent(Intent.ACTION_VIEW);
                    intentPlayer.setDataAndType(uri, "video/mp4");
                    context.startActivity(intentPlayer);
                }
            });
        }

        // 点击回帖九宫格，查看大图
        viewHolder.imagesGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imageBrowse(position, (ArrayList<String>) pai.getImages());
            }
        });


        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Record recordMessage = recordList.get(position);
                showPopupWindow(finalViewHolder.menu , pai);
            }
        });

        if (pai.getTag() == 1) {
            viewHolder.type.setImageResource(R.drawable.icon_pai_zy);
        }else if (pai.getTag() == 2) {
            viewHolder.type.setImageResource(R.drawable.icon_pai_wt);
        }else {
            viewHolder.type.setImageResource(R.drawable.icon_pai_good_jy);
        }

        return convertView;
    }

    private class ViewHolder{
        private TextView name,time,content,addTime;
        private ImageView header,menu;
        private ImageView video,play,type;
        private NoScrollGridView imagesGridview;
        private RelativeLayout rlVideo;
    }

    public static boolean isYesterday(long timestamp) {
        Calendar c = Calendar.getInstance();
        clearCalendar(c, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND);
        c.add(Calendar.DAY_OF_MONTH, -1);
        long firstOfDay = c.getTimeInMillis(); // 昨天最早时间

        c.setTimeInMillis(timestamp);
        clearCalendar(c, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND); // 指定时间戳当天最早时间

        return firstOfDay == c.getTimeInMillis();
    }

    public static boolean isToday(long timestamp) {
        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);


        Calendar cal = Calendar.getInstance();
        Date date = new Date(timestamp);
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == 0) {
                return true;
            }
        }
        return false;
    }

    private static void clearCalendar(Calendar c, int... fields) {
        for (int f : fields) {
            c.set(f, 0);
        }
    }

    protected void imageBrowse(int position, ArrayList<String> images) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, images);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        context.startActivity(intent);
    }

    private void showPopupWindow(View view , final Pai pai) {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(context).inflate(R.layout.pop_del, null);
        // 设置按钮的点击事件
        LinearLayout delete = (LinearLayout) contentView.findViewById(R.id.more_delete);

        final PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setTouchable(true);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCheckBarClickListener != null) {
                    popupWindow.dismiss();
                    onCheckBarClickListener.onMenuClick("11",pai);//交给前面的fragment处理
                }
            }
        });
        // 设置好参数之后再show
        popupWindow.showAsDropDown(view,0,0);
    }

    private onCheckBarClickListener onCheckBarClickListener;
    public void setOnCheckBarClickListener(onCheckBarClickListener onItemClickListener) {
        this.onCheckBarClickListener = onItemClickListener;
    }

    public interface onCheckBarClickListener {
        void onMenuClick(String type, Pai pai);
    }

    public void refreshMsg(int id) {
        if (paiArrayList != null && !paiArrayList.isEmpty()) {
            for (int i = 0; i < paiArrayList.size(); i++) {
                if (paiArrayList.get(i).getId() == id) {
                    paiArrayList.remove(i);
                    this.notifyDataSetChanged();
                    break;
                }
            }
        }
    }

}
