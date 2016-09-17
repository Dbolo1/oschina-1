package com.itheima.baseviewpagerfragment.fragment;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.itheima.baseviewpagerfragment.adapter.NewsHolderAdapter;
import com.itheima.baseviewpagerfragment.api.NetApi;
import com.itheima.baseviewpagerfragment.base.BaseListAdapter;
import com.itheima.baseviewpagerfragment.base.BaseListFragment;
import com.itheima.baseviewpagerfragment.domain.ListEntity;
import com.itheima.baseviewpagerfragment.domain.News;
import com.itheima.baseviewpagerfragment.domain.NewsList;
import com.itheima.baseviewpagerfragment.utils.XmlUtils;

/**
 * 类名:      NewsListFragment
 * 创建者:    PoplarTang
 * 创建时间:  2016/9/17.
 * 描述：     TODO
 */
public class NewsListFragment extends BaseListFragment<News> {


    int pageIndex = 0; // 页索引
    int catalog = 1; // 分类
    int pageSize = 20; // 页大小

    @NonNull
    @Override
    public BaseListAdapter<News> getListAdapter() {
        return new NewsHolderAdapter();
    }

    @Override
    public void requestData() {
        Toast.makeText(getContext(), "开始请求数据~", Toast.LENGTH_SHORT).show();
        NetApi.getNewsList(pageIndex, catalog, pageSize, callback);
    }

    @Override
    public ListEntity<News> parseData(String s) {
        return XmlUtils.toBean(NewsList.class, s);
    }

}
