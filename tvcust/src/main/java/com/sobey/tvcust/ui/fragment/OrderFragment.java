package com.sobey.tvcust.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.LoadingViewUtil;
import com.sobey.tvcust.entity.TestEntity;
import com.sobey.tvcust.ui.activity.HomeActivity;
import com.sobey.tvcust.ui.activity.OrderDetailActivity;
import com.sobey.tvcust.ui.activity.ReqfixActicity;
import com.sobey.tvcust.ui.adapter.OnRecycleItemClickListener;
import com.sobey.tvcust.ui.adapter.RecycleAdapterMsg;
import com.sobey.tvcust.ui.adapter.RecycleAdapterOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/2 0002.
 */
public class OrderFragment extends BaseFragment implements OnRecycleItemClickListener,View.OnClickListener{

    private int position;
    private View rootView;
    private ViewGroup showingroup;
    private View showin;

    private TabLayout tab;
    private SpringView springView;
    private RecyclerView recyclerView;
    private RecycleAdapterOrder adapter;
    private View btn_go_reqfix;

    private List<TestEntity> results = new ArrayList<>();

    public static Fragment newInstance(int position) {
        OrderFragment f = new OrderFragment();
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
        rootView = inflater.inflate(R.layout.fragment_order, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toolbar toolbar = (Toolbar) getView().findViewById(R.id.toolbar);
        toolbar.setTitle("订单");
        initBase();
        initView();
        initData();
        initCtrl();
    }

    private void initBase() {
    }

    private void initView() {
        showingroup = (ViewGroup) getView().findViewById(R.id.showingroup);
        tab = (TabLayout) getView().findViewById(R.id.tab_order);
        recyclerView = (RecyclerView) getView().findViewById(R.id.recycle_order);
        springView = (SpringView) getView().findViewById(R.id.spring_msg);
        btn_go_reqfix = getView().findViewById(R.id.btn_go_reqfix);
    }

    private void initCtrl() {
        btn_go_reqfix.setOnClickListener(this);
        TabLayout.Tab tab1 = tab.newTab().setText("全部订单");
        tab.addTab(tab1);
        TabLayout.Tab tab2 = tab.newTab().setText("进行中");
        tab.addTab(tab2);
        TabLayout.Tab tab3 = tab.newTab().setText("已完成");
        tab.addTab(tab3);

        adapter = new RecycleAdapterOrder(getActivity(),R.layout.item_recycle_order,results);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

        springView.setHeader(new AliHeader(getActivity(),false));
        springView.setFooter(new AliFooter(getActivity(),false));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        springView.onFinishFreshAndLoad();
                    }
                }, 2000);
            }

            @Override
            public void onLoadmore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        results.add(new TestEntity());
                        results.add(new TestEntity());
                        freshCtrl();
                        springView.onFinishFreshAndLoad();
                    }
                }, 2000);
            }
        });
    }

    private void freshCtrl(){
        adapter.notifyDataSetChanged();
    }

    private void initData() {
        showin = LoadingViewUtil.showin(showingroup,R.layout.layout_loading);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                results.add(new TestEntity());
                results.add(new TestEntity());
                results.add(new TestEntity());
                results.add(new TestEntity());
                results.add(new TestEntity());
                results.add(new TestEntity());
                results.add(new TestEntity());
                //加载成功
                freshCtrl();
                LoadingViewUtil.showout(showingroup,showin);

                //加载失败
//                LoadingViewUtil.showin(showingroup,R.layout.layout_lack,showin,new View.OnClickListener(){
//                    @Override
//                    public void onClick(View v) {
//                        initData();
//                    }
//                });
            }
        }, 1000);
    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder viewHolder) {
        Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
        getActivity().startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.btn_go_reqfix:
                intent.setClass(getActivity(), ReqfixActicity.class);
                startActivity(intent);
                break;
        }
    }
}
