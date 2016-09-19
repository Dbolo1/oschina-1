package com.itheima.oschina.fragment;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.ListPopupWindow;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.itheima.oschina.AppContext;
import com.itheima.oschina.R;
import com.itheima.oschina.api.ApiHttpClient;
import com.itheima.oschina.base.BaseFragment;
import com.itheima.oschina.bean.Comment;
import com.itheima.oschina.bean.Entity;
import com.itheima.oschina.bean.Result;
import com.itheima.oschina.bean.ResultBean;
import com.itheima.oschina.cache.CacheManager;
import com.itheima.oschina.ui.DetailActivity;
import com.itheima.oschina.ui.empty.EmptyLayout;
import com.itheima.oschina.util.HTMLUtil;
import com.itheima.oschina.util.TDevice;
import com.itheima.oschina.util.XmlUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

public abstract class BaseDetailFragment extends BaseFragment implements
        OnItemClickListener {

    public static final String INTENT_ACTION_COMMENT_CHANGED = "INTENT_ACTION_COMMENT_CHAGED";

    private ListPopupWindow mMenuWindow;
//    private MenuAdapter mMenuAdapter;

    protected EmptyLayout mEmptyLayout;

    public int mCommentCount = 0;

    protected WebView mWebView;

//    protected AsyncHttpResponseHandler mCommentHandler = new AsyncHttpResponseHandler() {
//
//        @Override
//        public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//            try {
//                ResultBean rsb = XmlUtils.toBean(ResultBean.class,
//                        new ByteArrayInputStream(arg2));
//                Result res = rsb.getResult();
//                if (res.OK()) {
////                    hideWaitDialog();
//                    AppContext.showToastShort(R.string.comment_publish_success);
//
//                    commentPubSuccess(rsb.getComment());
//                } else {
////                    hideWaitDialog();
//                    AppContext.showToastShort(res.getErrorMessage());
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                onFailure(arg0, arg1, arg2, e);
//            }
//        }
//
//        @Override
//        public void onFailure(int arg0, Header[] arg1, byte[] arg2,
//                Throwable arg3) {
////            hideWaitDialog();
//            AppContext.showToastShort(R.string.comment_publish_faile);
//        }
//
//        @Override
//        public void onFinish() {
////            ((DetailActivity) getActivity()).emojiFragment.hideAllKeyBoard();
//        };
//    };

    protected void recycleWebView() {
        if (mWebView != null) {
            mWebView.setVisibility(View.GONE);
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
    }

    protected void onCommentChanged(int opt, int id, int catalog,
            boolean isBlog, Comment comment) {}

    private AsyncTask<String, Void, Entity> mCacheTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mMenuAdapter = new MenuAdapter();
        setHasOptionsMenu(true);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestData(false);
    }

    protected boolean hasReportMenu() {
        return false;
    }

    @Override
    public void onDestroyView() {
        recycleWebView();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        cancelReadCache();
        recycleWebView();
        super.onDestroy();
    }



    protected String getCacheKey() {
        return null;
    }

    protected Entity parseData(InputStream is) throws Exception {
        return null;
    }

    protected Entity readData(Serializable seri) {
        return null;
    }

    protected void sendRequestData() {}

    protected void requestData(boolean refresh) {
        String key = getCacheKey();
        if (TDevice.hasInternet()
                && (!CacheManager.isExistDataCache(getActivity(), key) || refresh)) {
            sendRequestData();
        } else {
            readCacheData(key);
        }
    }

    // 刷新数据
    protected void sendRefresh() {
        sendRequestData();
    }

    private void readCacheData(String cacheKey) {
        cancelReadCache();
        mCacheTask = new CacheTask(getActivity()).execute(cacheKey);
    }

    private void cancelReadCache() {
        if (mCacheTask != null) {
            mCacheTask.cancel(true);
            mCacheTask = null;
        }
    }

    private class CacheTask extends AsyncTask<String, Void, Entity> {
        private final WeakReference<Context> mContext;

        private CacheTask(Context context) {
            mContext = new WeakReference<Context>(context);
        }

        @Override
        protected Entity doInBackground(String... params) {
            if (mContext.get() != null) {
                Serializable seri = CacheManager.readObject(mContext.get(),
                        params[0]);
                if (seri == null) {
                    return null;
                } else {
                    return readData(seri);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Entity entity) {
            super.onPostExecute(entity);
            if (entity != null) {
                executeOnLoadDataSuccess(entity);
            } else {
                executeOnLoadDataError(null);
            }
            executeOnLoadFinish();
        }
    }

    private class SaveCacheTask extends AsyncTask<Void, Void, Void> {
        private final WeakReference<Context> mContext;
        private final Serializable seri;
        private final String key;

        private SaveCacheTask(Context context, Serializable seri, String key) {
            mContext = new WeakReference<Context>(context);
            this.seri = seri;
            this.key = key;
        }

        @Override
        protected Void doInBackground(Void... params) {
            CacheManager.saveObject(mContext.get(), seri, key);
            return null;
        }
    }
    ApiHttpClient.OnHttpResultHandler mHandler = new ApiHttpClient.OnHttpResultHandler() {
        @Override
        public void onSuccess(String response) {
            try {
                Entity entity = parseData(new ByteArrayInputStream(response.getBytes()));
                if (entity != null) {
                    mEmptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                    executeOnLoadDataSuccess(entity);
                    saveCache(entity);
                } else {
                    throw new RuntimeException("load detail error");
                }
            } catch (Exception e) {
                e.printStackTrace();
                onError(e);
            }
        }

        @Override
        public void onError(Throwable e) {
            // executeOnLoadDataError(arg3.getMessage());
            readCacheData(getCacheKey());

        }
    };

//    protected AsyncHttpResponseHandler mHandler = new AsyncHttpResponseHandler() {
//
//        @Override
//        public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//            try {
//                Entity entity = parseData(new ByteArrayInputStream(arg2));
//                if (entity != null) {
//                    mEmptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
//                    executeOnLoadDataSuccess(entity);
//                    saveCache(entity);
//                } else {
//                    throw new RuntimeException("load detail error");
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                onFailure(arg0, arg1, arg2, e);
//            }
//        }
//
//        @Override
//        public void onFailure(int arg0, Header[] arg1, byte[] arg2,
//                Throwable arg3) {
//            // executeOnLoadDataError(arg3.getMessage());
//            readCacheData(getCacheKey());
//        }
//    };

    private boolean mIsFavorited;

    protected void saveCache(Entity entity) {
        new SaveCacheTask(getActivity(), entity, getCacheKey()).execute();
    }

    protected void executeOnLoadDataSuccess(Entity entity) {}

    protected void executeOnLoadDataError(String object) {
        mEmptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
        mEmptyLayout.setOnLayoutClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mState = STATE_REFRESH;
                mEmptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                requestData(true);
            }
        });
    }

    protected void executeOnLoadFinish() {}

    protected void onFavoriteChanged(boolean flag) {}

    protected int getFavoriteTargetId() {
        return -1;
    }

    protected int getFavoriteTargetType() {
        return -1;
    }

    protected String getShareUrl() {
        return "";
    }

    protected String getShareTitle() {
        return getString(R.string.share_title);
    }

    protected String getShareContent() {
        return "";
    }

    /***
     * 获取去除html标签的body
     * 
     * @param body
     * @return
     */
    protected String getFilterHtmlBody(String body) {
        if (body == null)
            return "";
        return HTMLUtil.delHTMLTag(body.trim());
    }

    protected void commentPubSuccess(Comment comment) {}

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        if (position == 0) {
            handleFavoriteOrNot();
            handleShare();
        } else if (position == 1) {
            onReportMenuClick();
        } else if (position == 2) {

        }
        if (mMenuWindow != null) {
            mMenuWindow.dismiss();
            mMenuWindow = null;
        }
    }

//    private final AsyncHttpResponseHandler mReportHandler = new TextHttpResponseHandler() {
//
//        @Override
//        public void onSuccess(int arg0, Header[] arg1, String arg2) {
//            if (TextUtils.isEmpty(arg2)) {
//                AppContext.showToastShort(R.string.tip_report_success);
//            } else {
//                AppContext.showToastShort(new String(arg2));
//            }
//        }
//
//        @Override
//        public void onFailure(int arg0, Header[] arg1, String arg2,
//                Throwable arg3) {
//            AppContext.showToastShort(R.string.tip_report_faile);
//        }
//
//        @Override
//        public void onFinish() {
////            hideWaitDialog();
//        }
//    };

    public void onReportMenuClick() {
    }

    protected String getRepotrUrl() {
        return "";
    }

    protected int getRepotrId() {
        return 0;
    }

    /**
     * 收藏
     */
    public void handleFavoriteOrNot() {
        if (!TDevice.hasInternet()) {
            AppContext.showToastShort(R.string.tip_no_internet);
            return;
        }
    }

    /**
     * 分享
     */
    public void handleShare() {
    }

    @SuppressWarnings("deprecation")
    private void shareToWeiChatCircle() {
    }

    @SuppressWarnings("deprecation")
    private void shareToWeiChat() {
        // 添加微信平台
    }

    private void shareToSinaWeibo() {
        // 设置新浪微博SSO handler
    }

    protected void notifyFavorite(boolean favorite) {
        mIsFavorited = favorite;
        FragmentActivity aty = getActivity();
        if (aty != null) {
            aty.supportInvalidateOptionsMenu();
        }
//        if (mMenuAdapter != null) {
//            mMenuAdapter.setFavorite(favorite);
//        }
//        onFavoriteChanged(favorite);
    }

//    @SuppressLint("ViewHolder")
//    private static class MenuAdapter extends BaseAdapter {
//
//        public void setFavorite(boolean favorite) {
//            notifyDataSetChanged();
//        }
//
//        @Override
//        public int getCount() {
//            return 2;
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        @SuppressLint("InflateParams")
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            convertView = LayoutInflater.from(parent.getContext()).inflate(
//                    R.layout.list_cell_popup_menu, null);
//            TextView name = (TextView) convertView.findViewById(R.id.tv_name);
//
//            int iconResId = 0;
//            // if (position == 0) {
//            // name.setText(isFavorite ? R.string.detail_menu_unfavorite
//            // : R.string.detail_menu_favorite);
//            // iconResId = isFavorite ?
//            // R.drawable.actionbar_menu_icn_unfavoirite
//            // : R.drawable.actionbar_menu_icn_favoirite;
//            // } else
//            if (position == 0) {
//                name.setText(parent.getResources().getString(
//                        R.string.detail_menu_for_share));
//                iconResId = R.drawable.abc_ic_menu_moreoverflow_normal_holo_dark;
//            } else if (position == 1) {
//                name.setText(parent.getResources().getString(
//                        R.string.detail_menu_for_report));
//                iconResId = R.drawable.abc_ic_menu_moreoverflow_normal_holo_dark;
//            }
//            Drawable drawable = AppContext.resources().getDrawable(iconResId);
//            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
//                    drawable.getMinimumHeight());
//            name.setCompoundDrawables(drawable, null, null, null);
//            return convertView;
//        }
//    }

    public abstract int getCommentCount();

    @Override
    public void onClick(View v) {}

    @Override
    public void initView(View view) {}

    public void onclickWriteComment() {}

    @Override
    public void initData() {}
}