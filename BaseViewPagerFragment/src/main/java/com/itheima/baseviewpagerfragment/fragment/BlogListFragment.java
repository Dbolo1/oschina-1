package com.itheima.baseviewpagerfragment.fragment;

import android.support.annotation.NonNull;

import com.itheima.baseviewpagerfragment.adapter.BlogListAdapter;
import com.itheima.baseviewpagerfragment.api.NetApi;
import com.itheima.baseviewpagerfragment.base.BaseListAdapter;
import com.itheima.baseviewpagerfragment.base.BaseListFragment;
import com.itheima.baseviewpagerfragment.domain.Blog;
import com.itheima.baseviewpagerfragment.domain.BlogList;
import com.itheima.baseviewpagerfragment.domain.ListEntity;
import com.itheima.baseviewpagerfragment.utils.XmlUtils;

/**
 * 类名:      BlogListFragment
 * 创建者:    PoplarTang
 * 创建时间:  2016/9/17.
 * 描述：     TODO
 */
public class BlogListFragment extends BaseListFragment<Blog> {
    @NonNull
    @Override
    public BaseListAdapter<Blog> getListAdapter() {
        return new BlogListAdapter();
    }

    int pageIndex = 0; // 页索引
    int pageSize = 20; // 页大小
    String type = "latest"; // 分类
//    http://www.oschina.net/action/api/blog_list?pageIndex=0&pageSize=20&type=latest
    @Override
    public void requestData() {
        NetApi.getBlogList(pageIndex, pageSize, type, callback);
    }

    @Override
    public ListEntity<Blog> parseData(String s) {
        return XmlUtils.toBean(BlogList.class, s);
    }
}
