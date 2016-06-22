package com.sobey.tvcust.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.sobey.common.entity.Images;
import com.sobey.common.view.BannerView;
import com.sobey.common.view.FullyListView;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.LoadingViewUtil;
import com.sobey.tvcust.entity.TestEntity;
import com.sobey.tvcust.ui.activity.DeviceDetailActivity;
import com.sobey.tvcust.ui.activity.DeviceListActivity;
import com.sobey.tvcust.ui.activity.ReqfixActicity;
import com.sobey.tvcust.ui.activity.WebActivity;
import com.sobey.tvcust.ui.adapter.ListAdapterHomeQW;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/2 0002.
 */
public class HomeQwFragment extends BaseFragment implements View.OnClickListener,AdapterView.OnItemClickListener,BannerView.OnBannerClickListener {

    private BannerView banner;
    private TextView text_reqfix;
    private TextView text_more;
    private ListView listView_full;
    private ListAdapterHomeQW adapter;
    private ViewGroup showingroup;
//    private Dialog loadingDialog;

    private int position;
    private View rootView;
    private List<Images> images = new ArrayList<>();
    private List<TestEntity> results = new ArrayList<>();

    public static Fragment newInstance(int position) {
        HomeQwFragment f = new HomeQwFragment();
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
        rootView = inflater.inflate(R.layout.fragment_home_qw,container,false);
        Log.e("liao","onCreateView");
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toolbar toolbar = (Toolbar) getView().findViewById(R.id.toolbar);
        toolbar.setTitle("全网");

        initBase();
        initView();
        initData();
        //initCtrl();

        banner.setFocusable(true);
        banner.setFocusableInTouchMode(true);
        banner.requestFocus();
    }

    @Override
    public void onDestroy() {
        Log.e("liao","onDestroy");
        super.onDestroy();
//        loadingDialog.dismiss();
    }

    private void initBase() {
//        loadingDialog = new LoadingDialog(getActivity());
    }

    private void initView() {
        banner = (BannerView) getView().findViewById(R.id.banner_home_qw);
        text_reqfix = (TextView) getView().findViewById(R.id.text_home_qw_reqfix);
        text_more = (TextView) getView().findViewById(R.id.text_home_qw_more);
        listView_full = (FullyListView) getView().findViewById(R.id.listfull_home_qw);
        showingroup = (ViewGroup) getView().findViewById(R.id.showingroup);
    }

    private void initData() {
//        loadingDialog.show();
        final View showin = LoadingViewUtil.showin(showingroup,R.layout.layout_loading);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //加载成功
                images.add(new Images(1,"夏季衬衫，清凉一夏","http://img2.imgtn.bdimg.com/it/u=2401368128,869327646&fm=21&gp=0.jpg"));
                images.add(new Images(2,"男子怒打妻儿，竟然只为了买一件衣服","http://img1.imgtn.bdimg.com/it/u=839795904,770645941&fm=21&gp=0.jpg"));
                images.add(new Images(3,"冠希复出，陈妍希表示呵呵","http://pic44.nipic.com/20140726/6205649_111852997000_2.jpg"));
                images.add(new Images(4,"iphon7预览版发售，你还在等什么","http://img4.imgtn.bdimg.com/it/u=3831361042,2579496760&fm=21&gp=0.jpg"));
                images.add(new Images(5,"马云：成功不只是嘴上说说","http://img0.imgtn.bdimg.com/it/u=1415714570,832901974&fm=21&gp=0.jpg"));

                results.add(new TestEntity());
                results.add(new TestEntity());
                results.add(new TestEntity());

                initCtrl();
                //freshCtrl();
                LoadingViewUtil.showout(showingroup,showin);

                //加载失败
//                LoadingViewUtil.showin(showingroup,R.layout.layout_lack,showin,new View.OnClickListener(){
//                    @Override
//                    public void onClick(View v) {
//                        initData();
//                    }
//                });

//                loadingDialog.hide();
            }
        },1000);
    }

    private void initCtrl() {
        banner.showTitle(false);
        banner.setDatas(images);
        banner.setOnBannerClickListener(this);
        text_reqfix.setOnClickListener(this);
        text_more.setOnClickListener(this);

        adapter = new ListAdapterHomeQW(getActivity(),R.layout.item_list_home_qw,results);
        listView_full.setAdapter(adapter);
        listView_full.setOnItemClickListener(this);
    }

    private void freshCtrl(){
        adapter.notifyDataSetChanged();
        banner.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.text_home_qw_reqfix:
                intent.setClass(getActivity(), ReqfixActicity.class);
                startActivity(intent);
                break;
            case R.id.text_home_qw_more:
                intent.setClass(getActivity(), DeviceListActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), DeviceDetailActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBannerClick(int position) {
        Intent intent = new Intent(getActivity(),WebActivity.class);
        intent.putExtra("title","资讯");
        intent.putExtra("url","http://cn.bing.com");//https://github.com    //http://cn.bing.com
        startActivity(intent);
    }
}
