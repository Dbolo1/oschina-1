package com.itheima.baseviewpagerfragment.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class BasicPagerAdapter extends FragmentPagerAdapter {

    List<PagerTab> tabs = new ArrayList<>();
    private final Context mContext;

    public BasicPagerAdapter(ViewPager mPager, FragmentManager fm) {
        super(fm);
        mContext = mPager.getContext();

        mPager.setAdapter(this);

    }

    public void addTab(String title, Class<?> clazz, Bundle bundle) {
        tabs.add(new PagerTab(title, clazz, bundle));
        // 通知数据适配器更新
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        PagerTab pagerTab = tabs.get(position);

        return Fragment.instantiate(mContext, pagerTab.clazz.getName(), pagerTab.bundle);
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position).title;
    }

    class PagerTab {
        String title;
        Class<?> clazz;
        Bundle bundle;

        public PagerTab(String title, Class<?> clazz, Bundle bundle) {
            this.title = title;
            this.clazz = clazz;
            this.bundle = bundle;
        }
    }
}