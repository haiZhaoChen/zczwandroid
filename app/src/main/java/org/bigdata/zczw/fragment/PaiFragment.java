package org.bigdata.zczw.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.bigdata.zczw.App;
import org.bigdata.zczw.R;
import org.bigdata.zczw.activity.AddPaiActivity;
import org.bigdata.zczw.adapter.PaiAdapter;
import org.bigdata.zczw.entity.AttendStatus;
import org.bigdata.zczw.entity.CheckStatus;
import org.bigdata.zczw.entity.DemoApiJSON;
import org.bigdata.zczw.entity.Pai;
import org.bigdata.zczw.entity.PaiData;
import org.bigdata.zczw.ui.WinToast;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.SPUtil;
import org.bigdata.zczw.utils.ServerUtils;
import org.bigdata.zczw.utils.Utils;
import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaiFragment extends Fragment implements View.OnClickListener , PullToRefreshBase.OnRefreshListener<ListView> {

    private ImageView addPai,icon;
    private LinearLayout llType;
    private ImageView imgNull;
    private Toolbar toolbar;
    private View bar;

    private PopupWindow popupWindow,popAddPai;
    private View contentView,view;

    private PullToRefreshListView listView;
    private PaiAdapter paiAdapter;
    private ArrayList<Pai> paiArrayList;

    private String category,tag,timeType;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 101:
                    icon.setImageResource(R.drawable.icon_down);
                    ServerUtils.getPaiList("" , "true" ,timeType,category , tag , paiCallBack);
                    break;
            }
        }
    };
    private int paiId;
    private int addType,workType;

    public PaiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pai, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (PullToRefreshListView) view.findViewById(R.id.pai_list_view);
        addPai = (ImageView) view.findViewById(R.id.img_add_pai);
        icon = (ImageView) view.findViewById(R.id.img_icon_pai_frg);
        imgNull = (ImageView) view.findViewById(R.id.img_null);
        llType = (LinearLayout) view.findViewById(R.id.ll_type_pai_frg);
        toolbar = (Toolbar) view.findViewById(R.id.tool_bar_pai_frg);
        bar = view.findViewById(R.id.view_pai_frg);

        contentView = LayoutInflater.from(getContext()).inflate(R.layout.pop_pai_type, null);

        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        initData();

    }

    private void initData() {
        category = "";
        tag = "";
        paiArrayList = new ArrayList<>();

        addType = 1;
        workType = 1;
        timeType = "";

        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(this);
        llType.setOnClickListener(this);
        addPai.setOnClickListener(this);

        ServerUtils.getPaiList("" , "true" , timeType ,category , tag , paiCallBack);
        //请求数据拦截发布随手拍
        ServerUtils.checkStatus(needCallBack);
    }

    private RequestCallBack<String> needCallBack = new RequestCallBack<String>() {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            CheckStatus bean = JsonUtils.jsonToPojo(json,CheckStatus.class);
            if (bean != null && bean.getStatus() == 200) {
                AttendStatus status = bean.getData();

                if (status.isUnitAccount()){
                    addPai.setVisibility(View.GONE);


                }else {
                    addPai.setVisibility(View.VISIBLE);

                }
            }

        }

        @Override
        public void onFailure(HttpException e, String s) {
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_add_pai:
                showAddPop();
                break;
            case R.id.ll_type_pai_frg:
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    icon.setImageResource(R.drawable.icon_down);
                }else {
                    showPopWindow(category,tag,timeType);
                    icon.setImageResource(R.drawable.icon_up);
                }
                break;

            case R.id.txt_cancel_dialog_act:
                addType = 1;
                workType = 1;
                popAddPai.dismiss();
                break;
            case R.id.txt_ok_dialog_act:
                popAddPai.dismiss();
                Intent intent = new Intent(getContext(),AddPaiActivity.class);
                intent.putExtra("category",addType+"");
                intent.putExtra("workType",workType+"");
                startActivityForResult(intent,101);
                addType = 1;
                workType = 1;
                break;
        }
    }

    private PaiAdapter.onCheckBarClickListener onCheckBarClickListener = new PaiAdapter.onCheckBarClickListener() {
        @Override
        public void onMenuClick(String type, Pai pai) {
            switch (type){
                case "11":
                    paiId = pai.getId();
                    deleteDialog(pai);
                    break;
            }
        }
    };

    private void deleteDialog(final Pai pai) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("是否删除此随手拍？");
        builder.setTitle("删除随手拍提醒：");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String user = SPUtil.getString(getContext(), App.USER_NAME);
                if (pai.getAuthor().getName().equals(user)) {
                    ServerUtils.delPai(pai.getId()+"",delCallBack);
                }else {
                    Utils.showToast(getContext(),"无法删除他人随手拍");
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    private void showAddPop() {
        view = LayoutInflater.from(getContext()).inflate(R.layout.pop_add_pai, null);

        popAddPai = new PopupWindow(this.view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        TextView textView2 = (TextView) view.findViewById(R.id.txt_ok_dialog_act);
        TextView textView3 = (TextView) view.findViewById(R.id.txt_cancel_dialog_act);
        RadioGroup rgType= (RadioGroup) view.findViewById(R.id.rg_add_type);
        RadioGroup rgTime= (RadioGroup) view.findViewById(R.id.rg_add_time);

        textView2.setOnClickListener(this);
        textView3.setOnClickListener(this);

        rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.rb_aq_add_type:
                        addType = 1;
                        break;
                    case R.id.rb_zt_add_type:
                        addType = 2;
                        break;
                }
            }
        });
        rgTime.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.rb_before_add_time:
                        workType = 1;
                        break;
                    case R.id.rb_on_add_time:
                        workType = 2;
                        break;
                    case R.id.rb_after_add_time:
                        workType = 3;
                        break;
                    case R.id.rb_else_add_time:
                        workType = 4;
                        break;
                }
            }
        });

        popAddPai.setTouchable(true);

        popAddPai.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
            }
        });

        popAddPai.setAnimationStyle(R.style.anim_popup_dir);

        // 设置好参数之后再show
        popAddPai.showAtLocation(getActivity().findViewById(R.id.rl_pai_frg), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void showPopWindow(String str1 , String str2,String str3) {
        // 一个自定义的布局，作为显示的内容
        RadioGroup rgCategory= (RadioGroup) contentView.findViewById(R.id.rg_category);
        RadioGroup rgTag= (RadioGroup) contentView.findViewById(R.id.rg_tag);
        RadioGroup rgTime= (RadioGroup) contentView.findViewById(R.id.rg_add_time_type);
        LinearLayout linearLayout = (LinearLayout) contentView.findViewById(R.id.ll_black);

        switch (str1){
            case "":
                rgCategory.check(R.id.rb_category_all);
                break;
            case "1":
                rgCategory.check(R.id.rb_category_aq);
                break;
            case "2":
                rgCategory.check(R.id.rb_category_zt);
                break;
        }

        switch (str2){
            case "":
                rgTag.check(R.id.rb_tag_all);
                break;
            case "1":
                rgTag.check(R.id.rb_tag_zy);
                break;
            case "2":
                rgTag.check(R.id.rb_tag_wt);
                break;
            case "3":
                rgTag.check(R.id.rb_tag_jy);
                break;
        }

        switch (str3){
            case "":
                rgTime.check(R.id.rb_add_time_type_all);
                break;
            case "1":
                rgTime.check(R.id.rb_add_time_type_before);
                break;
            case "2":
                rgTime.check(R.id.rb_add_time_type_on);
                break;
            case "3":
                rgTime.check(R.id.rb_add_time_type_after);
                break;
            case "4":
                rgTime.check(R.id.rb_add_time_type_else);
                break;
        }

        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                icon.setImageResource(R.drawable.icon_down);
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        rgCategory.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.rb_category_all:
                        category = "";
                        handler.sendEmptyMessage(101);
                        popupWindow.dismiss();
                        break;
                    case R.id.rb_category_aq:
                        category = "1";
                        handler.sendEmptyMessage(101);
                        popupWindow.dismiss();
                        break;
                    case R.id.rb_category_zt:
                        category = "2";
                        handler.sendEmptyMessage(101);
                        popupWindow.dismiss();
                        break;
                }
            }
        });

        rgTag.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.rb_tag_all:
                        tag = "";
                        handler.sendEmptyMessage(101);
                        popupWindow.dismiss();
                        break;
                    case R.id.rb_tag_zy:
                        tag = "1";
                        handler.sendEmptyMessage(101);
                        popupWindow.dismiss();
                        break;
                    case R.id.rb_tag_wt:
                        tag = "2";
                        handler.sendEmptyMessage(101);
                        popupWindow.dismiss();
                        break;
                    case R.id.rb_tag_jy:
                        tag = "3";
                        handler.sendEmptyMessage(101);
                        popupWindow.dismiss();
                        break;
                }
            }
        });

        rgTime.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.rb_add_time_type_all:
                        timeType = "";
                        handler.sendEmptyMessage(101);
                        popupWindow.dismiss();
                        break;
                    case R.id.rb_add_time_type_before:
                        timeType = "1";
                        handler.sendEmptyMessage(101);
                        popupWindow.dismiss();
                        break;
                    case R.id.rb_add_time_type_on:
                        timeType = "2";
                        handler.sendEmptyMessage(101);
                        popupWindow.dismiss();
                        break;
                    case R.id.rb_add_time_type_after:
                        timeType = "3";
                        handler.sendEmptyMessage(101);
                        popupWindow.dismiss();
                        break;
                    case R.id.rb_add_time_type_else:
                        timeType = "4";
                        handler.sendEmptyMessage(101);
                        popupWindow.dismiss();
                        break;
                }
            }
        });

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        backgroundAlpha(0.6f);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                icon.setImageResource(R.drawable.icon_down);
            }
        });
        popupWindow.setAnimationStyle(R.style.anim_pop_down_dir);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                icon.setImageResource(R.drawable.icon_down);
                backgroundAlpha(1f);
            }
        });
        // 设置好参数之后再show
        popupWindow.showAsDropDown(toolbar);

    }

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        String str = DateUtils.formatDateTime(getActivity(),
                System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
        // 下拉刷新 业务代码
        if (refreshView.isShownHeader()) {
            listView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
            listView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
            listView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
            refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新时间:" + str);
            if (paiArrayList!=null && paiArrayList.size()>0) {
                ServerUtils.getPaiList(paiArrayList.get(0).getId()+"" , "true" , timeType,category , tag , freshCallBack);
            }else {
                ServerUtils.getPaiList("" , "true" , timeType,category , tag , paiCallBack);
            }
        }

        // 上拉加载更多 业务代码
        if (refreshView.isShownFooter()) {
            listView.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
            listView.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
            listView.getLoadingLayoutProxy().setReleaseLabel("释放开始加载");
            refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后加载时间:" + str);
            if (paiArrayList!=null && paiArrayList.size()>0) {
                int index = paiArrayList.size() - 1;
                ServerUtils.getPaiList(paiArrayList.get(index).getId()+"" , "false" ,timeType, category , tag , addCallBack);
            }else {
                Utils.showToast(getContext(),"暂无内容");
            }
        }
    }

    private RequestCallBack<String> paiCallBack = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            paiArrayList.clear();
            listView.onRefreshComplete();
            String json = responseInfo.result;
            PaiData paiData = JsonUtils.jsonToPojo(json,PaiData.class);
            if (paiData != null && paiData.getStatus() == 200 && paiData.getData() != null && paiData.getData().size()>0) {
                imgNull.setVisibility(View.GONE);
                paiArrayList = (ArrayList<Pai>) paiData.getData();
                paiAdapter = new PaiAdapter(getContext(),paiArrayList);
                listView.setAdapter(paiAdapter);
                paiAdapter.setOnCheckBarClickListener(onCheckBarClickListener);
            }else {
                imgNull.setVisibility(View.VISIBLE);
                Utils.showToast(getContext(),"暂无信息");

                if (paiAdapter != null) {
                    paiAdapter.notifyDataSetChanged();
                }
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };
    private RequestCallBack<String> freshCallBack = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            listView.onRefreshComplete();
            String json = responseInfo.result;
            PaiData paiData = JsonUtils.jsonToPojo(json,PaiData.class);
            if (paiData != null && paiData.getStatus() == 200 && paiData.getData() != null && paiData.getData().size()>0) {
                ArrayList<Pai> list = (ArrayList<Pai>) paiData.getData();
                paiArrayList.addAll(0,list);
                paiAdapter.notifyDataSetChanged();
            }else {
                Utils.showToast(getContext(),"没有更新的数据啦");
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };
    private RequestCallBack<String> addCallBack = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            listView.onRefreshComplete();
            String json = responseInfo.result;
            PaiData paiData = JsonUtils.jsonToPojo(json,PaiData.class);
            if (paiData != null && paiData.getStatus() == 200 && paiData.getData() != null && paiData.getData().size()>0) {
                ArrayList<Pai> list = (ArrayList<Pai>) paiData.getData();
                paiArrayList.addAll(list);
                paiAdapter.notifyDataSetChanged();
            }else {
                Utils.showToast(getContext(),"已经到最后一页了");
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };
    private RequestCallBack<String> delCallBack = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            DemoApiJSON demoApiJSON = JsonUtils.jsonToPojo(json, DemoApiJSON.class);
            if(demoApiJSON.getStatus() == 200){
                paiAdapter.refreshMsg(paiId);
            }else{
                WinToast.toast(getActivity(), demoApiJSON.getMsg());
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 103 && data != null) {
            if (data.getBooleanExtra("isSend",false)) {
                ServerUtils.getPaiList("" , "true" ,timeType, category , tag , paiCallBack);
            }
        }
    }
}
