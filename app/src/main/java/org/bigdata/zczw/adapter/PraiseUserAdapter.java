package org.bigdata.zczw.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import org.bigdata.zczw.Singleton;
import org.bigdata.zczw.activity.PersonalActivity;
import org.bigdata.zczw.activity.UserInfoActivity;
import org.bigdata.zczw.entity.PraiseUser;
import org.bigdata.zczw.entity.User;
import org.bigdata.zczw.utils.DateUtils;
import org.bigdata.zczw.utils.SPUtil;

import java.util.List;

/**
 * Created by darg on 2016/10/28.
 */
public class PraiseUserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PraiseUser> praiseUserList;
    private Context context;

    public PraiseUserAdapter(Context context, List<PraiseUser> praiseUserList){
        this.context = context;
        this.praiseUserList = praiseUserList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        ViewHolder holder = (ViewHolder) viewHolder;

        holder.time.setText(DateUtils.convertToDate(praiseUserList.get(position).getPraiseTime()));
        if (!TextUtils.isEmpty(praiseUserList.get(position).getUnitsName())) {
            holder.name.setText(praiseUserList.get(position).getUserName()
                    +"-" +praiseUserList.get(position).getUnitsName()
                    +"-"+praiseUserList.get(position).getJobsName());
        }else {
            holder.name.setText(praiseUserList.get(position).getUserName());
        }


        String img = praiseUserList.get(position).getImagePosition();
        if (!TextUtils.isEmpty(img)) {
            Picasso.with(context).load(img).into(holder.img);
        }

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String authorId =praiseUserList.get(position).getUserId()+"";
                if(authorId.equals(SPUtil.getString(context, App.USER_ID))){
                    context.startActivity(new Intent(context, UserInfoActivity.class));
                }else{
                    Intent authorIntent = new Intent(context, PersonalActivity.class);
                    authorIntent.putExtra("PERSONAL",authorId);
                    context.startActivity(authorIntent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return praiseUserList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name,time;
        private ImageView img;
        private RelativeLayout item;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.item_name_user);
            time = (TextView) view.findViewById(R.id.item_time_user);
            img = (ImageView) view.findViewById(R.id.item_img_user);
            item = (RelativeLayout) view.findViewById(R.id.item_user);
        }
    }
}
