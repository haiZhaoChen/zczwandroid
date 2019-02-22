package org.bigdata.zczw.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.bigdata.zczw.R;
import org.bigdata.zczw.entity.Comment;
import org.bigdata.zczw.entity.DynamicComments;
import org.bigdata.zczw.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by froger_mcs on 11.11.14.
 */
public class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Comment> commentList ;
    private Context context;

    public CommentsAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        CommentViewHolder holder = (CommentViewHolder) viewHolder;
        Comment comment = commentList.get(position);
        holder.comment_tv_name.setText(comment.getUserName());
        String commentsTime= DateUtils.convertToDate(comment.getCommentsTime());
        holder.comment_tv_publish_time.setText(commentsTime);

        String content = comment.getCommentsContent();
        ArrayList<String> numList = new ArrayList<>();
        if (!TextUtils.isEmpty(comment.getRangeStr())) {
            String rangeStr = comment.getRangeStr();
            String s[]=rangeStr.split("/");
            for (int i = 0; i < s.length; i++) {
                String num[]=s[i].split("-");
                numList.add(num[0]);
                numList.add(num[1]);
            }
            SpannableStringBuilder builder = new SpannableStringBuilder(content);
            //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
            int n = 0;
            while (n<numList.size()){
                int a = Integer.parseInt(numList.get(n));
                int b = Integer.parseInt(numList.get(n+1));
                builder.setSpan(new ForegroundColorSpan(Color.BLUE), a, b, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                n = n+2;
            }

            holder.comment_tv_commentContent.setText(builder);
        }else {
            holder.comment_tv_commentContent.setText(content);
        }

        if (comment.getImagePosition() != null) {
            Picasso.with(context).load(comment.getImagePosition()).into(holder.comment_iv_userProfile);
        }
    }


    @Override
    public int getItemCount() {
        return commentList==null?0:commentList.size();
    }

    public void updateItems() {
        notifyDataSetChanged();
    }

    public void addItem() {
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        ImageView comment_iv_userProfile;
        TextView comment_tv_name;

        TextView comment_tv_commentContent;
        TextView comment_tv_publish_time;

        public CommentViewHolder(View view) {
            super(view);
            comment_iv_userProfile = (ImageView) view.findViewById(R.id.comment_iv_userProfile);
            comment_tv_name = (TextView) view.findViewById(R.id.comment_tv_name);
            comment_tv_commentContent = (TextView) view.findViewById(R.id.comment_tv_commentContent);
            comment_tv_publish_time = (TextView) view.findViewById(R.id.comment_tv_publish_time);
        }
    }
}
