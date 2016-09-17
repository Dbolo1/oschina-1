package com.itheima.baseviewpagerfragment.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.baseviewpagerfragment.R;
import com.itheima.baseviewpagerfragment.domain.News;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 类名:      BaseListAdapter
 * 创建者:    PoplarTang
 * 创建时间:  2016/9/17.
 * 描述：     TODO
 */
public class NewsAdapter_ extends BaseAdapter {


    private List<News> datas = new ArrayList<>();

    // 上拉加载更多, 添加数据
    public void addDatas(List<News> datas) {
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    // 下拉刷新, 初始化数据
    public void setDatas(List<News> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public News getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    // parent即为ListView
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = View.inflate(parent.getContext(), R.layout.item_list_news, null);
            holder = new ViewHolder();
            holder.initViewHolder(view);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        News item = getItem(position);

        holder.mIvImage.setImageResource(R.mipmap.ic_launcher);
        holder.mTvContent.setText(item.getTitle());
        return view;
    }
    static class ViewHolder {
        @BindView(R.id.iv_image)
        ImageView mIvImage;
        @BindView(R.id.tv_content)
        TextView mTvContent;

        public void initViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }
    }
}
