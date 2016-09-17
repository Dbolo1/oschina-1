package com.itheima.baseviewpagerfragment.base;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.itheima.baseviewpagerfragment.R;
import com.itheima.baseviewpagerfragment.domain.Entity;
import com.itheima.baseviewpagerfragment.domain.ListEntity;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * 类名:      BaseListFragment
 * 创建者:    PoplarTang
 * 创建时间:  2016/9/17.
 * 描述：     TODO
 */
public abstract class BaseListFragment<T extends Entity> extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.lv)
    ListView mLv;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    public BaseListAdapter<T> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_news, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 1. 查找控件
        ButterKnife.bind(this, view);

        // 2. 初始化控件
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        // 3. 创建数据适配器 (特殊)
        adapter = getListAdapter();

        // 4. 设置数据适配器
        mLv.setAdapter(adapter);

        // 5. 请求网络数据 (特殊)
        requestData();
    }

    // a.由子类创建Adapter
    @NonNull
    public abstract BaseListAdapter<T> getListAdapter();

    // b.由子类请求网络数据
    public abstract void requestData();

    // c.由子类解析数据对象
    public abstract ListEntity<T> parseData(String s);
//        return XmlUtils.toBean(NewsList.class, s);

    // d.由子类更新UI
//    public abstract void updateUI(List<T> list);
//        ((BaseListAdapter)adapter).addDatas(list);

    // 数据回调
    public StringCallback callback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int i) {
            mSwipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getActivity(), "请求失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResponse(String s, int i) {
            mSwipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getActivity(), "请求成功!", Toast.LENGTH_SHORT).show();
            // a.解析数据 (特殊)
            ListEntity<T> resultBean = parseData(s);
            List<T> list = resultBean.getList();

            // b.展示数据 (特殊)
            adapter.addDatas(list);
        }
    };



    @Override
    public void onRefresh() {
        Toast.makeText(getContext(), "下拉刷新", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestData();
            }
        }, 2000);
    }
}
