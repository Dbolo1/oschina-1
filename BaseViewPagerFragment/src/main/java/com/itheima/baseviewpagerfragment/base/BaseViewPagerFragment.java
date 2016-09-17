package com.itheima.baseviewpagerfragment.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itheima.baseviewpagerfragment.R;
import com.itheima.baseviewpagerfragment.adapter.NewsPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 类名:      BaseViewPagerFragment
 * 创建者:    PoplarTang
 * 创建时间:  2016/9/17.
 * 描述：     TODO
 */
public abstract class BaseViewPagerFragment extends Fragment {

    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;

//    @BindView(R.id.tabStrip)
//    PagerSlidingTabStrip tabLayout;

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

        // 2. 创建并设置数据适配器
        mAdapter = new NewsPagerAdapter(mPager, getChildFragmentManager());

        // 3. 关联页签指示器和ViewPager
        mTabLayout.setupWithViewPager(mPager);
//        tabLayout.setViewPager(mPager);

        // 4. 更新数据适配器 (特殊)
        setupAdapter(mAdapter);

    }

    protected abstract void setupAdapter(NewsPagerAdapter adapter);
//        mAdapter.addTab("资讯", DefaultFragment.class, getBundle("咨讯的参数"));
//        mAdapter.addTab("热点", DefaultFragment.class, getBundle("热点的参数"));
//        mAdapter.addTab("博客", DefaultFragment.class, getBundle("博客的参数"));
//        mAdapter.addTab("推荐", DefaultFragment.class, getBundle("推荐的参数"));

    @NonNull
    public Bundle getBundle(String str) {
        Bundle bundle = new Bundle();
        bundle.putString("key", str);
        return bundle;
    }


}
