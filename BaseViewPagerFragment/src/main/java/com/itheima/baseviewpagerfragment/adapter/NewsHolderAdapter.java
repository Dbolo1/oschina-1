package com.itheima.baseviewpagerfragment.adapter;

import android.support.annotation.NonNull;

import com.itheima.baseviewpagerfragment.base.BaseHolder;
import com.itheima.baseviewpagerfragment.base.BaseHolderAdapter;
import com.itheima.baseviewpagerfragment.domain.News;
import com.itheima.baseviewpagerfragment.holder.NewsHolder;

/**
 * 类名:      NewsHolderAdapter
 * 创建者:    PoplarTang
 * 创建时间:  2016/9/17.
 * 描述：     TODO
 */
public class NewsHolderAdapter extends BaseHolderAdapter<News> {
    @NonNull
    @Override
    public BaseHolder<News> getViewHolder() {
        return new NewsHolder();
    }
}
