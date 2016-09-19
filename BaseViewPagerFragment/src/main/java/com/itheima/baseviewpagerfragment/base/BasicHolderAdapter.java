package com.itheima.baseviewpagerfragment.base;

import android.support.annotation.NonNull;

/**
 * 类名:      BasicHolderAdapter
 * 创建者:    PoplarTang
 * 创建时间:  2016/9/18.
 * 描述：     TODO
 */
public class BasicHolderAdapter<T> extends BaseHolderAdapter<T> {


    private Class clazz;

    public BasicHolderAdapter(Class clazz) {
        super();
        this.clazz = clazz;
    }

    @NonNull
    @Override
    public BaseHolder<T> getViewHolder() {
        try {
            return (BaseHolder<T>) clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
