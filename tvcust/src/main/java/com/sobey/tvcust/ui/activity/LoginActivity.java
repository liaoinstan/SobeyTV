package com.sobey.tvcust.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sobey.tvcust.R;
import com.sobey.tvcust.ui.fragment.LoginFragment;
import com.sobey.tvcust.ui.fragment.RegistFragment;

public class LoginActivity extends BaseAppCompatActicity {

    private ViewPager viewPager;
    private MyPagerAdapter pagerAdapter;
    private String phone;

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initData();
        initCtrl();
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.pager_login);
    }

    private void initData() {
    }

    private void initCtrl() {
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        String[] title = new String[]{"登录", "注册"};

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
                LoginFragment fragment = (LoginFragment) LoginFragment.newInstance(position);
                fragment.setFatherPager(viewPager);
                return fragment;
            } else if (position == 1) {
                RegistFragment fragment = (RegistFragment) RegistFragment.newInstance(position);
                fragment.setFatherPager(viewPager);
                return fragment;
            }
            return null;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }
    }

    private long exitTime;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (viewPager.getCurrentItem()==0) {
                //双击退出
                if ((System.currentTimeMillis() - exitTime) > 2000){
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                } else{
                    super.finish();
                }
                return true;
            }else {
                viewPager.setCurrentItem(0);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
