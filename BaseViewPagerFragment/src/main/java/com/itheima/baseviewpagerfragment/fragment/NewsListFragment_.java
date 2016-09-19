package com.itheima.baseviewpagerfragment.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.itheima.baseviewpagerfragment.R;
import com.itheima.baseviewpagerfragment.adapter.NewsAdapter;
import com.itheima.baseviewpagerfragment.base.BaseListAdapter;
import com.itheima.baseviewpagerfragment.domain.News;
import com.itheima.baseviewpagerfragment.domain.NewsList;
import com.itheima.baseviewpagerfragment.utils.XmlUtils;
import com.zhy.http.okhttp.OkHttpUtils;
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
public class NewsListFragment_ extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.lv)
    ListView mLv;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private BaseListAdapter adapter;

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
        adapter = new NewsAdapter();

        // 4. 设置数据适配器
        mLv.setAdapter(adapter);

        // 5. 请求网络数据 (特殊)
        Toast.makeText(getContext(), "开始请求数据~", Toast.LENGTH_SHORT).show();
        // http://www.oschina.net/action/api/news_list?pageIndex=0&catalog=1&pageSize=20
        OkHttpUtils
                .get()
                .url("http://www.oschina.net/action/api/news_list")
                .addParams("pageIndex", pageIndex + "")
                .addParams("catalog", catalog + "")
                .addParams("pageSize", pageSize + "")
                .build()
                .execute(callback);
    }

    int pageIndex = 0; // 页索引
    int catalog = 1; // 分类
    int pageSize = 20; // 页大小
    // 数据回调
    public StringCallback callback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int i) {
            Toast.makeText(getActivity(), "请求失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResponse(String s, int i) {
            Toast.makeText(getActivity(), "请求成功!", Toast.LENGTH_SHORT).show();
            // a.解析数据
            NewsList newsList = XmlUtils.toBean(NewsList.class, s);
            List<News> list = newsList.getList();

            // b.展示数据
            adapter.addDatas(list);
        }
    };

    @Override
    public void onRefresh() {
        Toast.makeText(getContext(), "下拉刷新", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }
}
