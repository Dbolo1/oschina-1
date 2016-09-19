package com.itheima.datarequest;

import android.app.Application;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * 类名:      MyApp
 * 创建者:    PoplarTang
 * 创建时间:  2016/9/19.
 * 描述：     TODO
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // 内存cookie缓存器
//        CookieJarImpl cookieJarMemory = new CookieJarImpl(new MemoryCookieStore());

        // 文件cookie存储器
        CookieJarImpl cookieJarFile = new CookieJarImpl(new PersistentCookieStore(this));

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                .readTimeout(3000, TimeUnit.MILLISECONDS)
//                .cookieJar(CookieJar.NO_COOKIES)
                .cookieJar(cookieJarFile)
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }
}
