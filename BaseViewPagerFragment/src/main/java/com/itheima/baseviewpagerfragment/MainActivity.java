package com.itheima.baseviewpagerfragment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.itheima.baseviewpagerfragment.viewpager.NewsVPFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new NewsVPFragment())
                .commit();

    }
}
