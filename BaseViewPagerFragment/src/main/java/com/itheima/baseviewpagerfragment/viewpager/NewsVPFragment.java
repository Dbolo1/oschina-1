package com.itheima.baseviewpagerfragment.viewpager;

import com.itheima.baseviewpagerfragment.fragment.DefaultFragment;

/**
 * 类名:      NewsVPFragment
 * 创建者:    PoplarTang
 * 创建时间:  2016/9/17.
 * 描述：     TODO
 */
public class NewsVPFragment extends BaseViewPagerFragment {
    @Override
    protected void setupAdapter(NewsPagerAdapter adapter) {
        adapter.addTab("资讯", DefaultFragment.class, getBundle("咨讯的参数"));
        adapter.addTab("热点", DefaultFragment.class, getBundle("热点的参数"));
        adapter.addTab("博客", DefaultFragment.class, getBundle("博客的参数"));
        adapter.addTab("推荐", DefaultFragment.class, getBundle("推荐的参数"));
    }
}
