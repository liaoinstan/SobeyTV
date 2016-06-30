package com.sobey.tvcust.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.sobey.common.view.bundle.BundleEntity;
import com.sobey.tvcust.R;
import com.sobey.tvcust.ui.fragment.CompOfficeFragment;
import com.sobey.tvcust.ui.fragment.CompTVStationFragment;
import com.sobey.tvcust.ui.fragment.LoginFragment;
import com.sobey.tvcust.ui.fragment.RegistFragment;

public class CompActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MyPagerAdapter pagerAdapter;
    private String type = "";

    public String getType() {
        return type;
    }

    public void go(){
        int position = viewPager.getCurrentItem();
        if (position==0){
            viewPager.setCurrentItem(1);
            getSupportActionBar().setTitle("电视台");
        }else {
            viewPager.setCurrentItem(0);
            getSupportActionBar().setTitle("办事处");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("办事处");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initBase();
        initView();
        initData();
        initCtrl();
    }

    private void initBase() {
        Intent intent = getIntent();
        if (intent.hasExtra("type")) {
            type = intent.getStringExtra("type");
        }
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.pager_comp);
    }

    private void initData() {
    }

    private void initCtrl() {
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        String[] title = new String[]{"办事处", "电视台"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }

        @Override
        public int getCount() {
            return title.length;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return CompOfficeFragment.newInstance(position);
            } else if (position == 1) {
                return CompTVStationFragment.newInstance(position);
            }
            return null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                int position = viewPager.getCurrentItem();
                if (position==0){
                    finish();
                }else {
                    go();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (viewPager.getCurrentItem()==0) {
                finish();
                return true;
            }else {
                viewPager.setCurrentItem(0);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
