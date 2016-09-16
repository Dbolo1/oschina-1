package com.itheima.oschina.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;
import okhttp3.Response;
import okhttp3.internal.Util;

import com.itheima.oschina.R;
import com.itheima.oschina.bean.News;
import com.itheima.oschina.bean.NewsDetail;
import com.itheima.oschina.bean.NewsList;
import com.itheima.oschina.util.XmlUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

/**
 *
 */
public class DefaultFragment extends Fragment {
    private String mParam1;

    @InjectView(R.id.tv_content)
    TextView tv_content;

    @InjectView(R.id.wv)
    WebView wv;

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
        WebSettings settings = wv.getSettings();
        settings.setJavaScriptEnabled(true); // 设置支持js

        if (TextUtils.isEmpty(mParam1)) {
            tv_content.setText("我是一个测试用的Fragment, 我创建的时候没有传进来Bundle, 所以显示这个内容.");
        } else {
            tv_content.setText(mParam1);
        }

//
//        // 同步请求 = 同步框架 + 子线程 + Handler线程调度
//        // 异步请求 = 异步框架
//
//		/* http://www.oschina.net/action/api/news_list?pageIndex=0&catalog=1&pageSize=20 */
//        OkHttpUtils
//                .get()
//                .url("http://www.oschina.net/action/api/news_list")
//                .id(101)
//                .addParams("pageIndex", "0")
//                .addParams("catalog", "1")
//                .addParams("pageSize", "20")
//                .build()
//                .execute(callback);
//
//        // http://www.oschina.net/action/api/news_detail?id=64198
//        OkHttpUtils
//                .get()
//                .url("http://www.oschina.net/action/api/news_detail")
//                .id(102)
//                .addParams("id", "64198")
//                .build()
//                .execute(callback);

    }

    StringCallback callback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int i) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "请求失败!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResponse(String s, int i) {
            Toast.makeText(getActivity(), "请求成功!", Toast.LENGTH_SHORT).show();
            System.out.println(s);
//			tv_content.setText(s);
            if (i == 101) {
                NewsList newsList = XmlUtils.toBean(NewsList.class, s.getBytes());
                List<News> list = newsList.getList();

//				tv_content.append("list.size: " + list.size() + "\n");
//				tv_content.append("list.item: " + list.get(0).getTitle());
            } else if (i == 102) {
                NewsDetail newsDetail = XmlUtils.toBean(NewsDetail.class, s.getBytes());
                String body = newsDetail.getNews().getBody();
//				tv_content.append(body);

                // 加载一个网址
//				wv.loadUrl("http://m.oschina.net");
                // 加载本地文件
//				wv.loadUrl("file:///android_asset/index.html");

                // 加载html文本.html,css, js
                wv.loadDataWithBaseURL(null, body, "text/html", "UTF-8", null);
            }


        }
    };

}
