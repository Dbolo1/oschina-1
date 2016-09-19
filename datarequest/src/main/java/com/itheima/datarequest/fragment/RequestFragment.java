package com.itheima.datarequest.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.datarequest.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.cookie.CookieJarImpl;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.CookieJar;
import okhttp3.OkHttpClient;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestFragment extends Fragment {


    @BindView(R.id.tv_content)
    TextView mTvContent;

    public RequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @OnClick({
            R.id.btn_request,
            R.id.btn_request_private,
            R.id.btn_login,
            R.id.btn_logout,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_request: // 请求公共数据
                // http://www.oschina.net/action/api/news_list?pageIndex=0&catalog=1&pageSize=20
                OkHttpUtils
                        .get()
                        .url("http://www.oschina.net/action/api/news_list")
                        .addParams("pageIndex", "0")
                        .addParams("catalog", "1")
                        .addParams("pageSize", "20")
                        .id(101)
                        .build()
                        .execute(callback);

                break;
            case R.id.btn_request_private: // 请求私有数据 token
//                http://www.oschina.net/action/api/my_information?uid=993896
                OkHttpUtils
                        .get()
                        .url("http://www.oschina.net/action/api/my_information")
//                        .addHeader("Cookie", "oscid=kPPoEbNkRJUGtEi%2FAytC1PjDaPpI3WJYko4WZijVGdJ32lHA%2BOUgzFg0bb0t9QrnUF6BMx2TMs9VE%2BFwczAwqcxvZrJObLRw5X6NwrF3UI%2BwfX227coVGsZtGq7ZaqB3QLz765sgWpnJtsOwlnuVPw%3D%3D;")
                        .addParams("uid", "993896")
                        .id(102)
                        .build()
                        .execute(callback);

                break;

            case R.id.btn_login: // 登录
                OkHttpUtils
                        .post()
                        .url("http://www.oschina.net/action/api/login_validate")
                        .addParams("username", "poplar_tang@163.com")
                        .addParams("pwd", "itheima123")
                        .id(103)
                        .build()
                        .execute(callback);

                break;
            case R.id.btn_logout: // 注销
                OkHttpClient okHttpClient = OkHttpUtils.getInstance().getOkHttpClient();
                CookieJar cookieJar = okHttpClient.cookieJar();
                if(cookieJar instanceof CookieJarImpl){
                    ((CookieJarImpl) cookieJar).getCookieStore().removeAll();
                }

                break;
        }
    }
    StringCallback callback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int i) {
            Toast.makeText(getActivity(), "请求错误", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResponse(String s, int id) {
            Toast.makeText(getActivity(), "请求成功!", Toast.LENGTH_SHORT).show();
            if(id == 101){
                mTvContent.setText(s);
            } else if(id == 102){
                mTvContent.setText(s);
            }else if(id == 103){
                Toast.makeText(getActivity(), "登录完毕!", Toast.LENGTH_SHORT).show();
                mTvContent.setText(s);
            }
        }
    };
}
