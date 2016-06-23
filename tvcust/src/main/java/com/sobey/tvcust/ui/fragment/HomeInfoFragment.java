package com.sobey.tvcust.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sobey.tvcust.R;
import com.sobey.tvcust.common.LoadingViewUtil;
import com.sobey.tvcust.ui.activity.SignActivity;

/**
 * Created by Administrator on 2016/6/2 0002.
 */
public class HomeInfoFragment extends BaseFragment implements View.OnClickListener{

    private int position;
    private View rootView;

    private ViewPager viewPager;
    private MyPagerAdapter pagerAdapter;
    private TabLayout tab;

    private View btn_go_scan;
    private View btn_go_sign;

    public static Fragment newInstance(int position) {
        HomeInfoFragment f = new HomeInfoFragment();
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
        rootView = inflater.inflate(R.layout.fragment_info, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toolbar toolbar = (Toolbar) getView().findViewById(R.id.toolbar);
        toolbar.setTitle("资讯");
        initBase();
        initView();
        initData();
        initCtrl();
    }

    private void initBase() {

    }

    private void initView() {
        btn_go_scan = getView().findViewById(R.id.btn_go_scan);
        btn_go_sign = getView().findViewById(R.id.btn_go_sign);
        viewPager = (ViewPager) getView().findViewById(R.id.pager_info);
        tab = (TabLayout) getView().findViewById(R.id.tab_info);
    }

    private void initData() {
    }

    private void initCtrl() {
        btn_go_scan.setOnClickListener(this);
        btn_go_sign.setOnClickListener(this);
        pagerAdapter = new MyPagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tab.setupWithViewPager(viewPager);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.btn_go_scan:
                break;
            case R.id.btn_go_sign:
                intent.setClass(getActivity(), SignActivity.class);
                startActivity(intent);
                break;
        }
    }


    private class MyPagerAdapter extends FragmentPagerAdapter {

        String[] title = new String[]{"互动圈", "产品介绍", "行业新闻"};

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
            return InfoQuanFragment.newInstance(position);
        }
    }
}
