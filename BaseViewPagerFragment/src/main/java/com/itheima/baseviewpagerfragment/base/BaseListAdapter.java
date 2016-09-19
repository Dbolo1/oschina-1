package com.itheima.baseviewpagerfragment.base;

import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名:      BaseListAdapter
 * 创建者:    PoplarTang
 * 创建时间:  2016/9/17.
 * 描述：     TODO
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {


    List<T> datas = new ArrayList<>();

    // 上拉加载更多, 添加数据
    public void addDatas(List<T> datas) {
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    // 下拉刷新, 初始化数据
    public void setDatas(List<T> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public T getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int p) {
        return p;
    }


}
