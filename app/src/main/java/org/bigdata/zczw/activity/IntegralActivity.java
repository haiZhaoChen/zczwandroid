package org.bigdata.zczw.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.squareup.picasso.Picasso;

import org.bigdata.zczw.R;
import org.bigdata.zczw.adapter.IntegralListAdapter;
import org.bigdata.zczw.entity.IntegralListModel;
import org.bigdata.zczw.entity.IntegralMapModel;
import org.bigdata.zczw.entity.IntegralStatusModel;
import org.bigdata.zczw.entity.IntegralStatusModelBean;
import org.bigdata.zczw.entity.IntegralUserBean;
import org.bigdata.zczw.ui.WinToast;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.ServerUtils;
import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

public class IntegralActivity extends AppCompatActivity implements View.OnClickListener,PullToRefreshBase.OnRefreshListener<ListView>{


    private PullToRefreshListView listView;
    private TextView rulesText;
    private ImageView reBtn,icon;
    private LinearLayout typeLable;
    private PopupWindow popupWindow;
    private Toolbar toolbar;
    private TextView homeText;

    private View contentView,backView;

    private int rankType,type,showType,popViewType;
    private IntegralStatusModel statusModel;
    private String rankId;
    private ArrayList<IntegralListModel> dataList;

    private RadioGroup rgCategory1;
    private RadioButton button1;
    private RadioButton button2;
    private RadioButton button3;
    private RadioButton button4;
    private RadioButton button5;
    private RadioButton button6;
    private RadioButton button7;
    private ArrayList<String> typeNameList;
    private ArrayList<String> typeValueList;
    //通过这个固定数组判断id是几，
    private ArrayList<String> typeSendList;
    private ArrayList<RadioButton> buttons;
    //点击详情
    private TextView meRankNum;
    private TextView meNameLB;
    private TextView meScore;
    private ImageView meIcon;
    private Button buttonInfo;
    private LinearLayout bottomView;

//    private String rankType;
//    private String unitType;
    private IntegralListAdapter integralListAdapter;
    private IntegralListModel integralListModel;



    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 101:
                    icon.setImageResource(R.drawable.icon_down);
                    ServerUtils.getScoreList(rankType+"" , type+"","", listCallBack);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_integral_list);
        //
        getSupportActionBar().hide();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);
        AppManager.getAppManager().addActivity(this);
        //初始化页面和数据
        initView();
        initData();
    }

    private void initView(){
        listView = (PullToRefreshListView)findViewById(R.id.integral_plistview);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        listView.setOnRefreshListener(this);
        listView.setScrollingWhileRefreshingEnabled(true);

        rulesText = (TextView)findViewById(R.id.integral_rules);
        homeText= (TextView)findViewById(R.id.txt_type_feed_frg);
        reBtn = (ImageView) findViewById(R.id.integral_back_home);
        typeLable = (LinearLayout) findViewById(R.id.ll_type_name_lable);
        icon = (ImageView)findViewById(R.id.img_icon_feed_frg);
        contentView = LayoutInflater.from(this).inflate(R.layout.pop_integral_type, null);
        rgCategory1= (RadioGroup) contentView.findViewById(R.id.integral_type1);
        button1 = (RadioButton)contentView.findViewById(R.id.integral_type_all);
        button2 = (RadioButton)contentView.findViewById(R.id.integral_type_job);
        button3 = (RadioButton)contentView.findViewById(R.id.integral_type_unit);
        button4 = (RadioButton)contentView.findViewById(R.id.integral_type_4);
        button5 = (RadioButton)contentView.findViewById(R.id.integral_type_5);
        button6 = (RadioButton)contentView.findViewById(R.id.integral_type_6);
        button7 = (RadioButton)contentView.findViewById(R.id.integral_type_7);
        buttons = new ArrayList<>();
        buttons.add(button1);
        buttons.add(button2);
        buttons.add(button3);
        buttons.add(button4);
        buttons.add(button5);
        buttons.add(button6);
        buttons.add(button7);

        bottomView = (LinearLayout)findViewById(R.id.integral_bottom_bar);;
        buttonInfo = (Button) findViewById(R.id.score_info);
        buttonInfo.setOnClickListener(this);

        //我的信息，详情
        meRankNum = (TextView) findViewById(R.id.integral_me_rank_num);
        meNameLB = (TextView) findViewById(R.id.integral_me_name);
        meScore = (TextView) findViewById(R.id.integral_me_score);
        meIcon = (ImageView) findViewById(R.id.integral_me_icon);

        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        toolbar = (Toolbar)findViewById(R.id.tool_integral_frg);
        rulesText.setOnClickListener(this);
        typeLable.setOnClickListener(this);
        reBtn.setOnClickListener(this);
    }
    private void initData(){


        /*
        * 0：公共
1：养护
2：收费
3：机电
4：信调
5: 机关科室
6：处属单位
        * */
        dataList = new ArrayList<IntegralListModel>();
        rankType = 2;
        type = 0;
        rankId = "";
        popViewType = 0;
        typeSendList = new ArrayList<String>(6);
        typeSendList.add("公共");
        typeSendList.add("养护");
        typeSendList.add("收费");
        typeSendList.add("机电");
        typeSendList.add("信调");
        typeSendList.add("机关科室");

        //获取当前状态
        ServerUtils.getIntegralStatus(statusCallBack);



    }

    /**
     * 请求用户状态
     * @param v
     */
    private RequestCallBack<String> statusCallBack = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {

            String json = responseInfo.result;
            IntegralStatusModelBean bean = JsonUtils.jsonToPojo(json,IntegralStatusModelBean.class);
            if (bean != null && bean.getStatus() == 200 && bean.getData()!=null) {

                statusModel = bean.getData();

                //后台返回来的类型：普通，专业，领导，部门号
                typeNameList = new ArrayList<>();
                typeValueList = new ArrayList<>();
                String typeName = statusModel.getUnits();

                int typeSize=3;
                showType = statusModel.getUserType();

                if (showType != 4){
                    //获取本年度，公共分
                    type = 0;
                    ServerUtils.getScoreList(rankType+"" , type+"",rankId, listCallBack);
                }else {
                    //获取本年度，公共分
                    type = 5;
                    ServerUtils.getScoreList(rankType+"" , type+"",rankId, listCallBack);
                    String[] nameTime = {"","近30天","年度"};
                    String[] nametype = {"公共","养护","收费","机电","信调","机关科室","处属单位"};
                    String name = nametype[type]+nameTime[rankType]+"排名";
                    homeText.setText(name);


                }

                switch (showType){
                    case 1:
                        //普通员工
                        typeSize = 3;
                        //少于四条改变UI/一个公共两个部门
                        typeNameList.add("公共");
                        typeNameList.add("机关科室");
                        typeNameList.add("处属单位");
                        typeValueList.add("1");
                        typeValueList.add("2");
                        typeValueList.add("3");

                        button4.setText("");
                        button4.setBackground(null);

                        for(int i=0;i<typeSize;i++){
                            buttons.get(i).setText(typeNameList.get(i));
                        }

                        break;
                    case 2:
                        //专业员工
                        typeSize = 4;
                        typeNameList.add("公共");
                        typeNameList.add(typeName);
                        typeNameList.add("机关科室");
                        typeNameList.add("处属单位");
                        typeValueList.add("1");
                        typeValueList.add("2");
                        typeValueList.add("3");
                        typeValueList.add("4");

                        for(int i=0;i<typeSize;i++){
                            buttons.get(i).setText(typeNameList.get(i));
                        }

                        break;
                    case 3:
                        //处级领导
                        typeSize = 7;
                        typeNameList.add("公共");
                        typeNameList.add("养护");
                        typeNameList.add("收费");
                        typeNameList.add("机电");
                        typeNameList.add("信调");
                        typeNameList.add("机关科室");
                        typeNameList.add("处属单位");
                        typeValueList.add("1");
                        typeValueList.add("2");
                        typeValueList.add("3");
                        typeValueList.add("4");
                        typeValueList.add("5");
                        typeValueList.add("6");
                        typeValueList.add("7");

                        for(int i=0;i<typeSize;i++){
                            buttons.get(i).setText(typeNameList.get(i));
                        }

                        break;
                    case 4:
                        //部门账号
                        typeSize = 2;
                        typeNameList.add("机关科室");
                        typeNameList.add("处属单位");
                        typeValueList.add("2");
                        typeValueList.add("3");

                        button3.setText("");
                        button3.setBackground(null);
                        button4.setText("");
                        button4.setBackground(null);

                        for(int i=0;i<typeSize;i++){
                            buttons.get(i).setText(typeNameList.get(i));
                        }
                        break;

                }

                if (typeSize<=4){
                    rgCategory1.setVisibility(View.GONE);
                }else {
                    rgCategory1.setVisibility(View.VISIBLE);
                }

            }

        }

        @Override
        public void onFailure(HttpException e, String s) {
            System.out.println(s);

        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_type_name_lable:

                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    icon.setImageResource(R.drawable.icon_down);
                }else {
                    showPopWindow(popViewType,rankType);
                    icon.setImageResource(R.drawable.icon_up);
                }
                break;
            case R.id.score_info:
                Intent intent = new Intent(IntegralActivity.this,IntegralScoreActivity.class);
                intent.putExtra("integralModel",integralListModel);
                int typeId = 1;
                if (type == 0){
                    typeId = 1;
                }else if(type > 0 && type <5){
                    typeId = 2;
                }else if(type >= 5){
                    typeId = 3;
                }
                intent.putExtra("type",typeId);
                startActivity(intent);
                break;
            case R.id.integral_back_home:
                finish();
                break;
        }
    }

    private void showPopWindow(int tagStr,int timeTypeStr){
        // 一个自定义的布局，作为显示的内容
        final RadioGroup rgCategory= (RadioGroup) contentView.findViewById(R.id.integral_type);
        RadioGroup rgTime= (RadioGroup) contentView.findViewById(R.id.integral_time);

        LinearLayout linearLayout = (LinearLayout) contentView.findViewById(R.id.integral_black);


        switch (tagStr){
            case 0:
                rgCategory.check(R.id.integral_type_all);
                break;
            case 1:
                rgCategory.check(R.id.integral_type_job);
                break;
            case 2:
                rgCategory.check(R.id.integral_type_unit);
                break;
            case 3:
                rgCategory.check(R.id.integral_type_4);
                break;
            case 4:
                rgCategory1.check(R.id.integral_type_5);
                break;
            case 5:
                rgCategory1.check(R.id.integral_type_6);
                break;
            case 6:
                rgCategory1.check(R.id.integral_type_7);
                break;

        }

        switch (timeTypeStr){
            case 2:
                rgTime.check(R.id.integral_time_week);
                break;
            case 1:
                rgTime.check(R.id.integral_time_month);
                break;
            case 0:
                rgTime.check(R.id.integral_time_year);
                break;
            case 3:
                rgTime.check(R.id.integral_time_all);
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

                    case R.id.integral_type_all:
                        if (button1.isChecked())
                            rgCategory1.clearCheck();

                        popViewType =0;

                        if (showType == 4){
                            type = 5;
                        }else {
                            type = 0;
                        }

                        handler.sendEmptyMessage(101);
                        popupWindow.dismiss();
                        break;
                    case R.id.integral_type_job:
                        if (button2.isChecked())
                            rgCategory1.clearCheck();

                        popViewType =1;
                        if (showType == 1){
                            type = 5;
                        }else if (showType == 2){
                            int index =typeSendList.indexOf(statusModel.getUnits());
                            if (index>0 &&index<5){
                                type = index;
                            }else {
                                type = 1;
                            }

                        }else if(showType == 3) {
                            type = 1;
                        }else {
                            type = 6;
                        }

                        handler.sendEmptyMessage(101);
                        popupWindow.dismiss();
                        break;
                    case R.id.integral_type_unit:
                        if (button3.isChecked())
                            rgCategory1.clearCheck();

                        popViewType =2;
                        if (showType == 1){
                            type = 6;
                        }else if (showType == 2){
                            type=5;

                        }else if(showType == 3) {
                            type = 2;
                        }
                        handler.sendEmptyMessage(101);
                        popupWindow.dismiss();
                        break;
                    case R.id.integral_type_4:
                        if (button4.isChecked())
                            rgCategory1.clearCheck();
                        popViewType =3;
                        if (showType == 2){
                            type=6;

                        }else if(showType == 3) {
                            type = 3;
                        }
                        handler.sendEmptyMessage(101);
                        popupWindow.dismiss();
                        break;
                }

                String[] nameTime = {"","近30天","年度"};
                String[] nametype = {"公共","养护","收费","机电","信调","机关科室","处属单位"};
                String name = nametype[type]+nameTime[rankType]+"排名";
                homeText.setText(name);
            }
        });

        rgCategory1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){

                    case R.id.integral_type_5:
                        if (button5.isChecked())
                            rgCategory.clearCheck();
                        type = 4;
                        popViewType = 4;
                        handler.sendEmptyMessage(101);
                        popupWindow.dismiss();
                        break;
                    case R.id.integral_type_6:
                        if (button6.isChecked())
                            rgCategory.clearCheck();
                        type = 5;
                        popViewType = 5;
                        handler.sendEmptyMessage(101);
                        popupWindow.dismiss();
                        break;
                    case R.id.integral_type_7:
                        if (button7.isChecked())
                            rgCategory.clearCheck();
                        type = 6;
                        popViewType = 6;
                        handler.sendEmptyMessage(101);
                        popupWindow.dismiss();
                        break;

                }


                String[] nameTime = {"","近30天","年度"};
                String[] nametype = {"公共","养护","收费","机电","信调","机关科室","处属单位"};
                String name = nametype[type]+nameTime[rankType]+"排名";
                homeText.setText(name);
            }
        } );

        rgTime.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.integral_time_week:
                        rankType = 2;
                        handler.sendEmptyMessage(101);
                        popupWindow.dismiss();
                        break;
                    case R.id.integral_time_month:
                        rankType = 1;
                        handler.sendEmptyMessage(101);
                        popupWindow.dismiss();
                        break;
                    case R.id.integral_time_year:
                        rankType = 0;
                        handler.sendEmptyMessage(101);
                        popupWindow.dismiss();
                        break;
                    case R.id.integral_time_all:
                        rankType = 3;
                        handler.sendEmptyMessage(101);
                        popupWindow.dismiss();
                        break;
                }


                String[] nameTime = {"","近30天","年度"};
                String[] nametype = {"公共","养护","收费","机电","信调","机关科室","处属单位"};
                String name = nametype[type]+nameTime[rankType]+"排名";
                homeText.setText(name);
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
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    private RequestCallBack<String> listCallBack = new RequestCallBack<String>() {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {

            String json = responseInfo.result;
            IntegralUserBean bean = JsonUtils.jsonToPojo(json,IntegralUserBean.class);
            if (bean != null && bean.getStatus() == 200 && bean.getData()!=null){
                IntegralMapModel model = bean.getData();
                ArrayList<IntegralListModel> userList = model.getAll();
                if (userList.size()>0){
                    dataList = userList;
                    integralListModel = model.getMe().size()>0?model.getMe().get(0):null;
                    if (integralListModel != null && integralListModel.getName() != null){
                        bottomView.setVisibility(View.VISIBLE);
                        //显示数据
                        meRankNum.setText(integralListModel.getId()+"");
                        meNameLB.setText(integralListModel.getName());
                        meScore.setText(integralListModel.getPublicIntegral()+"积分");
                        if (!TextUtils.isEmpty(integralListModel.getPortrait())) {
                            Picasso.with(contentView.getContext()).load(integralListModel.getPortrait()).into(meIcon);
                        }else {
                            meIcon.setImageResource(R.drawable.de_default_portrait);
                        }

                    }else {
                        bottomView.setVisibility(View.GONE);
                    }

                    integralListAdapter = new IntegralListAdapter(IntegralActivity.this,dataList);
                    listView.setAdapter(integralListAdapter);
                }else {
                    WinToast.toast(getApplicationContext(), "没有排名数据");
                }

            }else {

                WinToast.toast(getApplicationContext(), "服务器错误");
            }


        }

        @Override
        public void onFailure(HttpException e, String s) {
            System.out.println(s+"==="+e.getLocalizedMessage());

        }
    };

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {

        // 下拉刷新 业务代码
        if (refreshView.isShownFooter()) {
            listView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
            listView.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
            listView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
            String lastId = null;
            if (integralListAdapter != null) {
                lastId = integralListAdapter.getLastId();
            }
            if (lastId != null) {
                rankId = lastId+"";
                //获取本年度，公共分
                ServerUtils.getScoreList(rankType+"" , type+"",rankId, pushListCallBack);
            } else {
                listView.onRefreshComplete();
            }


        }
    }

    private RequestCallBack<String> pushListCallBack = new RequestCallBack<String>() {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {

            String json = responseInfo.result;
            IntegralUserBean bean = JsonUtils.jsonToPojo(json,IntegralUserBean.class);
            if (bean != null && bean.getStatus() == 200 && bean.getData()!=null){
                IntegralMapModel model = bean.getData();
                ArrayList<IntegralListModel> userList = model.getAll();
                if (userList.size()>0){
                    dataList.addAll(userList);
                    integralListAdapter.notifyDataSetChanged();
                }else {
                    WinToast.toast(getApplicationContext(), "已经到最后一页了");
                }



            }else {

                WinToast.toast(getApplicationContext(), "服务器错误");
            }

            listView.onRefreshComplete();


        }

        @Override
        public void onFailure(HttpException e, String s) {
            System.out.println(s+"==="+e.getLocalizedMessage());
            listView.onRefreshComplete();

        }
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
