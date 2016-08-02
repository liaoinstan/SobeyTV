package com.sobey.tvcust.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.sobey.common.view.DotView;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.entity.User;
import com.sobey.tvcust.ui.fragment.StartUpFragment;

public class StartUpActivity extends BaseAppCompatActivity {

    private ViewPager viewPager;
    private DotView dotView;

    private int[] srcs = new int[]{R.drawable.startup1, R.drawable.startup2, R.drawable.startup3, R.drawable.startup4, R.drawable.startup5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        AppData.App.saveStartUp(true);

        initView();
        initData();
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.startup_viewpager);
        dotView = (DotView) findViewById(R.id.startup_dot);
    }

    private void initData() {
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        dotView.setViewPager(viewPager);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "title";
        }

        @Override
        public int getCount() {
            return srcs.length;
        }

        @Override
        public Fragment getItem(int position) {
            return StartUpFragment.newInstance(srcs[position], position == srcs.length - 1);
        }
    }

    public void onGo(View v) {
        Intent intent = new Intent();
        User user = AppData.App.getUser();
        if (user != null) {
            //去首页
            intent.setClass(this, HomeActivity.class);
        } else {
            //去登录页
            intent.setClass(this, LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
