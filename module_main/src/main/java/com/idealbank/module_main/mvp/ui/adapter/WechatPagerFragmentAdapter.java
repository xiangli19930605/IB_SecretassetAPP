package com.idealbank.module_main.mvp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.idealbank.module_main.mvp.ui.fragment.ReadTagFragment;
import com.idealbank.module_main.mvp.ui.fragment.WriteTagFragment;


/**
 * Created by YoKeyword on 16/6/5.
 */
public class WechatPagerFragmentAdapter extends FragmentPagerAdapter {
    private String[] mTitles={"读标签","写标签"};
    String id;
    public WechatPagerFragmentAdapter(FragmentManager fm, String id) {
        super(fm);
        this.id = id;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return ReadTagFragment.newInstance(id);
        } else {
            return WriteTagFragment.newInstance(id);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
