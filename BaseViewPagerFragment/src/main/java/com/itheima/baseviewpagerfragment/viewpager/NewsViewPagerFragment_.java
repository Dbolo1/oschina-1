package com.itheima.baseviewpagerfragment.viewpager;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itheima.baseviewpagerfragment.R;
import com.itheima.baseviewpagerfragment.fragment.DefaultFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 类名:      BaseViewPagerFragment
 * 创建者:    PoplarTang
 * 创建时间:  2016/9/17.
 * 描述：     TODO
 */
public class NewsViewPagerFragment_ extends Fragment {

    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.pager)
    ViewPager mPager;
    private NewsPagerAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_viewpager_news, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 1. 查找关心的控件
        ButterKnife.bind(this, view);

        // 2. 设置数据适配器
        mAdapter = new NewsPagerAdapter(mPager, getChildFragmentManager());
        mPager.setAdapter(mAdapter);

        // 3. 关联页签指示器和ViewPager
        mTabLayout.setupWithViewPager(mPager);

        // 4. 更新数据适配器 (特殊)
        mAdapter.addTab("资讯", DefaultFragment.class, getBundle("咨讯的参数"));
        mAdapter.addTab("热点", DefaultFragment.class, getBundle("热点的参数"));
        mAdapter.addTab("博客", DefaultFragment.class, getBundle("博客的参数"));
        mAdapter.addTab("推荐", DefaultFragment.class, getBundle("推荐的参数"));

    }

    @NonNull
    private Bundle getBundle(String str) {
        Bundle bundle = new Bundle();
        bundle.putString("key", str);
        return bundle;
    }

    public class NewsPagerAdapter extends FragmentStatePagerAdapter {

        List<PagerTab> tabs = new ArrayList<>();
        private final Context mContext;

        public NewsPagerAdapter(ViewPager mPager, FragmentManager fm) {
            super(fm);
            mContext =  mPager.getContext();

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
}
