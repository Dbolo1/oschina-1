package com.itheima.baseviewpagerfragment.api;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

/**
 * 类名:      NetApi
 * 创建者:    PoplarTang
 * 创建时间:  2016/9/17.
 * 描述：     负责网络请求的封装
 */
public class NetApi {
    /**
     * 类名:      NetApi
     * 创建者:    poplar
     * 创建时间:  2016/9/17
     * 描述：     请求新闻列表数据
     *
     // http://www.oschina.net/action/api/news_list?pageIndex=0&catalog=1&pageSize=20
     */
    public static void getNewsList(int pageIndex, int catalog, int pageSize, StringCallback callback) {
        OkHttpUtils
                .get()
                .url("http://www.oschina.net/action/api/news_list")
                .addParams("pageIndex", pageIndex + "")
                .addParams("catalog", catalog + "")
                .addParams("pageSize", pageSize + "")
                .build()
                .execute(callback);
    }
    // http://www.oschina.net/action/api/blog_list?pageIndex=0&pageSize=20&type=latest
    public static void getBlogList(int pageIndex, int pageSize, String type, StringCallback callback) {
        OkHttpUtils
                .get()
                .url("http://www.oschina.net/action/api/blog_list")
                .addParams("pageIndex", pageIndex + "")
                .addParams("pageSize", pageSize + "")
                .addParams("type", type)
                .build()
                .execute(callback);
    }
}
