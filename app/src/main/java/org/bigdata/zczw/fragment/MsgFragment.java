package org.bigdata.zczw.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;

import org.bigdata.zczw.R;
import org.bigdata.zczw.SendGroupMsg.SendGroupActivity;
import org.bigdata.zczw.activity.GroupListActivity;
import org.bigdata.zczw.adapter.MsgPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MsgFragment extends Fragment implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private ViewPager viewPager;
    private RadioButton msg;
    private RadioButton friend;
    private ImageView imgMsg;

    private List<Fragment> fragmentList=new ArrayList<Fragment>();
    private MsgPagerAdapter msgPagerAdapter;
    private MyListFragment myListFragment;
    private FriendFragment friendFragment;

    public MsgFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_msg, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (friend.isChecked()) {
            viewPager.setCurrentItem(1);
        }else{
            viewPager.setCurrentItem(0);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = (ViewPager) view.findViewById(R.id.vp_frg_msg);
        friend = (RadioButton) view.findViewById(R.id.rb_friend_msg_frg);
        msg = (RadioButton) view.findViewById(R.id.rb_msg_msg_frg);
        imgMsg = (ImageView) view.findViewById(R.id.img_add_msg_frg);

        friendFragment = new FriendFragment();
        myListFragment = new MyListFragment();
        fragmentList.add(myListFragment);
        fragmentList.add(friendFragment);

        msgPagerAdapter = new MsgPagerAdapter(getChildFragmentManager(),fragmentList);
        viewPager.setAdapter(msgPagerAdapter);

        viewPager.addOnPageChangeListener(this);
        msg.setOnClickListener(this);
        friend.setOnClickListener(this);
        imgMsg.setOnClickListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            msg.setChecked(true);
        }else{
            friend.setChecked(true);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rb_msg_msg_frg:
                viewPager.setCurrentItem(0);
                break;
            case R.id.rb_friend_msg_frg:
                viewPager.setCurrentItem(1);
                break;
            case R.id.img_add_msg_frg:
                showPopupMenu(imgMsg);
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
}
