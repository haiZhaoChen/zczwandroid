package org.bigdata.zczw.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by darg on 2016/8/5.
 */
public class MsgPagerAdapter extends FragmentPagerAdapter  {
    List<Fragment> fragmentList;

    public MsgPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public MsgPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList=fragmentList;

    }

    @Override
    public Fragment getItem(int position) {
        if(position<fragmentList.size()){
            return  fragmentList.get(position);
        }
        return null;
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }


}
