package com.sobey.tvcust.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sobey.common.entity.Images;
import com.sobey.common.utils.StrUtils;
import com.sobey.common.view.BannerView;
import com.sobey.tvcust.R;
import com.sobey.tvcust.ui.activity.ScannerActivity;
import com.sobey.tvcust.ui.activity.SignActivity;
import com.sobey.tvcust.ui.activity.WebActivity;
import com.sobey.common.utils.PermissionsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/2 0002.
 */
public class HomeInfoFragment extends BaseFragment implements View.OnClickListener, BannerView.OnBannerClickListener {

    private int position;
    private View rootView;

    private ViewPager viewPager;
    private MyPagerAdapter pagerAdapter;
    private TabLayout tab;

    private View btn_go_scan;
    private View btn_go_sign;

    private List<Images> images = new ArrayList<>();
    private static final int CODE_ZQR = 0xff10;// 结果

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
        images.clear();
        images.add(new Images(1, "夏季衬衫，清凉一夏", "http://img2.imgtn.bdimg.com/it/u=2401368128,869327646&fm=21&gp=0.jpg"));
        images.add(new Images(2, "男子怒打妻儿，竟然只为了买一件衣服", "http://img1.imgtn.bdimg.com/it/u=839795904,770645941&fm=21&gp=0.jpg"));
        images.add(new Images(3, "冠希复出，陈妍希表示呵呵", "http://pic44.nipic.com/20140726/6205649_111852997000_2.jpg"));
        images.add(new Images(4, "iphon7预览版发售，你还在等什么", "http://img4.imgtn.bdimg.com/it/u=3831361042,2579496760&fm=21&gp=0.jpg"));
        images.add(new Images(5, "马云：成功不只是嘴上说说", "http://img0.imgtn.bdimg.com/it/u=1415714570,832901974&fm=21&gp=0.jpg"));
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
        switch (v.getId()) {
            case R.id.btn_go_scan:
                if (PermissionsUtil.requsetScan(getActivity(),getView().findViewById(R.id.coordinator))) {
                    intent.setClass(getActivity(), ScannerActivity.class);
                    startActivityForResult(intent, CODE_ZQR);
                }
//                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
//                    startScanner();
//                }
                break;
            case R.id.btn_go_sign:
                intent.setClass(getActivity(), SignActivity.class);
                startActivity(intent);
                break;
        }
    }

    @RequiresPermission(android.Manifest.permission.CAMERA)
    private void startScanner() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), ScannerActivity.class);
        startActivityForResult(intent, CODE_ZQR);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CODE_ZQR:
                if (resultCode == Activity.RESULT_OK) {
                    String result = data.getStringExtra(ScannerActivity.KEY_RESULT);
                    Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                    if (StrUtils.isUrl(result)) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(result));
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), "该二维码无效", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    public void onBannerClick(int position) {
        Intent intent = new Intent(getActivity(), WebActivity.class);
        intent.putExtra("title", "资讯");
        intent.putExtra("url", "http://cn.bing.com");//https://github.com    //http://cn.bing.com
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
