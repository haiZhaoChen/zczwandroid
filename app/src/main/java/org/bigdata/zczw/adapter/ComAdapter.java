package org.bigdata.zczw.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.bigdata.zczw.App;
import org.bigdata.zczw.R;
import org.bigdata.zczw.activity.PersonalActivity;
import org.bigdata.zczw.activity.UserInfoActivity;
import org.bigdata.zczw.entity.Comment;
import org.bigdata.zczw.entity.Pai;
import org.bigdata.zczw.utils.DateUtils;
import org.bigdata.zczw.utils.SPUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by darg on 2016/10/28.
 */
public class ComAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_1 = 0;
    private final int TYPE_2 = 1;
    private final int VIEW_TYPE = 2; //

    private List<Comment> commentList ;
    private Context context;

    //建立枚举 2个item 类型
    public enum ITEM_TYPE {
        ITEM1, //类型1 灰底
        ITEM2 //类型2 白底
    }

    public ComAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @Override
    public int getItemViewType(int position) {
        //Enum类提供了一个ordinal()方法，返回枚举类型的序数，这里ITEM_TYPE.ITEM1.ordinal()代表0， ITEM_TYPE.ITEM2.ordinal()代表1
        if (TextUtils.isEmpty(commentList .get(position).getRootCommentsId())) {
            return ITEM_TYPE.ITEM1.ordinal();
        }else {
            return ITEM_TYPE.ITEM2.ordinal();
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //加载Item View的时候根据不同TYPE加载不同的布局
        if (viewType == ITEM_TYPE.ITEM1.ordinal()) {
            final View view = LayoutInflater.from(context).inflate(R.layout.item_com, parent, false);
            return new ComViewHolder1(view);
        } else {
            final View view = LayoutInflater.from(context).inflate(R.layout.item_reply, parent, false);
            return new ComViewHolder2(view);
        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        if (viewHolder instanceof ComViewHolder1) {
            ComViewHolder1 holder = (ComViewHolder1) viewHolder;

            if (!TextUtils.isEmpty(commentList.get(position).getUnitsName())) {
                holder.name.setText(commentList.get(position).getUserName()+"-"+commentList.get(position).getUnitsName()+"-"+commentList.get(position).getJobsName());
            }else {
                holder.name.setText(commentList.get(position).getUserName());
            }


            String commentsTime= DateUtils.convertToDate(commentList.get(position).getCommentsTime());
            holder.time.setText(commentsTime);
            holder.content.setText(commentList.get(position).getCommentsContent());

            String img = commentList.get(position).getImagePosition();
            if (!TextUtils.isEmpty(img)) {
                Picasso.with(context).load(img).into(holder.img);
            }else {
                holder.img.setImageResource(R.drawable.de_default_portrait);
            }

            Comment comment = commentList.get(position);
            ArrayList<String> numList = new ArrayList<>();
            if (!TextUtils.isEmpty(comment.getRangeStr())) {
                String rangeStr = comment.getRangeStr();
                String s[]=rangeStr.split("/");
                for (int i = 0; i < s.length; i++) {
                    String num[]=s[i].split("-");
                    numList.add(num[0]);
                    numList.add(num[1]);
                }
                SpannableStringBuilder builder = new SpannableStringBuilder(commentList.get(position).getCommentsContent());
                //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
                int n = 0;
                while (n<numList.size()){
                    int a = Integer.parseInt(numList.get(n));
                    int b = Integer.parseInt(numList.get(n+1));
                    builder.setSpan(new ForegroundColorSpan(Color.BLUE), a, b, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    n = n+2;
                }

                holder.content.setText(builder);

            }else {
                holder.content.setText(commentList.get(position).getCommentsContent());
            }

            if (comment.getListComentReplyInfo()!=null && comment.getListComentReplyInfo().size()>0) {
                holder.line.setVisibility(View.VISIBLE);
            }else {
                holder.line.setVisibility(View.GONE);
            }

            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String authorId =commentList.get(position).getUserId()+"";
                    if(authorId.equals(SPUtil.getString(context, App.USER_ID))){
                        context.startActivity(new Intent(context, UserInfoActivity.class));
                    }else{
                        Intent authorIntent = new Intent(context, PersonalActivity.class);
                        authorIntent.putExtra("PERSONAL",authorId);
                        context.startActivity(authorIntent);
                    }
                }
            });
            holder.content.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onLongListener != null) {
                        onLongListener.onContentClick(2 , v,commentList.get(position));
                    }
                    return true;
                }
            });
            holder.content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onLongListener != null) {
                        onLongListener.onContentClick(1 , v , commentList.get(position));
                    }
                }
            });
            holder.reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onLongListener != null) {
                        onLongListener.onContentClick(1 , v , commentList.get(position));
                    }
                }
            });

            holder.itemView.setTag(position);
        } else {
            ComViewHolder2 holder = (ComViewHolder2) viewHolder;
            SpannableStringBuilder builder = new SpannableStringBuilder(
                    commentList.get(position).getUserName()+ "：回复"+commentList.get(position).getBuserName()+"："+
                            commentList.get(position).getCommentsContent());
            //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
            int n = commentList.get(position).getUserName().length();
            builder.setSpan(new ForegroundColorSpan(Color.parseColor("#127dc4")), 0, n, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            holder.content.setText(builder);
            holder.itemView.setTag(position);
            holder.content.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onLongListener != null) {
                        onLongListener.onContentClick(2,v,commentList.get(position));
                    }
                    return true;
                }
            });
            holder.content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onLongListener != null) {
                        onLongListener.onContentClick(1 , v , commentList.get(position));
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return commentList==null?0:commentList.size();
    }

    class ComViewHolder1 extends RecyclerView.ViewHolder {

        private TextView name,time,content,reply;
        private ImageView img;
        private RelativeLayout item;
        private View line;

        public ComViewHolder1(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.comment_msg_name);
            time = (TextView) view.findViewById(R.id.comment_msg_publish_time);
            content = (TextView) view.findViewById(R.id.comment_msg_commentContent);
            reply = (TextView) view.findViewById(R.id.txt_reply);
            img = (ImageView) view.findViewById(R.id.comment_msg_userProfile);
            item = (RelativeLayout) view.findViewById(R.id.item_com);
            line =  view.findViewById(R.id.line_com_item);
        }
    }

    class ComViewHolder2 extends RecyclerView.ViewHolder {

        private TextView content;

        public ComViewHolder2(View view) {
            super(view);
            content = (TextView) view.findViewById(R.id.txt_reply_item);
        }
    }


    private OnLongListener onLongListener;
    public void setOnLongListener(OnLongListener onLongListener) {
        this.onLongListener = onLongListener;
    }

    public interface OnLongListener {
        void onContentClick(int type ,View v , Comment com);
    }
}
