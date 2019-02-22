package org.bigdata.zczw.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.bigdata.zczw.R;
import org.bigdata.zczw.adapter.CollectUserAdapter;
import org.bigdata.zczw.adapter.PraiseUserAdapter;
import org.bigdata.zczw.entity.CollectList;
import org.bigdata.zczw.entity.CollectUser;
import org.bigdata.zczw.entity.PraiseList;
import org.bigdata.zczw.entity.PraiseUser;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.ServerUtils;
import org.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {

    private TextView textView;
    private RecyclerView listView;

    private List<PraiseUser> praiseUserList;
    private List<CollectUser> collectUserList;
    private PraiseUserAdapter userAdapter;
    private CollectUserAdapter collectUserAdapter;

    private String type;
    private Long messageId;

    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView = (TextView) view.findViewById(R.id.null_user_list_frag);
        listView = (RecyclerView) view.findViewById(R.id.listView_user_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        listView.setLayoutManager(linearLayoutManager);
        listView.setHasFixedSize(true);
        listView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        type = getArguments().getString("type");
        messageId = getArguments().getLong("id");
        if (type.equals("save")) {
            ServerUtils.collectUserList(messageId,1,200,collectList);
        }else if(type.equals("praise")){
            ServerUtils.prasieUserList(messageId,1,200,praiseList);
        }

//        listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                Log.e("1111", "onScrollStateChanged: "+recyclerView.canScrollVertically(-1));
//            }
//        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (type.equals("save")) {
            ServerUtils.collectUserList(messageId,1,200,collectList);
        }else if(type.equals("praise")){
            ServerUtils.prasieUserList(messageId,1,200,praiseList);
        }
    }

    private RequestCallBack<String> collectList = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
//            Log.e("1111", "json: "+json );
            CollectList collect = JsonUtils.jsonToPojo(json, CollectList.class);
            if (collect.getMsg().equals("OK")) {
                collectUserList = collect.getData();
                if (collectUserList != null) {
                    textView.setVisibility(View.GONE);
                    collectUserAdapter = new CollectUserAdapter(getContext(),collectUserList);
                    listView.setAdapter(collectUserAdapter);
                }
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
            Toast.makeText(getContext(), "连接失败，请检查网络连接。", Toast.LENGTH_SHORT).show();
        }
    };
    private RequestCallBack<String> praiseList = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
//            Log.e("1111", "json: "+json );
            PraiseList praiseList = JsonUtils.jsonToPojo(json, PraiseList.class);
            if (praiseList.getMsg().equals("OK")) {
                praiseUserList = praiseList.getData();
                if (praiseUserList != null) {
                    textView.setVisibility(View.GONE);
                    userAdapter = new PraiseUserAdapter(getContext(),praiseUserList);
                    listView.setAdapter(userAdapter);
                }
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
            Toast.makeText(getContext(),"连接失败，请检查网络连接。",Toast.LENGTH_SHORT).show();
        }
    };
}
