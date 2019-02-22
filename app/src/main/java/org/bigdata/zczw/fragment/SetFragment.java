package org.bigdata.zczw.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.squareup.picasso.Picasso;

import org.bigdata.zczw.App;
import org.bigdata.zczw.R;
import org.bigdata.zczw.SendGroupMsg.SendGroupActivity;
import org.bigdata.zczw.activity.AllNoticeActivity;
import org.bigdata.zczw.activity.CheckActivity;
import org.bigdata.zczw.activity.CheckListActivity;
import org.bigdata.zczw.activity.CollectActivity;
import org.bigdata.zczw.activity.ExamListPageActivity;
import org.bigdata.zczw.activity.GroupListActivity;
import org.bigdata.zczw.activity.IntegralActivity;
import org.bigdata.zczw.activity.MainActivity;
import org.bigdata.zczw.activity.RegulationsActivity;
import org.bigdata.zczw.activity.SetActivity;
import org.bigdata.zczw.activity.UserInfoActivity;
import org.bigdata.zczw.entity.AttendCount;
import org.bigdata.zczw.entity.AttendCountBean;
import org.bigdata.zczw.entity.AttendStatus;
import org.bigdata.zczw.entity.CheckStatus;
import org.bigdata.zczw.entity.Num;
import org.bigdata.zczw.entity.NumBean;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.SPUtil;
import org.bigdata.zczw.utils.ServerUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetFragment extends Fragment implements OnClickListener{

    private TextView userName;
    private ImageView userimage;
    private RelativeLayout tr_shoucang;

    private RelativeLayout test;
    private RelativeLayout money;
    private RelativeLayout msg;
    private ImageView set;
    private RelativeLayout check;
    private RelativeLayout regulation;
    private LinearLayout imgMsg;
    private ImageView toNew,redPoint;
    private TextView trendNum;
    private Num num;
    private View view = null;
    private AttendStatus status;
    private AttendCount attendCount;

    public SetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_set, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        userName = (TextView) view.findViewById(R.id.person_name);
        trendNum = (TextView) view.findViewById(R.id.txt_trend_num);
        userimage = (ImageView) view.findViewById(R.id.userImage);
        imgMsg = (LinearLayout) view.findViewById(R.id.img_add_msg_set_frg);
        toNew = (ImageView) view.findViewById(R.id.imageView_userdetail);;
        tr_shoucang = (RelativeLayout) view.findViewById(R.id.user_btn_shoucang);
        regulation = (RelativeLayout) view.findViewById(R.id.user_btn_regulation);
        test = (RelativeLayout) view.findViewById(R.id.user_btn_test);
        check = (RelativeLayout) view.findViewById(R.id.user_btn_check);

        msg = (RelativeLayout) view.findViewById(R.id.user_btn_msg);

        redPoint = (ImageView) view.findViewById(R.id.img_red_point);
        set = (ImageView) view.findViewById(R.id.user_btn_set);
        money = (RelativeLayout) view.findViewById(R.id.user_btn_money);
        tr_shoucang.setOnClickListener(this);

        test.setOnClickListener(this);
        money.setOnClickListener(this);
        imgMsg.setOnClickListener(this);
        msg.setOnClickListener(this);
        set.setOnClickListener(this);
        check.setOnClickListener(this);
        regulation.setOnClickListener(this);
        userimage.setOnClickListener(this);

        userName.setText(SPUtil.getString(getContext(), App.USER_NAME));

        ServerUtils.getBoxUnreadCount(zanCallBack);
//        check.setVisibility(View.VISIBLE);
        ServerUtils.checkStatus(needCallBack);



        registerMessageReceiver();
        return view;
    }

    private RequestCallBack<String> countCallBack = new RequestCallBack<String>() {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            AttendCountBean bean = JsonUtils.jsonToPojo(json,AttendCountBean.class);
            if (bean != null && bean.getData()!=null) {
                attendCount = bean.getData();
                if (attendCount.getLeaveCount()+attendCount.getTiaoXiuCount()+attendCount.getXiaoJiaCount()>0) {
                    redPoint.setVisibility(View.VISIBLE);
                }else {
                    redPoint.setVisibility(View.GONE);
                }
            }

        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };

    private RequestCallBack<String> zanCallBack = new RequestCallBack<String>() {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            NumBean numBean = JsonUtils.jsonToPojo(json,NumBean.class);
            if (numBean != null && numBean.getStatus() == 200) {
                if (numBean.getData()>0) {
                    trendNum.setVisibility(View.VISIBLE);
                    toNew.setVisibility(View.GONE);
                    trendNum.setText(numBean.getData()+"");
                }else {
                    trendNum.setVisibility(View.GONE);
                    toNew.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
        }
    };
    private RequestCallBack<String> needCallBack = new RequestCallBack<String>() {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            CheckStatus bean = JsonUtils.jsonToPojo(json,CheckStatus.class);
            if (bean != null && bean.getStatus() == 200) {
                status = bean.getData();
                if (status.isNeedShowStat()) {
                    check.setVisibility(View.VISIBLE);
                }else {
                    check.setVisibility(View.GONE);
                }
            }

        }

        @Override
        public void onFailure(HttpException e, String s) {
        }
    };
    @Override
    public void onStart() {
        super.onStart();
        String img = SPUtil.getString(getContext(),App.IMAGE_POSITION,"");
        if (!TextUtils.isEmpty(img)&& img.length()>5) {
            Picasso.with(getActivity()).load(img).into(userimage);
        }
        ServerUtils.attendMsgCount(countCallBack);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //消息
            case R.id.user_btn_msg:
                trendNum.setVisibility(View.GONE);
                toNew.setVisibility(View.VISIBLE);
//                Intent intent = new Intent(view.getContext(), TrendMsgActivity.class);
//                if (num!=null && num.isFlag()) {
//                    intent.putExtra("num",num);
//                }
//                startActivity(intent);

                startActivity(new Intent(view.getContext(), AllNoticeActivity.class));
                break;
            //收藏
            case R.id.user_btn_shoucang:
                startActivity(new Intent(view.getContext(), CollectActivity.class));
                break;
            //钱包
            case R.id.user_btn_money:
//                JrmfClient.intentWallet(getActivity());

                startActivity(new Intent(view.getContext(), IntegralActivity.class));

                break;
            //考试
            case R.id.user_btn_test:
                startActivity(new Intent(view.getContext(), ExamListPageActivity.class));
//                startActivity(new Intent(view.getContext(), TestListActivity.class));
                break;
            //用户信息
            case R.id.userImage:
                startActivity(new Intent(view.getContext(), UserInfoActivity.class));
                break;
            case R.id.img_add_msg_set_frg:
//                showPopupMenu(imgMsg);
                startActivity(new Intent(getContext(),CheckActivity.class));
                break;
            case R.id.user_btn_set:
                startActivity(new Intent(getContext(), SetActivity.class));
                break;
            case R.id.user_btn_check:
                if (redPoint.getVisibility() ==View.VISIBLE) {
                    redPoint.setVisibility(View.GONE);
                }

                Intent intent = new Intent(getContext(), CheckListActivity.class);
                if (attendCount != null) {
                    intent.putExtra("count",attendCount);
                }
                startActivity(intent);
                break;
            case R.id.user_btn_regulation://规章制度
                startActivity(new Intent(getContext(), RegulationsActivity.class));
                break;
        }
    }


    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.de_main_menu, popupMenu.getMenu());      // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add_item1:// 群发消息
                        startActivity(new Intent(getContext(), SendGroupActivity.class));
                        break;
                    case R.id.add_item2://发起群聊
                        Intent intent = new Intent(getContext(),GroupListActivity.class);
                        intent.putExtra("group",true);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }
    private MessageReceiver mMessageReceiver;
    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MainActivity.MESSAGE_RECEIVED_ACTION);
        getContext().registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MainActivity.MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String extras = intent.getStringExtra(MainActivity.KEY_EXTRAS);
                if (!TextUtils.isEmpty(extras) && extras.contains("MessageBox")) {
                    Num num = JsonUtils.jsonToPojo(extras,Num.class);
                    if (num != null) {
                        if (num.getMsgType().equals("MessageBox")) {
                            trendNum.setVisibility(View.VISIBLE);
                            toNew.setVisibility(View.GONE);
                            trendNum.setText(num.getCount()+"");
                        }else {
                            trendNum.setVisibility(View.GONE);
                            toNew.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        }
    }

}
