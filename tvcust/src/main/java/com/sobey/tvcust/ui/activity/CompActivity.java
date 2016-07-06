package com.sobey.tvcust.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.sobey.tvcust.R;
import com.sobey.tvcust.ui.fragment.CompBranchFragment;
import com.sobey.tvcust.ui.fragment.CompOfficeFragment;
import com.sobey.tvcust.ui.fragment.CompTVStationFragment;
import com.sobey.tvcust.ui.fragment.RegistDetailFragment;

public class CompActivity extends BaseAppCompatActicity {

    private ViewPager viewPager;
    private MyPagerAdapter pagerAdapter;
    private String type = RegistDetailFragment.TYPE_GROUP_USER;
    private String[] title = new String[]{"公司","办事处", "电视台"};

    public String getType() {
        return type;
    }

    public void next(){
        int position = viewPager.getCurrentItem();
        if (position==0){
            setPage(1);
        }else if(position==1){
            setPage(2);
        }
    }
    public void last(){
        int position = viewPager.getCurrentItem();
        if (position==2){
            setPage(1);
        }else if(position==1){
            if (RegistDetailFragment.TYPE_USER.equals(type)) {
                finish();
            }else {
                setPage(0);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title[0]);
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

        //如果是用户则直接到办事处页面
        if (RegistDetailFragment.TYPE_USER.equals(type)) {
            setPage(1);
        }
    }

    private void setPage(int pos){
        viewPager.setCurrentItem(pos);
        getSupportActionBar().setTitle(title[pos]);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

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
            if (position == 0){
                return CompBranchFragment.newInstance(position);
            } else if (position == 1) {
                return CompOfficeFragment.newInstance(position);
            } else if (position == 2) {
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
                    last();
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
                last();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
