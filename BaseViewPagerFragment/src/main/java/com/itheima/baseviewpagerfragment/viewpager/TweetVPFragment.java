package com.itheima.baseviewpagerfragment.viewpager;

import com.itheima.baseviewpagerfragment.R;
import com.itheima.baseviewpagerfragment.fragment.DefaultFragment;

/**
 * 类名:      TweetVPFragment
 * 创建者:    PoplarTang
 * 创建时间:  2016/9/17.
 * 描述：     TODO
 */
public class TweetVPFragment extends BaseViewPagerFragment {
    @Override
    protected void setupAdapter(NewsPagerAdapter adapter) {
        String[] array = getResources().getStringArray(R.array.tweets_viewpage_arrays);

        adapter.addTab(array[0], DefaultFragment.class, getBundle("最新动弹的参数"));
        adapter.addTab(array[1], DefaultFragment.class, getBundle("热门动弹的参数"));
        adapter.addTab(array[2], DefaultFragment.class, getBundle("我的动弹的参数"));
    }
}
