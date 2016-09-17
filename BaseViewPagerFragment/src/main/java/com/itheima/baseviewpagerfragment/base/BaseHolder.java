package com.itheima.baseviewpagerfragment.base;

import android.content.Context;
import android.view.View;

import butterknife.ButterKnife;

public abstract class BaseHolder<T> {

    View view; // 条目根布局

    public void initViewHolder(Context context, int position) {
        view = onCreateViewHolder(context);
        ButterKnife.bind(this, view);
        view.setTag(this);
    }

    public abstract View onCreateViewHolder(Context context);

    public abstract void bindView(T item);

//            holder.mIvImage.setImageResource(R.mipmap.ic_launcher);
//            holder.mTvContent.setText(item.getTitle());

    public View getView() {
        return view;
    }

}