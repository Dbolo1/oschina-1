package com.itheima.oschina.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.oschina.R;
import com.itheima.oschina.base.ListBaseAdapter;
import com.itheima.oschina.bean.Active;
import com.itheima.oschina.bean.Active.ObjectReply;
import com.itheima.oschina.bean.Tweet;
import com.itheima.oschina.emoji.InputHelper;
import com.itheima.oschina.util.ImageUtils;
import com.itheima.oschina.util.StringUtils;
import com.itheima.oschina.util.UIHelper;
import com.itheima.oschina.widget.AvatarView;
import com.itheima.oschina.widget.MyLinkMovementMethod;
import com.itheima.oschina.widget.MyURLSpan;
import com.itheima.oschina.widget.TweetTextView;

import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.bitmap.BitmapCallBack;
import org.kymjs.kjframe.bitmap.BitmapHelper;
import org.kymjs.kjframe.utils.DensityUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

//import com.itheima.oschina.ui.ImagePreviewActivity;

public class ActiveAdapter extends ListBaseAdapter {
    private final static String AT_HOST_PRE = "http://my.oschina.net";
    private final static String MAIN_HOST = "http://www.oschina.net";

    public ActiveAdapter() {
    }

    private Bitmap recordBitmap;
    private final KJBitmap kjb = new KJBitmap();
    private int rectSize;

    private void initRecordImg(Context cxt) {
        recordBitmap = BitmapFactory.decodeResource(cxt.getResources(),
                R.drawable.audio3);
        recordBitmap = ImageUtils.zoomBitmap(recordBitmap,
                DensityUtils.dip2px(cxt, 20f), DensityUtils.dip2px(cxt, 20f));
    }

    private void initImageSize(Context cxt) {
        if (cxt != null && rectSize == 0) {
            rectSize = (int) cxt.getResources().getDimension(R.dimen.space_100);
        } else {
            rectSize = 300;
        }
    }

    @Override
    @SuppressLint("InflateParams")
    protected View getRealView(int position, View convertView,
                               final ViewGroup parent) {
        ViewHolder vh = null;
        initImageSize(parent.getContext());
        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(
                    R.layout.list_cell_active, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        final Active item = (Active) mDatas.get(position);

        vh.name.setText(item.getAuthor());

        vh.action.setText(UIHelper.parseActiveAction(item.getObjectType(),
                item.getObjectCatalog(), item.getObjectTitle()));

        if (TextUtils.isEmpty(item.getMessage())) {
            vh.body.setVisibility(View.GONE);
        } else {
            // 设置TextView支持链接点击
//            vh.body.setMovementMethod(LinkMovementMethod.getInstance());
            vh.body.setMovementMethod(MyLinkMovementMethod.a());
            // 不可获取焦点
            vh.body.setFocusable(false);
            // 在非链接区域, 将触摸事件分发给父控件
            vh.body.setDispatchToParent(true);
            // 禁用长按点击事件
            vh.body.setLongClickable(false);

            Spanned span = Html.fromHtml(modifyPath(item.getMessage()));

            if (!StringUtils.isEmpty(item.getTweetattach())) {
                if (recordBitmap == null) {
                    initRecordImg(parent.getContext());
                }
                ImageSpan recordImg = new ImageSpan(parent.getContext(),
                        recordBitmap);
                SpannableString str = new SpannableString("c");
                str.setSpan(recordImg, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                vh.body.setText(str);
                span = InputHelper.displayEmoji(parent.getContext()
                        .getResources(), span);
                vh.body.append(span);
            } else {

                // 1. 内容的表情 [大兵] [大笑]
                span = InputHelper.displayEmoji(parent.getContext().getResources(), span);
                vh.body.setText(span);
            }
            // 2. 内容链接, 把<a href=""></a> 转成可点击区域
            MyURLSpan.parseLinkText(vh.body, span);
        }

        ObjectReply reply = item.getObjectReply();
        if (reply != null) {
            vh.reply.setMovementMethod(MyLinkMovementMethod.a());
            vh.reply.setFocusable(false);
            vh.reply.setDispatchToParent(true);
            vh.reply.setLongClickable(false);
            Spanned span = UIHelper.parseActiveReply(reply.objectName,
                    reply.objectBody);
            vh.reply.setText(span);//
            MyURLSpan.parseLinkText(vh.reply, span);
            vh.lyReply.setVisibility(TextView.VISIBLE);
        } else {
            vh.reply.setText("");
            vh.lyReply.setVisibility(TextView.GONE);
        }

        vh.time.setText(StringUtils.friendly_time(item.getPubDate()));

        vh.from.setVisibility(View.VISIBLE);
        switch (item.getAppClient()) {
            default:
                vh.from.setText(R.string.from_web); // 不显示
                vh.from.setVisibility(View.GONE);
                break;
            case Tweet.CLIENT_MOBILE:
                vh.from.setText(R.string.from_mobile);
                break;
            case Tweet.CLIENT_ANDROID:
                vh.from.setText(R.string.from_android);
                break;
            case Tweet.CLIENT_IPHONE:
                vh.from.setText(R.string.from_iphone);
                break;
            case Tweet.CLIENT_WINDOWS_PHONE:
                vh.from.setText(R.string.from_windows_phone);
                break;
            case Tweet.CLIENT_WECHAT:
                vh.from.setText(R.string.from_wechat);
                break;
        }

        if (item.getCommentCount() > 0) {
            vh.commentCount.setText(String.valueOf(item.getCommentCount()));
            vh.commentCount.setVisibility(View.VISIBLE);
        } else {
            vh.commentCount.setVisibility(View.GONE);
        }

        vh.avatar.setUserInfo(item.getAuthorId(), item.getAuthor());
        vh.avatar.setAvatarUrl(item.getPortrait());

        if (!TextUtils.isEmpty(item.getTweetimage())) {
            setTweetImage(parent, vh, item);
        } else {
            vh.pic.setVisibility(View.GONE);
            vh.pic.setImageBitmap(null);
        }

        return convertView;
    }

    /**
     * 动态设置图片显示样式
     *
     * @author kymjs
     */
    private void setTweetImage(final ViewGroup parent, final ViewHolder vh,
                               final Active item) {
        vh.pic.setVisibility(View.VISIBLE);

        kjb.display(vh.pic, item.getTweetimage(), R.drawable.pic_bg, rectSize,
                rectSize, new BitmapCallBack() {
                    @Override
                    public void onSuccess(Bitmap bitmap) {
                        super.onSuccess(bitmap);
                        if (bitmap != null) {
                            bitmap = BitmapHelper.scaleWithXY(bitmap, rectSize
                                    / bitmap.getHeight());
                            vh.pic.setImageBitmap(bitmap);
                        }
                    }
                });

        vh.pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ImagePreviewActivity.showImagePrivew(parent.getContext(), 0,
//                        new String[] { getOriginalUrl(item.getTweetimage()) });
            }
        });
    }

    private String modifyPath(String message) {
//        <a href="http://m.oschina.net/tweet-topic/90%8D">#【杭州】源创会第37期开始报名#</a>
        message = message.replaceAll("(<a[^>]+href=\")/([\\S]+)\"", "$1"
                + AT_HOST_PRE + "/$2\"");
        message = message.replaceAll(
                "(<a[^>]+href=\")http://m.oschina.net([\\S]+)\"",
                "$1" + MAIN_HOST + "$2\"");
        return message;
    }

    private String getOriginalUrl(String url) {
        return url.replaceAll("_thumb", "");
    }

    static class ViewHolder {
        @InjectView(R.id.tv_name)
        TextView name;
        @InjectView(R.id.tv_from)
        TextView from;
        @InjectView(R.id.tv_time)
        TextView time;
        @InjectView(R.id.tv_action)
        TextView action;
        @InjectView(R.id.tv_action_name)
        TextView actionName;
        @InjectView(R.id.tv_comment_count)
        TextView commentCount;
        // @InjectView(R.id.tv_reply_content)
        // TextView retweetCount;
        @InjectView(R.id.tv_body)
        TweetTextView body;
        @InjectView(R.id.tv_reply)
        TweetTextView reply;
        @InjectView(R.id.iv_pic)
        ImageView pic;
        @InjectView(R.id.ly_reply)
        View lyReply;
        @InjectView(R.id.iv_avatar)
        AvatarView avatar;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
