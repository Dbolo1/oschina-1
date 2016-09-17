package com.itheima.baseviewpagerfragment.adapter;

import android.support.annotation.NonNull;

import com.itheima.baseviewpagerfragment.base.BaseHolder;
import com.itheima.baseviewpagerfragment.base.BaseHolderAdapter;
import com.itheima.baseviewpagerfragment.domain.Blog;
import com.itheima.baseviewpagerfragment.holder.BlogHolder;

/**
 * 类名:      BlogListAdapter
 * 创建者:    PoplarTang
 * 创建时间:  2016/9/17.
 * 描述：     TODO
 */
public class BlogListAdapter extends BaseHolderAdapter<Blog> {
    @NonNull
    @Override
    public BaseHolder<Blog> getViewHolder() {
        return new BlogHolder();
    }
}
