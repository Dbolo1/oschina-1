package com.itheima.richtext;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv_rich = (TextView) findViewById(R.id.tv_rich);

        String text = r("老宋") + ", " + g("老王") + ", " +
                "老马, 荆棘之花, 亚索, 亚瑟, 提莫, 安妮等"+r("32人")+"觉得很赞!";
        Spanned spanned = Html.fromHtml(text); // 实现了CharSequence接口
        tv_rich.setText(spanned);

        // -------------------------------------------------

        String str = "01234567"; // 把 234 替换成 右哼哼
        TextView tv_rich_image = (TextView) findViewById(R.id.tv_rich_image);

        // 1. 构建Spannable, 才可以替换自定义内容
        SpannableString spannable = new SpannableString(str);

        // 2. 获取Drawable对象
        Drawable drawable = getResources().getDrawable(R.drawable.smiley_46);
            // 设置宽高(必须设置)
            drawable.setBounds(0, 0, 50, 50);
        // 3. 把Drawable封装到ImageSpan中
        ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        // 4. 将自定义Spannable中的指定开始,结束位置替换成ImageSpan
        spannable.setSpan(imageSpan, 2, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tv_rich_image.setText(spannable);

        // ----------------------------------------------------
        EditText tv_rich_span = (EditText) findViewById(R.id.tv_rich_span);
        String strs = "_前包后不包_后包前不包_前后都不包_前后都包括_";

        // 当有新的内容插入时, 样式规则
        SpannableString ss = new SpannableString(strs);
        ss.setSpan(new ForegroundColorSpan(Color.RED), 1, 6, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.GREEN), 7, 12, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.BLUE), 13, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.MAGENTA), 19, 24, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        tv_rich_span.setText(ss);

//        tv_rich_span.setSelection(2, 6);

        // -----------------------------------------------------链接
        String s = "点击百度,点击拨号,@墨子,aaa";
        TextView tv_rich_link = (TextView) findViewById(R.id.tv_rich_link);
        // 设置TextView可以点击
        tv_rich_link.setMovementMethod(LinkMovementMethod.getInstance());

        SpannableString ssLink = new SpannableString(s);
        ssLink.setSpan(new URLSpan("http://m.baidu.com"), 2, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssLink.setSpan(new URLSpan("tel:112"), 7, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssLink.setSpan(new MyURLSpan("http://m.oschina.com/user/123456"), 10, 13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tv_rich_link.setText(ssLink);

    }

    // 自定义链接
    class MyURLSpan extends URLSpan {

        public MyURLSpan(String url) {
            super(url);
        }

        @Override
        public void onClick(View widget) {
//            super.onClick(widget);
            String url = getURL();
            Toast.makeText(getApplicationContext(), "路径被点击: " + url, Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    private String g(String str) {
        return String.format("<font color='#00FF00'>%s</font>", str);
    }

    @NonNull
    private String r(String str) {
        return String.format("<font color='red'>%s</font>", str);
    }
}
