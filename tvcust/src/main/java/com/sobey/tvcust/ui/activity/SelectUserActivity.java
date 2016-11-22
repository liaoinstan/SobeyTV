package com.sobey.tvcust.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.sobey.tvcust.R;
import com.sobey.tvcust.interfaces.ActivityGo;
import com.sobey.tvcust.ui.fragment.CompOfficeFragment;
import com.sobey.tvcust.ui.fragment.CompTVStationFragment;
import com.sobey.tvcust.ui.fragment.SelectUserFragment;

public class SelectUserActivity extends BaseAppCompatActivity implements ActivityGo{


    private ViewPager viewPager;
    private MyPagerAdapter pagerAdapter;
    private String type = "xxx";
    private String[] title = new String[]{"地区", "电视台", "选择用户"};

    @Override
    public String[] getTitles() {
        return title;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void next() {
        int position = viewPager.getCurrentItem();
        if (position == 0) {
            setPage(1);
        } else if (position == 1) {
            setPage(2);
        }
    }

    @Override
    public void last() {
        int position = viewPager.getCurrentItem();
        if (position == 2) {
            setPage(1);
        } else if (position == 1) {
            setPage(0);
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

    private void setPage(int pos) {
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
            if (position == 0) {
                return CompOfficeFragment.newInstance(position);
            } else if (position == 1) {
                return CompTVStationFragment.newInstance(position);
            } else if (position == 2) {
                return SelectUserFragment.newInstance(position);
            }
            return null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                int position = viewPager.getCurrentItem();
                if (position == 0) {
                    finish();
                } else {
                    last();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (viewPager.getCurrentItem() == 0) {
                finish();
                return true;
            } else {
                last();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
