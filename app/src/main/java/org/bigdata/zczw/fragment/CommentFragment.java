package org.bigdata.zczw.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.bigdata.zczw.App;
import org.bigdata.zczw.R;
import org.bigdata.zczw.activity.CommentsActivity;
import org.bigdata.zczw.activity.MessageActivity;
import org.bigdata.zczw.adapter.ComAdapter;
import org.bigdata.zczw.entity.Comment;
import org.bigdata.zczw.entity.DemoApiJSON;
import org.bigdata.zczw.utils.CustomLinearLayoutManager;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.SPUtil;
import org.bigdata.zczw.utils.ScreenUtils;
import org.bigdata.zczw.utils.ServerUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentFragment extends Fragment {

    private TextView textView;
    private RecyclerView listView;

    private Long messageId;

    private ArrayList<Comment> comments;
    private ArrayList<Comment> commentList;
    private ComAdapter commentsAdapter;

    private PopupWindow popupWindow;
    private View contentView;
    private Comment comment1;

    public CommentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView = (TextView) view.findViewById(R.id.null_comment);
        listView = (RecyclerView) view.findViewById(R.id.listView_comment);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext()){
//            @Override
//            public boolean canScrollVertically() {
//                return false;
//            }
        };
        listView.setLayoutManager(linearLayoutManager);
        listView.setHasFixedSize(true);
        listView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        messageId = getArguments().getLong("id");
        comments = new ArrayList<>();
        commentList = new ArrayList<>();
//        ServerUtils.getCommentList(messageId+"", comment);

        listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });



//        CustomLinearLayoutManager linearLayoutManager = new CustomLinearLayoutManager(mContext);
//        linearLayoutManager.setScrollEnabled(false);
//        mDevicesRV.setLayoutManager(linearLayoutManager);

    }

    @Override
    public void onStart() {
        super.onStart();
        commentList.clear();
        comments.clear();
        ServerUtils.getCommentList(messageId+"", 1 , 20 ,comment);
    }

    private RequestCallBack<String> comment = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            DemoApiJSON demoApiJSON = JsonUtils.jsonToPojo(json, DemoApiJSON.class);
            switch (demoApiJSON.getStatus()) {
                case 200://查询成功
                    textView.setVisibility(View.GONE);

                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        String data = jsonObject.getString("data");
                        comments = (ArrayList<Comment>) JsonUtils.jsonToList(data, Comment.class);

                        for (int i = 0; i < comments.size(); i++) {
                            commentList.add(comments.get(i));
                            if (comments.get(i).getListComentReplyInfo()!= null && comments.get(i).getListComentReplyInfo().size()>0) {
                                commentList.addAll(comments.get(i).getListComentReplyInfo());
                            }
                        }

                        commentsAdapter = new ComAdapter(getContext(), commentList);
                        listView.setAdapter(commentsAdapter);

                        commentsAdapter.setOnLongListener(onLongListener);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:

                    break;
            }
        }
        @Override
        public void onFailure(HttpException e, String s) {

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            ServerUtils.getCommentList(messageId+"", 1 , 20 ,comment);
        }
    }

    private ComAdapter.OnLongListener onLongListener = new ComAdapter.OnLongListener() {
        @Override
        public void onContentClick(int type, View v, Comment com) {
            Intent intent = new Intent(getContext(), CommentsActivity.class);//查看评论
            intent.putExtra("messageId", messageId+"");
            intent.putExtra("commentCount", commentList.size() + "");
            intent.putExtra("com",com);
            startActivity(intent);

        }
    };


}
