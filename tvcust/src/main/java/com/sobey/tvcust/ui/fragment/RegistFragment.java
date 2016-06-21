package com.sobey.tvcust.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sobey.tvcust.R;

/**
 * Created by Administrator on 2016/6/2 0002.
 */
public class RegistFragment extends BaseFragment implements View.OnClickListener{

    private int position;
    private View rootView;

    private ViewPager fatherpager;
    private ViewPager viewPager;
    private MyPagerAdapter pagerAdapter;

    private View btn_back;


    public void setFatherPager(ViewPager fatherpager){
        this.fatherpager = fatherpager;
    }

    public static Fragment newInstance(int position) {
        RegistFragment f = new RegistFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.position = getArguments().getInt("position");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_regist, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initBase();
        initView();
        initData();
        initCtrl();
    }

    private void initBase() {

    }

    private void initView() {
        viewPager = (ViewPager) getView().findViewById(R.id.pager_regist);
        btn_back = getView().findViewById(R.id.btn_back);
    }

    private void initData() {
    }

    private void initCtrl() {
        pagerAdapter = new MyPagerAdapter(getFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        btn_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                int currentItem = viewPager.getCurrentItem();
                if (currentItem == 0){
                    fatherpager.setCurrentItem(0);
                }else if (currentItem == 1){
                    viewPager.setCurrentItem(0);
                }
                break;
        }
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        String[] title = new String[]{"验证手机","注册"};

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
            if (position==0) {
                RegistPhoneFragment fragment = (RegistPhoneFragment) RegistPhoneFragment.newInstance(position);
                fragment.setFatherpager(viewPager);
                return fragment;
            }else if(position==1){
                RegistDetailFragment fragment = (RegistDetailFragment) RegistDetailFragment.newInstance(position);
                fragment.setFatherPager(viewPager);
                fragment.setSufatherPager(fatherpager);
                return fragment;
            }
            return null;
        }
    }
}
