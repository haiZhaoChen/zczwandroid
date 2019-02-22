package org.bigdata.zczw.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by darg on 2016/8/17.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }


    @Override
    public void finishUpdate(ViewGroup container) {
        try {
            super.finishUpdate(container);
        }catch (NullPointerException nullPointerException){
            Log.d("1004","Catch the NullPointerException in FragmentPagerAdapter.finishUpdate");
        }

    }

    }
