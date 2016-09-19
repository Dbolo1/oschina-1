package com.itheima.oschina.fragment;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import android.view.View;
import android.widget.AdapterView;

import com.itheima.oschina.adapter.NewsAdapter;
import com.itheima.oschina.api.remote.OSChinaApi;
import com.itheima.oschina.base.BaseListFragment;
import com.itheima.oschina.base.ListBaseAdapter;
import com.itheima.oschina.bean.News;
import com.itheima.oschina.bean.NewsList;
import com.itheima.oschina.interf.OnTabReselectListener;
import com.itheima.oschina.ui.empty.EmptyLayout;
import com.itheima.oschina.util.UIHelper;
import com.itheima.oschina.util.XmlUtils;

/**
 * 新闻资讯
 * 
 * 通过 parseList 解析数据
 * 
 * 
 * 
 */
public class NewsFragment extends BaseListFragment<News> implements
        OnTabReselectListener {

    protected static final String TAG = NewsFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "newslist_";

    @Override
    protected NewsAdapter getListAdapter() {
        return new NewsAdapter();
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + mCatalog;
    }

    @Override
    protected NewsList parseList(InputStream is) throws Exception {
        NewsList list = null;
        try {
            list = XmlUtils.toBean(NewsList.class, is);
        } catch (NullPointerException e) {
            list = new NewsList();
        }
        return list;
    }

    @Override
    protected NewsList readList(Serializable seri) {
        return ((NewsList) seri);
    }

    @Override
    protected void sendRequestData() {
/*    	// 方式一: 每次new AsyncHttpClient, 消耗大
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get("http://192.168.18.93:8080/oschina/news_list/page0.xml", new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, org.apache.http.Header[] arg1,
					byte[] arg2, Throwable arg3) {
			}

			@Override
			public void onSuccess(int arg0, org.apache.http.Header[] arg1,
					byte[] arg2) {
			}
		});
        
        // 方式二: 直接用封装了AsyncHttpClient的ApiHttpClient静态方法, 封装路径和拼接参数, 麻烦
        ApiHttpClient.getLocal("oschina/news_list/page2.xml", mHandler);*/
    	
    	// 方式三: (推荐)主线程 mCatalog = 1 资讯, mCatalog = 4 热点
        OSChinaApi.getNewsList(mCatalog, mCurrentPage, mHandler);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        News news = mAdapter.getItem(position);
        if (news != null) {
            UIHelper.showNewsRedirect(view.getContext(), news);

            // 放入已读列表
            saveToReadedList(view, NewsList.PREF_READED_NEWS_LIST, news.getId()
                    + "");
        }
    }

    @Override
    protected void executeOnLoadDataSuccess(List<News> data) {
        if (mCatalog == NewsList.CATALOG_WEEK
                || mCatalog == NewsList.CATALOG_MONTH) {
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            if (mState == STATE_REFRESH)
                mAdapter.clear();
            mAdapter.addData(data);
            mState = STATE_NOMORE;
            mAdapter.setState(ListBaseAdapter.STATE_NO_MORE);
            return;
        }
        super.executeOnLoadDataSuccess(data);
    }

    @Override
    public void onTabReselect() {
        onRefresh();
    }

    @Override
    protected long getAutoRefreshTime() {
        // 最新资讯两小时刷新一次
        if (mCatalog == NewsList.CATALOG_ALL) {

            return 2 * 60 * 60;
        }
        return super.getAutoRefreshTime();
    }
}
