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

import com.sobey.common.entity.Images;
import com.sobey.common.view.BannerView;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.LoadingViewUtil;
import com.sobey.tvcust.ui.activity.SignActivity;
import com.sobey.tvcust.ui.activity.WebActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/2 0002.
 */
public class HomeInfoFragment extends BaseFragment implements View.OnClickListener,BannerView.OnBannerClickListener{

    private int position;
    private View rootView;

    private ViewPager viewPager;
    private MyPagerAdapter pagerAdapter;
    private TabLayout tab;
    private BannerView banner;

    private View btn_go_scan;
    private View btn_go_sign;

    private List<Images> images = new ArrayList<>();

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
        banner = (BannerView) getView().findViewById(R.id.banner);
    }

    private void initData() {
        images.clear();
        images.add(new Images(1,"夏季衬衫，清凉一夏","http://img2.imgtn.bdimg.com/it/u=2401368128,869327646&fm=21&gp=0.jpg"));
        images.add(new Images(2,"男子怒打妻儿，竟然只为了买一件衣服","http://img1.imgtn.bdimg.com/it/u=839795904,770645941&fm=21&gp=0.jpg"));
        images.add(new Images(3,"冠希复出，陈妍希表示呵呵","http://pic44.nipic.com/20140726/6205649_111852997000_2.jpg"));
        images.add(new Images(4,"iphon7预览版发售，你还在等什么","http://img4.imgtn.bdimg.com/it/u=3831361042,2579496760&fm=21&gp=0.jpg"));
        images.add(new Images(5,"马云：成功不只是嘴上说说","http://img0.imgtn.bdimg.com/it/u=1415714570,832901974&fm=21&gp=0.jpg"));
    }

    private void initCtrl() {
        btn_go_scan.setOnClickListener(this);
        btn_go_sign.setOnClickListener(this);
        pagerAdapter = new MyPagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tab.setupWithViewPager(viewPager);

        banner.showTitle(false);
        banner.setDatas(images);
        banner.setOnBannerClickListener(this);
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

    @Override
    public void onBannerClick(int position) {
        Intent intent = new Intent(getActivity(), WebActivity.class);
        intent.putExtra("title","资讯");
        intent.putExtra("url","http://cn.bing.com");//https://github.com    //http://cn.bing.com
        getActivity().startActivity(intent);
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
