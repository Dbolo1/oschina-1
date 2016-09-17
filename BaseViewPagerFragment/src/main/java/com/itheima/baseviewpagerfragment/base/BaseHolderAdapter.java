package com.itheima.baseviewpagerfragment.base;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

/**
 * 类名:      NewsAdapter
 * 创建者:    PoplarTang
 * 创建时间:  2016/9/17.
 * 描述：     TODO
 */
public abstract class BaseHolderAdapter<T> extends BaseListAdapter<T> {

    // parent即为ListView
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseHolder holder;
        if (convertView == null) {
            // 创建ViewHolder
            holder = getViewHolder();
            holder.initViewHolder(parent.getContext(), position);
        } else {
            holder = (BaseHolder) convertView.getTag();
        }

        T item = getItem(position);
        // 绑定ViewHolder
        holder.bindView(item);

        View view = holder.getView();
        return view;
    }

    @NonNull
    public abstract BaseHolder<T> getViewHolder();
}
