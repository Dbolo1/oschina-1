package com.itheima.baseviewpagerfragment.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.baseviewpagerfragment.R;
import com.itheima.baseviewpagerfragment.base.BaseHolder;
import com.itheima.baseviewpagerfragment.domain.Blog;

import butterknife.BindView;

/**
 * 类名:      BlogHolder
 * 创建者:    PoplarTang
 * 创建时间:  2016/9/17.
 * 描述：     TODO
 */
public class BlogHolder extends BaseHolder<Blog> {

    @BindView(R.id.iv_image)
    ImageView mIvImage;
    @BindView(R.id.tv_content)
    TextView mTvContent;

    @Override
    public View onCreateViewHolder(Context context) {
        return View.inflate(context, R.layout.item_list_news, null);
    }

    @Override
    public void bindView(Blog item) {
        mIvImage.setImageResource(R.mipmap.ic_launcher);
        mTvContent.setText(item.getTitle());
    }
}
