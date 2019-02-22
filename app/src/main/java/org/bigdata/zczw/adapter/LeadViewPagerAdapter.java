package org.bigdata.zczw.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.bigdata.zczw.fragment.LeadImageFragment;

import java.util.List;

/**
 * Created by darg on 2016/6/7.
 */
public class LeadViewPagerAdapter extends FragmentPagerAdapter {

    private List<LeadImageFragment> list;

    public LeadViewPagerAdapter(FragmentManager fm, List<LeadImageFragment> list){
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }
}
