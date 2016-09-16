package com.itheima.oschina.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.itheima.oschina.R;
import com.itheima.oschina.bean.News;
import com.itheima.oschina.bean.NewsList;
import com.itheima.oschina.bean.TweetsList;
import com.itheima.oschina.util.XmlUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import java.io.IOException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;
import okhttp3.Response;

/**
 *
 */
public class DefaultFragment extends Fragment {
    private String mParam1;

    @InjectView(R.id.tv_content)
    TextView tv_content;
    private RequestCall requestCall;


    public DefaultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString("key");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_default, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
        System.out.println(mParam1 + " - 创建完毕");
        initView(view);
    }

    private void initView(View view) {

        if (TextUtils.isEmpty(mParam1)) {
            tv_content.setText("我是一个测试用的Fragment, 我创建的时候没有传进来Bundle, 所以显示这个内容.");
        } else {
            tv_content.setText(mParam1);
        }
//        http://www.oschina.net/action/api/news_list?pageIndex=0&catalog=1&pageSize=20
        // 执行get请求

        // 同步框架  阻塞线程网络请求 + 子线程(线程池) + 线程切换Handler
        requestData();

        // 异步框架
        requestDataAsync();


    }

    private void requestDataAsync() {
        // http://www.oschina.net/action/api/tweet_list?uid=0&pageIndex=0&pageSize=20
        OkHttpUtils
                .post()
                .url("http://www.oschina.net/action/api/tweet_list")
                .addParams("uid", 0 + "")
                .addParams("pageIndex", 0 + "")
                .addParams("pageSize", 20 + "")
                .id(101)
                .build()
                .execute(callback);

        OkHttpUtils
                .get()
                .url("http://www.oschina.net/action/api/news_list")
                .addParams("pageIndex", 0 + "")
                .addParams("catalog", 1 + "")
                .addParams("pageSize", 20 + "")
                .id(102)
                .build()
                .execute(callback);

    }
    StringCallback callback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            // 错误, 主线程
        }

        @Override
        public void onResponse(String s, int id) {
            if(101 == id){
                // 成功, 主线程

                // 动弹
                TweetsList list = XmlUtils.toBean(TweetsList.class, s);
                tv_content.setText("list: " + list.getList().size());

            }else if(102 == id){
                // 资讯
                // 将网络请求到的数据转成javaBean
                NewsList newsList = XmlUtils.toBean(NewsList.class, s);
                tv_content.setText("list: " + newsList.getList().size());
            }

        }
    };

    private void requestData() {
        new Thread(){
            @Override
            public void run() {
                try {
                    Response response = OkHttpUtils
                            .get()
                            .url("http://www.oschina.net/action/api/news_list")
                            .addParams("pageIndex", 0 + "")
                            .addParams("catalog", 1 + "")
                            .addParams("pageSize", 20 + "")
                            .build()
                            .execute();

                    final String string = response.body().string();
                    System.out.println("string: " + string);

                    // 将网络请求到的数据转成javaBean
                    NewsList newsList = XmlUtils.toBean(NewsList.class, string);

                    final List<News> list = newsList.getList();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_content.setText(list.size() + "->" + list.get(0).getTitle());

                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if(requestCall != null){
//            requestCall.cancel();
//        }
    }
}
